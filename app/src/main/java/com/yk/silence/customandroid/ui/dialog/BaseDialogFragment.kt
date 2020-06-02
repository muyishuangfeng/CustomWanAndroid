package com.yk.silence.customandroid.ui.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.yk.silence.customandroid.impl.OnDialogCancelListener
import com.yk.silence.customandroid.impl.OnCallDialog


class BaseDialogFragment : DialogFragment() {

    //监听弹出窗是否被取消
    private var mCancelListener: OnDialogCancelListener? = null
    //回调获得需要显示的dialog
    private var mOnCallDialog: OnCallDialog? = null

    companion object {

        fun newInstance(call: OnCallDialog?, cancelable: Boolean): BaseDialogFragment? {
            return newInstance(
                call,
                cancelable,
                null
            )
        }

        fun newInstance(
            call: OnCallDialog?, cancelable: Boolean,
            cancelListener: OnDialogCancelListener?
        ): BaseDialogFragment? {
            val instance =
                BaseDialogFragment()
            instance.isCancelable = cancelable
            instance.mCancelListener = cancelListener
            instance.mOnCallDialog = call
            return instance
        }
    }


    /**
     * 创建对话框
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (null == mOnCallDialog) {
            super.onCreateDialog(savedInstanceState)
        }
        return mOnCallDialog!!.getDialog(requireActivity())
    }

    /**
     * 开始
     */
    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) { //在5.0以下的版本会出现白色背景边框，若在5.0以上设置则会造成文字部分的背景也变成透明
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) { //目前只有这两个dialog会出现边框
                if (dialog is ProgressDialog || dialog is DatePickerDialog) {
                    getDialog()!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
            }
            val window: Window? = getDialog()!!.window
            val windowParams: WindowManager.LayoutParams = window!!.attributes
            windowParams.dimAmount = 0.0f
            window.attributes = windowParams
        }
    }

    /**
     * 取消
     */
    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (mCancelListener != null)
            mCancelListener?.onCancel()
    }


}