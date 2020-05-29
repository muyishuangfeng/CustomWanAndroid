package com.yk.silence.customandroid.common

import android.app.Activity
import android.content.Intent
import com.yk.silence.customandroid.ext.putExtras

/**
 * activity管理类
 */
object ActivityManager {

    //activity集合
     val activities = mutableListOf<Activity>()

    /**
     * 开始
     */
    fun start(clazz: Class<out Activity>, params: Map<String, Any> = emptyMap()) {
        val currentActivity = activities[activities.lastIndex]
        val intent = Intent(currentActivity, clazz)
        params.forEach {
            intent.putExtras(it.key to it.value)
        }
        currentActivity.startActivity(intent)
    }

    /**
     * finish指定的一个或多个Activity
     */
    fun finish(vararg clazz: Class<out Activity>) {
        activities.forEach { activity ->
            if (clazz.contains(activity::class.java)) {
                activity.finish()
            }
        }
    }

}