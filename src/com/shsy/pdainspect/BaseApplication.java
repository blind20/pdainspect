package com.shsy.pdainspect;


import java.util.concurrent.TimeUnit;

import com.shsy.pdainspect.network.CookieJarImpl;
import com.shsy.pdainspect.network.PersistentCookieStore;
import com.zhy.http.okhttp.OkHttpUtils;

import android.app.Application;
import okhttp3.OkHttpClient;


public class BaseApplication extends Application {
	
	private static BaseApplication instance;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
							        		.connectTimeout(5000L, TimeUnit.MILLISECONDS)
											.readTimeout(10000L, TimeUnit.MILLISECONDS)
											.build();
		
		OkHttpUtils.initClient(okHttpClient);
	}
	
	
}
