package com.yk.silence.customandroid.db

import androidx.room.Room
import com.yk.silence.customandroid.common.APP
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.model.Tag

/**
 * 数据库帮助类
 */
object RoomHelper {

    private val appDataBase by lazy {
        Room.databaseBuilder(APP.sInstance, AppDataBase::class.java, "database_wanandroid").build()
    }
    //历史记录
    private val mReadHistoryDao = appDataBase.getReadHistory()

    /**
     * 查询所有阅读记录(倒叙)
     */
    suspend fun queryAllReadHistory() = mReadHistoryDao.queryAll().map {
        it.article.apply { tags = it.tags }
    }.reversed()

    /**
     * 添加阅读记录
     */
    suspend fun addReadHistory(article: Article) {
        mReadHistoryDao.queryArticle(article.id).let {
            mReadHistoryDao.deleteArticle(article)
        }
        mReadHistoryDao.insert(article.apply { primaryKeyId = 0 })
        article.tags.forEach {
            mReadHistoryDao.insertArticleTag(
                Tag(
                    id = 0, articleId = article.id.toLong(), name = it.name,
                    url = it.url
                )
            )
        }
    }

    /**
     * 删除阅读记录
     */
    suspend fun deleteReadHistory(article: Article) = mReadHistoryDao.deleteArticle(article)

}