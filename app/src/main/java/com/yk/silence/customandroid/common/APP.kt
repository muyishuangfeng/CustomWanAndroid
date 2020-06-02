package com.yk.silence.customandroid.common

import android.app.Application
import com.yk.silence.customandroid.util.SettingsStore
import com.yk.silence.customandroid.util.DayOrNightUtil
import com.yk.silence.customandroid.util.ProcessHelper


class APP : Application() {



    companion object {
        lateinit var sInstance: APP
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this
        if (ProcessHelper.isMainProcess(this)) {
            init()
        }
    }

    /**
     * 初始化
     */
    private fun init() {
        registerActivityCallbacks()
        setDayOrNightModel()

    }

    /**
     * 设置日夜间模式
     */
    private fun setDayOrNightModel() {
        DayOrNightUtil.setNightModel(SettingsStore.getNightMode())
    }

    /**
     * 监听activity回调
     */
    private fun registerActivityCallbacks() {
        registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksAdapter(
            onActivityCreated = { activity, _ ->
                ActivityManager.activities.add(activity)
            },
            onActivityDestroyed = { activity ->
                ActivityManager.activities.remove(activity)
            }

        ))
    }
}