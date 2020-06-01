package com.yk.silence.customandroid.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Fragment页面适配器
 */
class SimpleFragmentPagerAdapter(
    fm: FragmentManager,
    private val mFragment: List<Fragment>,
    private val mTitles: List<CharSequence>? = null
) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    init {
        require(!(mTitles != null && mTitles.size != mFragment.size)) {
            "Fragments and titles list size must match!"
        }
    }


    override fun getItem(position: Int): Fragment = mFragment[position]

    override fun getCount(): Int = mFragment.size

    override fun getPageTitle(position: Int): CharSequence? = mTitles!![position]

    override fun getItemId(position: Int): Long {
        return mFragment[position].hashCode().toLong()
    }
}