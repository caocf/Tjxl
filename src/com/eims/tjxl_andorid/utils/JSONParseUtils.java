package com.eims.tjxl_andorid.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.constans.URLs;
import com.eims.tjxl_andorid.entity.ClassifyRankBean;
import com.eims.tjxl_andorid.entity.FactoryCollectionBean;
import com.eims.tjxl_andorid.entity.FactoryMessageBean;
import com.eims.tjxl_andorid.entity.FilterBrandItemBean;
import com.eims.tjxl_andorid.entity.FilterBrandSelectItemBean;
import com.eims.tjxl_andorid.entity.FilterCatogeryItemBean;
import com.eims.tjxl_andorid.entity.FilterCatogerySelectItemBean;
import com.eims.tjxl_andorid.entity.FilterItemBean;
import com.eims.tjxl_andorid.entity.FilterPropertyItemBean;
import com.eims.tjxl_andorid.entity.FilterPropertySelectItemBean;
import com.eims.tjxl_andorid.entity.FilterSelectItem;
import com.eims.tjxl_andorid.entity.LogicticsCampanyBean;
import com.eims.tjxl_andorid.entity.OrderDetailBean;
import com.eims.tjxl_andorid.entity.ProductBean;
import com.eims.tjxl_andorid.entity.ProductCollectionBean;
import com.eims.tjxl_andorid.entity.OrderDetailBean.OrderGoodBean;
import com.eims.tjxl_andorid.entity.RefundGoodBean;
import com.eims.tjxl_andorid.entity.RefundGoodSizeBean;
import com.eims.tjxl_andorid.entity.StarFactoryBean;
import com.google.gson.Gson;

/**
 * Json解析工具，处理简单的解析
 * 
 * @author 刘远祺
 * 
 */
public class JSONParseUtils {
	public static final String TAG = "JsonParser";

