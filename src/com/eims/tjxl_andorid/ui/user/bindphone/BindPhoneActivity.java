package com.eims.tjxl_andorid.ui.user.bindphone;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 * 绑定手机 Created by kuangyong on 2015/7/1
 */
public class BindPhoneActivity extends BaseActivity {

	public static final String IS_BIND = "is_bind";
	public static final String PHONE_NUMBER = "phone_number";
	private HeadView headView;
	private ValidateUserFragment validateUserFragment;			// 验证账号
	private BindPhoneFragment bindPhoneFragment;				// 绑定手机
	private boolean isBindPhone;								// 是否绑定手机
	private String phone;										// 手机号码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bind_phone);
		isBindPhone = getIntent().getBooleanExtra(IS_BIND, false);
		phone = getIntent().getStringExtra(PHONE_NUMBER);
		findview();
		initView();
	}

	private void setActionBar() {
		// TODO Auto-generated method stub
		String text="";
		if(isBindPhone){
			text="修改手机";
			validateUser();
		}else{
			text="绑定手机";
			bindPhone(AppContext.getInstance().getLocalUserInfo(mContext).id);
		}
		headView.setText(text);
		headView.setGobackVisible();
		headView.setRightGone();
	}

	private void findview() {
		headView = (HeadView) findViewById(R.id.head);
	}

	private void initView() {
		setActionBar();
	}


	/**
	 * 验证用户
	 */
	private void validateUser(){
		validateUserFragment=ValidateUserFragment.newInstance(R.layout.fragment_bindphone_user,phone);
		validateUserFragment.setOnFragmentResultListener(new ValidateUserFragment.OnFragmentResultListener() {
			@Override
			public void onResult(String uid) {
				if (!TextUtils.isEmpty(uid)) {
					Toast.makeText(mContext,"验证成功",Toast.LENGTH_SHORT).show();
					bindPhone(uid);
				}
			}
		});
		showFragment(validateUserFragment, R.id.frame_bind_phone);

	}

	/**
	 * 绑定手机
	 * @param uid
	 */
	private void bindPhone(String uid){
		bindPhoneFragment=BindPhoneFragment.newInstance(R.layout.fragment_bindp_hone,uid);
		bindPhoneFragment.setOnFragmentResultListener(new BindPhoneFragment.OnFragmentResultListener() {
			@Override
			public void onResult() {
				setResult(RESULT_OK);
				finish();
			}
		});
		showFragment(bindPhoneFragment, R.id.frame_bind_phone);
	}
}
