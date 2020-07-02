package com.yk.silence.customandroid.model

/**
 * 积分排行
 */
data class PointRank(
    val coinCount: Int,
    val level: Int,
    val rank: Int,
    val userId: Int,
    val username: String
)