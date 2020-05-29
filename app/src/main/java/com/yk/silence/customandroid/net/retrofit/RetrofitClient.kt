package com.yk.silence.customandroid.net.retrofit

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.yk.silence.customandroid.common.APP
import com.yk.silence.customandroid.net.api.ApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit
 */
object RetrofitClient {
    //自动cookies持久化
    private val cookieJar = PersistentCookieJar(
        SetCookieCache(),
        SharedPrefsCookiePersistor(APP.sInstance)
    )
    //okHttp客户端
    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .connectTimeout(10, TimeUnit.SECONDS)
        .cookieJar(cookieJar)
        .build()
    //retrofit客户端
    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    /**
     * 清空cookies
     */
    fun clearCookie() = cookieJar.clear()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}