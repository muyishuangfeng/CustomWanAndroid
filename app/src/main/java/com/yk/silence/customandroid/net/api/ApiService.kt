package com.yk.silence.customandroid.net.api

import com.yk.silence.customandroid.model.BannerModel
import com.yk.silence.customandroid.model.FrequentlyModel
import com.yk.silence.customandroid.model.HotWord
import com.yk.silence.customandroid.model.UserModel
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    companion object {
        const val BASE_URL = "https://www.wanandroid.com"
    }

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResult<UserModel>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): ApiResult<UserModel>

    /**
     * 获取Banner列表
     */
    @GET("banner/json")
    suspend fun getBanners(): ApiResult<List<BannerModel>>

    /**
     * 热门词汇
     */
    @GET("hotkey/json")
    suspend fun getHotWords(): ApiResult<List<HotWord>>

    /**
     * 常用网址
     */
    @GET("friend/json")
    suspend fun getFrequentlyWebsites(): ApiResult<List<FrequentlyModel>>
}