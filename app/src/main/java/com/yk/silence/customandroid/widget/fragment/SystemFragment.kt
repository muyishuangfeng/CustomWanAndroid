package com.yk.silence.customandroid.widget.fragment

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.databinding.FragmentSystemBinding
import com.yk.silence.customandroid.impl.ScrollToTop
import com.yk.silence.customandroid.model.Category
import com.yk.silence.customandroid.viewmodel.system.SystemViewModel
import com.yk.silence.customandroid.widget.activity.MainActivity
import com.yk.silence.customandroid.widget.adapter.SimpleFragmentPagerAdapter
import kotlinx.android.synthetic.main.include_reload.view.*

class SystemFragment : BaseVMFragment<SystemViewModel, FragmentSystemBinding>(), ScrollToTop {

    companion object {
        fun newInstance() = SystemFragment()
    }

    private var currentOffset = 0
    private val titles = mutableListOf<String>()
    private val fragments = mutableListOf<SystemPagerFragment>()
    private var categoryFragment: SystemCategoryFragment? = null

    override fun getLayoutID() = R.layout.fragment_system

    override fun viewModelClass() = SystemViewModel::class.java

    override fun initBinding(mBinding: FragmentSystemBinding) {
        super.initBinding(mBinding)
        mBinding.reloadView.btnReload.setOnClickListener {
            mViewModel.getArticleCategory()
        }
        mBinding.ivFilter.setOnClickListener {
            categoryFragment?.show(childFragmentManager)
        }
        mBinding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })
    }



    override fun observe() {
        super.observe()
        mViewModel.run {
            mCategorys.observe(viewLifecycleOwner, Observer {
                mBinding.ivFilter.visibility = View.VISIBLE
                mBinding.tabLayout.visibility = View.VISIBLE
                mBinding.viewPager.visibility = View.VISIBLE
                setup(it)
                categoryFragment = SystemCategoryFragment.newInstance(ArrayList(it))
            })
            mLoadingStatus.observe(viewLifecycleOwner, Observer {
                mBinding.progressBar.isVisible = it
            })
            mReLoadState.observe(viewLifecycleOwner, Observer {
                mBinding.reloadView.isVisible = it
            })
        }
    }

    override fun initData() {
        mViewModel.getArticleCategory()
    }

    private fun setup(categories: MutableList<Category>) {
        titles.clear()
        fragments.clear()
        categories.forEach {
            titles.add(it.name)
            fragments.add(SystemPagerFragment.newInstance(it.children))
        }
        mBinding.viewPager.adapter = SimpleFragmentPagerAdapter(childFragmentManager, fragments, titles)
        mBinding.viewPager.offscreenPageLimit = titles.size
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }

    override fun scrollToTop() {
        if (fragments.isEmpty()) return
        fragments[mBinding.viewPager.currentItem].scrollToTop()
    }

    fun getCurrentChecked(): Pair<Int, Int> {
        if (fragments.isEmpty()) return 0 to 0
        val first = mBinding.viewPager.currentItem
        val second = fragments[mBinding.viewPager.currentItem].checkedPosition
        return first to second
    }

    fun check(position: Pair<Int, Int>) {
        if (fragments.isEmpty()) return
        mBinding.viewPager.currentItem = position.first
        fragments[position.first].check(position.second)
    }
}