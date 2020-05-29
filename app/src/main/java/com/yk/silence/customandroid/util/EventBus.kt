package com.yk.silence.customandroid.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * 事件总线
 */
object EventBus {
    /**
     * 传递参数
     */
    inline fun <reified T> post(channel: String, value: T) =
        LiveEventBus.get(channel, T::class.java).post(value)

    /**
     * 监听事件
     */
    inline fun <reified T> observe(
        channel: String,
        owner: LifecycleOwner,
        crossinline observer: ((value: T) -> Unit)
    ) =
        LiveEventBus.get(channel, T::class.java).observe(owner, Observer { observer(it) })
}