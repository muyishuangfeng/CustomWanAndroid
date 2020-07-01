package com.yk.silence.customandroid.viewmodel.home.project

import com.yk.silence.customandroid.net.retrofit.RetrofitClient

class ProjectRepository {
    /**
     * 获取工程分类
     */
    suspend fun getProjectCategories() = RetrofitClient.apiService.getProjectCategories().apiData()

    /**
     * 获取工程集合
     */
    suspend fun getProjectListByCid(page: Int, cid: Int) =
        RetrofitClient.apiService.getProjectListByCid(page, cid).apiData()
}