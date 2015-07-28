/**
 * 
 */
package com.eims.tjxl_andorid.ui.home;

import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.BannerAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseLazyFragment;
import com.eims.tjxl_andorid.base.ImageDataLoader;
import com.eims.tjxl_andorid.base.ImageDataLoader.OnLoaderCompleteListener;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.BannerBean;
import com.eims.tjxl_andorid.entity.BannerBean.BannerItem;
import com.eims.tjxl_andorid.entity.HomeBean;
import com.eims.tjxl_andorid.ui.exhibition.ExhibitionActivity;
import com.eims.tjxl_andorid.ui.home.area.SimpleAreaActivity;
import com.eims.tjxl_andorid.ui.product.GlobalClassifySearchActivity;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.utils.ToolImageLoad;
import com.eims.tjxl_andorid.utils.Utils;
import com.eims.tjxl_andorid.weght.flow.CircleFlowIndicator;
import com.eims.tjxl_andorid.weght.flow.ViewFlow;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshBase;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONObject;

import java.util.ArrayList;

import loopj.android.http.RequestParams;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 逛商城
 * @Author zd
 * @date 2015年4月14日 下午3:10:29
 *************************************************************************** 
 */
public class HomeFragment extends BaseLazyFragment implements OnClickListener{

	public static HomeFragment homeFragment;
	// 标志位，标志已经初始化完成。
	private boolean isPrepared;
	private View inflate;
	private ViewFlow mViewFlow;
	private CircleFlowIndicator indicator;
	private BannerAdapter bannerAdapter;
	private ArrayList<BannerItem> imagePath;
	private final int AD_WIDTH = 480;
	private final int AD_HEIGHT = 251;
	private FrameLayout frameLayout;
	private ImageButton imgBtn_left;
	private RelativeLayout rl_serach;
	private LinearLayout layout_tj;// 特价专区
	private LinearLayout layout_rx;// 热销商品
	private LinearLayout layout_pp;// 品牌馆
	private LinearLayout layout_pp_rootview;
	private LinearLayout layout_mx;// 明星厂家
	private LinearLayout layout_mxcj;// 明星厂家
	private LinearLayout layout_xy;// 鞋样推荐
	private LinearLayout ll_hotproduct;// 热销商品布局容器
	private LinearLayout ll_starfactory;// 明星厂家商品布局容器
	private LinearLayout ll_brandpavilion;// 品牌馆布局容器
	private LinearLayout ll_shoerecommended;// 鞋样推荐布局容器
	private HomeBean homeBean;
	private ImageView mTj01,mTj02,mTj03,mTj04,mTj05;
	private ImageView mHt01,mHt02,mHt03,mHt04;
	//明星厂家
	private ImageView mstart01,mstart02,mstart03,mstart04;
	private ImageView mppg01,mppg02,mppg03,mppg04;
	private ImageView xytj01,xytj04,xytj05,xytj06;
	private LinearLayout ll_stzt;
	private PullToRefreshScrollView mRefreshScrollView;
	private int pageIndex = 1;
	public static HomeFragment getInstance() {
		if (homeFragment == null) {
			homeFragment = new HomeFragment();
		}
		return homeFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.home_layout, null);
		findviews();
		lazyload();
		return inflate;
	}

	private void setLinsenter() {
		imgBtn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 显示分类
				ActivitySwitch.openActivity(GlobalClassifySearchActivity.class, null,
						getActivity());
			}
		});
		rl_serach.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ActivitySwitch.openActivity(SearchActivity.class, null,
						getActivity());
			}
		});
		layout_tj.setOnClickListener(this);
		layout_rx.setOnClickListener(this);
		layout_xy.setOnClickListener(this);
		ll_stzt.setOnClickListener(this);
	}

	private void initBannerPager() {
		WindowManager manager = getActivity().getWindowManager();
		Point point = AppContext.getPICSize(manager);
		int PIC_WID = point.x;// 屏幕的宽
		int bitmapWidth = AD_WIDTH;
		int bitmapHeight = AD_HEIGHT;
		int wedgitWidth = PIC_WID;
		// 根据容器的宽度去缩放图片的高度
		int scallHeight = AppContext.getScallZoomHeight(manager, bitmapHeight,
				bitmapWidth, wedgitWidth);
		frameLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, scallHeight));

		bannerAdapter = new BannerAdapter(getActivity());
	

		bannerAdapter.addItem(imagePath);
		mViewFlow.setmSideBuffer(imagePath.size());
		mViewFlow.setAdapter(bannerAdapter);
		mViewFlow.setFlowIndicator(indicator);
		mViewFlow.setTimeSpan(3000);
		mViewFlow.setSelection(0); // 设置初始位置
		mViewFlow.startAutoFlowTimer();
	}

	private void findviews() {
		// TODO Auto-generated method stub
		imgBtn_left = (ImageButton) inflate.findViewById(R.id.left_menu_btn);
		rl_serach = (RelativeLayout) inflate.findViewById(R.id.rl_home_serach);
		frameLayout = (FrameLayout) inflate.findViewById(R.id.view_flow_frame);
		mViewFlow = (ViewFlow) inflate.findViewById(R.id.viewflow);
		mTj01=(ImageView) inflate.findViewById(R.id.tj_image_first);
		mTj02=(ImageView) inflate.findViewById(R.id.tj_image_second);
		mTj03=(ImageView) inflate.findViewById(R.id.tj_image_third);
		mTj04=(ImageView) inflate.findViewById(R.id.tj_image_four);
		mTj05=(ImageView) inflate.findViewById(R.id.tj_image_five);
		
		mHt01=(ImageView) inflate.findViewById(R.id.rx01);
		mHt02=(ImageView) inflate.findViewById(R.id.rx02);
		mHt03=(ImageView) inflate.findViewById(R.id.rx03);
		mHt04=(ImageView) inflate.findViewById(R.id.rx04);
		
		xytj01=(ImageView) inflate.findViewById(R.id.xy01);
//		xytj02=(ImageView) inflate.findViewById(R.id.xytj02);
//		xytj03=(ImageView) inflate.findViewById(R.id.xytj03);
		xytj04=(ImageView) inflate.findViewById(R.id.xy02);
		xytj05=(ImageView) inflate.findViewById(R.id.xy03);
		xytj06=(ImageView) inflate.findViewById(R.id.xy04);
		
		layout_xy= (LinearLayout) inflate.findViewById(R.id.layout_xy);	
		indicator = (CircleFlowIndicator) inflate.findViewById(R.id.viewflowindic);
		ll_hotproduct = (LinearLayout) inflate.findViewById(R.id.ll_hotsale_product);
		layout_tj = (LinearLayout) inflate.findViewById(R.id.layout_tj);
		layout_rx = (LinearLayout) inflate.findViewById(R.id.layout_rx);
		ll_starfactory = (LinearLayout) inflate.findViewById(R.id.ll_star_factory);
		ll_brandpavilion = (LinearLayout) inflate.findViewById(R.id.ll_Brand_pavilion);
		ll_stzt=(LinearLayout) inflate.findViewById(R.id.ll_stzt);
		ll_shoerecommended = (LinearLayout) inflate.findViewById(R.id.ll_shoe_recommended);
		mRefreshScrollView=(PullToRefreshScrollView) inflate.findViewById(R.id.scrollview);
		// 控件初始化完成
		isPrepared = true;
	}

	@Override
	protected void lazyload() {
		if (!isPrepared || !isVisible) {
			return;
		}
		imagePath = new ArrayList<BannerItem>();
 
		loadBanner();
		addstartFactoryView();
		addbrandprvionView();
		setLinsenter();
		getHomeData();
		setPullRefreshView();
		// 数据加载完时 ，要赋值为false，不然数据会重复加载
		isPrepared = false;
	}

	private void loadBanner(){
		ImageDataLoader  dataLoader=new ImageDataLoader(getActivity(), ImageDataLoader.FIRST_PAGE);
		dataLoader.setOnLoaderCompleteListener(new OnLoaderCompleteListener() {		
			@Override
			public void onComplete(ArrayList<BannerItem> imagePath) {
				// TODO Auto-generated method stub
			     HomeFragment.this.imagePath=imagePath;
			     initBannerPager();
			}
		});
	}

	/**
	 * 
	 * 添加明星厂家.
	 */
	private void addstartFactoryView() {
		View view = LayoutInflater.from(ct).inflate(
				R.layout.startfactory_layout, null);
		layout_mx= (LinearLayout) view.findViewById(R.id.layout_mx);
		layout_mxcj=(LinearLayout) view.findViewById(R.id.ll_startfactory);
		mstart01=(ImageView) view.findViewById(R.id.start01);
		mstart02=(ImageView) view.findViewById(R.id.start02);
		mstart03=(ImageView) view.findViewById(R.id.start03);
		mstart04=(ImageView) view.findViewById(R.id.start04);	
		layout_mx.setOnClickListener(this);
		layout_mxcj.setOnClickListener(this);
		ll_starfactory.addView(view);
	}

	/**
	 * 
	 * 添加品牌馆
	 * 
	 * @author zd
	 */
	private void addbrandprvionView() {
		View view = LayoutInflater.from(ct).inflate(
				R.layout.brand_pavilion_layout, null);
		layout_pp= (LinearLayout) view.findViewById(R.id.layout_pp);
		layout_pp_rootview= (LinearLayout) view.findViewById(R.id.ll_pp_rootview);
		mppg01=(ImageView) view.findViewById(R.id.ppg01);
		mppg02=(ImageView) view.findViewById(R.id.ppg02);
		mppg03=(ImageView) view.findViewById(R.id.ppg03);
		mppg04=(ImageView) view.findViewById(R.id.ppg04);
		layout_pp.setOnClickListener(this);
		layout_pp_rootview.setOnClickListener(this);
		ll_brandpavilion.addView(view);
	}


	/**查询首页数据**/
	private void  getHomeData(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(getActivity(), true,"正在加载..."){
          	@Override
          	public void onSuccess(int statusCode, String content) {
          		// TODO Auto-generated method stub
          		super.onSuccess(statusCode, content);
          		LogUtil.d(TAG, content);
          		mRefreshScrollView.onRefreshComplete();
          		resolve(content);
          	}
          	@Override
          	public void onFailure(Throwable error, String content) {
          	// TODO Auto-generated method stub
             	super.onFailure(error, content);
            	mRefreshScrollView.onRefreshComplete();
          	}
	};
	RequestParams  params=new RequestParams();
	HttpClient.QueryHome(mHandler, params);
	}

	/**   
	 * 解析数据
	 */ 
	protected void resolve(String content) {
		// TODO Auto-generated method stub
		try {
			JSONObject object=new JSONObject(content);
			String type = object.getString("type");
			if("1".equals(type)){
				String msg = object.getString("msg");
				String json = object.getString("data");
				homeBean = GsonUtils.json2bean(json, HomeBean.class);	
			}else{
				TipToast.makeText(getActivity(), "亲，你的网络不太流畅哦", Toast.LENGTH_SHORT).show();
			}
			showImage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private  void  showImage(){
		showSP();
		showHotGood();
		showStartFactory();
		showPPG();
		showXYTJ();
	}
	/**显示特价图片**/
	private void  showSP(){
		if(homeBean!=null&&homeBean.spList!=null){

			DisplayImageOptions fadeOptions = ToolImageLoad.getFadeOptions(R.drawable.morenpic,R.drawable.morenpic,R.drawable.morenpic);
			ImageManager.Load(homeBean.spList.get(0).img, mTj01,fadeOptions);
			ImageManager.Load(homeBean.spList.get(1).img, mTj02,fadeOptions);
			ImageManager.Load(homeBean.spList.get(2).img, mTj03,fadeOptions);
			ImageManager.Load(homeBean.spList.get(3).img, mTj04,fadeOptions);
			ImageManager.Load(homeBean.spList.get(4).img, mTj05,fadeOptions);
			mTj01.setOnClickListener(new ImageClick(homeBean.spList.get(0).commodity_id));
			mTj02.setOnClickListener(new ImageClick(homeBean.spList.get(1).commodity_id));
			mTj03.setOnClickListener(new ImageClick(homeBean.spList.get(2).commodity_id));
			mTj04.setOnClickListener(new ImageClick(homeBean.spList.get(3).commodity_id));
			mTj05.setOnClickListener(new ImageClick(homeBean.spList.get(4).commodity_id));
			LogUtil.d("特价大图：",homeBean.spList.get(4).img);
		}
	}
	
	/**显示热销图片**/
	private void  showHotGood(){
		if(homeBean!=null&&homeBean.htList!=null){
			DisplayImageOptions fadeOptions = ToolImageLoad.getFadeOptions(R.drawable.morenpic,R.drawable.morenpic,R.drawable.morenpic);
			ImageManager.Load(homeBean.htList.get(0).img, mHt01,fadeOptions);
			ImageManager.Load(homeBean.htList.get(1).img, mHt02,fadeOptions);
			ImageManager.Load(homeBean.htList.get(2).img, mHt03,fadeOptions);
			ImageManager.Load(homeBean.htList.get(3).img, mHt04,fadeOptions);
			
			mHt01.setOnClickListener(new ImageClick(homeBean.htList.get(0).commodity_id));
			mHt02.setOnClickListener(new ImageClick(homeBean.htList.get(1).commodity_id));
			mHt03.setOnClickListener(new ImageClick(homeBean.htList.get(2).commodity_id));
			mHt04.setOnClickListener(new ImageClick(homeBean.htList.get(3).commodity_id));
		}
	}
	/**显示明星厂家图片**/
	private void  showStartFactory(){
		if(homeBean!=null&&homeBean.fcList!=null){
			DisplayImageOptions fadeOptions = ToolImageLoad.getFadeOptions(R.drawable.morenpic,R.drawable.morenpic,R.drawable.morenpic);
			ImageManager.Load(homeBean.fcList.get(0).img,mstart01,fadeOptions);
			ImageManager.Load(homeBean.fcList.get(1).img, mstart02,fadeOptions);
			ImageManager.Load(homeBean.fcList.get(2).img, mstart03,fadeOptions);
			ImageManager.Load(homeBean.fcList.get(3).img, mstart04,fadeOptions);

		}
	}
	
	/**显示品牌馆 图片**/
	private void  showPPG(){
		if(homeBean!=null&&homeBean.bdList!=null){
			DisplayImageOptions fadeOptions = ToolImageLoad.getFadeOptions(R.drawable.morenpic,R.drawable.morenpic,R.drawable.morenpic);
			ImageManager.Load(homeBean.bdList.get(0).img,mppg01,fadeOptions);
			ImageManager.Load(homeBean.bdList.get(1).img, mppg02,fadeOptions);
			ImageManager.Load(homeBean.bdList.get(2).img, mppg03,fadeOptions);
			ImageManager.Load(homeBean.bdList.get(3).img, mppg04,fadeOptions);

		}
	}
	
	/**显示图片**/
	private void  showXYTJ(){
		if(homeBean!=null&&homeBean.exList!=null){
			LogUtil.d("实体展厅", "homeBean.exList--"+homeBean.exList.size());
			DisplayImageOptions fadeOptions = ToolImageLoad.getFadeOptions(R.drawable.morenpic,R.drawable.morenpic,R.drawable.morenpic);
			ImageManager.Load(homeBean.exList.get(0).img,xytj01,fadeOptions);
//			ImageManager.Load(homeBean.smList.get(1).img, xytj02,fadeOptions);
//			ImageManager.Load(homeBean.smList.get(2).img, xytj03,fadeOptions);
			ImageManager.Load(homeBean.exList.get(1).img, xytj04,fadeOptions);
			ImageManager.Load(homeBean.exList.get(2).img, xytj05,fadeOptions);
			ImageManager.Load(homeBean.exList.get(3).img, xytj06,fadeOptions);
			
//			xytj01.setOnClickListener(new ImageClick(homeBean.smList.get(0).commodity_id));
////			xytj02.setOnClickListener(new ImageClick(homeBean.smList.get(1).commodity_id));
////			xytj03.setOnClickListener(new ImageClick(homeBean.smList.get(2).commodity_id));
//			xytj04.setOnClickListener(new ImageClick(homeBean.smList.get(1).commodity_id));
//			xytj05.setOnClickListener(new ImageClick(homeBean.smList.get(2).commodity_id));
//			xytj06.setOnClickListener(new ImageClick(homeBean.smList.get(3).commodity_id));
		}
	}

	@Override
	public void onClick(View v) {
		Bundle bundle=new Bundle();
		switch (v.getId()){
			case R.id.layout_tj://特价专区
				bundle.putInt(SimpleAreaActivity.AREA_TYPE, 1);
				ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle, getActivity());
				break;
			case R.id.layout_rx://热销商品
				bundle.putInt(SimpleAreaActivity.AREA_TYPE, 2);
				ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle, getActivity());
				break;
			case R.id.layout_pp://品牌馆
				bundle.putInt(SimpleAreaActivity.AREA_TYPE, 4);
				ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle, getActivity());
				break;
			case R.id.layout_mx://明星厂家
				bundle.putInt(SimpleAreaActivity.AREA_TYPE, 3);
				ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle, getActivity());
				break;
			case R.id.ll_stzt:
			case R.id.layout_xy://实体展厅
