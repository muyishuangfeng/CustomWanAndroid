package com.yk.silence.customandroid.viewmodel.login

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.util.EventBus

class LoginViewModel : BaseViewModel() {
    private val mLoginRepository by lazy { LoginRepository() }
    //提交
    val mSubmitting = MutableLiveData<Boolean>()
    //登录结果
    val mLoginResult = MutableLiveData<Boolean>()

    /**
     * 登录
     */
    fun login(account: String, password: String) {
        mSubmitting.value = true
        launch(
            block = {
                val userInfo = mLoginRepository.login(account, password)
                mUserRepository.updateUserInfo(userInfo)
                EventBus.post(Constants.USER_COLLECT_UPDATED, true)
                mSubmitting.value = false
                mLoginResult.value = true
            },
            error = {
                mSubmitting.value = false
                mLoginResult.value = false
            }

        )
    }

}