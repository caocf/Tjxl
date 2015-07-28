package com.eims.tjxl_andorid.ui.product;

import java.util.List;
import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.ClassifyRankBean;
import com.eims.tjxl_andorid.entity.FilterSelectItem;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("ValidFragment")
public class FilterRankListFragment extends Fragment {

	private static final String TAG = "ClassifyListFragment";
	private LayoutInflater mInflater;
	private ListView mListView;
	private RankAdapter mAdapter;
	public List<FilterSelectItem> mRandItems;// 分类搜索数据
	private OnItemClickListener onItemClickListener;

	private static OnCanRefreshListener onCanRefreshListener;

	interface OnCanRefreshListener {
		void canRefresh();
	}

	public void setOnCanRefreshListener(OnCanRefreshListener listener) {
		this.onCanRefreshListener = listener;
	}

	public FilterRankListFragment(List<FilterSelectItem> items) {
		mRandItems = items;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View view = inflater.inflate(R.layout.fragment_classify_list, null);
		mListView = (ListView) view.findViewById(R.id.listview);
		mAdapter = new RankAdapter();
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		mListView.setOnItemClickListener(onItemClickListener);
		return view;
	}

	public void refresh(List<ClassifyRankBean> datas) {
		mAdapter.notifyDataSetChanged();
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		onItemClickListener = listener;
	}

	class RankAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mRandItems == null ? 0 : mRandItems.size();
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
			view = mInflater.inflate(R.layout.classify_search_item, null);
			TextView title = (TextView) view.findViewById(R.id.title);
			title.setText(mRandItems.get(arg0).getName());
			return view;
		}
	}
}
