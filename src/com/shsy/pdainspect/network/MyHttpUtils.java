package com.shsy.pdainspect.network;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.shsy.pdainspect.CommonConstants;
import com.shsy.pdainspect.utils.Logger;
import com.shsy.pdainspect.utils.SharedPreferenceUtils;
import com.shsy.pdainspect.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Response;

public class MyHttpUtils {
	
	private static Context mContext;
	
	
	private static MyHttpUtils instance;
	
	private MyHttpUtils(Context context){
		mContext = context;
	}
	
	public static synchronized MyHttpUtils getInstance(Context context) {
		if (instance == null) {
			instance = new MyHttpUtils(context);
		}
		return instance;
	}
	
	
	public void postHttpByParam(String url,Map<String, String> map,StringCallback callback){
		Map<String, String> headers = new HashMap<String, String>();
//		String session = (String) SharedPreferenceUtils.get(mContext, CommonConstants.JSESSIONID, "");
//		if(TextUtils.isEmpty(session)){
//			return;
//		}
//		headers.put("Cookie", "JSESSIONID="+session);
		PostFormBuilder builder = OkHttpUtils.post().url(url).headers(headers);
		for(Map.Entry<String, String> entry: map.entrySet()){
			builder.addParams(entry.getKey(),entry.getValue());
		}
		builder.build().execute(callback);
	}
	

	
}
