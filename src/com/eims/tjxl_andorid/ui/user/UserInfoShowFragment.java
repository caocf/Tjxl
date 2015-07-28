package com.eims.tjxl_andorid.ui.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.base.BaseCameraFragment;
import com.eims.tjxl_andorid.entity.IQueryUserStoreInfo;
import com.eims.tjxl_andorid.entity.UserInfoBean;
import com.eims.tjxl_andorid.ui.shopcart.AdressListActivity;
import com.eims.tjxl_andorid.ui.user.bindemail.BindEmailActivity;
import com.eims.tjxl_andorid.ui.user.bindphone.BindPhoneActivity;
import com.eims.tjxl_andorid.ui.user.updatepwd.UpdatePwdActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.weght.CircularImage;

import org.apache.http.Header;

import loopj.android.http.RequestParams;

/**
 * 账户信息展示 Created by kuangyong on 2015/6/24.
 */
public class UserInfoShowFragment extends BaseCameraFragment implements View.OnClickListener{
	private static final String TAG = "UserInfoShowFragment";
	private static final String USER_ID = "user_id";
	private static final int REQUEST_BIND_PHONE_or_email = 1;	//绑定手机或邮箱
	private String userId;										//账户id
	private com.eims.tjxl_andorid.weght.CircularImage ciuser;	//用户头像
	private TextView tvusername;								//用户名
	private TextView tvsex;										//性别
	private TextView tvbirthday;								//生日
	private TextView tvname;									//真实姓名
	private TextView tvidcard;									//身份证
//	private TextView tvqq;//qq
	private TextView tvshoestore;								//鞋店名称
	private TextView tvadress;									//鞋店地址
	private TextView tvbusinesslicense;							//营业执照
	private TextView tvphone;									//手机
	private TextView tvemail;									//邮箱
	private TextView tvupdatepwd;								//修改密码
	private TextView tvreceivingadress;							//收货地址
	private TextView tvbrowserecord;							//浏览过的商品
	private LinearLayout btn_bind_phone;						//绑定手机
	private LinearLayout btn_bind_email;						//绑定邮箱
	private ScrollView sv_main;									//主体布局
	private IQueryUserStoreInfo storeInfo;

	/**
	 * @param uid
	 *            用户id
	 * @return
	 */
	public static UserInfoShowFragment newInstance(String uid) {
		UserInfoShowFragment fragment = new UserInfoShowFragment();
		Bundle args = new Bundle();
		args.putSerializable(USER_ID, uid);
		fragment.setArguments(args);
		return fragment;
	}

