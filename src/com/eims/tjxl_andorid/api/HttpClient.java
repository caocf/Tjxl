package com.eims.tjxl_andorid.api;
import java.util.HashMap;
import java.util.Iterator;
import loopj.android.http.AsyncHttpClient;
import loopj.android.http.AsyncHttpResponseHandler;
import loopj.android.http.RequestParams;
import com.eims.tjxl_andorid.constans.Constans;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.utils.Des3;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.SignaturesUtils;

public class HttpClient {
	private final static String TAG = "HttpClient";
	/**
	 * 定义一个异步网络客户端 默认超时未10秒 当超过，默认重连次数为5次 默认最大连接数为10个
	 */
	private static final int TIMEOUT_SECOND = 10000;
	private static AsyncHttpClient mClient = null;
	static {
		/* 设置连接和响应超时时间 */
		mClient = new AsyncHttpClient();
		mClient.setTimeout(TIMEOUT_SECOND);
	}

	/**
	 * @param url
	 *            API 地址
	 * @param mHandler
	 *            数据加载状态回调
	 */
	public static void post(String url, AsyncHttpResponseHandler mHandler) {

		post(url, null, mHandler);
	}

	/**
	 * post 请求
	 * 
	 * @param url
	 *            API 地址
	 * @param params
	 *            请求的参数
	 * @param mHandler
	 *            数据加载状态回调
	 */
	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler mHandler) {
		/* 将参数顺序传递 */
		if (params != null) {
			LogUtil.i(TAG, "发起post请求:" + url + "?" + params.toString());
		} else {
			LogUtil.i(TAG, "发起post请求:" + url);
		}
		mClient.post(url, params, mHandler);
	}

	/**
	 * post 请求(加密)
	 * 
	 * @param url
	 *            API 地址
	 * @param params
	 *            请求的参数
	 * @param mHandler
	 *            数据加载状态回调
	 */
	public static void mPost(String url, RequestParams params,
			AsyncHttpResponseHandler mHandler) {
		if (params != null) {
			try {
//				//遍历参数进行参数值编码处理，解决请求中文乱码问题
//				LogUtil.i(TAG, "加密前的值---->" + params.toString());
//				HashMap<String ,String> map= params.strParams;
//				Iterator it=map.keySet().iterator();
//				while (it.hasNext()){
//					String key=(String)it.next();
//					String value=map.get(key);
//					String resetValue=Des3.encode(value);
//					params.put(key,resetValue);
//
//				}
//				LogUtil.i(TAG, "发起前对参数值进行加密处理的值---->" + params.toString());
				// mkey的出处------------------------------------------------
				String mKey = SignaturesUtils.getSignData(params.strParams);
				// LogUtil.i(TAG, "keys:" + keys);
				// String mKey = CryptionUtil.md5(keys.trim());
				params.put("mKey", mKey + Constans.KEYS);
				// params.put("", Constans.KEYS);
				LogUtil.e(TAG, "mKey:" + mKey);
			//	System.out.println(url);
				LogUtil.i(TAG, "发起post请求2:" + url + "?" + params.toString());
				mClient.post(url, params, mHandler);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 注册
	 */
	public static void getRegister(AsyncHttpResponseHandler mHandler,
			RequestParams params) {
		mPost(URLs.REGISTER, params, mHandler);
	}

	/**
	 * 发送验证码
	 */
	public static void sendCode(AsyncHttpResponseHandler mHandler,
			RequestParams params) {
		mPost(URLs.SENDCODE, params, mHandler);
	}

	public static void QueryVIPNumber(AsyncHttpResponseHandler mHandler,
			RequestParams params) {
		mPost(URLs.IQUERYRIGESTEDCOUNT, params, mHandler);
	}

	public static void QueryVIPList(AsyncHttpResponseHandler mHandler,
			RequestParams params) {
		mPost(URLs.QUERY_VIPLIST, params, mHandler);
	}

	public static void doLogin(AsyncHttpResponseHandler mHandler,
			RequestParams params) {
		mPost(URLs.DO_LOGIN, params, mHandler);
	}

	public static void QueryHome(AsyncHttpResponseHandler mHandler,
			RequestParams params) {
		mPost(URLs.HOME_DATA, params, mHandler);
	}

	public static void QueryGoodDetail(AsyncHttpResponseHandler mHandler,
			RequestParams params) {
		mPost(URLs.GOOD_DETAIL, params, mHandler);
	}

	/* 搜索商品 */
	public static void LoadSearchProduct(AsyncHttpResponseHandler mHandler,
			RequestParams params) {
		mPost(URLs.SEARCH_PRODUCT, params, mHandler);
	}

		 /***查询购买记录**/
		 public static void QueryGoodBuyRecords(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.BUY_GOOD_RECORDS,params,mHandler);
		 }
		 /***省市区**/
		 public static void QueryPrvionceCity(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.QUERY_ADDRESS,params,mHandler);
		 }
		 
		 public static void QueryYunFei(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.QUERY_YUNFEI,params,mHandler);
		 }
		 //加入购物车
		 public static void addShopCart(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.ADD_SHOPCART,params,mHandler);
		 }
		 //查询购物车
		 public static void QueryShopCart(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.QUERY_SHOPCART_LIST,params,mHandler);
		 }
		 //修改购物车
		 public static void Modfiy_ShopCart(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.MODFIY_SHOPCART,params,mHandler);
		 }
		 //购物车结算
		 public static void Commint_ShopCart(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.COMMINT_SHOPCART,params,mHandler);
		 }
		 //新增收货地址
		 public static void Add_Adress(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.ADD_ADRESS,params,mHandler);
		 }
		 //查询收货地址
		 public static void Query_Adress(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.QUERY_user_ADRESS,params,mHandler);
		 }
		 //修改收货地址
		 public static void Modfiy_Adress(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.MODFIY_ADRESS,params,mHandler);
		 }
		 //删除收货地址
		 public static void Del_Adress(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.DELETE_ADRESS,params,mHandler);
		 }
		 public static void Set_defaultAdress(AsyncHttpResponseHandler mHandler, RequestParams params) {
			 mPost(URLs.SET_DEFADDRESS,params,mHandler);
		 }

	/*特价专区*/
	public static void iQuerySpCommodity(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.IQUERY_SPCOMMODITY, params, mHandler);
	}
	/*鞋样推荐*/
	public static void iQuerySmCommodity(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IQUERY_SMCOMMODITY, params, mHandler);
	}
	/*热销商品*/
	public static void iQueryHtCommodity(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.IQUERY_HTCOMMODITY, params, mHandler);
	}
	/*品牌馆*/
	public static void iQueryBrandStorePage(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.IQUERY_BRAND_STORE_PAGE, params, mHandler);
	}
	/*明星厂家接口*/
	public static void iQueryStarFactoryPage(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.iQuery_Star_Factory_Page, params, mHandler);
	}
	/*厂家首页信息*/
	public static void loadFactoryMessage(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.FACTORY_MESSAGE, params, mHandler);
	}
	/*厂家热销产品*/
	public static void loadFactoryHatSaleMessage(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.FACTORY_HOT_SALE, params, mHandler);
	}
	
	/*添加收藏*/
	public static void AddCollection(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.FACTORY_ADD_COLLECTION, params, mHandler);
	}
	/*取消收藏*/
	public static void CancelCollection(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.FACTORY_CANCEL_COLLECTION, params, mHandler);
	}
	/*获取会员中心数据*/
	public static void iQueryUserCenterInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IQUERY_USER_CENTER_INFO, params, mHandler);
	}
	
	/*厂家商品*/
	public static void loadFactoryProductMessage(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.FACTORY_PRODUCT_SALE, params, mHandler);
	}
	
	/*获取店铺分类*/
	public static void loadFactoryClassifyMessage(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.FACTORY_CLASSIFY_MESSAGE, params, mHandler);
	}
	/*店铺个人中心*/
	public static void iQueryUserStoreInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IQUERY_USER_STORE_INFO, params, mHandler);
	}
	/*修改店铺会员资料*/
	public static void iUpdateStoreUserInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IUPDATE_STORE_USER_INFO, params, mHandler);
	}

	public static void Commint_order(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {

    mPost(URLs.COMMINT_ORDER, params, mHandler);
	}

	/*修改店铺会员资料*/
	public static void iAppImgUpload(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IAPP_IMG_UPLOAD, params, mHandler);
	}
	
	public static void query_order(ListResponseHandler mHandler,
			 RequestParams params) {
   mPost(URLs.QUERY_ORDER, params, mHandler);
	}

	public static void query_order_info(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {
   mPost(URLs.QUERY_ORDER_INFO, params, mHandler);
	}
	public static void Order_Cancel(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {
  mPost(URLs.ORDER_CANCEL, params, mHandler);
	}
	//订单确认收货
	public static void Order_Result_Okl(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {
 mPost(URLs.ORDER_OK, params, mHandler);
	}
	/*查看 物流*/
	public static void Order_Logistis(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {
    mPost(URLs.ORDER_LOGISTIS, params, mHandler);
	}
	


	/*展厅列表分页*/
	public static void iQueryExhibitionPage(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IQUERY_EXHIBITION_PAGE, params, mHandler);
	}

	/*展厅详情*/
	public static void iQueryExhibitionInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IQUERY_EXHIBITION_INFO, params, mHandler);
	}

	/*查询是否收藏*/
	public static void IQUERY_IS_COLLECTED(AsyncHttpResponseHandler mHandler,
			RequestParams params) {

		mPost(URLs.FACTORY_IS_COLLECTED, params, mHandler);
	}
	
	/*获取收藏店铺信息*/
	public static void loadFactoryCollectionInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.FACTORY_COLLECTION_MESSAGE, params, mHandler);
	}
	
	/*获取收藏商品信息*/
	public static void loadProductCollectionInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.PRODUCT_COLLECTION_MESSAGE, params, mHandler);
	}
	
	/*提交反馈信息*/
	public static void commitFeedbackMessage(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.COMMIT_FEEDBACK, params, mHandler);
	}

	/*系统消息列表分页*/
	public static void iQueryLetterPage(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IQUERY_LETTER_PAGE, params, mHandler);
	}

	/*系统消息详情*/
	public static void iQueryLetterInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IQUERY_LETTER_INFO, params, mHandler);
	}
	
	/*获取关于我们页面信息*/
	public static void loadAboutUsInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryAboutUs, params, mHandler);
	}
	
	/*获取使用帮助页面信息*/
	public static void loadUseGuiderInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryUseHelp, params, mHandler);
	}
	/*获取功能介绍页面信息*/
	public static void loadFunctionIntroInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryFunIntr, params, mHandler);
	}
	
	/*获取客服热线页面信息*/
	public static void loadOnlineServiceInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryCostumePhone, params, mHandler);
	}

	/*判断用户名是否存在*/
	public static void iIsExistByUserName(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.ISEXIST_BY_USER_NAME, params, mHandler);
	}

	/*验证身份*/
	public static void iFindPwdOne(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.FIND_PWD_ONE, params, mHandler);
	}

	/*修改密码*/
	public static void iFindPwdSave(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.FIND_PWD_SAVE, params, mHandler);
	}	

	/*首页分类搜索*/
	public static void loadGlobalCatogeryInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryCommodityCategory, params, mHandler);
	}
	/*填写申诉内容*/
	public static void iAppAccountAppeal(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IAPP_ACCOUNT_APPEAL, params, mHandler);
	}
	/*查看申诉结果*/
	public static void iIsExitsByCodePwd(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.ISEXITS_BYCODE_PWD, params, mHandler);
	}
	//查询退货订单
	public static void QueryReturn(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {

mPost(URLs.iQueryReturn_Goods, params, mHandler);
}	
	public static void CancelReturn(AsyncHttpResponseHandler mHandler,
		 RequestParams params) {

    mPost(URLs.CancelReturn_GoodsSQ, params, mHandler);
}
	
	public static void QueryReplaceOrder(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {

	    mPost(URLs.Query_ReplaceOrder, params, mHandler);
	}
	
	/*提交退货（退款）申请*/
	public static void uploadRefundApply(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iAddReturn, params, mHandler);
	}
	
	/*申请退款之前，获取订单信息*/
	public static void loadRefundApplyInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iApplyReturnCom, params, mHandler);
	}

	/*修改密码*/
	public static void iAlertPwdSave(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IALERT_PWDSAVE, params, mHandler);
	}

	/*获取退货详情信息*/
	public static void loadRefundDetailInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryReturnInfo, params, mHandler);
	}
	
	/*获取退货清单信息*/
	public static void loadRefundBillInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryReturnCom, params, mHandler);
	}
	/*获取维权列表*/
	public static void QueryWqOrderList(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.Query_ActivistListOrder, params, mHandler);
	}
	

	/*获取换货申请信息*/
	public static void loadExchangeInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iApplyReplaceCom, params, mHandler);
	}
	

	/*提交换货申请*/
	public static void uploadExchangeApply(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iAddReplace, params, mHandler);
	}
	
	/*获取换货详情*/
	public static void uploadExchangeDetailInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.buyerReplaceDetail, params, mHandler);
	}

	/*加盟厂家*/
	public static void iUserInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.IUSER_INFO, params, mHandler);
	}
	/*退货维权申请详情*/
	public static void WqReturnGoodApplyfor(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.WqReturenGood, params, mHandler);
	}
	/*提交退货维权申请*/
	public static void CommintReturnGoodWqApplyfor(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.commintRetrunGoodApplyfor, params, mHandler);
	}
	
	/*换货维权申请数据*/
	public static void iApplyUygurPowerReplace_detail(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iApplyUygurPowerReplace_detail, params, mHandler);
	}

	
	/**换货维权申请数据*/
	public static void ReturnGood_wqDetail(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.ReturnUygurPowerDetail, params, mHandler);
	}

	
	/*获取换货详情*/
	public static void loadExchangeDetailInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.buyerReplaceDetail, params, mHandler);
	}
	
	/*获取换货清单*/
	public static void loadExchangeBillInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryReplaceCom, params, mHandler);
	}
	
	/*获取可换回货品列表信息*/
	public static void loadExchangeProducts(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryComListPage, params, mHandler);
	}

	/**添加协商记录**/
	public static void addRecords(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {
		mPost(URLs.iAddRecord, params, mHandler);
	}
	/**维权退货清单列表**/
	public static void iQueryReturnDetailsCom(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {
		mPost(URLs.iQueryReturnDetailsCom, params, mHandler);
	}
	/**维权换货详情**/
	public static void iUpdateReplaceUygurPowerInit(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {
		mPost(URLs.iUpdateReplaceUygurPowerInit, params, mHandler);
	}
	/**维权换货清单**/
	public static void iQueryReplaceDetailsCom(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {
		mPost(URLs.iQueryReplaceDetailsCom, params, mHandler);
	}
	/**评论维权详情**/
	public static void iUpRviewDetail(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {
		mPost(URLs.iUpRviewDetail, params, mHandler);
	}
	/**评论维权通过**/
	public static void iUpdateBuyUygurPowerStatusOK(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {
		mPost(URLs.iUpdateBuyUygurPowerStatusOK, params, mHandler);
	}
	/**撤销维权**/
	public static void iUpdateBuyUygurPowerStatus(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {
		mPost(URLs.iUpdateBuyUygurPowerStatus, params, mHandler);
	}
	/**发票**/
	public static void iAddInvoices(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {
		mPost(URLs.iAddInvoices, params, mHandler);
	}
	/*获取物流公司名称和Id*/
	public static void loadLogicticsInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryLogisticsList, params, mHandler);
	}
	
	/*提交物流信息*/
	public static void uploadLogicticsInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iUpRejectStatus, params, mHandler);
	}

	/*修改手机号码*/
	public static void iSellerBdPhoneSd(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.ISELLER_BD_PHONE_SD, params, mHandler);
	}
	/*修改邮箱*/
	public static void iSellerBdEmailSd(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.ISELLER_BD_EMAIL_SD, params, mHandler);
	}

	/*我的购买记录*/
	public static void iQueryMyBuyedList(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {
		mPost(URLs.iQueryMyBuyedList, params, mHandler);
	}
	/*删除系统消息*/
	public static void iDelLeave(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {
		mPost(URLs.DEL_LEAVE, params, mHandler);

	}

	public static void iComCommentInit(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {

		mPost(URLs.iComCommentInit, params, mHandler);

		
	}
	/*获取PC端热搜关键字*/
	public static void loadPCHotSearchInfo(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {


		mPost(URLs.iHotSearchList, params, mHandler);

	}
	
	public static void iFindReviewById(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {

mPost(URLs.iFindReviewById, params, mHandler);

		
	}
	
	/*搜索店铺*/
	public static void loadFactorySearchResult(AsyncHttpResponseHandler mHandler,
										 RequestParams params) {

		mPost(URLs.iQueryFactoryPage, params, mHandler);
	}


	public static void iAddReview(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {

mPost(URLs.iAddReview, params, mHandler);
}
	/**购物车删除**/
	public static void iDelShopCar(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {

     mPost(URLs.iDelShopCar, params, mHandler);
}
	/**计算运费**/
	public static void iQueryFreightListByUsers(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {
     mPost(URLs.iCalFreight, params, mHandler);
}
	/**初始化发票**/
	public static void iQueryInvoicesByuid(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {
     mPost(URLs.iQueryInvoicesByuid, params, mHandler);
   }
	/**Banner 图**/
	public static void iQueryBannerList(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {
     mPost(URLs.iQueryBannerList, params, mHandler);
   }
	/**服务协议**/
	public static void iRegistAgreement(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {
     mPost(URLs.iRegistAgreement, params, mHandler);
   }
	/**换货取消申请**/
	public static void CancelReplace_GoodsSQ(AsyncHttpResponseHandler mHandler,
			 RequestParams params) {
     mPost(URLs.CancelReplace_GoodsSQ, params, mHandler);
   }
}

