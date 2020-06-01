package com.yk.silence.customandroid.widget.fragment

import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseFragment
import com.yk.silence.customandroid.databinding.FragmentProjectBinding

class ProjectFragment : BaseFragment<FragmentProjectBinding>() {

    companion object {
        fun newInstance() = ProjectFragment()
    }

    override fun getLayoutID() = R.layout.fragment_project

    override fun initBinding(mBinding: FragmentProjectBinding) {
        super.initBinding(mBinding)
    }
}