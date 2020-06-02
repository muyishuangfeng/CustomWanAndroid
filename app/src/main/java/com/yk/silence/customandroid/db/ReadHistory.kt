package com.yk.silence.customandroid.db

import androidx.room.Embedded
import androidx.room.Relation
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.model.Tag

/**
 * 阅读历史记录
 */
data class ReadHistory(
    @Embedded
    var article: Article,
    @Relation(parentColumn = "id", entityColumn = "article_id")
    var tags: List<Tag>
)