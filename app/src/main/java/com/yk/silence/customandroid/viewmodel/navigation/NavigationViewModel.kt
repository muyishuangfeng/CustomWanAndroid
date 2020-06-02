package com.yk.silence.customandroid.viewmodel.navigation

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.model.NavigationModel

class NavigationViewModel : BaseViewModel() {
    //导航
    val mNavigationRepository by lazy { NavigationRepository() }
    //导航集合
    val mNavigationList = MutableLiveData<List<NavigationModel>>()
    //刷新
    val mRefreshState = MutableLiveData<Boolean>()
    //加载
    val mReLoadState = MutableLiveData<Boolean>()

    /**
     * 获取导航信息
     */
    fun getNavigations() {
        mRefreshState.value = true
        mReLoadState.value = false
        launch(
            block = {
                mNavigationList.value = mNavigationRepository.getNavigations()
                mRefreshState.value = false
            },
            error = {
                mRefreshState.value = false
                mReLoadState.value = mNavigationList.value.isNullOrEmpty()
            }
        )
    }

}