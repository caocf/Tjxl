package com.eims.tjxl_andorid.ui.shopcart.shorder;
import loopj.android.http.RequestParams;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.alipay.PayUtils;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.entity.OrderBean.OrderItemBean;
import com.eims.tjxl_andorid.entity.ReplaceOrderBean.ReplaceItemBean;
import com.eims.tjxl_andorid.entity.ReturnGoodBean.ReturnItemBean;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.shopcart.ApplyforWqActivity;
import com.eims.tjxl_andorid.ui.shopcart.ExchangeProductDetailActivity;
import com.eims.tjxl_andorid.ui.shopcart.RefundDetailActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.utils.imageupload.TipsToast;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 换货列表  条目封装
 * @Author zd
 * @date 2015年7月1日  上午10:12:55
 *************************************************************************** 
 */
public class ReplaceOrderItemView extends  LinearLayout{

    private static final String TAG = "ReturnGoodItemView";
	private Context mContext;
    private TextView order_item_shoename;//店铺名称
    private TextView order_Status;//订单状态
    private  ImageView order_item_shoeImages;//图
    private  TextView order_no;//订单编号
    private TextView order_money;//订单金额
    private   TextView order_returntime;//退货时间
    private  TextView order_item_numer;//商品数量
    private Button qxsq;//取消申请
    private Button hhxq;//换货详情
    private Button wyfh;//我要发货
    private Button sqwq;//申请维权
    private Button zfce;//支付差额
    private Button qrsh;//确认收货
    private ReplaceItemBean data;
    private String uid;
    
    
	public ReplaceOrderItemView(Context context) {
		super(context);
		this.mContext=context;
		LayoutInflater.from(context).inflate(R.layout.replace_item_view, this, true);
		findView();
		setListener();
	}

