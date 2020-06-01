package com.yk.silence.customandroid.widget.fragment

import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.databinding.FragmentPublicBinding

class PublicNOFragment:BaseFragment<FragmentPublicBinding>() {

    companion object {
        fun newInstance() = PublicNOFragment()
    }

    override fun getLayoutID()=R.layout.fragment_public

    override fun initBinding(mBinding: FragmentPublicBinding) {
        super.initBinding(mBinding)
    }
}