package com.yk.silence.customandroid.viewmodel.register

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.util.EventBus

class RegisterViewModel : BaseViewModel() {

    private val mRegisterRepository by lazy { RegisterRepository() }
    //提交
    val mSubmitting = MutableLiveData<Boolean>()
    //注册结果
    val mRegisterResult = MutableLiveData<Boolean>()

    /**
     * 注册
     */
    fun register(account: String, password: String, rePassword: String) {
        mSubmitting.value = true
        launch(
            block = {
                val userInfo = mRegisterRepository.register(account, password, rePassword)
                mUserRepository.updateUserInfo(userInfo)
                EventBus.post(Constants.USER_COLLECT_UPDATED, true)
                mSubmitting.value = false
                mRegisterResult.value = true
            },
            error = {
                mSubmitting.value = false
                mRegisterResult.value = false
            }
        )

    }

}