package com.shsy.pdainspect;

public class CommonConstants {
	
	private CommonConstants(){}

	public static final String SP_NAME = "sp_config";
	public static final boolean isShowLog = true;
	
	
	public final static int OPTIONS_INSAMPLESIZE = 4;
	
	
	//http请求
	public final static int STATE_SUCCESS = 1;
	public final static int STATE_FAIL = 0;
	
	
	
	//查验项判定
	public final static int CHECKPASS = 1;
	public final static int CHECKFAIL = 2;
	public final static int NOTCHECK = 0;
	
	//查验结论判定
	public final static String CYJL_SUCCESS = "1";
	public final static String CYJL_FAILURE = "2";	
	
	
	public final static String XMLCODE_OK = "1";
	public final static String XMLCODE_1 = "-1";
	public final static String XMLCODE_2 = "-2";
	public final static String XMLCODE_3 = "-3";
	public final static String XMLCODE_4 = "-4";
	public final static String XMLCODE_E = "$E";

	
	
	public final static String DIALOGTYPE = "Dialog_Type";
	public final static String ISRECYCLE = "ISRECYCLE";
	
	
	//--new服务器地址--
	public final static String SERVERADDRESS = "http://192.168.10.70:9080/cypda";
	public final static String JSESSIONID = "sessionid";
	
	////--new sharepreference key --
	public static final String USERNAME ="username";
	public static final String PASSWORD ="password";
	
	//startActivityForResult request code
	public final static int REQUEST_LOGIN = 100;

}
