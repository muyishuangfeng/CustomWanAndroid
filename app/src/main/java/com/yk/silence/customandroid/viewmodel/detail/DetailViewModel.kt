package com.yk.silence.customandroid.viewmodel.detail

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.viewmodel.collect.CollectRepository

/**
 * 详情
 */
class DetailViewModel : BaseViewModel() {
    //阅读详情
    val mDetailRepository by lazy { DetailRepository() }
    //收藏
    val mCollectRepository by lazy { CollectRepository() }
    //是否收藏
    val isCollect = MutableLiveData<Boolean>()

    /**
     * 收藏
     */
    fun collect(id: Int) {
        launch(
            block = {
                //收藏
                mCollectRepository.collect(id)
                //收藏完成更新用户信息
                mUserRepository.updateUserInfo(mUserRepository.getUserInfo()!!.apply {
                    if (!collectIds.contains(id)) collectIds.add(id)
                })
                isCollect.value = true
            },
            error = {
                isCollect.value = false
            }
        )
    }

    /**
     * 取消收藏
     */
    fun unCollect(id: Int) {
        launch(
            block = {
                //取消收藏
                mCollectRepository.unCollect(id)
                // 取消收藏成功，更新userInfo
                mUserRepository.updateUserInfo(mUserRepository.getUserInfo()!!.apply {
                    if (collectIds.contains(id)) collectIds.remove(id)
                })
                isCollect.value = false
            },
            error = {
                isCollect.value = true
            }
        )
    }

    /**
     * 更新收藏状态
     */
    fun updateCollectState(id: Int) {
        isCollect.value = if (mUserRepository.isLogin()) {
            mUserRepository.getUserInfo()!!.collectIds.contains(id)
        } else {
            false
        }
    }

    /**
     * 保存阅读历史
     */
    fun saveReadHistory(article: Article) {
        launch(
            block = { mDetailRepository.saveReadHistory(article) }
        )
    }


}