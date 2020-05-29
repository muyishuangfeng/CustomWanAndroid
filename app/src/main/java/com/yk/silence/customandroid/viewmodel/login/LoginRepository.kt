package com.yk.silence.customandroid.viewmodel.login

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

/**
 * 登录
 */
class LoginRepository {

    /**
     * 登录
     */
    suspend fun login(account: String, password: String) =
        RetrofitClient.apiService.login(account, password).apiData()
}