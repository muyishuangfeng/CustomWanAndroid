package com.yk.silence.customandroid.widget.activity

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.databinding.ActivityPointRankBinding
import com.yk.silence.customandroid.ui.CommonLoadMoreView
import com.yk.silence.customandroid.viewmodel.rank.point.PointRankViewModel
import com.yk.silence.customandroid.widget.adapter.PointRankAdapter
import kotlinx.android.synthetic.main.include_reload.view.*

class PointRankActivity : BaseVMActivity<PointRankViewModel, ActivityPointRankBinding>() {

    private lateinit var mAdapter: PointRankAdapter

    override fun getLayoutID() = R.layout.activity_point_rank

    override fun viewModelClass() = PointRankViewModel::class.java

    override fun initBinding(mBinding: ActivityPointRankBinding) {
        super.initBinding(mBinding)
        mAdapter = PointRankAdapter().apply {
            bindToRecyclerView(mBinding.recyclerView)
            setLoadMoreView(CommonLoadMoreView())
            setOnLoadMoreListener({ mViewModel.loadMoreRank() }, mBinding.recyclerView)
        }
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }
        mBinding.ivBack.setOnClickListener { ActivityManager.finish(PointRankActivity::class.java) }
        mBinding.tvTitle.setOnClickListener { mBinding.recyclerView.smoothScrollToPosition(0) }
        mBinding.reloadView.btnReload.setOnClickListener { mViewModel.refresh() }
    }

    override fun initData() {
        super.initData()
        mViewModel.refresh()
    }

    override fun observer() {
        super.observer()
        mViewModel.run {
            mRefreshState.observe(this@PointRankActivity, Observer {
                mBinding.swipeRefreshLayout.isRefreshing = it
            })
            mReLoadState.observe(this@PointRankActivity, Observer {
                mBinding.reloadView.isVisible = it
            })
            mPointRankList.observe(this@PointRankActivity, Observer {
                mAdapter.setNewData(it)
            })
            mLoadMoreState.observe(this@PointRankActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    else -> return@Observer
                }
            })
        }
    }
}
