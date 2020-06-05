package com.yk.silence.customandroid.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yk.silence.customandroid.common.APP
import com.yk.silence.customandroid.util.ScreenUtil

abstract class BaseBottomDialogFragment<V : ViewDataBinding> : BottomSheetDialogFragment() {

    protected lateinit var mBinding: V
    protected var height: Int? = null
    protected var behavior: BottomSheetBehavior<View>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater, getLayoutID(), container,
            false
        )
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(mBinding)
    }

    /**
     * 加载布局
     */
    open fun getLayoutID() = 0

    /**
     * 加载控件
     */
    open fun initBinding(mBinding: V) {
        this.mBinding = mBinding
    }


    override fun onStart() {
        super.onStart()
        val bottomSheet: View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        height?.let { behavior?.peekHeight = it }
        dialog?.window?.let {
            it.setGravity(Gravity.BOTTOM)
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, height ?: ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    /**
     * 显示
     */
    fun show(manager: FragmentManager, height: Int? = null) {
        this.height = height ?: (ScreenUtil.getScreenHeight(APP.sInstance) * 0.75f).toInt()
        if (!this.isAdded) {
            super.show(manager, "SystemCategoryFragment")
        }
    }

}