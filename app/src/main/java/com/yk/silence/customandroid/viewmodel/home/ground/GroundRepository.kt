package com.yk.silence.customandroid.viewmodel.home.ground

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class GroundRepository {
    /**
     * 获取用户文章集合
     */
    suspend fun getUserArticleList(page: Int) =
        RetrofitClient.apiService.getUserArticleList(page).apiData()
}