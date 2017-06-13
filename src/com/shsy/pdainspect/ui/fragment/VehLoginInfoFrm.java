package com.shsy.pdainspect.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.common.CommonAdapter;
import com.shsy.pdainspect.common.SerializableMap;
import com.shsy.pdainspect.common.ViewHolder;
import com.shsy.pdainspect.entity.CheckBaseInfo;
import com.shsy.pdainspect.utils.JsonArrayUtil;
import com.shsy.pdainspect.utils.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class VehLoginInfoFrm extends BaseFragment {

	
	public static final String ARGUMENT = "argument";
	public static final String RESPONSE_VALUE = "response_value";
	
	private String mArgument;
	private ListView mListView;
	private CommonAdapter<Map<String,String>> mAdapter;
	//set到list的数据
	private List<Map<String,String>> mList;
	//根据argument解析成map
	private Map<String,Object> mVehLoginMap;
	//属性名、中文名对应的map
	private LinkedHashMap<String,String> mAttriMap;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		mVehLoginMap = new HashMap<String, Object>();
		if(bundle !=null){
			mArgument = bundle.getString(ARGUMENT);
			mVehLoginMap = JsonArrayUtil.parseJSon2Map(mArgument);
		}
		super.onCreate(savedInstanceState);
	}
	
	
	public static VehLoginInfoFrm newInstance(String argument){
		Bundle bundle = new Bundle();
		bundle.putString(ARGUMENT, argument);  
		VehLoginInfoFrm selectedFrm = new VehLoginInfoFrm();
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
		mAttriMap = initAttribute(R.array.veh_login_info,R.array.veh_login_info_zn);
		mList = initData(mVehLoginMap);
		List<Map<String,String>> tlist = new ArrayList<Map<String,String>>();
		for(int i=0;i<mList.size();i++){
			Map<String,String> map = mList.get(i);
			for(Map.Entry<String, String> entry : map.entrySet()){
				String key = entry.getKey();
				String value = entry.getValue();
				if(mAttriMap.containsKey(key)){
					if(!TextUtils.isEmpty(mAttriMap.get(key))){
						Logger.show("list", "key"+i+"="+key);
						tlist.add(map);
					}
				}
			}
		}
		viewSetAdapter(tlist);
	}


	private void initView() {
		mListView = (ListView) mRootView.findViewById(R.id.listview);
	}
	
	private void viewSetAdapter(List<Map<String,String>> list) {
//		for(int i=0;i<list.size();i++){
//			Map<String, String> _tmap = list.get(i); 
//			for(Map.Entry<String, String> entry : _tmap.entrySet()){
//				String key = entry.getKey();
//				String value = entry.getValue();
//				Logger.show("list", "key"+i+"="+key+",value"+i+"="+value);
//			}
//		}
		
		
		
		mAdapter = new CommonAdapter<Map<String,String>>(list,mActivity,R.layout.item_check_basicinfo) {
			@Override
			public void convert(ViewHolder holder, Map<String, String> map) {
				for(Map.Entry<String, String> entry : map.entrySet()){
					String key = entry.getKey();
					String value = entry.getValue();
					if(!TextUtils.isEmpty(mAttriMap.get(key))||
							!("null".equals(mAttriMap.get(key)))){
						holder.setText(R.id.item_key, mAttriMap.get(key))
						.setText(R.id.item_value, value);
					}else{
						Logger.show("vloginfo", "vloginfo");
					}
				}
				
			}
		};
		mListView.setAdapter(mAdapter);
	}

	
	
	private List<Map<String,String>> initData(Map<String,Object> arguMap,LinkedHashMap<String,String> attriMap){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(Map.Entry<String, String> l_entry : attriMap.entrySet()){
			String l_key = l_entry.getKey();
		}
		
		
		return list;
	}


	private List<Map<String,String>> initData(Map<String,Object> arguMap){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		int i=0;
		for(Map.Entry<String, Object> entry: arguMap.entrySet()){
			Logger.show("initData", "entrykey:"+i+"="+entry.getKey()+",entryvalue"+i+"="+entry.getValue());
			i++;
			if(entry.getValue()instanceof String){
				Map<String,String> data = new HashMap<String, String>();
				data.put(entry.getKey(), (String) entry.getValue());
				list.add(data);
			}
		}
		return list;
	}
	
	
	private LinkedHashMap<String,String> initAttribute(int resId1,int resId2){
		LinkedHashMap<String,String> data = new LinkedHashMap<String, String>();
		String[] array1 = getResources().getStringArray(resId1);
		String[] array2 = getResources().getStringArray(resId2);
		for(int i=0;i<array1.length;i++){
			data.put(array1[i], array2[i]);
		}
		return data;
	}
	
	

}
