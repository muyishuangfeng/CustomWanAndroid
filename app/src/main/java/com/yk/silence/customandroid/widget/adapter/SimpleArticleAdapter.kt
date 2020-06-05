package com.yk.silence.customandroid.widget.adapter

import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.ext.htmlToSpanned
import com.yk.silence.customandroid.model.Article
import kotlinx.android.synthetic.main.item_article_simple.view.*

class SimpleArticleAdapter(layoutID: Int = R.layout.item_article_simple) :
    BaseQuickAdapter<Article, BaseViewHolder>(layoutID) {

    override fun convert(helper: BaseViewHolder, item: Article?) {
        helper.run {
            itemView.run {
                tv_author.text = when {
                    !item?.author.isNullOrEmpty() -> {
                        item?.author
                    }
                    item?.shareUser.isNullOrEmpty() -> {
                        item?.shareUser
                    }
                    else -> context.getString(R.string.anonymous)
                }
                tv_fresh.isVisible = item!!.fresh
                tv_title.text = item.title.htmlToSpanned()
                tv_time.text = item.niceDate
                iv_collect.isSelected = item.collect
            }
            addOnClickListener(R.id.iv_collect)
        }

    }
}