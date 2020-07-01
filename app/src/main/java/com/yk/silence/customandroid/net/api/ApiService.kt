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

    /**
     * 分享文章
     */
    @FormUrlEncoded
    @POST("lg/user_article/add/json")
    suspend fun shareArticle(
        @Field("title") title: String,
        @Field("link") link: String
    ): ApiResult<Any>

    /**
     * 搜索文章
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun search(
        @Field("k") keywords: String,
        @Path("page") page: Int
    ): ApiResult<Pagination<Article>>

    /**
     * 获取热门文章集合
     */
    @GET("/article/top/json")
    suspend fun getTopArticleList(): ApiResult<List<Article>>

    /**
     * 获取文章集合
     */
    @GET("/article/list/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int): ApiResult<Pagination<Article>>

    /**
     * 获取最新的项目
     */
    @GET("/article/listproject/{page}/json")
    suspend fun getProjectList(@Path("page") page: Int): ApiResult<Pagination<Article>>

    /**
     * 获取项目分类
     */
    @GET("project/tree/json")
    suspend fun getProjectCategories(): ApiResult<MutableList<Category>>

    /**
     * 获取项目分类数据
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectListByCid(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ApiResult<Pagination<Article>>

    /**
     * 获取用户文章集合
     */
    @GET("/user_article/list/{page}/json")
    suspend fun getUserArticleList(@Path("page") page: Int): ApiResult<Pagination<Article>>

    /**
     * 公众号分类
     */
    @GET("wxarticle/chapters/json")
    suspend fun getWechatCategories(): ApiResult<MutableList<Category>>

    /**
     * 获取公众号文章集合
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWechatArticleList(
        @Path("page") page: Int,
        @Path("id") id: Int
    ): ApiResult<Pagination<Article>>
}