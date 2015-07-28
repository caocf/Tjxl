package com.eims.tjxl_andorid.api;

import java.net.SocketTimeoutException;

import loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.xlistview.XListLayout;


public class ListResponseHandler extends AsyncHttpResponseHandler {
    private final static String TAG = "ListResponseHandler";
    
	private XListLayout mXListLayout;
	private int mLoadType = 1;

	public ListResponseHandler(XListLayout xListLayout, int loadType) {
		mXListLayout = xListLayout;
		mLoadType = loadType;
	}

	@Override
	public void onStart() {
		super.onStart();
	//	LogUtil.d(TAG, "----->onStart");
		mXListLayout.start();
	}

	@Override
	public void onFinish() {
		super.onFinish();
	//	LogUtil.d(TAG, "----->onFinish");
		mXListLayout.success();
	}

	@Override
	public void onFailure(Throwable error, String content) {
		super.onFailure(error, content);
		if (error instanceof SocketTimeoutException) {
			/* 服务器响应超时 */
		} else {
		}
		//LogUtil.d(TAG, "error-->" + content);
		LogUtil.d(TAG, "----->onFailure");
		mXListLayout.setErrorCode(XListLayout.ERROR_NOTNETWORK);
	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, String content) {
		super.onSuccess(statusCode, headers, content);
	//	LogUtil.d(TAG, "---------------onSuccess");
		try {
			switch (statusCode) {
			case 200:
				LogUtil.d(TAG, "---------------onSuccess:"+statusCode);
			    onRefreshData(content, mLoadType);
				break;
			case 401:
				LogUtil.d(TAG, "---------------onSuccess:  401");
			    mXListLayout.setErrorCode(XListLayout.ERROR_NOTNETWORK);
				break;
			case 403:
				LogUtil.d(TAG, "---------------onSuccess:  403");
			    mXListLayout.setErrorCode(XListLayout.ERROR_NOTNETWORK);
				break;
			default:
				LogUtil.d(TAG, "---------------onSuccess:  default");
			    mXListLayout.setErrorCode(XListLayout.ERROR_NOTNETWORK);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.d(TAG, "---------------onSuccess:  Exception");
			mXListLayout.setErrorCode(XListLayout.ERROR_NOTDATA);
		}
	}
	
	public void onRefreshData(String content, int loadType){
	}

}
