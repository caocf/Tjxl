/**
 * 
 */
package com.eims.tjxl_andorid.ui.user;

import org.json.JSONObject;

import loopj.android.http.RequestParams;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.WebViewUtil;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 服务协议
 * @Author zd
 * @date 2015年6月23日  下午4:01:43
 *************************************************************************** 
 */
public class ServerXieyiActivity extends BaseActivity{
	private HeadView head;
	private WebView webview;
	private String dataHtml;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server_xieyi_layout);
		head = (HeadView) findViewById(R.id.head);
		webview=(WebView) findViewById(R.id.webview);
		initActionBar();
		loaddata();
	}
	
	private void initActionBar() {
		head.setText("服务协议");
		head.setRightGone();
	}
	
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews() {
		//svmain.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(dataHtml)) {
			loadWebData();
		} else {
			dataHtml = "暂无信息";
			loadWebData();
		}
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
		webSettings.setSupportMultipleWindows(true);
		webSettings.setSupportZoom(true);
		webSettings.setDefaultZoom(ZoomDensity.FAR);
	//	webSettings.setUseWideViewPort(true); 
		//webSettings.setLoadWithOverviewMode(true); 
//		webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

		// 设置自动加载图片
		webSettings.setLoadsImagesAutomatically(true);
		//设置让webview中的图片自适应屏幕的大小
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		webview.setBackgroundResource(R.color.transparent); // 设置背景色
		webview.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		webview.setVisibility(View.INVISIBLE);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 结束
				super.onPageFinished(view, url);
				webview.setVisibility(View.VISIBLE);
			}

		});
	}
	public void loadWebData() {
		if (webview != null) {
			webview.loadDataWithBaseURL(null,
					WebViewUtil.initWebViewFor19(dataHtml), "text/html",
					"utf-8", null);
		}
	}
	
	private void  loaddata(){
		CustomResponseHandler handler=new CustomResponseHandler(mContext, true, "正在加载..."){
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				LogUtil.d(TAG, content);
				try {
			       JSONObject object=new JSONObject(content);
			      dataHtml= object.getString("data");
			      initViews();
				} catch (Exception e) {
				e.printStackTrace();
				}
			}
	};
		RequestParams params=new RequestParams();
		HttpClient.iRegistAgreement(handler, params);
	}

}
