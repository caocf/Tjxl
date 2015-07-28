package com.eims.tjxl_andorid.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.common.CommonConfrimCancelDialog;
import com.eims.tjxl_andorid.entity.ImageData;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.imageupload.ImageFolder;
import com.eims.tjxl_andorid.utils.imageupload.ImageUpload;
import com.eims.tjxl_andorid.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.eims.tjxl_andorid.utils.imageupload.SelectPicPopupWindow;

import java.io.File;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class BaseCameraFragment extends Fragment {

	private static final String TAG = "BaseCameraFragment";
	protected Context context;
	
	/**相机**/
	public static final int CAMER_CODE = 007;
	
	/**相册**/
	public static final int LOCAL_CODE = 006;
	
	/**当前弹出的popwindow**/
	private SelectPicPopupWindow mPopupWindow = null;
	
	/**当前照片的文件**/
	private File imageFile;
  
	/**图片上传类**/
	private ImageUpload mIUpload;
	
	/**是否裁剪，相机相册之后**/
	private boolean crop = true;
	
	/**是否需要上传图片**/
	private boolean needUpload;
	
	/**是否是上传头像**/
	private boolean isUploadUserHead;
	
	
	/**是否打开相机取图片**/
	private boolean isOpenCamera = false;
	
	/**
	 * 启动对话框的Activity去选择相机，相册
	 */
	protected void startCameraDialogActivity(boolean crop){
		
		Intent intentHead = new Intent(getActivity(), CommonConfrimCancelDialog.class);
		intentHead.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_CHOICE_UPLOAD_TYPE);
		startActivityForResult(intentHead, CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE);
	}
	
	/**
	 * 显示弹起菜单
	 * @param main
	 */
	protected void showCameraPopwindow(View main){
		showCameraPopwindow(main, true, true);
	}
	
	public void showCameraPopwindow(View main, final boolean crop, boolean needUpload){
		showCameraPopwindow(main, crop, needUpload, false);
	}
	
	/**
	 * <b>显示相机，相册PopWindow</br></b>
	 * 1.自动处理了图片上传功能</br>
	 * 2.需要重写 UpLoadSuccess方法来接受上传成功的图片</br>
	 * @param main
	 */
	public void showCameraPopwindow(View main, final boolean crop, boolean needUpload, boolean isUploadUserHead) {
		
		this.crop = crop;
		this.needUpload = needUpload;
		this.isUploadUserHead = isUploadUserHead;
		
		if (mPopupWindow == null) {
			mPopupWindow = new SelectPicPopupWindow(getActivity(), new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mPopupWindow.isShowing()) {
						mPopupWindow.dismiss();
					}
					switch (v.getId()) {
					case R.id.btn_take_photo: // 拍照
						callCamerImage(crop);
						break;
					case R.id.btn_pick_photo:
						callLocalImage(crop);
						break;
					}
				}
			});
		}
		if (!mPopupWindow.isShowing()) {
			mPopupWindow.showAtLocation(main, Gravity.BOTTOM, 0, 0);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		context=getActivity();
	}

	/**
	 * 打开相机
	 */
	private void callCamerImage(boolean crop) {

		Log.i(TAG, "启动照相机");
		imageFile = ImageFolder.getTempImageName();
		Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uri = Uri.fromFile(imageFile);
		captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		captureIntent.putExtra("return-data", true);
		
		if(crop){
			startActivityForResult(captureIntent, CAMER_CODE);
		}else{
			startActivityForResult(captureIntent, LOCAL_CODE);
		}
		
		//标记是打开相机
		isOpenCamera = true;
	}

	/**
	 * 打开本地图库
	 */
	private void callLocalImage(boolean crop) {

		Log.i(TAG, "打开图库");
		// Intent localIntent = new Intent(Intent.ACTION_GET_CONTENT);
		// localIntent.addCategory(Intent.CATEGORY_OPENABLE);
		// localIntent.setType("image/*");
		// // 是否调用系统裁剪
		// localIntent.putExtra("crop", "true");
		//
		// /* 图片宽高的像素值 */
		// localIntent.putExtra("outputX", 100);
		// localIntent.putExtra("outputY", 100);
		// localIntent.putExtra("return-data", true);
		// startActivityForResult(localIntent, LOCAL_CODE);
		imageFile = ImageFolder.getTempImageName();
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		
		//如果裁剪，则设置裁剪参数
		if(crop){
			intent.putExtra("crop", "true"); // 开启剪切
			intent.putExtra("aspectX", 1); // 剪切的宽高比为1：1
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 100); // 保存图片的宽和高
			intent.putExtra("outputY", 100);
		}
		
		intent.putExtra("output", Uri.fromFile(imageFile)); // 保存路径
		intent.putExtra("outputFormat", "JPEG");// 返回格式
		startActivityForResult(intent, LOCAL_CODE);
		
		//标记是打开图库
		isOpenCamera = false;
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		requestCode = curRequestCode;
		if (requestCode == CommonConfrimCancelDialog.CHOICE_UPLOAD_TYPE_CODE && resultCode == Activity.RESULT_OK) {
			//对话框Activity, setResult过来的
			
			/* 选择图片来源 */
			String uploadType = data.getStringExtra(CommonConfrimCancelDialog.TAG);
			if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_CAMER)) {
				
				/* 相机 */
				callCamerImage(this.crop);
			} else if (uploadType.equals(CommonConfrimCancelDialog.UPLOAD_TYPE_LOCAL)) {
				
				/* 图库 */
				callLocalImage(this.crop);
			}

		} else if (resultCode == Activity.RESULT_OK && requestCode == CAMER_CODE) {
			
			//相机拍照，返回，再跳转到裁剪界面
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(Uri.fromFile(imageFile), "image/*");
			intent.putExtra("crop", "true");
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("outputX", 100);
			intent.putExtra("outputY", 100);
			intent.putExtra("noFaceDetection", false);
			intent.putExtra("scale", true);
			imageFile = ImageFolder.getTempImageName();
			intent.putExtra("output", Uri.fromFile(imageFile));
			intent.putExtra("outputFormat", "JPEG");// 返回格式
			startActivityForResult(intent, LOCAL_CODE);
		} else if (resultCode == Activity.RESULT_OK && requestCode == LOCAL_CODE) {
			Log.i(TAG, "获取图片返回成功");
			
//			ArrayList<String> temp = new ArrayList<String>();
//			temp.add(imageFile.getPath());
			ArrayList<String> temp = getPhotoPath(data, crop, isOpenCamera);
			
			if(needUpload){
				//上传图片
				mIUpload = new ImageUpload(getActivity(), temp, uploadListener);
				if(isUploadUserHead){
					mIUpload.upLoadHeadImage();
				}else{
					mIUpload.startLoad();
				}
			}else{
				
				//将本地图片路径返回
				onUpLoadSuccess("", temp.get(0));
			}
		}
	}

	/**
	 * 取照片
	 * @param isCrop
	 * @param isOpenCamera
	 * @return
	 */
	private ArrayList<String> getPhotoPath(Intent data, boolean isCrop, boolean isOpenCamera) {
		ArrayList<String> temp = new ArrayList<String>();
		if (isOpenCamera) {
			
			//拍照是一定有路径的
			temp.add(imageFile.getAbsolutePath());
		} else {

			if(isCrop){
				
				//相册-裁剪也是一定有路径的
				temp.add(imageFile.getAbsolutePath());
				
			}else{
				
				//单独选择相册图片是没有图片的,要去图库查询
				if (data != null && data.getData() != null) {
					Uri selectedImage = data.getData();
					String[] filePathColumns = { MediaStore.Images.Media.DATA };
					Cursor c = ((Context)getActivity()).getContentResolver().query(selectedImage, filePathColumns, null, null, null);
					c.moveToFirst();
					int columnIndex = c.getColumnIndex(filePathColumns[0]);
					String picturePath = c.getString(columnIndex);
					c.close();
					temp.add(picturePath);
				}
			}

		}

		return temp;
	}
	
	
	
	private UpLoadImageListener uploadListener = new UpLoadImageListener() {
		
		@Override
		public void UpLoadSuccess(ArrayList<String> netimageurls) {
			if(null != netimageurls && netimageurls.size() > 0){
				
				LogUtil.i(TAG, "----图片上传成功-----");
				for(int i=0; i<netimageurls.size(); i++){
					
					//打印所有上传成功的图片
					LogUtil.i(TAG, "图片"+i+" url: "+ netimageurls.get(i));
					LogUtil.i(TAG, "图片"+i+" path: "+ imageFile.getPath());
					
					//通知到界面，刷新数据
					onUpLoadSuccess(netimageurls.get(i), imageFile.getPath());
				}
				
			}else{
				
				//图片上传失败
				UpLoadFail();
			}
			
		}
		
		@Override
		public void UpLoadFail() {
			//提示图片上传失败
			showMyToast(getString(R.string.toast_upload_failed));
		}
	};

	protected void showToast(String showText){
		if(null!=context){
			Toast.makeText(context,showText,Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 显示toast
	 * @param msg
	 */
	protected void showMyToast(String msg){
		Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
	}
	
	
	/**
	 * 图片上传成功，将图片url，和本地路径返回
	 * @param imageUrl
	 * @param imageFile
	 */
	public void onUpLoadSuccess(String imageUrl, String imageFile) {
	}


    /**
     * 批量上传图片
     * @param list
     */
    public void uploadImage(List<ImageData> list, UpLoadImageListener listener){

        //判断图片非空
        if(null == list || list.size() == 0){

            if(null != listener){
                listener.UpLoadSuccess(null);
            }
            return;
        }

        //上传图片
        ArrayList<String> imgs = new ArrayList<String>();
        ImageData imgData = null;
        for(int i=0; i<list.size(); i++){
            imgData = list.get(i);
            if(imgData.localFileExist()){
                imgs.add(list.get(i).fileName);
            }
        }
        ImageUpload mIUpload = new ImageUpload(getActivity(), imgs, listener);
        mIUpload.startLoad();

    }


	private int curRequestCode = 0;
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		this.curRequestCode = requestCode;
	}
}
