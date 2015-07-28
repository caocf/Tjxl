/**
 * 
 */
package com.eims.tjxl_andorid.ui.user;

import org.json.JSONObject;

import loopj.android.http.RequestParams;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.MD5;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.utils.TipToast;





import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: 鞋店注册
 * @Author zd
 * @date 2015年6月23日 上午9:54:02
 *************************************************************************** 
 */
public class ShoeStoreFragment extends Fragment {

	private View rootView;
	private EditText mUserEimalOrPhone;
	private EditText mUserNickName;
	private EditText mCode;
	private EditText mUserPwd,confirmPwd;
	private EditText mReferees;
	private LinearLayout Register_btn;
	private TextView mTvXieyi;
	private CheckBox mCheckBox;
	private boolean isflag=false;
	private String emailOrPhone;
	private String nickname;
	private String code;
	private String pwd;
	private String mTuijianren;
	private String confirmUserPwd;
	private TextView mGetCode;
	private TimeCount timeCount;
	public static final String TAG = "ShoeStoreFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.shoe_store_layout, null);
		}
		// 缓存的rootView需要判断是否已经被加过parent，
		// 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
		}
		timeCount=new TimeCount(60000, 1000);
		return rootView;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		findviews();
		setListener();
	}

	private void findviews() {

		mUserEimalOrPhone = (EditText) getActivity().findViewById(
				R.id.email_phone);
		mUserNickName = (EditText) getActivity().findViewById(R.id.user_nickname);
		mCode = (EditText) getActivity().findViewById(R.id.code);
		mUserPwd = (EditText) getActivity().findViewById(R.id.user_password);
		confirmPwd = (EditText) getActivity().findViewById(R.id.confirm_user_password);
		mReferees = (EditText) getActivity().findViewById(R.id.tuijianren);
		Register_btn = (LinearLayout) getActivity().findViewById(R.id.Register_btn);
		mTvXieyi=(TextView) getActivity().findViewById(R.id.xieyi);
		mCheckBox=(CheckBox) getActivity().findViewById(R.id.checkbox);
		mGetCode=(TextView) getActivity().findViewById(R.id.getCode);
		mTvXieyi.setText("<<鞋联服务协议>>");
	}
	
	private void  setListener(){
		mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean value) {
				if(value){
					isflag=true;
				}else{
					isflag=false;	
				}
			}
		});
		
		mGetCode.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View arg0) {
				emailOrPhone = mUserEimalOrPhone.getText().toString();
				if (emailOrPhone != null && emailOrPhone != ""&& emailOrPhone.length() > 0) {
					if (!StringUtils.isNumeric(emailOrPhone)&& !StringUtils.isEmail(emailOrPhone)) {
						TipToast.makeText(getActivity(), "手机或邮箱格式不正确", 0).show();
						return ;
					}
					if (StringUtils.isNumeric(emailOrPhone)&& !StringUtils.isMobileNO(emailOrPhone)) {
						TipToast.makeText(getActivity(), "手机或邮箱格式不正确", 0).show();
						return ;
					}

				} else {
					TipToast.makeText(getActivity(), "手机或邮箱不能为空", 0).show();
					return ;
			    	}
				mGetCode.setEnabled(false);
				timeCount.start();
				GoSendCode();
				
				
			}
		});
		
		Register_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(checkInfo()){
					GoRegister();
				}
			}
		});
		
		mTvXieyi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ActivitySwitch.openActivity(ServerXieyiActivity.class, null, getActivity());
			}
		});
	}
	
	private void GoRegister(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(getActivity(), true,"正在注册..."){
		         
		          	@Override
		          	public void onSuccess(int statusCode, String content) {
		          		// TODO Auto-generated method stub
		          		super.onSuccess(statusCode, content);
		          		LogUtil.d(TAG, content);
		          		try {
		          			JSONObject  json=new JSONObject(content);
		          			if("1".equals(json.getString("type"))){
		          				clearData();
		          			}
		          			String msg = json.getString("msg");
		          			TipToast.makeText(getActivity(), msg, 0).show();
		          			ActivitySwitch.openActivity(LoginActivity.class, null, getActivity());
						} catch (Exception e) {
							e.printStackTrace();
						}
		          		
		          	}
		          	@Override
		          	public void onFailure(Throwable error, String content) {
		          	// TODO Auto-generated method stub
		          	super.onFailure(error, content);
		          	}
		};
		RequestParams  params=new RequestParams();
		params.put("username", emailOrPhone);
		params.put("password", md5(pwd));
		params.put("rePassword", md5(confirmUserPwd));
		params.put("code", code);
		params.put("references", mTuijianren);
		params.put("member_type", "0");
		HttpClient.getRegister(mHandler, params);
		
	}

	protected String md5(String content){
		return MD5.md5(content);
	}

	/**发送验证码**/
	private void GoSendCode(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(getActivity(), false,"正在获取验证码..."){
		         
		          	@Override
		          	public void onSuccess(int statusCode, String content) {
		          		// TODO Auto-generated method stub
		          		super.onSuccess(statusCode, content);
		          		LogUtil.d(TAG, content);
		          	}
		          	@Override
		          	public void onFailure(Throwable error, String content) {
		          	// TODO Auto-generated method stub
		             	super.onFailure(error, content);
		          	}
		};
		RequestParams  params=new RequestParams();
		params.put("phone", emailOrPhone);
		HttpClient.sendCode(mHandler, params);
		
	}

	private boolean checkInfo() {
		emailOrPhone = mUserEimalOrPhone.getText().toString();
		//nickname = mUserNickName.getText().toString();
		code = mCode.getText().toString();
		pwd = mUserPwd.getText().toString();
		mTuijianren = mReferees.getText().toString();
		confirmUserPwd = confirmPwd.getText().toString();
		if (emailOrPhone != null && emailOrPhone != ""&& emailOrPhone.length() > 0) {
			if (!StringUtils.isNumeric(emailOrPhone)&& !StringUtils.isEmail(emailOrPhone)) {
				TipToast.makeText(getActivity(), "手机或邮箱格式不正确", 0).show();
				return false;
			}
			if (StringUtils.isNumeric(emailOrPhone)&& !StringUtils.isMobileNO(emailOrPhone)) {
				TipToast.makeText(getActivity(), "手机或邮箱格式不正确", 0).show();
				return false;
			}
//			if(TextUtils.isEmpty(nickname)){
//				TipToast.makeText(getActivity(), "昵称不能为空", 0).show();
//				return false;
//			}
			
			if(TextUtils.isEmpty(code)){
				TipToast.makeText(getActivity(), "请输入验证码", 0).show();
				return false;
			}
			if(TextUtils.isEmpty(pwd)){
				TipToast.makeText(getActivity(), "请输入密码", 0).show();
				return false;
			}
			if(!pwd.equals(confirmUserPwd)){
				TipToast.makeText(getActivity(), "两次密码输入不一致", 0).show();
				return false;
			}
			
			if(!isflag){
				TipToast.makeText(getActivity(), "请阅读鞋联服务协议", 0).show();
				return false;
			}
		} else {
			TipToast.makeText(getActivity(), "手机或邮箱不能为空", 0).show();
			return false;
		}
		return true;
	}
	
	   class TimeCount extends CountDownTimer {
	    	public TimeCount(long millisInFuture, long countDownInterval) {
	    	super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
	    	}
	    	@Override
	    	public void onFinish() {//计时完毕时触发
	    		mGetCode.setText("发送验证码");
	    		mGetCode.setEnabled(true);
	    	}
	    	@Override
	    	public void onTick(long millisUntilFinished){//计时过程显示
	    		mGetCode.setEnabled(false);
	    		mGetCode.setText(millisUntilFinished /1000+"秒后可获取");
	    	}
	   }
	   
	   private void clearData(){
		   isflag=false;
		   mUserEimalOrPhone.setText("");
		   mCode.setText("");
		   mUserPwd.setText("");
		   confirmPwd.setText("");
		   mReferees.setText("");
		   mCheckBox.setChecked(isflag);
	   }

}
