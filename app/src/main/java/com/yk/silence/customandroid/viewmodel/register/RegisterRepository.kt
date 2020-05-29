package com.yk.silence.customandroid.viewmodel.register

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

/**
 * 注册
 */
class RegisterRepository {
    /**
     * 注册
     */
    suspend fun register(account: String, password: String, rePassword: String) =
        RetrofitClient.apiService.register(account, password, rePassword).apiData()

}