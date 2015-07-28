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
 * @Description: 用户确认收货弹出框
 * @Author zd
 * @date 2015年7月16日  下午5:05:10
 *************************************************************************** 
 */
public class UserSHDialog extends Dialog{

    private Context mContext;
    private TextView uname;
    private EditText upassword;
    private TextView title;
    private Button btn_cancel,btn_ok;
    private String username;
    private String password;
	public UserSHDialog(Context context,String name) {
		super(context);
		this.username=name;
	   this.mContext=context;
	}
	public UserSHDialog(Context context, int theme,String name) {
		super(context, theme);
		 this.mContext=context;
			this.username=name;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usersh_dialog_layout);
		uname=(TextView) findViewById(R.id.username);
		upassword=(EditText) findViewById(R.id.password);
		title=(TextView) findViewById(R.id.tip_tv);
		btn_cancel=(Button) findViewById(R.id.btn_cacanel);
		btn_ok=(Button) findViewById(R.id.ok_btn);
		
		title.setText("确认收货");
		btn_cancel.setText("取消");
		btn_ok.setText("确定");
		uname.setText(username);
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
				password=upassword.getText().toString();
				if(TextUtils.isEmpty(password)){
					TipToast.makeText(mContext, "请输入密码", 0).show();
					return;
				}
				login();	
			}
		});
		
	}
	public OnSHClickListener mClickListener;
	public  interface  OnSHClickListener{
		void onCliclkSHListener(String content);
	} 
	public void setClickListener(OnSHClickListener mClickListener) {
		this.mClickListener = mClickListener;
	}
	
	private void  login(){

			CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
					true, "正在校验...") {
				@Override
				public void onSuccess(int statusCode, String content) {
					// TODO Auto-generated method stub
					super.onSuccess(statusCode, content);
					//resloveJson(content);
					LogUtil.d(TAG, content);
					mClickListener.onCliclkSHListener(content);
				}

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
				}
			};
			RequestParams params = new RequestParams();
			params.put("uname", username);
			params.put("pwd", MD5.md5(password));
			HttpClient.doLogin(mHandler, params);

	
	}

}
