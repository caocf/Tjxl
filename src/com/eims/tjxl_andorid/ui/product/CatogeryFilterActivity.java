package com.eims.tjxl_andorid.ui.product;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.FilterCatogeryItemBean;
import com.eims.tjxl_andorid.entity.FilterCatogerySelectItemBean;
import com.eims.tjxl_andorid.entity.FilterSelectItem;
import com.eims.tjxl_andorid.ui.product.ClassifyListFragment.OnCanRefreshListener;
import com.eims.tjxl_andorid.ui.shop.ShopClassifyActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 * 商品筛选页面
 * @author 
 *
 */
public class CatogeryFilterActivity extends BaseActivity {

	private HeadView titleView;
	private ListView mClassifyListView;
	private ArrayList<FilterSelectItem> mSelectItems;
	private LayoutInflater mInflater;
	private ClassfyAdapter mAdapter;
	private FilterCatogeryItemBean filterCatogeryItemBean;
	private int rankShowed = 1;
	
	private FragmentManager fragmentManager;
	private ClassifyListFragment fragmentRankFirst;
	private ClassifyListFragment fragmentRankSecond;
	private ClassifyListFragment fragmentRankThree;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classify_search);
		init();
	}

	public void init() {
		
		fragmentManager = getSupportFragmentManager();
		fragmentRankFirst = new ClassifyListFragment();
		fragmentRankSecond = new ClassifyListFragment();
		fragmentRankThree = new ClassifyListFragment();
		replaceFragment(fragmentRankThree);
		replaceFragment(fragmentRankSecond);
		replaceFragment(fragmentRankFirst);
		fragmentRankFirst.setOnItemClickListener(rankFirstListener);
		fragmentRankSecond.setOnItemClickListener(rankSecondListener);
		fragmentRankFirst.setOnCanRefreshListener(new OnCanRefreshListener() {

			@Override
			public void canRefresh() {
			}
		});

		mInflater = LayoutInflater.from(this);

		mSelectItems = new ArrayList<FilterSelectItem>();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			filterCatogeryItemBean = (FilterCatogeryItemBean) bundle
					.get("data");
		}
		titleView = (HeadView) findViewById(R.id.head);
		mAdapter = new ClassfyAdapter();

		titleView.setText("分类");
		mClassifyListView.setAdapter(mAdapter);

		mClassifyListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				CatogeryFilterActivity.this.finish();
				Bundle bundle = new Bundle();
				bundle.putString("tag", "filter");
				ActivitySwitch.openActivity(CatogeryFilterActivity.class,
						bundle, CatogeryFilterActivity.this);
			}
		});
	}
	
	private  void replaceFragment(ClassifyListFragment fragment){
		fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
	}

	class ClassfyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
//			Log.d("zhiheng", "getView mSelectItems = " + mSelectItems.size());
			return mSelectItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			FilterCatogerySelectItemBean bean = (FilterCatogerySelectItemBean) mSelectItems
					.get(arg0);
			View view = arg1;
			view = mInflater.inflate(R.layout.classify_search_item, null);
			TextView title = (TextView) view.findViewById(R.id.title);
			title.setText(bean.getCategory_name());
			return view;
		}

	}
	
	OnItemClickListener rankFirstListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
//			selectedBean = mDatas.get(arg2);
//			fragmentManager.beginTransaction()
//					.replace(R.id.container, fragmentRankSecond).commit();
//			fragmentRankSecond.refresh(selectedBean.getClassifyBeansRank2());
		}
	};

	OnItemClickListener rankSecondListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Bundle bundle = new Bundle();
//			bundle.putSerializable(FactorySearchResultActivity.RESULT_TAG_KEY, FactorySearchResultActivity.RESULT_TAG_CLASSIFY);
//			bundle.putString("cgid", selectedBean.getClassifyBeansRank2().get(arg2).getId());
//			bundle.putString("title",selectedBean.getClassifyBeansRank2().get(arg2).getCategory_name());
//			ActivitySwitch.openActivity(ShopClassifyActivity.class, bundle, ClassifySearchActivity.this);
		}
	};
}
