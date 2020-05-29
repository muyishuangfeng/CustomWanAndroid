package com.yk.silence.customandroid.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.yk.silence.customandroid.R
import kotlinx.android.synthetic.main.fragment_progress_dialog.*

/**
 * 进度对话框
 */
class ProgressDialogFragment : DialogFragment() {

    private var messageResId: Int? = null


    companion object {
        fun newInstance() = ProgressDialogFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txt_msg.text = getString(messageResId ?: R.string.text_loading)
    }

    /**
     * 显示对话框
     */
    fun show(fragmentManager: FragmentManager, messageResID: Int, cancelAble: Boolean = false) {
        this.messageResId = messageResID
        this.isCancelable = cancelAble
        show(fragmentManager, "ProgressDialogFragment")
    }
}