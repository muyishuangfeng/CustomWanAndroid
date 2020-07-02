package com.yk.silence.customandroid.widget.adapter

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.ext.toDateTime
import com.yk.silence.customandroid.model.PointRecord
import kotlinx.android.synthetic.main.item_mine_points.view.*

class MinePointAdapter : BaseQuickAdapter<PointRecord, BaseViewHolder>(R.layout.item_mine_points) {

    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder, item: PointRecord) {
        helper.itemView.run {
            tvReason.text = item.reason
            tvTime.text = item.date.toDateTime("YYYY-MM-dd HH:mm:ss")
            tvPoint.text = "+${item.coinCount}"
        }
    }
}