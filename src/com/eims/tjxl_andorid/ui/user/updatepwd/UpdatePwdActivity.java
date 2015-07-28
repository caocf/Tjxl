package com.eims.tjxl_andorid.ui.user.updatepwd;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.IQueryUserStoreInfo;
import com.eims.tjxl_andorid.entity.IsExistUserNameBean;
import com.eims.tjxl_andorid.entity.ValidateWayBean;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 * 找回密码 Created by kuangyong on 2015/7/1.
 */
public class UpdatePwdActivity extends BaseActivity {

	public static final String STORE_INFO="store_info";
	private IQueryUserStoreInfo storeInfo;
	private HeadView headView;
	private ValidateUserFragment validateUserFragment;//验证账号
	private RetrievePassWordFragment retrievePassWordFragment;//重置密码

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.layout_update_pwd);
		storeInfo=(IQueryUserStoreInfo)getIntent().getSerializableExtra(STORE_INFO);
		findview();
		initView();
	}

	private void setActionBar() {
		// TODO Auto-generated method stub
		headView.setText("修改密码");
		headView.setGobackVisible();
		headView.setRightGone();
	}

	private void findview() {
		headView = (HeadView) findViewById(R.id.head);
	}

	private void initView() {
		setActionBar();
		validateUser();
	}


	/**
	 * 验证用户
	 */
	private void validateUser(){
		IsExistUserNameBean bean=new IsExistUserNameBean();
		ValidateWayBean data=new ValidateWayBean();
		if(null!=storeInfo&&null!=storeInfo.getData()){
			data.setPhone(storeInfo.getData().getPhone());
			data.setEmail(storeInfo.getData().getEmail());
			data.setIs_verify_phone(storeInfo.getData().getIs_verify_phone());
			data.setIs_verify_email(storeInfo.getData().getIs_verify_email());
		}
		bean.setData(data);
		validateUserFragment=ValidateUserFragment.newInstance(R.layout.fragment_update_user,bean);
		validateUserFragment.setOnFragmentResultListener(new ValidateUserFragment.OnFragmentResultListener() {
			@Override
			public void onResult(String uid) {
				if (!TextUtils.isEmpty(uid)) {
					retrievePwd(uid);
				}
			}
		});
		showFragment(validateUserFragment, R.id.frame_retrieve_pwd);

	}

	/**
	 * 重置密码
	 * @param uid
	 */
	private void retrievePwd(String uid){
		retrievePassWordFragment=RetrievePassWordFragment.newInstance(R.layout.fragment_update_pwd,uid);
		retrievePassWordFragment.setOnFragmentResultListener(new RetrievePassWordFragment.OnFragmentResultListener() {
			@Override
			public void onResult() {
				finish();
			}
		});
		showFragment(retrievePassWordFragment, R.id.frame_retrieve_pwd);
		Toast.makeText(mContext,"验证成功",Toast.LENGTH_SHORT).show();
	}
}
