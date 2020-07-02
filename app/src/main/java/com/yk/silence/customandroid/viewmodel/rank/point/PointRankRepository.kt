package com.yk.silence.customandroid.viewmodel.rank.point

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class PointRankRepository {
    /**
     * 获取积分排行
     */
    suspend fun getPointRank(page: Int) = RetrofitClient.apiService.getPointsRank(page).apiData()
}