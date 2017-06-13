package com.shsy.pdainspect.ui.fragment;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.CommonConstants;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.network.MyHttpUtils;
import com.shsy.pdainspect.ui.activity.CheckVehActivity;
import com.shsy.pdainspect.utils.AddressUrlUtils;
import com.shsy.pdainspect.utils.Logger;
import com.shsy.pdainspect.utils.SharedPreferenceUtils;
import com.shsy.pdainspect.utils.ToastUtils;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.Call;

public class ScanLshFrm extends BaseFragment implements View.OnClickListener{

	public static final String ARGUMENT = "argument";
	
	private EditText et_lsh;
	private Button btn_scan;
	private Button btn_search;
	
	public ScanLshFrm() {
	}

	@Override
	public int getLayoutResID() {
		return R.layout.frm_scan_lsh;
	}

	@Override
	public void initParam() {
		initView();
		setView();
	}

	private void initView() {
		et_lsh = (EditText) mRootView.findViewById(R.id.et_lsh);
		btn_scan = (Button) mRootView.findViewById(R.id.btn_scan);
		btn_search = (Button) mRootView.findViewById(R.id.btn_search);
	}

	private void setView() {
		btn_scan.setOnClickListener(this);
		btn_search.setOnClickListener(this);
	}
	
	
	
	@Override
	public void onClick(View v) {
		
		Intent intent = new Intent();
		switch (v.getId()) {
		
			case R.id.btn_scan:
                intent.setClass(mActivity, CaptureActivity.class);
                startActivityForResult(intent,0);
				break;
	
			case R.id.btn_search:
                
//                String lsh = et_lsh.getText().toString();
//                String url=AddressUrlUtils.getAddressUrl(AddressUrlUtils.CHECKURL);
//                getVehInfoByLshNetwork(lsh,url);
				
				/**测试 start**/
				String body = "{\"yxqz\":\"2019-03-31 00:00:00.0\",\"jyw\":\"421F070504040701020C02080B747708027500010864383E07363A43396B535E347C0006030D030273FFE17CE09781FEF4D083F7FCF52E340C435F425C6F3022324B33333031303130312C3031303230312C3031303230322C3031303330312C3031303330322C3031303430312C3031303530312C3031303630312C3031303730322C3031303730332C3031303830312C3031303930322C303130393031303130332C303130372C303130382C303139382C30313939303230312C303230322C303230332C303230342C303230352C303230362C303231302C3032333933333233\",\"ywcyxm\":\"011001,011002,011003,011201,011101\",\"csys\":\"AHJ\",\"hphm\":\"D691WR\",\"qpzk\":[],\"fzjg\":\"苏D\",\"qzbfqz\":\"2099-12-31 00:00:00.0\",\"clxh\":\"DFL7205ASL2\",\"sjhm\":\"18260493139\",\"gcjk\":\"A\",\"dybj\":\"0\",\"hxnbkd\":[],\"ccrq\":\"2016-10-25 00:00:00.0\",\"hxnbgd\":[],\"ccdjrq\":\"2017-03-07 12:38:21.0\",\"hxnbcd\":[],\"zzg\":\"156\",\"clpp1\":\"英菲尼迪牌\",\"ywlx\":\"A\",\"zzl\":\"2200\",\"cyzp\":\"0103,0107,0108,0198,0199\",\"hdzk\":\"5\",\"qlj\":\"1547\",\"zj\":\"2898\",\"hdzzl\":[],\"cllx\":\"K33\",\"zxxs\":\"1\",\"zs\":\"2\",\"hpzk\":[],\"zt\":\"A\",\"hpzl\":\"02\",\"yqjyqzbfqz\":\"2024-03-31 00:00:00.0\",\"ywyy\":\"A\",\"ytsx\":\"9\",\"lsh\":\"1170307008608\",\"clyt\":\"P1\",\"djzsbh\":\"320024255024\",\"djrq\":[],\"syqsrq\":\"2017-03-07 00:00:00.0\",\"ywcyzp\":[],\"cwkc\":\"4840\",\"syr\":\"刘佳怡\",\"zsxxdz\":\"江苏省常州市武进区遥观镇遥观村委大岸塘6号\",\"cwkg\":\"1456\",\"gbthps\":[],\"zqyzl\":[],\"cwkk\":\"1823\",\"bxzzrq\":\"2018-03-06 00:00:00.0\",\"zzxxdz\":\"江苏省常州市武进区遥观镇遥观村委大岸塘6号\",\"zbzl\":\"1675\",\"fdjxh\":\"274A\",\"hgzbh\":\"WAC2X1001175606\",\"xszbh\":\"3230025251865\",\"gl\":\"155\",\"hlj\":\"1566\",\"zlzp\":\"0201,0202,0203,0204,0205,0206,0210,0239\",\"ltgg\":\"225/55RF17\",\"syxz\":\"A\",\"cyxm\":\"010101,010201,010202,010301,010302,010401,010501,010601,010702,010703,010801,010902,010901\",\"lts\":\"4\",\"sfbz\":\"0\",\"rlzl\":\"A\",\"xh\":\"32040017053121\",\"zzcmc\":\"东风汽车有限公司\",\"fdjh\":\"E052567A\",\"clsbdh\":\"LGBW1PE00GR034933\",\"pl\":\"1991\"}";
				Intent intent2 = new Intent(mActivity,CheckVehActivity.class);
				intent2.putExtra(ScanLshFrm.ARGUMENT, body);
				startActivity(intent2);
				/**测试 end**/
				break;
		}
	} 
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0){
			if(data!=null){
				Bundle bundle = data.getExtras();
				if(bundle!=null){
					String lsh = bundle.getString("result");
					et_lsh.setText(lsh);
//					String url=AddressUrlUtils.getAddressUrl(AddressUrlUtils.CHECKURL);
//					getVehInfoByLshNetwork(lsh,url);
				}
			}
		}
	}

	
	
	private void getVehInfoByLshNetwork(String lsh,String url) {
		if(TextUtils.isEmpty(lsh)){
			ToastUtils.showToast(mActivity, "流水号为空", Toast.LENGTH_SHORT);
			return;
		}
		
		HashMap<String,String> map = new HashMap<String,String>();
		map.put("jkid", "18CA1");
		map.put("lsh", lsh);
		MyHttpUtils.getInstance(mActivity).postHttpByParam(url, map, new StringCallback() {
			
			@Override
			public void onResponse(String response, int id) {
				try {
					JSONArray ja = new JSONObject(response).getJSONArray("body");
					JSONObject jo = ja.getJSONObject(0);
					String body = jo.toString();
					Logger.show("body", "body="+body);
					if(!TextUtils.isEmpty(body)){
						Intent intent = new Intent(mActivity,CheckVehActivity.class);
						intent.putExtra(ScanLshFrm.ARGUMENT, body);
						startActivity(intent);
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(Call call, Exception e, int id) {
				ToastUtils.showToast(mActivity, "网络问题", Toast.LENGTH_SHORT);
				e.printStackTrace();
			}
		});
		
	}

	
}
