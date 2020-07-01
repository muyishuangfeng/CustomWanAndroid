package com.yk.silence.customandroid.viewmodel.home.last

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class LastRepository {
    /**
     * 获取最新的数据
     */
    suspend fun getLastProjectList(page: Int) =
        RetrofitClient.apiService.getProjectList(page).apiData()
}