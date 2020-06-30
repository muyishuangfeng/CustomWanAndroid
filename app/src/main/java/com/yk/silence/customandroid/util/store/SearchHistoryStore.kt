package com.yk.silence.customandroid.util.store

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yk.silence.customandroid.common.APP
import com.yk.silence.customandroid.ext.SpHelper

/**
 * 搜索历史缓存
 */
object SearchHistoryStore {

    private const val SP_SEARCH_HISTORY = "sp_search_history"
    private const val KEY_SEARCH_HISTORY = "searchKey"
    private val mGson: Gson by lazy { Gson() }

    /**
     * 保存搜索的关键字
     */
    fun saveSearchHistory(searchWords: String) {
        val history = getHistoryStore()
        if (history.contains(searchWords)) {
            history.remove(searchWords)
        }
        history.add(0, searchWords)
        val listResult = mGson.toJson(history)
        SpHelper.putSpValue(SP_SEARCH_HISTORY, APP.sInstance, KEY_SEARCH_HISTORY, listResult)

    }

    /**
     * 删除搜索关键字
     */
    fun deleteSearchHistory(searchWords: String) {
        val history = getHistoryStore()
        history.remove(searchWords)
        val listResult = mGson.toJson(history)
        SpHelper.putSpValue(SP_SEARCH_HISTORY, APP.sInstance, KEY_SEARCH_HISTORY, listResult)
    }


    /**
     * 获取搜索历史
     */
    fun getHistoryStore(): MutableList<String> {
        val listResult =
            SpHelper.getSpValue(SP_SEARCH_HISTORY, APP.sInstance, KEY_SEARCH_HISTORY, "")
        return if (listResult.isEmpty()) {
            mutableListOf()
        } else {
            mGson.fromJson(listResult, object : TypeToken<MutableList<String>>() {}.type)
        }
    }

}