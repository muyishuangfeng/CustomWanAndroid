package com.yk.silence.customandroid.viewmodel.discover

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class DiscoverRepository {
    /**
     * 获取热门词汇
     */
    suspend fun getHotWords() = RetrofitClient.apiService.getHotWords().apiData()

    /**
     * 获取Banner
     */
    suspend fun getBanners() = RetrofitClient.apiService.getBanners().apiData()

    /**
     * 获取常用网址
     */
    suspend fun getFrequentlyWebsites() = RetrofitClient.apiService.getFrequentlyWebsites().apiData()
}