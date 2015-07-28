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
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.base.BaseBean;
import com.eims.tjxl_andorid.base.BaseFragment;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.StringUtils;

/**
 * 绑定手机 Created by kuangyong on 2015/7/1.
 */
public class BindPhoneFragment extends BaseFragment implements
		View.OnClickListener {
	private static final String TAG = "BindPhoneFragment";
	private static final String PHONE = "phone";
	private static BindPhoneFragment fragment;
	private TimeCount timeCount;
	private String emailOrPhone;
	private android.widget.EditText etphone;
	private android.widget.TextView btnvalidatecode;
	private android.widget.EditText etvalidatecode;
	private android.widget.TextView btnnext;

	public static BindPhoneFragment newInstance(int layoutId, String phone) {
		if (null == fragment) {
			fragment = new BindPhoneFragment();
		}
		Bundle args = new Bundle();
		args.putSerializable(PHONE, phone);
		args.putInt(LAYOUT_ID, layoutId);
		fragment.setArguments(args);
		return fragment;
	}

	public BindPhoneFragment() {
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
		btnvalidatecode.setOnClickListener(this);
	}

	private void findView() {
		this.btnvalidatecode = (TextView) findViewById(R.id.btn_validate_code);
		this.etvalidatecode = (EditText) findViewById(R.id.et_validate_code);
		this.btnnext = (TextView) findViewById(R.id.btn_next);
		this.etphone = (EditText) findViewById(R.id.et_phone);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findView();
		setListener();
		timeCount = new TimeCount(30000, 1000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_validate_code:// 获取验证码
			if(ckeckPhoneNum()){
				timeCount.start();
				GoSendCode();
			}
			break;
			case R.id.btn_next:// 下一步
			iSellerBdPhoneSd();
			break;
		}
	}

	private boolean ckeckPhoneNum(){
		emailOrPhone = etphone.getText().toString();
		boolean isOK=true;
		if (TextUtils.isEmpty(emailOrPhone)) {
			isOK = false;
			Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT).show();
		} else if (!StringUtils.isPhoneNumber(emailOrPhone)) {
			isOK = false;
			Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT)
					.show();
		}
		return isOK;
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			btnvalidatecode.setText("获取验证码");
			btnvalidatecode.setEnabled(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			btnvalidatecode.setEnabled(false);
			btnvalidatecode.setText(millisUntilFinished / 1000 + "秒后可获取");
		}
	}

	/** 发送验证码 **/
	private void GoSendCode() {
		CustomResponseHandler mHandler = new CustomResponseHandler(
				getActivity(), false, "正在获取验证码...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				if (LogUtil.isDebug) {
					LogUtil.d(TAG, content);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
			}
		};
		RequestParams params = new RequestParams();
		params.put("phone", emailOrPhone);
		params.put("type", "5");//类型（1注册 2找回密码 3修改密码 4修改银行卡信息 5绑定手机/绑定邮箱）
		HttpClient.sendCode(mHandler, params);
	}

	/**
	 * 绑定手机
	 */
	private void iSellerBdPhoneSd() {
		emailOrPhone = etphone.getText().toString();
		String valicode = etvalidatecode.getText().toString();
		if (ckeck(valicode)) {
			CustomResponseHandler handler = new CustomResponseHandler(
					getActivity(), true, "验证中...") {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						String content) {
					super.onSuccess(statusCode, headers, content);
					if (LogUtil.isDebug) {
						LogUtil.i(TAG, "UID==" + content);
					}
					BaseBean bean = BaseBean
							.explainJson(content, getActivity());
					LogUtil.e("1234","回调1");
					if (null != bean) {
						if (null != listener) {
							LogUtil.e("1234","回调2");
							listener.onResult();
						}
					}
				}
			};
			RequestParams params = new RequestParams();
			params.put("phone", emailOrPhone);
			params.put("code", valicode);
			params.put("uid",
					AppContext.getInstance().getLocalUserInfo(getActivity()).id);
			HttpClient.iSellerBdPhoneSd(handler, params);
		}
	}

	/**
	 * 验证
	 * 
	 * @param valicode
	 * @return
	 */
	private boolean ckeck(String valicode) {
		boolean isOk = true;
		if (TextUtils.isEmpty(emailOrPhone)) {
			isOk = false;
			Toast.makeText(getActivity(), "请输入手机号码", Toast.LENGTH_SHORT).show();
		} else if (!StringUtils.isPhoneNumber(emailOrPhone)) {
			isOk = false;
			Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT)
					.show();
		} else if (TextUtils.isEmpty(valicode)) {
			isOk = false;
			Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_SHORT).show();
		}
		return isOk;
	}

	public interface OnFragmentResultListener {
		void onResult();
	}

	private OnFragmentResultListener listener;

	public void setOnFragmentResultListener(OnFragmentResultListener listener) {
		this.listener = listener;
	}
}
