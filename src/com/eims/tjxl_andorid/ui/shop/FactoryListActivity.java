package com.eims.tjxl_andorid.ui.shop;

import java.util.ArrayList;

import loopj.android.http.RequestParams;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.MyListAdapter.onItemClickListener;
import com.eims.tjxl_andorid.adapter.StarFactoryGridAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.IQueryStarFactoryPageBean;
import com.eims.tjxl_andorid.entity.StarFactoryBean;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.home.SearchActivity;
import com.eims.tjxl_andorid.ui.product.FactoryActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.Utils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyPopupWindow;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshBase;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshGridView;
import com.google.gson.Gson;

/**
 * 搜索厂家的结果显示页面
 * @author lzh
 *
 */
public class FactoryListActivity extends BaseActivity implements OnClickListener{
	
	private static final String TAG1 = "FactoryListActivity";
	public static final String KYE_WORD = "key_word";
	private View view_back;
	private ImageButton right_menu_btn;
	private TextView serach_edit;
	private PullToRefreshGridView factoryGridView;
	private TextView tip_no_content;
	
	private MyPopupWindow popupWindow;
	private ArrayList<String> items;
	private ArrayList<StarFactoryBean> productList;
	private StarFactoryGridAdapter adapterGv;// 缩略图适配器
	
	//请求参数
	private int pageIndex = 1;
	private int pageSize = 6;// 每页多少条数据
	
	private String keyWord;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_factory_list);
		initData();
		findView();
		loadData();
	}
	
	private void findView(){
		view_back = findViewById(R.id.view_back);
		right_menu_btn = (ImageButton) findViewById(R.id.right_menu_btn);
		serach_edit = (TextView) findViewById(R.id.serach_edit);
		factoryGridView = (PullToRefreshGridView) findViewById(R.id.gv_pro);
		tip_no_content = (TextView) findViewById(R.id.tip_no_content);
		
		view_back.setOnClickListener(this);
		setRightMenuClickListener();
		serach_edit.setText(keyWord);
		factoryGridView.setAdapter(adapterGv);
		
		setPullRefreshView();
	}
	
	private void initData(){
		keyWord = getIntent().getExtras().getString(KYE_WORD);
		items = new ArrayList<String>();
		productList = new ArrayList<StarFactoryBean>();
		
		adapterGv = new StarFactoryGridAdapter(this, productList);
	}
	
	private void loadData(){
		CustomResponseHandler mHandler = new CustomResponseHandler(this, true, "正在加载...") {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				Log.d(TAG1, "onSuccess result is :"+content);
				if(JSONParseUtils.isErrorJSONResult(content)){
					Log.d(TAG1, "onSuccess ,load factory list faulure");
				}else {
					ArrayList<StarFactoryBean> list = (ArrayList<StarFactoryBean>) JSONParseUtils.parseFactoryListInfo(content);
					
					if(list.size() == 0 && pageIndex > 1){
						Toast.makeText(FactoryListActivity.this, "已没有更多数据", Toast.LENGTH_SHORT).show();
						return;
					}
					productList.addAll(list);
					Log.d(TAG1, "productList size = "+ productList.size());
					if(productList.size() == 0){
						tip_no_content.setVisibility(View.VISIBLE);
						factoryGridView.setVisibility(View.INVISIBLE);
						return;
					}
					adapterGv.notifyDataSetChanged();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				factoryGridView.onRefreshComplete();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "load factory list onFailure ,error msg:"+content);
				factoryGridView.onRefreshComplete();
				tip_no_content.setVisibility(View.VISIBLE);
				factoryGridView.setVisibility(View.INVISIBLE);
				adapterGv.notifyDataSetChanged();
				Toast.makeText(FactoryListActivity.this, "网络错误",
						Toast.LENGTH_LONG).show();
			}
		};
		RequestParams params = new RequestParams();
		params.put("kword", keyWord + "");
		HttpClient.loadFactorySearchResult(mHandler, params);
	}
	
	private void setPullRefreshView() {

		factoryGridView.getLoadingLayoutProxy(false, true).setPullLabel("往下拉更新数据...");
		factoryGridView.getLoadingLayoutProxy(false, true).setReleaseLabel("放开更新数据...");
		factoryGridView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在载入中...");

		factoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
//				int index = position - gv_pro.getHeadCount();
				Bundle bundle=new Bundle();
				bundle.putString(FactoryActivity.ID,productList.get(position).getUid());
				ActivitySwitch.openActivity(FactoryActivity.class, bundle, FactoryListActivity.this);
			}
		});
		factoryGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				productList.clear();
				pageIndex = 1;
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView) {
				loadData();
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.view_back:
			ActivitySwitch.finishActivity(this);
			break;

		default:
			break;
		}
	}
	
	private void setRightMenuClickListener(){
		
		right_menu_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				popupWindow = new MyPopupWindow(FactoryListActivity.this,
						right_menu_btn.getWidth() * 3, HeadView.items);
				// popupWindow.setAnimationStyle(R.style.PopupAnimation);
				// popupWindow.setAnimationStyle(Style.CONFIRM);
				popupWindow.showAsDropDown(right_menu_btn, 0, 0);
				popupWindow.update();
				popupWindow.setOnItemClickListener(new onItemClickListener() {
					@Override
					public void click(int position, View view) {
						AppManager.getAppManager().finishToHome();
						if (position != 4) {
							MainActivity.radioGroup.getChildAt(position)
									.performClick();
							MainActivity.mainPager.setCurrentItem(position);
						} else {
							ActivitySwitch.openActivity(SearchActivity.class,
									null, (Activity) FactoryListActivity.this);
						}

					}
				});
			}
		});
	}
}
