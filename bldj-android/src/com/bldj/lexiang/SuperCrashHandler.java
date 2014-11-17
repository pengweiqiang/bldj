package com.bldj.lexiang;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.ApiUtils;
import com.bldj.lexiang.api.bo.ExceptionInfo;

/**
 * 自定义的异常处理类,整个应用程序只有一个SuperCrashHandler
 */
public class SuperCrashHandler implements UncaughtExceptionHandler {

    private static SuperCrashHandler myCrashHandler;
    private Context context;

    private SuperCrashHandler() {
    }

    public static synchronized SuperCrashHandler getInstance() {
	if (myCrashHandler != null) {
	    return myCrashHandler;
	} else {
	    myCrashHandler = new SuperCrashHandler();
	    return myCrashHandler;
	}
    }

    public void init(Context context) {
	this.context = context;
    }

    public void uncaughtException(Thread arg0, Throwable arg1) {
	// 当前版本号
	String versioninfo = getVersionInfo();
	// 设备硬件信息.
	String mobileInfo = getMobileInfo();
	// 错误的堆栈信息
	String errorinfo = getErrorInfo(arg1);
	// 异常信息收集
	System.out.println("-------------------Crash信息-------------------");
	System.out.println("程序版本号：" + versioninfo);
	System.out.println("设备信息：" + mobileInfo);
	System.out.println("异常信息：" + errorinfo);
	ApiUserUtils.repoException(new ExceptionInfo(versioninfo, mobileInfo, errorinfo));
	// 重启APP
	ApiUtils.restart();
    }

    /**
     * 获取错误的信息 
     * @param arg1
     * @return
     */
    private String getErrorInfo(Throwable arg1) {
	Writer writer = new StringWriter();
	PrintWriter pw = new PrintWriter(writer);
	arg1.printStackTrace(pw);
	pw.close();
	String error = writer.toString();
	return error;
    }

    /**
     * 获取手机的硬件信息 
     * @return
     */
    private String getMobileInfo() {
	StringBuffer sb = new StringBuffer();
	// 反射获取系统的硬件信息
	try {
	    Field[] fields = Build.class.getDeclaredFields();
	    for (Field field : fields) {
		field.setAccessible(true);
		String name = field.getName();
		String value = field.get(null).toString();
		sb.append(name + "=" + value);
		sb.append("\n");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return sb.toString();
    }

    /**
     * 获取手机的版本信息
     * @return
     */
    private String getVersionInfo() {
	try {
	    PackageManager pm = context.getPackageManager();
	    PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
	    return info.versionName;
	} catch (Exception e) {
	    e.printStackTrace();
	    return "未知版本号";
	}
    }
}