package com.eims.tjxl_andorid.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.base.BaseBean;
import com.eims.tjxl_andorid.base.BaseCameraFragment;
import com.eims.tjxl_andorid.entity.CityAdressBean;
import com.eims.tjxl_andorid.entity.UserInfoBean;
import com.eims.tjxl_andorid.ui.product.pop.AreasPop;
import com.eims.tjxl_andorid.ui.product.pop.CityPop;
import com.eims.tjxl_andorid.ui.product.pop.PopAdapter;
import com.eims.tjxl_andorid.ui.product.pop.ProvincePop;
import com.eims.tjxl_andorid.utils.DateUtil;
import com.eims.tjxl_andorid.utils.DialogUtil;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.StringUtils;
import com.eims.tjxl_andorid.weght.CircularImage;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import loopj.android.http.RequestParams;

/**
 * 账户信息编辑 Created by kuangyong on 2015/6/24.
 */
public class UserInfoEditFragment extends BaseCameraFragment implements
		OnClickListener {
	private static final String TAG = "UserInfoEditFragment";
	public static final int RSULTOK = 1;// 结果正常
	public static final int RSULTERROR = -1;// 请求错误
	private static final String USER_INFO = "user_info";
	private UserInfoBean userInfoBean;// 账户信息
	private com.eims.tjxl_andorid.weght.CircularImage ciuser;
	private TextView tvusername;// 用户名
	// private android.widget.EditText etnickname;
	private RadioGroup rg_sex;
	private android.widget.RadioButton rbman;
	private android.widget.RadioButton rbwomen;
	private android.widget.TextView etbirthday;
	private android.widget.EditText etname;
	private android.widget.EditText etidcard;
	// private android.widget.EditText etqq;
	private android.widget.EditText etshoestore;
	private TextView tvprovince;
	private android.widget.LinearLayout layoutprovince;
	private TextView tvcity;
	private android.widget.LinearLayout layoutcity;
	private TextView tvarea;
	private android.widget.LinearLayout layoutarea;
	private android.widget.EditText etadress;
	private android.widget.ImageView ivbusinesslicense;
	private android.widget.ImageView ivdel;
	// private android.widget.EditText etphone;
	// private TextView tvphonetip;
	// private android.widget.EditText etverificationcode;
	// private android.widget.EditText etemail;
	// private TextView tvemailtip;
	private String head_img;// 用户头像
	private String true_name;// 真实姓名
	private String id_card;// 身份证
	private String sex;// 性别 0男 1女
	private String birth;// 生日，如2015-05-05
	private String s_province_id;// 省ID
	private String s_city_id;// 市ID
	private String s_town_id;// 县ID
	private String s_province_name;// 省名称
	private String s_city_name;// 市名称
	private String s_town_name;// 县名称
	private String s_address_info;// 街道地址
	// private String email;//邮箱
	// private String phone;//手机
	private String s_store_name;// 店铺名称
	private String s_business_license;// 营业执照
	private boolean isHeadImg = true;// 是否上传头像

	// 省份集合
	private List<CityAdressBean.Adress> adressProvince = new ArrayList<CityAdressBean.Adress>();
	private List<CityAdressBean.City> adressCity = new ArrayList<CityAdressBean.City>();
	private List<CityAdressBean.Areas> adressAreas = new ArrayList<CityAdressBean.Areas>();
	private ProvincePop mProvincePop;
	private CityPop cityPop;;
	private AreasPop areasPop;

	/**
	 * @param user
	 *            账户信息
	 * @return
	 */
	public static UserInfoEditFragment newInstance(UserInfoBean user) {
		UserInfoEditFragment fragment = new UserInfoEditFragment();
		Bundle args = new Bundle();
		args.putSerializable(USER_INFO, user);
		fragment.setArguments(args);
		return fragment;
	}

	public UserInfoEditFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			userInfoBean = (UserInfoBean) getArguments().getSerializable(
					USER_INFO);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_user_info_edit,
				container, false);
		findView(view);
		setListener();
		return view;
	}

	/**
	 * 设置监听事件
	 */
	private void setListener() {
		layoutprovince.setOnClickListener(this);
		layoutcity.setOnClickListener(this);
		layoutarea.setOnClickListener(this);
		ciuser.setOnClickListener(this);
		ivbusinesslicense.setOnClickListener(this);
		ivdel.setOnClickListener(this);
		etbirthday.setOnClickListener(this);
		rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_man:// 男
					sex = "0";
					break;
				case R.id.rb_women:// 女
					sex = "1";
					break;
				}
			}
		});
	}

	private void findView(View view) {
		// this.tvemailtip = (TextView) view.findViewById(R.id.tv_email_tip);
		// this.etemail = (EditText) view.findViewById(R.id.et_email);
		// this.etverificationcode = (EditText)
		// view.findViewById(R.id.et_verification_code);
		// this.tvphonetip = (TextView) view.findViewById(R.id.tv_phone_tip);
		// this.etphone = (EditText) view.findViewById(R.id.et_phone);
		this.ivdel = (ImageView) view.findViewById(R.id.iv_del);
		this.ivbusinesslicense = (ImageView) view
				.findViewById(R.id.iv_business_license);
		this.etadress = (EditText) view.findViewById(R.id.et_adress);
		this.layoutarea = (LinearLayout) view.findViewById(R.id.layout_area);
		this.tvarea = (TextView) view.findViewById(R.id.tv_area);
		this.layoutcity = (LinearLayout) view.findViewById(R.id.layout_city);
		this.tvcity = (TextView) view.findViewById(R.id.tv_city);
		this.layoutprovince = (LinearLayout) view
				.findViewById(R.id.layout_province);
		this.tvprovince = (TextView) view.findViewById(R.id.tv_province);
		this.etshoestore = (EditText) view.findViewById(R.id.et_shoe_store);
		// this.etqq = (EditText) view.findViewById(R.id.et_qq);
		this.etidcard = (EditText) view.findViewById(R.id.et_idcard);
		this.etname = (EditText) view.findViewById(R.id.et_name);
		this.rg_sex = (RadioGroup) view.findViewById(R.id.rg_sex);
		this.etbirthday = (TextView) view.findViewById(R.id.et_birthday);
		this.rbwomen = (RadioButton) view.findViewById(R.id.rb_women);
		this.rbman = (RadioButton) view.findViewById(R.id.rb_man);
		this.tvusername = (TextView) view.findViewById(R.id.tv_user_name);
		this.ciuser = (CircularImage) view.findViewById(R.id.ci_user);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViewUI();
	}

	/**
	 * 初始化UI
	 */
	private void initViewUI() {
		if (null != userInfoBean) {
			ImageManager.Load(userInfoBean.getHead_img(), ciuser);
			head_img = null == setTextNoEmpty(userInfoBean.getHead_img()) ? ""
					: setTextNoEmpty(userInfoBean.getHead_img());
			sex = userInfoBean.getSexInteger() + "";
			if (0 == userInfoBean.getSexInteger()) {// 男
				rbman.setChecked(true);
				sex = "0";
			} else {// 女
				rbwomen.setChecked(true);
				sex = "1";
			}
			tvusername.setText(userInfoBean.getUsername());
			etbirthday.setText(userInfoBean.getBirthWithGign());
			birth = null == setTextNoEmpty(userInfoBean.getBirthWithGign()) ? ""
					: setTextNoEmpty(userInfoBean.getBirthWithGign());
			etname.setText(userInfoBean.getTrue_name());
			true_name = null == setTextNoEmpty(userInfoBean.getTrue_name()) ? ""
					: setTextNoEmpty(userInfoBean.getTrue_name());
			etidcard.setText(userInfoBean.getId_card());
			id_card = null == setTextNoEmpty(userInfoBean.getId_card()) ? ""
					: setTextNoEmpty(userInfoBean.getId_card());
			etshoestore.setText(userInfoBean.getS_store_name());
			s_store_name = null == setTextNoEmpty(userInfoBean
					.getS_store_name()) ? "" : setTextNoEmpty(userInfoBean
					.getS_store_name());
			tvprovince.setText(userInfoBean.getS_province_name());
			s_province_name = null == setTextNoEmpty(userInfoBean
					.getS_province_name()) ? "" : setTextNoEmpty(userInfoBean
					.getS_province_name());
			s_province_id = null == setTextNoEmpty(userInfoBean
					.getS_province_id()) ? "" : setTextNoEmpty(userInfoBean
					.getS_province_id());
			tvcity.setText(userInfoBean.getS_city_name());
			s_city_name = null == setTextNoEmpty(userInfoBean.getS_city_name()) ? ""
					: setTextNoEmpty(userInfoBean.getS_city_name());
			s_city_id = null == setTextNoEmpty(userInfoBean.getS_city_id()) ? ""
					: setTextNoEmpty(userInfoBean.getS_city_id());
			tvarea.setText(userInfoBean.getS_town_name());
			s_town_name = null == setTextNoEmpty(userInfoBean.getS_town_name()) ? ""
					: setTextNoEmpty(userInfoBean.getS_town_name());
			s_town_id = null == setTextNoEmpty(userInfoBean.getS_town_id()) ? ""
					: setTextNoEmpty(userInfoBean.getS_town_id());
			etadress.setText(userInfoBean.getS_address_info());
			s_address_info = null == setTextNoEmpty(userInfoBean
					.getS_address_info()) ? "" : setTextNoEmpty(userInfoBean
					.getS_address_info());
			s_business_license = null == setTextNoEmpty(userInfoBean
					.getS_business_license()) ? ""
					: setTextNoEmpty(userInfoBean.getS_business_license());
			if (!StringUtils.isEmpty(s_business_license)) {
				ivdel.setVisibility(View.VISIBLE);
			}
			ImageManager.Load(userInfoBean.getS_business_license(),
					ivbusinesslicense);
			// etphone.setText(userInfoBean.getPhone());
			// etemail.setText(userInfoBean.getEmail());
			initAdressData();
		}
	}

	/**
	 * 非空设值
	 */
	private String setTextNoEmpty(String userInfoValue) {
		if (!TextUtils.isEmpty(userInfoValue)) {
			return userInfoValue;
		} else {
			return null;
		}
	}

	private void initAdressData() {
		SharedPreferences mPreferences = getActivity().getSharedPreferences(
				"adress", Context.MODE_PRIVATE);
//		if (!mPreferences.contains("adress")) {
//			loadAdress();
//		}
		String adressXml = mPreferences.getString("adress", "");
		CityAdressBean adressBean = GsonUtils.json2bean(adressXml,
				CityAdressBean.class);
		if(null!=adressBean&&null!=adressBean.data){
			for (int i = 0; i < adressBean.data.size(); i++) {
				String pn = adressBean.data.get(i).pn;
				LogUtil.d(TAG, pn);
				CityAdressBean.Adress adress = adressBean.data.get(i);
				adressProvince.add(adress);
			}
		}
	}

