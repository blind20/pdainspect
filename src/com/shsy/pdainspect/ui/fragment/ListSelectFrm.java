package com.shsy.pdainspect.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.common.CommonAdapter;
import com.shsy.pdainspect.common.SerializableMap;
import com.shsy.pdainspect.common.ViewHolder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListSelectFrm extends BaseFragment {

	public static final int CYZL = 4;
	public static final int SYXZ = 5;
	public static final int CLLX = 6;
	
	public static final String ARGUMENT = "argument";
	public static final String RESPONSE_VALUE = "response_value";
	
	private Integer mArgument;
	private ListView mListView;
	private CommonAdapter<Map<String,String>> mAdapter;
	private List<Map<String,String>> mList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if(bundle !=null){
			mArgument = bundle.getInt(ARGUMENT);
		}
		super.onCreate(savedInstanceState);
	}
	
	
	public static ListSelectFrm newInstance(int argument){
		Bundle bundle = new Bundle();
		bundle.putInt(ARGUMENT, argument);  
		ListSelectFrm selectedFrm = new ListSelectFrm();
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
		mList = initData(mArgument);
		viewSetAdapter(mList,mArgument);
	}


	private void initView() {
		mListView = (ListView) mRootView.findViewById(R.id.listview);
	}
	
	private void viewSetAdapter(List<Map<String,String>> list,Integer argument) {
		mAdapter = new CommonAdapter<Map<String,String>>(list,mActivity,R.layout.item_single_text) {
			@Override
			public void convert(ViewHolder holder, Map<String, String> map) {
				String txt = "";
				for(Map.Entry<String, String> entry : map.entrySet()){
					txt = entry.getKey()+" "+entry.getValue();
				}
				holder.setText(R.id.item_text, txt);
			}
		};
		mListView.setAdapter(mAdapter);
		onItemClick(argument);
	}

	
	//argument表示requestcode,也表示前一个页面的position
	private void onItemClick(final Integer argument) {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Map<String,String> map = mList.get(position);
				SerializableMap serializableMap = new SerializableMap();
				serializableMap.setMap(map);
				Intent intent = new Intent();  
		        intent.putExtra(RESPONSE_VALUE, serializableMap);
		        mActivity.setResult(argument, intent);
		        mActivity.finish();
			}
		});
	}


	private List<Map<String,String>> initData(int type){
		switch (type) {
			case CYZL:
				return getResMap(R.array.cyzl_code,R.array.cyzl);
				
			case SYXZ:
				return getResMap(R.array.syxz_code,R.array.syxz);
	
			default:
				return getResMap(R.array.cllx_code,R.array.cllx);
		}
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
