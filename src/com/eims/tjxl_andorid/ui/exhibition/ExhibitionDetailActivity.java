package com.eims.tjxl_andorid.ui.exhibition;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.BannerAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.base.ImageDataLoader;
import com.eims.tjxl_andorid.entity.BannerBean;
import com.eims.tjxl_andorid.entity.IQueryExhibitionInfoBean;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.WebViewUtil;
import com.eims.tjxl_andorid.weght.BorderScrollView;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyWebView;
import com.eims.tjxl_andorid.weght.flow.CircleFlowIndicator;
import com.eims.tjxl_andorid.weght.flow.ViewFlow;

import org.apache.http.Header;

import java.util.ArrayList;

import loopj.android.http.RequestParams;

/**
 * 展厅详情
 * Created by kuangyong on 2015/6/30.
 */
public class ExhibitionDetailActivity extends BaseActivity {
	private static final String TAG = "ExhibitionDetailActivity";
	public static final String EXHIBITION_ID = "exhibition_id";
	private String exhibition_id;
	private String dataHtml;
	private HeadView head;
	private android.webkit.WebView webview;
	private IQueryExhibitionInfoBean bean;// 展厅该数据
//	private com.eims.tjxl_andorid.weght.BorderScrollView svmain;
	private ViewFlow mViewFlow;
	private CircleFlowIndicator indicator;
	private FrameLayout frameLayout;
	private BannerAdapter bannerAdapter;
	private ArrayList<BannerBean.BannerItem> imagePath;
	private final int AD_WIDTH = 480;
	private final int AD_HEIGHT = 251;
//	private View imageBanar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_exhibition_detail);
		exhibition_id = getIntent().getStringExtra(EXHIBITION_ID);
		findView();
		iQueryExhibitionInfo();
		ImageDataLoader loader=new ImageDataLoader(mContext,ImageDataLoader.EXHIBITION_DETAIL_PAGE);
		loader.setOnLoaderCompleteListener(new ImageDataLoader.OnLoaderCompleteListener() {
			@Override
			public void onComplete(ArrayList<BannerBean.BannerItem> imagePath) {
				ExhibitionDetailActivity.this.imagePath = imagePath;
				initBannerPager();
			}
		});
	}

	private void initheadview() {
		String titleName = bean.getData().getExhibition();
		head.setText(titleName);
		head.setGobackVisible();
		head.setRightResource();
	}

	private void findView() {
//		this.svmain = (BorderScrollView) findViewById(R.id.sv_main);
		this.webview = (MyWebView) findViewById(R.id.webview);
		this.head = (HeadView) findViewById(R.id.head);
		frameLayout = (FrameLayout) findViewById(R.id.view_flow_frame);
		mViewFlow = (ViewFlow)findViewById(R.id.viewflow);
		indicator = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
	}

	private void initBannerPager() {
		// TODO Auto-generated method stub
		WindowManager manager = getWindowManager();
		Point point = AppContext.getPICSize(manager);
		int PIC_WID = point.x;// 屏幕的宽
		int bitmapWidth = AD_WIDTH;
		int bitmapHeight = AD_HEIGHT;
		int wedgitWidth = PIC_WID;
		// 根据容器的宽度去缩放图片的高度
		int scallHeight = AppContext.getScallZoomHeight(manager, bitmapHeight,
				bitmapWidth, wedgitWidth);
		// System.out.println("x:"+point.x+"------Y:"+point.y+"高度："+scallHeight);
		frameLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, scallHeight));

		bannerAdapter = new BannerAdapter(this);
		bannerAdapter.addItem(imagePath);
		mViewFlow.setmSideBuffer(imagePath.size());
		mViewFlow.setAdapter(bannerAdapter);
		mViewFlow.setFlowIndicator(indicator);
		mViewFlow.setTimeSpan(3000);
		mViewFlow.setSelection(0); // 设置初始位置
		mViewFlow.startAutoFlowTimer();
//		webview.addView(imageBanar);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("SetJavaScriptEnabled")
	private void initViews() {

//		svmain.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(dataHtml)) {
			loadWebData();
		} else {
			dataHtml = "暂无信息";
			loadWebData();
		}
		//设置缩放开关
		webview.getSettings().setSupportZoom(true);
		//4.4以上的缩放支持
		if(android.os.Build.VERSION.SDK_INT>=19) {
			webview.getSettings().setBuiltInZoomControls(true);
		}
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setDisplayZoomControls(true);
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
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

	private void iQueryExhibitionInfo() {
		CustomResponseHandler handler = new CustomResponseHandler(mContext,
				true, "正在加载...") {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					String content) {
				super.onSuccess(statusCode, headers, content);
				if (LogUtil.isDebug) {
					LogUtil.i(TAG, "展厅详情数据---" + content);
				}
				bean = IQueryExhibitionInfoBean.explainJson(content, mContext);
				if (null != bean && bean.getData() != null) {
					dataHtml=bean.getData().getDescription();
					initViews();
					initheadview();
				}else {
					Toast.makeText(ExhibitionDetailActivity.this,"获取的数据为空", Toast.LENGTH_SHORT).show();
				}
			}
		};
		RequestParams params = new RequestParams();
		params.put("id", exhibition_id);
		HttpClient.iQueryExhibitionInfo(handler, params);
    }
}
