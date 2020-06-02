package com.yk.silence.customandroid.model

/**
 * 热门词汇
 */
data class HotWord(
    val id: Int,
    val link: String,
    val order: Int,
    val name: String,
    val visible: Int
)