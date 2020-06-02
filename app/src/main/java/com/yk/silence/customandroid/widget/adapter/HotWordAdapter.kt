package com.yk.silence.customandroid.widget.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.model.HotWord
import kotlinx.android.synthetic.main.item_hot_word.view.*

/**
 * 热门词汇适配器
 */
class HotWordAdapter(layoutID: Int = R.layout.item_hot_word) :
    BaseQuickAdapter<HotWord, BaseViewHolder>(layoutID) {

    override fun convert(helper: BaseViewHolder, item: HotWord?) {
        helper.itemView.txt_item_discover_content.text = item?.name
    }
}