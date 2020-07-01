package com.yk.silence.customandroid.viewmodel.home.wechat

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class PublicNORepository {
    /**
     * 获取公众号分类
     */
    suspend fun getPublicNOCategories() = RetrofitClient.apiService.getWechatCategories().apiData()

    /**
     * 获取公众号文章集合
     */
    suspend fun getPublicNOArticleList(page: Int, id: Int) =
        RetrofitClient.apiService.getWechatArticleList(page, id).apiData()
}