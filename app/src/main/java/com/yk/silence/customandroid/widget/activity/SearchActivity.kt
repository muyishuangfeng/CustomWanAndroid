package com.yk.silence.customandroid.widget.activity

import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.databinding.ActivitySearchBinding
import com.yk.silence.customandroid.ext.hideSoftInput
import com.yk.silence.customandroid.widget.fragment.SearchHistoryFragment
import com.yk.silence.customandroid.widget.fragment.SearchResultFragment

class SearchActivity : BaseActivity<ActivitySearchBinding>() {

    override fun getLayoutID() = R.layout.activity_search

    override fun initBinding(mBinding: ActivitySearchBinding) {
        super.initBinding(mBinding)
        val mResultFragment = SearchResultFragment.instance()
        val mHistoryFragment = SearchHistoryFragment.instance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.flContainer, mHistoryFragment)
            .add(R.id.flContainer, mResultFragment)
            .show(mHistoryFragment)
            .hide(mResultFragment)
            .commit()
        mBinding.imgSearchBack.setOnClickListener {
            if (mResultFragment.isVisible) {
                supportFragmentManager
                    .beginTransaction()
                    .hide(mResultFragment)
                    .commit()
            } else {
                ActivityManager.finish(SearchActivity::class.java)
            }
        }
        mBinding.imgSearchDone.setOnClickListener {
            val mSearchContent = mBinding.edtSearchContent.text.toString()
            if (mSearchContent.isEmpty()) return@setOnClickListener
            it.hideSoftInput()
            mHistoryFragment.addSearchHistory(mSearchContent)
            mResultFragment.doSearch(mSearchContent)
            supportFragmentManager
                .beginTransaction()
                .show(mResultFragment)
                .commit()
        }
        mBinding.edtSearchContent.run {
            addTextChangedListener(afterTextChanged = {
                mBinding.imgSearchClear.isGone = it.isNullOrEmpty()
            })
            setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mBinding.imgSearchDone.performClick()
                    true
                } else {
                    false
                }
            }
        }
        mBinding.imgSearchClear.setOnClickListener {
            mBinding.edtSearchContent.setText("")
        }

    }


    fun fillSearchInput(word: String) {
        mBinding.edtSearchContent.setText(word)
        mBinding.edtSearchContent.setSelection(word.length)
    }

    override fun onBackPressed() {
        mBinding.imgSearchBack.performClick()
    }

}
