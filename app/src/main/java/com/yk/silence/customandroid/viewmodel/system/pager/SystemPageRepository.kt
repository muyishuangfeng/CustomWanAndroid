package com.yk.silence.customandroid.viewmodel.system.pager

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class SystemPageRepository {
    /**
     * 通过cid获取当前页的文章
     */
    suspend fun getArticleListByCid(page: Int, cid: Int) =
        RetrofitClient.apiService.getArticleListByCid(page, cid).apiData()

}