package com.yk.silence.customandroid.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.util.List;
import java.util.Locale;

public class AppUtil {

    /**
     * 获取当前应用程序的包名
     *
     * @param context 上下文对象
     * @return 返回包名
     */
    public static String getPackageName(Context context) {
        //当前应用pid
        int pid = android.os.Process.myPid();
        //任务管理类
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //遍历所有应用
        assert manager != null;
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo info : infos) {
            if (info.pid == pid)//得到当前应用
                return info.processName;//返回包名
        }
        return "";
    }


    /**
     * Android系统版本
     */
    public static String getOSVersion() {
        String osVersion = "";
        try {
            osVersion = "OS Version: " + Build.VERSION.RELEASE + Build.VERSION.SDK_INT;
            return osVersion;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "0.0";
        }
    }

    /**
     * 获取手机制造商
     */
    public static String getManufacturer() {
        String phone = "";
        try {
            phone = "Phone: " + Build.MANUFACTURER + "--Model: " + Build.MODEL;
            return phone;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Phone";
        }
    }

    /**
     * 获取地区
     */
    public static String getLocation() {
        String local = "";
        try {
            local = Locale.getDefault().getCountry();
            return local;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "US";
        }
    }

    /**
     * 获取语言
     */
    public static String getLanguage() {
        String location = "";
        try {
            location = Locale.getDefault().getLanguage();
            return location;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "English";
        }
    }


}
