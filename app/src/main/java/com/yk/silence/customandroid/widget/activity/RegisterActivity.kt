package com.yk.silence.customandroid.widget.activity

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.databinding.ActivityRegisterBinding
import com.yk.silence.customandroid.viewmodel.register.RegisterViewModel
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseVMActivity<RegisterViewModel,ActivityRegisterBinding>() {

    override fun getLayoutID() = R.layout.activity_register

    override fun viewModelClass() = RegisterViewModel::class.java

    override fun initBinding(mBinding: ActivityRegisterBinding) {
        super.initBinding(mBinding)
        mBinding.imgRegisterClose.setOnClickListener { ActivityManager.finish(RegisterActivity::class.java) }

        mBinding.edtConfirmPassword.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                btn_register.performClick()
                true
            } else {
                false
            }
        }

        mBinding.btnRegister.setOnClickListener {
            mBinding.txtAccount.error = ""
            mBinding.txtPassword.error = ""
            mBinding.txtConfirmPassword.error = ""

            val account = edt_account.text.toString()
            val password = edt_password.text.toString()
            val confirmPass = edt_confirm_password.text.toString()

            when {
                account.isEmpty() -> mBinding.txtAccount.error =
                    getString(R.string.account_can_not_be_empty)
                account.length < 3 -> mBinding.txtAccount.error =
                    getString(R.string.account_length_over_three)
                password.isEmpty() ->  mBinding.txtPassword.error =
                    getString(R.string.password_can_not_be_empty)
                password.length < 6 ->  mBinding.txtPassword.error =
                    getString(R.string.password_length_over_six)
                confirmPass.isEmpty() -> mBinding.txtConfirmPassword.error =
                    getString(R.string.confirm_password_can_not_be_empty)
                password != confirmPass -> mBinding.txtConfirmPassword.error =
                    getString(R.string.two_password_are_inconsistent)
                else -> mViewModel.register(account, password, confirmPass)
            }
        }
    }


    override fun observer() {
        super.observer()
        mViewModel.run {
            mSubmitting.observe(this@RegisterActivity, Observer {
                if (it) showProgressDialog(R.string.text_registering) else hideProgressDialog()
            })
            mRegisterResult.observe(this@RegisterActivity, Observer {
                if (it) ActivityManager.finish(RegisterActivity::class.java)
            })
        }
    }

}
