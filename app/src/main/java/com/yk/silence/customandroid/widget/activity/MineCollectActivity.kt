package com.yk.silence.customandroid.widget.activity

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.databinding.ActivityMineCollectBinding
import com.yk.silence.customandroid.ui.CommonLoadMoreView
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.collect.CollectViewModel
import com.yk.silence.customandroid.widget.adapter.ArticleAdapter
import kotlinx.android.synthetic.main.include_reload.view.*

class MineCollectActivity : BaseVMActivity<CollectViewModel, ActivityMineCollectBinding>() {


    private lateinit var mAdapter: ArticleAdapter

    override fun getLayoutID() = R.layout.activity_mine_collect

    override fun viewModelClass() = CollectViewModel::class.java

    override fun initBinding(mBinding: ActivityMineCollectBinding) {
        super.initBinding(mBinding)
        mAdapter = ArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(mBinding.recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityManager.start(
                    DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            setOnItemChildClickListener { _, view, position ->
                val article = data[position]
                if (view.id == R.id.iv_collect) {
                    mViewModel.unCollect(article.originId)
                    removeItem(position)
                }
            }
            setOnLoadMoreListener({ mViewModel.loadMore() }, mBinding.recyclerView)
        }
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.refresh()
        }
        mBinding.ivBack.setOnClickListener { ActivityManager.finish(MineCollectActivity::class.java) }
    }

    override fun initData() {
        super.initData()
        mViewModel.refresh()
    }

    override fun observer() {
        super.observer()
        mViewModel.run {
            mArticleList.observe(this@MineCollectActivity, Observer {
                mAdapter.setNewData(it)
            })
            mRefreshState.observe(this@MineCollectActivity, Observer {
                mBinding.swipeRefreshLayout.isRefreshing = it
            })
            mEmptyState.observe(this@MineCollectActivity, Observer {
                mBinding.emptyView.isVisible = it
            })
            mLoadMoreState.observe(this@MineCollectActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
            mReLoadState.observe(this@MineCollectActivity, Observer {
                mBinding.reloadView.isVisible = it
            })
        }
        EventBus.observe<Pair<Int, Boolean>>(
            Constants.USER_COLLECT_UPDATED,
            this
        ) { (id, collect) ->
            if (collect) {
                mViewModel.refresh()
            } else {
                val position = mAdapter.data.indexOfFirst { it.originId == id }
                if (position != -1) {
                    removeItem(position)
                }
            }
        }
    }

    private fun removeItem(position: Int) {
        mAdapter.remove(position)
        mBinding.emptyView.isVisible = mAdapter.data.isEmpty()
    }
}
