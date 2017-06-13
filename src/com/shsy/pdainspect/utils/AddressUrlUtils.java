package com.shsy.pdainspect.utils;

import com.shsy.pdainspect.CommonConstants;


public class AddressUrlUtils {

	public static final String LOGINURL = "/user/login";
	public static final String CHECKURL = "/check/query";
	public static final String WRITEURL = "/check/write";
	
	public static final String GONGGAOURL = "/check/getGonggaoList";
	
	public static final String VERSIONURL = "/pdafile/version.json";
	public static final String APKURL = "/pdafile/pdainspect.apk";
	
	public static final String PROPURL = "/pdafile/prop.properties";
	
	public static String getAddressUrl(String path){
		return CommonConstants.SERVERADDRESS +path;
	}
}
