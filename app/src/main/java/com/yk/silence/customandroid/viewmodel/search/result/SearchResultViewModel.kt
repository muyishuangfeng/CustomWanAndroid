package com.yk.silence.customandroid.viewmodel.search.result

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.collect.CollectRepository
import java.util.*

/**
 * 查询结果
 */
class SearchResultViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    //查询
    val mSearchResultRepository by lazy { SearchResultRepository() }

    //收藏
    val mCollectionRepository by lazy { CollectRepository() }

    //文章集合
    val mArticleList = MutableLiveData<MutableList<Article>>()

    //上拉刷新状态
    val mRefreshStatus = MutableLiveData<Boolean>()

    //重新加载状态
    val mReLoadStatus = MutableLiveData<Boolean>()

    //空状态
    val mEmptyStatus = MutableLiveData<Boolean>()

    //上拉加载状态
    val mLoadMoreStatus = MutableLiveData<LoadMoreStatus>()

    //实时关键字
    private var currentKeyWords = ""
    private var page = INITIAL_PAGE

    /**
     * 查询
     */
    fun search(keywords: String = currentKeyWords) {
        if (currentKeyWords != keywords) {
            currentKeyWords = keywords
            mArticleList.value = emptyList<Article>().toMutableList()
        }
        mRefreshStatus.value = true
        mReLoadStatus.value = false
        mEmptyStatus.value = false

        launch(block = {
            val pagination = mSearchResultRepository.search(keywords, INITIAL_PAGE)
            page = pagination.curPage
            mArticleList.value = pagination.datas.toMutableList()
            mRefreshStatus.value = false
            mEmptyStatus.value = pagination.datas.isEmpty()
        },
            error = {
                mRefreshStatus.value = false
                mReLoadStatus.value = page == INITIAL_PAGE
            }
        )

    }

    /**
     * 加载更多
     */
    fun loadMore() {
        mLoadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = mSearchResultRepository.search(currentKeyWords, INITIAL_PAGE)
                page = pagination.curPage
                val currentList = mArticleList.value?.toMutableList()
                currentList?.addAll(pagination.datas)
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
                mCollectionRepository.collect(id)
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
                mCollectionRepository.collect(id)
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
     * 更新收藏集合状态
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

}