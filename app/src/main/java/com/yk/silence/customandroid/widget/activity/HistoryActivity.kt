package com.yk.silence.customandroid.widget.activity

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseActivity
import com.yk.silence.customandroid.base.BaseVMActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.Constants
import com.yk.silence.customandroid.databinding.ActivityHistoryBinding
import com.yk.silence.customandroid.ui.dialog.CommonDialog
import com.yk.silence.customandroid.ui.dialog.DialogFragmentHelper
import com.yk.silence.customandroid.util.EventBus
import com.yk.silence.customandroid.viewmodel.history.HistoryViewModel
import com.yk.silence.customandroid.widget.adapter.ArticleAdapter

class HistoryActivity : BaseVMActivity<HistoryViewModel, ActivityHistoryBinding>() {
    //适配器
    private lateinit var mAdapter: ArticleAdapter

    override fun getLayoutID() = R.layout.activity_history

    override fun viewModelClass() = HistoryViewModel::class.java

    override fun initBinding(mBinding: ActivityHistoryBinding) {
        super.initBinding(mBinding)
        mAdapter = ArticleAdapter().apply {
            bindToRecyclerView(mBinding.rlvHistory)
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityManager.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )

            }
            setOnItemChildClickListener { _, view, position ->
                val article = data[position]
                if (view.id == R.id.iv_collect && checkLoginState()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        mViewModel.unCollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }

                }
            }
            setOnItemLongClickListener { _, _, position ->
                DialogFragmentHelper.setOnCommonDialog(supportFragmentManager,
                    getString(R.string.text_history),
                    getString(R.string.confirm_delete_history),
                    object : CommonDialog.OnCommonCheckListener {
                        override fun onCommonCheck() {
                            mViewModel.deleteHistory(data[position])
                            mAdapter.remove(position)
                            mBinding.emptyView.isVisible = data.isEmpty()
                        }
                    })

                true
            }
        }

        mBinding.imgHistoryBack.setOnClickListener {
            ActivityManager.finish(HistoryActivity::class.java)
        }

    }


    override fun initData() {
        super.initData()
        mViewModel.getData()
    }

    override fun observer() {
        super.observer()
        mViewModel.run {
            mArticleList.observe(this@HistoryActivity, Observer {
                mAdapter.setNewData(it)
            })

            mEmptyStatus.observe(this@HistoryActivity, Observer {
                mBinding.emptyView.isVisible = it
            })
        }
        EventBus.observe<Boolean>(Constants.USER_LOGIN_STATE_CHANGED, this) {
            mViewModel.updateListCollectState()
        }
        EventBus.observe<Pair<Int, Boolean>>(Constants.USER_COLLECT_UPDATED, this) {
            mViewModel.updateItemCollectState(it)
        }
    }
}
