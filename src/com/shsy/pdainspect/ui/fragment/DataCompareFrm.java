package com.shsy.pdainspect.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.common.CommonAdapter;
import com.shsy.pdainspect.common.ViewHolder;

import android.widget.ListView;

public class DataCompareFrm extends BaseFragment {

	private ListView mListView;
	private CommonAdapter<HashMap<String,String>> mAdapter;
	private List<HashMap<String,String>> mList;
	
	public DataCompareFrm() {
	}

	@Override
	public int getLayoutResID() {
		return R.layout.frm_data_comparison;
	}

	@Override
	public void initParam() {
		initView();
		initData();
		viewSetAdapter();
	}

	private void initView() {
		mListView = (ListView) mRootView.findViewById(R.id.listview);
	}

	private void initData() {
		mList = new ArrayList<HashMap<String,String>>();
		for(int i=0;i<10;i++){
			HashMap<String,String> map = new HashMap<String, String>();
			map.put("attr", "整备质量"+i);
			map.put("value1", "质量1000KG " + i);
			map.put("value2", "这样设置才会生效。在代码中设置时，需要setAdjustViewBounds为true在代码中设置时，需要setAdjustViewBounds为true" + i*9);
			mList.add(map);
		}
	}
	


	private void viewSetAdapter() {
		mAdapter = new CommonAdapter<HashMap<String,String>>(mList,mActivity,R.layout.item_list_tablerow) {
			
			@Override
			public void convert(ViewHolder holder, HashMap<String, String> t) {
				holder.setText(R.id.tv_row_attri,t.get("attr"));
				holder.setText(R.id.tv_row_value1,t.get("value1"));
				holder.setText(R.id.tv_row_value2,t.get("value2"));
			}
		};
		
		mListView.setAdapter(mAdapter);
	}
}
