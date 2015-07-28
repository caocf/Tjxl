package com.eims.tjxl_andorid.ui.user.complain;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.base.BaseFragment;
import com.eims.tjxl_andorid.entity.ComplainReturnBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;

/**
 * 申诉失败 Created by kuangyong on 2015/6/24.
 */
public class ComplainFailedFragment extends BaseFragment implements
		OnClickListener {
	private static final String TAG = "ComplainEditFragment";
	public static final String RETURN_BEAN="return_bean";
	private ComplainReturnBean returnBean;
	private TextView tvconplaincode;
	private TextView tv_failed_reason;//申诉失败原因
	private TextView btn_complain_again;//再次申诉
	private TextView btn_confirm;//确定

	public static ComplainFailedFragment newInstance(int layoutId,ComplainReturnBean returnBean) {
		ComplainFailedFragment fragment = new ComplainFailedFragment();
		Bundle args=new Bundle();
		args.putSerializable(RETURN_BEAN,returnBean);
		args.putInt(LAYOUT_ID, layoutId);
		fragment.setArguments(args);
		return fragment;
	}

	public ComplainFailedFragment() {
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
		tvconplaincode.setText("您好，非常遗憾，账号申诉编号："+returnBean.getData()+"，申诉失败！");
		if(!TextUtils.isEmpty(returnBean.getContent())){
			tv_failed_reason.setText("登录账号："+returnBean.getContent());
		}
	}

	/**
	 * 设置监听事件
	 */
	private void setListener() {
		btn_confirm.setOnClickListener(this);
		btn_complain_again.setOnClickListener(this);
	}

	private void findView() {
		this.tv_failed_reason = (TextView) findViewById(R.id.tv_failed_reason);
		this.btn_complain_again = (TextView) findViewById(R.id.btn_complain_again);
		this.btn_confirm = (TextView) findViewById(R.id.btn_confirm);
		this.tvconplaincode = (TextView) findViewById(R.id.tv_conplain_code);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_complain_again://再次申诉
				ActivitySwitch.openActivity(FillInComplainActivity.class,null,getActivity());
				break;
			case R.id.btn_confirm://确定
				getActivity().finish();
				break;
		}
	}
}
