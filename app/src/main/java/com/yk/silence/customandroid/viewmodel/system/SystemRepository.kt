package com.yk.silence.customandroid.viewmodel.system

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class SystemRepository {
    /**
     * 获取文章类别
     */
    suspend fun getArticleCategories() = RetrofitClient.apiService.getArticleCategories().apiData()
}