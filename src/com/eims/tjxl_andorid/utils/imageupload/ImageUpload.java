package com.eims.tjxl_andorid.utils.imageupload;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.utils.Base64;

import java.io.File;
import java.util.ArrayList;

import loopj.android.http.RequestParams;


public class ImageUpload {
	private Context mContext;
	private ArrayList<String> hostimageurls = null;
	private ArrayList<String> netimageurls = null;
	private int upimage = 0;
	private UpLoadImageListener mUpLoadImageListener;

	public ImageUpload(Context mContext, ArrayList<String> hostimageurls, UpLoadImageListener mUpLoadImageListener) {
		this.mContext = mContext;
		this.hostimageurls = hostimageurls;
		this.mUpLoadImageListener = mUpLoadImageListener;
		netimageurls = new ArrayList<String>();
	}

	public void reLoad() {
		if (hostimageurls != null && hostimageurls.size() > 0) {
			Tip.showLoadDialog(mContext, "正在上传");
			if (netimageurls == null)
				netimageurls = new ArrayList<String>();
			imageup(hostimageurls.get(upimage));
		} else if (mUpLoadImageListener != null) {
			mUpLoadImageListener.UpLoadFail();
		}
	}

	public void startLoad() {
		this.reLoad();
	}

	private void imageup(String path) {
		File file = new File(path);
		if(!file.exists()){
			Tip.colesLoadDialog();
			if (mUpLoadImageListener != null) {
				mUpLoadImageListener.UpLoadFail();
			}
			//file is not exists
			return;
		}

		String data = null;
		try {
			Bitmap temp = BitmapUtils.showimageFull(path, 1024, 1024);
			data = Base64.imgToBase64(temp, Bitmap.CompressFormat.PNG);
			temp.recycle();
		}catch(Exception e){
			e.printStackTrace();
			Tip.colesLoadDialog();
			if (mUpLoadImageListener != null) {
				mUpLoadImageListener.UpLoadFail();
			}
			return;
		}

		// ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// try {
		// fis = new FileInputStream(path);
		// byte[] tempBuffer = new byte[2048];
		// int count = 0;
		// while ((count = fis.read(tempBuffer)) >= 0)
		// {
		// baos.write(tempBuffer, 0, count);
		// }
		// fis.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// Tip.colesLoadDialog();
		// }
		// data = new String(Base64.encode(baos.toByteArray()));

		MyCustomResponseHandler handler = new MyCustomResponseHandler(mContext, false) {

			@Override
			public void onRefreshData(String content) {
				ImageUrlBean bean = ImageUrlBean.explainJson(content, mContext);
				if (bean.type == 1 && bean.data != null) {
					netimageurls.add(bean.data);
					if (hostimageurls.size() - 1 > upimage) {
						upimage++;
						imageup(hostimageurls.get(upimage));
					} else {
						Tip.colesLoadDialog();
						if (mUpLoadImageListener != null) {
							mUpLoadImageListener.UpLoadSuccess(netimageurls);
						}
					}
				} else {
					Tip.colesLoadDialog();
					Toast.makeText(mContext, "上传失败!", Toast.LENGTH_SHORT).show();
					if (mUpLoadImageListener != null) {
						mUpLoadImageListener.UpLoadFail();
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Tip.colesLoadDialog();
				Toast.makeText(mContext, "上传失败!", Toast.LENGTH_SHORT).show();
				if (mUpLoadImageListener != null) {
					mUpLoadImageListener.UpLoadFail();
				}
				return;
			}
		};
		//TODO
		RequestParams params=new RequestParams();
		params.put("fileType","png");
		params.put("data",data);
		HttpClient.iAppImgUpload(handler,params);
	}

	public interface UpLoadImageListener {

		void UpLoadSuccess(ArrayList<String> netimageurls);

		void UpLoadFail();

	}

	public void upLoadHeadImage() {
		this.reLoad();
	}
}
