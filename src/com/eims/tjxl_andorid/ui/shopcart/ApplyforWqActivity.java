/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import loopj.android.http.RequestParams;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.MyListAdapter;
import com.eims.tjxl_andorid.adapter.WqPopAdapter.onItemClickListener;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.AppyforWqBean;
import com.eims.tjxl_andorid.entity.Picture;
import com.eims.tjxl_andorid.entity.AppyforWqBean.GoodColorList;
import com.eims.tjxl_andorid.entity.AppyforWqBean.GoodList;
import com.eims.tjxl_andorid.entity.AppyforWqBean.WqType;
import com.eims.tjxl_andorid.entity.OrderDetailBean.OrderGoodBean;
import com.eims.tjxl_andorid.entity.OrderDetailBean.OrderGoodSizeBean;
import com.eims.tjxl_andorid.ui.shopcart.RefundFragment.MyGridAdapter;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.utils.imageupload.BitmapUtils;
import com.eims.tjxl_andorid.utils.imageupload.ImageFolder;
import com.eims.tjxl_andorid.utils.imageupload.ImageUpload;
import com.eims.tjxl_andorid.utils.imageupload.SelectPicPopupWindow;
import com.eims.tjxl_andorid.utils.imageupload.ImageUpload.UpLoadImageListener;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.ImageViewWithDel;
import com.eims.tjxl_andorid.weght.MyListView;
import com.eims.tjxl_andorid.weght.MyPopupWindow;
import com.eims.tjxl_andorid.weght.WqPopupWindow;
import com.eims.tjxl_andorid.weght.ImageViewWithDel.MyImageViewCallBack;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 申请维权
 * @Author zd
 * @date 2015年7月6日  下午3:42:01
 *************************************************************************** 
 */
public class ApplyforWqActivity extends BaseActivity {

	protected static final String TAG = "ApplyforWqActivity";
    private HeadView headView;
	public static  String ITEM_TAG = "activity_item_tag";
    public static String ORDER_ID="order_id";//订单id
    public static String ORDER_CODE="order_code";//订单编号
    public static String RETURN_ORDERCODE_ACTION="return_order_wqapplyfor.action";//退货维权申请成功 ation
    public static String REPLACE_ORDER_ACTION="replace_order_wqapplyfor.action";//换货维权申请成功 ation
    public static String ACTION_SEND_PRODUCT="action.send.product";//买家（退货/换货）发货之后刷新列表

