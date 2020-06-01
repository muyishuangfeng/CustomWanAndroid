package com.yk.silence.customandroid.widget.fragment

import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.databinding.FragmentSystemBinding

class SystemFragment:BaseFragment<FragmentSystemBinding>(){

    companion object {
        fun newInstance() = SystemFragment()
    }

    override fun getLayoutID()=R.layout.fragment_system

    override fun initBinding(mBinding: FragmentSystemBinding) {
        super.initBinding(mBinding)
    }
}