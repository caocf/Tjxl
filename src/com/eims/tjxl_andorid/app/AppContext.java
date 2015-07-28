package com.eims.tjxl_andorid.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Point;
import android.os.Environment;
import android.view.WindowManager;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.ui.home.bean.SearchBean;
import com.eims.tjxl_andorid.utils.FileUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.NetworkUtils;
import com.eims.tjxl_andorid.utils.SharedPreferencesUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AppContext extends Application {

	public static final String TAG = "AppContext";
	/**
	 * 是否打印日志信息
	 */
	public static final boolean isDebugEnable = true;
	public static boolean isInitData = true;
	/**
	 * 是否输出错误日志到本地文件<br>
	 * mnt/sdcard/com.eims/log/tjxlLog.txt
	 */
	public static final boolean isPrintCrashInfo = true;
	public static boolean isFirstCopy = true;
	public static int AppLaunchTimes = 0;
	public static String mSdcardDataDir = Environment
			.getExternalStorageDirectory().getPath() + "/tjxl";
	public static AppContext appContext;
	public static List<SearchBean> mLists=new ArrayList<SearchBean>();
	public static User userInfo;
	public static boolean isLogin;
	public static final String exit = "1";
	public static final String nExit = "2";
	public static String appStatu="";

	public static AppContext getInstance() {
		return appContext;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		appContext = this;
		initImageLoader(getApplicationContext());
		NetworkUtils.getInstance().init(getApplicationContext());
		getAreaJson();
		// 处理未捕获的异常
		CrashHandler crashHandler = CrashHandler.getInstance();
	    crashHandler.init(this);
		//ToolImageLoad.initImageLoader(getApplicationContext(), "");
	}

	public void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "/tjxl/cache/ImageCache");
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
		}
		DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
		 .showStubImage(R.drawable.morenpic)
		 .showImageForEmptyUri(R.drawable.morenpic)
		 .showImageOnFail(R.drawable.morenpic).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(4 * 1024 * 1024))
				.memoryCacheSize(4 * 1024 * 1024)
				.discCacheSize(100 * 1024 * 1024)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.discCacheFileCount(200).defaultDisplayImageOptions(mOptions)
				.discCache(new UnlimitedDiscCache(cacheDir)).build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 判断是否为第一次启动应用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFirstLaunchApp(Context context) {
		if (!SharedPreferencesUtils.getBoolean(context, "FirstInstalled", true)) {
			AppLaunchTimes = SharedPreferencesUtils.getInt(context,
					"LaunchTimes", 1);
			AppLaunchTimes++;
			SharedPreferencesUtils.saveInt(context, "LaunchTimes",
					AppLaunchTimes);
			return false;
		} else {
			/* 第一次启动应用 */
			AppLaunchTimes = 1;
			SharedPreferencesUtils.saveInt(context, "LaunchTimes",
					AppLaunchTimes);
			SharedPreferencesUtils
					.saveBoolean(context, "FirstInstalled", false);
			return true;
		}
	}

	/**
	 * 根据容器宽度缩放图片高度
	 * 
	 * @param manager
	 * @param bitmapHeight
	 *            图片的高
	 * @param bitmapWidth
	 *            图片的宽
	 * @param widgitWidth
	 *            容器的宽
	 * @return
	 */
	public static int getScallZoomHeight(WindowManager manager,
			final int bitmapHeight, final int bitmapWidth, final int widgitWidth) {
		int zoomHight = 0;
		BigDecimal PIC_X = new BigDecimal(widgitWidth);
		BigDecimal imageWidth = new BigDecimal(bitmapWidth);
		float widthScall = PIC_X
				.divide(imageWidth, 2, BigDecimal.ROUND_HALF_UP).floatValue();
		zoomHight = (int) (widthScall * bitmapHeight);
		return zoomHight;
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

	// 关闭服务
	public void stopTackingService() {
		// TODO Auto-generated method stub

	}
	
	public static void saveUserInfo(Context mContext, User mUserInfo) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"UserInfo", Context.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("check_status", mUserInfo.check_status);
		mEditor.putString("id", mUserInfo.id);
		mEditor.putString("member_type", mUserInfo.member_type);
		mEditor.putString("user_status", mUserInfo.user_status);
		mEditor.putString("username", mUserInfo.username);
		mEditor.putString("password", mUserInfo.password);
		mEditor.putString("exit", nExit);
		mEditor.commit();
	}
	
	public static void saveUserNull(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"UserInfo", Context.MODE_PRIVATE);
		Editor mEditor = mPreferences.edit();
		mEditor.putString("check_status", "");
		mEditor.putString("id", "");
		mEditor.putString("member_type", "");
		mEditor.putString("user_status","");
		mEditor.putString("username","");
		mEditor.putString("password", "");
		mEditor.putString("exit", exit);
		mEditor.commit();
	}
	
	public static User getLocalUserInfo(Context mContext) {
		SharedPreferences mPreferences = mContext.getSharedPreferences(
				"UserInfo", Context.MODE_PRIVATE);
		if (mPreferences == null || !mPreferences.contains("check_status")) {
			return null;
		}
		User userInfo =new User();
		userInfo.check_status = mPreferences.getString("check_status", "");
		userInfo.id = mPreferences.getString("id", "");
		userInfo.member_type = mPreferences.getString("member_type", "");
		userInfo.user_status = mPreferences.getString("user_status", "");
		userInfo.username = mPreferences.getString("username", "");
		userInfo.password = mPreferences.getString("password", "");
		appStatu = mPreferences.getString("exit", exit);
		
		return userInfo;
	}

	public void getAreaJson() {
		String filePath="json/psx.txt";
		String areaJson=FileUtils.ReadTxtFile(filePath,this);
		SharedPreferences mPreferences =getSharedPreferences("adress",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor mEditor = mPreferences.edit();
		mEditor.putString("adress", areaJson);
		mEditor.commit();
	}
}
