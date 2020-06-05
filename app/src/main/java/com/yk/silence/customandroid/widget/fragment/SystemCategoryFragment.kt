package com.yk.silence.customandroid.widget.fragment

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseBottomDialogFragment
import com.yk.silence.customandroid.databinding.FragmentSystemCategoryBinding
import com.yk.silence.customandroid.model.Category
import com.yk.silence.customandroid.widget.adapter.SystemCategoryAdapter

class SystemCategoryFragment : BaseBottomDialogFragment<FragmentSystemCategoryBinding>() {

    companion object {
        const val CATEGORY_LIST = "categoryList"
        fun newInstance(categoryList: ArrayList<Category>): SystemCategoryFragment {
            return SystemCategoryFragment().apply {
                arguments = Bundle().apply { putParcelableArrayList(CATEGORY_LIST, categoryList) }
            }
        }

    }

    override fun getLayoutID() = R.layout.fragment_system_category

    override fun initBinding(mBinding: FragmentSystemCategoryBinding) {
        super.initBinding(mBinding)
        val categoryList: ArrayList<Category> = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        val checked = (parentFragment as SystemFragment).getCurrentChecked()
        Log.e("TAG",""+categoryList.size+"==="+checked)
        SystemCategoryAdapter(R.layout.item_system_category, categoryList, checked).run {
            bindToRecyclerView(mBinding.rlvSystemCategory)
            onCheckedListener = {
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                view?.postDelayed({
                    (parentFragment as SystemFragment).check(it)
                }, 300)
            }

        }
        view?.post {
            (mBinding.rlvSystemCategory.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(checked.first, 0)
        }
    }
}