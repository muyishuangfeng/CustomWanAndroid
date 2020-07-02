package com.yk.silence.customandroid.widget.adapter

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.model.PointRank
import kotlinx.android.synthetic.main.item_points_rank.view.*

class PointRankAdapter : BaseQuickAdapter<PointRank, BaseViewHolder>(R.layout.item_points_rank) {

    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder, item: PointRank) {
        helper.itemView.run {
            tvNo.text = "${helper.adapterPosition + 1}"
            tvName.text = item.username
            tvPoints.text = item.coinCount.toString()
        }
    }
}