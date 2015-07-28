package com.eims.tjxl_andorid.ui.product;

import java.util.ArrayList;
import java.util.List;

import android.R.interpolator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.FilterBrandSelectItemBean;
import com.eims.tjxl_andorid.entity.FilterCatogerySelectItemBean;
import com.eims.tjxl_andorid.entity.FilterItemBean;
import com.eims.tjxl_andorid.entity.FilterPropertySelectItemBean;
import com.eims.tjxl_andorid.entity.FilterSelectItem;
import com.eims.tjxl_andorid.utils.MathUtil;

@SuppressLint("NewApi")
public class ProductFilterFragment extends Fragment implements OnClickListener {

	private static final String TAG = "ProductFilterFragment";
	private LayoutInflater mInflater;
	// private String testStrings[] = new String[]{"红色","橙色","黄色","绿色","篮色"};
	private List<FilterItemBean> mDatas;
	private FilterSelectItem selectedItems[];
	private ListView mListView;
	private EditText price_from, price_to;
	private Button btn_clear, btn_commit;
	private ConditionsAdapter mAdapter;
	private boolean isClearing;
	private static final String DEFAULT_PRICE_FROM = "0";
	private static final String DEFAULT_PRICE_TO = "9999999";
	
	public interface OnFilterFinishListener{
		void filterFinish(String pps,String pri,String pri2,String bid);
	}
	
	private OnFilterFinishListener mListener;
	public void setOnFilterFinishListener(OnFilterFinishListener listener){
		this.mListener = listener;
	}
	

	public ProductFilterFragment() {
		mAdapter = new ConditionsAdapter();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAdapter = new ConditionsAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater = inflater;
		View view = inflater.inflate(R.layout.fragment_product_filter, null);
		mListView = (ListView) view
				.findViewById(R.id.filter_condition_listview);
		price_from = (EditText) view.findViewById(R.id.price_from);
		price_to = (EditText) view.findViewById(R.id.price_to);
		btn_clear = (Button) view.findViewById(R.id.btn_clear);
		btn_commit = (Button) view.findViewById(R.id.btn_commit);
		btn_clear.setOnClickListener(this);
		btn_commit.setOnClickListener(this);

		mListView.setAdapter(mAdapter);
		return view;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_clear:
			clear();
			break;
		case R.id.btn_commit:
			commit();
			break;
		default:
			break;
		}
	}
	
	public void clear(){
		isClearing = true;
		mAdapter.notifyDataSetChanged();
		price_from.setText("");
		price_to.setText("");
		price_from.setHint(DEFAULT_PRICE_FROM);
		price_to.setHint(DEFAULT_PRICE_TO);
		for(int i = 0; i < selectedItems.length; i++){
			selectedItems[i] = null;
		}
	}
	
	private String bid;
	private String pri;
	private String pri2;
	private StringBuffer pps;
	
	public void commit(){
		pri = price_from.getText().toString().trim();
		pri2 = price_to.getText().toString().trim();
		if(pri == null || pri.length() == 0 || pri.equals("")){
			pri = DEFAULT_PRICE_FROM;
		}
		if(pri2 == null || pri.length() == 0 || pri2.equals("")){
			pri2 = DEFAULT_PRICE_TO;
		}
		if(!MathUtil.isLager(pri2, pri)){
			showToast("价格范围错误");
			return;
		}
		
		pps = new StringBuffer();
		for (int i = 0; i < selectedItems.length; i++) {
			FilterSelectItem itemTemp = selectedItems[i];
			if(itemTemp != null){
				if (itemTemp instanceof FilterBrandSelectItemBean) {
					bid = itemTemp.getId();
				}else if(itemTemp instanceof FilterCatogerySelectItemBean){
					
				}else if(itemTemp instanceof FilterPropertySelectItemBean){
					pps.append("@"+itemTemp.getId()+"_"+itemTemp.getUncode());
				}
			}
		}
		
		if (mListener != null) {
			mListener.filterFinish(pps.toString(), pri, pri2, bid);
		}
	}

	public void setData(List<FilterItemBean> data) {
		Log.d(TAG, "setData == > data size = "+data.size());
		if (mDatas == null || mDatas.size() <= 0) {
			mDatas = new ArrayList<FilterItemBean>();
			mDatas.addAll(data);
		}
		selectedItems = new FilterSelectItem[mDatas.size()];
		mAdapter.notifyDataSetChanged();
		mListView.setSelection(0);
		
	}

	public void refresh() {
		mAdapter.notifyDataSetChanged();
	}

	public void showToast(String message) {
		Toast.makeText(this.getActivity(), message, Toast.LENGTH_SHORT).show();
	}

	class ConditionsAdapter extends BaseAdapter {

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
			View view = arg1;
			view = mInflater.inflate(R.layout.filter_condition_item, null);
			View layoutSpinner = view.findViewById(R.id.layout_spinner);
			TextView title = (TextView) view.findViewById(R.id.title);
			Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
			// ArrayAdapter<String>(ProductFilterFragment.this.getActivity(),
			// R.layout.layout_spinner_item, testStrings);
			title.setText(mDatas.get(arg0).getItem_title());
			SpinnerAdapter adapter = new SpinnerAdapter(mDatas.get(arg0).items);
			spinner.setAdapter(adapter);
			spinner.setTag(arg0);//用于标识筛选属性在selectedItems里面的位置
			spinner.setSelection(mDatas.get(arg0).selecteItemIndex);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					int tag = (Integer) arg0.getTag();
					if(arg2 == 0){
						selectedItems[tag] = null; 
					}else {
						selectedItems[tag] = mDatas.get(tag).items.get(arg2);
					}
					mDatas.get(tag).selecteItemIndex = arg2;
						
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			});
			
			//重置搜索条件
			if(isClearing){
				spinner.setSelection(0);
				if(arg0 == (mDatas.size()-1)){
					isClearing = false;
				}
			}
			return view;
		}
	}

	class SpinnerAdapter extends BaseAdapter {

		List<FilterSelectItem> selectItems;

		public SpinnerAdapter(List<FilterSelectItem> items) {
			selectItems = items;
		}

		@Override
		public int getCount() {
			return selectItems == null ? 0 : selectItems.size();
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
			if (view == null) {
				view = mInflater.inflate(R.layout.layout_spinner_item, null);
			}
			TextView textView = (TextView) view.findViewById(R.id.textview);
			textView.setText(selectItems.get(arg0).name);
			return view;
		}

	}

}
