package com.yk.silence.customandroid.widget.fragment

import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.databinding.FragmentLastBinding

class LastFragment:BaseFragment<FragmentLastBinding>() {

    companion object {
        fun newInstance() = LastFragment()
    }

    override fun getLayoutID()=R.layout.fragment_last

    override fun initBinding(mBinding: FragmentLastBinding) {
        super.initBinding(mBinding)
    }
}