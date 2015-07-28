/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import java.io.Serializable;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.UserAdressBean.AdressBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.imageupload.TipsToast;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月29日  下午5:27:48
 *************************************************************************** 
 */
public class PaySuccessActivity extends  BaseActivity{
	
	private TextView payPrice;
	private TextView user_adress;
	private TextView order_number;
	private AdressBean bean;
	private HeadView headView;
	private String money,order_no;
	private Button btn_OrderDetail,btn_orderList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_success_layout);
		findviews();
		setActionBar();
		Bundle bundle = getIntent().getExtras();
		bean = (AdressBean) bundle.getSerializable("bean");
		money = bundle.getString("money");
		order_no = bundle.getString("order_no");
		initdata();
		setListener();
	}
	
	private  void  findviews(){
		headView=(HeadView) findViewById(R.id.head);
		payPrice=(TextView) findViewById(R.id.pay_money);
		user_adress=(TextView) findViewById(R.id.user_consgine_adress);
		order_number=(TextView) findViewById(R.id.order_number);
		btn_OrderDetail=(Button) findViewById(R.id.btn_order_detail);
		btn_orderList=(Button) findViewById(R.id.btn_order_list);
	}
	private void setActionBar() {
		// TODO Auto-generated method stub
		headView.setText("支付货款");
		headView.setGobackVisible();
		headView.setRightGone();
	}
	
	private  void  initdata(){
		payPrice.setText(money);
		order_number.setText("订单编号:"+order_no);
		if(bean!=null){
			user_adress.setText(bean.address_show+bean.address+"      "+bean.consignee_name+"（收）"+bean.consignee_phone);
		}
	}
	private  void  setListener(){
		btn_OrderDetail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TipsToast.makeText(mContext, order_no, 0).show();
				Bundle bundle=new Bundle();
				bundle.putString("order_id", order_no);
				ActivitySwitch.openActivity(OrderDetailActivity.class, bundle, PaySuccessActivity.this);
			}
		});
		btn_orderList.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ActivitySwitch.openActivity(OrderListActivity.class, null, PaySuccessActivity.this);
			}
		});
		
	}

}
