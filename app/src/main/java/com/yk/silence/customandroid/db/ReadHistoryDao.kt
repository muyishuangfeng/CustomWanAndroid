package com.yk.silence.customandroid.db

import androidx.room.*
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.model.Tag
import java.util.*

/**
 * 历史记录接口
 */
@Dao
interface ReadHistoryDao {
    /**
     * 插入文章
     */
    @Transaction
    @Insert(entity = Article::class)
    suspend fun insert(article: Article): Long

    /**
     * 插入文章标签
     */
    @Transaction
    @Insert(entity = Tag::class)
    suspend fun insertArticleTag(tag: Tag): Long

    /**
     * 查询所有文章信息
     */
    @Transaction
    @Query("SELECT * FROM article")
    suspend fun queryAll(): List<ReadHistory>

    /**
     * 根据ID查询文章
     */
    @Transaction
    @Query("SELECT * FROM article where id=(:id)")
    suspend fun queryArticle(id: Int): Article

    /**
     * 删除文章
     */
    @Transaction
    @Delete(entity = Article::class)
    suspend fun deleteArticle(article: Article)

    /**
     * 更新文章
     */
    @Transaction
    @Update(entity = Article::class)
    suspend fun updateArticle(article: Article)
}