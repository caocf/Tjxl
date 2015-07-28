/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.alipay.PayUtils;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.UserAdressBean.AdressBean;
import com.eims.tjxl_andorid.pay.wx.PayUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: zhifu
 * @Author zd
 * @date 2015年6月29日  下午3:23:56
 *************************************************************************** 
 */
public class PayMentActivity  extends BaseActivity{

	
	public static final String TAG = "PayMentActivity";
	
	public static final String INTENT_KEY_TOTAL_PRICE = "mtotalPrice";
	public static final String INTENT_KEY_ORDER_NUM = "order_no";
	public static final String INTENT_KEY_ADDRESS = "adress_bean";
	private HeadView headView;
	private TextView payMoney;
	private String order_price,order_num;
	private RadioGroup group;
	private LinearLayout ll_goPay;
	private RadioButton rb_hxzf,rb_ylzf,rb_wx,rb_zfb;
	private int type=0;
	private AdressBean bean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.payment_layout);
		findviews();
		setActionBar();
		Bundle extras = getIntent().getExtras();
		order_price = extras.getString("mtotalPrice");
		order_num=extras.getString("order_no");
		bean=(AdressBean) extras.getSerializable("adress_bean");
		payMoney.setText(order_price);
	   
		setListener();
	}
	private void findviews(){
		headView=(HeadView) findViewById(R.id.head);
		payMoney=(TextView) findViewById(R.id.pay_money);
		rb_hxzf=(RadioButton) findViewById(R.id.rb_hxzf);
		rb_ylzf=(RadioButton) findViewById(R.id.rb_ylzh);
		rb_zfb=(RadioButton) findViewById(R.id.rb_zhb);
		rb_wx = (RadioButton) findViewById(R.id.rb_wx);
		ll_goPay=(LinearLayout) findViewById(R.id.goPay);
		group=(RadioGroup) findViewById(R.id.group);
	}
	private void setActionBar() {
		// TODO Auto-generated method stub
		headView.setText("支付货款");
		headView.setGobackVisible();
		headView.setRightGone();
	}
	private  void  setListener(){
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				if(rb_hxzf.getId()==checkedId){
					//TipToast.makeText(mContext, "正在开发中..", 0).show();
					type=1;
				}else if(rb_ylzf.getId()==checkedId){
					//TipToast.makeText(mContext, "正在开发中..", 0).show();
					type=2;
				}else if(rb_zfb.getId()==checkedId){
					type=3;//支付宝支付
				}else if(rb_wx.getId()==checkedId){
					type = 4;//微信支付
				}
			}
		});
		
		ll_goPay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(type==0){
					TipToast.makeText(mContext, "请选择支付方式..", 0).show();
				}else if(type==3){
					payNow(order_num, "商品名称", "订单描述", order_price);
				}else if(type == 4){
//					TipToast.makeText(mContext, "正在开发中..", 0).show();
					payNowWx(order_num, "商品名称", "订单描述", order_price);
				}
			}
		});
	}
	
	private void payNowWx(String order_no,String order_name,String order_desc,String order_fee) {
		PayUtil payUtil = new PayUtil(PayMentActivity.this);
		payUtil.pay();
	}
	
	private void payNow(String order_no,String order_name,String order_desc,String order_fee){ 
//		AlipayUtil alipay = new AlipayUtil(this);
//		System.out.println("order_fee:"+order_fee);
//		alipay.alipayOrder(order_no, order_name, order_desc, order_fee);
		PayUtils payUtils=new PayUtils(PayMentActivity.this,"1");
		payUtils.pay();
	}
	//支付成功回调
	public void paymentSuccess() {
		Intent i = new Intent(this,PaySuccessActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		Bundle  bundle=new Bundle();
		bundle.putSerializable("bean", bean);
		bundle.putString("money", order_price);
		bundle.putString("order_no", order_num);
		i.putExtras(bundle);
		startActivity(i);
		finish(); 
	}
	
}
