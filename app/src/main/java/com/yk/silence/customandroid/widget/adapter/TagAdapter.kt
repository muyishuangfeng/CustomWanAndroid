package com.yk.silence.customandroid.widget.adapter

import android.view.LayoutInflater
import android.view.View
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.model.FrequentlyModel
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_nav_tag.view.*

class TagAdapter(private val frequentlyList: List<FrequentlyModel>) :
    TagAdapter<FrequentlyModel>(frequentlyList) {

    override fun getView(parent: FlowLayout?, position: Int, t: FrequentlyModel?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_nav_tag, parent, false).apply {
                tvTag.text = frequentlyList[position].name
            }
    }
}