package com.yk.silence.customandroid.widget.fragment

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.databinding.FragmentNavigationBinding
import com.yk.silence.customandroid.impl.ScrollToTop
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.viewmodel.navigation.NavigationViewModel
import com.yk.silence.customandroid.widget.activity.DetailActivity
import com.yk.silence.customandroid.widget.activity.MainActivity
import com.yk.silence.customandroid.widget.adapter.NavigationAdapter

class NavigationFragment : BaseVMFragment<NavigationViewModel, FragmentNavigationBinding>(),
    ScrollToTop {

    private lateinit var mAdapter: NavigationAdapter
    private var currentPosition = 0

    companion object {
        fun newInstance() = NavigationFragment()
    }

    override fun getLayoutID() = R.layout.fragment_navigation

    override fun viewModelClass() = NavigationViewModel::class.java

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initBinding(mBinding: FragmentNavigationBinding) {
        super.initBinding(mBinding)
        mBinding.srlNavigation.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.getNavigations()
            }
        }
        mAdapter = NavigationAdapter(R.layout.item_navigation).apply {
            bindToRecyclerView(mBinding.rlvNavigation)
            onItemTagClickListener = {
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(
                        DetailActivity.PARAM_ARTICLE to Article(
                            title = it.title,
                            link = it.link
                        )
                    )
                )
            }
        }
        mBinding.reloadView.setOnClickListener {
            mViewModel.getNavigations()
        }
        mBinding.rlvNavigation.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
            if (scrollY < oldScrollY) {
                mBinding.tvFloatTitle.text = mAdapter.data[currentPosition].name
            }

            val manager = mBinding.rlvNavigation.layoutManager as LinearLayoutManager
            val nextView = manager.findViewByPosition(currentPosition + 1)
            if (nextView != null) {
                mBinding.tvFloatTitle.y = if (nextView.top < mBinding.tvFloatTitle.measuredHeight) {
                    (nextView.top - mBinding.tvFloatTitle.measuredHeight).toFloat()
                } else {
                    0F
                }
            }
            currentPosition = manager.findFirstVisibleItemPosition()
            if (scrollY > oldScrollY) {
                mBinding.tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
        }
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mViewModel.getNavigations()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            mNavigationList.observe(viewLifecycleOwner, Observer {
                mBinding.tvFloatTitle.isGone = it.isEmpty()
                mBinding.tvFloatTitle.text = it[0].name
                mAdapter.setNewData(it)
            })
            mRefreshState.observe(viewLifecycleOwner, Observer {
                mBinding.srlNavigation.isRefreshing = it
            })
            mReLoadState.observe(viewLifecycleOwner, Observer {
                mBinding.reloadView.isVisible = it
            })
        }
    }


    override fun scrollToTop() {
        mBinding.rlvNavigation.smoothScrollToPosition(0)
    }

}