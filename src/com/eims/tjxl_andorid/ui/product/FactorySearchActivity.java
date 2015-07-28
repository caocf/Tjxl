package com.eims.tjxl_andorid.ui.product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.SearchAdapter;
import com.eims.tjxl_andorid.adapter.MyListAdapter.onItemClickListener;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.ui.home.SearchActivity;
import com.eims.tjxl_andorid.ui.home.bean.SearchBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.SharedPreferencesUtils;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyPopupWindow;

public class FactorySearchActivity extends BaseActivity implements
		OnClickListener {

	private HeadView headView;
	// private LinearLayout ll_serachType;
	private EditText serachEdit;
	private Button btn_serach;
	private MyPopupWindow popupWindow;
	private List<String> mItems;
	private Set<String> historySet;
	private ListView mListView;
	private SearchAdapter mAdapter;
	private SearchBean bean;
	private LinearLayout ll_nosearchdata;
	private LinearLayout ll_clearsearchdata;
	private ImageButton left_menu_btn;
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_factory_search);
		uid = getIntent().getExtras().getString("uid");
		findview();
		setActionBar();
		mItems = new ArrayList<String>();
		btn_serach.setOnClickListener(this);
		left_menu_btn.setOnClickListener(this);
		mAdapter = new SearchAdapter(this, AppContext.getInstance().mLists);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				serachEdit.setText(AppContext.getInstance().mLists.get(arg2).searchText);
			}
		});
		showClearOrNoData();
	}

	private void setActionBar() {
		headView.setText("店铺内搜索");
		headView.setGobackVisible();
		// headView.setRightResource();
	}

	private void findview() {
		headView = (HeadView) findViewById(R.id.head);
		mListView = (ListView) findViewById(R.id.listview_search);
		btn_serach = (Button) findViewById(R.id.btn_serach_text);
		serachEdit = (EditText) findViewById(R.id.serach_edit);
		ll_nosearchdata = (LinearLayout) findViewById(R.id.ll_nosearchdata);
		ll_clearsearchdata = (LinearLayout) findViewById(R.id.ll_clearsearchdata);
		left_menu_btn = (ImageButton) findViewById(R.id.left_menu_btn);
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
		historySet = SharedPreferencesUtils.getFactorylSearchHistory(this);
		setHistoryItems();
		showClearOrNoData();
		
		serachEdit.setText("");
		serachEdit.setHint("请输入关键字搜索");
	}

	@Override
	public void onClick(View v) {
		Bundle bundle = null;
		switch (v.getId()) {
		case R.id.left_menu_btn:
			bundle = new Bundle();
			bundle.putString("uid", uid);
			ActivitySwitch.openActivity(ClassifySearchActivity.class, bundle,
					this);
			break;
		case R.id.btn_serach_text:
			String mSearchText = serachEdit.getText().toString();
			if (TextUtils.isEmpty(mSearchText)) {
				TipToast.makeText(FactorySearchActivity.this, "请输入搜索关键字", 0)
						.show();
				return;
			}
			bean = new SearchBean();
			bean.setSearchText(mSearchText);
			bean.setDate(new Date().getTime());
			addSearchBean(bean);

			bundle = new Bundle();
			bundle.putString("uid", uid);
			bundle.putString("key_word", mSearchText);
			ActivitySwitch.openActivity(FactorySearchResultActivity.class,
					bundle, FactorySearchActivity.this);
			break;
		case R.id.ll_clearsearchdata:
			AppContext.getInstance().mLists.clear();
			historySet.clear();
			SharedPreferencesUtils.saveFactorylSearchHistory(this, historySet);
			mAdapter.notifyDataSetChanged();
			// showClearOrNoData();
			break;
		default:
			break;
		}
	}

	private boolean addSearchBean(SearchBean bean) {
		boolean isContain = false;
		String history = null;
		Iterator<String> iterator = historySet.iterator();
		while(iterator.hasNext()){
			history = iterator.next();
			if(history.contains(bean.toStringPart())){
				isContain = true;
				break;
			}
		}
		if(isContain){
			historySet.remove(history);//删除旧的记录
		}
		historySet.add(bean.toString());//存储新的记录
		SharedPreferencesUtils.saveFactorylSearchHistory(this, historySet);

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

}
