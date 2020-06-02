package com.yk.silence.customandroid.widget.activity

import android.annotation.SuppressLint
import android.widget.CompoundButton
import androidx.constraintlayout.solver.Cache
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.yk.silence.customandroid.BuildConfig
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseActivity
import com.yk.silence.customandroid.base.BaseVMActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.databinding.ActivitySettingBinding
import com.yk.silence.customandroid.ext.SettingsStore
import com.yk.silence.customandroid.ui.dialog.CommonDialog
import com.yk.silence.customandroid.ui.dialog.DialogFragmentHelper
import com.yk.silence.customandroid.ui.dialog.FontSizeDialog
import com.yk.silence.customandroid.util.CacheUtil
import com.yk.silence.customandroid.util.DayOrNightUtil
import com.yk.silence.customandroid.viewmodel.setting.SettingViewModel

@SuppressLint("SetTextI18n")
class SettingActivity : BaseVMActivity<SettingViewModel, ActivitySettingBinding>() {

    override fun getLayoutID() = R.layout.activity_setting

    override fun viewModelClass() = SettingViewModel::class.java

    @SuppressLint("StringFormatInvalid")
    override fun initBinding(mBinding: ActivitySettingBinding) {
        super.initBinding(mBinding)
        val textZoom = SettingsStore.getWebTextZoom()
        mBinding.txtFontSize.text = "$textZoom%"
        mBinding.txtAboutVersion.text =
            getString(R.string.current_version, BuildConfig.VERSION_NAME)
        mBinding.imgSettingBack.setOnClickListener {
            ActivityManager.finish(SettingActivity::class.java)
        }
        mBinding.lytClearCache.setOnClickListener {
            DialogFragmentHelper.setOnCommonDialog(supportFragmentManager,
                getString(R.string.clear_cache),
                getString(R.string.confirm_clear_cache),
                object : CommonDialog.OnCommonCheckListener {
                    override fun onCommonCheck() {
                        CacheUtil.clearCache(this@SettingActivity)
                        mBinding.txtClearCache.text = CacheUtil.getCacheSize(this@SettingActivity)
                    }

                })
        }

        mBinding.txtLoginOut.setOnClickListener {
            DialogFragmentHelper.setOnCommonDialog(supportFragmentManager,
                getString(R.string.logout),
                getString(R.string.confirm_logout),
                object : CommonDialog.OnCommonCheckListener {
                    override fun onCommonCheck() {
                        mViewModel.loginOut()
                        ActivityManager.start(LoginActivity::class.java)
                        ActivityManager.finish(SettingActivity::class.java)
                    }

                })
        }

        mBinding.lytAboutUs.setOnClickListener { }
        mBinding.lytCheckVersion.setOnClickListener {

        }
        mBinding.lytFontSize.setOnClickListener {
            DialogFragmentHelper.setFontSizeDialog(supportFragmentManager,
                object : FontSizeDialog.OnFontChangeListener {
                    override fun onFontChange(size: Int) {
                        mBinding.txtFontSize.text = "$size%"
                    }
                })
        }

        mBinding.switchNight.setOnCheckedChangeListener { _, isChecked ->
            DayOrNightUtil.setNightModel(isChecked)
            SettingsStore.setNightMode(isChecked)
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.getLoginStatus()
    }

    override fun observer() {
        super.observer()
        mViewModel.isLogin.observe(this, Observer {
            mBinding.txtLoginOut.isVisible = it
        })
    }
}
