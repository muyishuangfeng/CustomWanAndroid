package com.yk.silence.customandroid.viewmodel.system.pager

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.collect.CollectRepository
import kotlinx.coroutines.Job

class SystemPageViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    val mSystemPageRepository by lazy { SystemPageRepository() }
    //收藏
    val mCollectRepository by lazy { CollectRepository() }
    //文章集合
    val mArticleList = MutableLiveData<MutableList<Article>>()
    //加载更多状态
    val mLoadMoreState = MutableLiveData<LoadMoreStatus>()
    //刷新
    val mRefreshState = MutableLiveData<Boolean>()
    //加载
    val mReLoadState = MutableLiveData<Boolean>()

    private var id: Int = -1
    private var page = INITIAL_PAGE
    //刷新协程
    private var mRefreshJob: Job? = null

    /**
     * 刷新文章集合
     */
    fun refreshArticleList(cid: Int) {
        if (cid != id) {
            cancelJob(mRefreshJob)
            id = cid
            mArticleList.value = mutableListOf()
        }
        mRefreshState.value = true
        mReLoadState.value = false
        mRefreshJob = launch(
            block = {
                val pagination = mSystemPageRepository.getArticleListByCid(INITIAL_PAGE, cid)
                page = pagination.curPage
                mArticleList.value = pagination.datas.toMutableList()
                mRefreshState.value = false
            },
            error = {
                mRefreshState.value = false
                mReLoadState.value = mArticleList.value?.isEmpty()
            }
        )
    }

    /**
     * 加载更多
     */
    fun loadMoreArticleList(cid: Int) {
        mLoadMoreState.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = mSystemPageRepository.getArticleListByCid(page, cid)
                page = pagination.curPage

                val currentList = mArticleList.value ?: mutableListOf()
                currentList.addAll(pagination.datas)

                mArticleList.value = currentList
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
     * 收藏
     */
    fun collect(id: Int) {
        launch(
            block = {
                mCollectRepository.collect(id)
                mUserRepository.updateUserInfo(mUserRepository.getUserInfo()!!.apply {
                    if (!collectIds.contains(id)) collectIds.add(id)
                })
                updateListCollectStatus()
                EventBus.post(Constants.USER_COLLECT_UPDATED, id to true)
            },
            error = {
                updateListCollectStatus()
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
                updateListCollectStatus()
                EventBus.post(Constants.USER_COLLECT_UPDATED, id to false)
            },
            error = {
                updateListCollectStatus()
            }
        )
    }

    /**
     * 更新列表收藏状态
     */
    fun updateListCollectStatus() {
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
     * 更新子条目的收藏状态
     */
    fun updateItemCollectStatus(target: Pair<Int, Boolean>) {
        val list = mArticleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        mArticleList.value = list
    }

}