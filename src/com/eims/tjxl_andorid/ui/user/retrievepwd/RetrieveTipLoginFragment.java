package com.eims.tjxl_andorid.ui.user.retrievepwd;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.base.BaseFragment;

/**
 * 验证用户 Created by kuangyong on 2015/7/1.
 */
public class RetrieveTipLoginFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG="RetrievePassWordFragment";
	private TextView btnnext;
	private TextView tv_login;
	private static RetrieveTipLoginFragment fragment;

	public static RetrieveTipLoginFragment newInstance(int layoutId) {
        if (null == fragment) {
            fragment = new RetrieveTipLoginFragment();
        }
        Bundle args = new Bundle();
        args.putInt(LAYOUT_ID, layoutId);
        fragment.setArguments(args);
        return fragment;
    }

	public RetrieveTipLoginFragment() {
		// Required empty public constructor
	}


    private void setListener() {
        btnnext.setOnClickListener(this);
        tv_login.setOnClickListener(this);

    }


    private void findView() {
		this.btnnext = (TextView) findViewById(R.id.btn_next);
		this.tv_login = (TextView) findViewById(R.id.tv_login);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        setListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next://登录
                login();
                break;
            case R.id.tv_login://登录
                login();
                break;
        }
    }

    private void login(){
        if(null!=listener){
            listener.onResult();
        }
    }

    public interface OnFragmentResultListener{
        void onResult();
    }

    private OnFragmentResultListener listener;

    public void setOnFragmentResultListener(OnFragmentResultListener listener){
        this.listener=listener;
    }
}
