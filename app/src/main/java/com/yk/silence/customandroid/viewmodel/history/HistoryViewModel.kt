package com.yk.silence.customandroid.viewmodel.history

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.collect.CollectRepository
import java.util.*

class HistoryViewModel : BaseViewModel() {

    private val mHistoryRepository by lazy { HistoryRepository() }
    private val mCollectRepository by lazy { CollectRepository() }

    //文章集合
    val mArticleList = MutableLiveData<MutableList<Article>>()

    //空状态
    val mEmptyStatus = MutableLiveData<Boolean>()

    /**
     * 获取数据
     */
    fun getData() {
        mEmptyStatus.value = false
        launch(
            block = {
                val readHistory = mHistoryRepository.searchAllHistory()
                val collectIDs = mUserRepository.getUserInfo()?.collectIds ?: emptyList<Int>()
                // 更新收藏状态
                readHistory.forEach { it.collect = collectIDs.contains(it.id) }

                mArticleList.value = readHistory.toMutableList()
                mEmptyStatus.value = readHistory.isEmpty()
            }
        )
    }

    /**
     * 收藏
     */
    fun collect(id: Int) {
        launch(
            block = {
                mCollectRepository.collect(id)
                mUserRepository.updateUserInfo(mUserRepository.getUserInfo()!!.apply {
                    if (!collectIds.contains(id)) collectIds.add(id)
                })
                updateItemCollectState(id to true)
                EventBus.post(Constants.USER_COLLECT_UPDATED, id to true)
            },
            error = {
                updateItemCollectState(id to false)
            }
        )
    }

    /**
     * 取消收藏
     */
    fun unCollect(id: Int) {
        launch(
            block = {
                mCollectRepository.unCollect(id)
                mUserRepository.updateUserInfo(mUserRepository.getUserInfo()!!.apply {
                    if (collectIds.contains(id)) collectIds.remove(id)
                })
                updateItemCollectState(id to false)
                EventBus.post(Constants.USER_COLLECT_UPDATED, id to false)
            },
            error = {
                updateItemCollectState(id to true)
            }
        )
    }

    /**
     * 更新集合的收藏状态
     */
    fun updateListCollectState() {
        val list = mArticleList.value
        if (list.isNullOrEmpty()) return
        if (mUserRepository.isLogin()) {
            val collectIDs = mUserRepository.getUserInfo()?.collectIds ?: return
            list.forEach { it.collect = collectIDs.contains(it.id) }
        } else {
            list.forEach { it.collect = false }
        }
        mArticleList.value = list
    }


    /**
     * 更新收藏状态
     */
    fun updateItemCollectState(target: Pair<Int, Boolean>) {
        val list = mArticleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        mArticleList.value = list
    }

    /**
     * 删除历史记录
     */
    fun deleteHistory(article: Article) {
        launch(
            block = {
                mHistoryRepository.deleteAllHistory(article)
            }

        )
    }

}