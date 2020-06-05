package com.yk.silence.customandroid.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 类别
 */
@Parcelize
data class Category(
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int,
    val children: ArrayList<Category>
) : Parcelable