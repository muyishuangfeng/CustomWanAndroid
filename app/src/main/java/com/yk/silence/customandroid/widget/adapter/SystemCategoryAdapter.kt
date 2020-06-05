package com.yk.silence.customandroid.widget.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.ext.htmlToSpanned
import com.yk.silence.customandroid.model.Category
import kotlinx.android.synthetic.main.item_system_category.view.*

/**
 * 分类体系适配器
 */
class SystemCategoryAdapter(
    layoutID: Int = R.layout.item_system_category,
    mList: ArrayList<Category>,
    var checked: Pair<Int, Int>
) : BaseQuickAdapter<Category, BaseViewHolder>(layoutID, mList) {

    var onCheckedListener: ((checked: Pair<Int, Int>) -> Unit)? = null

    override fun convert(helper: BaseViewHolder, item: Category?) {
        helper.itemView.run {
            title.text = item?.name.htmlToSpanned()
            tagFlowLayout.adapter = ItemCategoryTagAdapter(item!!.children)
            if (checked.first == helper.adapterPosition) {
                tagFlowLayout.adapter.setSelectedList(checked.second)
            }
            tagFlowLayout.setOnTagClickListener { view, position, parent ->
                checked = helper.adapterPosition to position
                notifyDataSetChanged()
                tagFlowLayout.postDelayed({
                    onCheckedListener?.invoke(checked)
                }, 300)
                true
            }


        }
    }
}