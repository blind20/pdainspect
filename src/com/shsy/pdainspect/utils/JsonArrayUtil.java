package com.shsy.pdainspect.utils;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.content.Context;

public class JsonArrayUtil {

	public static String getLableByCode(Context con,String code,int nLables, int nCodes){
		String[] lables = con.getResources().getStringArray(nLables);
		String[] codes = con.getResources().getStringArray(nCodes);
		String lable = null;
		for(int i=0;i<codes.length;i++){
			if(code.equals(codes[i])){
				lable = lables[i];
				break;
			}
		}
		return lable;
	}
	
	
	public static String getCodeByLable(Context con,String lable, int nLables, int nCodes){
		String[] lables = con.getResources().getStringArray(nLables);
		String[] codes = con.getResources().getStringArray(nCodes);
		String code = null;
		for(int i=0;i<lables.length;i++){
			if(lable.equals(lables[i])){
				code = codes[i];
				break;
			}
		}
		return code;
	}
	
	public static Map<String, Object> parseJSon2Map(String data) {
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		Map<String, Object> map = g.fromJson(data, new TypeToken<Map<String, Object>>() {
		}.getType());
		return map;
	}
	
}
