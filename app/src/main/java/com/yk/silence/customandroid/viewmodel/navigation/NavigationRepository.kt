package com.yk.silence.customandroid.viewmodel.navigation

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class NavigationRepository {
    /**
     * 获取导航信息
     */
    suspend fun getNavigations() = RetrofitClient.apiService.getNavigations().apiData()
}