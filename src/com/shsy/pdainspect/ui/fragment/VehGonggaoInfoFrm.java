package com.shsy.pdainspect.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.common.CommonAdapter;
import com.shsy.pdainspect.common.SerializableMap;
import com.shsy.pdainspect.common.ViewHolder;
import com.shsy.pdainspect.entity.CheckBaseInfo;
import com.shsy.pdainspect.network.MyHttpUtils;
import com.shsy.pdainspect.ui.activity.CheckVehActivity;
import com.shsy.pdainspect.utils.AddressUrlUtils;
import com.shsy.pdainspect.utils.JsonArrayUtil;
import com.shsy.pdainspect.utils.Logger;
import com.shsy.pdainspect.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import android.widget.ListView;
import android.widget.Toast;

public class VehGonggaoInfoFrm extends BaseFragment {

	
	public static final String ARGUMENT = "argument";
	public static final String RESPONSE_VALUE = "response_value";
	
	private String mArgument;
	private ListView mListView;
	private CommonAdapter<Map<String,String>> mAdapter;
	//set到list的数据
	private List<Map<String,String>> mShowList;
	//根据clxh解析成map
	private List<Map<String,Object>> mGongGaoList =new ArrayList<Map<String,Object>>();;
	//属性名、中文名对应的map
	private Map<String,String> mAttriMap;
	private String clxh;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		mGongGaoList =new ArrayList<Map<String,Object>>();
		if(bundle !=null){
			mArgument = bundle.getString(ARGUMENT);
		}
		super.onCreate(savedInstanceState);
	}


	public static VehGonggaoInfoFrm newInstance(String argument){
		Bundle bundle = new Bundle();
		bundle.putString(ARGUMENT, argument);  
		VehGonggaoInfoFrm selectedFrm = new VehGonggaoInfoFrm();
		selectedFrm.setArguments(bundle);
		return selectedFrm;
	}

	@Override
	public int getLayoutResID() {
		return R.layout.listview_common;
	}

	@Override
	public void initParam() {
		initView();
		mAttriMap = initAttribute(R.array.gonggao_info,R.array.gonggao_info_zn);
		getGonggaoInfo(mArgument);
	}


	private void initView() {
		mListView = (ListView) mRootView.findViewById(R.id.listview);
	}
	

	private List<Map<String,String>> initData(List<Map<String,Object>> list,int position){
		List<Map<String,String>> datas = new ArrayList<Map<String,String>>();
		Map<String,Object> map = new HashMap<String, Object>();
		if(position>list.size()||position<0){
			return datas;
		}
		map = list.get(position);
		for(Map.Entry<String, Object> entry: map.entrySet()){
			if(entry.getValue()instanceof String){
				Map<String,String> gonggao = new HashMap<String, String>();
				gonggao.put(entry.getKey(), (String) entry.getValue());
				datas.add(gonggao);
			}
		}
		return datas;
	}
	
	
	private Map<String,String> initAttribute(int resId1,int resId2){
		Map<String,String> data = new HashMap<String, String>();
		String[] array1 = getResources().getStringArray(resId1);
		String[] array2 = getResources().getStringArray(resId2);
		for(int i=0;i<array1.length;i++){
			data.put(array1[i], array2[i]);
		}
		return data;
	}
	
	

	private void getGonggaoInfo(String arg) {
		Map<String, Object> map = JsonArrayUtil.parseJSon2Map(arg);
		if(map.get("clxh")instanceof String){
			clxh = (String) map.get("clxh");
		}
		if(!TextUtils.isEmpty(clxh)){
			String url=AddressUrlUtils.getAddressUrl(AddressUrlUtils.GONGGAOURL);
			getGonggaoInfoByClxhNetwork(url,clxh);
		}
	}


	private void getGonggaoInfoByClxhNetwork(String url, String clxh) {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("clxh", clxh);
		MyHttpUtils.getInstance(mActivity).postHttpByParam(url, map, new StringCallback() {
			
			@Override
			public void onResponse(String response, int id) {
				parseResponse(response);
				if(mGongGaoList.size()>0){
					mShowList = initData(mGongGaoList,0);
					viewSetAdapter(mShowList);
				}
			}
			
			@Override
			public void onError(Call call, Exception e, int id) {
				ToastUtils.showToast(mActivity, "网络问题,获取不到公告信息", Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
		});
	}
	
	
	
	private String parseResponse(String response){
		String res ="";
		try {
			JSONArray ja = new JSONArray(response);
			for(int i=0;i<ja.length();i++){
				String str = ja.getString(i);
				Map<String,Object> map = JsonArrayUtil.parseJSon2Map(str);
				mGongGaoList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			res ="获取公告信息失败";
		}
		return res;
	}
	
	
	private void viewSetAdapter(List<Map<String,String>> list) {
		mAdapter = new CommonAdapter<Map<String,String>>(list,mActivity,R.layout.item_check_basicinfo) {
			@Override
			public void convert(ViewHolder holder, Map<String, String> map) {
				for(Map.Entry<String, String> entry : map.entrySet()){
					String key = entry.getKey();
					String value = entry.getValue();
					if(!TextUtils.isEmpty(mAttriMap.get(key))){
						holder.setText(R.id.item_key, mAttriMap.get(key))
						.setText(R.id.item_value, value);
					}
				}
				
			}
		};
		mListView.setAdapter(mAdapter);
	}

	
	
	private List<Map<String,String>> getResMap(int resId1, int resId2){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		String[] keys = getResources().getStringArray(resId1); 
		String[] values = getResources().getStringArray(resId2);
		for(int i=0;i<keys.length;i++){
			Map<String,String> data = new HashMap<String, String>();
			data.put(keys[i], values[i]);
			list.add(data);
		}
		return list;
	}
	

}
