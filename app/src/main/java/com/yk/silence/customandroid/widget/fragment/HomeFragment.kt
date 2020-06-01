package com.yk.silence.customandroid.widget.fragment

import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.databinding.FragmentHomeBinding
import com.yk.silence.customandroid.impl.ScrollToTop
import com.yk.silence.customandroid.widget.activity.MainActivity
import com.yk.silence.customandroid.widget.activity.SearchActivity
import com.yk.silence.customandroid.widget.adapter.SimpleFragmentPagerAdapter

class HomeFragment : BaseFragment<FragmentHomeBinding>(), ScrollToTop {

    private lateinit var mFragments: List<Fragment>
    private var currentOffset = 0

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun getLayoutID() = R.layout.fragment_home

    override fun initBinding(mBinding: FragmentHomeBinding) {
        super.initBinding(mBinding)
        mFragments = listOf(
            HotFragment.newInstance(),
            LastFragment.newInstance(),
            GroundFragment.newInstance(),
            ProjectFragment.newInstance(),
            PublicNOFragment.newInstance()
        )
        val mTitles = listOf<CharSequence>(
            getString(R.string.text_hot),
            getString(R.string.text_last),
            getString(R.string.text_ground),
            getString(R.string.text_project),
            getString(R.string.text_public_no)
        )
        mBinding.vpHome.adapter =
            SimpleFragmentPagerAdapter(childFragmentManager, mFragments, mTitles)
        mBinding.vpHome.offscreenPageLimit = mFragments.size
        mBinding.tabHome.setupWithViewPager(mBinding.vpHome)
        mBinding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }

        })
        mBinding.lytSearch.setOnClickListener { ActivityManager.start(SearchActivity::class.java) }

    }

    override fun scrollToTop() {
        if (!this::mFragments.isInitialized) return
        val currentFragment = mFragments[mBinding.vpHome.currentItem]
        if (currentFragment is ScrollToTop && currentFragment.isVisible) {
            currentFragment.scrollToTop()
        }
    }

}