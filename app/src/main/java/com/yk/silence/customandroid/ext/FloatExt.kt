package com.yk.silence.customandroid.ext

import com.yk.silence.customandroid.common.APP


/**
 * Created by xiaojianjun on 2019-12-02.
 */
fun Float.toPx() = dpToPx(APP.sInstance, this)

fun Float.toIntPx() = dpToPx(APP.sInstance, this).toInt()

fun Float.toDp() = pxToDp(APP.sInstance, this)

fun Float.toIntDp() = pxToDp(APP.sInstance, this).toInt()