package com.shsy.pdainspect.ui.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.shsy.pdainspect.BaseFragment;
import com.shsy.pdainspect.CommonConstants;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.adapter.GridAdapter;
import com.shsy.pdainspect.entity.CarPhotoEntity;
import com.shsy.pdainspect.network.MyHttpUtils;
import com.shsy.pdainspect.ui.activity.CheckVehActivity;
import com.shsy.pdainspect.utils.AddressUrlUtils;
import com.shsy.pdainspect.utils.DensityUtil;
import com.shsy.pdainspect.utils.FileUtil;
import com.shsy.pdainspect.utils.JsonArrayUtil;
import com.shsy.pdainspect.utils.Logger;
import com.shsy.pdainspect.utils.PictureUtil;
import com.shsy.pdainspect.utils.ToastUtils;
import com.shsy.pdainspect.utils.ToolUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class CheckPhotoFrm extends BaseFragment {

	private GridView mGridView;
	private GridAdapter mGridAdapter;
	
	private List<CarPhotoEntity> mList;
	private boolean isRecycle = false;
	
	private String timeStamp;
	
	
	private Context context;
	private int mPosition;
	
	//照片类型：原始照片、上传照片、缩略图
	private final static int OriginType = 0;
	private final static int UploadType = 1;
	private final static int ThumbnaiType = 2;
	
	//图片质量
	private final static int UploadQuality =80;
	private final static int ThumbnailQuality =30;
	
	public static final int REQ_CAMERA_DATA = 100;
	public static final int REQ_SELECT_PHOTO = 101;
	
	public static final int OPTIONS_INSAMPLESIZE = 4;
	public static final String PHOTO_IS_MUST = "1";
	public static final String PHOTO_NOT_MUST = "0";
	
	public static final String ARG_CYZP = "arg_cyzp";
	private String mArgCyzp;
	
	private TextView mTextnote;
	private ProgressDialog mProgressDlg ;
	private int mWhich = -1;
	
	private Map<String, String> mVehCSCheckRecord;
	
	
	public static CheckPhotoFrm newInstance(String cyzp){
		Bundle bundle = new Bundle();
		bundle.putString(ARG_CYZP, cyzp); 
		CheckPhotoFrm checkPhotoFrm = new CheckPhotoFrm();
		checkPhotoFrm.setArguments(bundle);
		return checkPhotoFrm;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		if(bundle !=null){
			mArgCyzp = bundle.getString(ARG_CYZP);
		}
		super.onCreate(savedInstanceState);
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		if(savedInstanceState != null ){
			isRecycle = savedInstanceState.getBoolean("isRecycle");
			if(isRecycle){
				ArrayList list = savedInstanceState.getParcelableArrayList("list");
				mList = (List<CarPhotoEntity>) list.get(0);
			}
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context = mActivity;
	}


	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		isRecycle = true;
		ArrayList list = new ArrayList();
		list.add(mList);
		outState.putBoolean("isRecycle", isRecycle);
		outState.putParcelableArrayList("list", list);
	}
	

	@Override
	public void initParam() {
		findView();
		initView();
		viewSetAdapter();
	}

	
	private void findView() {
		mGridView = (GridView) mRootView.findViewById(R.id.grid_photo);
		mTextnote = (TextView) mRootView.findViewById(R.id.tv_note);
	}
	

	private void initView(){
		mList = initPhotoGrid(mArgCyzp);
	}
	

	@Override
	public int getLayoutResID() {
		return R.layout.frm_outer_photo;
	}
	
	
	private List<CarPhotoEntity> initPhotoGrid(String argCyzp) {
		List<CarPhotoEntity> list = initFullPhotoList(mActivity);
		String[] cyzps = parseCyzp(argCyzp);
		if(cyzps !=null){
			for(int i=0;i<list.size();i++){
				for(String cyzp:cyzps){
					if(cyzp.equals(list.get(i).getPhotoTypeCode())){
						list.get(i).setIsMustFlag(PHOTO_IS_MUST);
						break;
					}
				}
			}
		}
		return list;
	}
	
	private String[] parseCyzp(String argCyzp) {
		String jo=null;
		try {
			JSONObject obj = new JSONObject(argCyzp);
			if(obj != null){
				if(!(obj.get("cyzp")instanceof JSONArray)){
					jo = obj.getString("cyzp");
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
	
	
	
	private void viewSetAdapter() {
		mGridAdapter = new GridAdapter(mActivity, mList, 0);
		mGridView.setAdapter(mGridAdapter);
		
		
		mGridView.setOnItemClickListener(new OnItemClickListener(){
		
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mTextnote.setText("");
				mPosition = position;
				
				if(!TextUtils.isEmpty(mList.get(position).getUploadPhotoFilePath())){
					viewImgHasPhoto(position);
					
				}else{
					if(getSdcarState()){
						timeStamp = getCurrentTimeStamp();
						startCaptureAty(timeStamp);
					}else{
						ToastUtils.showToast(mActivity, getActivity().getString(R.string.sd_disable), Toast.LENGTH_LONG);
					}
				}
				
			}
		});
	}
	
	
	
	private void startCaptureAty(String timeStamp) {
		Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		String originPath = create3PhotoPathByType(timeStamp, CheckPhotoFrm.OriginType);
		File photoFile = new File(originPath);
		Uri fileUri = Uri.fromFile(photoFile);
		photoIntent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
	    startActivityForResult(photoIntent, CheckPhotoFrm.REQ_CAMERA_DATA);
	}
	
	
	private void viewImgHasPhoto(int position) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://"+mList.get(position).getUploadPhotoFilePath()), "image/*");
		startActivity(intent);
	}
	 
	
	

	/**
	 * 
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if( requestCode == CheckPhotoFrm.REQ_CAMERA_DATA ) {
	    	Logger.show("onActivityResult", "which"+mWhich);
	    	
	    	String[] pathArray = createUploadAndThumbnailFile(timeStamp, CheckPhotoFrm.UploadQuality);
	    	if(pathArray ==null){
	    		return;
	    	}
	        
	        CarPhotoEntity carPhoto = mList.get(mPosition);
	        carPhoto.setThumbnailBmp(BitmapFactory.decodeFile(pathArray[1]));
	        carPhoto.setUploadPhotoFilePath(pathArray[0]);
	        carPhoto.setThumbnailPhotoFilePath(pathArray[1]);
//	        mListener.onAddPhoto(carPhoto);
	        
	        mGridAdapter.setData(mList);
	        mGridAdapter.notifyDataSetChanged();
	        
	        String url=AddressUrlUtils.getAddressUrl(AddressUrlUtils.WRITEURL); 
	        uploadPhoto(url,carPhoto.getPhotoTypeCode(),carPhoto.getUploadPhotoFilePath());
	    }
	    
	    if(requestCode == CheckPhotoFrm.REQ_SELECT_PHOTO){
	    	mWhich = data.getIntExtra(MyDialogFragment.RES_SELECT_PHOTO, -1);
	    	timeStamp = getCurrentTimeStamp();
			startCaptureAty(timeStamp);
	    }
	}
	
	

	
	private void uploadPhoto(String url,String zpzl,String filepath) {
		Map<String,Object> vehCsLoginInfo = getVehCSLoginInfoByJson(mArgCyzp);
		Map<String, String> zpInfo = packZpInfoByArgu(vehCsLoginInfo,zpzl,filepath);
		Logger.show("zpInfo", "zpInfo="+zpInfo.get("zp"));
		showProgressDlg("上传照片");
		zpInfo.put("jkid", "18CB2");
		MyHttpUtils.getInstance(mActivity).postHttpByParam(url, zpInfo, new StringCallback() {
			
			@Override
			public void onResponse(String response, int id) {
				Logger.show("response", "response="+response);
				String result = parseResponse(response);
				mTextnote.setText(result);
				dismissProgressDlg();
			}
			
			@Override
			public void onError(Call call, Exception e, int id) {
				ToastUtils.showToast(mActivity, "网络问题,上传失败", Toast.LENGTH_SHORT);
				mTextnote.setText("网络问题,上传失败");
				dismissProgressDlg();
				e.printStackTrace();
			}
		});
	}
	
	
	private Map<String, String> packZpInfoByArgu(Map<String,Object> vehCsLoginInfo,String zpzl,String filepath){
		Map<String, String> vehZpInfo = new HashMap<String, String>();
		vehZpInfo.put("lsh", (String)vehCsLoginInfo.get("lsh"));
		if(!(vehCsLoginInfo.get("xh")instanceof ArrayList)){
			vehZpInfo.put("xh", (String)vehCsLoginInfo.get("xh"));
		}
		if(!(vehCsLoginInfo.get("hphm")instanceof ArrayList)){
			vehZpInfo.put("hphm", (String)vehCsLoginInfo.get("hphm"));
		}
		if(!(vehCsLoginInfo.get("hpzl")instanceof ArrayList)){
			vehZpInfo.put("hpzl", (String)vehCsLoginInfo.get("hpzl"));
		}
		vehZpInfo.put("clsbdh", (String)vehCsLoginInfo.get("clsbdh"));
		vehZpInfo.put("pssj", ToolUtils.getCurDate());
		vehZpInfo.put("zpzl", zpzl);
		vehZpInfo.put("zp", FileUtil.encodeBase64File(filepath));
		return vehZpInfo;
	}
	
	
	
	private String parseResponse(String response) {
		String result="";
		try {
			JSONObject jo = new JSONArray(response).getJSONObject(0);
			String code = jo.getString("code");
			if(CommonConstants.XMLCODE_OK.equals(code)){
				result = "照片上传成功";
				ToastUtils.showToast(mActivity, result, Toast.LENGTH_LONG);
			}else{
				result = "上传失败,原因:"+jo.getString("message");
				ToastUtils.showToast(mActivity, result, Toast.LENGTH_LONG);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	

	private String[] createUploadAndThumbnailFile(String timeStamp,int compressQuality) {
		
		String originFilepath = create3PhotoPathByType(timeStamp, CheckPhotoFrm.OriginType);
		String uploadFilepath = create3PhotoPathByType(timeStamp, CheckPhotoFrm.UploadType);
		String thumbnailFilepath = create3PhotoPathByType(timeStamp, CheckPhotoFrm.ThumbnaiType);
		
		Bitmap uploadBmp = compressBitmapByInsample(originFilepath, OPTIONS_INSAMPLESIZE);
		if(uploadBmp == null){
			return null;
		}
		
		int screenW = (context.getResources().getDisplayMetrics().widthPixels 
				- DensityUtil.dip2px(context,20))/4;
		Bitmap thumbnailBmp = ThumbnailUtils.extractThumbnail(uploadBmp, screenW, screenW);
		
		PictureUtil.createFileIfNonOrDeleteIfExists(uploadFilepath);
		PictureUtil.createFileIfNonOrDeleteIfExists(thumbnailFilepath);
		
		PictureUtil.createFileIfNonOrDeleteIfExists(originFilepath);
		
		try {
			FileOutputStream uploadfos = new FileOutputStream(new File(uploadFilepath));
			FileOutputStream thumbnailfos = new FileOutputStream(new File(thumbnailFilepath));
			
			uploadBmp.compress(Bitmap.CompressFormat.JPEG, compressQuality, uploadfos);
			thumbnailBmp.compress(Bitmap.CompressFormat.JPEG, CheckPhotoFrm.ThumbnailQuality, thumbnailfos);
			uploadfos.flush();
			thumbnailfos.flush();
			uploadfos.close();
			thumbnailfos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		PictureUtil.galleryAddPhoto(context,originFilepath);
		PictureUtil.galleryAddPhoto(context,uploadFilepath);
		PictureUtil.galleryAddPhoto(context,thumbnailFilepath);
		
		return new String[]{uploadFilepath,thumbnailFilepath};
	}
	
	/**
	 * @param imagePath
	 * @param scale
	 * @return
	 */
	private Bitmap compressBitmapByInsample(String imagePath,int scale){
		
		if(!new File(imagePath).exists()){
			return null;
		}
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);
		
		options.inSampleSize = scale;
		options.inPurgeable = true;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
		
		return PictureUtil.drawTextToBitmap(bitmap, ToolUtils.getCurDate());
	}
	

	/**
	 * @param timeStamp
	 */
	protected String create3PhotoPathByType(String timeStamp,int type) {
		
		String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
		String fileName = "";
		switch(type){
			case CheckPhotoFrm.OriginType:
				fileName = "Origin_Veh_" + timeStamp + ".jpg";
				break;
			case CheckPhotoFrm.UploadType:
				fileName = "Small_Veh_" + timeStamp + ".jpg";
				break;
			case CheckPhotoFrm.ThumbnaiType:
				fileName = "Thumbnai_Veh_" + timeStamp + ".jpg";
				break;
		}
        
		return path + "/" + fileName;
    }
	
	
	private String getCurrentTimeStamp(){
		return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	}
	

	private boolean getSdcarState(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	
	public static List<CarPhotoEntity> initFullPhotoList(Context con){
		List<CarPhotoEntity> list = new ArrayList<CarPhotoEntity>();
		String[] photoNames=con.getResources().getStringArray(R.array.photoname);
		String[] photoCodes=con.getResources().getStringArray(R.array.photocode);
		for(int i=0;i<photoNames.length;i++){
			Bitmap bmp = BitmapFactory.decodeResource(con.getResources(), R.drawable.ic_photo_add);
			CarPhotoEntity carPhoto = new CarPhotoEntity(photoCodes[i],photoNames[i],bmp,"","",PHOTO_NOT_MUST);
			list.add(carPhoto);
		}
		return list;
	}
	
	
	private Map<String, Object> getVehCSLoginInfoByJson(String json){
		return JsonArrayUtil.parseJSon2Map(json);
	}
	
	
	private void showProgressDlg(String txt) {
		mProgressDlg = new ProgressDialog(mActivity);
		mProgressDlg.setMessage(txt);
		mProgressDlg.show();
	}
	
	private void dismissProgressDlg() {
		mProgressDlg.dismiss();
	}
	
}
