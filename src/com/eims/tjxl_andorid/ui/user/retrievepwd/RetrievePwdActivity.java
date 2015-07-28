package com.eims.tjxl_andorid.ui.user.retrievepwd;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.IQueryLetterInfoBean;
import com.eims.tjxl_andorid.entity.IsExistUserNameBean;
import com.eims.tjxl_andorid.ui.user.LoginActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 * 找回密码 Created by kuangyong on 2015/7/1.
 */
public class RetrievePwdActivity extends BaseActivity {

	private HeadView headView;
	private IQueryLetterInfoBean bean;
	private HeadView head;
	private android.widget.TextView tvcircle1;// 1
	private android.widget.TextView tvdescrib1;// 输入账号
	private android.widget.TextView tvcircle2;// 2
	private android.widget.TextView tvdescrib2;// 验证身份
	private android.widget.TextView tvcircle3;// 3
	private android.widget.TextView tvdescrib3;// 重置密码
	private android.widget.TextView tvcircle4;// 4
	private android.widget.TextView tvdescrib4;// 重置成功
	private ValidateAccountFragment validateAccountFragment;//验证账号
	private ValidateUserFragment validateUserFragment;//验证账号
	private RetrievePassWordFragment retrievePassWordFragment;//重置密码
	private RetrieveTipLoginFragment retrieveTipLoginFragment;//提示登录

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.layout_retrieve_pwd);
		findview();
		initView();
	}

	private void setActionBar() {
		// TODO Auto-generated method stub
		headView.setText("找回密码");
		headView.setGobackVisible();
		headView.setRightGone();
	}

	private void findview() {
		headView = (HeadView) findViewById(R.id.head);
		this.tvdescrib4 = (TextView) findViewById(R.id.tv_describ4);
		this.tvcircle4 = (TextView) findViewById(R.id.tv_circle4);
		this.tvdescrib3 = (TextView) findViewById(R.id.tv_describ3);
		this.tvcircle3 = (TextView) findViewById(R.id.tv_circle3);
		this.tvdescrib2 = (TextView) findViewById(R.id.tv_describ2);
		this.tvcircle2 = (TextView) findViewById(R.id.tv_circle2);
		this.tvdescrib1 = (TextView) findViewById(R.id.tv_describ1);
		this.tvcircle1 = (TextView) findViewById(R.id.tv_circle1);
		this.head = (HeadView) findViewById(R.id.head);
	}

	private void initView() {
		setActionBar();
		validateAccount();
	}

	/**
	 * 验证账号
	 */
	private void validateAccount(){
		validateAccountFragment=ValidateAccountFragment.newInstance();
		validateAccountFragment.setOnFragmentResultListener(new ValidateAccountFragment.OnFragmentResultListener() {
			@Override
			public void onResult(IsExistUserNameBean bean) {
				if (null != bean.getData()) {
					validateUser(bean);
				}
			}
		});
		showFragment(validateAccountFragment, R.id.frame_retrieve_pwd);
		setStatus(1);
	}

	/**
	 * 验证用户
	 * @param bean
	 */
	private void validateUser(IsExistUserNameBean bean){
		validateUserFragment=ValidateUserFragment.newInstance(R.layout.fragment_validate_user,bean);
		validateUserFragment.setOnFragmentResultListener(new ValidateUserFragment.OnFragmentResultListener() {
			@Override
			public void onResult(String uid) {
				if(!TextUtils.isEmpty(uid)){
					retrievePwd(uid);
				}
			}
		});
		showFragment(validateUserFragment, R.id.frame_retrieve_pwd);
		Toast.makeText(mContext,"验证成功",Toast.LENGTH_SHORT).show();
		setStatus(2);
	}

	/**
	 * 重置密码
	 * @param uid
	 */
	private void retrievePwd(String uid){
		retrievePassWordFragment=RetrievePassWordFragment.newInstance(R.layout.fragment_retrieve_pwd,uid);
		retrievePassWordFragment.setOnFragmentResultListener(new RetrievePassWordFragment.OnFragmentResultListener() {
			@Override
			public void onResult() {
				retrieveTipLogin();
			}
		});
		showFragment(retrievePassWordFragment, R.id.frame_retrieve_pwd);
		Toast.makeText(mContext,"验证成功",Toast.LENGTH_SHORT).show();
		setStatus(3);
	}

	/**
	 * 提示登录
	 */
	private void retrieveTipLogin(){
		retrieveTipLoginFragment=RetrieveTipLoginFragment.newInstance(R.layout.fragment_validate_tip_login);
		retrieveTipLoginFragment.setOnFragmentResultListener(new RetrieveTipLoginFragment.OnFragmentResultListener() {
			@Override
			public void onResult() {
				goToLogin();
			}
		});
		showFragment(retrieveTipLoginFragment, R.id.frame_retrieve_pwd);
		setStatus(4);
	}

	/**
	 * 重置密码成功，登录
	 */
	private void goToLogin(){
		ActivitySwitch.openActivity(LoginActivity.class, null, this);
		this.finish();
	}

	/**
	 * 改变状态背景
	 * 
	 * @param status
	 */
	private void setStatus(int status) {
		switch (status) {
		case 1:
			setStatuColor(tvcircle1, R.drawable.circle_bg_red, tvdescrib1,
					getResources().getColor(R.color.red));
			setStatuColor(tvcircle2, R.drawable.circle_bg_black, tvdescrib2,
					getResources().getColor(R.color.black));
			setStatuColor(tvcircle3, R.drawable.circle_bg_black, tvdescrib3,
					getResources().getColor(R.color.black));
			setStatuColor(tvcircle4, R.drawable.circle_bg_black, tvdescrib4,
					getResources().getColor(R.color.black));
			break;
		case 2:
			setStatuColor(tvcircle1, R.drawable.circle_bg_red, tvdescrib1,
					getResources().getColor(R.color.red));
			setStatuColor(tvcircle2, R.drawable.circle_bg_red, tvdescrib2,
					getResources().getColor(R.color.red));
			setStatuColor(tvcircle3, R.drawable.circle_bg_black, tvdescrib3,
					getResources().getColor(R.color.black));
			setStatuColor(tvcircle4, R.drawable.circle_bg_black, tvdescrib4,
					getResources().getColor(R.color.black));
			break;
		case 3:
			setStatuColor(tvcircle1, R.drawable.circle_bg_red, tvdescrib1,
					getResources().getColor(R.color.red));
			setStatuColor(tvcircle2, R.drawable.circle_bg_red, tvdescrib2,
					getResources().getColor(R.color.red));
			setStatuColor(tvcircle3, R.drawable.circle_bg_red, tvdescrib3,
					getResources().getColor(R.color.red));
			setStatuColor(tvcircle4, R.drawable.circle_bg_black, tvdescrib4,
					getResources().getColor(R.color.black));
			break;
		case 4:
			setStatuColor(tvcircle1, R.drawable.circle_bg_red, tvdescrib1,
					getResources().getColor(R.color.red));
			setStatuColor(tvcircle2, R.drawable.circle_bg_red, tvdescrib2,
					getResources().getColor(R.color.red));
			setStatuColor(tvcircle3, R.drawable.circle_bg_red, tvdescrib3,
					getResources().getColor(R.color.red));
			setStatuColor(tvcircle4, R.drawable.circle_bg_red, tvdescrib4,
					getResources().getColor(R.color.red));
			break;
		}
	}

	private void setStatuColor(TextView circle, int drawable, TextView status,
			int color) {
		circle.setBackgroundResource(drawable);
		status.setTextColor(color);
	}
}
