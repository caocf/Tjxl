package com.eims.tjxl_andorid.constans;
/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月23日  上午11:29:04
 *************************************************************************** 
 */
public class URLs {
	/*雷云*/
//	public static   String SERVER_URL="http://192.168.3.65:8080/xlw/";
//	public static   String IMAGE_URL="http://192.168.3.65:8080/";

	/*杨福先*/
//	public static   String SERVER_URL="http://192.168.3.66:8080/xlw/";
//	public static   String IMAGE_URL="http://192.168.3.66:8080/";

	/*测试*/
//	public static   String SERVER_URL="http://d3.java.shovesoft.com/xlw/";
//	public static   String IMAGE_URL="http://d3.java.shovesoft.com/";
	
	/*正式站点*/
	public static   String SERVER_URL="http://www.xlw999.com/";
	public static   String IMAGE_URL="http://www.xlw999.com/";
	
	public static   String REGISTER=SERVER_URL+"iRegist.do";
	public static   String LOGIN=SERVER_URL+"iLogin.do";
	public static   String SENDCODE=SERVER_URL+"iSendPhoneCode.do";
	public static   String REGISTER_LIST=SERVER_URL+"iRegistedList.do";
	
	public static   String IQUERYRIGESTEDCOUNT=SERVER_URL+"iQueryRigestedCount.do";
	
	public static   String QUERY_VIPLIST=SERVER_URL+"iRegistedListPage.do";
	
	public static   String DO_LOGIN=SERVER_URL+"iLogin.do";
	//首页
	public static   String HOME_DATA=SERVER_URL+"iQueryIndex.do";
	//商品详情
	public static   String GOOD_DETAIL=SERVER_URL+"iQueryComDetails.do";

	//购买记录
	public static   String BUY_GOOD_RECORDS=SERVER_URL+"iQueryBuyedList.do";

	public static   String SEARCH_PRODUCT = SERVER_URL+"iQueryComSearch.do";
	
	public static   String QUERY_ADDRESS = SERVER_URL+"iQueryDdd.do";
	//查询商品运费
	public static   String QUERY_YUNFEI= SERVER_URL+"iQueryFreightByCity.do";
	//加入购物车
	public static   String ADD_SHOPCART= SERVER_URL+"iAddShopCar.do";
	//查询购物车列表
   public static   String QUERY_SHOPCART_LIST= SERVER_URL+"iQueryShopCarList.do";
    // 修改购物车
   public static   String MODFIY_SHOPCART= SERVER_URL+"iUpdShopCar.do";
   //提交购物车
   public static   String COMMINT_SHOPCART= SERVER_URL+"iSubShopCar.do";
   //新增收货地址
   public static   String ADD_ADRESS= SERVER_URL+"iAddUserAddress.do";
   //收货地址列表
   public static   String QUERY_user_ADRESS= SERVER_URL+"iQueryUserAddressList.do";
   //修改地址列表
    public static   String MODFIY_ADRESS= SERVER_URL+"iUpdUserAddress.do";
    //删除地址列表
    public static   String DELETE_ADRESS= SERVER_URL+"iDelAddress.do";
    //设置为默认地址
    public static   String SET_DEFADDRESS= SERVER_URL+"iSetDefAddress.do";
    
	/*特价专区*/
	public static   String IQUERY_SPCOMMODITY = SERVER_URL+"iQuerySpCommodity.do";
	/*鞋样推荐*/
	public static   String IQUERY_SMCOMMODITY = SERVER_URL+"iQuerySmCommodity.do";
	/*热销商品*/
	public static   String IQUERY_HTCOMMODITY = SERVER_URL+"iQueryHtCommodity.do";
	/*品牌馆接口*/
	public static   String IQUERY_BRAND_STORE_PAGE = SERVER_URL+"iQueryBrandStorePage.do";
	/*明星厂家接口*/
	public static   String iQuery_Star_Factory_Page = SERVER_URL+"iQueryStarFactoryPage.do";
	/*厂家店铺信息*/
	public static   String FACTORY_MESSAGE = SERVER_URL+"iQueryStoreFactory.do";
	/*厂家热销产品*/
	public static   String FACTORY_HOT_SALE = SERVER_URL+"iQueryStoreHtCommodity.do";
	/*添加收藏*/
	public static   String FACTORY_ADD_COLLECTION = SERVER_URL+"iAddCollect.do";
	/*查询是否收藏*/
	public static   String FACTORY_IS_COLLECTED = SERVER_URL+"iQueryCollectByOId.do";
	/*获取会员中心数据*/
	public static   String IQUERY_USER_CENTER_INFO = SERVER_URL+"iQueryUserCenterInfo.do";
	/*厂家商品;分类搜索接口；厂内搜索接口*/
	public static   String FACTORY_PRODUCT_SALE = SERVER_URL+"iQueryStoreCommodity.do";
	/*店铺分类*/
	public static   String FACTORY_CLASSIFY_MESSAGE = SERVER_URL+"iQueryStoreCategory.do";
	/*修改店铺会员资料*/
	public static   String IUPDATE_STORE_USER_INFO = SERVER_URL+"iUpdateStoreUserInfo.do";
	/*店铺个人中心 */
	public static   String IQUERY_USER_STORE_INFO = SERVER_URL+"iQueryUserStoreInfo.do";

