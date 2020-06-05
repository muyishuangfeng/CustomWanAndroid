package com.yk.silence.customandroid.viewmodel.system

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.model.Category

class SystemViewModel : BaseViewModel() {

    private val mSystemRepository by lazy { SystemRepository() }
    val mCategorys = MutableLiveData<MutableList<Category>>()
    val mLoadingStatus = MutableLiveData<Boolean>()
    val mReLoadState = MutableLiveData<Boolean>()

    /**
     * 获取文章分类
     */
    fun getArticleCategory() {
        mLoadingStatus.value = true
        mReLoadState.value = false
        launch(
            block = {
                mCategorys.value = mSystemRepository.getArticleCategories()
                mLoadingStatus.value = false
            },
            error = {
                mLoadingStatus.value = false
                mReLoadState.value = true
            }
        )
    }

}