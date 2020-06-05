package com.yk.silence.customandroid.widget.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.model.NavigationModel
import kotlinx.android.synthetic.main.item_navigation.view.*

/**
 * 导航适配器
 */
class NavigationAdapter(layoutID: Int = R.layout.item_navigation) :
    BaseQuickAdapter<NavigationModel, BaseViewHolder>(layoutID) {

    var onItemTagClickListener: ((article: Article) -> Unit)? = null

    override fun convert(helper: BaseViewHolder, item: NavigationModel?) {
        helper.itemView.txt_item_navigation_title.text = item?.name
        helper.itemView.tag_item_navigation_flawLayout.adapter = ItemArticleTagAdapter(item!!.articles)
        helper.itemView.tag_item_navigation_flawLayout.setOnTagClickListener { _, position, _ ->
            onItemTagClickListener?.invoke(item.articles[position])
            true
        }


    }
}