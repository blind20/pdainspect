package com.shsy.pdainspect.ui.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.network.MyHttpUtils;
import com.shsy.pdainspect.ui.activity.LoginActivity;
import com.shsy.pdainspect.utils.AddressUrlUtils;
import com.shsy.pdainspect.utils.Logger;
import com.shsy.pdainspect.utils.PhoneUtils;
import com.shsy.pdainspect.utils.ProgressDlgUtil;
import com.shsy.pdainspect.utils.ToastUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import okhttp3.Call;


public class SplashAppFrm extends BaseFragment {

	private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private long id;
    private Timer mTimer;
    private TimerTask downloadTask;
    private DownloadManager.Query query = new DownloadManager.Query();
	
	public SplashAppFrm() {
	}
	
	@Override
	public int getLayoutResID() {
		return R.layout.frm_splash;
	}

	@Override
	public void initParam() {
		initDownLoadMag();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				getVersionInfo();
			}
		}, 2000);
	}

//	String downloadUrl = "http://192.168.1.220:8080/test/pdafile/pdainspect.apk";
	String downloadUrl = "http://ucdl.25pp.com/fs08/2017/01/20/2/2_87a290b5f041a8b512f0bc51595f839a.apk";
	private void initDownLoadMag() {
//		String downloadUrl=AddressUrlUtils.getAddressUrl(AddressUrlUtils.APKURL);
		
		downloadManager = (DownloadManager) mActivity.getSystemService(mActivity.DOWNLOAD_SERVICE);
		request = new DownloadManager.Request(Uri.parse(downloadUrl));
		request.setTitle("版本更新");
		request.setMimeType("application/vnd.android.package-archive");
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
		Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app-release.apk");
	}

	private void getVersionInfo(){
		String url=AddressUrlUtils.getAddressUrl(AddressUrlUtils.VERSIONURL);
		url = "http://192.168.1.220:8080/test/pdafile/version.json";
		Map<String, String> map = new HashMap<String, String>();
		ProgressDlgUtil.showProgressDialog(mActivity, "连接网络");
		MyHttpUtils.getInstance(mActivity).postHttpByParam(url, map, new StringCallback() {
			@Override
			public void onResponse(String response, int id) {
				try {
					int versionCode = new JSONObject(response).getInt("versionCode");
					int appVersion = PhoneUtils.getVersionCode(mActivity);
					Logger.show("versionCode", "versionCode"+versionCode);
					if(versionCode > appVersion){
						ProgressDlgUtil.dismissProgressDialog();
						downLoadApk();
					}else{
						ProgressDlgUtil.dismissProgressDialog();
						startAty();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					ProgressDlgUtil.dismissProgressDialog();
				}
			}
			
			@Override
			public void onError(Call call, Exception e, int id) {
				Logger.show("onError", "err"+e);
				ProgressDlgUtil.dismissProgressDialog();
//				ToastUtils.showToast(mActivity, "网络无法连接", Toast.LENGTH_LONG);
//				mActivity.finish();
				downLoadApk();
				e.printStackTrace();
			}
		});
	}
	
	private void downLoadApk(){
		id = downloadManager.enqueue(request);
		ProgressDlgUtil.showProgressDialog(mActivity, "检查到新版本,正在更新");
		Logger.show("downLoadApk", "downLoadApk");
		mTimer = new Timer();
		downloadTask = new TimerTask() {
			@Override
			public void run() {
				Cursor cursor = downloadManager.query(query.setFilterById(id));
				if (cursor != null && cursor.moveToFirst()) {

					if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) 
							== DownloadManager.STATUS_SUCCESSFUL) {
						
						installApk(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
								+ "/app-release.apk");
						ProgressDlgUtil.dismissProgressDialog();
						downloadTask.cancel();
						startAty();
						
					} 
				}
				cursor.close();
			}
		};
		mTimer.schedule(downloadTask, 0, 1000);
		downloadTask.run();
	}
	
	private void installApk(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
	
	private void startAty(){
		startActivity(new Intent(mActivity, LoginActivity.class));
		mActivity.finish();
	}
}
