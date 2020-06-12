package com.yk.silence.customandroid.viewmodel.history

import com.yk.silence.customandroid.db.RoomHelper
import com.yk.silence.customandroid.model.Article

class HistoryRepository {
    /**
     * 查询所有历史记录
     */
    suspend fun searchAllHistory() = RoomHelper.queryAllReadHistory()

    /**
     * 删除所有历史记录
     */
    suspend fun deleteAllHistory(article: Article) = RoomHelper.deleteReadHistory(article)
}