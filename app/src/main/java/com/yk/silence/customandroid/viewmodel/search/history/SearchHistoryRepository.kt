package com.yk.silence.customandroid.viewmodel.search.history

import com.yk.silence.customandroid.net.retrofit.RetrofitClient
import com.yk.silence.customandroid.util.store.SearchHistoryStore

class SearchHistoryRepository {
    /**
     * 热门搜索
     */
    suspend fun getHotSearch() = RetrofitClient.apiService.getHotWords().apiData()

    /**
     * 保存搜索的关键字
     */
    fun saveSearchHistory(searchWords: String) {
        SearchHistoryStore.saveSearchHistory(searchWords)
    }

    /**
     * 删除搜索的关键字
     */
    fun deleteSearchHistory(searchWords: String) {
        SearchHistoryStore.deleteSearchHistory(searchWords)
    }

    /**
     * 获取搜索历史
     */
    fun getSearchHistory() = SearchHistoryStore.getHistoryStore()
}