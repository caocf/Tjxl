package com.eims.tjxl_andorid.ui.user.updatepwd;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.base.BaseFragment;
import com.eims.tjxl_andorid.entity.IsExistUserNameBean;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.StringUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import loopj.android.http.RequestParams;

/**
 * 验证用户 Created by kuangyong on 2015/7/1.
 */
public class ValidateUserFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG="ValidateUserFragment";
    private static final String USER_VALIDATE_INFO="user_validate_info";
	private EditText etvalidatecode;
    private LinearLayout layout_validate_select;//选择验证方式
//	private TextView btncancelretrieve;
	private TextView tv_validate_type;//显示验证方式
	private TextView btnnext;
	private TextView btn_validate_code;//获取验证码
	private static ValidateUserFragment fragment;
    private IsExistUserNameBean bean;
    private ArrayList<String> validateType;
    private Map<String ,String > typeMap;
    private TimeCount timeCount;
    private String emailOrPhone;

	public static ValidateUserFragment newInstance(int layoutId,IsExistUserNameBean bean) {
        if (null == fragment) {
            fragment = new ValidateUserFragment();
        }
        Bundle args = new Bundle();
        args.putSerializable(USER_VALIDATE_INFO, bean);
        args.putInt(LAYOUT_ID, layoutId);
        fragment.setArguments(args);
        return fragment;
    }

	public ValidateUserFragment() {
		// Required empty public constructor
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bean = (IsExistUserNameBean)getArguments().getSerializable(USER_VALIDATE_INFO);
            initTypeData();
        }
    }

    /**
     * 初始化验证方式数据
     */
    private void initTypeData(){
        validateType=new ArrayList<String>();
        typeMap=new HashMap<String, String>();
        String idBindPhone=bean.getData().getIs_verify_phone();
        String idBindEmail=bean.getData().getIs_verify_email();
        if("1".equals(idBindPhone)){//验证了手机
//            String showPhone="手机："+ StringUtils.changePhoneForrmat(bean.getData().getPhone());
            String showPhone="手机："+bean.getData().getPhone();
            validateType.add(showPhone);
            typeMap.put(showPhone,bean.getData().getPhone());
        }
        if("1".equals(idBindEmail)){//验证了邮箱
            String showEmail="邮箱：" + bean.getData().getEmail();
//            String showEmail="邮箱：" + StringUtils.changeEmailForrmat(bean.getData().getEmail());
            validateType.add(showEmail);
            typeMap.put(showEmail, bean.getData().getEmail());
        }
    }

    private void setListener() {
        btnnext.setOnClickListener(this);
        layout_validate_select.setOnClickListener(this);
//        btncancelretrieve.setOnClickListener(this);
        btn_validate_code.setOnClickListener(this);
    }


    private void findView() {
		this.btnnext = (TextView) findViewById(R.id.btn_next);
//		this.btncancelretrieve = (TextView) findViewById(R.id.btn_cancel_retrieve);
		this.tv_validate_type = (TextView) findViewById(R.id.tv_validate_type);
		this.btn_validate_code = (TextView) findViewById(R.id.btn_validate_code);
		this.etvalidatecode = (EditText) findViewById(R.id.et_validate_code);
		this.layout_validate_select = (LinearLayout) findViewById(R.id.layout_validate_select);
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findView();
        setListener();
        timeCount=new TimeCount(30000, 1000);
        initView();
    }

    private void initView() {
        tv_validate_type.setText(validateType.get(0));
        emailOrPhone=typeMap.get(validateType.get(0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_validate_code://获取验证码
                timeCount.start();
                GoSendCode();
                break;
            case R.id.layout_validate_select://选择验证方式
                selectValidateType();
                break;
            case R.id.btn_cancel_retrieve://取消
                getActivity().finish();
                break;
            case R.id.btn_next://下一步
                iFindPwdOne();
                break;
        }
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {//计时完毕时触发
            btn_validate_code.setText("获取验证码");
            btn_validate_code.setEnabled(true);
        }
        @Override
        public void onTick(long millisUntilFinished){//计时过程显示
            btn_validate_code.setEnabled(false);
            btn_validate_code.setText(millisUntilFinished /1000+"秒后可获取");
        }
    }



    /**
     * 选择验证方式
     */
    private void selectValidateType() {
        showListPopBelowView(getActivity(), layout_validate_select, validateType, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                emailOrPhone = typeMap.get(validateType.get(position));
                tv_validate_type.setText(validateType.get(position));
                if(null!=listItemWindow&&listItemWindow.isShowing()){
                    listItemWindow.dismiss();
                }
            }
        });
    }

    /**发送验证码**/
    private void GoSendCode(){
        CustomResponseHandler  mHandler=new CustomResponseHandler(getActivity(), false,"正在获取验证码..."){

            @Override
            public void onSuccess(int statusCode, String content) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, content);
                if(LogUtil.isDebug){
                    LogUtil.d(TAG, content);
                }
            }
            @Override
            public void onFailure(Throwable error, String content) {
                // TODO Auto-generated method stub
                super.onFailure(error, content);
            }
        };
        RequestParams  params=new RequestParams();
        params.put("phone", emailOrPhone);
        params.put("type", "3");//类型（1注册 2找回密码 3修改密码 4修改银行卡信息 5绑定手机 6绑定邮箱）
        HttpClient.sendCode(mHandler, params);
    }

    /**
     * 验证用户名是否存在
     */
    private void iFindPwdOne() {
        String valicode=etvalidatecode.getText().toString();
        if(!TextUtils.isEmpty(valicode)){
            CustomResponseHandler handler=new CustomResponseHandler(getActivity(),true,"验证中..."){
                @Override
                public void onSuccess(int statusCode, Header[] headers, String content) {
                    super.onSuccess(statusCode, headers, content);
                    try {
                        if(LogUtil.isDebug){
                            LogUtil.i(TAG,"UID=="+content);
                        }
                        JSONObject json=new JSONObject(content);
                        String uid=json.optString("data");
                        if(!TextUtils.isEmpty(uid)){
                            if(null!=listener){
                                listener.onResult(uid);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            RequestParams params=new RequestParams();
            params.put("user_name",emailOrPhone);
            params.put("valicode",valicode);
            HttpClient.iFindPwdOne(handler, params);
        }else{
            Toast.makeText(getActivity(),"请输入验证码",Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnFragmentResultListener{
        void onResult(String uid);
    }

    private OnFragmentResultListener listener;

    public void setOnFragmentResultListener(OnFragmentResultListener listener){
        this.listener=listener;
    }
}
