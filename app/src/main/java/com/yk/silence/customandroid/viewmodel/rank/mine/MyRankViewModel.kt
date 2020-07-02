package com.yk.silence.customandroid.viewmodel.rank.mine

import androidx.lifecycle.MutableLiveData
import com.yk.silence.customandroid.base.BaseViewModel
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.model.PointRank
import com.yk.silence.customandroid.model.PointRecord

class MyRankViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    //积分排行
    private val mMyRankRepository by lazy { MyRankRepository() }

    //积分记录集合
    val mPointList = MutableLiveData<MutableList<PointRecord>>()

    //积分排行
    val mPointRank = MutableLiveData<PointRank>()

    //刷新状态
    val mRefreshState = MutableLiveData<Boolean>()

    //重新加载状态
    val mReLoadState = MutableLiveData<Boolean>()

    //加载更多状态
    val mLoadMoreState = MutableLiveData<LoadMoreStatus>()

    var page = INITIAL_PAGE

    /**
     * 刷新
     */
    fun refresh() {
        mRefreshState.value = true
        mReLoadState.value = false
        launch(
            block = {
                val points = mMyRankRepository.getMyPoint()
                val pagination = mMyRankRepository.getPointRecord(INITIAL_PAGE)
                page = pagination.curPage

                mPointRank.value = points
                mPointList.value = pagination.datas.toMutableList()
                mRefreshState.value = false
            },
            error = {
                mRefreshState.value = false
                mReLoadState.value = page == INITIAL_PAGE
            }
        )

    }

    /**
     * 加载更多记录
     */
    fun loadMoreRecord() {
        mLoadMoreState.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = mMyRankRepository.getPointRecord(page + 1)
                page = pagination.curPage
                mPointList.value?.addAll(pagination.datas)
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