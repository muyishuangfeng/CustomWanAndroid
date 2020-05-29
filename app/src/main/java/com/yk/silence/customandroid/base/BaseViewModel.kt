package com.yk.silence.customandroid.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.common.APP
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.net.exception.ApiException
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.util.showToast
import com.yk.silence.customandroid.viewmodel.user.UserRepository
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException

typealias Block<T> = suspend () -> T
typealias Error = suspend (e: Exception) -> Unit
typealias Cancel = suspend (e: Exception) -> Unit

open class BaseViewModel : ViewModel() {
    //用户信息
    protected val mUserRepository by lazy { UserRepository() }
    //登录状态
    val loginStatusInvalid: MutableLiveData<Boolean> = MutableLiveData()


    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param error 错误时执行
     * @return Job
     */
    protected fun launch(block: Block<Unit>, error: Error? = null, cancel: Cancel? = null): Job {
        return viewModelScope.launch {
            try {
                block.invoke()
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        onError(e)
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @return Deferred<T>
     */
    protected fun <T> async(block: Block<T>): Deferred<T> {
        return viewModelScope.async { block.invoke() }
    }

    /**
     * 取消协程
     * @param job 协程job
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 统一处理错误
     * @param e 异常
     */
    private fun onError(e: Exception) {
        when (e) {
            is ApiException -> {
                when (e.code) {
                    -1001 -> {
                        // 登录失效
                        mUserRepository.clearLoginState()
                        EventBus.post(Constants.USER_LOGIN_STATE_CHANGED, false)
                        loginStatusInvalid.value = false

                    }
                    -1 -> {
                        // 其他Api错误
                        APP.sInstance.showToast(e.message)
                    }
                    else -> {
                        // 其他错误
                        APP.sInstance.showToast(e.message)
                    }
                }
            }
            is ConnectException -> {
                // 连接失败
                APP.sInstance.showToast(APP.sInstance.getString(R.string.network_connection_failed))
            }
            is SocketTimeoutException -> {
                // 请求超时
                APP.sInstance.showToast(APP.sInstance.getString(R.string.network_request_timeout))
            }
            is JsonParseException -> {
                // 解析失败
                APP.sInstance.showToast(APP.sInstance.getString(R.string.api_data_parse_error))
            }
        }

    }

    /**
     * 登录状态
     */
    fun loginState() = mUserRepository.isLogin()
}