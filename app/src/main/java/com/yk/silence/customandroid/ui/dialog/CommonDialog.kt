package com.yk.silence.customandroid.ui.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import com.yk.silence.customandroid.R

/**
 * 公共对话框
 */
class CommonDialog(context: Context, themeID: Int) :
    AlertDialog(context, themeID), View.OnClickListener {
    private var mTitle: CharSequence? = null
    private var mContent: CharSequence? = null
    private var mListener: OnCommonCheckListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_common_layout)
        //设置背景透明，不然会出现白色直角问题
        val window = window!!
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCanceledOnTouchOutside(false)
        //初始化布局控件
        initView()
        //设置参数必须在show之后，不然没有效果
        val params = window.attributes
        window.attributes.alpha=1.0f
        window.attributes = params
        window.setWindowAnimations(R.style.updateDialogStyle) //添加动画
    }

    /**
     * 初始化控件
     */
    private fun initView() { //对话框标题
        val txtTitle = findViewById<TextView>(R.id.txt_dialog_title)
        //对话框描述信息
        val txtContent = findViewById<TextView>(R.id.txt_dialog_content)
        //确定按钮和取消
        val mTxtOk = findViewById<TextView>(R.id.txt_dialog_sure)
        val txtCancel = findViewById<TextView>(R.id.txt_dialog_cancel)
        assert(txtTitle != null)
        txtTitle!!.text = mTitle
        assert(txtContent != null)
        txtContent!!.text = mContent
        assert(mTxtOk != null)
        mTxtOk!!.setOnClickListener(this)
        assert(txtCancel != null)
        txtCancel!!.setOnClickListener(this)
    }

    /**
     * 标题
     */
    override fun setTitle(title: CharSequence?) {
        mTitle = title
    }

    /**
     * 内容
     */
    fun setContent(content: CharSequence?) {
        mContent = content
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.txt_dialog_sure -> {
                //确定
                if (mListener != null) {
                    mListener!!.onCommonCheck()
                }
                dismiss()
            }
            R.id.txt_dialog_cancel -> {
                //取消
                dismiss()
            }
        }
    }

    /**
     * 接口
     */
    interface OnCommonCheckListener {
        fun onCommonCheck()
    }

    /**
     * 回调
     */
    fun setOnCommonClickListener(confirmListener: OnCommonCheckListener?) {
        mListener = confirmListener
    }
}