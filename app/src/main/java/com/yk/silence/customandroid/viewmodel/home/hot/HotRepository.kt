package com.yk.silence.customandroid.viewmodel.home.hot

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class HotRepository {
    /**
     * 获取热门文章集合
     */
    suspend fun getTopArticleList()=RetrofitClient.apiService.getTopArticleList().apiData()

    /**
     * 获取文章集合
     */
    suspend fun getArticleList(page:Int)=RetrofitClient.apiService.getArticleList(page).apiData()
}