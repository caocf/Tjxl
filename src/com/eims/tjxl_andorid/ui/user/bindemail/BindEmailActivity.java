package com.eims.tjxl_andorid.ui.user.bindemail;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.ui.user.bindphone.BindPhoneFragment;
import com.eims.tjxl_andorid.ui.user.bindphone.ValidateUserFragment;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 * 绑定邮箱 Created by kuangyong on 2015/7/1
 */
public class BindEmailActivity extends BaseActivity {

	public static final String IS_BIND = "is_bind";
	public static final String EMAIL_NUMBER = "email_number";
	private HeadView headView;
	private ValidateUserFragment validateUserFragment;			// 验证账号
	private BindEmailFragment bindEmailFragment;				// 绑定邮箱
	private boolean isBindEmail;								// 是否绑定邮箱
	private String email;										// 邮箱号码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bind_phone);
		isBindEmail = getIntent().getBooleanExtra(IS_BIND, false);
		email = getIntent().getStringExtra(EMAIL_NUMBER);
		findview();
		initView();
	}

	private void setActionBar() {
		// TODO Auto-generated method stub
		String text="";
		if(isBindEmail){
			text="修改邮箱";
			validateUser();
		}else{
			text="绑定邮箱";
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
		validateUserFragment=ValidateUserFragment.newInstance(R.layout.fragment_bindphone_user,email);
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
	 * 绑定邮箱
	 * @param uid
	 */
	private void bindPhone(String uid){
		bindEmailFragment=BindEmailFragment.newInstance(R.layout.fragment_bindp_email,uid);
		bindEmailFragment.setOnFragmentResultListener(new BindEmailFragment.OnFragmentResultListener() {
			@Override
			public void onResult() {
				setResult(RESULT_OK);
				finish();
			}
		});
		showFragment(bindEmailFragment, R.id.frame_bind_phone);
	}
}
