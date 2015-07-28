/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import loopj.android.http.RequestParams;

import org.json.JSONObject;

import android.media.Image;
import android.os.Bundle;
import android.provider.Telephony.Sms.Conversations;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.WqDeatilBean;
import com.eims.tjxl_andorid.entity.AppyforWqBean.GoodColorList;
import com.eims.tjxl_andorid.entity.AppyforWqBean.GoodList;
import com.eims.tjxl_andorid.ui.product.ProductDeatil;
import com.eims.tjxl_andorid.ui.shopcart.WqOrderGoodListActivity.WqGoodList.GoodWq;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 维权清单
 * @Author zd
 * @date 2015年7月8日  上午11:12:22
 *************************************************************************** 
 */
public class WqOrderGoodListActivity  extends BaseActivity{
	private static final String TAG = "WqOrderGoodListActivity";
	private  HeadView head;
	private TextView orderNo;//订单编号
	private TextView afterSaleNo;//售后订单编号
	private TextView cjName;
	private TextView total_wqprice;
	private MyListView wqListView;
	private String wqId;
	private String thorderId;
	private List<GoodWq> dataList=new ArrayList<WqOrderGoodListActivity.WqGoodList.GoodWq>();
	private WqGoodList bean;
	private GoodAdapter mAdapter;
	private ImageView img1,img2,img3;
	private String[] allImgsUrl;
	private String keyValue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wqlisting_layout);
		findviews();
		initActionBar();
		loaddata();
	}
	private  void  findviews(){
		     head=(HeadView) findViewById(R.id.head);
		     orderNo=(TextView) findViewById(R.id.order_no);
		     afterSaleNo=(TextView) findViewById(R.id.aftersale_no);
		     cjName=(TextView) findViewById(R.id.cj_shoename);
		     total_wqprice=(TextView) findViewById(R.id.total_wqprice);
		     wqListView=(MyListView) findViewById(R.id.order_wqlist_listview);
		     img1=(ImageView) findViewById(R.id.img1);
		     img2=(ImageView) findViewById(R.id.img2);
		     img3=(ImageView) findViewById(R.id.img3);
		     if(getIntent().getExtras()!=null){
	    		  Bundle bundle = getIntent().getExtras();
	    		  wqId= bundle.getString("wqid");
	    		  thorderId=bundle.getString("thorderid");
	    		  keyValue = bundle.getString(WqOrderDeatilActivity.FROM_TYPE);
	    	  }
		     if(mAdapter==null){
		    	 mAdapter=new GoodAdapter();
		     }
		     wqListView.setAdapter(mAdapter);
	}
	  private void initActionBar() { 
	    	 head.setText("维权清单");
	 		 head.setRightGone();
	 	}
	  private void  showUI(){
		  if(bean!=null){
			  dataList.clear();
			  dataList.addAll(bean.data);
			  mAdapter.notifyDataSetChanged();
			  orderNo.setText(bean.map.from_order_code);
			  afterSaleNo.setText(bean.map.up_code);
			  cjName.setText(bean.map.f_factory_name);
			  total_wqprice.setText("小计金额：￥"+bean.map.total_price);
			   String imgs = bean.map.evidence_img;
               allImgsUrl = imgs.split(",");
               Log.d(TAG, allImgsUrl[0]);
               if(allImgsUrl.length==1){
            	   img1.setVisibility(View.VISIBLE);
            	   img2.setVisibility(View.INVISIBLE);
            	   img3.setVisibility(View.INVISIBLE);
            	   ImageManager.Load(URLs.IMAGE_URL+allImgsUrl[0],img1);
               }else if(allImgsUrl.length==2){
            	   img1.setVisibility(View.VISIBLE);
            	   img2.setVisibility(View.VISIBLE);
            	   img3.setVisibility(View.INVISIBLE);
            	   ImageManager.Load(URLs.IMAGE_URL+allImgsUrl[0],img1);
            	   ImageManager.Load(URLs.IMAGE_URL+allImgsUrl[1],img2);
               }else if(allImgsUrl.length==3){
            	   img1.setVisibility(View.VISIBLE);
            	   img2.setVisibility(View.VISIBLE);
            	   img3.setVisibility(View.VISIBLE);
            	   ImageManager.Load(URLs.IMAGE_URL+allImgsUrl[0],img1);
            	   ImageManager.Load(URLs.IMAGE_URL+allImgsUrl[1],img2);
            	   ImageManager.Load(URLs.IMAGE_URL+allImgsUrl[1],img3);
               }
		  }
	  }
	  private  void  loaddata(){
		  CustomResponseHandler  mHandler=new CustomResponseHandler(mContext, true, "正在加载..."){
		
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				LogUtil.d(TAG, content);
				try {
					JSONObject obj=new JSONObject(content);
					String type = obj.getString("type");
					if("1".equals(type)){
		               bean = GsonUtils.json2bean(content, WqGoodList.class);
		               showUI();
					}else{
						TipToast.makeText(mContext, obj.getString("msg"), 0).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				TipToast.makeText(mContext, "数据加载失败...",0).show();
			}
		  };
		  RequestParams params=new RequestParams();
		  params.put("uid", user.id);
		  params.put("id",  thorderId);//退货订单id
		  params.put("up_id", wqId);//维权表id
		  if(WqOrderDeatilActivity.THWQ.equals(keyValue)){
			  HttpClient.iQueryReturnDetailsCom(mHandler, params);
		  }else if(WqOrderDeatilActivity.HHWQ.equals(keyValue)){
			  HttpClient.iQueryReplaceDetailsCom(mHandler, params);
		  }else if(WqOrderDeatilActivity.DDWQ.equals(keyValue)){
			  
		  }
		  
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
		         RelativeLayout root=(RelativeLayout) covertview.findViewById(R.id.root);
		         final GoodWq goodList = dataList.get(arg0);
		         ImageManager.Load(goodList.commodity_img_m, image);
		         name.setText(goodList.commodity_name);
		         root.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Bundle bundle=new Bundle();
							bundle.putString(ProductDeatil.INTENT_KEY, goodList.commodity_id);
							ActivitySwitch.openActivity(ProductDeatil.class, bundle, WqOrderGoodListActivity.this);
							//OrderActivity.this.finish();
						}
					});
		         ll_order_goodsize.removeAllViews();
		         addLayout(ll_order_goodsize,goodList);
				return covertview;
			}
	    	
	    }
		   
	    private void  addLayout(LinearLayout ll_order_goodsize, GoodWq datas){
	    	for(com.eims.tjxl_andorid.ui.shopcart.WqOrderGoodListActivity.WqGoodList.GoodColorList temp : datas.goodList){
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
	  
	  public  class  WqGoodList{
		  
		  public List<GoodWq> data;
		  public Info map;
		  
		  public class GoodWq{
					public String commodity_id;
					public String commodity_img_m;
					public String commodity_name;
					public List<GoodColorList> goodList;
				}
				public class GoodColorList{
					public String commodity_price;
					public String quantity;
					public String spec_name_value;
				}
		  
		  
		  public  class Info{
			  public String evidence_img;//凭证
			  public String f_factory_name;
			  public String from_order_code;//关联订单编号
			  public  String total_price;
			  public  String  up_code;//关联售后编号
		  }
	  }

}
