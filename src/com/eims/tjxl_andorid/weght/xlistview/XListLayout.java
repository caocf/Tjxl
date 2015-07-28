package com.eims.tjxl_andorid.weght.xlistview;

import java.util.Date;





import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.utils.ToolsLoadDialog;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ViewSwitcher;


public class XListLayout extends RelativeLayout {
	public final static String TAG = "XListLayout";

	/** 无数据 */
	public final static int ERROR_NOTDATA = 0x1;
	/** 无网络 */
	public final static int ERROR_NOTNETWORK = 0x2;
	/** 刷新 */
	public final static int LOAD_TYPE_REFRESH = 0x1;
	/** 加载更 */
	public final static int LOAD_TYPE_MORE = 0x2;

	private Context mContext;
	private XListView mListView;
	private XListLayoutCallback mCallback;
	private ViewSwitcher mvViewSwitcher;
	private RelativeLayout pulltorefresh_list_view_error_rt;
	/** 当前加载方式 */
	private int mCurrLoadType = LOAD_TYPE_REFRESH;
	private int mErrorCode = 0;

	public XListLayout(Context context) {
		super(context);
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.xlist_view_layout, this, true);
		findViews();
	}

	public XListLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		LayoutInflater.from(mContext).inflate(R.layout.xlist_view_layout, this, true);
		findViews();
	}

	private void findViews() {
		mvViewSwitcher = (ViewSwitcher) findViewById(R.id.xlist_vswit);
		mListView = (XListView) findViewById(R.id.xlist_view);
		pulltorefresh_list_view_error_rt = (RelativeLayout) findViewById(R.id.xlist_error_rt);
		pulltorefresh_list_view_error_rt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCallback != null) {
					mCallback.onLoadClick();
				}
			}
		});
	}

	public void start() {
		endUpData();
		mErrorCode = 0;
		int count = mListView.getAdapter().getCount();
		System.out.println("count---"+count);
		if (count <= 2) {
			mvViewSwitcher.setDisplayedChild(0);
			findViewById(R.id.xlist_notnetwork_lt).setVisibility(View.GONE);
		//	findViewById(R.id.xlist_progressBar).setVisibility(View.VISIBLE);
			ToolsLoadDialog.showLoadDialog(mContext, "正在加载...");
			findViewById(R.id.xlist_notdata_lt).setVisibility(View.GONE);
		}
	}

	public void success() {
		endUpData();
	
		int count = mListView.getAdapter().getCount();
		final int child = mvViewSwitcher.getDisplayedChild();
		if (mErrorCode == 0 && count > 2) {
			mvViewSwitcher.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (child != 1) {
						ToolsLoadDialog.colesLoadDialog();
						mvViewSwitcher.setDisplayedChild(1);
					}
				}
			}, 500);
		}else if (mErrorCode == ERROR_NOTNETWORK && count <= 2) { // 无网络
			mvViewSwitcher.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (child != 0) {
						mvViewSwitcher.setDisplayedChild(0);
					}
					findViewById(R.id.xlist_notdata_lt).setVisibility(View.GONE);
					//findViewById(R.id.xlist_progressBar).setVisibility(View.GONE);
					ToolsLoadDialog.colesLoadDialog();
					findViewById(R.id.xlist_notnetwork_lt).setVisibility(View.VISIBLE);
				}
			}, 500);
		} 
		else if (count <= 2) { // 无数据mErrorCode == ERROR_NOTDATA &&
			mvViewSwitcher.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (child != 0) {
						mvViewSwitcher.setDisplayedChild(0);
					}
					findViewById(R.id.xlist_notnetwork_lt).setVisibility(View.GONE);
					//findViewById(R.id.xlist_progressBar).setVisibility(View.GONE);
					ToolsLoadDialog.colesLoadDialog();
					findViewById(R.id.xlist_notdata_lt).setVisibility(View.VISIBLE);
				}
			}, 500);
		} 
	}

	public XListView getListView() {
		return mListView;
	}

	public void setLoadType(int type) {
		mCurrLoadType = type;
	}

	public int getLoadType() {
		return mCurrLoadType;
	}

	public void endUpData() {
		mListView.stopLoadMore();
		mListView.stopRefresh();
		mListView.setRefreshTime(DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()).toString());
	}

	public void setErrorCode(int ErrorCode) {
		mErrorCode = ErrorCode;
	}

	public void setXListLayoutCallback(XListLayoutCallback callback) {
		mCallback = callback;
	}

	/**
	 * 定义接口
	 */
	public interface XListLayoutCallback {
		public void onLoadClick();
	}

}
