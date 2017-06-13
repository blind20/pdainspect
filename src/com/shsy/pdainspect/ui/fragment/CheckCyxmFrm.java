package com.shsy.pdainspect.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.CommonConstants;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.common.CommonAdapter;
import com.shsy.pdainspect.common.ViewHolder;
import com.shsy.pdainspect.entity.CheckItemEntity;
import com.shsy.pdainspect.ui.fragment.CheckBasicInfoFrm.OnItemSelectedListener;
import com.shsy.pdainspect.utils.Logger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CheckCyxmFrm extends BaseFragment {
	
	public static final String CYZL = "cyzl";
	public static final int CYZL_VEH = 0x10;
	public static final int CYZL_SCHOOL = 0x11;
	public static final int REQ_CHECKITEM =0x120;
	
	
	public static final String ARG_CYZL = "arg_cyzl";
	public static final String ARG_CYXM = "arg_cyxm";
	private int mArgCyzl;
	private String mArgCyxm;
	
	private ListView mListView;
	private CommonAdapter<CheckItemEntity> mAdapter;
	
	private List<CheckItemEntity> mCyxmList;
	private int mPosition;


	public static CheckCyxmFrm newInstance(int cyzl,String cyxm){
		Bundle bundle = new Bundle();
		bundle.putInt(ARG_CYZL, cyzl);  
		bundle.putString(ARG_CYXM, cyxm); 
		CheckCyxmFrm checkCyxmFrm = new CheckCyxmFrm();
		checkCyxmFrm.setArguments(bundle);
		Logger.show("newInstance", "newInstance:"+cyzl);
		return checkCyxmFrm;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Logger.show("cyxm onCreate", "onCreate");
		Bundle bundle = getArguments();
		if(bundle !=null){
			mArgCyzl = bundle.getInt(ARG_CYZL);
			mArgCyxm = bundle.getString(ARG_CYXM);
		}
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public int getLayoutResID() {
		return R.layout.frm_check_itemsinfo;
	}

	@Override
	public void initParam() {
		initView();
		mCyxmList = initCYXM(mArgCyzl,mArgCyxm);
		viewSetAdapter();
		onItemClick();
	}

	private void initView() {
		mListView = (ListView) mRootView.findViewById(R.id.listview);
	}

	
	private List<CheckItemEntity> initCYXM(int argCyzl,String argCyxm){
		String[] cyxmArray = (argCyzl == CYZL_VEH)? (getResources().getStringArray(R.array.check_items_veh))
				:(getResources().getStringArray(R.array.check_items_school));
		
		mCyxmList = new ArrayList<CheckItemEntity>();
		for(int i=0;i<cyxmArray.length;i++){
			CheckItemEntity checkItemEntity = new CheckItemEntity();
			checkItemEntity.setSeq(i+1);
			checkItemEntity.setTextCheckItem(cyxmArray[i]);
			checkItemEntity.setCheckflag(CommonConstants.NOTCHECK);
			mCyxmList.add(checkItemEntity);
		}
		
		String[] cyxmMustArray = parseCyxm(argCyxm);
		if(cyxmMustArray!=null){
			for(String str: cyxmMustArray){
				int check = parseMustCheckCyxm(str);
				mCyxmList.get(check-1).setCheckflag(CommonConstants.CHECKPASS);
			}
		}
		
		return mCyxmList;
	}	
	
	
	private String[] parseCyxm(String argCyxm) {
		String jo=null;
		try {
			JSONObject obj = new JSONObject(argCyxm);
			if(obj != null){
				if(!(obj.get("cyxm")instanceof JSONArray)){
					jo = obj.getString("cyxm");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		if(!TextUtils.isEmpty(jo)){
			return jo.split(",");
		}
		return null;
	}
	
	private int parseMustCheckCyxm(String data){
		String str = data.substring(2, 4);
		return Integer.parseInt(str);
	}
	
	

	private void viewSetAdapter() {
		mAdapter = new CommonAdapter<CheckItemEntity>(mCyxmList,mActivity,R.layout.item_check_itemsinfo) {
			@Override
			public void convert(ViewHolder holder, CheckItemEntity t) {
//				Logger.show("changeCyxm", "item:"+t);
				holder.setText(R.id.tv_checkitem, t.getTextCheckItem()).setText(R.id.tv_seq, t.getSeq()+"");
				
				if(CommonConstants.CHECKPASS == t.getCheckflag()){
					holder.setImageResource(R.id.iv_checkflag,R.drawable.checkflg_pass);
				}else if(CommonConstants.NOTCHECK == t.getCheckflag()){
					holder.setImageResource(R.id.iv_checkflag,R.drawable.checkflg_notcheck);
				}else{
					holder.setImageResource(R.id.iv_checkflag,R.drawable.checkflg_fail);
				}
			}
		};
		mListView.setAdapter(mAdapter);
	}
	
	

	private void onItemClick() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mPosition = position;
				MyDialogFragment myDialog = MyDialogFragment.newInstance(
						MyDialogFragment.DLG_CHECK,
						mCyxmList.get(position).getTextCheckItem(),
						"",
						REQ_CHECKITEM);
				
				myDialog.setTargetFragment(CheckCyxmFrm.this, REQ_CHECKITEM);
				myDialog.show(getFragmentManager(), "");
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQ_CHECKITEM){
			int checkRst = data.getIntExtra(MyDialogFragment.RES_CHECKRST, 0);
			mCyxmList.get(mPosition).setCheckflag(checkRst);
			mAdapter.notifyDataSetChanged();
			mCallback.onCyxmResult(mPosition,checkRst);
		}
	}
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		Logger.show("cyxm onResume", "onResume");
//		for(String str : mCyxmList){
//			Logger.show("onResume", "onResume:"+str+"\n");
//		}
//		mAdapter.notifyDataSetChanged();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logger.show("cyxm onCreateView", "onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Logger.show("cyxm onActivityCreated", "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Logger.show("cyxm onViewCreated", "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}
	
	
	/**
	 * 选择"查验种类"后更新查验项目
	 * @param cyzl
	 */
	public void changeCyxm(int cyzl){
		Logger.show("changeCyxm", "changeCyxm:"+cyzl);
//		mCyxmList = initCYXM(cyzl);
//		for(CheckItemEntity str : mCyxmList){
//			Logger.show("changeCyxm", "cyxms:"+str.getTextCheckItem()+"\n");
//		}
		mAdapter.notifyDataSetChanged();
	}
	
	
	protected OnCyxmResultListener mCallback;
	public interface OnCyxmResultListener {
		public void onCyxmResult(int position,int checkRst);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {  
            mCallback = (OnCyxmResultListener) activity;  
        } catch (ClassCastException e) {  
            throw new ClassCastException(activity.toString()  
                    + " must implement OnCyxmResultListener");  
        }
	}
}
