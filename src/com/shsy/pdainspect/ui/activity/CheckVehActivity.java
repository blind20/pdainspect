package com.shsy.pdainspect.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.shsy.pdainspect.BaseActivity;
import com.shsy.pdainspect.CommonConstants;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.adapter.TabAdapter;
import com.shsy.pdainspect.common.TitleBarView;
import com.shsy.pdainspect.network.MyHttpUtils;
import com.shsy.pdainspect.ui.fragment.CheckBasicInfoFrm;
import com.shsy.pdainspect.ui.fragment.CheckCyxmFrm;
import com.shsy.pdainspect.ui.fragment.CheckPhotoFrm;
import com.shsy.pdainspect.ui.fragment.DataCompareFrm;
import com.shsy.pdainspect.ui.fragment.ScanLshFrm;
import com.shsy.pdainspect.utils.AddressUrlUtils;
import com.shsy.pdainspect.utils.JsonArrayUtil;
import com.shsy.pdainspect.utils.Logger;
import com.shsy.pdainspect.utils.ToastUtils;
import com.shsy.pdainspect.utils.ToolUtils;
import com.viewpagerindicator.TabPageIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import okhttp3.Call;

public class CheckVehActivity extends BaseActivity implements View.OnClickListener,
CheckBasicInfoFrm.OnItemSelectedListener,CheckCyxmFrm.OnCyxmResultListener{

	protected TitleBarView mTitleBarView;
	private TabPageIndicator mTabPageIndicator;
	private ViewPager mViewPager;
	private TabAdapter mAdapter ;
	private List<Fragment> mFragments;
	private FragmentManager mManager;
	
	private String mArgument;
	private Map<String,Object> mVehCSLoginInfo;
	
	private int[] mCyxmResult;
	
	private Context mContext;
	@Override
	public int getLayoutResID() {
		return R.layout.aty_checkveh;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mArgument = getIntent().getStringExtra(ScanLshFrm.ARGUMENT);
		mContext = CheckVehActivity.this;
		super.onCreate(savedInstanceState);
	}

	@Override
	public void findView() {
		mTabPageIndicator = (TabPageIndicator)findViewById(R.id.tabIndicator);
		mViewPager = (ViewPager)findViewById(R.id.viewpager);
		mTitleBarView = (TitleBarView)findViewById(R.id.titlebar);
		mTitleBarView.setCommonTitle(View.VISIBLE, View.VISIBLE, View.VISIBLE);
		mTitleBarView.setTitle(getTitleByArgument(mArgument));
		mTitleBarView.setBtnRightOnclickListener(this);
		mTitleBarView.setBtnLeftOnclickListener(this);
	}

	

	@Override
	public void initParam() {
		mManager = getSupportFragmentManager();
		mFragments = initFragment();
		mAdapter = new TabAdapter(mManager,mFragments,TabAdapter.CHECK_VEH);
		mViewPager.setAdapter(mAdapter);
		mTabPageIndicator.setViewPager(mViewPager,0);
		
		//等待修复
		mCyxmResult = initCyxmResData(mArgument,20);
	}

	
	private int[] initCyxmResData(String argCyxm,int cyxmSize) {
		int[] record = new int[cyxmSize];
		for(int i=0;i<record.length;i++){
			record[i]=CommonConstants.NOTCHECK;
		}
		String[] cyxmMustArray = parseCyxm(argCyxm);
		if(cyxmMustArray!=null){
			for(int j=0;j<cyxmMustArray.length;j++){
				int check = parseMustCheckCyxm(cyxmMustArray[j]);
				record[check-1]=CommonConstants.CHECKPASS;
			}
		}
		return record;
	}

	private List<Fragment> initFragment() {
		List<Fragment> fms = new ArrayList<Fragment>();
		fms.add(CheckBasicInfoFrm.newInstance(mArgument));
		fms.add(CheckPhotoFrm.newInstance(mArgument));
		fms.add(CheckCyxmFrm.newInstance(CheckCyxmFrm.CYZL_VEH,mArgument));
		return fms;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_right:
				upLoadCheckRecord(mArgument);
				break;
			case R.id.title_left:
				finish();
				break;
		}
	}

	private void upLoadCheckRecord(String argument) {
		Map<String, String> checkInfo = getVehCsCheckInfoByMap(mVehCSLoginInfo);
		/*for(Map.Entry<String, String> entry: checkInfo.entrySet()){
			Logger.show("response", "key="+entry.getKey()+",value="+entry.getValue()+"\n");
		}*/
		if(validateMap(checkInfo)){
			String url=AddressUrlUtils.getAddressUrl(AddressUrlUtils.WRITEURL);
			postCheckRecordNetwork(url,checkInfo);
		}
	}
	
	
	private void postCheckRecordNetwork(final String url,final Map<String, String> checkrecord) {
		
		checkrecord.put("jkid", "18CB1");
		MyHttpUtils.getInstance(CheckVehActivity.this).postHttpByParam(url, checkrecord, new StringCallback() {
			
			@Override
			public void onResponse(String response, int id) {
				Logger.show("upLoadCheckRecord", "upLoadCheckRecord="+response);
				try {
					JSONObject jo = new JSONArray(response).getJSONObject(0);
					String res = "";
					String code = jo.getString("code");
					if(CommonConstants.XMLCODE_OK.equals(code)){
						res = "查验结果上传成功";
						ToastUtils.showToast(CheckVehActivity.this, res, Toast.LENGTH_SHORT);
						applicationAudit(url,checkrecord);
					}else{
						res = "上传失败,原因:"+jo.getString("message");
						ToastUtils.showToast(CheckVehActivity.this, res, Toast.LENGTH_LONG);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(Call call, Exception e, int id) {
				ToastUtils.showToast(CheckVehActivity.this, "网络问题,上传失败", Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
		});
	}
	
	
	/**
	 * 申请审核
	 */
	private void applicationAudit(String url,Map<String, String> map){
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("lsh", map.get("lsh"));
		param.put("jkid", "18CB3");
		
		MyHttpUtils.getInstance(CheckVehActivity.this).postHttpByParam(url, param, new StringCallback() {
			
			@Override
			public void onResponse(String response, int id) {
				Logger.show("applicationAudit", "applicationAudit="+response);
				try {
					JSONObject jo = new JSONArray(response).getJSONObject(0);
					String res = "";
					String code = jo.getString("code");
					if(CommonConstants.XMLCODE_OK.equals(code)){
						res = "申请审核成功";
						ToastUtils.showToast(CheckVehActivity.this, res, Toast.LENGTH_LONG);
						CheckVehActivity.this.finish();
					}else{
						res = "申请审核失败,原因:"+jo.getString("message");
						ToastUtils.showToast(CheckVehActivity.this, res, Toast.LENGTH_LONG);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(Call call, Exception e, int id) {
				ToastUtils.showToast(CheckVehActivity.this, "网络问题,申请审核失败,重新申请", Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
		});
	}
	
	
	
	/*private Map<String, String> test() {
		Map<String, String> map =  new HashMap<String, String>();
		map.put("jkid", "18CB1");
		map.put("lsh", "1170313007505");
		map.put("hpzl", "02");
		map.put("syr", "测试用户1");
		map.put("cyrq", "2017-03-14 09:01:54");
		map.put("cyyxm", "杨潇骏");
		map.put("cyysfzmhm", "320483199009220017");
		map.put("ywlx", "A");
		map.put("ywyy", "A");
		map.put("syxz", "A");
		map.put("clsbdh", "LGBH52E27HY017233");
		map.put("cyjl", "1");
		map.put("syr", "测试用户1");
		map.put("cyrq", "2017-03-14 09:01:54");
		map.put("cyyxm", "杨潇骏");
		for(int i=0;i<20;i++){
			map.put("cyxm"+(i+1), "0");
		}
		return map;
	}*/
	
	
	private Map<String, Object> getVehCSLoginInfoByJson(String json){
		return JsonArrayUtil.parseJSon2Map(json);
	}

	
	/**
	 * 组织上传数据
	 * @param map
	 * @return
	 */
	private Map<String, String> getVehCsCheckInfoByMap(Map<String, Object> map) {
		Map<String, String> vehCSCheckInfo = new HashMap<String, String>();
		vehCSCheckInfo.put("lsh", (String)map.get("lsh"));
		if(!(map.get("hphm")instanceof ArrayList)){
			vehCSCheckInfo.put("hphm", (String)map.get("hphm"));
		}
		if(!(map.get("hpzl")instanceof ArrayList)){
			vehCSCheckInfo.put("hpzl", (String)map.get("hpzl"));
		}
		if(!(map.get("syxz")instanceof ArrayList)){
			vehCSCheckInfo.put("syxz", (String)map.get("syxz"));
		}
		
		vehCSCheckInfo.put("clsbdh", (String)map.get("clsbdh"));
		vehCSCheckInfo.put("syr", (String)map.get("syr"));
		vehCSCheckInfo.put("ywlx", (String)map.get("ywlx"));
		vehCSCheckInfo.put("ywyy", (String)map.get("ywyy"));
		vehCSCheckInfo.put("cyzl", (String)map.get("cyzl"));
		vehCSCheckInfo.put("cyjl", scanCyxmResult(mCyxmResult));
		vehCSCheckInfo.put("cyyxm", "杨潇骏");
		vehCSCheckInfo.put("cyysfzmhm", "320483199009220017");
		vehCSCheckInfo.put("cyrq", ToolUtils.getCurDate());
		
		for(int i=0;i<20;i++){
			vehCSCheckInfo.put("cyxm"+(i+1), mCyxmResult[i]+"");
		}
		if("A".equals(vehCSCheckInfo.get("ywlx"))){
			vehCSCheckInfo.put("cyxm4", (String)map.get("csys"));
			vehCSCheckInfo.put("cyxm5", (String)map.get("hdzk"));
			vehCSCheckInfo.put("cyxm6", (String)map.get("cllx"));
		}
		
//		for(int i=0;i<20;i++){
//			Logger.show("getVehCsCheckInfoByMap", "cyxm"+i+":"+vehCSCheckInfo.get("cyxm"+i));
//		}
		
		return vehCSCheckInfo;
	}

	private String getTitleByArgument(String argument) {
		mVehCSLoginInfo = getVehCSLoginInfoByJson(argument);
		return (String) mVehCSLoginInfo.get("clsbdh");
	}
	
	
	private boolean validateMap(Map<String, String> map){
		if(TextUtils.isEmpty(map.get("cyzl"))){
			ToastUtils.showToast(mContext, "请先选择查验种类", Toast.LENGTH_LONG);
			return false;
		}
		return true;
	}
	
	@Override
	public void onItemSelected(String attri,String attrValue) {
		mVehCSLoginInfo.put(attri, attrValue);
	}
	

	@Override
	public void onCyxmResult(int position,int checkRst) {
		mCyxmResult[position]=checkRst;
	}

	private String scanCyxmResult(int[] result){
		for(int i=0;i<result.length;i++){
			if(result[i]==CommonConstants.CHECKFAIL){
				return CommonConstants.CYJL_FAILURE;
			}
		}
		return CommonConstants.CYJL_SUCCESS;
	}
	
	private String[] parseCyxm(String argCyxm) {
		String jo=null;
		try {
			JSONObject obj = new JSONObject(argCyxm);
			if(obj != null){
				if(!(obj.get("cyxm")instanceof JSONArray)){
					jo = obj.getString("cyxm");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		if(!TextUtils.isEmpty(jo)){
			return jo.split(",");
		}
		return null;
	}
	
	private int parseMustCheckCyxm(String data){
		String str = data.substring(2, 4);
		return Integer.parseInt(str);
	}
	
}