	public UserInfoShowFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			userId =  getArguments().getString(USER_ID);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_user_info_show,
				container, false);
		findView(view);
		setListener();
		return view;
	}

	private void setListener() {
		tvupdatepwd.setOnClickListener(this);
		tvreceivingadress.setOnClickListener(this);
		tvbrowserecord.setOnClickListener(this);
		btn_bind_phone.setOnClickListener(this);
		btn_bind_email.setOnClickListener(this);
	}

	private void findView(View view){
		this.tvbrowserecord = (TextView) view.findViewById(R.id.tv_browse_record);
		this.btn_bind_phone = (LinearLayout) view.findViewById(R.id.btn_bind_phone);
		this.btn_bind_email = (LinearLayout) view.findViewById(R.id.btn_bind_email);
		this.tvreceivingadress = (TextView) view.findViewById(R.id.tv_receiving_adress);
		this.tvupdatepwd = (TextView) view.findViewById(R.id.tv_update_pwd);
		this.tvemail = (TextView) view.findViewById(R.id.tv_email);
		this.tvphone = (TextView) view.findViewById(R.id.tv_phone);
		this.tvbusinesslicense = (TextView) view.findViewById(R.id.tv_business_license);
		this.tvadress = (TextView) view.findViewById(R.id.tv_adress);
		this.tvshoestore = (TextView) view.findViewById(R.id.tv_shoe_store);
//		this.tvqq = (TextView) view.findViewById(R.id.tv_qq);
		this.tvidcard = (TextView) view.findViewById(R.id.tv_idcard);
		this.tvname = (TextView) view.findViewById(R.id.tv_name);
		this.tvbirthday = (TextView) view.findViewById(R.id.tv_birthday);
		this.tvsex = (TextView) view.findViewById(R.id.tv_sex);
		this.tvusername = (TextView) view.findViewById(R.id.tv_user_name);
		this.ciuser = (CircularImage) view.findViewById(R.id.ci_user);
		this.sv_main = (ScrollView) view.findViewById(R.id.sv_main);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getIQueryUserStoreInfo();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_bind_phone://绑定手机
				boolean isBindingPhone=true;
				if("1".equals(storeInfo.getData().getIs_verify_phone())){
					isBindingPhone=true;
				}else{
					isBindingPhone=false;
				}
				Bundle bundle_phone=new Bundle();
				bundle_phone.putBoolean(BindPhoneActivity.IS_BIND, isBindingPhone);
				bundle_phone.putString(BindPhoneActivity.PHONE_NUMBER, storeInfo.getData().getPhone());
				ActivitySwitch.openActivityForResult(BindPhoneActivity.class, bundle_phone, getActivity(),REQUEST_BIND_PHONE_or_email);
				break;
			case R.id.btn_bind_email://绑定邮箱
				boolean isBindingEmail=true;
				if("1".equals(storeInfo.getData().getIs_verify_email())){
					isBindingEmail=true;
				}else{
					isBindingEmail=false;
				}
				Bundle bundle_email=new Bundle();
				bundle_email.putBoolean(BindEmailActivity.IS_BIND, isBindingEmail);
				bundle_email.putString(BindEmailActivity.EMAIL_NUMBER, storeInfo.getData().getEmail());
				ActivitySwitch.openActivityForResult(BindEmailActivity.class, bundle_email, getActivity(),REQUEST_BIND_PHONE_or_email);
				break;
			case R.id.tv_update_pwd://修改密码
				Bundle bundle=new Bundle();
				bundle.putSerializable(UpdatePwdActivity.STORE_INFO,storeInfo);
				ActivitySwitch.openActivity(UpdatePwdActivity.class,bundle,getActivity());
				break;
			case R.id.tv_receiving_adress://收货地址
				Bundle bundle_adress=new Bundle();
				bundle_adress.putBoolean(AdressListActivity.IS_USERINFO, true);
				ActivitySwitch.openActivity(AdressListActivity.class,bundle_adress,getActivity());
				break;
			case R.id.tv_browse_record://浏览过的商品
				ActivitySwitch.openActivity(BrowseHistoryActivity.class,null,getActivity());
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode==Activity.RESULT_OK){
			switch (requestCode){
				case REQUEST_BIND_PHONE_or_email://绑定手机
					getIQueryUserStoreInfo();
					break;
			}
		}
	}

	/**
	 * 店铺个人中心
	 */
	private void getIQueryUserStoreInfo(){
		CustomResponseHandler handler=new CustomResponseHandler(getActivity(),true,"正在加载..."){
			@Override
			public void onSuccess(int statusCode, Header[] headers, String content) {
				super.onSuccess(statusCode, headers, content);
				Log.i(TAG, "个人资料信息---" + content);
				storeInfo=IQueryUserStoreInfo.explainJson(content, getActivity());
				if(null!=storeInfo){
					init(storeInfo);
					if(null!=listener){
						listener.callBackResult();
					}
				}
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", userId);
		HttpClient.iQueryUserStoreInfo(handler,params);
	}

	private void init(IQueryUserStoreInfo storeInfo) {
		sv_main.setVisibility(View.VISIBLE);//显示主体布局
		UserInfoBean userInfo=storeInfo.getData();
		ImageManager.Load(userInfo.getHead_img(), ciuser);
		tvusername.setText(userInfo.getUsername());
		tvsex.setText(userInfo.getSex());
		tvbirthday.setText(userInfo.getBirth());
		tvname.setText(userInfo.getTrue_name());
		tvidcard.setText(userInfo.getId_card());
//		tvqq.setText(userInfo.getId_card());
		tvshoestore.setText(userInfo.getS_store_name());
		tvadress.setText(userInfo.getAdress());
		String isUpload="";
		if(!StringUtils.isEmpty(userInfo.getS_business_license())){
			isUpload="已上传";
		}else{
			isUpload="未上传";
		}
		tvbusinesslicense.setText(isUpload);
		String isBindingPhone="";
		if("1".equals(userInfo.getIs_verify_phone())){
			isBindingPhone="已绑定";
		}else{
			isBindingPhone="未绑定";
		}
		tvphone.setText(isBindingPhone);
		String isBindingEmail="";
		if("1".equals(userInfo.getIs_verify_email())){
			isBindingEmail="已绑定";
		}else{
			isBindingEmail="未绑定";
		}
		tvemail.setText(isBindingEmail);
	}

	public interface OnLoadSuccessInfoResultListener {
		void callBackResult();
	}

	private OnLoadSuccessInfoResultListener listener;

	public void setOnLoadSuccessInfoResultListener(
			OnLoadSuccessInfoResultListener listener) {
		this.listener = listener;
	}
}
