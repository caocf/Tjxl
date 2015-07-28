/**
 * 
 */
package com.eims.tjxl_andorid.ui.product;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.utils.WebViewUtil;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年7月21日  上午9:50:58
 *************************************************************************** 
 */
public class ShareActivity extends BaseActivity {
	private HeadView head;
	private WebView webview;
	private String datahtml=URLs.SERVER_URL+"appDown.do";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuwendetail_layout);
		findviews();
		setActionBar();
		initViews();
	}
	
	private void findviews(){
		head=(HeadView) findViewById(R.id.head);	
		webview=(WebView) findViewById(R.id.webview);
	}
	private void setActionBar() {
		head.setText("分享");
		head.setGobackVisible();
		head.setRightGone();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initViews() {
	
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webview.setBackgroundResource(R.color.transparent); // 设置背景色
		webview.setVisibility(View.INVISIBLE);
		webview.loadUrl(datahtml);
		webview.setWebChromeClient(new WebChromeClient());
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 结束
				super.onPageFinished(view, url);
				webview.setVisibility(View.VISIBLE);
			}

		});
	}


}
