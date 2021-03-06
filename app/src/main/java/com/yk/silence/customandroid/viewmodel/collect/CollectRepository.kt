package com.yk.silence.customandroid.viewmodel.collect

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class CollectRepository {
    /**
     * 收藏
     */
    suspend fun collect(id: Int) = RetrofitClient.apiService.collect(id).apiData()

    /**
     * 取消收藏
     */
    suspend fun unCollect(id: Int) = RetrofitClient.apiService.unCollect(id).apiData()

    /**
     * 获取我的收藏列表
     */
    suspend fun getCollectList(page: Int) =
        RetrofitClient.apiService.getCollectionList(page).apiData()
}