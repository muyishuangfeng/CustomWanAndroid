package com.yk.silence.customandroid.widget.fragment

import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.databinding.FragmentNavigationBinding

class NavigationFragment:BaseFragment<FragmentNavigationBinding>(){

    companion object {
        fun newInstance() = NavigationFragment()
    }

    override fun getLayoutID()=R.layout.fragment_navigation

    override fun initBinding(mBinding: FragmentNavigationBinding) {
        super.initBinding(mBinding)
    }
}