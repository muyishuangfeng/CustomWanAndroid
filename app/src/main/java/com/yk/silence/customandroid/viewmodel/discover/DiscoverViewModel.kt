package com.yk.silence.customandroid.viewmodel.discover

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.model.BannerModel
import com.yk.silence.customandroid.model.FrequentlyModel
import com.yk.silence.customandroid.model.HotWord

/**
 * 探索ViewModel
 */
class DiscoverViewModel : BaseViewModel() {

    private val mDiscoverRepository by lazy { DiscoverRepository() }
    //banner集合
    val mBannerList = MutableLiveData<List<BannerModel>>()
    //热门词汇集合
    val mHotWords = MutableLiveData<List<HotWord>>()
    //常用网址
    val mFrequentlyList = MutableLiveData<List<FrequentlyModel>>()
    //刷新状态
    val mRefreshState = MutableLiveData<Boolean>()
    //加载状态
    val mReloadState = MutableLiveData<Boolean>()

    /**
     * 获取数据
     */
    fun getData() {
        mRefreshState.value = true
        mReloadState.value = false

        launch(
            block = {
                mBannerList.value = mDiscoverRepository.getBanners()
                mHotWords.value = mDiscoverRepository.getHotWords()
                mFrequentlyList.value = mDiscoverRepository.getFrequentlyWebsites()
                mRefreshState.value = false
            },
            error = {
                mRefreshState.value = false
                mReloadState.value = true
            }
        )

    }

}