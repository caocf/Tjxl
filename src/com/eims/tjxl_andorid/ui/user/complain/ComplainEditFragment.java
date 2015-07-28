package com.eims.tjxl_andorid.ui.user.complain;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.base.BaseCameraFragment;
import com.eims.tjxl_andorid.entity.ComplainResultBean;
import com.eims.tjxl_andorid.utils.DateUtil;
import com.eims.tjxl_andorid.utils.DialogUtil;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.ResourceUtils;
import com.eims.tjxl_andorid.utils.StringUtils;

import org.apache.http.Header;

import java.util.Date;
import java.util.Map;

import loopj.android.http.RequestParams;

/**
 * 申诉内容填写 Created by kuangyong on 2015/6/24.
 */
public class ComplainEditFragment extends BaseCameraFragment implements
		OnClickListener {
	private static final String TAG = "ComplainEditFragment";
	public static final int RSULTOK = 1; // 结果正常
	public static final int RSULTERROR = -1; // 请求错误
	private static final int IDCARD1 = 1; // 身份证1
	private static final int IDCARD2 = 2; // 身份证2
	private static final int BUSINESS_LICENSE = 3; // 营业执照
	private static final int CODE_LICENSE = 4; // 组织机构代码证
	private static final int TAX_LICENSE = 5; // 税务登记证
	private int uploadType;
	private android.widget.EditText etname; // 真实姓名
	private android.widget.EditText etidcard; // 身份证号码
	private android.widget.EditText etphone; // 绑定手机
	private android.widget.EditText etemail; // 绑定邮箱
	private android.widget.EditText etqq; // QQ号码
	private android.widget.EditText etrecommended; // 推荐人
	private android.widget.EditText etbrandname; // 品牌名称
	private android.widget.TextView etcreatetime; // 成立时间
	private android.widget.EditText etrepresentative; // 法定代表人
	private android.widget.EditText etregisterednum; // 工商注册号
	private android.widget.EditText etremark; // 工商注册号
	private android.widget.ImageView ividcard1; // 身份证1
	private android.widget.ImageView ivdelidcard1; //
	private android.widget.ImageView ividcard2; // 身份证2
	private android.widget.ImageView ivdelidcard2; //
	private android.widget.ImageView ivbusinesslicense; // 营业执照
	private android.widget.ImageView ivdelbusinesslicense;//
	private android.widget.ImageView ivcodelicense; // 组织机构代码证
	private android.widget.ImageView ivdelcodelicense; //
	private android.widget.ImageView ivtaxlicense; // 税务登记证
	private android.widget.ImageView ivdeltaxlicense; //
	private TextView tv_idcard_img; //身份证(正反面)
	private TextView tv_idcard; //身份证号码：
	private TextView tv_show_name; //真实姓名：


	/* 请求参数 */
	private String true_name = ""; // 真实姓名
	private String f_brank = ""; // 品牌名称
	private String f_create_time = ""; // 成立时间
	private String id_card = ""; // 身份证号码
	private String f_legal_representative = ""; // 法定代表人
	private String phone = ""; // 绑定手机
	private String f_registration_mark = ""; // 工商注册号
	private String mail = ""; // 绑定邮箱
	private String f_qq = ""; // QQ
	private String remark = ""; // 申诉原因及其他补充内容
	private String id_catd_img = ""; // 身份证正反面图片
	private String id_catd_img1 = ""; // 身份证正反面图片1
	private String id_catd_img2 = ""; // 身份证正反面图片2
	private String tax_registration_img = ""; // 税务登记证
	private String business_license = ""; // 营业执照
	private String organization_code_img = ""; // 组织机构代码证
	private String references; // 推荐人

	public static ComplainEditFragment newInstance() {
		ComplainEditFragment fragment = new ComplainEditFragment();
		return fragment;
	}

	public ComplainEditFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_complain_edit,
				container, false);
		findView(view);
		setListener();
		return view;
	}

	/**
	 * 设置监听事件
	 */
	private void setListener() {
		ividcard1.setOnClickListener(this);
		ividcard2.setOnClickListener(this);
		ivdelidcard1.setOnClickListener(this);
		ivdelidcard2.setOnClickListener(this);
		ivbusinesslicense.setOnClickListener(this);
		ivdelbusinesslicense.setOnClickListener(this);
		ivcodelicense.setOnClickListener(this);
		ivdelcodelicense.setOnClickListener(this);
		ivtaxlicense.setOnClickListener(this);
		ivdeltaxlicense.setOnClickListener(this);
		etcreatetime.setOnClickListener(this);
	}

	private void findView(View view) {
		this.ivdeltaxlicense = (ImageView) view
				.findViewById(R.id.iv_del_tax_license);
		this.ivtaxlicense = (ImageView) view.findViewById(R.id.iv_tax_license);
		this.ivdelcodelicense = (ImageView) view
				.findViewById(R.id.iv_del_code_license);
		this.ivcodelicense = (ImageView) view
				.findViewById(R.id.iv_code_license);
		this.ivdelbusinesslicense = (ImageView) view
				.findViewById(R.id.iv_del_business_license);
		this.ivbusinesslicense = (ImageView) view
				.findViewById(R.id.iv_business_license);
		this.ivdelidcard2 = (ImageView) view.findViewById(R.id.iv_del_idcard2);
		this.ividcard2 = (ImageView) view.findViewById(R.id.iv_idcard2);
		this.ivdelidcard1 = (ImageView) view.findViewById(R.id.iv_del_idcard1);
		this.ividcard1 = (ImageView) view.findViewById(R.id.iv_idcard1);
		this.etregisterednum = (EditText) view
				.findViewById(R.id.et_registered_num);
		this.etrepresentative = (EditText) view
				.findViewById(R.id.et_representative);
		this.etcreatetime = (TextView) view.findViewById(R.id.et_create_time);
		this.etbrandname = (EditText) view.findViewById(R.id.et_brand_name);
		this.etqq = (EditText) view.findViewById(R.id.et_qq);
		this.etrecommended = (EditText) view.findViewById(R.id.et_recommended);
		this.etemail = (EditText) view.findViewById(R.id.et_email);
		this.etphone = (EditText) view.findViewById(R.id.et_phone);
		this.etidcard = (EditText) view.findViewById(R.id.et_idcard);
		this.etname = (EditText) view.findViewById(R.id.et_name);
		this.etremark = (EditText) view.findViewById(R.id.et_remark);
		this.tv_idcard_img = (TextView) view.findViewById(R.id.tv_idcard_img);
		this.tv_idcard = (TextView) view.findViewById(R.id.tv_idcard);
		this.tv_show_name = (TextView) view.findViewById(R.id.tv_show_name);
		//灰色#AFAFAF
		//红色#D00000
		Spanned text_idcard_num=Html.fromHtml(ResourceUtils.changeStringColor("#D00000", "*") + ResourceUtils.changeStringColor("#AFAFAF", "身份证号码："));
		Spanned text_name=Html.fromHtml(ResourceUtils.changeStringColor("#D00000", "*") + ResourceUtils.changeStringColor("#AFAFAF", "真实姓名："));
		Spanned text_idcard_img=Html.fromHtml(ResourceUtils.changeStringColor("#D00000", "*") + ResourceUtils.changeStringColor("#AFAFAF", "身份证(正反面)"));
		tv_idcard_img.setText(text_idcard_img); //身份证(正反面)
		tv_idcard.setText(text_idcard_num); //身份证号码：
		tv_show_name.setText(text_name); //真实姓名：
	}

	/**
	 * 提交申诉
	 */
	public void commitComplain() {
		if (checkInfo()) {
			CustomResponseHandler handler = new CustomResponseHandler(
					getActivity(), true, "正在提交...") {
				@Override
				public void onSuccess(int statusCode, Header[] headers,
						String content) {
					super.onSuccess(statusCode, headers, content);
					ComplainResultBean bean = ComplainResultBean.explainJson(
							content, getActivity());
					if (null != bean) {// 返回正常
						if (null != listener) {
							listener.callBackResult(bean);
						}
					}
				}
			};
			RequestParams params = new RequestParams();
			params.put("true_name", true_name);
			params.put("f_brank", f_brank);
			params.put("f_create_time", f_create_time);
			params.put("id_card", id_card);
			params.put("f_legal_representative", f_legal_representative);
			params.put("phone", phone);
			params.put("f_registration_mark", f_registration_mark);
			params.put("email", mail);
			params.put("f_qq", f_qq);
			params.put("remark", remark);
			params.put("id_catd_img", id_catd_img);
			params.put("tax_registration_img", tax_registration_img);
			params.put("business_license", business_license);
			params.put("organization_code_img", organization_code_img);
			params.put("references", references);
			HttpClient.iAppAccountAppeal(handler, params);
		}
	}

	private boolean checkInfo() {
		boolean isOk = true;
		getTextVelue();
		if (isEmptyWithToast(true_name, "请输入真实姓名")) {
			isOk = false;
		} else if (isEmptyWithToast(id_card, "请输入身份证号码")) {
			isOk = false;
		} else if (!StringUtils.isIdCard(id_card)) {
			Toast.makeText(getActivity(), "请输入正确的身份证号码", Toast.LENGTH_SHORT)
					.show();
			isOk = false;
		} else if (!TextUtils.isEmpty(phone)
				&& !StringUtils.isPhoneNumber(phone)) {
			Toast.makeText(getActivity(), "请输入正确的手机号码", Toast.LENGTH_SHORT)
					.show();
			isOk = false;
		} else if (!TextUtils.isEmpty(mail) && !StringUtils.isEmail(mail)) {
			Toast.makeText(getActivity(), "请输入正确的邮箱号码", Toast.LENGTH_SHORT)
					.show();
			isOk = false;
		} else if (isEmptyWithToast(id_catd_img1, "请上传身份证照片")) {
			isOk = false;
		} else if (isEmptyWithToast(id_catd_img2, "请上传身份证照片")) {
			isOk = false;
		}
		return isOk;
	}

	/**
	 * 获取显示框的值
	 */
	private void getTextVelue() {
		true_name = etname.getText().toString();
		id_card = etidcard.getText().toString();
		phone = etphone.getText().toString();
		mail = etemail.getText().toString();
		f_qq = etqq.getText().toString();
		references = etrecommended.getText().toString();
		f_brank = etbrandname.getText().toString();
		f_create_time = etcreatetime.getText().toString();
		f_legal_representative = etrepresentative.getText().toString();
		f_registration_mark = etregisterednum.getText().toString();
		remark = etremark.getText().toString();
		id_catd_img = id_catd_img1 + "," + id_catd_img2;
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
		void callBackResult(ComplainResultBean bean);
	}

	private OnSavaUserInfoResultListener listener;

	public void setOnSavaUserInfoResultListener(
			OnSavaUserInfoResultListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.et_create_time:// 创立时间
			DialogUtil.showBirthdayDialog(etcreatetime, new OnClickListener() {
				@Override
				public void onClick(View v) {
					String selecteDate = etcreatetime.getText().toString();
					if (checkSelectBirthday(selecteDate)) {
						f_create_time = selecteDate;
					} else {
						etcreatetime.setText(f_create_time);
						Toast.makeText(getActivity(), "生日不能超过当前系统时间",
								Toast.LENGTH_SHORT).show();
					}
				}
			}, getActivity());
			break;
		case R.id.iv_business_license:// 营业执照
			uploadType = BUSINESS_LICENSE;
			showCameraPopwindow(v, true, true, true);
			break;
		case R.id.iv_del_business_license:// 删除营业执照
			ivbusinesslicense.setImageResource(R.drawable.add_pic);
			business_license = "";
			ivdelbusinesslicense.setVisibility(View.GONE);
			break;
		case R.id.iv_idcard1:// 身份证1
			uploadType = IDCARD1;
			showCameraPopwindow(v, true, true, true);
			break;
		case R.id.iv_del_idcard1:// 删除身份证1
			ividcard1.setImageResource(R.drawable.add_pic);
			id_catd_img1 = "";
			ivdelidcard1.setVisibility(View.GONE);
			break;
		case R.id.iv_idcard2:// 身份证2
			uploadType = IDCARD2;
			showCameraPopwindow(v, true, true, true);
			break;
		case R.id.iv_del_idcard2:// 删除身份证2
			ividcard2.setImageResource(R.drawable.add_pic);
			id_catd_img2 = "";
			ivdelidcard2.setVisibility(View.GONE);
			break;
		case R.id.iv_code_license:// 组织机构代码证
			uploadType = CODE_LICENSE;
			showCameraPopwindow(v, true, true, true);
			break;
		case R.id.iv_del_code_license:// 删除组织机构代码证
			ivcodelicense.setImageResource(R.drawable.add_pic);
			organization_code_img = "";
			ivdelcodelicense.setVisibility(View.GONE);
			break;
		case R.id.iv_tax_license:// 税务登记证
			uploadType = TAX_LICENSE;
			showCameraPopwindow(v, true, true, true);
			break;
		case R.id.iv_del_tax_license:// 删除税务登记证
			ivtaxlicense.setImageResource(R.drawable.add_pic);
			tax_registration_img = "";
			ivdeltaxlicense.setVisibility(View.GONE);
			break;
		}
	}

	private boolean checkSelectBirthday(String birthday) {
		boolean result = true;
		Date curDate=new Date();
		String curTime=DateUtil.dateToStr(curDate);
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
	public void onUpLoadSuccess(String imageUrl, String imageFile) {
		// TODO Auto-generated method stub
		super.onUpLoadSuccess(imageUrl, imageFile);
		switch (uploadType) {
		case BUSINESS_LICENSE:// 营业执照
			if (!TextUtils.isEmpty(imageFile)) {
				ImageManager.Load("file:///" + imageFile, ivbusinesslicense);
			}
			if (!TextUtils.isEmpty(imageUrl)) {
				ivdelbusinesslicense.setVisibility(View.VISIBLE);
				business_license = imageUrl;
			}
			break;
		case IDCARD1:// 身份证1
			if (!TextUtils.isEmpty(imageFile)) {
				ImageManager.Load("file:///" + imageFile, ividcard1);
			}
			if (!TextUtils.isEmpty(imageUrl)) {
				ivdelidcard1.setVisibility(View.VISIBLE);
				id_catd_img1 = imageUrl;
			}
			break;
		case IDCARD2:// 身份证2
			if (!TextUtils.isEmpty(imageFile)) {
				ImageManager.Load("file:///" + imageFile, ividcard2);
			}
			if (!TextUtils.isEmpty(imageUrl)) {
				ivdelidcard2.setVisibility(View.VISIBLE);
				id_catd_img2 = imageUrl;
			}
			break;
		case CODE_LICENSE:// 组织机构代码证
			if (!TextUtils.isEmpty(imageFile)) {
				ImageManager.Load("file:///" + imageFile, ivcodelicense);
			}
			if (!TextUtils.isEmpty(imageUrl)) {
				ivdelcodelicense.setVisibility(View.VISIBLE);
				organization_code_img = imageUrl;
			}
			break;
		case TAX_LICENSE:// 税务登记证
			if (!TextUtils.isEmpty(imageFile)) {
				ImageManager.Load("file:///" + imageFile, ivtaxlicense);
			}
			if (!TextUtils.isEmpty(imageUrl)) {
				ivdeltaxlicense.setVisibility(View.VISIBLE);
				tax_registration_img = imageUrl;
			}
			break;
		}
	}
}
