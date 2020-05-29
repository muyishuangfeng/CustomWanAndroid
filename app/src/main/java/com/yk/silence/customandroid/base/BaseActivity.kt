package com.yk.silence.customandroid.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.yk.silence.customandroid.ui.ProgressDialogFragment

/**
 * 加载状态有4种：
 * 1.整页数据加载，加载动画在页面中间
 * 2.下拉刷新
 * 3.分页加载更多
 * 4.数据提交服务器加载对话框
 */

/**
 * 加载结果：
 * 1.空，无数据
 * 2.无网络
 * 3.失败，点击重试
 */
abstract class BaseActivity<V: ViewDataBinding> : AppCompatActivity() {

    private lateinit var mDialogFragment: ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mBinding: V = DataBindingUtil.setContentView(this, getLayoutID())
        initBinding(mBinding)
    }



    /**
     * 加载布局
     */
    open fun getLayoutID() = 0

    open fun initBinding(mBinding: V){

    }

    /**
     * 显示进度对话框
     */
    fun showProgressDialog(message: Int) {
        if (!this::mDialogFragment.isInitialized) {
            mDialogFragment = ProgressDialogFragment.newInstance()
        }
        mDialogFragment.show(supportFragmentManager, message, false)


    }

    /**
     * 隐藏进度对话框
     */
    fun hideProgressDialog() {
        if (this::mDialogFragment.isInitialized && mDialogFragment.isVisible) {
            mDialogFragment.dismiss()
        }


    }


}