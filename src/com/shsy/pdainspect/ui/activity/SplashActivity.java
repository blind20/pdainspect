package com.shsy.pdainspect.ui.activity;


import com.shsy.pdainspect.R;
import com.shsy.pdainspect.common.SingleFrmActivity;
import com.shsy.pdainspect.ui.fragment.LoginAppFrm;
import com.shsy.pdainspect.ui.fragment.SplashAppFrm;
import com.shsy.pdainspect.ui.fragment.TitleBarFrm;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;

public class SplashActivity extends SingleFrmActivity {
	
	private FrameLayout fl_titlebar;
	
	@Override
	public void findView() {
		fl_titlebar = (FrameLayout) findViewById(R.id.fl_titlebar);
		fl_titlebar.setVisibility(View.GONE);
	}

	@Override
	protected Fragment createFragment() {
		return new SplashAppFrm();
	}



	@Override
	protected Fragment createTitleFragment() {
		return TitleBarFrm.newInstance(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, "");
	}

	@Override
	public void onTitleBar() {
		
	}
	

}
