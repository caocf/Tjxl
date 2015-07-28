package com.eims.tjxl_andorid.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import loopj.android.http.RequestParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.app.Preferences;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.ui.user.LoginActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;

/**
 * 
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 欢迎界面
 * @Author zd
 *************************************************************************** 
 */
public class WelcomeActivity extends BaseActivity {

	private boolean isFirstLaunch = true;
	private String upwd;
	private String check_status;
	private String id;
	private String member_type;
	private String user_status;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view=View.inflate(mContext, R.layout.welcome, null);
		setContentView(view);
		// 判断App是否是第一次打开：
		if (AppContext.isFirstLaunchApp(this)) {
			isFirstLaunch = true;
			loadGudieImage();
		} else {
			// 进入主页
			isFirstLaunch = false;
		}
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (isFirstLaunch) {
					// 进入引导页
//					Bundle bundle = new Bundle();
//					bundle.putIntArray("Images", IImages);
					ActivitySwitch.openActivity(GuideActivity.class, null,WelcomeActivity.this);
					finish();
				} else {
					// 登录成功进入主页
					final User info=AppContext.getLocalUserInfo(WelcomeActivity.this);
					if (info!=null&&!AppContext.appStatu.equals(AppContext.exit)){
						AppContext.isLogin = true;
						AppContext.userInfo = info;
						autoLogin(info);
					}
					ActivitySwitch.openActivity(MainActivity.class, null,WelcomeActivity.this);
					finish();
				}
			}
		}, 3000);
		
    animation(view);
	

	}
	


	/**
	 * 自动登录
	 * @param info 
	 * 
	 */
	protected void autoLogin(final User info) {
		CustomResponseHandler  mHandler=new CustomResponseHandler(mContext, false,""){
	         
          	@Override
          	public void onSuccess(int statusCode, String content) {
          		// TODO Auto-generated method stub
          		super.onSuccess(statusCode, content);
                    resloveJson(content,info.password);
          	        LogUtil.d(TAG, statusCode+"登录状态....");
          	}
          	@Override
          	public void onFailure(Throwable error, String content) {
             	super.onFailure(error, content);
          	}
     };
		RequestParams  params=new RequestParams();
		params.put("uname", info.username);
	    params.put("pwd",md5(info.password));

		HttpClient.doLogin(mHandler, params);
	}

	/**
	 * 获取引导页图片
	 */
	private void loadGudieImage() {
		// TODO Auto-generated method stub

	}
	
	private void animation(View view) {
		
		// 渐变展示启动屏
				AlphaAnimation animation = new AlphaAnimation(0.8f, 1.0f);
				animation.setDuration(3000);
				view.startAnimation(animation);
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation arg0) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationStart(Animation animation) {
					}

				});
		
	}
	
	private void resloveJson(String content, String password) {
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
				userInfo.password =password;
				AppContext.isLogin = true;
				AppContext.userInfo = userInfo;
				Preferences.putString(mContext, Preferences.Key.USER_PWD, password);//密码
				Preferences.putString(mContext, Preferences.Key.USER_NAME, username);//用户名
				AppContext.saveUserInfo(getBaseContext(), userInfo);
				LogUtil.d("zd", userInfo.check_status+"====="+check_status);

			} else {
			     LogUtil.d("zd", msg);
				//TipToast.makeText(mContext, msg, 0).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
