package com.yk.silence.customandroid.widget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.yk.silence.customandroid.R
import kotlinx.android.synthetic.main.item_search_history.view.*

class SearchHistoryAdapter(
    private var context: Context,
    private var layoutID: Int = R.layout.item_search_history
) :
    ListAdapter<String, SearchHistoryAdapter.SearchHistoryHolder>(HistoryDiffCallBack()) {

    var onItemClickListener: ((position: Int) -> Unit)? = null
    var onDeleteClickListener: ((position: Int) -> Unit)? = null
    var mList: MutableList<String> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHistoryHolder {
        return SearchHistoryHolder(LayoutInflater.from(context).inflate(layoutID, parent, false))
    }

    override fun onBindViewHolder(holder: SearchHistoryHolder, position: Int) {
        holder.itemView.run {
            tvLabel.text = getItem(position)
            setOnClickListener {
                onItemClickListener?.invoke(holder.adapterPosition)
            }
            ivDelete.setOnClickListener {
                onDeleteClickListener?.invoke(holder.adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(
        holder: SearchHistoryHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        onBindViewHolder(holder, position)
    }

    override fun submitList(list: MutableList<String>?) {
        mList = if (list.isNullOrEmpty()) {
            mutableListOf()
        } else {
            arrayListOf()
        }
        super.submitList(mList)
    }

    /**
     * viewHolder
     */
    class SearchHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    /**
     * 动态更新
     */
    class HistoryDiffCallBack : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem

    }
}