package com.eims.tjxl_andorid.ui.shopcart.shorder;
import loopj.android.http.RequestParams;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.alipay.PayUtils;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.dialog.UpdateOrderMoneyDialog;
import com.eims.tjxl_andorid.dialog.UpdateOrderMoneyDialog.OnModfiyGoodMoneyListener;
import com.eims.tjxl_andorid.entity.OrderBean.OrderItemBean;
import com.eims.tjxl_andorid.entity.ReturnGoodBean.ReturnItemBean;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.shopcart.ApplyforWqActivity;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 退货列表  条目封装
 * @Author zd
 * @date 2015年7月1日  上午10:12:55
 *************************************************************************** 
 */
public class ReturnGoodItemView extends  LinearLayout{

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
    private Button tkxq;//退款详情
    private Button fh;//发货
    private Button sqwq;//申请维权
    private Button xgtkje;//修改退款金额
    private ReturnItemBean data;
    private String uid;
	private UpdateOrderMoneyDialog dialog;
    
	public ReturnGoodItemView(Context context) {
		super(context);
		this.mContext=context;
		LayoutInflater.from(context).inflate(R.layout.return_item_view, this, true);
		findView();
		setListener();
	}

	public ReturnGoodItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
		LayoutInflater.from(context).inflate(R.layout.return_item_view, this, true);
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
		tkxq=(Button) findViewById(R.id.tkxq);
		fh=(Button) findViewById(R.id.fh);
		sqwq=(Button) findViewById(R.id.sqwq);
		xgtkje=(Button) findViewById(R.id.xgtkje);
	}
	public void setUid(String uid){
		this.uid=uid;
	}
	
	public void setData(ReturnItemBean data){
		this.data = data;
		refreshItemView();
	}
	
	public void refreshItemView() {
	        order_item_shoename.setText(data.f_factory_name);
	     //   LogUtil.d(TAG, data.statu_name);
			order_Status.setText(data.statu_name);
			ImageManager.Load(data.commodity_img_m, order_item_shoeImages);
			order_no.setText(data.order_code);
			order_money.setText(data.total_price);
			order_returntime.setText(data.return_time0);
			order_item_numer.setText(data.quantity+"双");
		    updataButtom();
		
	}

	private void updataButtom() {
		// TODO Auto-generated method stub
	//	退货状态（0申请中，1卖家同意，2买家发货，卖3家确认收货，5卖家拒绝，6退款关闭，8已完成）
		qxsq.setVisibility(View.GONE);
		tkxq.setVisibility(View.GONE);
		fh.setVisibility(View.GONE);
		sqwq.setVisibility(View.GONE);
		xgtkje.setVisibility(View.GONE);
	    String  status=data.return_statu;
	    if("0".equals(status)){
	        	if("-1".equals(data.uygur_power_id)&&"1".equals(data.is_out)){//申请维权
	        		sqwq.setVisibility(View.VISIBLE);	
	        		tkxq.setVisibility(View.VISIBLE);
	        	}else{
	        		qxsq.setVisibility(View.VISIBLE);
	        		tkxq.setVisibility(View.VISIBLE);
	        	}
	    }else  if("1".equals(status)){
	    	fh.setVisibility(View.VISIBLE);
    		tkxq.setVisibility(View.VISIBLE);
	    }else  if("2".equals(status)){
	    	tkxq.setVisibility(View.VISIBLE);
	    }else  if("3".equals(status)){
	    	tkxq.setVisibility(View.VISIBLE);
	    }else  if("5".equals(status)){
	    	    if("1".equals(data.is_reject)){//修改退款金额
	    	    	xgtkje.setVisibility(View.VISIBLE);
	    	    }else if("-1".equals(data.uygur_power_id)&&"1".equals(data.rp)){
	    	    	sqwq.setVisibility(View.VISIBLE);	
	    	    }
	    	    tkxq.setVisibility(View.VISIBLE);
	    }else  if("6".equals(status)){
	    	sqwq.setVisibility(View.VISIBLE);	
	    }else  if("8".equals(status)){
	    	qxsq.setVisibility(View.GONE);
			tkxq.setVisibility(View.GONE);
			fh.setVisibility(View.GONE);
			sqwq.setVisibility(View.GONE);
			xgtkje.setVisibility(View.GONE);
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
				Bundle bundle=new Bundle();
				bundle.putString(ApplyforWqActivity.ORDER_ID, data.id);
				bundle.putString(ApplyforWqActivity.ORDER_CODE, data.order_code);
				bundle.putString(ApplyforWqActivity.ITEM_TAG, "1");
				ActivitySwitch.openActivity(ApplyforWqActivity.class, bundle, (Activity)mContext);
			}
		});
		fh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putInt(RefundDetailActivity.KEY_PAGE_TYPE, RefundDetailActivity.PAGE_TYPE_LOGISTICS);
				bundle.putString(RefundDetailActivity.KEY_RETURN_ID, data.id);
				ActivitySwitch.openActivity(RefundDetailActivity.class, bundle, (Activity)mContext);
			}
		});
		tkxq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putInt(RefundDetailActivity.KEY_PAGE_TYPE, RefundDetailActivity.PAGE_TYPE_DETAIL);
				bundle.putString(RefundDetailActivity.KEY_RETURN_ID, data.id);
				ActivitySwitch.openActivity(RefundDetailActivity.class, bundle, (Activity)mContext);				
			}
		});
		
		xgtkje.setOnClickListener(new OnClickListener() {		
	

			@Override
			public void onClick(View arg0) {
				dialog = new UpdateOrderMoneyDialog(mContext,R.style.loading_dialog, data.reject_desc);
				dialog.show();
				dialog.setClickModfiyListener(new OnModfiyGoodMoneyListener() {				
					@Override
					public void onModifyListener(String content) {
						// TODO Auto-generated method stub
						modfiyMoney(content);
					}
				});
			}
		});
	}
	
    /**取消申请
     * @param  **/
    private  void  CancelSq(){
    	CustomResponseHandler handler=new CustomResponseHandler(mContext, false, ""){
			@Override
    		public void onSuccess(int statusCode, String content) {
    			super.onSuccess(statusCode, content);
    			LogUtil.d(TAG, content);
                data.return_statu="6";
                data.statu_name="退款关闭";
                refreshItemView();
    		}
    	};
    	RequestParams params=new RequestParams();
    	params.put("uid", uid);
    	params.put("id", data.id);
    	params.put("status", data.return_statu);
    	HttpClient.CancelReturn(handler, params);
    }
    private void  modfiyMoney(String money){
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在修改...") {
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				//resloveJson(content);
				LogUtil.d(TAG, content);
			        data.return_statu="0";
	                data.statu_name="申请中";
	                refreshItemView();
				   dialog.dismiss();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("id", data.id);
		params.put("status", "10");
		params.put("total_price", money);
		HttpClient.uploadLogicticsInfo(mHandler, params);
    }
}
