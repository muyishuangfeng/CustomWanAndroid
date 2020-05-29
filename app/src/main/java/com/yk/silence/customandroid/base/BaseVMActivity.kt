package com.yk.silence.customandroid.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.widget.activity.LoginActivity

/**
 * 封装ViewModel和DataBinding
 */
abstract class BaseVMActivity<VM : BaseViewModel, V : ViewDataBinding> : BaseActivity<V>() {

    protected open lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
        observer()
        // 因为Activity恢复后savedInstanceState不为null，
        // 重新恢复后会自动从ViewModel中的LiveData恢复数据，
        // 不需要重新初始化数据。
        if (savedInstanceState == null) {
            initData()
        }
    }

    abstract fun viewModelClass(): Class<VM>

    /**
     * 初始化viewModel
     */
    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }

    /**
     * 如有需要可以初始化
     */
    open fun initView() {

    }

    /**
     * 如有需要可以初始化
     */
    open fun initData() {

    }

    /**
     * 监听登录状态
     */
    open fun observer() {
        // 登录失效，跳转登录页
        mViewModel.loginStatusInvalid.observe(this, Observer {
            if (it) {
                EventBus.post(Constants.USER_COLLECT_UPDATED, false)
                ActivityManager.start(LoginActivity::class.java)
            }

        })
    }

    /**
     * 是否登录，如果登录了就执行then，没有登录就直接跳转登录界面
     * @return true-已登录，false-未登录
     */
    fun checkLoginState(then: (() -> Unit)? = null): Boolean {
        return if (mViewModel.loginState()) {
            then?.invoke()
            true
        } else {
            ActivityManager.start(LoginActivity::class.java)
            false
        }
    }
}