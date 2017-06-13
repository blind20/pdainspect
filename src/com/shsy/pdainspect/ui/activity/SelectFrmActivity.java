package com.shsy.pdainspect.ui.activity;


import com.shsy.pdainspect.common.SingleFrmActivity;
import com.shsy.pdainspect.ui.fragment.CsysSelectFrm;
import com.shsy.pdainspect.ui.fragment.ListSelectFrm;
import com.shsy.pdainspect.ui.fragment.TitleBarFrm;
import com.shsy.pdainspect.utils.Logger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

public class SelectFrmActivity extends SingleFrmActivity {
	
	public static final String FRMTYPE = "frmtype";
	public static final String FRMTYPE_CSYS = "frmtype_csys";
	public static final String FRMTYPE_SELECT = "frmtype_select";
	
	public static final String TITLE = "title";
	private String mArguTitle;
	private int mArguSelect;
	private String mArguFrmType;
	private String mArguCsys;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mArguFrmType = getIntent().getStringExtra(FRMTYPE);
		mArguTitle = getIntent().getStringExtra(TITLE);
		
		if(mArguFrmType.equals(SelectFrmActivity.FRMTYPE_CSYS)){
			//车身颜色
			mArguSelect = getIntent().getIntExtra(CsysSelectFrm.ARGREQCODE, -1);
			mArguCsys =  getIntent().getStringExtra(CsysSelectFrm.ARGCSYS);
			Logger.show("RESPONSE_VALUE", "color="+mArguCsys+",reqcode="+mArguSelect);
		}else if(mArguFrmType.equals(SelectFrmActivity.FRMTYPE_SELECT)){
			//单选类型
			mArguSelect = getIntent().getIntExtra(ListSelectFrm.ARGUMENT, -1);
		}
		
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void findView() {
	}

	@Override
	protected Fragment createFragment() {
		if(FRMTYPE_SELECT.equals(mArguFrmType)){
			return ListSelectFrm.newInstance(mArguSelect);
		}
		return CsysSelectFrm.newInstance(mArguSelect,mArguCsys);
	}



	@Override
	protected Fragment createTitleFragment() {
		return TitleBarFrm.newInstance(View.VISIBLE, View.VISIBLE, View.INVISIBLE, mArguTitle);
	}

	@Override
	public void onTitleBar() {
		
	}
	

}
