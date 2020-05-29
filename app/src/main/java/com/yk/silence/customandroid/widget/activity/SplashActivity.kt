package com.yk.silence.customandroid.widget.activity

import android.os.Bundle
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.base.BaseActivity
import com.yk.silence.customandroid.common.ActivityManager
import com.yk.silence.customandroid.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getLayoutID() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.postDelayed({
            ActivityManager.start(MainActivity::class.java)
            ActivityManager.finish(SplashActivity::class.java)
        }, 1000)
    }


}
