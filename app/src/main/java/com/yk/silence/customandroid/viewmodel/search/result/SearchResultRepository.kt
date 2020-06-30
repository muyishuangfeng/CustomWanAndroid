package com.yk.silence.customandroid.viewmodel.search.result

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class SearchResultRepository {
    /**
     * 查询
     */
    suspend fun search(keyWords: String, page: Int) =
        RetrofitClient.apiService.search(keyWords, page).apiData()
}