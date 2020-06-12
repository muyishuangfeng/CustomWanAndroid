package com.yk.silence.customandroid.widget.activity

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.databinding.ActivityShareBinding
import com.yk.silence.customandroid.ext.hideSoftInput
import com.yk.silence.customandroid.ext.showToast
import com.yk.silence.customandroid.viewmodel.share.ShareViewModel

class ShareActivity : BaseVMActivity<ShareViewModel, ActivityShareBinding>() {

    override fun getLayoutID() = R.layout.activity_share

    override fun viewModelClass() = ShareViewModel::class.java

    override fun initBinding(mBinding: ActivityShareBinding) {
        super.initBinding(mBinding)
        mBinding.ivBack.setOnClickListener { ActivityManager.finish(ShareActivity::class.java) }
        mBinding.acetTitle.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                mBinding.tvSubmit.performClick()
                true
            } else {
                false
            }
        }

        mBinding.tvSubmit.setOnClickListener {
            val title = mBinding.acetTitle.text.toString()
            val link = mBinding.acetlink.text.toString()
            if (title.isEmpty()) {
                showToast(R.string.title_toast)
                return@setOnClickListener
            }

            if (link.isEmpty()) {
                showToast(R.string.link_toast)
                return@setOnClickListener
            }
            mBinding.tvSubmit.hideSoftInput()
            mViewModel.shareArticle(title, link)

        }

    }

    override fun initData() {
        super.initData()
        mViewModel.getUserInfo()
    }

    override fun observer() {
        super.observer()
        mViewModel.run {

            mUserInfo.observe(this@ShareActivity, Observer {
                val sharePeople = if (it.username.isEmpty()) it.username else it.nickname
                mBinding.acetSharePeople.setText(sharePeople)
            })

            mSubmitting.observe(this@ShareActivity, Observer {
                if (it) showProgressDialog(R.string.sharing_article) else hideProgressDialog()
            })

            mShareResult.observe(this@ShareActivity, Observer {
                if (it) {
                    showToast(R.string.share_article_success)
                }
            })
        }
    }
}
