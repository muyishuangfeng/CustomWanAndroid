package com.yk.silence.customandroid.viewmodel.detail

import com.yk.silence.customandroid.db.RoomHelper
import com.yk.silence.customandroid.model.Article

class DetailRepository {
    /**
     * 保存阅读记录
     */
    suspend fun saveReadHistory(article: Article) = RoomHelper.addReadHistory(article)
}