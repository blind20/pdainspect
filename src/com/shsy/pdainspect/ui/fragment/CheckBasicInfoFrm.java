package com.shsy.pdainspect.ui.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.common.CommonAdapter;
import com.shsy.pdainspect.common.SerializableMap;
import com.shsy.pdainspect.common.ViewHolder;
import com.shsy.pdainspect.entity.CheckBaseInfo;
import com.shsy.pdainspect.entity.VehCSLogin;
import com.shsy.pdainspect.ui.activity.SelectFrmActivity;
import com.shsy.pdainspect.ui.activity.GonggaoActivty;
import com.shsy.pdainspect.utils.JsonArrayUtil;
import com.shsy.pdainspect.utils.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class CheckBasicInfoFrm extends BaseFragment implements View.OnClickListener{

	private ListView mListView;
	private List<String> mItems;
	private CommonAdapter<CheckBaseInfo> mAdaper;
	
	private Map<String,Object> mVehCSLoginInfo;
	private String mArgument;
	public static final String ARGUMENT = "argument";
	
	private List<CheckBaseInfo> mCheckInfos;
	private Button btn_jscs1;
	private Button btn_jscs2;
	
	public static CheckBasicInfoFrm newInstance(String argument){
		Bundle bundle = new Bundle();
		bundle.putString(ARGUMENT, argument);  
		CheckBasicInfoFrm checkBasicInfoFrm = new CheckBasicInfoFrm();
		checkBasicInfoFrm.setArguments(bundle);
		return checkBasicInfoFrm;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		mVehCSLoginInfo = new HashMap<String,Object>();
        if (bundle != null){  
            mArgument = bundle.getString(ARGUMENT);
            mVehCSLoginInfo = getVehCSLoginInfoByJson(mArgument);
        }
        initDatas(mVehCSLoginInfo);
        super.onCreate(savedInstanceState);
	}

	

	@Override
	public int getLayoutResID() {
		return R.layout.frm_check_basicinfo;
	}

	@Override
	public void initParam() {
		initView();
		viewSetAdapter();
	}

	private void initView() {
		mListView = (ListView) mRootView.findViewById(R.id.listview);
		btn_jscs1 =  (Button) mRootView.findViewById(R.id.btn_jscs1);
		btn_jscs2 =  (Button) mRootView.findViewById(R.id.btn_jscs2);
	}

	
	private void initDatas(Map<String,Object> map) {
		mCheckInfos = new ArrayList<CheckBaseInfo>();
		String[] array = getResources().getStringArray(R.array.check_basic_info);
		String[] arrayName = getResources().getStringArray(R.array.check_basic_info_name);
		
		for(int i=0;i<array.length;i++){
			CheckBaseInfo checkBaseInfo = new CheckBaseInfo();
			checkBaseInfo.attri = array[i];
			checkBaseInfo.attrMapZN = arrayName[i];
			if(map.get(array[i])!= null){
				if(map.get(array[i])instanceof ArrayList){
					checkBaseInfo.attrValue = null;
				}else{
					checkBaseInfo.attrValue = (String) map.get(array[i]);
				}
				
			}
			mCheckInfos.add(checkBaseInfo);
		}
	}
	

	private void viewSetAdapter() {
		mAdaper = new CommonAdapter<CheckBaseInfo>(mCheckInfos,mActivity,R.layout.item_check_basicinfo) {
			
			@Override
			public void convert(ViewHolder holder, CheckBaseInfo t) {
				holder.setText(R.id.item_key, t.attrMapZN);
				if(!TextUtils.isEmpty(t.attrValue)){
					convertToZN(holder,t);
				}else{
					holder.setText(R.id.item_value, "请输入"+t.attrMapZN);
				}
			}
		};
		mListView.setAdapter(mAdaper);
		onItemClick();
	}
	
	
	/**
	 * 转换代码值
	 * @param holder
	 * @param t
	 */
	private void convertToZN(ViewHolder holder, CheckBaseInfo t) {
		String name="";
		if(t.attri.equals("ywlx")){
			name = JsonArrayUtil.getLableByCode(mActivity, t.attrValue, R.array.ywzl, R.array.ywzl_code);
		}else if(t.attri.equals("hpzl")){
			name = JsonArrayUtil.getLableByCode(mActivity, t.attrValue, R.array.hpzl, R.array.hpzl_code);
		}else if(t.attri.equals("cyzl")){
			name = JsonArrayUtil.getLableByCode(mActivity, t.attrValue, R.array.cyzl, R.array.cyzl_code);
		}else if(t.attri.equals("syxz")){
			name = JsonArrayUtil.getLableByCode(mActivity, t.attrValue, R.array.syxz, R.array.syxz_code);
		}else if(t.attri.equals("cllx")){
			name = JsonArrayUtil.getLableByCode(mActivity, t.attrValue, R.array.cllx, R.array.cllx_code);
		}else if(t.attri.equals("csys")){
			char[] strChar= t.attrValue.toCharArray();
			for(char ch: strChar){
				String str = JsonArrayUtil.getLableByCode(mActivity, String.valueOf(ch), R.array.csys, R.array.csys_code);
				name +=str;
			}
		}else{
			name = t.attrValue;
		}
		
		holder.setText(R.id.item_value, name);
	}
	
	
	
	
	/**
	 * 把startActivityForResult中的
	 * requestCode 设置为 position(例如SelectedFrm.CYZL=4)
	 * 这样容易取出mItems.get(position)
	 * 
	 */
	private void onItemClick() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				switch (position) {
					case 4:
					case 5:
					case 6:
						startSelectAtyResult(position, intent);
						break;
						
					case 7:
						startSelectAtyResult(position, intent);
						break;
				}
			}
		});
		
		btn_jscs1.setOnClickListener(this);
		btn_jscs2.setOnClickListener(this);
	}
	
	
	private void startSelectAtyResult(int position, Intent intent) {
		int arg =-1;
		switch (position) {
			case ListSelectFrm.CYZL:
				arg = ListSelectFrm.CYZL;
				startAtyByIntent(position, intent, arg,SelectFrmActivity.FRMTYPE_SELECT);
				break;
			case ListSelectFrm.SYXZ:
				arg = ListSelectFrm.SYXZ;
				startAtyByIntent(position, intent, arg,SelectFrmActivity.FRMTYPE_SELECT);
				break;
			case ListSelectFrm.CLLX:
				arg = ListSelectFrm.CLLX;
				startAtyByIntent(position, intent, arg,SelectFrmActivity.FRMTYPE_SELECT);
				break;
			case 7:
				arg = CsysSelectFrm.CSYS;
				startAtyByIntent(position, intent, arg,SelectFrmActivity.FRMTYPE_CSYS);
				break;
		}
		
	}

	private void startAtyByIntent(int position, Intent intent, int pos,String frmtype) {
		intent.putExtra(SelectFrmActivity.FRMTYPE, frmtype);
		intent.putExtra(SelectFrmActivity.TITLE, mCheckInfos.get(position).attrMapZN);
		
		if(frmtype.equals(SelectFrmActivity.FRMTYPE_CSYS)){
			//车身颜色
			intent.putExtra(CsysSelectFrm.ARGREQCODE, pos);
			intent.putExtra(CsysSelectFrm.ARGCSYS, mCheckInfos.get(position).attrValue);
		}else if(frmtype.equals(SelectFrmActivity.FRMTYPE_SELECT)){
			//单选类型
			intent.putExtra(ListSelectFrm.ARGUMENT, pos);
		}
		
		intent.setClass(mActivity, SelectFrmActivity.class);
		startActivityForResult(intent, pos);
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case ListSelectFrm.CYZL:
			case ListSelectFrm.SYXZ:
			case ListSelectFrm.CLLX:
				notifyChange(requestCode,data);
				break;
			case CsysSelectFrm.CSYS:
				notifyCsysChange(requestCode,data);
				break;
		}
		mCallback.onItemSelected(mCheckInfos.get(requestCode).attri, mCheckInfos.get(requestCode).attrValue);
	}
	

	private void notifyChange(int position,Intent data){
		if(data !=null && data.getExtras()!= null ){
			SerializableMap serializableMap = (SerializableMap)data.getExtras().get(ListSelectFrm.RESPONSE_VALUE);
			Map<String,String> map = serializableMap.getMap();
			String value = null;
			for(Map.Entry<String, String> entry : map.entrySet()){
				value = entry.getKey();
			}
			mCheckInfos.get(position).attrValue = value;
			mAdaper.notifyDataSetChanged();
			
//			if(position == ListSelectFrm.CYZL){
//				cyxmChanged(value);
//			}
		}
	}
	
	private void notifyCsysChange(int position, Intent data) {
		if(data !=null ){
			if(!TextUtils.isEmpty(data.getStringExtra(CsysSelectFrm.RESPONSE_VALUE))){
				String csys = data.getStringExtra(CsysSelectFrm.RESPONSE_VALUE);
				mCheckInfos.get(position).attrValue = csys;
				Logger.show("csys", "csys=" + csys);
				mAdaper.notifyDataSetChanged();
			}
		}
	}
	
	
