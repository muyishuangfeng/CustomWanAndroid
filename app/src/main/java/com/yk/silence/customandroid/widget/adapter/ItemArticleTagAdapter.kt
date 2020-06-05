package com.yk.silence.customandroid.widget.adapter

import android.view.LayoutInflater
import android.view.View
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.model.Article
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_nav_tag.view.*

class ItemArticleTagAdapter(private val mArticleList: List<Article>) : TagAdapter<Article>(mArticleList) {

    override fun getView(parent: FlowLayout?, position: Int, t: Article?): View {
        return LayoutInflater.from(parent?.context).inflate(R.layout.item_nav_tag, parent, false)
            .apply {
                tvTag.text = mArticleList[position].title
            }
    }
}