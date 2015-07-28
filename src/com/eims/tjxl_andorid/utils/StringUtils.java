package com.eims.tjxl_andorid.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.eims.tjxl_andorid.constans.URLs;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.view.WindowManager;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: StringUtils
 * @Description: 字符串操作工具包
 * @Author
 * @date 2013-8-6 下午1:51:14
 ************************************************************************** 
 */
public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	private final static String PHONE = "^1[3,5,8]\\d{9}$";

	private final static String ID_CARD = "([0-9]{17}([0-9]|X))|([0-9]{15})";

	/**
	 * 将字符串转位日期类型
	 *
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	// public static boolean isEmail(String email) {
	// if (email == null || email.trim().length() == 0)
	// return false;
	// return emailer.matcher(email).matches();
	// }

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 判断是否为手机号码
	 * 
	 * @param phoneNumber
	 * @return
	 */
	public static boolean isPhoneNumber(String phoneNumber) {

		return phoneNumber.matches(PHONE);
	}


	/**
	 * 判断是否为身份证号码
	 *
	 * @param idCard
	 * @return
	 */
	public static boolean isIdCard(String idCard) {

		return idCard.matches(ID_CARD);
	}

	/**
	 * 像素值转换成dp
	 * 
	 * @param mContext
	 * @param px
	 * @return
	 */
	public static int PxToDp(Context mContext, int px) {
		final float scale = mContext.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/**
	 * DP转成像素
	 * 
	 * @param context
	 * @param dp
	 * @return
	 */
	public static int DpToPx(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale);
	}

	/**
	 * 参数排序签名
	 * 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	// mkey在这时生成-------------------------------------------------
	public static String getSignData(Map<String, String> params)
			throws Exception {
		StringBuffer content = new StringBuffer();
		// 按照key做排序
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);
			String value = (String) params.get(key);
			// 具体逻辑修改下面的路径进行拼接
			if (value != null) {
				content.append(value);
			} else {
				content.append("");
			}
		}
		return content.toString();
	}

	/**
	 * 获取屏幕的宽高
	 * 
	 * @param manager
	 * @return
	 */
	public static Point getPICSize(WindowManager manager) {
		Point mPoint = new Point();
		int x = manager.getDefaultDisplay().getWidth();
		int y = manager.getDefaultDisplay().getHeight();
		mPoint.x = x;
		mPoint.y = y;
		return mPoint;
	}

	/**
	 * 验证输入密码条件(字符与数据同时出现)
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 */
	public static boolean IsPassword(String str) {
		String regex = "[A-Za-z0-9]{6,18}";
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(str);
		// boolean b=;
		return match.matches();
	}

	/***
	 * 手机号码验证
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);

		return m.matches();
	}

	// 判断email格式是否正确

	public static boolean isEmail(String email) {

		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

		Pattern p = Pattern.compile(str);

		Matcher m = p.matcher(email);

		return m.matches();

	}
	
	//验证邮政编码  
    public static boolean checkPost(String post){  
        if(post.matches("[1-9]\\d{5}(?!\\d)")){  
            return true;  
        }else{  
            return false;  
        }  
    }

	// 判断是否全是数字

	public static boolean isNumeric(String str) {

		Pattern pattern = Pattern.compile("[0-9]*");

		Matcher isNum = pattern.matcher(str);

		if (!isNum.matches()) {

			return false;

		}

		return true;

	}
	
	/**
	 * 判断一个图片地址字符串是包含http协议标识
	 * @return
	 */
   public static String fixImageUrl(String url){
	   if(url.contains("http")){
		   return url;
	   }else {
		return URLs.IMAGE_URL + url;
	}
   }

	/**
	 * 将手机号码中间几位替换成*
	 * @param phoneNum
	 * @return
	 */
	public static String changePhoneForrmat(String phoneNum){
		if(TextUtils.isEmpty(phoneNum)){
			return null;
		}else if(phoneNum.length()<11){
			return null;
		}
		String forrmatNum="";
		int leghth=phoneNum.length();
		String start=phoneNum.substring(0, 2);
		String end=phoneNum.substring(leghth-2,leghth);
		forrmatNum=start+"******"+end;
		return forrmatNum;
	}

	/**
	 * 将邮箱名2位以后的替换成*
	 * @param email
	 * @return
	 */
	public static String changeEmailForrmat(String email){
		if(TextUtils.isEmpty(email)){
			return null;
		}
		if(!email.contains("@")){
			return null;
		}
		String forrmatNum="";
		int index=email.indexOf("@");
		String end=email.substring(index, email.length());
		String start="";
		StringBuffer sb=new StringBuffer();
		if(index<3){
			for (int i=0;i<6-index;i++){
				sb.append("*");
			}
			start=email.substring(0,index)+sb;
		}else{
			sb.append("****");
			start=email.substring(0,2)+sb;
		}
		forrmatNum=start+end;
		return forrmatNum;
	}

}
