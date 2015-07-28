/**
 * 
 */
package com.eims.tjxl_andorid.ui.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import loopj.android.http.RequestParams;
import android.R.integer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.MyListAdapter.onItemClickListener;
import com.eims.tjxl_andorid.adapter.SearchAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.ui.home.area.SimpleAreaActivity;
import com.eims.tjxl_andorid.ui.home.bean.SearchBean;
import com.eims.tjxl_andorid.ui.product.ProductListActivity;
import com.eims.tjxl_andorid.ui.shop.FactoryListActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.SharedPreferencesUtils;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyPopupWindow;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年4月15日 上午11:02:51
 *************************************************************************** 
 */
public class SearchActivity extends BaseActivity implements OnClickListener {

	private static final String TAG1 = "SearchActivity";
	private HeadView headView;
	private LinearLayout ll_serachType;
	private TextView typeText;
	private EditText serachEdit;
	private Button btn_serach;
	private MyPopupWindow popupWindow;
	private List<String> mItems;
	private List<SearchBean> pcSearchBeans;
	private Set<String> historySet;
	private ListView mListView;
	private GridView mGridView;
	private SearchAdapter mAdapter;
	private SearchGridAdapter mGridAdapter;
	private SearchBean bean;
	private LinearLayout ll_nosearchdata;
	private LinearLayout ll_clearsearchdata;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serach_layout);
		findview();
		setActionBar();
		initData();
		loadPcHotSearch();
	}
	
	private void initData(){
		mInflater = LayoutInflater.from(this);
		mItems = new ArrayList<String>();
		pcSearchBeans = new ArrayList<SearchBean>();
		mItems.add("鞋子");
		mItems.add("厂家");
		typeText.setText(mItems.get(0));
		ll_serachType.setOnClickListener(this);
		btn_serach.setOnClickListener(this);
		mAdapter = new SearchAdapter(this, AppContext.getInstance().mLists);
		mGridAdapter = new SearchGridAdapter();
		mListView.setAdapter(mAdapter);
		mGridView.setAdapter(mGridAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				serachEdit.setText(AppContext.getInstance().mLists.get(arg2).searchText);
			}
		});
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				serachEdit.setText(pcSearchBeans.get(arg2).getSearchText());
				String type = (String) typeText.getText();
				Bundle bundle = new Bundle();
				if(type.equals(mItems.get(0))){//搜索鞋子
					bundle.putString("key_word", pcSearchBeans.get(arg2).searchText);
					ActivitySwitch.openActivity(ProductListActivity.class, bundle,
							SearchActivity.this);
				}else if(type.equals(mItems.get(1))){//搜索厂家
					bundle.putString(FactoryListActivity.KYE_WORD, pcSearchBeans.get(arg2).searchText);
					ActivitySwitch.openActivity(FactoryListActivity.class, bundle,
							SearchActivity.this);
				}
			}
		});
	}
	
	
	private void loadPcHotSearch(){
		
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "loadPcHotSearch result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "loadPcHotSearch  获取热搜关键字失败");
					return;
				} else {
					String keys[] = JSONParseUtils.parsePcHotSearchWord(content);
//					String keys[] = JSONParseUtils.getString(content, "data").split(",");
					Log.d(TAG1, "data string = "+JSONParseUtils.getString(content, "data")+",keys.length = "+keys.length);
					for(int i = 0; i < keys.length; i++){
						if(keys[i] != null && !keys[i].trim().equals("")){
							pcSearchBeans.add(new SearchBean(keys[i]));
						}
					}
					mGridAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "loadPcHotSearch onFailure: content:" + content);
			}
		};
		RequestParams params = new RequestParams();
		HttpClient.loadPCHotSearchInfo(mHandler, params);
	}

	private void setHistoryItems() {
		AppContext.getInstance().mLists.clear();
		Iterator<String> iterator = historySet.iterator();
		while (iterator.hasNext()) {
			String item[] = iterator.next().split("-");
			SearchBean bean = new SearchBean();
			bean.setType(item[0]);
			bean.setSearchText(item[1]);
			bean.setDate(Long.valueOf(item[2]));
			AppContext.getInstance().mLists.add(bean);
		}
		Collections.sort(AppContext.getInstance().mLists, SearchBean.comparator);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onStart() {
		super.onStart();
		historySet = SharedPreferencesUtils.getGlobalSearchHistory(this);
		Log.d(TAG1, "=== > SearchActivity onStart, history size = "+historySet.size());
		setHistoryItems();
		showClearOrNoData();
		serachEdit.setText("");
		serachEdit.setHint("请输入关键字");
	}

	private void setActionBar() {
		headView.setText("搜索");
		headView.setGobackVisible();
		headView.setRightResource();
	}

	private void findview() {
		headView = (HeadView) findViewById(R.id.head);
		ll_serachType = (LinearLayout) findViewById(R.id.ll_serachtype);
		typeText = (TextView) findViewById(R.id.typetext);
		mListView = (ListView) findViewById(R.id.listview_search);
		mGridView = (GridView) findViewById(R.id.gridview_search);
		btn_serach = (Button) findViewById(R.id.btn_serach_text);
		serachEdit = (EditText) findViewById(R.id.serach_edit);
		ll_nosearchdata = (LinearLayout) findViewById(R.id.ll_nosearchdata);
		ll_clearsearchdata = (LinearLayout) findViewById(R.id.ll_clearsearchdata);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_serachtype:
			popupWindow = new MyPopupWindow(this, ll_serachType.getWidth(),
					mItems);
			popupWindow.showAsDropDown(ll_serachType, 0, 0);
			popupWindow.setOnItemClickListener(new onItemClickListener() {
				@Override
				public void click(int position, View view) {
					typeText.setText(mItems.get(position));
				}
			});
			break;
		case R.id.btn_serach_text:
			String type = typeText.getText().toString();
			String mSearchText = serachEdit.getText().toString();
			if (TextUtils.isEmpty(mSearchText)) {
				TipToast.makeText(SearchActivity.this, "请输入搜索关键字", 0).show();
				return;
			}
			bean = new SearchBean();
			bean.searchText = mSearchText;
			bean.type = type;
			bean.setDate(new Date().getTime());
			addSearchBean(bean);

			Bundle bundle = new Bundle();
			if(type.equals(mItems.get(0))){//搜索鞋子
				bundle.putString("key_word", mSearchText);
				ActivitySwitch.openActivity(ProductListActivity.class, bundle,
						SearchActivity.this);
			}else if(type.equals(mItems.get(1))){//搜索厂家
				bundle.putString(FactoryListActivity.KYE_WORD, mSearchText);
				ActivitySwitch.openActivity(FactoryListActivity.class, bundle,
						SearchActivity.this);
			}
			break;
		case R.id.ll_clearsearchdata:
			AppContext.getInstance().mLists.clear();
			historySet.clear();
			SharedPreferencesUtils.saveGlobalSearchHistory(this, historySet);
			mAdapter.notifyDataSetChanged();

			serachEdit.setText("");
			serachEdit.setHint("请输入关键字");
			break;
		default:
			break;
		}
	}

	private boolean addSearchBean(SearchBean bean) {
		boolean isContain = false;
		String history = null;
		Iterator<String> iterator = historySet.iterator();
		while (iterator.hasNext()) {
			history = iterator.next();
			if (history.contains(bean.toStringPart())) {
				isContain = true;
				break;
			}
		}
		if (isContain) {
			historySet.remove(history);// 删除旧的记录
		}
		historySet.add(bean.toString());// 存储新的记录
		SharedPreferencesUtils.saveGlobalSearchHistory(this, historySet);
		return isContain;
	}

	private void showClearOrNoData() {
		if (AppContext.getInstance().mLists != null
				&& AppContext.getInstance().mLists.size() > 0) {
			ll_nosearchdata.setVisibility(View.GONE);
			ll_clearsearchdata.setVisibility(View.VISIBLE);
			ll_clearsearchdata.setOnClickListener(this);
		} else {
			ll_nosearchdata.setVisibility(View.VISIBLE);
			ll_clearsearchdata.setVisibility(View.GONE);
		}
	}
	
	class SearchGridAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return pcSearchBeans == null ? 0 : pcSearchBeans.size();
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
			View view = arg1;
			if(view == null){
				view = mInflater.inflate(R.layout.layout_pc_search_item, null);
			}
			TextView keyword = (TextView) view.findViewById(R.id.tv_search_key);
			keyword.setText(pcSearchBeans.get(arg0).getSearchText());
			return view;
		}
		
	}

}
