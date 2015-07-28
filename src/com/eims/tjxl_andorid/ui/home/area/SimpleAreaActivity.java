package com.eims.tjxl_andorid.ui.home.area;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.base.ImageDataLoader;
import com.eims.tjxl_andorid.entity.BannerBean;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.weght.HeadView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 专区类型 Created by kuangyong on 2015/6/24.
 */
public class SimpleAreaActivity extends BaseActivity implements View.OnClickListener{
	public static final String AREA_TYPE = "type";// 专区类型
	private TextView btnlist;// 列表
	private TextView btngrid;// 缩略图
	private LinearLayout llimagetype;// 转换展示类型
	private LinearLayout layout_change;//
	private ImageView ivmain;// 顶部图片
	private EditText serachedit;// 搜索输入框
	private Button btnserach;// 搜索按钮
	private HeadView head;
	private int areaType;// 专区类型 1.特价专区 2.热销商品 3.明星厂家 4.品牌馆 5.鞋样推荐
	private Map<Integer, String> typeMap;
	private DiscountAreaFragment discountAreaFragment;// 特价专区
	private SellingProductsFragment sellingProductsFragment;// 热销商品
	private BrandHallFragment brandHallFragment;// 品牌馆
	private StarFactoryFragment starFactoryFragment;// 明星厂家
	private ShoePatternFragment shoePatternFragment;// 鞋样推荐
	private String keyWord="";//关键字

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_area_layout);
		areaType = getIntent().getIntExtra(AREA_TYPE, 1);
		getTypeMap();
		findViews();
		setlistener();
		initheadview();
		init();
		String location="";
		switch (areaType) {
			case 1:// 特价专区
				location=ImageDataLoader.DISCOUNT_PAGE;
				break;
			case 2:// 热销商品
				location=ImageDataLoader.HOTAREA_PAGE;
				break;
			case 3:// 明星厂家
				location=ImageDataLoader.STARFACTORY_PAGE;
				break;
			case 4:// 品牌馆
				location=ImageDataLoader.BRAND_PAGE;
				break;
		}
		ImageDataLoader loader=new ImageDataLoader(mContext,location);
		loader.setOnLoaderCompleteListener(new ImageDataLoader.OnLoaderCompleteListener() {
			@Override
			public void onComplete(ArrayList<BannerBean.BannerItem> imagePath) {
				if(null!=imagePath&&imagePath.size()!=0){
					final BannerBean.BannerItem item=imagePath.get(0);
					ImageManager.Load(item.img,ivmain);
					ivmain.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							ImageDataLoader.gotoWitchOne(SimpleAreaActivity.this,item);
						}
					});
				}
			}
		});
	}

	private void setlistener() {
		btnlist.setOnClickListener(this);
		btngrid.setOnClickListener(this);
		btnserach.setOnClickListener(this);
	}

	private void getTypeMap() {
		typeMap = new HashMap<Integer, String>();
		typeMap.put(1, "特价专区");
		typeMap.put(2, "热销商品");
		typeMap.put(3, "明星厂家");
		typeMap.put(4, "品牌馆");
		typeMap.put(5, "鞋样推荐");
	}

	private void findViews() {
		this.btnserach = (Button) findViewById(R.id.btn_serach);
		this.serachedit = (EditText) findViewById(R.id.serach_edit);
		this.ivmain = (ImageView) findViewById(R.id.iv_main);
		this.llimagetype = (LinearLayout) findViewById(R.id.ll_image_type);
		this.layout_change = (LinearLayout) findViewById(R.id.layout_change);
		this.btngrid = (TextView) findViewById(R.id.btn_grid);
		this.btnlist = (TextView) findViewById(R.id.btn_list);
		this.head = (HeadView) findViewById(R.id.head);
	}

	private void initheadview() {
		String titleName = typeMap.get(areaType);
		head.setText(titleName);
		head.setGobackVisible();
		head.setRightResource();
	}

	private void init() {
		switch (areaType) {
		case 1:// 特价专区
            discountAreaFragment = DiscountAreaFragment.newInstance(keyWord);
            showFragment(discountAreaFragment);
			break;
		case 2:// 热销商品
			sellingProductsFragment=SellingProductsFragment.newInstance(keyWord);
			showFragment(sellingProductsFragment);
			break;
		case 3:// 明星厂家
			starFactoryFragment=StarFactoryFragment.newInstance(keyWord);
			showFragment(starFactoryFragment);
			layout_change.setVisibility(View.GONE);//隐藏切换模式
			break;
		case 4:// 品牌馆
			brandHallFragment=BrandHallFragment.newInstance(keyWord);
			showFragment(brandHallFragment);
			break;
		case 5:// 鞋样推荐
			shoePatternFragment=ShoePatternFragment.newInstance(keyWord);
			showFragment(shoePatternFragment);
			break;
		}

	}

	/** 当前显示的fragment **/
	private Fragment currentFragment = null;

	/**
	 * 显示framgnet
	 * 
	 * @param fragment
	 */
	private void showFragment(Fragment fragment) {
		// 非空
		if (null == fragment) {
			return;
		}
		// 已经显示
		if (fragment.isAdded() && !fragment.isHidden()) {
			return;
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transation = fragmentManager.beginTransaction();
		// 隐藏当前显示的fragment
		if (null != currentFragment && currentFragment.isAdded()) {
			transation.hide(currentFragment);
		}
		// 显示传入的fragment
		if (!fragment.isAdded()) {
			transation.add(R.id.frame_pro_list, fragment);
			transation.commit();
		} else {
			transation.show(fragment);
			transation.commit();
		}
		// 记住当前的fragment
		currentFragment = fragment;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_serach://搜索
				keyWord=serachedit.getText().toString();
				init();
				break;
			case R.id.btn_list://列表
				showListOrList(true);
				break;
			case R.id.btn_grid://缩略图
				showListOrList(false);
				break;
		}
	}

	/**
	 * 是否显示列表
	 * @param isList
	 */
	private void showListOrList(boolean isList){
		setShowTypeBackground(isList);
		switch (areaType) {
			case 1:// 特价专区
				discountAreaFragment.showListOrGrid(isList);
				break;
			case 2:// 热销商品
				sellingProductsFragment.showListOrGrid(isList);
				break;
			case 3:// 明星厂家
				break;
			case 4:// 品牌馆
				brandHallFragment.showListOrGrid(isList);
				break;
			case 5:// 鞋样推荐
				shoePatternFragment.showListOrGrid(isList);
				break;
		}
	}

	/**
	 * 设置背景
	 * @param isList
	 */
	private void setShowTypeBackground(boolean isList){
		if(isList){//列表
			llimagetype.setBackgroundResource(R.drawable.lbs);
		}else{//缩略图
			llimagetype.setBackgroundResource(R.drawable.slt);
		}
	}
}
