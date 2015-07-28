package com.eims.tjxl_andorid.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.app.Preferences;
import com.eims.tjxl_andorid.entity.IQueryUserStoreInfo;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.google.gson.Gson;

/**
 * 账户信息个人资料 Created by kuangyong on 2015/6/24.
 */
public class UserInfoActivity extends BaseActivity implements
		View.OnClickListener {
	private UserInfoEditFragment userInfoEditFragment;//编辑信息
	private UserInfoShowFragment userInfoShowFragment;//展示信息
	private HeadView head;
	private TextView btn_commit;// 底部按钮
	private boolean isExit = true;// 是否是退出登录

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_layout);
		findViews();
		setlistener();
		initheadview();
		init();
		userInfoShowFragment=UserInfoShowFragment.newInstance(AppContext.getInstance().getLocalUserInfo(mContext).id);
		userInfoShowFragment.setOnLoadSuccessInfoResultListener(new UserInfoShowFragment.OnLoadSuccessInfoResultListener() {
			@Override
			public void callBackResult() {
				head.showRightText("编辑资料", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						editUserInfo();
					}
				});
			}
		});
		showFragment(userInfoShowFragment);
	}

	private void setlistener() {
		btn_commit.setOnClickListener(this);
	}

	private void findViews() {
		this.head = (HeadView) findViewById(R.id.head);
		this.btn_commit = (TextView) findViewById(R.id.btn_commit);
	}

	private void initheadview() {
		head.setText("账户信息");
		head.setGobackVisible();
		head.setRightResource();
		head.setRightGone();
		head.showRightText("编辑资料", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				editUserInfo();
			}
		});
	}

	/**
	 * 编辑账户信息
	 */
	private void editUserInfo() {
		IQueryUserStoreInfo user=getLocalUserInfo();
		if(null!=user){
			userInfoEditFragment = UserInfoEditFragment.newInstance(user.getData());
			userInfoEditFragment.setOnSavaUserInfoResultListener(new UserInfoEditFragment.OnSavaUserInfoResultListener() {
				@Override
				public void callBackResult(int result_code) {
					String msg="";
					if(result_code==UserInfoEditFragment.RSULTOK){//保存成功
						isExit = true;
						btn_commit.setText("退出登录");
						userInfoShowFragment=UserInfoShowFragment.newInstance(AppContext.getLocalUserInfo(mContext).id);
						userInfoShowFragment.setOnLoadSuccessInfoResultListener(new UserInfoShowFragment.OnLoadSuccessInfoResultListener() {
							@Override
							public void callBackResult() {
								head.showRightText("编辑资料", new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										editUserInfo();
									}
								});
							}
						});
						showFragment(userInfoShowFragment);
						msg="修改成功";
					}else{
						msg="亲，网络不给力啊，请稍后再试...";
					}
					Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
				}
			});
			showFragment(userInfoEditFragment);
			isExit = !isExit;
			init();
			head.GoneRightText("编辑资料", new View.OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
		}else{
			Toast.makeText(mContext,"网络开小差了，请重新刷新页面试试...",Toast.LENGTH_LONG).show();
		}
	}

	private void init() {
		if (isExit) {
			btn_commit.setText("退出登录");
		}else{
			btn_commit.setText("保存信息");
		}
	}

	/**
	 * 获取本地个人资料信息
	 * @return
	 */
	private IQueryUserStoreInfo getLocalUserInfo(){
		String json=Preferences.getString(mContext,Preferences.Key.USER_STORE_INFO);

		if(!StringUtils.isEmpty(json)){
			return new Gson().fromJson(json,IQueryUserStoreInfo.class);
		}else{
			return null;
		}
	}

	/** 当前显示的fragment **/
	private Fragment currentFragment = null;

	/**
	 * 显示framgnet
	 * 
	 * @param fragment
	 */
	private void showFragment(Fragment fragment) {
		// 非空
		if (null == fragment) {
			return;
		}
		// 已经显示
		if (fragment.isAdded() && !fragment.isHidden()) {
			return;
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transation = fragmentManager.beginTransaction();
		// 隐藏当前显示的fragment
		if (null != currentFragment && currentFragment.isAdded()) {
			transation.hide(currentFragment);
		}
		// 显示传入的fragment
		if (!fragment.isAdded()) {
			transation.add(R.id.frame_user_info, fragment);
			transation.commit();
		} else {
			transation.show(fragment);
			transation.commit();
		}
		// 记住当前的fragment
		currentFragment = fragment;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		currentFragment.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_commit:// 底部按钮
			if (isExit) {// 退出登录
				exitLogin();
			} else {// 保存信息
				saveUserInfo();
			}
			break;
		}
	}

	/**
	 * 退出登录
	 */
	private void exitLogin(){
		AppContext.saveUserNull(this);
		AppContext.isLogin = false;
		AppContext.userInfo = null;
		ActivitySwitch.openActivity(LoginActivity.class, null,this);
		this.finish();
	}

	/**
	 * 保存账户信息
	 */
	private void saveUserInfo(){
		if(null!=userInfoEditFragment){
			userInfoEditFragment.saveUserInfo();
		}
	}
}
