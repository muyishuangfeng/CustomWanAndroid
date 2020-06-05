package com.yk.silence.customandroid.widget.adapter

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.ext.htmlToSpanned
import com.yk.silence.customandroid.ext.toIntPx
import com.yk.silence.customandroid.model.Category
import kotlinx.android.synthetic.main.item_category_sub.view.*

/**
 * 分类适配器
 */
class CategorySubAdapter(layoutID: Int = R.layout.item_category_sub) :
    BaseQuickAdapter<Category, BaseViewHolder>(layoutID) {

    private var mCheckPosition = 0
    var onCheckedListener: ((position: Int) -> Unit)? = null

    override fun convert(helper: BaseViewHolder, item: Category?) {
        helper.itemView.run {
            ctvCategory.text = item?.name.htmlToSpanned()
            ctvCategory.isChecked = mCheckPosition == helper.adapterPosition
            setOnClickListener {
                val position = helper.adapterPosition
                check(position)
                onCheckedListener?.invoke(position)
            }
            updateLayoutParams<ViewGroup.MarginLayoutParams> {
                marginStart = if (helper.adapterPosition == 0) 8f.toIntPx() else 0f.toIntPx()
            }

        }
    }


    fun check(position: Int) {
        mCheckPosition = position
        notifyDataSetChanged()
    }

}