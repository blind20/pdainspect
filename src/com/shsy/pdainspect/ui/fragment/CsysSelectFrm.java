package com.shsy.pdainspect.ui.fragment;


import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.utils.Logger;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class CsysSelectFrm extends BaseFragment {

	public static final int CSYS = 7;
	
	public static final String ARGREQCODE = "pos";
	public static final String ARGCSYS = "csys";
	public static final String RESPONSE_VALUE = "response_value";
	
	private Button btnSave;
	private Spinner mCsysSpinner1;
	private Spinner mCsysSpinner2;
	private Spinner mCsysSpinner3;
	private ArrayAdapter<String> mCsys1Adapter;
	private ArrayAdapter<String> mCsys2Adapter;
	private ArrayAdapter<String> mCsys3Adapter;
	
	public String[] mCsys;
	public String[] mCsysCode;
	
	public int mArgReqCode;
	public String mArgCsys;
	
	private String mRes ="";
	
	
	public static CsysSelectFrm newInstance(int pos,String csys){
		Bundle bundle = new Bundle();
		bundle.putInt(ARGREQCODE, pos);  
		bundle.putString(ARGCSYS, csys); 
		CsysSelectFrm csysSelectFrm = new CsysSelectFrm();
		csysSelectFrm.setArguments(bundle);
		return csysSelectFrm;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if(bundle !=null){
			mArgReqCode = bundle.getInt(ARGREQCODE);
			mArgCsys = bundle.getString(ARGCSYS);
		}
		super.onCreate(savedInstanceState);
	}

	
	@Override
	public int getLayoutResID() {
		return R.layout.frm_select_csys;
	}

	@Override
	public void initParam() {
		findView();
		initDatas();
		setView();
		onSelectItem();
	}

	private void findView() {
		btnSave = (Button) mRootView.findViewById(R.id.btn_save);
		mCsysSpinner1 = (Spinner) mRootView.findViewById(R.id.spn_csys1);
		mCsysSpinner2 = (Spinner) mRootView.findViewById(R.id.spn_csys2);
		mCsysSpinner3 = (Spinner) mRootView.findViewById(R.id.spn_csys3);
	}

	private void initDatas() {
		String[] colors = getResources().getStringArray(R.array.csys);
		
		mCsys = new String[colors.length+1];
		System.arraycopy(colors, 0, mCsys,1, colors.length); 
		mCsys[0]="请选择颜色";
		mCsysCode = getResources().getStringArray(R.array.csys_code);
	}
	
	private void setView() {
		mCsys1Adapter = new ArrayAdapter<String>(mActivity, R.layout.myspinner_bar,mCsys);
		mCsys1Adapter.setDropDownViewResource(R.layout.myspinner_item);
		mCsysSpinner1.setAdapter(mCsys1Adapter);
		
		mCsys2Adapter = new ArrayAdapter<String>(mActivity, R.layout.myspinner_bar,mCsys);
		mCsys2Adapter.setDropDownViewResource(R.layout.myspinner_item);
		mCsysSpinner2.setAdapter(mCsys2Adapter);
		
		mCsys3Adapter = new ArrayAdapter<String>(mActivity, R.layout.myspinner_bar,mCsys);
		mCsys3Adapter.setDropDownViewResource(R.layout.myspinner_item);
		mCsysSpinner3.setAdapter(mCsys3Adapter);
	}

	
	private void onSelectItem() {
		spinnerItemClick(mCsysSpinner1);
		spinnerItemClick(mCsysSpinner2);
		spinnerItemClick(mCsysSpinner3);
		
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Logger.show("btnSave", "btnSave="+mRes);
				if(!TextUtils.isEmpty(mRes)){
					Intent intent = new Intent();  
			        intent.putExtra(RESPONSE_VALUE, mRes);
			        mActivity.setResult(mArgReqCode, intent);
				}
		        mActivity.finish();
			}
		});
	}

	private void spinnerItemClick(Spinner spinner){
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(position!=0){
					mRes +=  mCsysCode[position-1];
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	
	

}
