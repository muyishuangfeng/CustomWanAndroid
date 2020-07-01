package com.yk.silence.customandroid.widget.fragment

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.databinding.FragmentLastBinding
import com.yk.silence.customandroid.impl.ScrollToTop
import com.yk.silence.customandroid.ui.CommonLoadMoreView
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.home.last.LastViewModel
import com.yk.silence.customandroid.widget.activity.DetailActivity
import com.yk.silence.customandroid.widget.adapter.ArticleAdapter
import kotlinx.android.synthetic.main.include_reload.view.*

class LastFragment : BaseVMFragment<LastViewModel, FragmentLastBinding>(), ScrollToTop {

    private lateinit var mAdapter: ArticleAdapter

    companion object {
        fun newInstance() = LastFragment()
    }

    override fun getLayoutID() = R.layout.fragment_last

    override fun viewModelClass() = LastViewModel::class.java

    override fun initBinding(mBinding: FragmentLastBinding) {
        super.initBinding(mBinding)
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.refreshArticleList()
            }
        }
        mAdapter = ArticleAdapter(R.layout.item_article).apply {
            bindToRecyclerView(mBinding.recyclerView)
            setLoadMoreView(CommonLoadMoreView())
            setOnLoadMoreListener({ mViewModel.loadMoreArticleList() }, mBinding.recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = mAdapter.data[position]
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            setOnItemChildClickListener { adapter, view, position ->
                val article = mAdapter.data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        mViewModel.unCollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }
                }
            }
        }
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.refreshArticleList()
        }
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mViewModel.refreshArticleList()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            mArticleList.observe(viewLifecycleOwner, Observer {
                mAdapter.setNewData(it)
            })

            mRefreshStatus.observe(viewLifecycleOwner, Observer {
                mBinding.swipeRefreshLayout.isRefreshing = it
            })

            mReLoadStatus.observe(viewLifecycleOwner, Observer {
                mBinding.reloadView.isVisible = it
            })
            mLoadMoreStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    else -> return@Observer
                }
            })
        }
        EventBus.observe<Boolean>(Constants.USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            mViewModel.updateListCollectState()
        }
        EventBus.observe<Pair<Int, Boolean>>(Constants.USER_COLLECT_UPDATED, viewLifecycleOwner) {
            mViewModel.updateItemCollectState(it)
        }
    }

    override fun scrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }
}