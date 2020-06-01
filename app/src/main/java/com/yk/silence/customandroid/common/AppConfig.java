package com.yk.silence.customandroid.common;

import android.os.Environment;


import java.io.File;

public class AppConfig {

    public static final String SDK_PATH = Environment
            .getExternalStorageDirectory().getPath() +
            File.separator+ "WanAndroid" + File.separator;

    public static final String LOG_PATH = SDK_PATH + "log" + File.separator;
}
