package com.shsy.pdainspect.ui.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shsy.pdainspect.BaseActivity;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.adapter.TabAdapter;
import com.shsy.pdainspect.common.TitleBarView;
import com.shsy.pdainspect.ui.fragment.CheckBasicInfoFrm;
import com.shsy.pdainspect.ui.fragment.VehGonggaoInfoFrm;
import com.shsy.pdainspect.ui.fragment.VehLoginInfoFrm;
import com.viewpagerindicator.TabPageIndicator;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

public class GonggaoActivty extends BaseActivity implements View.OnClickListener,CheckBasicInfoFrm.OnItemSelectedListener{

	protected TitleBarView mTitleBarView;
	private TabPageIndicator mTabPageIndicator;
	private ViewPager mViewPager;
	private TabAdapter mAdapter ;
	private List<Fragment> mFragments;
	private FragmentManager mManager;
	
	private String mArgument;
	private Map<String,Object> mVehCSLoginInfo;
	
	
	private Context mContext;

	@Override
	public int getLayoutResID() {
		return R.layout.aty_checkveh;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mArgument = getIntent().getStringExtra(CheckBasicInfoFrm.ARGUMENT);
		mContext = GonggaoActivty.this;
		super.onCreate(savedInstanceState);
	}

	@Override
	public void findView() {
		mTabPageIndicator = (TabPageIndicator)findViewById(R.id.tabIndicator);
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mTitleBarView = (TitleBarView)findViewById(R.id.titlebar);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.GONE);
		mTitleBarView.setTitle("技术参数信息");
		mTitleBarView.setBtnLeftOnclickListener(this);
	}

	@Override
	public void initParam() {
		mManager = getSupportFragmentManager();
		mFragments = initFragment();
		mAdapter = new TabAdapter(mManager,mFragments,TabAdapter.JSCS_VEH);
		mViewPager.setAdapter(mAdapter);
		mTabPageIndicator.setViewPager(mViewPager,0);
	}
	
	private List<Fragment> initFragment() {
		List<Fragment> fms = new ArrayList<Fragment>();
		fms.add(VehLoginInfoFrm.newInstance(mArgument));
		fms.add(VehGonggaoInfoFrm.newInstance(mArgument));
		return fms;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_left:
				finish();
				break;
		}
	}

	@Override
	public void onItemSelected(String attri, String attrValue) {
		
	}

}
