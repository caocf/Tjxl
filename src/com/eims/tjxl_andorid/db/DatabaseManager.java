package com.eims.tjxl_andorid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库管理类，负责数据库的创建、版本更新和相关数据操作（增、删、改、查）
 * 
 * @version [版本号, 2012-2-17]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class DatabaseManager {

	/**数据库文件名**/
	private static final String DB_NAME = "huiyin.db";
	//private static final String DB_NAME = "/sdcard/Huiyin/DB/huiyin.db";

	/**数据库版本号**/
	private static final int DB_VERSION = 1;
	
	/**历史记录表**/
	public static final String TABLE_HISTORY = "table_history";

	/**数据库代理类实例**/
	public static DatabaseHelper openDataBaseHelper = null;
	

	/**
	 * 历史记录表字段
	 * 
	 * @author kuangyong
	 * 
	 */
	public static final class HistoryTableColumns {
		
		public static final String _ID 					= "_id"; 				// 数据库自增id
		public static final String PRODUCT_ID 			= "product_Id"; 		// 商品ID
		public static final String PRODUCT_IMG			= "product_img"; 		// 商品图片
		public static final String PRODUCT_NAME 		= "product_name"; 		// 商品名称
		public static final String PRODUCT_PRICE 		= "product_price"; 		// 商品价格
		public static final String PRODUCT_SALES_NUM 	= "product_sales_num"; 	// 商品销量
		public static final String CREATE_TIME 			= "create_time"; 		// 创建时间
	}



	/**
	 * 数据库代理类，负责数据库的创建和版本更新
	 * 
	 * @version [版本号, 2015-7-04]
	 * @see [相关类/方法]
	 * @since [产品/模块版本]
	 */
	public static class DatabaseHelper extends SQLiteOpenHelper {
		private SQLiteDatabase database;
		private Context context;

		public SQLiteDatabase getDatabase() {
			return this.database;
		}

		@Override
		public synchronized SQLiteDatabase getWritableDatabase() {
			if (this.database == null) {
				this.database = super.getWritableDatabase();
			}
			return this.database;
		}

		@Override
		public synchronized SQLiteDatabase getReadableDatabase() {
			this.database = super.getReadableDatabase();
			return database;
		}

		DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
			this.context = context;
			this.database = null;
		}

		public void CloseDatabase() {
			if (this.database != null && this.database.isOpen()) {
				this.database.close();
				this.database = null;
			}
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			StringBuffer stringBuffer = new StringBuffer();

			//创建 历史记录表
			stringBuffer.append("create table if not exists " + TABLE_HISTORY).append(" (").append(HistoryTableColumns._ID)
					.append(" integer primary key autoincrement,")
					.append(HistoryTableColumns.PRODUCT_ID).append(" text,")
					.append(HistoryTableColumns.PRODUCT_NAME).append(" text,")
					.append(HistoryTableColumns.PRODUCT_IMG).append(" text,")
					.append(HistoryTableColumns.PRODUCT_PRICE).append(" text,")
					.append(HistoryTableColumns.CREATE_TIME).append(" text,")
					.append(HistoryTableColumns.PRODUCT_SALES_NUM).append(" text)");
			db.execSQL(stringBuffer.toString());
			
			//创建第二表
//			stringBuffer = new StringBuffer();
//			stringBuffer.append("create table if not exists " + TABLE_HISTORY).append(" (").append(HistoryTableColumns._ID)
//					.append(" integer primary key autoincrement,")
//					.append(HistoryTableColumns.PRODUCT_ID).append(" text,")
//					.append(HistoryTableColumns.PRODUCT_NAME).append(" text,")
//					.append(HistoryTableColumns.PRODUCT_PRICE).append(" text)");
//			db.execSQL(stringBuffer.toString());
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//			List<String> versionSql = new ArrayList<String>();
//			//varchar(20)
//			String v2 = "alter table cart_product add points_consume text default 'false'";
//			String v3 = "alter table cart_product add not_send int default 0";
//			String v4 = "alter table cart_product add product_type int default 0";
//			versionSql.add("");//v0
//			versionSql.add("");//v1
//			versionSql.add(v2);//v2
//			versionSql.add(v3);//v3
//			versionSql.add(v4);//v4
//			
//			//迭代每一次版本，更新数据库
//			for(int i=oldVersion+1; i<=newVersion && i<versionSql.size(); i++){
//				String sql = versionSql.get(i);
//				if(!StrUtil.isNull(sql)){
//					db.execSQL(sql);
//				}
//			}
		}
	}

	/**
	 * 初始化数据库代理类
	 * 
	 * @param  context 应用上下文]
	 * @return [DatabaseHelper 数据库代理类]
	 * @exception/throws [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public synchronized static DatabaseHelper getDataBaseHelper(Context context) {
		if (null == openDataBaseHelper) {
			if (null == context) {
				openDataBaseHelper = new DatabaseHelper(context);
			} else {
				openDataBaseHelper = new DatabaseHelper(context.getApplicationContext());
			}
		}
		return openDataBaseHelper;
	}

	/**
	 * 获得可写的Database对象
	 * 
	 * @param context
	 * @return
	 */
	public static SQLiteDatabase getWriteableDatabase(Context context) {
		SQLiteDatabase database = null;
		DatabaseHelper helper = getDataBaseHelper(context);
		database = helper.getWritableDatabase();
		return database;
	}
}