	/**
	 * 解析一个key值(方便 解析状态值)
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static String getString(String json, String key) {
		try {
			JSONObject obj = new JSONObject(json);
			return obj.optString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析一个key值(方便 解析状态值)
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static String getString(JSONObject jsonObj, String key) {
		try {
			return jsonObj.optString(key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

		return null;
	}

	/**
	 * 解析一个key值(方便 解析状态值)
	 * 
	 * @param json
	 * @param key
	 * @return
	 */
	public static int getInt(String json, String key) {
		try {
			JSONObject obj = new JSONObject(json);
			return obj.optInt(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;

		return -1;
	}

	/**
	 * 判断http返回的json是否为一个正常的返回值
	 * 
	 * @param content
	 * @return
	 */
	public static boolean isErrorJSONResult(String content) {

		if (!TextUtils.isEmpty(content)) {
			int type = getInt(content, "type");
			return type != 1;
		}
		return true;
	}

	/**
	 * 返回key对应的jsonObject
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public static String getJSONObject(String content, String key) {
		try {
			JSONObject obj = new JSONObject(content);
			return obj.optJSONObject(key).toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 返回key对应的jsonArray
	 * 
	 * @param content
	 * @param key
	 * @return
	 */
	public static JSONArray getJSONArray(String content, String key) {
		try {
			JSONObject obj = new JSONObject(content);
			JSONArray array = obj.getJSONArray(key);
			return array;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解析商品结果
	 */
	public static List<ProductBean> parseProductBeans(String content) {
		List<ProductBean> beans = new ArrayList<ProductBean>();
		ProductBean bean = null;
		try {
			JSONObject object = new JSONObject(content);
			if (!object.has("data")) {
				Log.d(TAG,"parseProductBeans no data tag");
				return beans;
			}
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				bean = new ProductBean();
				object = array.getJSONObject(i);
				bean.setImage(object.getString("main_img_m"));
				bean.setName(object.getString("brand_name"));
				bean.setPrice(object.getString("price"));
				bean.setSprice(object.getString("sprice"));
				bean.setCommodity_code(object.getString("commodity_code"));
				bean.setCommodity_name(object.getString("commodity_name"));
				bean.setMin_batch(object.getString("min_batch"));
				bean.setId(object.getString("id"));
				bean.setSpec_name(object.getString("spec_name"));
				Log.d(TAG, "has key total_sell:" + object.has("total_sell"));
				if (object.has("total_sell")) {
					bean.setSaleNumber(object.optString("total_sell"));
					Log.d(TAG, "total_sell = " + object.optString("total_sell"));
				}
				beans.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(AppContext.appContext, "数据解析错误", Toast.LENGTH_SHORT)
					.show();
			Log.d(TAG, "parseProductBeans error : = " + e);
		}
		Log.d(TAG, "parseProductBeans size = " + beans.size());
		return beans;
	}

	public static List<FilterItemBean> ParseFilterItems(String content) {
		List<FilterItemBean> datasBeans = new ArrayList<FilterItemBean>();
		try {
			JSONObject object = new JSONObject(content);
			datasBeans.add(ParseFilterBrandItems(object
					.getJSONArray("brandList")));
			datasBeans.add(ParseFilterCatogeryItems(object
					.getJSONArray("caList")));
			datasBeans.addAll(ParseFilterPropertyItems(object
					.getJSONArray("proList")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datasBeans;
	}

	public static FilterItemBean ParseFilterCatogeryItems(JSONArray array) {
		FilterCatogeryItemBean bean = new FilterCatogeryItemBean();
		bean.setItem_title("分类");

		FilterCatogerySelectItemBean item = new FilterCatogerySelectItemBean();
		item.setId("0");
		item.setName("无限");

		bean.items.add(item);
		try {
			for (int i = 0; i < array.length(); i++) {
				JSONObject object = array.getJSONObject(i);
				item = new FilterCatogerySelectItemBean();
				item.setCategory_name(object.getString("category_name"));
				item.setName(object.getString("category_name"));
				item.setId(object.getString("id"));
				item.setRank(object.getInt("rank"));
				bean.items.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "parse catogery error msg:" + e);
		}
		return bean;
	}

	public static FilterItemBean ParseFilterBrandItems(JSONArray array) {
		FilterBrandItemBean bean = new FilterBrandItemBean();
		bean.setItem_title("品牌");

		FilterBrandSelectItemBean item = new FilterBrandSelectItemBean();
		item.setId("0");
		item.setName("无限");
		bean.items.add(item);
		try {
			for (int i = 0; i < array.length(); i++) {
				item = new FilterBrandSelectItemBean();
				JSONObject object = array.getJSONObject(i);
				item.id = object.getString("brands_id");
				item.name = object.getString("brands_name");
				bean.items.add(item);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return bean;
	}

	public static List<FilterItemBean> ParseFilterPropertyItems(JSONArray array) {

		List<FilterItemBean> datasBeans = new ArrayList<FilterItemBean>();
		try {
			Log.d(TAG, "property item num = " + array.length());
			for (int i = 0; i < array.length(); i++) {
				FilterPropertySelectItemBean item = new FilterPropertySelectItemBean();
				FilterPropertyItemBean bean = new FilterPropertyItemBean();
				JSONObject object = array.getJSONObject(i);
				bean.item_id = object.getString("id");
				bean.item_title = object.getString("name");

				item.setId("0");
				item.setName("无限");
				bean.items.add(item);
				JSONArray array2 = object.getJSONArray("daList");
				for (int j = 0; j < array2.length(); j++) {
					item = new FilterPropertySelectItemBean();
					object = array2.getJSONObject(j);
					item.setId(object.getString("id"));
					item.setName(object.getString("property_value"));
					Log.d(TAG,
							"ParseFilterPropertySelectItems:property_value = "
									+ item.name);

					item.setUncode(object.getString("uncode"));
					bean.items.add(item);
					Log.d(TAG, "ParseFilterPropertySelectItems:name = "
							+ bean.items.get(j).name);
				}
				datasBeans.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return datasBeans;
	}

	/**
	 * 获取店铺信息
	 * 
	 * @param object
	 * @return
	 */
	public static FactoryMessageBean parseFactoryMessage(String content) {
		FactoryMessageBean bean = new FactoryMessageBean();
		try {
			JSONObject object = new JSONObject(content);
			object = object.getJSONObject("data");
			bean.setMiao_a(object.getString("miao_a"));
			bean.setId(object.getString("uid"));
			bean.setWuliu_a(object.getString("wuliu_a"));
			bean.setWuliu(object.getString("wuliu"));
			bean.setFuwu(object.getString("fuwu"));
			bean.setFuwu_a(object.getString("fuwu_a"));
			bean.setMiao(object.getString("miao"));
			bean.setAddress(object.getString("address"));
			bean.setFactory_name(object.getString("factory_name"));
			bean.setHead_img(object.getString("head_img"));
			bean.setQq(object.getString("qq"));
			Log.d(TAG, "parseFactoryMessage name = " + bean.getFactory_name()
					+ "icon url = " + bean.getHead_img());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "parseFactoryMessage parse error :" + e);
			e.printStackTrace();
		}

		return bean;
	}

	/**
	 * 解析首页分类搜索-1级搜索列表
	 * 
	 * @param content
	 * @return
	 */
	public static List<ClassifyRankBean> parserGlobalClassifyRank1(
			String content) {
		List<ClassifyRankBean> list = new ArrayList<ClassifyRankBean>();

		try {
			JSONObject object = new JSONObject(content);
			ClassifyRankBean bean = null;
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				object = array.getJSONObject(i);
				bean = new ClassifyRankBean();
				bean.setId(object.getString("id"));
				bean.setRank(object.getString("rank"));
				bean.setCategory_name(object.getString("category_name"));
				bean.setClassifyBeansRank2(parserGlobalClassifyRank2(object
						.getJSONArray("twoList")));
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "parserGlobalClassifyRank1 error = " + e);
		}
		Log.d(TAG, "parserGlobalClassifyRank1 size = " + list.size());
		return list;
	}

	/**
	 * 解析首页分类搜索-2级搜索列表
	 * 
	 * @param content
	 * @return
	 */
	public static List<ClassifyRankBean> parserGlobalClassifyRank2(
			JSONArray array) {
		List<ClassifyRankBean> list = new ArrayList<ClassifyRankBean>();
		ClassifyRankBean bean = null;
		try {
			JSONObject object = null;
			for (int i = 0; i < array.length(); i++) {
				object = array.getJSONObject(i);
				bean = new ClassifyRankBean();
				bean.setId(object.getString("id"));
				bean.setRank(object.getString("rank"));
				bean.setCategory_name(object.getString("category_name"));
				bean.setClassifyBeansRank2(parserGlobalClassifyRank3(object
						.getJSONArray("teeList")));
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "parserGlobalClassifyRank2 error = " + e);
		}
		Log.d(TAG, "parserGlobalClassifyRank2 size = " + list.size());
		return list;
	}

	/**
	 * 解析首页分类搜索-3级搜索列表
	 * 
	 * @param content
	 * @return
	 */
	public static List<ClassifyRankBean> parserGlobalClassifyRank3(
			JSONArray array) {
		List<ClassifyRankBean> list = new ArrayList<ClassifyRankBean>();
		ClassifyRankBean bean = null;
		try {
			JSONObject object = null;
			for (int i = 0; i < array.length(); i++) {
				object = array.getJSONObject(i);
				bean = new ClassifyRankBean();
				bean.setId(object.getString("id"));
				bean.setRank(object.getString("rank"));
				bean.setCategory_name(object.getString("category_name"));
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "parserGlobalClassifyRank3 error = " + e);
		}
		Log.d(TAG, "parserGlobalClassifyRank3 size = " + list.size());
		return list;
	}

	public static List<ClassifyRankBean> parserClassifyRankBeans1(String content) {
		List<ClassifyRankBean> list = new ArrayList<ClassifyRankBean>();
		JSONObject object;
		Log.d(TAG, "店铺分类搜索Json 信息：" + content);
		try {
			object = new JSONObject(content);
			JSONArray array = object.getJSONArray("data");
			ClassifyRankBean bean = null;
			for (int i = 0; i < array.length(); i++) {
				object = array.getJSONObject(i);
				bean = new ClassifyRankBean();
				bean.setId(object.getString("id"));
				bean.setRank(object.getString("rank"));
				bean.setCategory_name(object.getString("category_name"));
				bean.setPid(object.getString("pid"));
				bean.setClassifyBeansRank2(parserClassifyRankBeans2(object
						.getJSONArray("twoList")));
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "parserClassifyRankBeans size = " + list.size());
		return list;

	}

	public static List<ClassifyRankBean> parserClassifyRankBeans2(
			JSONArray array) {
		List<ClassifyRankBean> list = new ArrayList<ClassifyRankBean>();
		JSONObject object;
		try {
			ClassifyRankBean bean = null;
			for (int i = 0; i < array.length(); i++) {
				object = array.getJSONObject(i);
				bean = new ClassifyRankBean();
				bean.setId(object.getString("id"));
				bean.setRank(object.getString("rank"));
				bean.setCategory_name(object.getString("category_name"));
				bean.setPid(object.getString("pid"));
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "parserClassifyRankBeans size = " + list.size());
		return list;
	}

	/**
	 * /** 解析分类筛选信息
	 * 
	 * @param content
	 * @return
	 */
	public static List<ClassifyRankBean> parserClassifyRankBeans(String content) {
		List<ClassifyRankBean> list = new ArrayList<ClassifyRankBean>();
		List<ClassifyRankBean> listTemp = new ArrayList<ClassifyRankBean>();
		JSONObject object;
		Log.d(TAG, "店铺分类搜索Json 信息：" + content);
		try {
			object = new JSONObject(content);
			JSONArray arrayFirst = object.getJSONArray("data");
			JSONArray arraySecond = null;
			ClassifyRankBean beanRankFirst = null;
			ClassifyRankBean beanRankSecond = null;
			for (int i = 0; i < arrayFirst.length(); i++) {
				object = arrayFirst.getJSONObject(i);
				beanRankFirst = new ClassifyRankBean();
				beanRankFirst.setId(object.getString("id"));
				beanRankFirst.setRank(object.getString("rank"));
				beanRankFirst.setCategory_name(object
						.getString("category_name"));
				beanRankFirst.setPid(object.getString("pid"));
				arraySecond = object.getJSONArray("twoList");
				listTemp.clear();
				for (int j = 0; j < arraySecond.length(); j++) {
					object = arraySecond.getJSONObject(j);
					beanRankSecond = new ClassifyRankBean();
					beanRankSecond = new ClassifyRankBean();
					beanRankSecond.setId(object.getString("id"));
					beanRankSecond.setRank(object.getString("rank"));
					beanRankSecond.setCategory_name(object
							.getString("category_name"));
					beanRankSecond.setPid(object.getString("pid"));
					listTemp.add(beanRankSecond);
				}
				beanRankFirst.setClassifyBeansRank2(listTemp);
				list.add(beanRankFirst);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "parserClassifyRankBeans size = " + list.size());
		return list;

	}

	/**
	 * 解析收藏商品信息
	 * 
	 * @param content
	 * @return
	 */
	public static List<ProductCollectionBean> parseProductCollectionInfo(
			String content) {
		List<ProductCollectionBean> list = new ArrayList<ProductCollectionBean>();
		try {
			JSONArray array = new JSONObject(content).getJSONArray("data");
			ProductCollectionBean bean = null;
			for (int i = 0; i < array.length(); i++) {
				bean = new ProductCollectionBean();
				JSONObject object = array.getJSONObject(i);
				bean.setId(object.optString("id"));
				bean.setName(object.optString("commodity_name"));
				bean.setSale_count(object.optString("total_sell"));
				bean.setIcon(object.optString("main_img_m"));
				bean.setPrice(object.optString("price"));
				bean.setIs_sell(object.optString("is_sell"));
				bean.setCommodity_id(object.optString("commodity_id"));
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "ProductCollectionInfo size = " + list.size());
		return list;
	}

	/**
	 * 解析收藏店铺信息
	 * 
	 * @param content
	 * @return
	 */
	public static List<FactoryCollectionBean> parseFactoryCollectionInfo(
			String content) {
		List<FactoryCollectionBean> list = new ArrayList<FactoryCollectionBean>();
		try {
			JSONArray array = new JSONObject(content).getJSONArray("data");
			FactoryCollectionBean bean = null;
			for (int i = 0; i < array.length(); i++) {
				bean = new FactoryCollectionBean();
				JSONObject object = array.getJSONObject(i);
				bean.setId(object.getString("id"));
				bean.setName(object.getString("f_factory_name"));
				bean.setScore(object.getString("fuwu"));
				bean.setIcon(object.getString("head_img"));
				bean.setSeller_id(object.getString("seller_id"));
				JSONArray array2 = object.getJSONArray("comList");
				Log.d(TAG, "Factory name = " + bean.getName() + " ,icon = "
						+ bean.getIcon() + ",fuwu = " + bean.getScore());
				for (int j = 0; j < array2.length(); j++) {
					ProductCollectionBean bean2 = new ProductCollectionBean();
					object = array2.getJSONObject(j);
					bean2.setId(object.getString("id"));
					bean2.setPrice(object.getString("price"));
					bean2.setSprice(object.getString("sprice"));
					bean2.setIcon(object.getString("main_img_m"));
					bean.getDisplayProducts().add(bean2);
				}
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "FactoryCollectionInfo size = " + list.size());
		return list;
	}

	// 获取申请退款的商品列表
	public static List<RefundGoodBean> refundGoodBeans(String content,
			int type, String orderId) {
		List<RefundGoodBean> list = new ArrayList<RefundGoodBean>();
		RefundGoodBean bean = null;
		try {
			JSONObject object = new JSONObject(content);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				bean = new RefundGoodBean();
				object = array.getJSONObject(i);
				bean.commodity_id = object.optString("commodity_id");
				bean.commodity_name = object.optString("commodity_name");
				bean.commodity_img_m = object.optString("commodity_img_m");
				bean.commodity_code = object.optString("commodity_code");
				bean.order_id = object.optString("order_id");
				Log.d(TAG, "refundGoodBeans commodity_name = "
						+ bean.commodity_name);
				bean.goodSizeBeans = refundGoodSizeBeans(
						object.getJSONArray("goodList"), type, bean.order_id);
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "refundGoodBeans error= " + e);
		}
		Log.d(TAG, "refundGoodBeans size = " + list.size());
		return list;
	}
	
	// 获取申请退款的商品列表
		public static List<RefundGoodBean> refundGoodBeans(String content,
				int type, String orderId,String arrayKey) {
			List<RefundGoodBean> list = new ArrayList<RefundGoodBean>();
			RefundGoodBean bean = null;
			try {
				JSONObject object = new JSONObject(content);
				JSONArray array = object.getJSONArray(arrayKey);
				for (int i = 0; i < array.length(); i++) {
					bean = new RefundGoodBean();
					object = array.getJSONObject(i);
					bean.commodity_id = object.optString("commodity_id");
					bean.commodity_name = object.optString("commodity_name");
					bean.commodity_img_m = object.optString("commodity_img_m");
					bean.commodity_code = object.optString("commodity_code");
					bean.order_id = object.optString("order_id");
					bean.goodSizeBeans = refundGoodSizeBeans(
							object.getJSONArray("goodList"), type, bean.order_id);
					list.add(bean);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				Log.d(TAG, "refundGoodBeans 2  error= " + e);
			}
			Log.d(TAG, "refundGoodBeans 2 size = " + list.size());
			return list;
		}

	// 获取申请退款的商品不同尺码列表
	public static List<RefundGoodSizeBean> refundGoodSizeBeans(JSONArray array,
			int type, String orderId) {
		List<RefundGoodSizeBean> list = new ArrayList<RefundGoodSizeBean>();
		RefundGoodSizeBean bean = null;
		try {
			JSONObject object = null;
			for (int i = 0; i < array.length(); i++) {
				bean = new RefundGoodSizeBean();
				object = array.getJSONObject(i);
				bean.commodity_price = object.optString("commodity_price");
				bean.quantity = object.optString("quantity");
				bean.good_code = object.optString("good_code");
				bean.total_stock = object.optString("total_stock");
				bean.spec_name_value = object.optString("spec_name_value");
				bean.order_id = orderId;
				bean.type = type;
				bean.setUniqueKey(orderId + "-" + bean.good_code);
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Log.d(TAG, "refundGoodSizeBeans errer msg: " + e);
		}
		Log.d(TAG, "refundGoodSizeBeans size = " + list.size());
		return list;
	}

	/**
	 * 解析物流公司信息
	 */
	public static List<LogicticsCampanyBean> paserLogicticsInfo(String content) {
		List<LogicticsCampanyBean> list = new ArrayList<LogicticsCampanyBean>();
		try {
			JSONObject object = new JSONObject(content);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				object = array.getJSONObject(i);
				LogicticsCampanyBean bean = new LogicticsCampanyBean(object.optString("id"),object.optString("logistics_en"),object.optString("logistics_name"));
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 解析厂家搜索结果
	 */
	public static List<StarFactoryBean> parseFactoryListInfo(String content) {
		List<StarFactoryBean> list = new ArrayList<StarFactoryBean>();
		try {
			JSONObject object = new JSONObject(content);
			JSONArray array = object.getJSONArray("data");
			for (int i = 0; i < array.length(); i++) {
				object = array.getJSONObject(i);
				StarFactoryBean bean = new StarFactoryBean();
				bean.setBrand_img_url_m(object.optString("head_img "));
				bean.setF_factory_name(object.optString("f_factory_name"));
				bean.setUid(object.optString("id"));
				list.add(bean);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取PC端热搜关键字
	 * @param content
	 * @return
	 */
	public static String[] parsePcHotSearchWord(String content) {
		String words[] = new String[]{};
		try {
			JSONObject object = new JSONObject(content);
			JSONArray array = object.optJSONArray("data");
			 words = new String[array.length()];
			for (int i = 0; i < array.length(); i++) {
				object = array.getJSONObject(i);
				words[i] = object.optString("kword");
			}
			return words;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return words;
	}
}
