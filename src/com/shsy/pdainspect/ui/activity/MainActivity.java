package com.shsy.pdainspect.ui.activity;


import com.shsy.pdainspect.BaseActivity;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.ui.fragment.PersonInfoFrm;
import com.shsy.pdainspect.ui.fragment.ScanLshFrm;
import com.shsy.pdainspect.ui.fragment.TitleBarFrm;
import com.shsy.pdainspect.utils.Logger;
import com.shsy.pdainspect.utils.ToolUtils;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class MainActivity extends BaseActivity implements View.OnClickListener,TitleBarFrm.OnTitleBarListener{

	public final static String CHOOSEINDEX = "chooseIndex";
	public final static String ISRECYCLE = "isrecycle";
	
	
	private ImageButton buttom_inspect;
	private ImageButton buttom_personinfo;
	
	private FragmentTransaction ft;
	
	private int chooseIndex=-1;
	
	private boolean isRecycle = false;
	//标记是否从其他fragment点击返回按钮
	private boolean isBack = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(savedInstanceState == null){
			onClick(buttom_inspect);
		}
	}
	
	
	@Override
	public int getLayoutResID() {
		return R.layout.aty_main;
	}

	@Override
	public void findView() {
		buttom_inspect = (ImageButton) findViewById(R.id.buttom_inspect);
		buttom_personinfo = (ImageButton) findViewById(R.id.buttom_personinfo);
	}

	@Override
	public void initParam() {
		buttom_inspect.setOnClickListener(this);
		buttom_personinfo.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		ft = getSupportFragmentManager().beginTransaction();
		switch (v.getId()) {
		
			case R.id.buttom_inspect:
				if(chooseIndex != 0 || isBack){
					chooseIndex = 0;
					tabBgChange(chooseIndex);
					ft.replace(R.id.fl_carinfo, ScanLshFrm.instantiate(MainActivity.this, ScanLshFrm.class.getName(), null),"ScanLshFrm");
					
					ft.replace(R.id.fl_titlebar, TitleBarFrm.newInstance(View.INVISIBLE, View.VISIBLE, View.INVISIBLE, "车辆查验"));
					
					isBack = false;
				}
				break;
				
				
			case R.id.buttom_personinfo:
				if(chooseIndex != 1 || isBack){
					chooseIndex = 1;
					tabBgChange(chooseIndex);
					ft.replace(R.id.fl_carinfo, PersonInfoFrm.instantiate(MainActivity.this, PersonInfoFrm.class.getName(), null),"PersonInfoFrm");
					ft.replace(R.id.fl_titlebar, TitleBarFrm.newInstance(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, ""));
					isBack = false;
				}
				break;
		}
		ft.commit();
	}

	
	private void tabBgChange(int chooseIndex2) {
		
		switch(chooseIndex2){
			case 0:
				buttom_inspect.setEnabled(false);
				buttom_personinfo.setEnabled(true);
				break;
			
			case 1:
				buttom_inspect.setEnabled(true);
				buttom_personinfo.setEnabled(false);
				break;
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		isRecycle = true;
		outState.putBoolean(MainActivity.ISRECYCLE, isRecycle);
		outState.putInt(MainActivity.CHOOSEINDEX, chooseIndex);
	}
	
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		isRecycle = savedInstanceState.getBoolean(MainActivity.ISRECYCLE);
		chooseIndex = savedInstanceState.getInt(MainActivity.CHOOSEINDEX);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isRecycle){
			switch (chooseIndex) {
				case 0:
					onClick(buttom_inspect);
					break;
					
				case 1:
					onClick(buttom_personinfo);
					break;
			}
		}
	}

	
	
	

	@Override
	public void onTitleBar() {
		
	}
	
}
