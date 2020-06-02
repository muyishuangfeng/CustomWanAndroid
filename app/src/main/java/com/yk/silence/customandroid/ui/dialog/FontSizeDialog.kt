package com.yk.silence.customandroid.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.ext.SettingsStore
import com.yk.silence.customandroid.widget.adapter.SeekBarChangeListenerAdapter
import kotlinx.android.synthetic.main.dialog_font_setting_layout.*

/**
 * 设置字体对话框
 */
class FontSizeDialog(context: Context) : AlertDialog(context) {

    private var mListener: OnFontChangeListener? = null
    private var tempTextZoom = 50

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_font_setting_layout)
        //设置背景透明，不然会出现白色直角问题
        val window = window!!
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        //初始化布局控件
        initView()
        //设置参数必须在show之后，不然没有效果
        val params = getWindow()!!.attributes
        getWindow()!!.attributes = params
        window.setWindowAnimations(R.style.updateDialogStyle) //添加动画
    }


    /**
     * 初始化控件
     */
    private fun initView() {
        val textZoom = SettingsStore.getWebTextZoom()
        tempTextZoom = textZoom
        skb_dialog_font_size.progress=textZoom
        skb_dialog_font_size.setOnSeekBarChangeListener(SeekBarChangeListenerAdapter(
            onProgressChanged = { _, progress, _ ->
                tempTextZoom = 50 + progress
            }
        ))
        txt_dialog_cancel.setOnClickListener {
            dismiss()
        }
        txt_dialog_sure.setOnClickListener {
            if (mListener != null) {
                mListener!!.onFontChange(tempTextZoom)
            }
            SettingsStore.setWebTextZoom(tempTextZoom)
            dismiss()
        }

    }

    /**
     * 字体选择接口
     */
    interface OnFontChangeListener {
        fun onFontChange(size: Int)
    }

    /**
     * 回调
     */
    fun setOnFontSizeChangeListener(confirmListener: OnFontChangeListener?) {
        mListener = confirmListener
    }


}