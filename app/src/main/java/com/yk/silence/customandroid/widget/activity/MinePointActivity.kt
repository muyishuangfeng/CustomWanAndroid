package com.yk.silence.customandroid.widget.activity

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.databinding.ActivityMinePointBinding
import com.yk.silence.customandroid.ui.CommonLoadMoreView
import com.yk.silence.customandroid.viewmodel.rank.mine.MyRankViewModel
import com.yk.silence.customandroid.widget.adapter.MinePointAdapter
import kotlinx.android.synthetic.main.header_mine_point.view.*
import kotlinx.android.synthetic.main.include_reload.view.*

class MinePointActivity : BaseVMActivity<MyRankViewModel, ActivityMinePointBinding>() {

    private lateinit var mAdapter: MinePointAdapter
    private lateinit var mHeaderView: View

    override fun getLayoutID() = R.layout.activity_mine_point

    override fun viewModelClass() = MyRankViewModel::class.java

    @SuppressLint("InflateParams")
    override fun initBinding(mBinding: ActivityMinePointBinding) {
        super.initBinding(mBinding)
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_mine_point, null)
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }

        mAdapter = MinePointAdapter().apply {
            bindToRecyclerView(mBinding.recyclerView)
            setOnLoadMoreListener({ mViewModel.loadMoreRecord() }, mBinding.recyclerView)
            setLoadMoreView(CommonLoadMoreView())
        }
        mBinding.ivBack.setOnClickListener {
            ActivityManager.finish(MinePointActivity::class.java)
        }
        mBinding.ivRank.setOnClickListener {
            ActivityManager.start(PointRankActivity::class.java)
        }
        mBinding.reloadView.btnReload.setOnClickListener { mViewModel.refresh() }
    }

    override fun initData() {
        super.initData()
        mViewModel.refresh()
    }

    override fun observer() {
        super.observer()
        mViewModel.run {
            mRefreshState.observe(this@MinePointActivity, Observer {
                mBinding.swipeRefreshLayout.isRefreshing = it
            })
            mReLoadState.observe(this@MinePointActivity, Observer {
                mBinding.reloadView.isVisible = it
            })
            mPointRank.observe(this@MinePointActivity, Observer {
                if (mAdapter.headerLayoutCount == 0) {
                    mAdapter.setHeaderView(mHeaderView)
                }
                mHeaderView.tvTotalPoints.text = it.coinCount.toString()
                mHeaderView.tvLevelRank.text = getString(R.string.level_rank, it.level, it.rank)
            })
            mPointList.observe(this@MinePointActivity, Observer {
                mAdapter.setNewData(it)
            })
            mLoadMoreState.observe(this@MinePointActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
        }
    }


}
