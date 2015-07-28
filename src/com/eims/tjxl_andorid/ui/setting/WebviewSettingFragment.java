package com.eims.tjxl_andorid.ui.setting;

import loopj.android.http.RequestParams;

import org.apache.http.Header;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.entity.IQueryExhibitionInfoBean;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.WebViewUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("ValidFragment")
public class WebviewSettingFragment extends Fragment {

	private static final String TAG1 = "WebviewSettingFragment";
	private WebView webview;
	private String dataHtml;
	private int tag;

	public WebviewSettingFragment(int tag) {
		this.tag = tag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_webview_setting, null);
		webview = (WebView) view.findViewById(R.id.webview);
		setWebview();
		loadPageMessage();

		return view;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setWebview() {
		if (!TextUtils.isEmpty(dataHtml)) {
			loadWebData();
		} else {
			dataHtml = "暂无信息";
			loadWebData();
		}
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBlockNetworkImage(false);
		webSettings.setBlockNetworkLoads(false);
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

	private void loadPageMessage() {
		CustomResponseHandler handler = new CustomResponseHandler(
				WebviewSettingFragment.this.getActivity(), true, "正在加载...") {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				Log.d(TAG1, "page mesage result :" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG, "热销信息获取失败");
					return;
				} else {
					dataHtml = JSONParseUtils.getString(content, "data");
					Log.d("zhiheng", "dataHtml content =  "+Html.fromHtml(dataHtml));
					loadWebData();
				}
			}
		};
		RequestParams params = new RequestParams();
		// params.put("id", exhibition_id);
		switch (tag) {
		case SettingDetailActivity.TAG_ABOUT_US:
			HttpClient.loadAboutUsInfo(handler, params);
			break;
		case SettingDetailActivity.TAG_FUNCTION_INTRO:
			HttpClient.loadFunctionIntroInfo(handler, params);
			break;
		case SettingDetailActivity.TAG_USE_GUIDE:
			HttpClient.loadUseGuiderInfo(handler, params);
			break;

		default:
			break;
		}
	}
}
