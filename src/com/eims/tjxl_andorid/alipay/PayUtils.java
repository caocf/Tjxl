/**
 * 
 */
package com.eims.tjxl_andorid.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.alipay.sdk.app.PayTask;


import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.ui.shopcart.OrderDetailActivity;
import com.eims.tjxl_andorid.ui.shopcart.PayMentActivity;
import com.eims.tjxl_andorid.utils.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年6月30日 上午9:56:49
 *************************************************************************** 
 */
public class PayUtils {

	// 商户PID
	public static final String PARTNER = "2088201257951992";
	// 商户收款账号
	public static final String SELLER = "e-shopping@doov.com.cn";
	/**** 商户私钥，openssl自助生成 */
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMF7PW55sjxx0AP3sM5esbrXMeE6sQQBvLRNLpI+W3k3eJ1uTAUWruOrsG2ICiFcUuOfy+ZqPj49hnv6jWyUFYbao/Ajl9lpAieZkna0J+hdadHcbdTiRg7gj+tsdAKlyEs6TTPmvYZO70PYFxBMCQgnEbPJ0T9JSr/bC24Sc6B/AgMBAAECgYAQB9wBKUghRfTMP1uA3cuwBWB2ntxHzrUJ41M2fsApfPUbZiYaTdTTvEfz60+bkAC2J2lHgAoEMijQYOQS2Pone1ftCY/neMbI+wSHZmt/fal1emhyv3gsDPbGG50F4Fvn5f0Q5R36lSPKRYcanX5ejljlRKGBpB/2sWSelEB2IQJBAOGrLxRHZKNJMCfcMQcQJRh9C4SoFYyJ1vv47OtQMvRCFlVXkWPE3Q9VReT+xzIJguuX91h+4Wu9nMRCEs5HDAkCQQDbfI4SkfCLWCQlvJHprfj13UHezPQMJRevp1mqYtJc3HiBiAbsQM7RW5lfjxXflemGLgngvLbMmPqrI3//q3pHAkEAkXAe5mXgoS+g/n91hYTNM4wbaUJeqPWI/Vl/b2R/glY5SIUoKXGNur0NT7k3rbLdknKdMYaMR0CrkPy4XAMD6QJBALExANFlGDimU6hTb8ijNpXmpcLk2zDi0RhxliXD2eg15ONNmakpOM4zkMJglVwHfptgl4DjWQAbkGKRKkiRewkCQEZlq4WVBVnt4rTgDgJOUQ6DIcnmTPzDQ3w9oPQhlby9bC+UDCtXc6EGtfNP1XaLAS1v/IBWNbOV9FHyqFcARDA=";
	/***** 支付宝公钥，支付宝demo提供 */
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;
	private static final String TAG = "PayUtils";

	private Activity context;
	private String type;

	public PayUtils(Activity context,String type) {
		this.context = context;
		this.type=type;
	}
	
	

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					// Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
					 if(context instanceof PayMentActivity){
						 ((PayMentActivity)context).paymentSuccess();
					 }else if(context instanceof OrderDetailActivity){
						 ((OrderDetailActivity)context).PayMentSuccess();
					 }
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT)
								.show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT)
								.show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(context, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT)
						.show();
				break;
			}
			default:
				break;
			}
		};
	};
	
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay() {
		// 订单
		String orderInfo = getOrderInfo("商品名称", "商品详情", "0.01");
        LogUtil.d(TAG, "支付宝："+orderInfo);
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(context);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	
	

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

//		// 服务器异步通知页面路径
//		orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
//				+ "\"";
		if("1".equals(type)){//从购物车 --订单 --支付
			orderInfo += "&notify_url=" + "\"" + URLs.SERVER_URL+"iAlipayAfterBlack.do"
					+ "\"";
		}else{
			//从 订单列表  即订单详细请
			orderInfo += "&notify_url=" + "\"" + URLs.ORDER_ZFB
					+ "\"";
		}
		

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	
	
	
	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(context);
		String version = payTask.getVersion();
		Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
	}


	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
	

}
