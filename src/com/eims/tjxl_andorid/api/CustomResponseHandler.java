package com.eims.tjxl_andorid.api;

import java.net.SocketTimeoutException;

import org.apache.http.Header;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.utils.ToolsLoadDialog;
import com.eims.tjxl_andorid.weght.ProgressView;

import loopj.android.http.AsyncHttpResponseHandler;
import android.content.Context;
import android.widget.Toast;

public class CustomResponseHandler extends AsyncHttpResponseHandler {

	public static final String TAG = "CustomResponseHandler";
	public boolean isShowLoading = false;
//public ProgressView mProgressView;
	private Context mContext;
   private String msg;
	public CustomResponseHandler(Context context, boolean isShowLoading,String msg) {
		this.isShowLoading = isShowLoading;
		this.mContext = context;
		//mProgressView = new ProgressView(mContext, R.style.loading_dilog);
		this.msg=msg;
	}

	public CustomResponseHandler(Context context) {
		this.mContext = context;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (isShowLoading) {
			ToolsLoadDialog.showLoadDialog(mContext,msg);
		}
	}

	@Override
	public void onFinish() {
		super.onFinish();
	
		ToolsLoadDialog.colesLoadDialog();

	}



	@SuppressWarnings("deprecation")
	@Override
	public void onFailure(Throwable error, String content) {

		if (error instanceof SocketTimeoutException) {
			/* 服务器响应超时 */
			//Toast.makeText(mContext, "连接超时", Toast.LENGTH_SHORT).show();
			TipToast.makeText(mContext, "服务器异常", 0).show();
		} else {
		//	Toast.makeText(mContext, "网络错误", Toast.LENGTH_SHORT).show();
		//	TipToast.makeText(mContext, "服务器异常", 0).show();
		}
		// } else {
		// Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_LONG)
		// .show();
		// }
		
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onSuccess(int statusCode, Header[] headers, String content) {
		super.onSuccess(statusCode, headers, content);
		// Log.i("Response_code", content);
		try {
			switch (statusCode) {
			case 200:
				onRefreshData(content);
				break;
			case 401:
				// onFailure("401", mContext.getString(R.string.error_401));
				break;
			case 403:
				// onFailure("404", mContext.getString(R.string.error_404));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Tip.showTips(mContext, R.drawable.tips_toast_error, "网络不给力！");
		}
	}

	public void onRefreshData(String content) {
	}
}
