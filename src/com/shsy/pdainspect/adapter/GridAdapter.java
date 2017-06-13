package com.shsy.pdainspect.adapter;

import java.util.List;

import com.shsy.pdainspect.R;
import com.shsy.pdainspect.entity.CarPhotoEntity;
import com.shsy.pdainspect.ui.fragment.CheckPhotoFrm;
import com.shsy.pdainspect.utils.DensityUtil;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {
	private List<CarPhotoEntity> photoList;
	private Context context;
	private int screenW;
	private int status;
	
	public GridAdapter(Context context,List<CarPhotoEntity> mCopyList,int status){
		this.context= context;
		this.photoList= mCopyList;
		this.status = status;
		screenW = (context.getResources().getDisplayMetrics().widthPixels 
				- DensityUtil.dip2px(context,20))/4;
	}
	
	@Override
	public int getCount() {
		return photoList.size();
	}
	
	public void setData(List<CarPhotoEntity> mCopyList){
		this.photoList= mCopyList;
	}
	
	@Override
	public Object getItem(int position) {
		return null;
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.griditem_photo, null);
			convertView.setLayoutParams(new GridView.LayoutParams(screenW, screenW));
			
			viewHolder = new ViewHolder();
			
			viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_photo);
			viewHolder.imageDelete = (ImageView) convertView.findViewById(R.id.iv_del);
			viewHolder.textview = (TextView) convertView.findViewById(R.id.tv_photoname);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.imageView.setImageBitmap(photoList.get(position).getThumbnailBmp());
		viewHolder.textview.setText(photoList.get(position).getPhotoTypeName());
		
		if(photoList.get(position).getIsMustFlag().equals(CheckPhotoFrm.PHOTO_IS_MUST)){
			viewHolder.textview.setBackgroundResource(R.color.light_red);
		}else{
			viewHolder.textview.setBackgroundResource(R.color.common_title);
		}
		
		if(TextUtils.isEmpty(photoList.get(position).getUploadPhotoFilePath())){
			viewHolder.imageDelete.setVisibility(View.INVISIBLE);
		}else{
			viewHolder.imageDelete.setVisibility(View.VISIBLE);
		}
		viewHolder.imageDelete.setTag(position);
		viewHolder.imageDelete.setOnClickListener(new MyListener(position));
		
		return convertView;
	}
	
	class ViewHolder{
		ImageView imageView;
		ImageView imageDelete;
		TextView textview;
	}
	
	
	private class MyListener implements OnClickListener{
		int mPos;
		public MyListener(int position){  
			mPos = position;  
		}
		
		@Override
		public void onClick(View v) {
			if(mPos == getCount()-1){
				return;
			}
			photoList.get(mPos).setThumbnailBmp(null);
			photoList.get(mPos).setThumbnailPhotoFilePath("");
			photoList.get(mPos).setUploadPhotoFilePath("");
//			photoList.remove(mPos);
			setData(photoList);
			notifyDataSetChanged();
//			ToastUtils.showToast(context, "mPos:"+mPos, Toast.LENGTH_SHORT);
		}
	}
	
}
