package com.yk.silence.customandroid.widget.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMFragment
import com.yk.silence.customandroid.databinding.FragmentSearchHistoryBinding
import com.yk.silence.customandroid.model.HotWord
import com.yk.silence.customandroid.viewmodel.search.history.SearchHistoryViewModel
import com.yk.silence.customandroid.widget.activity.SearchActivity
import com.yk.silence.customandroid.widget.adapter.SearchHistoryAdapter
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_nav_tag.view.*

class SearchHistoryFragment :
    BaseVMFragment<SearchHistoryViewModel, FragmentSearchHistoryBinding>() {

    private lateinit var mAdapter: SearchHistoryAdapter

    companion object {
        fun instance() = SearchHistoryFragment()
    }

    override fun getLayoutID() = R.layout.fragment_search_history

    override fun viewModelClass() = SearchHistoryViewModel::class.java

    override fun initBinding(mBinding: FragmentSearchHistoryBinding) {
        super.initBinding(mBinding)
        mAdapter = SearchHistoryAdapter(activity!!).apply {
            mBinding.rvSearchHistory.adapter = this
            onItemClickListener = {
                (activity as SearchActivity).fillSearchInput(mList[it])
            }
            onDeleteClickListener = {
                mViewModel.deleteSearchHistory(mList[it])
            }
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getHotSearch()
        mViewModel.getSearchHistory()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            hotWords.observe(viewLifecycleOwner, Observer {
                mBinding.tvHotSearch.visibility = View.VISIBLE
                setHotWords(it)
            })
            mSearchHistory.observe(viewLifecycleOwner, Observer {
                mBinding.tvSearchHistory.isGone = it.isEmpty()
                mAdapter.submitList(it)
            })
        }
    }

    /**
     * 设置热门词汇
     */
    private fun setHotWords(hotWords: List<HotWord>) {
        mBinding.tflHotSearch.run {
            adapter = object : TagAdapter<HotWord>(hotWords) {
                override fun getView(parent: FlowLayout?, position: Int, t: HotWord?): View {
                    return LayoutInflater.from(context)
                        .inflate(R.layout.item_hot_word, parent, false).apply {
                            this.tvTag.text = t?.name
                        }
                }
            }
            setOnTagClickListener { view, position, parent ->
                (activity as SearchActivity).fillSearchInput(hotWords[position].name)
                false
            }
        }
    }


    /**
     * 添加搜索历史
     */
    fun addSearchHistory(word: String) {
        mViewModel.saveSearchHistory(word)
    }
}