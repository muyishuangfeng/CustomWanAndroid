package com.yk.silence.customandroid.viewmodel.user

import com.yk.silence.customandroid.model.UserModel
import com.yk.silence.customandroid.net.retrofit.RetrofitClient
import com.yk.silence.customandroid.viewmodel.user.UserInfoStore

/**
 * 用户信息
 */
open class UserRepository {
    /**
     * 更新用户信息
     */
    fun updateUserInfo(userInfo: UserModel) = UserInfoStore.setUserInfo(userInfo)

    /**
     * 是否登录
     */
    fun isLogin() = UserInfoStore.isLogin()

    /**
     * 清除用户登录状态
     */
    fun clearLoginState() {
        UserInfoStore.clearUserInfo()
        RetrofitClient.clearCookie()
    }

    /**
     * 获取用户信息
     */
    fun getUserInfo() = UserInfoStore.getUserInfo()
}