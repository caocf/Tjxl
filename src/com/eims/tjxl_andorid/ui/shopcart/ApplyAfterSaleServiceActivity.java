package com.eims.tjxl_andorid.ui.shopcart;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.R.integer;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.OrderDetailBean;
import com.eims.tjxl_andorid.entity.Picture;
import com.eims.tjxl_andorid.entity.RefundGoodBean;
import com.eims.tjxl_andorid.entity.RefundGoodSizeBean;
import com.eims.tjxl_andorid.entity.OrderBean.OrderItemBean;
import com.eims.tjxl_andorid.utils.imageupload.ImageUpload;
import com.eims.tjxl_andorid.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.eims.tjxl_andorid.utils.imageupload.SelectPicPopupWindow;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 * 
 * 申请售后页面，包括退货和换货功能
 * 申请售后需要上一个页面传过来的订单ＩＤ，或者是订单详情对象
 * @author lzh
 *
 */
public class ApplyAfterSaleServiceActivity extends BaseActivity implements OnClickListener{

	private static final String TAG1 = "ApplyAfterSaleServiceActivity";
	public static final String KEY_EXTRA_ORDER = "order_bean";//商品详情
	public static final String KEY_EXTRA_LIST_ITEM = "order_item_bean";//商品列表Item
	public static final String KEY_EXTRA_FROM = "data_from";//Intent来自哪里
	public static final int KEY_EXTRA_FROM_DETAIL = 1;//Intent来自订单详情
	public static final int KEY_EXTRA_FROM_LIST = 2;//Intent来自订单列表
	
	private HeadView headview;
	private TextView tv_refund, tv_exchange;
	private View line_refund, line_exchange;
	private ScrollView scrollView;
	
	private OrderDetailBean detailBean;
	private OrderItemBean itemBean;
	private int intentFrom;
	
	private FragmentManager fragmentManager;
	private RefundFragment refundFragment;
	private ExchangeProductFragment changeProductFragment;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			Log.d("RefundFragment","message.what = "+msg.what);
			switch (msg.what) {
			case RefundGoodSizeBean.TYPE_REFUND_SELECT:
				refundFragment.setTotalPriceText();
				break;
			case RefundGoodSizeBean.TYPE_EXCHANGE_RECEIVE:
				changeProductFragment.setReceiveTotalPriceText();
				break;
			case RefundGoodSizeBean.TYPE_EXCHANGE_SEND:
				changeProductFragment.setSendTotalPriceText();
				break;
				
			default:
				break;
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_sale);
		findView();
		initData();
	}
	
	private void findView(){
		headview = (HeadView) findViewById(R.id.headview);
		tv_refund = (TextView) findViewById(R.id.tv_refund);
		tv_exchange = (TextView) findViewById(R.id.tv_exchange);
		line_refund = findViewById(R.id.line_refund);
		line_exchange = findViewById(R.id.line_exchange);
		scrollView = (ScrollView) findViewById(R.id.scrollview);
		
		tv_refund.setOnClickListener(this);
		tv_exchange.setOnClickListener(this);

		headview.setText("申请售后");
	}
	
	private void initData(){
		intentFrom = this.getIntent().getIntExtra(KEY_EXTRA_FROM, 1);
		if(intentFrom == KEY_EXTRA_FROM_DETAIL){
			
			detailBean = (OrderDetailBean) this.getIntent().getExtras().getSerializable(KEY_EXTRA_ORDER);
		}else if(intentFrom == KEY_EXTRA_FROM_LIST){
			itemBean = (OrderItemBean) this.getIntent().getSerializableExtra(KEY_EXTRA_LIST_ITEM);
		}
		Log.d(TAG1, "detailBean = "+detailBean);
		fragmentManager = getSupportFragmentManager();
		refundFragment = new RefundFragment(detailBean,itemBean,mHandler,this);
		changeProductFragment = new ExchangeProductFragment(detailBean,itemBean,mHandler,this);
		
		fragmentManager.beginTransaction().add(R.id.container, changeProductFragment).commit();
		fragmentManager.beginTransaction().add(R.id.container, refundFragment).commit();
		tv_refund.performClick();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.tv_refund:
			line_refund.setVisibility(View.VISIBLE);
			line_exchange.setVisibility(View.INVISIBLE);
			tv_exchange.setTextColor(getResources().getColor(R.color.black));
			tv_refund.setTextColor(getResources().getColor(R.color.sheng_red));
			showFragment(refundFragment, changeProductFragment);
			break;
		case R.id.tv_exchange:
			line_refund.setVisibility(View.INVISIBLE);
			line_exchange.setVisibility(View.VISIBLE);
			tv_exchange.setTextColor(getResources().getColor(R.color.sheng_red));
			tv_refund.setTextColor(getResources().getColor(R.color.black));
			showFragment(changeProductFragment, refundFragment);
			break;

		default:
			break;
		}
		
	}
	
	private void showFragment(Fragment show, Fragment hide){
		fragmentManager.beginTransaction().show(show).commit();
		fragmentManager.beginTransaction().hide(hide).commit();
		
		scrollView.smoothScrollTo(0, 0);//将视图移动到顶部
//		scrollView.scrollTo(0, 0);
	}
	
	private void replaceFragment(Fragment fragment){
		scrollView.scrollTo(0, 0);
		fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
	}
}