	/*提交订单 */
	public static   String COMMINT_ORDER = SERVER_URL+"iConfirmOrder.do";

	/*APP上传图片 */
	public static   String IAPP_IMG_UPLOAD = SERVER_URL+"iAppImgUpload.do";


	/*查询订单 */
	public static   String QUERY_ORDER = SERVER_URL+"iQueryOrderPage.do";
	/*订单详情 */
	public static   String QUERY_ORDER_INFO = SERVER_URL+"iQueryOrderInfo.do";
	/*订单取消 */
	public static   String ORDER_CANCEL = SERVER_URL+"iUpdStatusCancel.do";
	/*订单确认收货 */
	public static   String ORDER_OK= SERVER_URL+"iUpdStatusSub.do";
	/*查看 物流*/
	public static   String ORDER_LOGISTIS= SERVER_URL+"iQueryLogisticsInfo.do";
	/*订单支付宝回调*/
	public static   String ORDER_ZFB= SERVER_URL+"iAlipayAfterBlackOne.do";
	/*展厅列表分页 */
	public static   String IQUERY_EXHIBITION_PAGE = SERVER_URL+"iQueryExhibitionPage.do";
	/*展厅详情 */
	public static   String IQUERY_EXHIBITION_INFO = SERVER_URL+"iQueryExhibitionInfo.do";
    /*取消收藏*/
	public static   String FACTORY_CANCEL_COLLECTION = SERVER_URL+"iCancelCollect.do";
	/*店铺收藏信息 */
	public static   String FACTORY_COLLECTION_MESSAGE = SERVER_URL+"iQueryCollectShopPage.do";
	/*商品收藏信息 */
	public static   String PRODUCT_COLLECTION_MESSAGE = SERVER_URL+"iQueryCollectCommodityPage.do";
	/*系统消息列表分页 */
	public static   String IQUERY_LETTER_PAGE = SERVER_URL+"iQueryLetterPage.do";
	/*系统消息详情 */
	public static   String IQUERY_LETTER_INFO = SERVER_URL+"iQueryLetterInfo.do";
	/*提交反馈信息*/
	public static   String COMMIT_FEEDBACK = SERVER_URL + "iAddSuggest.do";
	/*判断用户名是否存在*/
	public static   String ISEXIST_BY_USER_NAME = SERVER_URL + "iIsExistByUserName.do";
	/*验证身份*/
	public static   String FIND_PWD_ONE = SERVER_URL + "iFindPwdOne.do";
	/*修改密码*/
	public static   String FIND_PWD_SAVE = SERVER_URL + "iFindPwdSave.do";
	/*关于我们*/
	public static   String iQueryAboutUs = SERVER_URL + "iQueryAboutUs.do";
	/*使用帮助*/
	public static   String iQueryUseHelp = SERVER_URL + "iQueryUseHelp.do";
	/*功能介绍*/
	public static   String iQueryFunIntr = SERVER_URL + "iQueryFunIntr.do";
	/*客服热线*/
	public static   String iQueryCostumePhone = SERVER_URL + "iQueryCostumePhone.do";
	/*首页分类搜索*/
	public static   String iQueryCommodityCategory = SERVER_URL + "iQueryCommodityCategory.do";
	/*退货列表*/
	public static   String iQueryReturn_Goods = SERVER_URL + "iQueryReturnPage.do";
	/*退货详情中改变提交物流信息*/
	public static   String iUpRejectStatus = SERVER_URL + "iUpRejectStatus.do";
	/*退货取消申请*/
	public static   String CancelReturn_GoodsSQ = SERVER_URL + "iUpRejectStatus.do";
	/*huan货取消申请*/
	public static   String CancelReplace_GoodsSQ = SERVER_URL + "iUpReplaceStatus.do";
	/*换货订单列表*/
	public static   String Query_ReplaceOrder = SERVER_URL + "iReplacePage.do";
	/*维权订单列表*/
	public static   String Query_ActivistListOrder = SERVER_URL + "iqQeryUygurPowerPage.do";
	/*提交退货（退款）申请*/
	public static   String iAddReturn = SERVER_URL + "iAddReturn.do";
	/*申请退款时，获取订单信息*/
	public static   String iApplyReturnCom = SERVER_URL + "iApplyReturnCom.do";
	