//	private void cyxmChanged(String value){
//		String[] cyzls = getResources().getStringArray(R.array.cyzl_code);
//		if(value.equals(cyzls[0])){
//			mCallback.onItemSelected(CheckCyxmFrm.CYZL_VEH);
//		}else{
//			mCallback.onItemSelected(CheckCyxmFrm.CYZL_SCHOOL);
//		}
//	}
	
	
	private Map<String, Object> getVehCSLoginInfoByJson(String json){
		return JsonArrayUtil.parseJSon2Map(json);
	}
	
//	class CheckBaseInfo{
//		String attri;      //ywlx  英文属性名
//		String attrMapZN;  //ywlx  中文属性名
//		String attrValue;  //ywlx  属性值(如果是代码值,不翻译)
//	}
	
	
	protected OnItemSelectedListener mCallback;
	public interface OnItemSelectedListener {
		public void onItemSelected(String attri,String attrValue);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {  
            mCallback = (OnItemSelectedListener) activity;  
        } catch (ClassCastException e) {  
            throw new ClassCastException(activity.toString()  
                    + " must implement OnCyzlSelectedListener");  
        }
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
			case R.id.btn_jscs1:
			case R.id.btn_jscs2:
				intent.setClass(mActivity, GonggaoActivty.class);
				intent.putExtra(CheckBasicInfoFrm.ARGUMENT, mArgument);
				startActivity(intent);
				break;
		}
	}
}
