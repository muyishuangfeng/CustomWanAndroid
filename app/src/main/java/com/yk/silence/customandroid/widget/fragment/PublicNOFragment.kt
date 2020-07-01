package com.yk.silence.customandroid.widget.fragment

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.databinding.FragmentPublicBinding
import com.yk.silence.customandroid.impl.ScrollToTop
import com.yk.silence.customandroid.ui.CommonLoadMoreView
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.home.wechat.PublicNOViewModel
import com.yk.silence.customandroid.widget.activity.DetailActivity
import com.yk.silence.customandroid.widget.adapter.CategorySubAdapter
import com.yk.silence.customandroid.widget.adapter.SimpleArticleAdapter
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.include_reload.view.*

class PublicNOFragment : BaseVMFragment<PublicNOViewModel, FragmentPublicBinding>(), ScrollToTop {

    private lateinit var mAdapter: SimpleArticleAdapter
    private lateinit var mCategoryAdapter: CategorySubAdapter

    companion object {
        fun newInstance() = PublicNOFragment()
    }

    override fun getLayoutID() = R.layout.fragment_public

    override fun viewModelClass() = PublicNOViewModel::class.java


    override fun initBinding(mBinding: FragmentPublicBinding) {
        super.initBinding(mBinding)
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshPublicNOArticleList() }
        }
        mCategoryAdapter = CategorySubAdapter().apply {
            bindToRecyclerView(mBinding.rvCategory)
            onCheckedListener = {
                mViewModel.refreshPublicNOArticleList(it)
            }
        }
        mAdapter = SimpleArticleAdapter().apply {
            bindToRecyclerView(mBinding.recyclerView)
            setLoadMoreView(CommonLoadMoreView())
            setOnLoadMoreListener({ mViewModel.loadMorePublicNOArticle() }, mBinding.recyclerView)
            setOnItemClickListener { adapter, view, position ->
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
            mViewModel.getPublicNOCategories()
        }
        mBinding.reloadListView.btnReload.setOnClickListener {
            mViewModel.refreshPublicNOArticleList()
        }

    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mViewModel.getPublicNOCategories()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            mRefreshStatus.observe(viewLifecycleOwner, Observer {
                mBinding.swipeRefreshLayout.isRefreshing = it
            })
            mReloadStatus.observe(viewLifecycleOwner, Observer {
                mBinding.reloadView.isVisible = it
            })
            mReloadListStatus.observe(viewLifecycleOwner, Observer {
                mBinding.reloadListView.isVisible = it
            })
            mCategoryList.observe(viewLifecycleOwner, Observer {
                mBinding.rvCategory.isGone = it.isEmpty()
                mCategoryAdapter.setNewData(it)
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