package com.yk.silence.customandroid.viewmodel.home.last

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.collect.CollectRepository
import com.yk.silence.customandroid.viewmodel.home.hot.HotViewModel

class LastViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    //最新数据
    private val mLastRepository by lazy { LastRepository() }

    //收藏
    private val mCollectRepository by lazy { CollectRepository() }

    //文章集合
    val mArticleList = MutableLiveData<MutableList<Article>>(mutableListOf())

    //刷新状态
    val mRefreshStatus = MutableLiveData<Boolean>()

    //重新加载状态
    val mReLoadStatus = MutableLiveData<Boolean>()

    //加载更多
    val mLoadMoreStatus = MutableLiveData<LoadMoreStatus>()

    private var page = HotViewModel.INITIAL_PAGE

    /**
     * 刷新文章集合
     */
    fun refreshArticleList() {
        mRefreshStatus.value = true
        mReLoadStatus.value = false
        launch(
            block = {
                val pagination = mLastRepository.getLastProjectList(INITIAL_PAGE)
                page = pagination.curPage
                mArticleList.value = pagination.datas.toMutableList()
                mRefreshStatus.value = false
            },
            error = {
                mRefreshStatus.value = false
                mReLoadStatus.value = page == INITIAL_PAGE
            }
        )

    }

    /**
     * 加载更多数据
     */
    fun loadMoreArticleList() {
        mLoadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = mLastRepository.getLastProjectList(page)
                page = pagination.curPage
                val currentList = mArticleList.value ?: mutableListOf()
                currentList.addAll(pagination.datas)
                mArticleList.value = currentList
                mLoadMoreStatus.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = {
                mLoadMoreStatus.value = LoadMoreStatus.ERROR
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
     * 更新列表收藏状态
     */
    fun updateListCollectState() {
        val list = mArticleList.value
        if (list.isNullOrEmpty()) return
        if (mUserRepository.isLogin()) {
            val collectIds = mUserRepository.getUserInfo()?.collectIds ?: return
            list.forEach { it.collect = collectIds.contains(it.id) }
        } else {
            list.forEach { it.collect = false }
        }
        mArticleList.value = list
    }

    /**
     * 更新Item的收藏状态
     */
    fun updateItemCollectState(target: Pair<Int, Boolean>) {
        val list = mArticleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        mArticleList.value = list
    }
}