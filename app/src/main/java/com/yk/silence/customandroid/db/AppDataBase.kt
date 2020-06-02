package com.yk.silence.customandroid.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.model.Tag

/**
 * 数据库
 */
@Database(entities = [Article::class, Tag::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    /**
     * 获取阅读的历史记录
     */
    abstract fun getReadHistory(): ReadHistoryDao
}