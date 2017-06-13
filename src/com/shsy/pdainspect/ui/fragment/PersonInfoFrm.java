package com.shsy.pdainspect.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.CommonConstants;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.common.CommonAdapter;
import com.shsy.pdainspect.common.ViewHolder;
import com.shsy.pdainspect.utils.Logger;
import com.shsy.pdainspect.utils.PhoneUtils;
import com.shsy.pdainspect.utils.SharedPreferenceUtils;
import com.shsy.pdainspect.utils.ToastUtils;
import com.shsy.pdainspect.utils.ToolUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class PersonInfoFrm extends BaseFragment implements View.OnClickListener{

	private ListView mListView;
	private TextView tv_usrname;
	private ImageView iv_head;
	
	@Override 
	public int getLayoutResID() {
		return R.layout.frm_personinfo;
	}
	
	public void initView() {
		mListView = (ListView) mRootView.findViewById(R.id.listview);
		tv_usrname = (TextView) mRootView.findViewById(R.id.tv_usrname);
		iv_head = (ImageView) mRootView.findViewById(R.id.iv_head);
		
		String name = (String)SharedPreferenceUtils.get(mActivity, CommonConstants.USERNAME, "");
		if(!TextUtils.isEmpty(name)){
			tv_usrname.setText(name);
		}
	}

	@Override
	public void initParam() {

		initView();
		
		tv_usrname.setOnClickListener(this);
		iv_head.setOnClickListener(this);
		
		mListView.setAdapter(new CommonAdapter<ItemHolder>(initItems(),
				mActivity,R.layout.item_dialog_define) {
					@Override
					public void convert(ViewHolder holder, ItemHolder t) {
						holder.setText(R.id.item_text, t.text)
						.setImageResource(R.id.item_imageview, t.ImageID);
					}
		});
		
		onItemClick();
	}
	

	private void onItemClick(){
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position == 2){
					String version = PhoneUtils.getVersionName(mActivity);
					ToastUtils.showToast(mActivity, version, Toast.LENGTH_LONG);
				}
			}
		});
	}
	

	public List<ItemHolder> initItems(){
		List<ItemHolder> list = new ArrayList<PersonInfoFrm.ItemHolder>();
		
		String[] itemTexts = getResources().getStringArray(R.array.pernfrm_item);
		String[] itemImgs = getResources().getStringArray(R.array.pernfrm_item_img);
		for(int i=0;i<itemTexts.length;i++){
			int resId = getResources().getIdentifier(itemImgs[i], "drawable", "com.shsy.pdainspect");
			list.add(new ItemHolder(itemTexts[i], resId));
		}
		
		return list;
	}
	
	
	
	public static class ItemHolder{  
        public String text;  
        public int ImageID;  
        public ItemHolder(String title,int imageID){  
            this.text = title;  
            this.ImageID = imageID;  
        }  
    }



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
			case R.id.tv_usrname:
			case R.id.iv_head:
				break;
		}
	}
	
	
}
