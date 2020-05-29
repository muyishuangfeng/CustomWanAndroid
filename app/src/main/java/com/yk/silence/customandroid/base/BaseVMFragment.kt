package com.yk.silence.customandroid.base

import android.os.Bundle
import android.view.View
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
abstract class BaseVMFragment<VM : BaseViewModel,V: ViewDataBinding> : BaseFragment<V>() {

    protected lateinit var mViewModel: VM
    //懒加载
    private var lazyLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
        observe()
        // 因为Fragment恢复后savedInstanceState不为null，
        // 重新恢复后会自动从ViewModel中的LiveData恢复数据，
        // 不需要重新初始化数据。
        if (savedInstanceState == null) {
            initData()
        }
    }

    override fun onResume() {
        super.onResume()
        // 实现懒加载
        if (!lazyLoaded) {
            lazyLoadData()
            lazyLoaded = true
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
     * 如果有需要初始化
     */
    open fun initData() {}

    /**
     * 如果有需要初始化
     */
    open fun initView() {}

    /**
     * 如果有需要初始化
     */
    open fun lazyLoadData() {
    }

    open fun observe() {
        // 登录失效，跳转登录页
        mViewModel.loginStatusInvalid.observe(viewLifecycleOwner, Observer {
            if (it) {
                EventBus.post(Constants.USER_LOGIN_STATE_CHANGED, false)
                ActivityManager.start(LoginActivity::class.java)
            }
        })
    }


    /**
     * 是否登录，如果登录了就执行then，没有登录就直接跳转登录界面
     * @return true-已登录，false-未登录
     */
    fun checkLogin(then: (() -> Unit)? = null): Boolean {
        return if (mViewModel.loginState()) {
            then?.invoke()
            true
        } else {
            ActivityManager.start(LoginActivity::class.java)
            false
        }
    }

}