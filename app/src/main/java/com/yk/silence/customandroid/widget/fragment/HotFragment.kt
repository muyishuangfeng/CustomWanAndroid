package com.yk.silence.customandroid.widget.fragment

import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.databinding.FragmentHotBinding

class HotFragment:BaseFragment<FragmentHotBinding>() {

    companion object {
        fun newInstance() = HotFragment()
    }

    override fun getLayoutID() = R.layout.fragment_hot


    override fun initBinding(mBinding: FragmentHotBinding) {
        super.initBinding(mBinding)
    }
}