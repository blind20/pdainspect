package com.shsy.pdainspect.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

public class PhoneUtils {

	public static String getVersionName(Context context) {
	    return getPackageInfo(context).versionName;
	}
	 
	public static int getVersionCode(Context context) {
	    return getPackageInfo(context).versionCode;
	}
	 
	private static PackageInfo getPackageInfo(Context context) {
	    PackageInfo pi = null;
	 
	    try {
	        PackageManager pm = context.getPackageManager();
	        pi = pm.getPackageInfo(context.getPackageName(),
	                PackageManager.GET_CONFIGURATIONS);
	 
	        return pi;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return pi;
	}
	
	public static String deviceImei(Context context){
		String devImei;
		TelephonyManager tm = (TelephonyManager)context.getSystemService(
				Context.TELEPHONY_SERVICE);
		try {
			devImei = tm.getDeviceId();
		} catch (Exception e) {
			devImei = "";
		}
		return devImei;
	}
}
