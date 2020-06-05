package com.yk.silence.customandroid.viewmodel.collect

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.util.EventBus

class CollectViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    val mCollectRepository by lazy { CollectRepository() }

    //文章集合
    val mArticleList = MutableLiveData<MutableList<Article>>()

    //刷新
    val mRefreshState = MutableLiveData<Boolean>()

    //重新加载
    val mReLoadState = MutableLiveData<Boolean>()

    //加载更多
    val mLoadMoreState = MutableLiveData<LoadMoreStatus>()

    //空状态
    val mEmptyState = MutableLiveData<Boolean>()
    private var mPage = INITIAL_PAGE

    /**
     * 刷新
     */
    fun refresh() {
        mRefreshState.value = true
        mReLoadState.value = false
        mEmptyState.value = false
        launch(
            block = {
                val pagination = mCollectRepository.getCollectList(INITIAL_PAGE)
                pagination.datas.forEach { it.collect = true }
                mPage = pagination.curPage
                mArticleList.value = pagination.datas.toMutableList()
                mEmptyState.value = pagination.datas.isEmpty()
                mRefreshState.value = false
            },
            error = {
                mRefreshState.value = false
                mReLoadState.value = mPage == INITIAL_PAGE
            }
        )
    }

    /**
     * 加载更多
     */
    fun loadMore() {
        mLoadMoreState.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = mCollectRepository.getCollectList(mPage)
                pagination.datas.forEach { it.collect = true }
                val mCurrentList = mArticleList.value ?: mutableListOf()
                mCurrentList.addAll(pagination.datas)

                mArticleList.value = mCurrentList
                mLoadMoreState.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }

            },
            error = {
                mLoadMoreState.value = LoadMoreStatus.ERROR
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
                EventBus.post(Constants.USER_COLLECT_UPDATED, id to false)
            }
        )
    }

}