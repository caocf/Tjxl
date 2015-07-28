package com.eims.tjxl_andorid.db.dbmodel;

import android.database.Cursor;

import com.eims.tjxl_andorid.db.DatabaseManager.HistoryTableColumns;

/**
 * 历史记录商品
 * @author kuangyong
 *
 */
public class Product {
	
	public String id;
	public String img;
	public String name;
	public String price;
	public String sales_num;
	public String create_time;

	public Product(Cursor cursor) {
		id = cursor.getString(cursor.getColumnIndex(HistoryTableColumns.PRODUCT_ID));
		img = cursor.getString(cursor.getColumnIndex(HistoryTableColumns.PRODUCT_IMG));
		name = cursor.getString(cursor.getColumnIndex(HistoryTableColumns.PRODUCT_NAME));
		price = cursor.getString(cursor.getColumnIndex(HistoryTableColumns.PRODUCT_PRICE));
		sales_num = cursor.getString(cursor.getColumnIndex(HistoryTableColumns.PRODUCT_SALES_NUM));
		create_time = cursor.getString(cursor.getColumnIndex(HistoryTableColumns.CREATE_TIME));
	}
	
	public Product (){
		
	}
}
