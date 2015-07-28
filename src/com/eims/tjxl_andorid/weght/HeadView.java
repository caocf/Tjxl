package com.eims.tjxl_andorid.weght;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.MyListAdapter.onItemClickListener;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.home.SearchActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年4月15日 下午2:40:10
 *************************************************************************** 
 */
public class HeadView extends RelativeLayout {

	private TextView titile;
	private TextView gobackText;
	private ImageButton goback, imgBtn_right;
	private MyPopupWindow popupWindow;
	private FrameLayout mFrameRight;
	private TextView mRightText;
	public static List<String> items = new ArrayList<String>();
	private RelativeLayout rl_headview;
	private Context mContext;
	
	static{
		items.add("逛商城");
		items.add("货品互换");
		items.add("进货单");
		items.add("个人详情");
		items.add("搜索");
	}
	public HeadView(Context context) {
		super(context);
	}

	public HeadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public HeadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initview(context);
	}

	private void initview(final Context context) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(context).inflate(R.layout.common_titlebar_view, this, true);
		mContext=context;
		findView();
		goback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AppManager.getAppManager().finishActivity((Activity) context);
				((Activity) mContext).overridePendingTransition(
						R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		gobackText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				AppManager.getAppManager().finishActivity((Activity) context);
				((Activity) mContext).overridePendingTransition(
						R.anim.push_right_in, R.anim.push_right_out);
			}
		});
		

		imgBtn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				popupWindow = new MyPopupWindow(context, imgBtn_right
						.getWidth() * 3, items);
				// popupWindow.setAnimationStyle(R.style.PopupAnimation);
				// popupWindow.setAnimationStyle(Style.CONFIRM);
				popupWindow.showAsDropDown(imgBtn_right, 0, 0);
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
									null, (Activity) context);
						}

					}
				});
			}
		});
	}

	private void findView() {
		// TODO Auto-generated method stub
		titile = (TextView) findViewById(R.id.tv_headview_title);
		goback = (ImageButton) findViewById(R.id.iv_headview_goback);
		gobackText = (TextView) findViewById(R.id.iv_headview_goback_text);
		imgBtn_right = (ImageButton) findViewById(R.id.imgbtn_right);
		rl_headview = (RelativeLayout) findViewById(R.id.headview);
		mFrameRight = (FrameLayout) findViewById(R.id.rightBtn_text);
		mRightText = (TextView) findViewById(R.id.right_text);
	}

	/**
	 * 设置标题
	 * 
	 * @param text
	 */
	public void setText(CharSequence text) {
		titile.setText(text);
	}

	/**
	 * 显示返回按钮
	 */
	public void setGobackVisible() {
		goback.setVisibility(View.VISIBLE);
		gobackText.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏返回按钮
	 */
	public void setGobackInVisible() {
		goback.setVisibility(View.GONE);
		gobackText.setVisibility(View.GONE);
	}

	/**
	 * 显示右侧按钮 This class is used for ...
	 * 
	 */
	public void setRightResource() {
		imgBtn_right.setVisibility(View.VISIBLE);
	}

	public void setRightGone() {
		imgBtn_right.setVisibility(View.GONE);
	}

	/**
	 * 
	 * 设置右侧 按钮文字 以及显示、监听
	 * 
	 */
	public void showRightText(String str, OnClickListener mClickListener) {
		mRightText.setText(str);
		mFrameRight.setVisibility(View.VISIBLE);
		mFrameRight.setOnClickListener(mClickListener);
	}

	/**
	 * 
	 * 设置右侧 按钮文字 以及显示、监听
	 */
	public void GoneRightText(String str, OnClickListener mClickListener) {
		mRightText.setText(str);
		mFrameRight.setVisibility(View.GONE);
		mFrameRight.setOnClickListener(mClickListener);
	}
	/**
	 * 
	 * 设置右侧 按钮文字 以及显示、监听
	 */
	public void ClickBlack(OnClickListener mClickListener) {
		goback.setVisibility(View.VISIBLE);
		gobackText.setVisibility(View.VISIBLE);
		goback.setOnClickListener(mClickListener);
		gobackText.setOnClickListener(mClickListener);
	}
}
