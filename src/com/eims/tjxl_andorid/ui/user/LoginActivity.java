/**
 * 
 */
package com.eims.tjxl_andorid.ui.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.app.Preferences;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.ui.MainActivity;
import com.eims.tjxl_andorid.ui.user.complain.FillInComplainActivity;
import com.eims.tjxl_andorid.ui.user.retrievepwd.RetrievePwdActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;

import org.json.JSONArray;
import org.json.JSONObject;

import loopj.android.http.RequestParams;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年4月27日 下午5:19:23
 *************************************************************************** 
 */
public class LoginActivity extends BaseActivity {

	private HeadView head;
	private EditText mUsername, mUserPwd;
	private LinearLayout ll_login_btn;
	private String uname;
	private String upwd;
	private String check_status;
	private String id;
	private String member_type;
	private String user_status;
	private String username;
	// 登录类型（正常方式登录，从商品详情或其他界面跳转登录）
	public static final String LOGIN_TYPE_KEY = "Login_type_key";
	public static final int LOGIN_TYPE_VALUE_DETAIL = 0X1;// 其他页面登录进入
	public static final int LOGIN_TYPE_VALUE_CUSTOR = 0X0;
	private int logintype=LOGIN_TYPE_VALUE_CUSTOR;
	private TextView btn_retrieve_pwd;// 忘记密码
	private TextView btn_complain;// 账号申诉
	private CheckBox login_check_box;// 记住密码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		findviews();
		initdata();
		initActionBar();
		setListener();
	}

	private void findviews() {
		head = (HeadView) findViewById(R.id.head);
		mUsername = (EditText) findViewById(R.id.username);
		mUserPwd = (EditText) findViewById(R.id.user_password);
		ll_login_btn = (LinearLayout) findViewById(R.id.login_btn);
		btn_retrieve_pwd = (TextView) findViewById(R.id.btn_retrieve_pwd);
		btn_complain = (TextView) findViewById(R.id.btn_complain);
		login_check_box = (CheckBox) findViewById(R.id.login_check_box);
	}

	private void initdata() {
		User mUserInfo = AppContext.getLocalUserInfo(getBaseContext());
		if (mUserInfo != null) {
			mUsername.setText(mUserInfo.username);
		}
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			logintype = bundle.getInt(LOGIN_TYPE_KEY);
		}
	}

	private void initActionBar() {
		head.setText("登录");
		head.setRightGone();
		head.showRightText("注册", new RightBtnClick());
	}

	private void setListener() {
		btn_retrieve_pwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivitySwitch.openActivity(RetrievePwdActivity.class, null,
						LoginActivity.this);
			}
		});
		btn_complain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ActivitySwitch.openActivity(FillInComplainActivity.class, null,
						LoginActivity.this);
			}
		});
		ll_login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (checkinfo()) {
					doLogin();
				}
			}
		});
		login_check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				Preferences.putBoolean(mContext, Preferences.Key.IS_RECORD_PWD, b);//是否记住密码
				if (!b) {
					Preferences.remove(mContext, Preferences.Key.USER_NAME);
					Preferences.remove(mContext, Preferences.Key.USER_PWD);
				}
			}
		});
		boolean isRecordPwd=Preferences.getBoolean(mContext,Preferences.Key.IS_RECORD_PWD,false);
		login_check_box.setChecked(isRecordPwd);
		if(isRecordPwd){
			uname=Preferences.getString(mContext,Preferences.Key.USER_NAME,"");
			upwd=Preferences.getString(mContext,Preferences.Key.USER_PWD,"");
			mUsername.setText(uname);
			mUserPwd.setText(upwd.trim());
		}
	}

	private boolean checkinfo() {
//		boolean isRecordPwd=Preferences.getBoolean(mContext,Preferences.Key.IS_RECORD_PWD,false);
//		if(isRecordPwd){
//			uname=Preferences.getString(mContext,Preferences.Key.USER_NAME,"");
//			upwd=Preferences.getString(mContext,Preferences.Key.USER_PWD,"");
//
//
//		}else{
			uname = mUsername.getText().toString();
			upwd = mUserPwd.getText().toString();
//		}
		if (TextUtils.isEmpty(uname)) {
			TipToast.makeText(mContext, "用户名不能为空", 0).show();
			return false;
		}
		if (TextUtils.isEmpty(upwd)) {
			TipToast.makeText(mContext, "密码不能为空", 0).show();
			return false;
		}

		return true;
	}

	private void doLogin() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在登录...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				resloveJson(content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
			}
		};
		RequestParams params = new RequestParams();
		params.put("uname", uname);
		params.put("pwd", md5(upwd));
		HttpClient.doLogin(mHandler, params);

	}

	private void resloveJson(String content) {
		try {
			JSONObject json = new JSONObject(content);
			String type = json.getString("type");
			String msg = json.getString("msg");
			if ("1".equals(type)) {
				JSONArray arr = new JSONArray(msg);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject temp = (JSONObject) arr.get(i);
					check_status = temp.getString("check_status");
					id = temp.getString("id");
					member_type = temp.getString("member_type");
					user_status = temp.getString("user_status");
					username = temp.getString("username");
				}
				User userInfo = new User();
				userInfo.check_status = check_status;
				userInfo.id = id;
				userInfo.member_type = member_type;
				userInfo.user_status = user_status;
				userInfo.username = username;
				userInfo.password = upwd.trim();
				AppContext.isLogin = true;
				AppContext.userInfo = userInfo;
				Preferences.putString(mContext, Preferences.Key.USER_PWD, upwd.trim());//密码
				Preferences.putString(mContext, Preferences.Key.USER_NAME, username.trim());//用户名
				AppContext.saveUserInfo(getBaseContext(), userInfo);
				LogUtil.d("zd", userInfo.check_status+"====="+check_status);
				LoginActivity.this.finish();
				swithUI(logintype);
			} else {
				mUserPwd.setText("");
				TipToast.makeText(mContext, msg, 0).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void swithUI(int type) {
		if (type == LOGIN_TYPE_VALUE_CUSTOR) {//默认情况下
			// 切换到main
			AppManager.getAppManager().finishToHome();
			MainActivity.radioGroup.getChildAt(3).performClick();
			MainActivity.mainPager.setCurrentItem(3);
		} else if (LOGIN_TYPE_VALUE_DETAIL == type) {
	       LogUtil.d("zd", LOGIN_TYPE_KEY);
			LoginActivity.this.finish();
		}

	}

	class RightBtnClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ActivitySwitch.openActivity(RegisterActivity.class, null,
					LoginActivity.this);
		}

	}

}
