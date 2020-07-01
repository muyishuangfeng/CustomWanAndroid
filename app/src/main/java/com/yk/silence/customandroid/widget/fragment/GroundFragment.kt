package com.yk.silence.customandroid.widget.fragment

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.databinding.FragmentGroundBinding
import com.yk.silence.customandroid.impl.ScrollToTop
import com.yk.silence.customandroid.ui.CommonLoadMoreView
import com.yk.silence.customandroid.viewmodel.home.ground.GroundViewModel
import com.yk.silence.customandroid.widget.activity.DetailActivity
import com.yk.silence.customandroid.widget.adapter.SimpleArticleAdapter
import kotlinx.android.synthetic.main.include_reload.view.*

class GroundFragment : BaseVMFragment<GroundViewModel, FragmentGroundBinding>(), ScrollToTop {

    private lateinit var mAdapter: SimpleArticleAdapter

    companion object {
        fun newInstance() = GroundFragment()
    }

    override fun getLayoutID() = R.layout.fragment_ground

    override fun viewModelClass() = GroundViewModel::class.java

    override fun initBinding(mBinding: FragmentGroundBinding) {
        super.initBinding(mBinding)
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshUserArticleList() }
        }
        mAdapter = SimpleArticleAdapter().apply {
            bindToRecyclerView(mBinding.recyclerView)
            setLoadMoreView(CommonLoadMoreView())
            setOnLoadMoreListener({ mViewModel.loadMoreUserArticle() }, mBinding.recyclerView)
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
            mViewModel.refreshUserArticleList()
        }

    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mViewModel.refreshUserArticleList()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            mRefreshStatus.observe(viewLifecycleOwner, Observer {
                mBinding.swipeRefreshLayout.isRefreshing = it
            })
            mReLoadStatus.observe(viewLifecycleOwner, Observer {
                mBinding.reloadView.isVisible = it
            })
            mArticleList.observe(viewLifecycleOwner, Observer {
                mAdapter.setNewData(it)
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
    }

    override fun scrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }
}