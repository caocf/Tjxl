package com.eims.tjxl_andorid.ui.product;

import java.util.ArrayList;
import java.util.List;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.ClassifyRankBean;

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

public class ClassifyListFragment extends Fragment{
	
	private static final String TAG = "ClassifyListFragment";
	private LayoutInflater mInflater;
	private ListView mListView;
	private ClassifyAdapter mAdapter;
	private List<ClassifyRankBean> mDatas;//分类搜索
	private OnItemClickListener onItemClickListener;

	private static OnCanRefreshListener onCanRefreshListener;
	interface OnCanRefreshListener{
		void canRefresh();
	}
	public void setOnCanRefreshListener(OnCanRefreshListener listener){
		this.onCanRefreshListener = listener;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		Log.d(TAG, "==>onCreate");
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
//		Log.d(TAG, "==>onAttach");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		Log.d(TAG, "==>onCreateView");
		mInflater = inflater;
		View view = inflater.inflate(R.layout.fragment_classify_list, null);
		mListView = (ListView) view.findViewById(R.id.listview);
		mAdapter = new ClassifyAdapter();
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		mListView.setOnItemClickListener(onItemClickListener);
//		if(onCanRefreshListener != null){
//			onCanRefreshListener.canRefresh();
//		}
		return view;
	}
	
	public void refresh(List<ClassifyRankBean> datas){
//		Log.d(TAG, "==>refresh ,datas size = "+datas.size());
		if(mDatas == null){
			mDatas = new ArrayList<ClassifyRankBean>();
		}
		mDatas.clear();
		mDatas.addAll(datas);
		mAdapter.notifyDataSetChanged();
	}
	
	public void setOnItemClickListener(OnItemClickListener listener){
		onItemClickListener = listener;
	}
	
	class ClassifyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mDatas == null ? 0 : mDatas.size();
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
//			Log.d(TAG, "getView position = "+arg0);
			View view = arg1;
			view = mInflater.inflate(R.layout.classify_search_item, null);
			TextView title = (TextView) view.findViewById(R.id.title);
			title.setText(mDatas.get(arg0).getCategory_name());
			return view;
		}
	}
}
