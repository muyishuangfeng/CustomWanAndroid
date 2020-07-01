package com.yk.silence.customandroid.widget.fragment

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.databinding.FragmentSearchResultBinding
import com.yk.silence.customandroid.ui.CommonLoadMoreView
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.search.history.SearchHistoryViewModel
import com.yk.silence.customandroid.viewmodel.search.result.SearchResultViewModel
import com.yk.silence.customandroid.widget.activity.DetailActivity
import com.yk.silence.customandroid.widget.adapter.ArticleAdapter
import kotlinx.android.synthetic.main.include_reload.view.*
import kotlinx.coroutines.delay

class SearchResultFragment :
    BaseVMFragment<SearchResultViewModel, FragmentSearchResultBinding>() {

    private lateinit var mAdapter: ArticleAdapter

    companion object {
        fun instance() = SearchResultFragment()
    }

    override fun getLayoutID() = R.layout.fragment_search_result

    override fun viewModelClass() = SearchResultViewModel::class.java

    override fun initBinding(mBinding: FragmentSearchResultBinding) {
        super.initBinding(mBinding)
        mAdapter = ArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(mBinding.recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            setOnItemChildClickListener { adapter, view, position ->
                val article = data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        mViewModel.unCollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }
                }
            }
            setOnLoadMoreListener({ mViewModel.loadMore() }, mBinding.recyclerView)
        }
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.search() }
        }
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.search()
        }

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
            mEmptyStatus.observe(viewLifecycleOwner, Observer {
                mBinding.emptyView.isVisible = it
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

            EventBus.observe<Boolean>(Constants.USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
                mViewModel.updateListCollectState()
            }
            EventBus.observe<Pair<Int, Boolean>>(
                Constants.USER_COLLECT_UPDATED,
                viewLifecycleOwner
            ) {
                mViewModel.updateItemCollectState(it)
            }


        }

    }

    /**
     * 查询
     */
    fun doSearch(words: String) {
        mViewModel.search(words)
    }
}