	/*填写申诉内容*/
	public static   String IAPP_ACCOUNT_APPEAL = SERVER_URL + "iAppAccountAppeal.do";
	/*查看申诉结果*/
	public static   String ISEXITS_BYCODE_PWD = SERVER_URL + "iIsExitsByCodePwd.do";
	/*退货详情*/
	public static String iQueryReturnInfo = SERVER_URL + "iQueryReturnInfo.do";
	/*退货清单*/
	public static String iQueryReturnCom = SERVER_URL + "iQueryReturnCom.do";
	/*修改密码*/
	public static String IALERT_PWDSAVE = SERVER_URL + "iAlertPwdSave.do";
	/*获取换货信息*/
	public static String iApplyReplaceCom = SERVER_URL + "iApplyReplaceCom.do";
	/*提交换货申请*/
	public static String iAddReplace = SERVER_URL + "iAddReplace.do";
	/*换货详情*/
	public static String buyerReplaceDetail = SERVER_URL + "iBuyerReplaceDetail.do";
	/*换货清单*/
	public static String iQueryReplaceCom = SERVER_URL + "iQueryReplaceCom.do";
	/*退货维权申请*/
	public static String WqReturenGood = SERVER_URL + "iApplyUygurPowerCom.do";
	/*提交维权申请*/
	public static String commintRetrunGoodApplyfor = SERVER_URL + "iAddUygurPower.do";
	/**换货维权**/
	public static String iApplyUygurPowerReplace_detail = SERVER_URL + "iApplyUygurPowerReplaceCom.do";
	/**维权详情**/
	public static String wqorder_detail = SERVER_URL + "iApplyUygurPowerReplaceCom.do";

	/**维权订单  退货维权详情**/
	public static String ReturnUygurPowerDetail = SERVER_URL + "iUpdateReturnUygurPowerInit.do";
	/**维权订单  换货维权详情**/
	public static String iUpdateReplaceUygurPowerInit = SERVER_URL + "iUpdateReplaceUygurPowerInit.do";
	/*选择换货商品*/
	public static String iQueryComListPage = SERVER_URL + "iQueryComListPage.do";
	/**维权添加协商记录**/
	public static String  iAddRecord= SERVER_URL + "iAddRecord.do";
	/**维权退货清单**/
	public static String  iQueryReturnDetailsCom= SERVER_URL + "iQueryReturnDetailsCom.do";
	/**维权换货清单**/
	public static String  iQueryReplaceDetailsCom= SERVER_URL + "iQueryReplaceDetailsCom.do";
	/**评论维权详情**/
	public static String  iUpRviewDetail= SERVER_URL + "iUpRviewDetail.do";
	/**评论维权通过**/
	public static String  iUpdateBuyUygurPowerStatusOK= SERVER_URL + "iUpdateBuyUygurPowerStatusOK.do";
	/**撤销维权**/
	public static String  iUpdateBuyUygurPowerStatus= SERVER_URL + "iUpdateBuyUygurPowerStatus.do";
	/**发票信息**/
	public static String  iAddInvoices= SERVER_URL + "iAddInvoices.do";
	/**我的购买记录**/
	public static String  iQueryMyBuyedList= SERVER_URL + "iQueryMyBuyedList.do";
	/**商品详情评论**/
	public static String  iComCommentInit= SERVER_URL + "iComCommentInit.do";
	/**订单评论详情**/
	public static String  iFindReviewById= SERVER_URL + "iFindReviewById.do";
	/**订单评论**/
	public static String  iAddReview= SERVER_URL + "iAddReview.do";
	/*加盟厂家*/
	public static String IUSER_INFO = SERVER_URL + "iUserInfo.do";
	/*获取物流公司信息*/
	public static String iQueryLogisticsList = SERVER_URL + "iQueryLogisticsList.do";
	/*退货换货之后提交物流信息*/
	public static String iQueryReturnPage = SERVER_URL + "iQueryReturnPage.do";
	/*修改手机号码*/
	public static String ISELLER_BD_PHONE_SD = SERVER_URL + "iSellerBdPhoneSd.do";
	/*修改邮箱*/
	public static String ISELLER_BD_EMAIL_SD = SERVER_URL + "iSellerBdEmailSd.do";
	/*删除系统消息*/
	public static String DEL_LEAVE = SERVER_URL + "iDelLeave.do";
	/*获取PC端搜索关键字*/
	public static String iHotSearchList = SERVER_URL + "iHotSearchList.do";
	/*首页搜索店铺*/
	public static String iQueryFactoryPage = SERVER_URL + "iQueryFactoryPage.do";
	/*购物车删除*/
	public static String iDelShopCar = SERVER_URL + "iDelShopCar.do";
	/*计算运费*/
	public static String iCalFreight = SERVER_URL + "iCalFreight.do";
	/*发票初始化*/
	public static String iQueryInvoicesByuid = SERVER_URL + "iQueryInvoicesByuid.do";
	/*Banner图*/
	public static String iQueryBannerList = SERVER_URL + "iQueryBannerList.do";
	/*服务协议*/
	public static String iRegistAgreement = SERVER_URL + "iRegistAgreement.do";
}
