package com.yk.silence.customandroid.widget.fragment

import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.databinding.FragmentGroundBinding

class GroundFragment : BaseFragment<FragmentGroundBinding>() {

    companion object {
        fun newInstance() = GroundFragment()
    }

    override fun getLayoutID() = R.layout.fragment_ground

    override fun initBinding(mBinding: FragmentGroundBinding) {
        super.initBinding(mBinding)
    }
}