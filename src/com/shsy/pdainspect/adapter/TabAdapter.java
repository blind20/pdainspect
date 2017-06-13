package com.shsy.pdainspect.adapter;


import java.util.List;

import com.shsy.pdainspect.CommonConstants;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class TabAdapter extends FragmentPagerAdapter {
	
	public static final int CHECK_VEH = 0; 
	public static final int JSCS_VEH = 1; 
	
	public static String[] CHECK_VEH_TITLES = new String[]
			{ "查验基础信息", "查验拍照","查验项目"};
	public static String[] VEH_JSCS_TITLES = new String[]
			{ "查验登记信息", "公告参数信息"};

	public String[] TITLES;
	
	public List<Fragment> mFragments;
	public Fragment currentFragment;

	public TabAdapter(FragmentManager fm) {
		super(fm);
	}
	
	public TabAdapter(FragmentManager fm,List<Fragment> fms,int type) {
		super(fm);
		this.mFragments = fms;
		switch (type) {
			case TabAdapter.CHECK_VEH:
				TITLES = CHECK_VEH_TITLES;
				break;
			case TabAdapter.JSCS_VEH:
				TITLES = VEH_JSCS_TITLES;
				break;
		}
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		if(TITLES == null){
			return 0;
		}
		return TITLES.length;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		if(TITLES == null){
			return null;
		}
		return TITLES[position];
	}
	
	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		currentFragment = (Fragment) object;
		super.setPrimaryItem(container, position, object);
	}
	
	
}
