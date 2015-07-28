/**
 * 
 */
package com.eims.tjxl_andorid.ui.product;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.SimpleWebAdapter;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.utils.ToolsLoadDialog;
import com.eims.tjxl_andorid.utils.WebViewUtil;
import com.eims.tjxl_andorid.weght.HeadView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 图文详情
 * @Author zd
 * @date 2015年7月2日  下午2:14:37
 *************************************************************************** 
 */
public class GraphicDetailsActivity extends BaseActivity {
	
	private HeadView head;
	public static String IMAGES_TAG="imgArrs";
	private String imgs;
	private ListView mylistview;
	private String[] strArrs;
	private WebView  webview;
	private String dataHtml;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tuwendetail_layout);
		findviews();
		setActionBar();
		initdata();
	}
	private void findviews(){
		head=(HeadView) findViewById(R.id.head);
		mylistview=(ListView) findViewById(R.id.mylistview);
		webview=(WebView) findViewById(R.id.webview);
	}
	private void setActionBar() {
		head.setText("图文详情");
		head.setGobackVisible();
		head.setRightGone();
	}
	private  void  initdata(){
		if(getIntent()!=null){
			Bundle bundle = getIntent().getExtras();
			dataHtml= bundle.getString(IMAGES_TAG);
//			imgs=imgs.substring(0, imgs.length()-1);
//			strArrs = imgs.split(",");
//			showUI();
			initViews();
		}
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
		webview.setBackgroundResource(R.color.transparent); // 设置背景色
		webview.getBackground().setAlpha(0); // 设置填充透明度 范围：0-255
		webview.setVisibility(View.INVISIBLE);
		ToolsLoadDialog.showLoadDialog(mContext,"正在加载...");
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// 结束
				super.onPageFinished(view, url);
				ToolsLoadDialog.colesLoadDialog();
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
	
	
	private void showUI(){
		 DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .showImageOnFail(R.drawable.default_icons)
        .delayBeforeLoading(0)
         .cacheOnDisc(false)
          .resetViewBeforeLoading(true)
         .displayer(new FadeInBitmapDisplayer(100))
         .showStubImage(R.drawable.loading)
//           .showImageOnLoading(R.drawable.loading) //设置图片在下载期间显示的图片  
      .showImageForEmptyUri(R.drawable.ic_launcher)
        .bitmapConfig(Bitmap.Config.RGB_565)
//        .imageScaleType(ImageScaleType.EXACTLY)
        .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
        .build();
		 
		if (isFinishing()) {
			return;
		}
		mylistview.setDividerHeight(0);
		SimpleWebAdapter adapter=new SimpleWebAdapter(this, strArrs);
		mylistview.setAdapter(adapter);	
	}
}
