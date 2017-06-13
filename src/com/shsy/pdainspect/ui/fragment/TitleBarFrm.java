package com.shsy.pdainspect.ui.fragment;

import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.common.TitleBarView;
import com.shsy.pdainspect.utils.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class TitleBarFrm extends BaseFragment {

	private TitleBarView mTitleBarView;
	
	private Integer leftVisibility;
	private Integer centerVisibility;
	private Integer rightVisibility;
	private String titleText;
	
	public static final String LEFTVISIBILITY = "leftVisibility";
	public static final String CENTERVISIBILITY = "centerVisibility";
	public static final String RIGHTVISIBILITY = "rightVisibility";
	public static final String TITLETXT = "titletxt";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(savedInstanceState == null){
			Bundle bundle = getArguments();
			if(bundle !=null){
				leftVisibility = bundle.getInt(LEFTVISIBILITY);
				centerVisibility = bundle.getInt(CENTERVISIBILITY);
				rightVisibility = bundle.getInt(RIGHTVISIBILITY);
				titleText = bundle.getString(TITLETXT);
				Logger.show("titlebar", "title = " + titleText);
			}
		}
	}
	
	public static TitleBarFrm newInstance(int left,int center,int right,String title){
		Bundle bundle = new Bundle();  
        bundle.putString(TITLETXT, title); 
        bundle.putInt(LEFTVISIBILITY, left); 
        bundle.putInt(CENTERVISIBILITY, center); 
        bundle.putInt(RIGHTVISIBILITY, right); 
        TitleBarFrm titleBarFrm = new TitleBarFrm();
        titleBarFrm.setArguments(bundle);
        return titleBarFrm;
	}
	
	
	@Override
	public int getLayoutResID() {
		return R.layout.frm_titlebar;
	}

	@Override
	public void initParam() {
		initView();
		setView(leftVisibility,centerVisibility,rightVisibility);
	}

	private void initView() {
		mTitleBarView = new TitleBarView(mActivity);
		mTitleBarView = (TitleBarView) mRootView.findViewById(R.id.titlebar);
		mTitleBarView.setCommonTitle(leftVisibility, centerVisibility, rightVisibility);
	}
	
	

	private void setView(int left,int center,int right) {
		mTitleBarView.setTitle(titleText);
		
		if(leftVisibility == View.VISIBLE){
			mTitleBarView.setBtnLeftOnclickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCallback.onTitleBar();
				}
			});
		}
		
		if(rightVisibility == View.VISIBLE){
			mTitleBarView.setBtnRightOnclickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mCallback.onTitleBar();
				}
			});
		}
	}
	
	
	
	
	protected OnTitleBarListener mCallback;
	public interface OnTitleBarListener{
		public void onTitleBar();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (OnTitleBarListener) mActivity;
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw new ClassCastException(mActivity.toString()
					+ " must implement OnTitleBarListener");
		}
	}
	
}
