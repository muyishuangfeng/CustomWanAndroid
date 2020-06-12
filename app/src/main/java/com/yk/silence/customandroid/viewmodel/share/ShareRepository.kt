package com.yk.silence.customandroid.viewmodel.share

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class ShareRepository {
    /**
     * 分享文章
     */
    suspend fun shareArticle(title: String, link: String) =
        RetrofitClient.apiService.shareArticle(title, link).apiData()
}