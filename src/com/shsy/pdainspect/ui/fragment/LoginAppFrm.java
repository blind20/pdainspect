package com.shsy.pdainspect.ui.fragment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.CommonConstants;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.ui.activity.MainActivity;
import com.shsy.pdainspect.utils.AddressUrlUtils;
import com.shsy.pdainspect.utils.Logger;
import com.shsy.pdainspect.utils.SharedPreferenceUtils;
import com.shsy.pdainspect.utils.ToastUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Call;

public class LoginAppFrm extends BaseFragment {
	
	private EditText et_username;
	private EditText et_password;
	private Button btn_login;
	private TextView tv_hint;
	
	
	private String mUsername;
	private String mPassword;
	
	public LoginAppFrm() {
	}

	
	private Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0x10:
                    String response=(String)msg.obj;
                    Logger.show("handler", "handler = " + response);
                    if(isUserLock(response,mUsername)){
//        				String loginUrl=AddressUrlUtils.getAddressUrl(AddressUrlUtils.LOGINURL);
//        				Login(mUsername,mPassword,loginUrl);
        				
                    	startActivity();
                    }else{
                    	tv_hint.setVisibility(View.VISIBLE);
                    	tv_hint.setText("使用有效期已到,请及时联系车管所延期");
                    };
            }
        }
    };
	
	@Override
	public int getLayoutResID() {
		return R.layout.frm_login;
	}

	@Override
	public void initParam() {
		initView();
		setView();
		setListener();
	}

	private void initView() {
		et_username = (EditText) mRootView.findViewById(R.id.et_username);
		et_password = (EditText) mRootView.findViewById(R.id.et_password);
		btn_login = (Button)mRootView.findViewById(R.id.btn_login);
		tv_hint =  (TextView) mRootView.findViewById(R.id.tv_hint);
	}

	private void setListener() {
		
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mUsername = et_username.getText().toString().trim();
				mPassword = et_password.getText().toString().trim();
				Logger.show("onClick", "onClick");
				
				//测试链接
				String propUrl = "http://192.168.0.14:8080/web/pda/aa.properties";
				
//				String propUrl = AddressUrlUtils.getAddressUrl(AddressUrlUtils.PROPURL);
				urlPostRequest(propUrl,"GBK");
				
			}
		});
	}
	
	

	private void Login(final String username, final String password,String url) {
		if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
			ToastUtils.showToast(mActivity, "请输入用户名,密码", Toast.LENGTH_SHORT);
			return;
		}
		Logger.show("response", "username:"+username+",password:"+password);
		OkHttpUtils.post().url(url).addParams("xm",username)
		.addParams("sfzmhm",password).build().execute(new StringCallback() {
			
			@Override
			public void onResponse(String response, int id) {
				Logger.show("response", "response"+response);
				try {
					JSONObject jo = new JSONObject(response);
					Integer state = (Integer) jo.get("state");
					if(CommonConstants.STATE_SUCCESS == state){
						SharedPreferenceUtils.put(mActivity, CommonConstants.USERNAME, username);
						SharedPreferenceUtils.put(mActivity, CommonConstants.PASSWORD, password);
						startActivity();
					}else if(CommonConstants.STATE_FAIL == state){
						ToastUtils.showToast(mActivity, "登陆失败"+jo.getString("message"), Toast.LENGTH_LONG);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(Call call, Exception e, int id) {
				ToastUtils.showToast(mActivity, "网络问题", Toast.LENGTH_LONG);
				Logger.show("onerror", "onerror");
				e.printStackTrace();
			}
		});
	}

	private void startActivity(){
		Intent intent = new Intent(mActivity, MainActivity.class);
		startActivity(intent);
		mActivity.finish();
		tv_hint.setVisibility(View.INVISIBLE);
	}
	
	private void setView() {
		mUsername = (String) SharedPreferenceUtils.get(mActivity, CommonConstants.USERNAME, "");
		mPassword = (String) SharedPreferenceUtils.get(mActivity, CommonConstants.PASSWORD, "");
		
		if("".equals(mUsername)||"".equals(mPassword)){
			return;
		}else{
			et_username.setText(mUsername);
			et_password.setText(mPassword);
		}
	}
	
	
	private boolean isUserLock(String response,String userName){
		String[] array = response.split(",|，");
		for(String name : array){
			Logger.show("isUserLock", name);
			name.trim();
			if(!TextUtils.isEmpty(name) ){
				if(name.indexOf(userName)>=0){
					String[] props = name.split("=");
					if(userName.equals(props[0].trim())){
						if(isValidate(props[1].trim())){
							Logger.show("isValidate", "通过");
							return true;
						}else{
							return false;
						}
					}
				}
				
			}
		}
		return true;
	}
	
	private boolean isValidate(String strDate) {
		Date now = new Date();
		Date propDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			propDate = sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(propDate!=null){
			return now.before(propDate);
		}
		return false;
	}
	
	/**
     * HttpURLConnection方式访问prop文件
     * 无中文乱码
     * @param path
     * @param encoding
     */
	private void urlPostRequest(final String path, final String encoding) {
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpURLConnection con =null;
				try {
					URL url = new URL(path);
					con = (HttpURLConnection) url.openConnection();
					con.setDoInput(true);
					con.setDoOutput(true);
					con.setRequestMethod("POST");
					con.setUseCaches(false);
					con.setRequestProperty("Content-Type", encoding);
					con.setConnectTimeout(5000);
					con.setReadTimeout(5000);
					con.connect();
					InputStream in = con.getInputStream();
					BufferedReader bufr = new BufferedReader(new InputStreamReader(in,encoding));
					StringBuilder response = new StringBuilder();
					String line = null;
					while ((line = bufr.readLine()) != null) {
						response.append(line);
					}
					
					Message message=new Message();
                    message.what=0x10;
                    message.obj=response.toString();
                    handler.sendMessage(message);
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					if(con !=null){
						con.disconnect();
					}
				}
				
			}
		}).start();
	}
}
