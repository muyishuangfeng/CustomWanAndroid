package com.yk.silence.customandroid.net.api

import com.yk.silence.customandroid.model.*
import retrofit2.http.*

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

    /**
     * 收藏
     */
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): ApiResult<Any?>

    /**
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Int): ApiResult<Any?>

    /**
     * 导航
     */
    @GET("navi/json")
    suspend fun getNavigations(): ApiResult<List<NavigationModel>>

    /**
     * 获取文章类别
     */
    @GET("tree/json")
    suspend fun getArticleCategories(): ApiResult<MutableList<Category>>

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectionList(@Path("page") page: Int): ApiResult<Pagination<Article>>

    /**
     * 通过Cid获取文章
     */
    @GET("article/list/{page}/json")
    suspend fun getArticleListByCid(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ApiResult<Pagination<Article>>
}