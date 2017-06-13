package com.shsy.pdainspect.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shsy.pdainspect.CommonConstants;
import com.shsy.pdainspect.R;
import com.shsy.pdainspect.common.CommonAdapter;
import com.shsy.pdainspect.common.ViewHolder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

public class MyDialogFragment extends DialogFragment {
	
	protected OnCheckJudgeListener mListener;
	
	public final static String DIALOGTYPE = "Dialog_Type";
	
	public static final String RES_CHECKRST = "res_checkrst";
	public static final String RES_SELECT_PHOTO = "res_photo_type";
	public static final String TITLE = "title";
	public static final String MESSAGE = "message";
	
	public static final String ISCONFIRM = "isConfirm";
	public static final String REQUESTCODE = "requestCode";
	
	public static final int DLG_PHOTO_TYPE = 0x12;
	public static final int DLG_CHECK = 0x10;
	public static final int DLG_CONFIRM = 0x11;
	
	public interface OnCheckJudgeListener{
		public void onCheckJudge(int result);
	}
	
	public static MyDialogFragment newInstance(int type){
		
		MyDialogFragment dialog = new MyDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(DIALOGTYPE, type);
		dialog.setArguments(bundle);
		return dialog;
	}
	
	public static MyDialogFragment newInstance(int type,String title,String msg,int requestCode){
		
		MyDialogFragment dialog = new MyDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(DIALOGTYPE, type);
		bundle.putString(MyDialogFragment.TITLE, title);
		bundle.putString(MyDialogFragment.MESSAGE, msg);
		bundle.putInt(MyDialogFragment.REQUESTCODE, requestCode);
		dialog.setArguments(bundle);
		return dialog;
	}
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(true);
		int style = DialogFragment.STYLE_NO_TITLE;
		int theme = 0; 
		setStyle(style, theme);
	}

	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		int dialog_type = getArguments().getInt(DIALOGTYPE);
		String title = getArguments().getString(TITLE);
		String msg = getArguments().getString(MESSAGE);
		
		AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
		builder.setTitle(title);
		switch (dialog_type) {
			case MyDialogFragment.DLG_CHECK:
				/*builder.setTitle(getTag()).setAdapter(new CommonAdapter<CheckStateHolder>(initCheckDlgData(),
						getActivity(),R.layout.item_dialog_define){
					@Override
					public void convert(ViewHolder holder, CheckStateHolder t) {
						holder.setText(R.id.item_text, t.text).setImageResource(R.id.item_imageview, t.ImageID);
					}
					
				},new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setResult(judgeResult(which));
					}
					
				});*/
				builder.setAdapter(new CommonAdapter<CheckStateHolder>(initCheckDlgData(),
						getActivity(),R.layout.item_dialog_define){

							@Override
							public void convert(ViewHolder holder, CheckStateHolder t) {
								holder.setText(R.id.item_text, t.text).setImageResource(R.id.item_imageview, t.ImageID);
							}
				},new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						setCheckResult(judgeResult(which));
					}
				});
				break;
				
			case MyDialogFragment.DLG_PHOTO_TYPE:
				builder.setTitle(getTag()).setAdapter(new ArrayAdapter<String>(getActivity(), 
						android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.photoname)), 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
//								setResultForSelectPhoto(which);
							}
						} );
				break;
			
			case MyDialogFragment.DLG_CONFIRM:
				
				final int requestCode = getArguments().getInt(MyDialogFragment.REQUESTCODE);
				builder.setMessage(msg)
						.setPositiveButton(getString(R.string.positive), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								setResultForConfirmDlg(requestCode,Activity.RESULT_OK,true);
							}
						})
						.setNegativeButton(getString(R.string.negative), new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
							}
						});
				
			default:
				break;
		}
		
		return builder.create();
	}

	
	private void setCheckResult(int checkRst){
		if(getTargetFragment()!=null){
			Intent intent = new Intent();
			intent.putExtra(MyDialogFragment.RES_CHECKRST, checkRst);
			getTargetFragment().onActivityResult(CheckCyxmFrm.REQ_CHECKITEM, Activity.RESULT_OK, intent);
		}
	}


	/*private void setResultForSelectPhoto(int which){
		Intent intent = new Intent();
		intent.putExtra(MyDialogFragment.RES_SELECT_PHOTO, which);
		getTargetFragment().onActivityResult(OuterPhotoFrm.REQ_SELECT_PHOTO, Activity.RESULT_OK, intent);
	}*/
	
	
	private void setResultForConfirmDlg(int requestCode,int resultCode,boolean isConfirm){
		if(getTargetFragment()!=null){
			Intent intent = new Intent();
			intent.putExtra(MyDialogFragment.ISCONFIRM, isConfirm);
			getTargetFragment().onActivityResult(requestCode, resultCode, intent);
		}
	}
	
	
	
	@Override
	public void onSaveInstanceState(Bundle arg0) {
		super.onSaveInstanceState(arg0);
	}
	
	
	/*
	 * ==================================查验判定对话框初始化===========================================================
	 */
	private List<CheckStateHolder> initCheckDlgData(){
		
		List<CheckStateHolder> list = new ArrayList<CheckStateHolder>();
		
		list.add(new CheckStateHolder(getString(R.string.checkpass),R.drawable.checkflg_pass));
		list.add(new CheckStateHolder(getString(R.string.checkfail),R.drawable.checkflg_fail));
		list.add(new CheckStateHolder(getString(R.string.notcheck),R.drawable.checkflg_notcheck));
		
		return list;
	}
	
	public static class CheckStateHolder{  
        public String text;  
        public int ImageID;  
        public CheckStateHolder(String title,int imageID){  
            this.text = title;  
            this.ImageID = imageID;  
        }  
    }
	
	private int judgeResult(int which){
		int imageId = initCheckDlgData().get(which).ImageID;
		if(R.drawable.checkflg_pass == imageId){
			return CommonConstants.CHECKPASS;
		}else if(R.drawable.checkflg_fail == imageId){
			return CommonConstants.CHECKFAIL;
		}else{
			return CommonConstants.NOTCHECK;
		}
	}
	/*
	 * ====================================查验判定对话框初始化=========================================================
	 */
}
