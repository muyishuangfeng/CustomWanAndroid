package com.yk.silence.customandroid.widget.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.ViewPropertyAnimator
import androidx.fragment.app.Fragment
import com.google.android.material.animation.AnimationUtils
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseActivity
import com.yk.silence.customandroid.databinding.ActivityMainBinding
import com.yk.silence.customandroid.impl.ScrollToTop
import com.yk.silence.customandroid.util.showToast
import com.yk.silence.customandroid.widget.fragment.*


class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var mFragments: Map<Int, Fragment>
    private var bottomNavigationViewAnimator: ViewPropertyAnimator? = null
    private var currentBottomNavigationState = true
    private var previousTimeMillis = 0L


    override fun getLayoutID() = R.layout.activity_main

    override fun initSaveState(mBinding: ActivityMainBinding, savedInstanceState: Bundle?) {
        super.initSaveState(mBinding, savedInstanceState)
        mFragments = mapOf(
            R.id.home to createFragment(HomeFragment::class.java),
            R.id.system to createFragment(SystemFragment::class.java),
            R.id.discovery to createFragment(DiscoverFragment::class.java),
            R.id.navigation to createFragment(NavigationFragment::class.java),
            R.id.mine to createFragment(MineFragment::class.java)
        )
        mBinding.bnvMain.run {
            setOnNavigationItemSelectedListener {
                showFragment(it.itemId)
                true
            }
            setOnNavigationItemReselectedListener { menuItem ->
                val fragment = mFragments.entries.find { it.key == menuItem.itemId }?.value
                if (fragment is ScrollToTop)
                    fragment.scrollToTop()
            }
        }
        if (savedInstanceState == null) {
            val initialItemId = R.id.home
            mBinding.bnvMain.selectedItemId = initialItemId
            showFragment(initialItemId)
        }
    }

    override fun onBackPressed() {
        val currentTimMillis = System.currentTimeMillis()
        if (currentTimMillis - previousTimeMillis < 2000) {
            super.onBackPressed()
        } else {
            showToast(R.string.press_again_to_exit)
            previousTimeMillis = currentTimMillis
        }
    }


    /**
     * 创建fragment
     */
    private fun createFragment(clazz: Class<out Fragment>): Fragment {
        var fragment = supportFragmentManager.fragments.find { it.javaClass == clazz }
        if (fragment == null) {
            fragment = when (clazz) {
                HomeFragment::class.java -> HomeFragment.newInstance()
                SystemFragment::class.java -> SystemFragment.newInstance()
                DiscoverFragment::class.java -> DiscoverFragment.newInstance()
                NavigationFragment::class.java -> NavigationFragment.newInstance()
                MineFragment::class.java -> MineFragment.newInstance()
                else -> throw IllegalArgumentException("argument${clazz.simpleName} is illega")
            }
        }
        return fragment
    }

    /**
     * 显示fragment
     */
    private fun showFragment(menuID: Int) {
        val currentFragment = supportFragmentManager.fragments.find {
            it.isVisible && it in mFragments.values
        }
        val targetFragment = mFragments.entries.find { it.key == menuID }?.value
        supportFragmentManager.beginTransaction().apply {
            currentFragment?.let { if (it.isVisible) hide(it) }
            targetFragment?.let {
                if (it.isAdded) show(it) else add(R.id.fly_container, it)
            }
        }.commit()
    }

    /**
     * 动画效果
     */
    fun animateBottomNavigationView(show: Boolean) {
        if (currentBottomNavigationState == show) {
            return
        }
        if (bottomNavigationViewAnimator != null) {
            bottomNavigationViewAnimator?.cancel()
            mBinding.bnvMain.clearAnimation()
        }
        currentBottomNavigationState = show
        val targetY = if (show) 0F else mBinding.bnvMain.measuredHeight.toFloat()
        val duration = if (show) 225L else 175L
        bottomNavigationViewAnimator = mBinding.bnvMain.animate()
            .translationY(targetY)
            .setDuration(duration)
            .setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    bottomNavigationViewAnimator = null
                }
            })
    }

    override fun onDestroy() {
        bottomNavigationViewAnimator?.cancel()
        mBinding.bnvMain.clearAnimation()
        bottomNavigationViewAnimator = null
        super.onDestroy()
    }
}
