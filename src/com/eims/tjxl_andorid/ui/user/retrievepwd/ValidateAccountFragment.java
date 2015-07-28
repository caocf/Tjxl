package com.eims.tjxl_andorid.ui.user.retrievepwd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.entity.IsExistUserNameBean;
import com.eims.tjxl_andorid.utils.CodeImage;

import org.apache.http.Header;

import loopj.android.http.RequestParams;

/**
 * 验证账号 Created by kuangyong on 2015/7/1.
 */
public class ValidateAccountFragment extends Fragment implements View.OnClickListener{

	private android.widget.EditText etusername;
	private android.widget.EditText etvalidatecode;
	private android.widget.ImageView ivvalidatecode;
	private TextView btncancelretrieve;
	private TextView btnnext;
	private static ValidateAccountFragment fragment;
    private String user_name;

	public static ValidateAccountFragment newInstance() {
        if (null == fragment) {
            fragment = new ValidateAccountFragment();
        }
        return fragment;
    }

	public ValidateAccountFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_validate_account, container,false);
        findView(view);
        setListener();
		return view;
	}

    private void setListener() {
        setValidataCode();
        btnnext.setOnClickListener(this);
        btncancelretrieve.setOnClickListener(this);
        ivvalidatecode.setOnClickListener(this);
    }

    private void setValidataCode(){
        ivvalidatecode.setImageBitmap(CodeImage.getInstance().createBitmap());
        ivvalidatecode.setTag(CodeImage.getInstance().getCode());
    }

    private void findView(View view) {
		this.btnnext = (TextView) view.findViewById(R.id.btn_next);
		this.btncancelretrieve = (TextView) view.findViewById(R.id.btn_cancel_retrieve);
		this.ivvalidatecode = (ImageView) view.findViewById(R.id.iv_validate_code);
		this.etvalidatecode = (EditText) view.findViewById(R.id.et_validate_code);
		this.etusername = (EditText) view.findViewById(R.id.et_username);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_validate_code://更换验证码
                setValidataCode();
                break;
            case R.id.btn_cancel_retrieve://取消
                getActivity().finish();
                break;
            case R.id.btn_next://下一步
                iIsExistByUserName();
                break;
        }
    }

    /**
     * 验证用户名是否存在
     */
    private void iIsExistByUserName() {
        user_name=etusername.getText().toString();
        if(ckeck()){
            CustomResponseHandler handler=new CustomResponseHandler(getActivity(),true,"验证中..."){
                @Override
                public void onSuccess(int statusCode, Header[] headers, String content) {
                    super.onSuccess(statusCode, headers, content);
                    IsExistUserNameBean bean=IsExistUserNameBean.explainJson(content,getActivity());
                    if(null!=bean){
                        if(null!=listener){
                            listener.onResult(bean);
                        }
                    }
                }
            };
            RequestParams params=new RequestParams();
            params.put("user_name",user_name);
            HttpClient.iIsExistByUserName(handler,params);
        }
    }

    public interface OnFragmentResultListener{
        void onResult(IsExistUserNameBean bean);
    }

    private OnFragmentResultListener listener;

    public void setOnFragmentResultListener(OnFragmentResultListener listener){
        this.listener=listener;
    }

    private boolean ckeck(){
        boolean isOk=true;
        if(TextUtils.isEmpty(user_name)){
            isOk=false;
            Toast.makeText(getActivity(),"用户名不能为空",Toast.LENGTH_SHORT).show();
        }
        String imgCode=(String)ivvalidatecode.getTag();
        String etCode=etvalidatecode.getText().toString();
        if(TextUtils.isEmpty(etCode)){
            isOk=false;
            Toast.makeText(getActivity(),"请输入验证码",Toast.LENGTH_SHORT).show();
        }else if(!etCode.equalsIgnoreCase(imgCode)){
            isOk=false;
            Toast.makeText(getActivity(),"验证码不正确",Toast.LENGTH_SHORT).show();
        }
        return isOk;
    }
}
