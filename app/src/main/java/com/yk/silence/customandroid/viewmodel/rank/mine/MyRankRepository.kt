package com.yk.silence.customandroid.viewmodel.rank.mine

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class MyRankRepository {
    /**
     * 获取我的排行
     */
    suspend fun getMyPoint() = RetrofitClient.apiService.getPoints().apiData()

    /**
     * 获取积分记录
     */
    suspend fun getPointRecord(page: Int) =
        RetrofitClient.apiService.getPointsRecord(page).apiData()
}