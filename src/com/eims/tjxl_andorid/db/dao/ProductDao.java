package com.eims.tjxl_andorid.db.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eims.tjxl_andorid.db.DatabaseManager;
import com.eims.tjxl_andorid.db.DatabaseManager.HistoryTableColumns;
import com.eims.tjxl_andorid.db.dbmodel.Product;
import com.eims.tjxl_andorid.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品历史记录Dao
 * @author kuangyong
 *
 */
public class ProductDao {

	/**
	 * 商品列表
	 * @param context
	 * @return
	 */
	public static List<Product> getProductHistory(Context context) {
		List<Product> shopCart = new ArrayList<Product>();
		SQLiteDatabase db = DatabaseManager.getWriteableDatabase(context);
		Cursor cursor = db.query(DatabaseManager.TABLE_HISTORY, null, null, null, null, null, null);
		Product Product = null;
		while (cursor.moveToNext()) {
			Product = new Product(cursor);
			shopCart.add(Product);
		}
		cursor.close();
		return shopCart;
	}

	/**
	 * 商品列表
	 * @param context
	 * @return
	 */
	public static List<Product> getProductHistory(Context context,int pageSize, int pageIndex) {
		List<Product> shopCart = new ArrayList<Product>();
		SQLiteDatabase db = DatabaseManager.getWriteableDatabase(context);

		String sql = "Select * from "+ DatabaseManager.TABLE_HISTORY + " order by "+ HistoryTableColumns.CREATE_TIME +" desc limit "+ pageSize+ " offset "+ pageIndex;
		Cursor cursor = db.rawQuery(sql, null);
		Product product = null;
		while (cursor.moveToNext()) {
			product = new Product(cursor);
			shopCart.add(product);
		}
		cursor.close();
		return shopCart;
	}
	
	public static Product findOneById(String id,Context context){
		SQLiteDatabase db = DatabaseManager.getWriteableDatabase(context);

		String sql = "Select * from "+ DatabaseManager.TABLE_HISTORY + " where "+ HistoryTableColumns.PRODUCT_ID +"="+ id;
		Cursor cursor = db.rawQuery(sql, null);
		Product product = null;
		if (cursor.moveToNext()) {
			product = new Product(cursor);
		}
		cursor.close();
		return product;
	}


	/**
	 * 保存一条数据到数据库
	 * 
	 * @param product
	 * @param context
	 * @return
	 */
	public static boolean addProduct(Product product, Context context) {
		SQLiteDatabase db = DatabaseManager.getWriteableDatabase(context);
		ContentValues values = new ContentValues();
		values.put(HistoryTableColumns.PRODUCT_ID, product.id+ "");
		values.put(HistoryTableColumns.PRODUCT_NAME, product.name + "");
		values.put(HistoryTableColumns.PRODUCT_IMG, product.img + "");
		values.put(HistoryTableColumns.PRODUCT_PRICE, product.price + "");
		values.put(HistoryTableColumns.PRODUCT_SALES_NUM, product.sales_num + "");
		values.put(HistoryTableColumns.CREATE_TIME,  DateUtil.dateToStr(new Date())+ "");
		long id = db.insert(DatabaseManager.TABLE_HISTORY, null, values);
//		if (id > 0) {
//			product.id = ((int) id);
//		}
		return id > 0;
	}

	/**
	 * 删除一条数据库
	 * @param productId
	 * @param context
	 * @return
	 */
	public static boolean delProduct(String productId, Context context) {
		SQLiteDatabase db = DatabaseManager.getWriteableDatabase(context);
		String whereClause = DatabaseManager.HistoryTableColumns.PRODUCT_ID + " = ? ";
		String[] whereArgs = new String[] {productId };
		int count = db.delete(DatabaseManager.TABLE_HISTORY, whereClause, whereArgs);
		return count > 0;
	}

	/**
	 * 删除一条数据库
	 * 
	 * @param context
	 * @return
	 */
	public static boolean delAllProduct(Context context) {
		SQLiteDatabase db = DatabaseManager.getWriteableDatabase(context);
		int count = db.delete(DatabaseManager.TABLE_HISTORY, null, null);
		return count > 0;
	}
}
