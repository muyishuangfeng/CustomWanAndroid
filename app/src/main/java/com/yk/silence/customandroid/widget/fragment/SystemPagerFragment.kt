package com.yk.silence.customandroid.widget.fragment

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.common.LoadMoreStatus
import com.yk.silence.customandroid.databinding.FragmentSystemPagerBinding
import com.yk.silence.customandroid.ext.toIntPx
import com.yk.silence.customandroid.impl.ScrollToTop
import com.yk.silence.customandroid.model.Category
import com.yk.silence.customandroid.ui.CommonLoadMoreView
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.system.pager.SystemPageViewModel
import com.yk.silence.customandroid.widget.activity.DetailActivity
import com.yk.silence.customandroid.widget.adapter.CategorySubAdapter
import com.yk.silence.customandroid.widget.adapter.SimpleArticleAdapter
import kotlinx.android.synthetic.main.include_reload.view.*

class SystemPagerFragment : BaseVMFragment<SystemPageViewModel, FragmentSystemPagerBinding>(),
    ScrollToTop {

    companion object {
        const val CATEGORY_LIST = "CATEGORY_LIST"

        fun newInstance(categoryList: ArrayList<Category>): SystemPagerFragment {
            return SystemPagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_LIST, categoryList)
                }
            }
        }
    }

    private lateinit var categoryList: List<Category>
    var checkedPosition = 0
    private lateinit var mAdapterSimple: SimpleArticleAdapter
    private lateinit var categoryAdapter: CategorySubAdapter

    override fun getLayoutID() = R.layout.fragment_system_pager

    override fun viewModelClass() = SystemPageViewModel::class.java

    override fun initBinding(mBinding: FragmentSystemPagerBinding) {
        super.initBinding(mBinding)
        mBinding. srlSystemPage.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }

        categoryList = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        checkedPosition = 0
        categoryAdapter = CategorySubAdapter(R.layout.item_category_sub).apply {
            bindToRecyclerView(mBinding.rlvSystemPage)
            setNewData(categoryList)
            onCheckedListener = {
                checkedPosition = it
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }

        mAdapterSimple = SimpleArticleAdapter(R.layout.item_article_simple).apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(mBinding.recyclerView)
            setOnLoadMoreListener({
                mViewModel.loadMoreArticleList(categoryList[checkedPosition].id)
            }, mBinding.recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = mAdapterSimple.data[position]
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            setOnItemChildClickListener { _, view, position ->
                val article = mAdapterSimple.data[position]
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
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            mArticleList.observe(viewLifecycleOwner, Observer {
                mAdapterSimple.setNewData(it)
            })
            mRefreshState.observe(viewLifecycleOwner, Observer {
                mBinding.srlSystemPage.isRefreshing = it
            })
            mLoadMoreState.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapterSimple.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapterSimple.loadMoreFail()
                    LoadMoreStatus.END -> mAdapterSimple.loadMoreEnd()
                    else -> return@Observer
                }
            })
            mReLoadState.observe(viewLifecycleOwner, Observer {
                mBinding.reloadView.isVisible = it
            })
        }
        EventBus.observe<Boolean>(Constants.USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            mViewModel.updateListCollectStatus()
        }
        EventBus.observe<Pair<Int, Boolean>>(Constants.USER_COLLECT_UPDATED, viewLifecycleOwner) {
            mViewModel.updateItemCollectStatus(it)
        }
    }

    override fun lazyLoadData() {
        mViewModel.refreshArticleList(categoryList[checkedPosition].id)
    }

    override fun scrollToTop() {
        mBinding.recyclerView.smoothScrollToPosition(0)
    }

    fun check(position: Int) {
        if (position != checkedPosition) {
            checkedPosition = position
            categoryAdapter.check(position)
            (mBinding.rlvSystemPage.layoutManager as? LinearLayoutManager)
                ?.scrollToPositionWithOffset(position, 8f.toIntPx())
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
    }
}