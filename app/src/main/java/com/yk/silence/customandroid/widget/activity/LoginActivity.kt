package com.yk.silence.customandroid.widget.activity

import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseVMActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.databinding.ActivityLoginBinding
import com.yk.silence.customandroid.viewmodel.login.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseVMActivity<LoginViewModel,ActivityLoginBinding>() {


    override fun getLayoutID() = R.layout.activity_login


    override fun viewModelClass() = LoginViewModel::class.java


    override fun initBinding(mBinding: ActivityLoginBinding) {
        super.initBinding(mBinding)
        mBinding.imgClose.setOnClickListener {
            ActivityManager.finish(LoginActivity::class.java)
        }

        mBinding.txtRegister.setOnClickListener {
            ActivityManager.start(RegisterActivity::class.java)
        }

        mBinding.edtPassword.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                btn_login.performClick()
                true
            } else {
                false
            }
        }

        mBinding.btnLogin.setOnClickListener {
            mBinding.txtAccount.error = ""
            mBinding.txtPassword.error = ""
            val account = mBinding.edtAccount.text.toString()
            val password = mBinding.edtPassword.text.toString()
            when {
                account.isEmpty() ->  mBinding.txtAccount.error =
                    getString(R.string.account_can_not_be_empty)
                password.isEmpty() -> mBinding.txtPassword.error =
                    getString(R.string.password_can_not_be_empty)
                else -> mViewModel.login(account, password)
            }
        }
    }




    override fun observer() {
        super.observer()
        mViewModel.run {
            mSubmitting.observe(this@LoginActivity, Observer {
                if (it) showProgressDialog(R.string.text_logining) else hideProgressDialog()
            })
            mLoginResult.observe(this@LoginActivity, Observer {
                if (it) {
                    ActivityManager.finish(LoginActivity::class.java)
                }
            })
        }


    }


}
