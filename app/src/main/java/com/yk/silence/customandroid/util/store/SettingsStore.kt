package com.yk.silence.customandroid.util.store

import com.yk.silence.customandroid.common.APP
import com.yk.silence.customandroid.ext.SpHelper

/**
 * 设置中心
 */
object SettingsStore {
    //sharePreference设置
    private const val SP_SETTINGS = "sp_settings"
    //默认大小设置
    private const val DEFAULT_WEB_TEXT_ZOOM = 100
    //webView大小设置
    private const val KEY_WEB_TEXT_ZOOM = "key_web_text_zoom"
    //日夜间模式设置
    private const val KEY_NIGHT_MODE = "key_night_mode"

    /**
     * 设置字体大小
     */
    fun setWebTextZoom(textZoom: Int) =
        SpHelper.putSpValue(
            SP_SETTINGS, APP.sInstance,
            KEY_WEB_TEXT_ZOOM, textZoom)

    /**
     * 获取字体大小
     */
    fun getWebTextZoom() =
        SpHelper.getSpValue(
            SP_SETTINGS, APP.sInstance,
            KEY_WEB_TEXT_ZOOM,
            DEFAULT_WEB_TEXT_ZOOM
        )

    /**
     * 设置日夜间模式
     */
    fun setNightMode(nightMode: Boolean) =
        SpHelper.putSpValue(
            SP_SETTINGS, APP.sInstance,
            KEY_NIGHT_MODE, nightMode)

    /**
     * 获取日夜间模式
     */
    fun getNightMode() =
        SpHelper.getSpValue(
            SP_SETTINGS, APP.sInstance,
            KEY_NIGHT_MODE, false)

}