//				bundle.putInt(SimpleAreaActivity.AREA_TYPE, 5);
//				ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle, getActivity());
				ActivitySwitch.openActivity(ExhibitionActivity.class, null, getActivity());
				break;
			case R.id.ll_startfactory:
				bundle.putInt(SimpleAreaActivity.AREA_TYPE, 3);
				ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle, getActivity());
				break;
			case R.id.ll_pp_rootview:
				bundle.putInt(SimpleAreaActivity.AREA_TYPE, 4);
				ActivitySwitch.openActivity(SimpleAreaActivity.class, bundle, getActivity());
				break;
		}
	}

	class ImageClick implements OnClickListener{
		
		private String goodId;
		public ImageClick(String goodid){
			this.goodId=goodid;
		}
		@Override
		public void onClick(View v) {
			LogUtil.d("ImageClick", goodId+"");
			Bundle bundle=new Bundle();
			bundle.putString(ProductDeatil.INTENT_KEY, goodId);
			ActivitySwitch.openActivity(ProductDeatil.class, bundle, getActivity());
		}		
	}

	
	private void setPullRefreshView() {
		mRefreshScrollView.setEnabled(false);
		// 设定上下拉刷新
		// lv_exhibition.setMode(Mode.BOTH);
		mRefreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		mRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.getCurrTiem());
		mRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉即可刷新...");
		mRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("加载中...");
		mRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("释放即可刷新...");

		// 下拉刷新数据
		mRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
			// 下拉刷新
			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				mRefreshScrollView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(
						"最近更新: "+ Utils.Long2DateStr_detail(System.currentTimeMillis()));
                      getHomeData();
                      loadBanner();
			}

			// 加载更多
			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				// 加载更多
			}
		});
	}
	
}