	public ReplaceOrderItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
		LayoutInflater.from(context).inflate(R.layout.replace_item_view, this, true);
		findView();
		setListener();
	}
	
	
	
	private void findView() {
		order_item_shoename=(TextView)findViewById(R.id.order_item_shoename);
		order_Status=(TextView) findViewById(R.id.order_return_item_status);
		order_item_shoeImages=(ImageView) findViewById(R.id.order_shoeImages);
		order_no=(TextView) findViewById(R.id.order_item_no);
		order_money=(TextView) findViewById(R.id.order_item_money);
		order_returntime=(TextView) findViewById(R.id.order_item_addtime);
		order_item_numer=(TextView) findViewById(R.id.order_item_numner);
		qxsq=(Button) findViewById(R.id.qxsq);
		hhxq=(Button) findViewById(R.id.hhxq);
		wyfh=(Button) findViewById(R.id.wyfh);
		sqwq=(Button) findViewById(R.id.sqwq);
		zfce=(Button) findViewById(R.id.zfce);
		qrsh=(Button) findViewById(R.id.qrsh);
	}
	public void setUid(String uid){
		this.uid=uid;
	}
	
	public void setData(ReplaceItemBean data){
		this.data = data;
		refreshItemView();
	}
	
	public void refreshItemView() {
	        order_item_shoename.setText(data.f_factory_home);
	     //   LogUtil.d(TAG, data.statu_name);
			order_Status.setText(data.statu_name);
			ImageManager.Load(data.commodity_img_m, order_item_shoeImages);
			order_no.setText(data.replace_code);
			order_money.setText(data.total_price);
			order_returntime.setText(data.repace_time0);
			order_item_numer.setText(data.quantity+"双");
		    updataButtom();
		
	}

	private void updataButtom() {
		// TODO Auto-generated method stub
	//	售后(换货)状态(0申请售后  1卖家同意 2支付差额 3买家发货 4卖家确认收货 5卖家发货 6买家确认收货(换货完成)) 
		//7卖家已拒绝 8买家已取消  9 申请超时  10维权处理',
		qxsq.setVisibility(View.GONE);
		hhxq.setVisibility(View.GONE);
		wyfh.setVisibility(View.GONE);
		sqwq.setVisibility(View.GONE);
		zfce.setVisibility(View.GONE);
		qrsh.setVisibility(View.GONE);
	    String  status=data.replace_statu;
        if("0".equals(status)){
        	if("0".equals(status) && "1".equals(data.is_out) && "-1".equals(data.uygur_power_id)){
        		sqwq.setVisibility(View.VISIBLE);
        	}else{
        		qxsq.setVisibility(View.VISIBLE);
        	}
        	hhxq.setVisibility(View.VISIBLE);
        }else if("1".equals(status)){
        	wyfh.setVisibility(View.VISIBLE);
        	zfce.setVisibility(View.VISIBLE);
        }else if("2".equals(status)){
        	qxsq.setVisibility(View.VISIBLE);
        	hhxq.setVisibility(View.VISIBLE);
        }else if("3".equals(status)){
        	hhxq.setVisibility(View.VISIBLE);
        }else if("4".equals(status)){
        	hhxq.setVisibility(View.VISIBLE);
        }else if("5".equals(status)){
        	qrsh.setVisibility(View.VISIBLE);
        	hhxq.setVisibility(View.VISIBLE);
        }else if("6".equals(status)){
        	hhxq.setVisibility(View.VISIBLE);
        }else if("7".equals(status)){//replace_statu=7 and hp=0 and uygur_power_id=-1申请维权
        	if("7".equals(status)&&"0".equals(data.hp)&&"-1".equals(data.uygur_power_id)){
        		sqwq.setVisibility(View.VISIBLE);
        	}
        	hhxq.setVisibility(View.VISIBLE);
        }else if("8".equals(status)){
        	hhxq.setVisibility(View.VISIBLE);
        }else if("9".equals(status)){
        	hhxq.setVisibility(View.VISIBLE);
        }else if("10".equals(status)){
        	hhxq.setVisibility(View.VISIBLE);
        }
  
	    
	}
	
	private  void  setListener(){
        
		 qxsq.setOnClickListener(new  OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				CancelSq();
			}
		});
		 sqwq.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Bundle bundle=new Bundle();
				bundle.putString(ApplyforWqActivity.ORDER_ID, data.id);
				bundle.putString(ApplyforWqActivity.ORDER_CODE, data.replace_code);
				bundle.putString(ApplyforWqActivity.ITEM_TAG, "2");
				ActivitySwitch.openActivity(ApplyforWqActivity.class, bundle, (Activity)mContext);
			}
		});
		 hhxq.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putInt(ExchangeProductDetailActivity.KEY_PAGE_TYPE, ExchangeProductDetailActivity.PAGE_TYPE_DETAIL);
				bundle.putString(ExchangeProductDetailActivity.KEY_RETURN_ID, data.id);
				ActivitySwitch.openActivity(ExchangeProductDetailActivity.class, bundle, (Activity)mContext);				
			}
		});
		 
		 wyfh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putInt(ExchangeProductDetailActivity.KEY_PAGE_TYPE, ExchangeProductDetailActivity.PAGE_TYPE_LOGISTICS);
				bundle.putString(ExchangeProductDetailActivity.KEY_RETURN_ID, data.id);
				ActivitySwitch.openActivity(ExchangeProductDetailActivity.class, bundle, (Activity)mContext);				
			}
		});
		
	}
	
    /**取消申请
     * @param  **/
    private  void  CancelSq(){
    	CustomResponseHandler handler=new CustomResponseHandler(mContext, false, ""){
			@Override
    		public void onSuccess(int statusCode, String content) {
    			// TODO Auto-generated method stub
    			super.onSuccess(statusCode, content);
    			LogUtil.d("zd", content);
                data.replace_statu="8";
                data.statu_name="换货关闭";
                refreshItemView();
    		}
    	};
    	RequestParams params=new RequestParams();
    	params.put("uid", uid);
    	params.put("id", data.id);
        params.put("status","8");
    	HttpClient.CancelReplace_GoodsSQ(handler, params);
    }
    

	

	

}
