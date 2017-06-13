package com.shsy.pdainspect.common;

import com.shsy.pdainspect.BaseActivity;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.ui.fragment.TitleBarFrm;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public abstract class SingleFrmActivity extends BaseActivity implements TitleBarFrm.OnTitleBarListener{

	
	private FragmentManager fm;
	private FragmentTransaction ft;

	protected abstract Fragment createFragment(); 
	protected abstract Fragment createTitleFragment(); 
	
	@Override
	public int getLayoutResID() {
		return R.layout.aty_common;
	}


	@Override
	public void initParam() {
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		Fragment titleFrm = fm.findFragmentById(R.id.fl_titlebar);
		Fragment contentFrm = fm.findFragmentById(R.id.fl_content);
		
		if(titleFrm==null){
			titleFrm = createTitleFragment();
		}
		if(contentFrm == null){;
			contentFrm = createFragment();
		}
		ft.replace(R.id.fl_titlebar, titleFrm);
		ft.replace(R.id.fl_content, contentFrm);
		ft.commit();
	}

	@Override
	public abstract void onTitleBar() ;
}
