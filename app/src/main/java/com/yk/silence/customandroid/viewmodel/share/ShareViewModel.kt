package com.yk.silence.customandroid.viewmodel.share

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.model.UserModel

class ShareViewModel : BaseViewModel() {
    private val mShareRepository by lazy { ShareRepository() }

    //用户
    val mUserInfo = MutableLiveData<UserModel>()

    //分享结果
    val mShareResult = MutableLiveData<Boolean>()

    //提交
    val mSubmitting = MutableLiveData<Boolean>()

    /**
     * 获取用户信息
     */
    fun getUserInfo() {
        mUserInfo.value = mUserRepository.getUserInfo()
    }

    /**
     * 分享文章
     */
    fun shareArticle(title: String, link: String) {
        mSubmitting.value = true
        launch(
            block = {
                mShareRepository.shareArticle(title, link)
                mShareResult.value = true
                mSubmitting.value = false
            },
            error = {
                mShareResult.value = false
                mSubmitting.value = false
            }
        )

    }
}