package com.yk.silence.customandroid.viewmodel.mine

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.model.UserModel

class MineViewModel : BaseViewModel() {

    val mUserModel = MutableLiveData<UserModel>()
    val isLogin = MutableLiveData<Boolean>()
    /**
     * 获取用户信息
     */
    fun getUserInfo() {
        mUserModel.value = mUserRepository.getUserInfo()
        isLogin.value = mUserRepository.isLogin()
    }
}