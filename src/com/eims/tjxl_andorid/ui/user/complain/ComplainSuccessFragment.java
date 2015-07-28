package com.eims.tjxl_andorid.ui.user.complain;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.base.BaseFragment;
import com.eims.tjxl_andorid.entity.ComplainReturnBean;
import com.eims.tjxl_andorid.ui.user.LoginActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;

/**
 * 申诉成功 Created by kuangyong on 2015/6/24.
 */
public class ComplainSuccessFragment extends BaseFragment implements
		OnClickListener {
	private static final String TAG = "ComplainEditFragment";
	public static final String RETURN_BEAN="return_bean";
	private ComplainReturnBean returnBean;
	private android.widget.TextView tvconplaincode;
	private android.widget.TextView tvusername;
	private android.widget.TextView tvuserpwd;
	private android.widget.TextView btnlogin;

	public static ComplainSuccessFragment newInstance(int layoutId,ComplainReturnBean returnBean) {
		ComplainSuccessFragment fragment = new ComplainSuccessFragment();
		Bundle args=new Bundle();
		args.putSerializable(RETURN_BEAN,returnBean);
		args.putInt(LAYOUT_ID, layoutId);
		fragment.setArguments(args);
		return fragment;
	}

	public ComplainSuccessFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			returnBean =(ComplainReturnBean)getArguments().getSerializable(RETURN_BEAN);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findView();
		setListener();
		initView();
	}

	private void initView() {
		tvconplaincode.setText("您好，恭喜您，账号申诉编号："+returnBean.getData()+"，申诉成功！");
		tvusername.setText("登录账号："+returnBean.getUsername());
		tvuserpwd.setText("登录密码："+returnBean.getPassword());
	}

	/**
	 * 设置监听事件
	 */
	private void setListener() {
		btnlogin.setOnClickListener(this);
	}

	private void findView() {
		this.btnlogin = (TextView) findViewById(R.id.btn_login);
		this.tvuserpwd = (TextView) findViewById(R.id.tv_user_pwd);
		this.tvusername = (TextView) findViewById(R.id.tv_user_name);
		this.tvconplaincode = (TextView) findViewById(R.id.tv_conplain_code);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_login://登录
				ActivitySwitch.openActivity(LoginActivity.class,null,getActivity());
				break;
		}
	}
}
