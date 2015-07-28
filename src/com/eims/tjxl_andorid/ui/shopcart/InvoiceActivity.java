/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import org.json.JSONObject;

import loopj.android.http.RequestParams;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TableLayout;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 填写发票
 * @Author zd
 * @date 2015年6月29日  下午2:58:45
 *************************************************************************** 
 */
public class InvoiceActivity extends BaseActivity{
	
	private HeadView head;
	private RadioGroup radioGroup;
	private RadioButton rb_common,rb_valueadd;
	private LinearLayout ll_common;
	private TableLayout ll_valueadded;
	private EditText mInvoice_title,mCompany_name,mTaxNo,madress,mPhone,mBank,mBankNo;
	private Button btn_commint;
	private boolean  whoice=true;//true默认选中增值发票  false 选中普通发票
	private String invoiceType;
	private String invoice_title;
	private String companyName;
	private String taxno;
	private String adress;
	private String phone;
	private String bank;
	private String bankNo;
	private InvoiceInitBean initBean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wirte_invoice_layout);
		findviews();
		setActionBar();
		  initUI();
		initLoadInvoices();
	  
		
	}
	private  void  findviews(){
		head=(HeadView) findViewById(R.id.head);
		ll_common=(LinearLayout) findViewById(R.id.ll_common_layout);
		ll_valueadded=(TableLayout) findViewById(R.id.ll_valueadd_layout);
		mInvoice_title=(EditText) findViewById(R.id.Invoice_title);//发票抬头
		mCompany_name=(EditText) findViewById(R.id.company_name);
		mTaxNo=(EditText) findViewById(R.id.taxNo);//纳税人编号
		madress=(EditText) findViewById(R.id.adress);
		mPhone=(EditText) findViewById(R.id.phone);
		mBank=(EditText) findViewById(R.id.bank);
		mBankNo=(EditText) findViewById(R.id.bank_no);
		radioGroup=(RadioGroup) findViewById(R.id.invoice_group);
		rb_common=(RadioButton) findViewById(R.id.btn_common);
		rb_valueadd=(RadioButton) findViewById(R.id.btn_valueadd);
		btn_commint=(Button) findViewById(R.id.btn_commint_invoice);
	}
	private void setActionBar() {
		head.setText("发票信息");
		head.setGobackVisible();
		head.setRightGone();
		
	}
	private void initdata(){
		if(initBean!=null){
			if (!StringUtils.isEmpty(initBean.invoice_title)) {
				whoice=false;
				rb_common.setChecked(true);
			}else{
				whoice=true;
				rb_valueadd.setChecked(true);
			}
			mInvoice_title.setText(initBean.invoice_title);
			mCompany_name.setText(initBean.company_name);
			mTaxNo.setText(initBean.identification_number);
			madress.setText(initBean.registered_address);
			mPhone.setText(initBean.registered_phone);
			mBank.setText(initBean.bank);
			mBankNo.setText(initBean.account);
		}
	}
	private void initUI(){
		if(whoice){
			rb_valueadd.setChecked(true);
			ll_valueadded.setVisibility(View.VISIBLE);
			ll_common.setVisibility(View.GONE);
		
		}else{
			ll_valueadded.setVisibility(View.GONE);
			ll_common.setVisibility(View.VISIBLE);
		
		
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==R.id.btn_common){
					ll_valueadded.setVisibility(View.GONE);
					ll_common.setVisibility(View.VISIBLE);
					whoice=false;
				}else if(checkedId==R.id.btn_valueadd){
					ll_valueadded.setVisibility(View.VISIBLE);
					ll_common.setVisibility(View.GONE);
					whoice=true;
				}
			}
		});
		btn_commint.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(checkInfo()){
					commintInvoice();
				}
			}
		});
	}
	
	private boolean  checkInfo(){
		invoice_title = mInvoice_title.getText().toString();
		companyName = mCompany_name.getText().toString();
		taxno = mTaxNo.getText().toString();
		adress = madress.getText().toString();
		phone = mPhone.getText().toString();
		bank = mBank.getText().toString();
		bankNo = mBankNo.getText().toString();
		if(!whoice){
			if(TextUtils.isEmpty(invoice_title)){
				TipToast.makeText(mContext, "请填写发票抬头", 0).show();
				return false;
			}
		}else{
			if(TextUtils.isEmpty(companyName)){
				TipToast.makeText(mContext, "请填写公司名称", 0).show();
				return false;
			}
		}
		
		return true;
	}
	
	private void commintInvoice(){
		if(whoice){
			invoiceType = "2";
		}else{
			invoiceType = "1";
		}
		CustomResponseHandler mHandler=new CustomResponseHandler(mContext, true, "正在加载.."){
		@Override
		public void onSuccess(int statusCode, String content) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, content);
			LogUtil.d(TAG, content);
			try {
				JSONObject obj=new JSONObject(content);
				String type = obj.getString("type");
				if("1".equals(type)){
					String InvoiceId = obj.getString("data");
					Intent intent=new Intent();
					intent.putExtra("invoiceId", InvoiceId);
					LogUtil.d(TAG, InvoiceId);
					
					setResult(RESULT_OK, intent);
				    InvoiceActivity.this.finish();
				}else{
					TipToast.makeText(mContext, obj.getString("msg"), 0).show();
				}
				
			} catch (Exception e) {
			   e.printStackTrace();
			}
		}	
		};
		RequestParams params=new RequestParams();
		params.put("uid", user.id);
		params.put("invoices_type_value", invoiceType);
		if(whoice){
			params.put("tt2_name", "");
			params.put("tt1_company_name",companyName);
			params.put("tt1_identification_number",taxno);
			params.put("tt1_login_address",adress);
			params.put("tt1_telephone",phone);
			params.put("tt1_bank", bank);
			params.put("tt1_bank_account",bankNo);
			params.put("tt1_invoice", "");
		}else{
			params.put("tt2_name", invoice_title);
			params.put("tt1_company_name", "");
			params.put("tt1_identification_number", "");
			params.put("tt1_login_address", "");
			params.put("tt1_telephone", "");
			params.put("tt1_bank", "");
			params.put("tt1_bank_account","");
			params.put("tt1_invoice", "");
		}
		
	
		HttpClient.iAddInvoices(mHandler, params);
	}

	private void initLoadInvoices(){
		CustomResponseHandler mHandler=new CustomResponseHandler(mContext, false, ""){
		

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				LogUtil.d(TAG, content);
				try {
					JSONObject obj=new JSONObject(content);
					String data = obj.getString("data");
					if(data.length()>4){
						initBean = GsonUtils.json2bean(data, InvoiceInitBean.class);
						initdata();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		RequestParams params=new RequestParams();
		params.put("uid", user.id);
		HttpClient.iQueryInvoicesByuid(mHandler, params);
	}
	
	class InvoiceInitBean{
		public String invoice_title;//发票抬头
		public String account;
		public String bank;
		public String company_name;
		public String identification_number;
		public String registered_phone;
		public String registered_address;
	}
	
}
