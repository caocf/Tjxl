/**
 * 
 */
package com.eims.tjxl_andorid.entity;

import java.io.Serializable;
import java.util.List;

import com.eims.tjxl_andorid.entity.ShopCartBean.Good;
import com.eims.tjxl_andorid.entity.ShopCartBean.GoodStandard;
import com.eims.tjxl_andorid.entity.ShopCartBean.ShopBean;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月28日  下午2:58:49
 *************************************************************************** 
 */
public class CommintOrderBean  implements Serializable{
	
	
	public String type;
	public String msg;
	public List<ShopBean> data;
	public BuyInfo paMap;
	
	public class ShopBean implements Serializable{
		//店铺名称
		public String f_factory_name;
		public String  seller_id;
		public List<Good> commodityList;
	    public String remarks;//备注
	    public String yunfei="0.00";
		
	}
	//购物车商品信息
	public class Good implements Serializable{
		public String  commodity_id;
		public String  commodity_name;
		public String  main_img_m;
		public String  min_batch;
		public String  seller_id;
		public  List<GoodStandard> goodList;
	
	}
	//商品规格
	public class GoodStandard implements Serializable{

		public String  commodity_id;
		public String  good_code;
		public String  goods_stock;
		public String  price;
		public String  quantity;
		public String  spec_name_value;
		public String  sprice;
	
	}
	
	public class  BuyInfo implements Serializable{
//	     "df_address_id": "",
//	        "goods_code_qty": "",
//	        "goods_codes": "143390948961814"
//	       "address_show": "",
//	        "consignee_name": "",
//	        "consignee_phone": "",
//	        "df_address_id": "",
		public String address_show;
		public String consignee_name;
		public String consignee_phone;
		public String df_address_id;
		public String goods_code_qty;
		public String goods_codes;
		public String city_id;
	}

}
