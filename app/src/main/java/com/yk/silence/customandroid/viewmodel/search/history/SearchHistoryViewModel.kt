package com.yk.silence.customandroid.viewmodel.search.history

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.model.HotWord

/**
 * 查询历史
 */
class SearchHistoryViewModel : BaseViewModel() {

    private val mSearchHistoryRepository by lazy { SearchHistoryRepository() }

    //搜索历史
    val mSearchHistory = MutableLiveData<MutableList<String>>()

    //热门词汇
    val hotWords = MutableLiveData<List<HotWord>>()

    /**
     * 热门搜索
     */
    fun getHotSearch() {
        launch(block = {
            hotWords.value = mSearchHistoryRepository.getHotSearch()
        })
    }

    /**
     * 获取搜索历史
     */
    fun getSearchHistory() {
        mSearchHistory.value = mSearchHistoryRepository.getSearchHistory()
    }

    /**
     * 保存搜索的关键字
     */
    fun saveSearchHistory(words: String) {
        val history = mSearchHistory.value ?: mutableListOf()
        if (history.contains(words)) {
            history.remove(words)
        }
        history.add(0, words)
        mSearchHistory.value = history
        mSearchHistoryRepository.saveSearchHistory(words)
    }

    /**
     * 删除搜索的关键字
     */
    fun deleteSearchHistory(words: String) {
        val history = mSearchHistory.value ?: mutableListOf()
        if (history.contains(words)) {
            history.remove(words)
            mSearchHistory.value = history
            mSearchHistoryRepository.deleteSearchHistory(words)
        }
    }

}