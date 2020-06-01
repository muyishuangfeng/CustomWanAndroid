package com.yk.silence.customandroid.widget.fragment

import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.databinding.FragmentDiscoverBinding


class DiscoverFragment : BaseFragment<FragmentDiscoverBinding>() {

    companion object {
        fun newInstance() = DiscoverFragment()
    }

    override fun getLayoutID() = R.layout.fragment_discover

    override fun initBinding(mBinding: FragmentDiscoverBinding) {
        super.initBinding(mBinding)

    }

}