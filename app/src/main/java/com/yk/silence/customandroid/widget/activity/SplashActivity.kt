package com.yk.silence.customandroid.widget.activity

import android.Manifest
import android.os.Bundle
import com.yk.silence.customandroid.util.FileUtils
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.common.AppConfig
import com.yk.silence.customandroid.common.CrashHandler
import com.yk.silence.customandroid.databinding.ActivitySplashBinding
import com.yk.silent.permission.HiPermission
import com.yk.silent.permission.impl.PermissionCallback
import com.yk.silent.permission.model.PermissionItem

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getLayoutID() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()

    }

    /**
     * 初始化数据
     */
    private fun initData() {
        val permissionItems = ArrayList<PermissionItem>()
        permissionItems.add(
            PermissionItem(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                resources.getString(R.string.text_write_storage), R.drawable.permission_ic_camera
            )
        )
        permissionItems.add(
            PermissionItem(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                resources.getString(R.string.text_write_storage), R.drawable.permission_ic_camera
            )
        )

        HiPermission.create(this)
            .title(resources.getString(R.string.permission_get))
            .msg(resources.getString(R.string.permission_desc))
            .permissions(permissionItems)
            .checkMutiPermission(object : PermissionCallback {
                override fun onClose() {
                }

                override fun onDeny(permission: String?, position: Int) {
                }

                override fun onFinish() {
                    if (FileUtils.isSdCardExist()) {
                        if (!FileUtils.isFileExists(AppConfig.SDK_PATH)) {
                            FileUtils.createFile(AppConfig.SDK_PATH)
                        }
                        if (!FileUtils.isFileExists(AppConfig.LOG_PATH)) {
                            FileUtils.createFile(AppConfig.LOG_PATH)
                        }
                        CrashHandler.getInstance().init(this@SplashActivity, AppConfig.LOG_PATH)
                    }

                    window.decorView.postDelayed({
                        ActivityManager.start(MainActivity::class.java)
                        ActivityManager.finish(SplashActivity::class.java)
                    }, 1000)

                }

                override fun onGuarantee(permission: String?, position: Int) {

                }
            })
    }
}

