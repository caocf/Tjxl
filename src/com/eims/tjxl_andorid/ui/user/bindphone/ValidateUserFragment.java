package com.eims.tjxl_andorid.ui.user.bindphone;


import loopj.android.http.RequestParams;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.base.BaseFragment;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.StringUtils;

/**
 * 验证用户 Created by kuangyong on 2015/7/1.
 */
public class ValidateUserFragment extends BaseFragment implements View.OnClickListener{

    private static final String TAG="ValidateUserFragment";
    private static final String PHONE="phone";
	private EditText etvalidatecode;
	private TextView tv_phone;//显示验证方式
	private TextView btnnext;
	private TextView btn_validate_code;//获取验证码
	private static ValidateUserFragment fragment;
    private TimeCount timeCount;
    private String emailOrPhone;

	public static ValidateUserFragment newInstance(int layoutId,String phone) {
        if (null == fragment) {
            fragment = new ValidateUserFragment();
        }
        Bundle args = new Bundle();
        args.putSerializable(PHONE, phone);
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
            emailOrPhone = getArguments().getString(PHONE);
        }
    }


    private void setListener() {
        btnnext.setOnClickListener(this);
//        btncancelretrieve.setOnClickListener(this);
        btn_validate_code.setOnClickListener(this);
    }


    private void findView() {
		this.btnnext = (TextView) findViewById(R.id.btn_next);
//		this.btncancelretrieve = (TextView) findViewById(R.id.btn_cancel_retrieve);
		this.tv_phone = (TextView) findViewById(R.id.tv_phone);
		this.btn_validate_code = (TextView) findViewById(R.id.btn_validate_code);
		this.etvalidatecode = (EditText) findViewById(R.id.et_validate_code);
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
        tv_phone.setText("已绑定手机号码:"+ StringUtils.changePhoneForrmat(emailOrPhone));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_validate_code://获取验证码
                timeCount.start();
                GoSendCode();
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
        params.put("type", "5");//类型（1注册 2找回密码 3修改密码 4修改银行卡信息 5绑定手机/绑定邮箱）
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
