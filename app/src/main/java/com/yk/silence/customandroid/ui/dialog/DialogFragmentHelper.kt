package com.yk.silence.customandroid.ui.dialog


import android.app.Dialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.impl.OnCallDialog


/**
 * DialogFragment帮助类
 */
object DialogFragmentHelper {

    /**
     * 加载中的弹出窗
     */
    private val mTheme: Int = R.style.CustomTheme_Dialog
    private val mDialogTheme: Int = R.style.CustomThemeDialog
    private val TAG = DialogFragmentHelper::class.java.simpleName
    private val mProgressTag: String = "$TAG:progress"


    /**
     * 设置字体大小
     */
    fun setFontSizeDialog(
        fragmentManager: FragmentManager?,
        mListener: FontSizeDialog.OnFontChangeListener

    ) {
        val dialogFragment: BaseDialogFragment? =
            BaseDialogFragment.newInstance(object : OnCallDialog {
                override fun getDialog(context: Context?): Dialog {
                    val mDialog =
                        FontSizeDialog(
                            context!!
                        )
                    mDialog.setOnFontSizeChangeListener(object :
                        FontSizeDialog.OnFontChangeListener {
                        override fun onFontChange(size: Int) {
                            mListener.onFontChange(size)
                        }

                    })
                    return mDialog
                }
            }, true, null)
        dialogFragment?.show(
            fragmentManager!!, "setFontSizeDialog"
        )
    }

    /**
     * 公共对话框
     */
    fun setOnCommonDialog(
        fragmentManager: FragmentManager?,
        title:String,
        content:String,
        mListener: CommonDialog.OnCommonCheckListener

    ) {
        val dialogFragment: BaseDialogFragment? =
            BaseDialogFragment.newInstance(object : OnCallDialog {
                override fun getDialog(context: Context?): Dialog {
                    val mDialog =
                        CommonDialog(
                            context!!,mTheme)
                    mDialog.setTitle(title)
                    mDialog.setContent(content)
                    mDialog.setOnCommonClickListener(object :
                        CommonDialog.OnCommonCheckListener {
                        override fun onCommonCheck() {
                            mListener.onCommonCheck()
                        }

                    })
                    return mDialog
                }
            }, true, null)
        dialogFragment?.show(
            fragmentManager!!, "setOnCommonDialog"
        )
    }


}