//	/**
//	 * 请求加载省市区数据
//	 */
//	private void loadAdress() {
//		CustomResponseHandler mHandler = new CustomResponseHandler(
//				getActivity(), false, "") {
//			@Override
//			public void onSuccess(int statusCode, String content) {
//				// TODO Auto-generated method stub
//				super.onSuccess(statusCode, content);
//				if (statusCode == 200) {
//					SharedPreferences mPreferences = getActivity()
//							.getSharedPreferences("adress",
//									Context.MODE_PRIVATE);
//					SharedPreferences.Editor mEditor = mPreferences.edit();
//					mEditor.putString("adress", content);
//					mEditor.commit();
//
//				}
//			}
//		};
//		RequestParams params = new RequestParams();
//		HttpClient.QueryPrvionceCity(mHandler, params);
//	}

	/**
	 * 保存
	 */
	public void saveUserInfo() {
		if (checkInfo()) {
			CustomResponseHandler handler = new CustomResponseHandler(
					getActivity(), true, "正在保存...") {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						String content) {
					super.onSuccess(statusCode, headers, content);
					BaseBean bean = BaseBean
							.explainJson(content, getActivity());
					if (null != bean) {// 返回正常
						if (null != listener) {
							listener.callBackResult(RSULTOK);
						}
					} else {
						if (null != listener) {
							listener.callBackResult(RSULTERROR);
						}
					}
				}
			};
			RequestParams params = new RequestParams();
			Context context = getActivity();
		 	params.put("uid", AppContext.getLocalUserInfo(context).id);
			params.put("head_img", head_img);
			params.put("true_name", true_name);
			params.put("id_card", id_card);
			params.put("sex", sex);
			params.put("birth", birth);
			params.put("s_province_id", s_province_id);
			params.put("s_city_id", s_city_id);
			params.put("s_town_id", s_town_id);
			params.put("s_province_name", s_province_name);
			params.put("s_city_name", s_city_name);
			params.put("s_town_name", s_town_name);
			params.put("s_address_info", s_address_info);
			// params.put("email", email);
			// params.put("phone", phone);
			params.put("s_store_name", s_store_name);
			params.put("s_business_license", s_business_license);
			HttpClient.iUpdateStoreUserInfo(handler, params);
		}
	}

	private boolean checkInfo() {
		boolean isOk = true;
		getTextVelue();
		if (isEmptyWithToast(head_img, "请上传头像")) {
			isOk = false;
		} else if (isEmptyWithToast(sex, "请选择性别")) {
			isOk = false;
		} else if (isEmptyWithToast(birth, "请选择生日")) {
			isOk = false;
		} else if (isEmptyWithToast(true_name, "请输入真实姓名")) {
			isOk = false;
		} else if (isEmptyWithToast(id_card, "请输入身份证")) {
			isOk = false;
		} else if (!StringUtils.isIdCard(id_card)) {
			Toast.makeText(getActivity(), "请输入正确的身份证号码", Toast.LENGTH_SHORT)
					.show();
			isOk = false;
		} else if (isEmptyWithToast(s_store_name, "请输入店铺名称")) {
			isOk = false;
		} else if (isEmptyWithToast(s_province_id, "请选择省")) {
			isOk = false;
		} else if (isEmptyWithToast(s_city_id, "请选择市")) {
			isOk = false;
		} else if (isEmptyWithToast(s_town_id, "请选择区")) {
			isOk = false;
		} else if (isEmptyWithToast(s_address_info, "请输入街道地址")) {
			isOk = false;
		} else if (isEmptyWithToast(s_business_license, "请上传营业执照")) {
			isOk = false;
		}
		return isOk;
	}

	/**
	 * 获取显示框的值
	 */
	private void getTextVelue() {
		birth = etbirthday.getText().toString();
		true_name = etname.getText().toString();
		id_card = etidcard.getText().toString();
		s_store_name = etshoestore.getText().toString();
		s_address_info = etadress.getText().toString();
	}

	/**
	 * 判断非空并给出提示
	 * 
	 * @param value
	 * @param tipMsg
	 * @return
	 */
	private boolean isEmptyWithToast(String value, String tipMsg) {
		if (TextUtils.isEmpty(value)) {
			Toast.makeText(getActivity(), tipMsg, Toast.LENGTH_SHORT).show();
			return true;
		} else {
			return false;
		}
	}

	public interface OnSavaUserInfoResultListener {
		void callBackResult(int result_code);
	}

	private OnSavaUserInfoResultListener listener;

	public void setOnSavaUserInfoResultListener(
			OnSavaUserInfoResultListener listener) {
		this.listener = listener;
	}

	private boolean checkSelectBirthday(String birthday) {
		boolean result = true;
		String curTime = userInfoBean.getSys_time();
		Map<String, Integer> date = DateUtil.strToMap(birthday, false);
		int year = date.get("year");
		int month = date.get("month");
		int day = date.get("day");
		Map<String, Integer> curdate = DateUtil.strToMap(curTime, false);
		int curyear = curdate.get("year");
		int curmonth = curdate.get("month");
		int curday = curdate.get("day");
		if (year > curyear) {// 当前年大于系统时间
			result = false;
		} else if (month > curmonth) {// 当前月大于系统时间
			result = false;
		} else if (day > curday) {// 当前日大于系统时间
			result = false;
		}
		return result;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ci_user:// 头像
			isHeadImg = true;
			showCameraPopwindow(v, true, true, true);
			break;
		case R.id.iv_business_license:// 营业执照
			isHeadImg = false;
			showCameraPopwindow(v, true, true, true);
			break;
		case R.id.iv_del:// 删除营业执照
			ivbusinesslicense.setImageResource(R.drawable.add_pic);
			s_business_license = "";
			ivdel.setVisibility(View.GONE);
			break;
		case R.id.et_birthday:// 生日
			DialogUtil.showBirthdayDialog(etbirthday, new OnClickListener() {
				@Override
				public void onClick(View v) {
					String selecteDate = etbirthday.getText().toString();
					if (checkSelectBirthday(selecteDate)) {
						birth = selecteDate;
					} else {
						etbirthday.setText(birth);
						Toast.makeText(getActivity(), "生日不能超过当前系统时间",
								Toast.LENGTH_SHORT).show();
					}
				}
			}, getActivity());
			break;
		case R.id.layout_province:// 省
			mProvincePop = new ProvincePop(getActivity(), 1, adressProvince);
			mProvincePop.showDialog();
			mProvincePop
					.setQuitDialogListener(new ProvincePop.ClickItemListener() {
						@Override
						public void onConfirmClick(CityAdressBean.Adress bean,
												   PopAdapter adapter) {
							// TODO Auto-generated method stub
							s_province_id = bean.pId;
							s_province_name = bean.pn;
							tvprovince.setText(s_province_name);
							clearCityOrArea(tvcity);
							clearCityOrArea(tvarea);
							// 应该获取当前省份下的所有城市
							adressCity.clear();
							for (int j = 0; j < bean.cities.size(); j++) {
								CityAdressBean.City city = bean.cities.get(j);
								adressCity.add(city);
							}
							layoutcity.performClick();
						}
					});
			break;
		case R.id.layout_city:// 市
			cityPop = new CityPop(getActivity(), 2, adressCity);
			cityPop.showDialog();
			cityPop.setQuitDialogListener(new CityPop.ClickItemListener() {
				@Override
				public void onConfirmClick(CityAdressBean.City bean) {
					// TODO Auto-generated method stub
					s_city_id = bean.cId;
					s_city_name = bean.cn;
					tvcity.setText(s_city_name);
					clearCityOrArea(tvarea);
					adressAreas.clear();
					for (int j = 0; j < bean.areas.size(); j++) {
						CityAdressBean.Areas areas = bean.areas.get(j);
						adressAreas.add(areas);
					}
					layoutarea.performClick();
				}
			});
			break;
		case R.id.layout_area:// 区
			areasPop = new AreasPop(getActivity(), 3, adressAreas);
			areasPop.showDialog();
			areasPop.setQuitDialogListener(new AreasPop.ClickItemListener() {
				@Override
				public void onConfirmClick(CityAdressBean.Areas bean) {
					// TODO Auto-generated method stub
					s_town_id = bean.aId;
					s_town_name = bean.an;
					tvarea.setText(s_town_name);
				}
			});
			break;
		}
	}

	/**
	 * 清除市或区
	 * 
	 * @param witchView
	 */
	private void clearCityOrArea(TextView witchView) {
		int id = witchView.getId();
		switch (id) {
		case R.id.tv_city:// 市
			tvcity.setText("市");
			s_city_name = "";
			s_city_id = "";
			break;
		case R.id.tv_area:// 区
			tvarea.setText("区");
			s_town_id = "";
			s_town_name = "";
			break;
		}
	}

	@Override
	public void onUpLoadSuccess(String imageUrl, String imageFile) {
		// TODO Auto-generated method stub
		super.onUpLoadSuccess(imageUrl, imageFile);
		if (isHeadImg) {// 用户头像
			if (!TextUtils.isEmpty(imageFile)) {
				ImageManager.Load("file:///" + imageFile, ciuser);
			}
			if (!TextUtils.isEmpty(imageUrl)) {
				head_img = imageUrl;
			}
		} else {// 营业执照
			if (!TextUtils.isEmpty(imageFile)) {
				ImageManager.Load("file:///" + imageFile, ivbusinesslicense);
			}
			if (!TextUtils.isEmpty(imageUrl)) {
				ivdel.setVisibility(View.VISIBLE);
				s_business_license = imageUrl;
			}
		}
	}

}
