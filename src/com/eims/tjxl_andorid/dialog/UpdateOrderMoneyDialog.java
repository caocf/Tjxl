/**
 * 
 */
package com.eims.tjxl_andorid.dialog;

import loopj.android.http.RequestParams;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.dialog.UploadSuccessDialog.DialogClickListener;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.MD5;
import com.eims.tjxl_andorid.utils.TipToast;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 修改退款金额
 * @Author zd
 * @date 2015年7月16日  下午5:05:10
 *************************************************************************** 
 */
public class UpdateOrderMoneyDialog extends Dialog{

    private Context mContext;
    private TextView mreason;
    private EditText mMoney;
    private TextView title;
    private Button btn_cancel,btn_ok;
    private String reason;
    private String money;
	public UpdateOrderMoneyDialog(Context context,String reason) {
		super(context);
		this.reason=reason;
	   this.mContext=context;
	}
	public UpdateOrderMoneyDialog(Context context, int theme,String reason) {
		super(context, theme);
		 this.mContext=context;
			this.reason=reason;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.updateordermoney_layout);
		mreason=(TextView) findViewById(R.id.reason);
		mMoney=(EditText) findViewById(R.id.modfiy_money);
		title=(TextView) findViewById(R.id.tip_tv);
		btn_cancel=(Button) findViewById(R.id.btn_cacanel);
		btn_ok=(Button) findViewById(R.id.ok_btn);
		
		title.setText("修改退款金额");
		btn_cancel.setText("取消");
		btn_ok.setText("确定");
		mreason.setText(reason);
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		btn_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				money=mMoney.getText().toString();
				if(TextUtils.isEmpty(money)){
					TipToast.makeText(mContext, "请输入退款金额", 0).show();
					return;
				}
			    mClickListener.onModifyListener(money);
			}
		});
		
	}
	public OnModfiyGoodMoneyListener mClickListener;
	public  interface  OnModfiyGoodMoneyListener{
		void onModifyListener(String content);
	} 
	public void setClickModfiyListener(OnModfiyGoodMoneyListener mClickListener) {
		this.mClickListener = mClickListener;
	}
	


}
