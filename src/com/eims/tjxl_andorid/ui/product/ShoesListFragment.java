package com.eims.tjxl_andorid.ui.product;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.mipmap;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.BigImageGridAdapter;
import com.eims.tjxl_andorid.adapter.SmalImageAdapter;
import com.eims.tjxl_andorid.entity.ProductBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.weght.MyGridView;
import com.eims.tjxl_andorid.weght.PullToRefreshView;
import com.eims.tjxl_andorid.weght.PullToRefreshView.OnFooterRefreshListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShoesListFragment extends Fragment implements OnClickListener {
	private static final String TAG = "ShoesList";

	private PullToRefreshView mPullToRefreshListView;
	private PullToRefreshView mPullToRefreshGridView;
	private TextView tip_no_content;
	private MyGridView mGridView;
	private BigImageGridAdapter bigImageGridAdapter;
	private SmalImageAdapter smalImageAdapter;
	private List<ProductBean> mProductBeans;
	private ListView mListView;
	private TextView BigImage, smallImage;
	private LinearLayout ll_image;
	private boolean isCreatedView;
	private String lastTime;

	private ImageLoader mImageLoader = ImageLoader.getInstance();

	public interface OnNotifyLoadDatasListener {
		void onNotify();
	}

	private static OnNotifyLoadDatasListener mNotifyLoadListener;

	public void setOnNotifyLoadDatasListener(OnNotifyLoadDatasListener listener) {
		mNotifyLoadListener = listener;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shoes_list, null);
		findViews(view);
		initGridView();
		isCreatedView = true;
		setLastTime();
		Log.d(TAG, "lastTime = "+lastTime);
		return view;
	}
	
	public boolean isCreatedView(){
		return isCreatedView;
	}
	
	public void setLastTime() {
		lastTime = StringUtils.friendly_time(new Date().toString());
	}

	private void findViews(View view) {
		mGridView = (MyGridView) view.findViewById(R.id.mgridview);
		mListView = (ListView) view.findViewById(R.id.listview_product);
		ll_image = (LinearLayout) view.findViewById(R.id.ll_image_type);
		BigImage = (TextView) view.findViewById(R.id.big_image);
		smallImage = (TextView) view.findViewById(R.id.small_image);
		tip_no_content = (TextView) view.findViewById(R.id.tip_no_content);

		BigImage.performClick();
		BigImage.setOnClickListener(this);
		smallImage.setOnClickListener(this);

		mPullToRefreshListView = (PullToRefreshView) view
				.findViewById(R.id.pull_to_refresh_listview);
		mPullToRefreshGridView = (PullToRefreshView) view
				.findViewById(R.id.pull_to_refresh_gridview);
		mPullToRefreshListView.setCanPullDown(false);
		mPullToRefreshListView.setOnFooterRefreshListener(new OnFooterRefreshListener() {

					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						if (mNotifyLoadListener != null) {
							mNotifyLoadListener.onNotify();
						}
						Log.d(TAG,"onFooterRefresh");
					}
				});
		mPullToRefreshGridView.setCanPullDown(false);
		mPullToRefreshGridView
				.setOnFooterRefreshListener(new OnFooterRefreshListener() {

					@Override
					public void onFooterRefresh(PullToRefreshView view) {
						if (mNotifyLoadListener != null) {
							mNotifyLoadListener.onNotify();
						}
					}
				});

	}

	public void setOnFooterRefreshComplete() {
		mPullToRefreshListView.onFooterRefreshComplete();
		mPullToRefreshGridView.onFooterRefreshComplete();
	}

	private void initGridView() {
		mGridView.setHaveScrollbar(true);
		mProductBeans = new ArrayList<ProductBean>();
		bigImageGridAdapter = new BigImageGridAdapter(getActivity(), mProductBeans);
		smalImageAdapter = new SmalImageAdapter(getActivity().getBaseContext(),mProductBeans);
		mGridView.setAdapter(bigImageGridAdapter);
		mListView.setAdapter(smalImageAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(arg2 < mProductBeans.size()){
					Bundle bundle = new Bundle();
					bundle.putString(ProductDeatil.INTENT_KEY, mProductBeans.get(arg2).id);
					ActivitySwitch.openActivity(ProductDeatil.class, bundle, ShoesListFragment.this.getActivity());
				}
			}
		});
		// 默认选中新品
	}

	public void reflesh(List<ProductBean> datas, int dataMode) {
		Log.d(TAG, "ShoesListFragment ===>> reflesh , datas size = "+datas.size());
		if(isCreatedView == false){
			return;
		}
		
		if (mProductBeans == null) {
			mProductBeans = new ArrayList<ProductBean>();
		}
		
		mProductBeans.clear();
		mProductBeans.addAll(datas);
		if(mProductBeans.size() == 0) {
			tip_no_content.setVisibility(View.VISIBLE);
		}else {
			tip_no_content.setVisibility(View.INVISIBLE);
		}
		bigImageGridAdapter = new BigImageGridAdapter(getActivity(), mProductBeans);
		mGridView.setAdapter(bigImageGridAdapter);
		smalImageAdapter = new SmalImageAdapter(getActivity(),mProductBeans);
		mListView.setAdapter(smalImageAdapter);
		Log.d(TAG,"mProductBeans size = "+mProductBeans.size());
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.big_image:
			ll_image.setBackgroundResource(R.drawable.slt);
			mPullToRefreshGridView.setVisibility(View.VISIBLE);
			mPullToRefreshListView.setVisibility(View.GONE);
			break;
		case R.id.small_image:
			ll_image.setBackgroundResource(R.drawable.lbs);
			mPullToRefreshGridView.setVisibility(View.GONE);
			mPullToRefreshListView.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}

}
