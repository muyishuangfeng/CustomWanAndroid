package com.yk.silence.customandroid.widget.fragment

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.databinding.FragmentProjectBinding
import com.yk.silence.customandroid.impl.ScrollToTop
import com.yk.silence.customandroid.ui.CommonLoadMoreView
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.home.project.ProjectViewModel
import com.yk.silence.customandroid.widget.activity.DetailActivity
import com.yk.silence.customandroid.widget.adapter.ArticleAdapter
import com.yk.silence.customandroid.widget.adapter.CategorySubAdapter
import kotlinx.android.synthetic.main.include_reload.view.*
import kotlin.time.milliseconds

class ProjectFragment : BaseVMFragment<ProjectViewModel, FragmentProjectBinding>(), ScrollToTop {

    private lateinit var mAdapter: ArticleAdapter
    private lateinit var mCategoryAdapter: CategorySubAdapter

    companion object {
        fun newInstance() = ProjectFragment()
    }

    override fun getLayoutID() = R.layout.fragment_project

    override fun viewModelClass() = ProjectViewModel::class.java

    override fun initBinding(mBinding: FragmentProjectBinding) {
        super.initBinding(mBinding)
        mBinding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshProjectList() }
        }
        mAdapter = ArticleAdapter().apply {
            bindToRecyclerView(mBinding.recyclerView)
            setLoadMoreView(CommonLoadMoreView())
            setOnLoadMoreListener({ mViewModel.loadMoreProjectList() }, mBinding.recyclerView)
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
        mCategoryAdapter = CategorySubAdapter().apply {
            bindToRecyclerView(mBinding.rvCategory)
            onCheckedListener = {
                mViewModel.refreshProjectList(it)
            }
        }

        mBinding.reloadListView.btnReload.setOnClickListener {
            mViewModel.refreshProjectList()
        }

        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.getProjectCategories()
        }

    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mViewModel.getProjectCategories()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            mRefreshStatus.observe(viewLifecycleOwner, Observer {
                mBinding.swipeRefreshLayout.isRefreshing = it
            })
            mReloadListStatus.observe(viewLifecycleOwner, Observer {
                mBinding.reloadListView.isVisible = it
            })
            mReloadStatus.observe(viewLifecycleOwner, Observer {
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
            mCheckedCategory.observe(viewLifecycleOwner, Observer {
                mCategoryAdapter.check(it)
            })
            mCategoryList.observe(viewLifecycleOwner, Observer {
                mBinding.rvCategory.isGone = it.isEmpty()
                mCategoryAdapter.setNewData(it)
            })

            mArticleList.observe(viewLifecycleOwner, Observer {
                mAdapter.setNewData(it)
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