@file:Suppress("UNCHECKED_CAST")

package com.yk.silence.customandroid.ext

import android.content.Context


/**
 * SharePreference帮助类
 */
private const val SP_WANANDROID = "sp_wanandroid"

object SpHelper {
    /**
     * 获取
     */
    @JvmOverloads
    fun <T> getSpValue(
        filename: String = SP_WANANDROID,
        context: Context,
        key: String,
        defaultVal: T
    ): T {
        val sp = context.getSharedPreferences(filename, Context.MODE_PRIVATE)
        return when (defaultVal) {
            is Boolean -> sp.getBoolean(key, defaultVal) as T
            is String -> sp.getString(key, defaultVal) as T
            is Int -> sp.getInt(key, defaultVal) as T
            is Long -> sp.getLong(key, defaultVal) as T
            is Float -> sp.getFloat(key, defaultVal) as T
            is Set<*> -> sp.getStringSet(key, defaultVal as Set<String>) as T
            else -> throw IllegalArgumentException("Unrecognized default value $defaultVal")
        }
    }

    /**
     * 插入
     */
    @JvmOverloads
    fun <T> putSpValue(
        filename: String = SP_WANANDROID,
        context: Context,
        key: String,
        value: T
    ) {
        val editor = context.getSharedPreferences(filename, Context.MODE_PRIVATE).edit()
        when (value) {
            is Boolean -> editor.putBoolean(key, value)
            is String -> editor.putString(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is Float -> editor.putFloat(key, value)
            is Set<*> -> editor.putStringSet(key, value as Set<String>)
            else -> throw UnsupportedOperationException("Unrecognized value $value")
        }
        editor.apply()
    }

    /**
     * 移除
     */
    @JvmOverloads
    fun removeSpValue(filename: String = SP_WANANDROID, context: Context, key: String) {
        context.getSharedPreferences(filename, Context.MODE_PRIVATE)
            .edit()
            .remove(key)
            .apply()
    }

    /**
     * 清空
     */
    @JvmOverloads
    fun clearSpValue(filename: String = SP_WANANDROID, context: Context) {
        context.getSharedPreferences(filename, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}