package com.eims.tjxl_andorid.ui.product;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.ClassifyRankBean;
import com.eims.tjxl_andorid.entity.FilterCatogeryItemBean;
import com.eims.tjxl_andorid.entity.FilterSelectItem;
import com.eims.tjxl_andorid.ui.product.ClassifyListFragment.OnCanRefreshListener;
import com.eims.tjxl_andorid.ui.shop.ShopClassifyActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.weght.HeadView;

public class ClassifySearchActivity extends BaseActivity {

	private static final String TAG1 = "ClassifySearchActivity";
	private HeadView titleView;
	private LayoutInflater mInflater;
	private List<ClassifyRankBean> mDatas;
	private ClassifyRankBean selectedBean;

	private FragmentManager fragmentManager;
	private ClassifyListFragment fragmentRankFirst;
	private ClassifyListFragment fragmentRankSecond;
	
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classify_search);
		init();
		loadClassifyMessage();
	}

	public void init() {

		uid = getIntent().getExtras().getString("uid");
		
		fragmentManager = getSupportFragmentManager();
		fragmentRankFirst = new ClassifyListFragment();
		fragmentRankSecond = new ClassifyListFragment();
		fragmentManager.beginTransaction()
		.replace(R.id.container, fragmentRankSecond).commit();
		fragmentManager.beginTransaction()
				.replace(R.id.container, fragmentRankFirst).commit();
		fragmentRankFirst.setOnItemClickListener(rankFirstListener);
		fragmentRankSecond.setOnItemClickListener(rankSecondListener);
		fragmentRankFirst.setOnCanRefreshListener(new OnCanRefreshListener() {

			@Override
			public void canRefresh() {
				fragmentRankFirst.refresh(mDatas);
			}
		});

		fragmentRankSecond.setOnCanRefreshListener(new OnCanRefreshListener() {

			@Override
			public void canRefresh() {
				if (selectedBean != null) {
					fragmentRankSecond.refresh(selectedBean
							.getClassifyBeansRank2());
				}
			}
		});

		mDatas = new ArrayList<ClassifyRankBean>();
		mInflater = LayoutInflater.from(this);
		titleView = (HeadView) findViewById(R.id.head);

		titleView.setText("分类");

	}

	@Override
	protected void onStart() {
		super.onStart();
		fragmentRankFirst.refresh(mDatas);
	}

	private void loadClassifyMessage() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onFinish() {
				super.onFinish();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "factory message result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "分类搜索信息失败");
					return;
				} else {
					mDatas.addAll(JSONParseUtils
							.parserClassifyRankBeans1(content));
					fragmentRankFirst.refresh(mDatas);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "onFailure: content:" + content);
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		HttpClient.loadFactoryClassifyMessage(mHandler, params);
	}

	OnItemClickListener rankFirstListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			selectedBean = mDatas.get(arg2);
			if(selectedBean.getClassifyBeansRank2().size() > 0){
				
				fragmentManager.beginTransaction()
				.replace(R.id.container, fragmentRankSecond).commit();
				fragmentRankSecond.refresh(selectedBean.getClassifyBeansRank2());
			}else {
				Bundle bundle = new Bundle();
//				bundle.putSerializable(FactorySearchResultActivity.RESULT_TAG_KEY, FactorySearchResultActivity.RESULT_TAG_CLASSIFY);
				bundle.putString("uid", uid);
				bundle.putString("cgid", selectedBean.getId());
				bundle.putString("title",selectedBean.getCategory_name());
				ActivitySwitch.openActivity(ShopClassifyActivity.class, bundle, ClassifySearchActivity.this);
			}
		}
	};

	OnItemClickListener rankSecondListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Bundle bundle = new Bundle();
//			bundle.putSerializable(FactorySearchResultActivity.RESULT_TAG_KEY, FactorySearchResultActivity.RESULT_TAG_CLASSIFY);
			bundle.putString("uid", uid);
			bundle.putString("cgid", selectedBean.getClassifyBeansRank2().get(arg2).getId());
			bundle.putString("title",selectedBean.getClassifyBeansRank2().get(arg2).getCategory_name());
			ActivitySwitch.openActivity(ShopClassifyActivity.class, bundle, ClassifySearchActivity.this);
		}
	};
}
