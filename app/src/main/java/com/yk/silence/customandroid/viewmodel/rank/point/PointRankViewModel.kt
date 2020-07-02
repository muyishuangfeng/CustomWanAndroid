package com.yk.silence.customandroid.viewmodel.rank.point

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.model.PointRank

class PointRankViewModel : BaseViewModel() {
    companion object {
        const val INITIAL_PAGE = 0
    }

    //积分排行
    private val mPointRankRepository by lazy { PointRankRepository() }

    //刷新状态
    val mRefreshState = MutableLiveData<Boolean>()

    //重新加载状态
    val mReLoadState = MutableLiveData<Boolean>()

    //加载更多状态
    val mLoadMoreState = MutableLiveData<LoadMoreStatus>()

    //积分排行集合
    val mPointRankList = MutableLiveData<MutableList<PointRank>>()
    var page = INITIAL_PAGE

    /**
     * 刷新
     */
    fun refresh() {
        mRefreshState.value = true
        mReLoadState.value = false
        launch(
            block = {
                val pagination = mPointRankRepository.getPointRank(INITIAL_PAGE)
                page = pagination.curPage
                mPointRankList.value = pagination.datas.toMutableList()
                mRefreshState.value = false
            },
            error = {
                mRefreshState.value = false
                mReLoadState.value = page == INITIAL_PAGE
            }
        )
    }

    /**
     * 加载更多
     */
    fun loadMoreRank() {
        mLoadMoreState.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = mPointRankRepository.getPointRank(page + 1)
                page = pagination.curPage
                mPointRankList.value?.addAll(pagination.datas)
                mLoadMoreState.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = { mLoadMoreState.value = LoadMoreStatus.ERROR }
        )
    }
}