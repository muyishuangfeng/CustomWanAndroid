package com.yk.silence.customandroid.widget.adapter

import android.view.LayoutInflater
import android.view.View
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.model.Article
import com.yk.silence.customandroid.model.Category
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_nav_tag.view.*

class ItemCategoryTagAdapter(private val mCategoryList: List<Category>) : TagAdapter<Category>(mCategoryList) {

    override fun getView(parent: FlowLayout?, position: Int, t: Category?): View {
        return LayoutInflater.from(parent?.context).inflate(R.layout.item_system_category_tag, parent, false)
            .apply {
                tvTag.text = mCategoryList[position].name
            }
    }

    override fun setSelected(position: Int, t: Category?): Boolean {
        return super.setSelected(position, t)
    }
}