package com.yk.silence.customandroid.widget.activity

import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.databinding.ActivityOpenSourceBinding
import com.yk.silence.customandroid.ext.OpenSource
import com.yk.silence.customandroid.widget.adapter.OpenSourceAdapter

class OpenSourceActivity : BaseActivity<ActivityOpenSourceBinding>() {


    override fun getLayoutID() = R.layout.activity_open_source

    override fun initBinding(mBinding: ActivityOpenSourceBinding) {
        super.initBinding(mBinding)
        OpenSourceAdapter().apply {
            bindToRecyclerView(mBinding.recyclerView)
            setNewData(OpenSource.openSourceData)
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
        }
        mBinding.ivBack.setOnClickListener { ActivityManager.finish(OpenSourceActivity::class.java) }
    }
}
