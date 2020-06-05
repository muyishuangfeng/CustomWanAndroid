package com.yk.silence.customandroid.util

import android.content.Context

object ScreenUtil {
    /**
     * 获取屏幕高度
     */
    fun getScreenHeight(context: Context) = context.resources.displayMetrics.heightPixels

    /**
     * 获取屏幕宽度
     */
    fun getScreenWidth(context: Context) = context.resources.displayMetrics.widthPixels

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }
}