    private Bundle bundle;
	private String orderid;//退货订单id
	private String orderCode;//退货订单编号
	/**代表从哪个list界面进入**/
	private String flag="1";//1 代表退货列表   2换货列表  3维权列表 
	private AppyforWqBean wqBean;
    private TextView cjName;//厂家店铺名称
    private MyListView myListView;
    private List<GoodList> dataList=new ArrayList<AppyforWqBean.GoodList>();
    private GoodAdapter mAdapter;
    private TextView totalPrice;//商品总价
    private RelativeLayout rl_wqType;//维权类型布局
    private TextView wqType;//维权类型
    private RelativeLayout rl_handling;//处理方式布局
    private TextView qwclHandling;//期望处理方式
	private WqPopupWindow popupWindow;
	private List<WqType> mItems;
	private MyPopupWindow myPopupWindow;
	private List<String> handItems;
	private WqType type;//维权类型对象
	private RelativeLayout root;
	private MyGridAdapter gridAdapter;//上传图片显示adpter
	private GridView mGridView;
	private Button btn_commint;
	private EditText wqDesc;//维权说明
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wq_applyfor_layout);
		findviews();
		setActionBar();
	   getIntentBundles();
		loadwq();
		setListener();
	}
	private void findviews(){
		headView=(HeadView) findViewById(R.id.head);
        cjName=(TextView) findViewById(R.id.cj_shoename);
        myListView=(MyListView) findViewById(R.id.order_applyforwq_listview);
        totalPrice=(TextView) findViewById(R.id.total_wqprice);
        rl_wqType=(RelativeLayout) findViewById(R.id.ll_wqtype);
        wqType=(TextView) findViewById(R.id.wq_type);
        root=(RelativeLayout) findViewById(R.id.root);
        mGridView=(GridView) findViewById(R.id.gridview);
        btn_commint=(Button) findViewById(R.id.btn_commint_wqsq);
        wqDesc=(EditText) findViewById(R.id.desc);
        rl_handling=(RelativeLayout) findViewById(R.id.rl_handling);
        qwclHandling=(TextView) findViewById(R.id.qwcl_handling);
	}
	private void setActionBar() {
		headView.setText("申请维权");
		headView.setGobackVisible();
		headView.setRightGone();
	}
	
	private  void  getIntentBundles(){
		if(getIntent()!=null){
			bundle = getIntent().getExtras();
			orderid = bundle.getString(ORDER_ID);
			orderCode = bundle.getString(ORDER_CODE);
			flag=bundle.getString(ITEM_TAG);
		 }
	}
	
	private void showUI(){
		if(wqBean!=null){
			cjName.setText(wqBean.map.f_factory_name);
			totalPrice.setText("小计金额：￥"+wqBean.map.total_price);
			dataList.clear();
			dataList.addAll(wqBean.data);
			if(mAdapter==null){
				mAdapter=new GoodAdapter();
			}
			myListView.setAdapter(mAdapter);
			initpop();
			if(gridAdapter==null){
				gridAdapter=new MyGridAdapter();
			}
			mGridView.setAdapter(gridAdapter);
			
		}
	}
	/**初始化pop中的数据**/
    private  void  initpop(){
	 	mItems = new ArrayList<WqType>();
	 	handItems=new ArrayList<String>();
	 	mItems.clear();
	 	handItems.clear();
	    for(WqType temp : wqBean.typeList){
	    	mItems.add(temp);
	    }
	    handItems.add("退货");
	    handItems.add("退款");
	    handItems.add("换货");
	    handItems.add("其他");
		mPicList = new ArrayList<Picture>();// GridView中显示需要上传的图片
		mPathList = new ArrayList<String>();// 上传图片的本地路径
		mUrlList = new ArrayList<String>();// 图片上传后返回图片的网络地址
		mPicList.add(mAddPicture);
	    
	    
 }
	private void  setListener(){
		rl_wqType.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				showOrderTypePop();
			}
		});
		rl_handling.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				QwClHandlingPop();
			}
		});
		btn_commint.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(checkComintInfo()){
					uploadImages();
				}
			}
		});
	}
	
	private  boolean  checkComintInfo(){
		String str = wqType.getText().toString().trim();//处理类型
		String qlfs= qwclHandling.getText().toString().trim();//处理类型
		if (mPathList.size() == 0) {
            TipToast.makeText(ApplyforWqActivity.this, "请至少选择一张图片上传",Toast.LENGTH_SHORT).show();
			return false;
		}
		if("请选择".equals(str)){
			    TipToast.makeText(ApplyforWqActivity.this, "请选择维权类型",Toast.LENGTH_SHORT).show();
				return false;
		}
		if(type==null){
			   TipToast.makeText(ApplyforWqActivity.this, "请选择维权类型",Toast.LENGTH_SHORT).show();
				return false;
		}
		if("请选择".equals(qlfs)){
		    TipToast.makeText(ApplyforWqActivity.this, "请选择处理方式",Toast.LENGTH_SHORT).show();
			return false;
	}
		if(TextUtils.isEmpty(wqDesc.getText().toString())){
			TipToast.makeText(ApplyforWqActivity.this, "请输入维权说明",Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	/**维权类型下拉框**/
    private void  showOrderTypePop(){
    	popupWindow = new WqPopupWindow(this, rl_wqType.getWidth(),mItems);
		popupWindow.showAsDropDown(rl_wqType, 0, 0);
		popupWindow.setOnItemClickListener(new onItemClickListener() {
			@Override
			public void click(int position, View view) {
				wqType.setText(mItems.get(position).type_name);		
				type = mItems.get(position);
		}
	});
    }
    
	/**期望处理类型下拉框**/
    private void  QwClHandlingPop(){
    	myPopupWindow = new MyPopupWindow(this, rl_handling.getWidth(),handItems);
    	myPopupWindow.showAsDropDown(rl_handling, 0, 0);
    	myPopupWindow.setOnItemClickListener(new MyListAdapter.onItemClickListener() {
			@Override
			public void click(int position, View view) {
				// TODO Auto-generated method stub
				qwclHandling.setText(handItems.get(position));
			}
		});
	
    }
	/**请求维权界面数据**/
	private void loadwq(){
		if(!available){
			TipToast.makeText(mContext, "亲，你的网络不太流畅哦", 0).show();
			return;
		}
		CustomResponseHandler mHandler=new CustomResponseHandler(mContext, true, "正在加载..."){
			@Override
		    public void onSuccess(int statusCode, String content) {
		    	// TODO Auto-generated method stub
		    	super.onSuccess(statusCode, content);
		    	//LogUtil.d(TAG, content);
		    	wqBean = GsonUtils.json2bean(content, AppyforWqBean.class);
		    	LogUtil.d("ApplyforWqActivity", wqBean.data.size()+"");
		    	btn_commint.setEnabled(true);
		    	showUI();
		    }	
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				TipToast.makeText(mContext, "服务端出现异常！", 0).show();
				btn_commint.setEnabled(false);
			}
		};	
		RequestParams params=new RequestParams();
		params.put("id", orderid);
		params.put("uid", user.id);
		if("1".equals(flag)){//退货
			HttpClient.WqReturnGoodApplyfor(mHandler, params);
		}else if("2".equals(flag)){//换货
			HttpClient.iApplyUygurPowerReplace_detail(mHandler, params);
		}
		
		
		
		
	}
	
	   
	    private void  addLayout(LinearLayout ll_order_goodsize, GoodList datas){
	    	for(GoodColorList temp : datas.goodList){
	    		  View view=View.inflate(mContext, R.layout.orderdetail_listview_item_goodsize_layout, null);
	    		  TextView sizeColor=(TextView) view.findViewById(R.id.order_good_size_color);
	    		  TextView sizePrice=(TextView) view.findViewById(R.id.good_sizeprice);
	    		  TextView sizeNum=(TextView) view.findViewById(R.id.good_sizenum);
	    		  String[] split = temp.spec_name_value.split("，");
	    		  String[] colors=split[0].split(":");
	    		  String colorValue=colors[1];
	    		  String[] size=split[1].split(":");
	    		  String sizeValue=size[1];
	    		  sizeColor.setText(colorValue+"      "+ sizeValue);
	    		  sizePrice.setText("￥"+temp.commodity_price);
	    		  sizeNum.setText("x"+temp.quantity);
	    		  
	    		  sizeColor.setTextColor(getResources().getColor(R.color.button_text));
	    		  sizePrice.setTextColor(getResources().getColor(R.color.button_text));
	    		  sizeNum.setTextColor(getResources().getColor(R.color.button_text));
	    		  ll_order_goodsize.addView(view);
	    		  
	    	}
	    }
	    
	    /**
		 * 图片来源选择的popupwindow
		 */
		private ArrayList<Picture> mPicList;// GridView中显示需要上传的图片
		private ArrayList<String> mPathList;// 上传图片的本地路径
		private ArrayList<String> mUrlList;// 图片上传后返回图片的网络地址
		private Picture mAddPicture = new Picture(R.drawable.add_pic, null);
		private int CAMERA_WITH_DATA = 0x11;
		private int CAMERA_PICK_PHOTO = 0x21;
		private SelectPicPopupWindow mPopupWindow = null;

		private void showpopwindow() {
			if (mPopupWindow == null) {
				mPopupWindow = new SelectPicPopupWindow(this,
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (mPopupWindow.isShowing()) {
									mPopupWindow.dismiss();
								}
								switch (v.getId()) {
								case R.id.btn_take_photo: // 拍照
									callCamerImage();
									break;
								case R.id.btn_pick_photo:
									callLocalImage();
									break;
								}
							}
						});
			}
			if (!mPopupWindow.isShowing()) {
				mPopupWindow.showAtLocation(root, Gravity.BOTTOM, 0, 0);
			}
		}
		
		
		/**
		 * 从照相机获取图片
		 */
		private File imageFile;

		public File getImageFile() {
			return imageFile;
		}
		
		private void callCamerImage() {
			imageFile = ImageFolder.getTempImageName();
			Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			Uri uri = Uri.fromFile(imageFile);
			captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			captureIntent.putExtra("return-data", true);
			startActivityForResult(captureIntent, CAMERA_WITH_DATA);
		}

		/**
		 * 从图库获取图片
		 */
		private void callLocalImage() {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
		   startActivityForResult(intent, CAMERA_PICK_PHOTO);
		}
		

		/**
		 * 以下代码用于选择本地图片或拍摄照片上传
		 */
		private Picture mPicture;

		public void refeshPictures(Picture picture) {
			mPicture = picture;
			addPicture();
		}

		private void addPicture() {
			mPathList.add(mPicture.getPath());
			int size = mPicList.size();
			mPicList.remove(size - 1);
			mPicList.add(mPicture);
			if (mPicList.size() < 3) {// 最多加载3张照片
				mPicList.add(mAddPicture);
			}
			gridAdapter.notifyDataSetChanged();
		}
		/***
		 *上传图片。
		 */
		private void uploadImages() {
			UpLoadImageListener mUpLoadImageListener = new UpLoadImageListener() {

				@Override
				public void UpLoadSuccess(ArrayList<String> netimageurls) {
					mUrlList = netimageurls;
					if (mUrlList.size() > 0) {
						CommintApplyforWq();
					}
				}

				@Override
				public void UpLoadFail() {
					Toast.makeText(mContext, "图片上传失败", Toast.LENGTH_SHORT).show();
				}

			};
			ImageUpload imageUpload = new ImageUpload(mContext, mPathList,
					mUpLoadImageListener);
			imageUpload.startLoad();
		}
		
		/**
		 *提交维权申请
		 */
		private  void  CommintApplyforWq(){
			
		 StringBuffer	buffer = new StringBuffer();
			for (int i = 0; i < mUrlList.size(); i++) {
				if (i == mUrlList.size() - 1) {
					buffer.append(mUrlList.get(i));
				} else {
					buffer.append(mUrlList.get(i) + ",");
				}
			}
		    String	imgs = buffer.toString();
			LogUtil.d(TAG, imgs);
			CustomResponseHandler handler=new CustomResponseHandler(mContext, true, "正在提交中...."){		
			        @Override
			        public void onSuccess(int statusCode, String content) {
			        	super.onSuccess(statusCode, content);
			        	LogUtil.d(TAG, content);
			        	try {
							JSONObject obj=new JSONObject(content);
							String msg = obj.getString("msg");
							TipToast.makeText(mContext, "提交成功", 0).show();
							//申请成功，刷新列表页状态
							intent=new Intent();
							if("1".equals(flag)){
								intent.setAction(RETURN_ORDERCODE_ACTION);
							}else if("2".equals(flag)){
								intent.setAction(REPLACE_ORDER_ACTION);
							}
							sendBroadcast(intent);
							new Handler().postDelayed(new Runnable() {				
								@Override
								public void run() {
									AppManager.getAppManager().finishActivity();
								}
							}, 2000);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
			        	
			        }	
			
			        @Override
			        public void onFailure(Throwable error, String content) {
			        super.onFailure(error, content);
			        TipToast.makeText(mContext, "申请失败", 0).show();
			        
			        }
			};
			RequestParams params=new RequestParams();
			params.put("buyer_id", user.id);
			params.put("seller_id", wqBean.map.id);//卖家id
			params.put("from_order_id", orderid);//订单id
			if("1".equals(flag)){
				params.put("from_order_type","2");//来源订单类型  0订单  1 换货 2退货  
			}else if("2".equals(flag)){
				params.put("from_order_type","1");//来源订单类型  0订单  1 换货 2退货  
			}
			params.put("uygur_power_type_id", type.id);//维权类型
			params.put("evidence_img", imgs);//图片
			params.put("description", wqDesc.getText().toString());//维权说明
			params.put("expect_way", qwclHandling.getText().toString());//处理方式
			params.put("from_order_code", orderCode);//订单编号
            HttpClient.CommintReturnGoodWqApplyfor(handler, params);
		}
		
		public File imageFiles;
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode != Activity.RESULT_OK)
				return;
			imageFiles = getImageFile();
			String localPath = null;
			Picture picture = null;
			switch (requestCode) {
			case 0x11:
				localPath = imageFile.getPath();
				Log.d(TAG, "图片来自图库，Path：" + imageFile.getPath());
				break;
			case 0x21:
				Uri selectedImage = data.getData();
				ContentResolver resolver = getContentResolver();
				String[] filePathColumns = { MediaStore.Images.Media.DATA };
				Cursor c = this.getContentResolver().query(selectedImage,
						filePathColumns, null, null, null);
				c.moveToFirst();
				int columnIndex = c.getColumnIndex(filePathColumns[0]);
				String picturePath = c.getString(columnIndex);
				c.close();
				localPath = picturePath;
				Log.d(TAG, "图片来自图库，Path：" + picturePath);
				break;
			}
			if(!(localPath.endsWith("jpg") || localPath.endsWith("png"))){
				Toast.makeText(this, "图片上传失败，仅支持“jpg”和“png”格式", Toast.LENGTH_SHORT).show();
				return;
			}if(new File(localPath).length() > (1024*1024)){
				Toast.makeText(this, "图片上传失败，图片尺寸不得超过1M", Toast.LENGTH_SHORT).show();
				return;
			}
			
			refeshPictures(new Picture(0, localPath));
		}
		
		
		
		class  GoodAdapter extends BaseAdapter{
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return dataList==null ? 0 :dataList.size();
			}

			@Override
			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return dataList.get(arg0);
			}

			@Override
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return arg0;
			}

		
			@Override
			public View getView(int arg0, View covertview, ViewGroup arg2) {
		         if(covertview==null){
		        	 covertview=View.inflate(mContext, R.layout.order_detail_listview_item_layout, null);
		         }
		         ImageView image=(ImageView) covertview.findViewById(R.id.good_image);
		         TextView name= (TextView) covertview.findViewById(R.id.good_name);
		         LinearLayout ll_order_goodsize=(LinearLayout) covertview.findViewById(R.id.ll_order_goodsize);
		         GoodList goodList = dataList.get(arg0);
		         ImageManager.Load(goodList.commodity_img_m, image);
		         name.setText(goodList.commodity_name);
		         ll_order_goodsize.removeAllViews();
		         addLayout(ll_order_goodsize,goodList);
				return covertview;
			}
	    	
	    }
		

		class MyGridAdapter extends BaseAdapter {
			@Override
			public int getCount() {
				return mPicList.size();
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
				ImageViewWithDel view = (ImageViewWithDel) arg1;
				final int position = arg0;
				if (view == null) {
					view = new ImageViewWithDel(ApplyforWqActivity.this);
				}
				if (mPicList.get(arg0).getPath() != null) {
					Bitmap bm = BitmapUtils.showimageFull(mPicList.get(arg0)
							.getPath(), 100, 100);
					view.setImage(bm, mPicList.get(arg0).getPath());
					view.setVisibility(View.VISIBLE);
					view.setCallback(new MyImageViewCallBack() {
						@Override
						public void imgClick() {

						}

						@Override
						public void del(View v) {
							mPicList.remove(position);
							mPathList.remove(position);
							gridAdapter.notifyDataSetChanged();
							if (mPicList.size() < 3
									&& !mPicList.contains(mAddPicture)) {
								mPicList.add(mAddPicture);
								Log.d(TAG, "deleted a origin picture");
							}
						}
					});
				} else {
					view.setImage(mPicList.get(arg0).getId());
					view.setVisibility(View.GONE);
					view.setCallback(new MyImageViewCallBack() {

						@Override
						public void imgClick() {
							showpopwindow();
						}

						@Override
						public void del(View v) {

						}
					});
				}

				return view;
			}

		}
		
}
