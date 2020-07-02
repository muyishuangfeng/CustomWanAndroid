package com.yk.silence.customandroid.widget.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.model.Article
import kotlinx.android.synthetic.main.item_open_source.view.*

class OpenSourceAdapter : BaseQuickAdapter<Article, BaseViewHolder>(R.layout.item_open_source) {

    override fun convert(helper: BaseViewHolder, item: Article) {
        helper.itemView.run {
            tvTitle.text = item.title
            tvLink.text = item.link
        }
    }
}