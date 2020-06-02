package com.yk.silence.customandroid.viewmodel.setting

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.util.EventBus

class SettingViewModel : BaseViewModel() {

    val isLogin = MutableLiveData<Boolean>()

    /**
     * 获取登录状态
     */
    fun getLoginStatus() {
        mUserRepository.isLogin()
    }

    /**
     * 退出登录
     */
    fun loginOut() {
        mUserRepository.clearLoginState()
        EventBus.post(Constants.USER_LOGIN_STATE_CHANGED, false)
    }
}