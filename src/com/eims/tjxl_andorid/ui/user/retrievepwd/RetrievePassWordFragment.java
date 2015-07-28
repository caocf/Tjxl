package com.eims.tjxl_andorid.ui.user.retrievepwd;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.base.BaseBean;
import com.eims.tjxl_andorid.base.BaseFragment;

import org.apache.http.Header;

import loopj.android.http.RequestParams;

/**
 * 重置密码 Created by kuangyong on 2015/7/1.
 */
public class RetrievePassWordFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG="RetrievePassWordFragment";
    private static final String UID="uid";
	private EditText et_new_pwd;//新密码
	private EditText et_confirm_pwd;//确认密码
	private TextView btnnext;
	private static RetrievePassWordFragment fragment;
    private String uid;

	public static RetrievePassWordFragment newInstance(int layoutId,String uid) {
        if (null == fragment) {
            fragment = new RetrievePassWordFragment();
        }
        Bundle args = new Bundle();
        args.putString(UID, uid);
        args.putInt(LAYOUT_ID, layoutId);
        fragment.setArguments(args);
        return fragment;
    }

	public RetrievePassWordFragment() {
		// Required empty public constructor
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uid = getArguments().getString(UID);
        }
    }

    private void setListener() {
        btnnext.setOnClickListener(this);

    }


    private void findView() {
		this.btnnext = (TextView) findViewById(R.id.btn_next);
		this.et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
		this.et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
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
            case R.id.btn_next://下一步
                iFindPwdSave();
                break;
        }
    }

    /**
     * 修改新密码
     */
    private void iFindPwdSave() {
        String cpwd=et_confirm_pwd.getText().toString();
        String pwd=et_new_pwd.getText().toString();
        if(ckeck(pwd,cpwd)){
            CustomResponseHandler handler=new CustomResponseHandler(getActivity(),true,"验证中..."){
                @Override
                public void onSuccess(int statusCode, Header[] headers, String content) {
                    super.onSuccess(statusCode, headers, content);
                    BaseBean bean=BaseBean.explainJson(content,getActivity());
                    if(null!=bean){
                        Toast.makeText(getActivity(),"修改密码成功",Toast.LENGTH_SHORT).show();
                        if(null!=listener){
                            listener.onResult();
                        }
                    }
                }
            };
            RequestParams params=new RequestParams();
            params.put("id",uid);
            params.put("password",md5(pwd));
            params.put("password2",md5(cpwd));
            HttpClient.iFindPwdSave(handler, params);
        }
    }

    private boolean  ckeck(String pwd,String confirmPwd){
        boolean isOk=true;
        if(TextUtils.isEmpty(pwd)){
            Toast.makeText(getActivity(),"请输入新密码",Toast.LENGTH_SHORT).show();
            isOk=false;
        }else if(TextUtils.isEmpty(confirmPwd)){
            Toast.makeText(getActivity(),"请输入确认密码",Toast.LENGTH_SHORT).show();
            isOk=false;
        }else if(!pwd.matches("^[^\\s]{6,16}$")){
            Toast.makeText(getActivity(),"密码由6-16位字符(字母、数字、符号)组合而成",Toast.LENGTH_SHORT).show();
            isOk=false;
        }else if(!TextUtils.isEmpty(pwd)&&!pwd.equals(confirmPwd)){
            Toast.makeText(getActivity(),"两次密码输入不一致，请重新输入",Toast.LENGTH_SHORT).show();
            isOk=false;
        }
        return isOk;
    }

    public interface OnFragmentResultListener{
        void onResult();
    }

    private OnFragmentResultListener listener;

    public void setOnFragmentResultListener(OnFragmentResultListener listener){
        this.listener=listener;
    }
}
