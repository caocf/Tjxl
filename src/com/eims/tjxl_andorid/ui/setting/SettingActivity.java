package com.eims.tjxl_andorid.ui.setting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.entity.SettingItemBean;
import com.eims.tjxl_andorid.entity.UpdataInfo;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.DataCleanManager;
import com.eims.tjxl_andorid.utils.FileUtils;
import com.eims.tjxl_andorid.utils.JSONParseUtils;
import com.eims.tjxl_andorid.utils.SharedPreferencesUtils;
import com.eims.tjxl_andorid.utils.UpdateManager;
import com.eims.tjxl_andorid.utils.UpdateVersionTool;
import com.eims.tjxl_andorid.weght.HeadView;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends com.eims.tjxl_andorid.app.BaseActivity {

	private static final String TAG1 = "SettingActivity";
	private HeadView headView;
	private ListView settingListView;
	
	private List<SettingItemBean> items;
	private SettingItemAdapter mAdapter;
	
	private LayoutInflater minInflater;
	
	private int typeIndex;
	
	/**
	 * 设置选项
	 */
	public static final String TITLE_PIC_QUALITY = "图片质量设置";
	public static final String TITLE_CLEAR_CACHE = "清除缓存";
	public static final String TITLE_FUNCTION_INTRODUTION = "功能介绍";
	public static final String TITLE_USE_GUIDER = "使用帮助";
	public static final String TITLE_FEEDBACK = "意见反馈";
	public static final String TITLE_ENCOURAGE_US = "给我们鼓励";
	public static final String TITLE_ABOUT_US = "关于我们";
	public static final String TITLE_WULIU_SETTING = "物流设置";
	public static final String TITLE_VERSION_CHECK = "检测新版本";
	public static final String TITLE_ONLINE_SERVICE = "客服热线";
	
	
	
	private String onlineTel;
//	private String picMode ;
	private String versionName;
	private String cacheSize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		init();
		setItems();
		getValues();
	}
	
	
	private void init(){
		headView = (HeadView) findViewById(R.id.headview);
		settingListView = (ListView) findViewById(R.id.listview_setting_items);
		minInflater = LayoutInflater.from(this);
		
		items = new ArrayList<SettingItemBean>();
		mAdapter = new SettingItemAdapter();
		settingListView.setAdapter(mAdapter);
		
		headView.setText("设置");
		headView.setRightResource();
		settingListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SettingItemBean itemBean = items.get(arg2);
				if(itemBean.getTag() == SettingDetailActivity.TAG_CLEAR_CACHE){//清除缓存
					clearCacheData();
				}else if(itemBean.getTag() == SettingDetailActivity.TAG_VERSION_CHECK){//检测新版本
					UpdataInfo info = new UpdataInfo();
					info.setVersionCode(UpdateVersionTool.getVersionCode(SettingActivity.this));
					updateApk(info);
				}else if(itemBean.getTag() == SettingDetailActivity.TAG_ONLINE_SERVICE){//客服热线
					callOnlineService();
				}else {//跳转到SettingDetailActivity
					switchToDetailActivty(items.get(arg2).getName(), items.get(arg2).getTag());
				}
			}
		});
		
	}
	
	private void setItems(){
//		SettingItemBean itemPicQuality = new SettingItemBean(TITLE_PIC_QUALITY, "", true,SettingDetailActivity.TAG_PIC_QUALITY);
		SettingItemBean itemFunctionIntrodution = new SettingItemBean(TITLE_FUNCTION_INTRODUTION, "", true,SettingDetailActivity.TAG_FUNCTION_INTRO);
		SettingItemBean itemUseGuide = new SettingItemBean(TITLE_USE_GUIDER, "", true,SettingDetailActivity.TAG_USE_GUIDE);
		SettingItemBean itemFeedback = new SettingItemBean(TITLE_FEEDBACK, "", true,SettingDetailActivity.TAG_FEEDBACK);
//		SettingItemBean itemEncourageUs = new SettingItemBean(TITLE_ENCOURAGE_US, "", false,SettingDetailActivity.TAG_ERROR);
		SettingItemBean itemAboutUs = new SettingItemBean(TITLE_ABOUT_US, "", true,SettingDetailActivity.TAG_ABOUT_US);
		SettingItemBean itemWuliuSetting = new SettingItemBean(TITLE_WULIU_SETTING, "", true,SettingDetailActivity.TAG_WULIU_SETTING);
		SettingItemBean itemClearCache = new SettingItemBean(TITLE_CLEAR_CACHE, "当前缓存 5.00M",false,SettingDetailActivity.TAG_CLEAR_CACHE);
		SettingItemBean itemCheckVersion = new SettingItemBean(TITLE_VERSION_CHECK, "", false,SettingDetailActivity.TAG_VERSION_CHECK);
		SettingItemBean itemOnlineService = new SettingItemBean(TITLE_ONLINE_SERVICE, "", false,SettingDetailActivity.TAG_ONLINE_SERVICE);
		
//		items.add(itemPicQuality);
		items.add(itemFunctionIntrodution);
		items.add(itemUseGuide);
		items.add(itemFeedback);
//		items.add(itemEncourageUs);
		items.add(itemAboutUs);
		items.add(itemWuliuSetting);
		items.add(itemClearCache);
		items.add(itemCheckVersion);
		items.add(itemOnlineService);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		picMode = getPicModeTitle();
//		items.get(0).setValue(picMode);
//		mAdapter.notifyDataSetChanged();
	}
	private void getValues(){
//		picMode = getPicModeTitle();
		versionName = UpdateVersionTool.getVersionName(this);
		cacheSize = getCacheSize();
		loadOnlineServiceTele();
		
		items.get(5).setValue(cacheSize);
		items.get(6).setValue("更新至 "+versionName+" 版本");
		mAdapter.notifyDataSetChanged();
	}
	
	private String getPicModeTitle(){
		int modeIndex = SharedPreferencesUtils.getPicQualityMode(this);
		String title = "";
		switch(modeIndex){
		case 0:
			title = PictQualitySettingFragment.PIC_MODE_SMART;
			break;
		case 1:
			title = PictQualitySettingFragment.PIC_MODE_TOP_SPEED;
			break;
		case 2:
			title = PictQualitySettingFragment.PIC_MODE_INFLUENCY;
			break;
		case 3:
			title = PictQualitySettingFragment.PIC_MODE_HIGH_DEFINITION;
			break;
		}
		
		return title;
	}
	
	
	
	private void switchToDetailActivty(String title,int tag){
		Bundle bundle = new Bundle();
		bundle.putString(SettingDetailActivity.KEY_TITLE, title);
		bundle.putInt(SettingDetailActivity.KEY_TAG, tag);
		ActivitySwitch.openActivity(SettingDetailActivity.class, bundle, this);
	}
	
	/**
	 * 获取在线客服号码
	 */
	private void loadOnlineServiceTele(){
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG1, "online service message result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG1, "在线客服信息获取失败");
					return;
				} else {
					onlineTel = JSONParseUtils.getString(content, "data");
					items.get(7).setValue(onlineTel);
					mAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG1, "onFailure: content:" + content);
			}
		};
		RequestParams params = new RequestParams();
		HttpClient.loadOnlineServiceInfo(mHandler, params);
	}
	
	/**
	 * 获取缓存图片和缓存文件大小
	 */
	private String getCacheSize(){
		File imageCache = new File(Environment.getExternalStorageDirectory(), "/tjxl/cache/ImageCache");
		File tempImageCache = new File(Environment.getExternalStorageDirectory(), "/tjxl/cache/tempImage");
		long cacheSize = FileUtils.getFileSize(imageCache) + FileUtils.getFileSize(tempImageCache);
		String sizeString = FileUtils.FormetFileSize(cacheSize);
		Log.d(TAG1,"cacheSize = "+cacheSize+", size format:"+sizeString);
		if(sizeString.startsWith(".")){
			sizeString = "0.0B";
		}
		return sizeString;
	}
	
	/**
	 * 清除缓存
	 */
	private void clearCacheData(){
		DataCleanManager.deleteFilesByDirectory(StorageUtils
				.getOwnCacheDirectory(getApplicationContext(),
                        "/tjxl/cache/ImageCache"));
		DataCleanManager.deleteFilesByDirectory(StorageUtils
				.getOwnCacheDirectory(getApplicationContext(),
                        "/tjxl/cache/tempImage"));
		Toast.makeText(mContext, "图片清除完毕", Toast.LENGTH_SHORT).show();
		cacheSize = getCacheSize();
		items.get(5).setValue(cacheSize);
		mAdapter.notifyDataSetChanged();
	}
	/***
	 * 检测版本
	 * */
	private void requestVersion() {
		CustomResponseHandler mHandler = new CustomResponseHandler(mContext,
				true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG, "factory message result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					Log.d(TAG, "版本信息获取失败");
					return;
				} else {
					analyticalVersionData(content);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG, "onFailure: content:" + content);
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", AppContext.userInfo.id);
		HttpClient.loadFactoryMessage(mHandler, params);
	}

	/***
	 * 解析版本数据
	 * */
	private void analyticalVersionData(String content) {
		try {
			JSONObject roots = new JSONObject(content);
			if (roots.getString("type").equals("1")) {
				String url = roots.getString("editionUrl");
				String versionName = roots.getString("editionName");
				String versionVersion = roots.getString("editionExplain");
				
				int versionCode = Integer.valueOf(roots.getString("edition"));
				UpdataInfo updateInfo = new UpdataInfo();
				updateInfo.setUrl(url);
				updateInfo.setVersion(versionName);
				updateInfo.setVersionCode(versionCode);
				updateInfo.setDescription(versionVersion);
				
				updateApk(updateInfo);

			} else {
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 版本更新
	 * @param updateInfo
	 */
	private void updateApk(final UpdataInfo updateInfo) {
		if (UpdateVersionTool.getVersionCode(this) >= updateInfo
				.getVersionCode()) {
			Toast.makeText(mContext, "已是最新版本！", Toast.LENGTH_SHORT).show();
			return;
		} else {

			UpdateManager manager = new UpdateManager(this, "tjxl_android",
					updateInfo.getUrl());
			manager.checkUpdate(updateInfo.getVersionCode(), updateInfo.getDescription());

		}
	}

	private void callOnlineService(){
		Log.d(TAG1, "===>callOnlineService, onlineTel = "+onlineTel);
		Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+onlineTel));
		startActivity(intent);
	}
	
	class SettingItemAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			
			View view = arg1;
			ViewHolder holder = null;
			if(view == null){
				view = minInflater.inflate(R.layout.layout_setting_item, null);
				holder = new ViewHolder();
				holder.title = (TextView) view.findViewById(R.id.title);
				holder.message = (TextView) view.findViewById(R.id.message);
				holder.arrow = (ImageView) view.findViewById(R.id.arrow);
				view.setTag(holder);
			}
			holder = (ViewHolder) view.getTag();
			holder.title.setText(items.get(arg0).getName());
			holder.message.setText(items.get(arg0).getValue());
			if(items.get(arg0).getIsShowArrow()){
				holder.arrow.setVisibility(View.VISIBLE);
			}else{
				holder.arrow.setVisibility(View.INVISIBLE);
			}
			return view;
		}
		
	}
	
	class ViewHolder{
		TextView title;
		TextView message;
		ImageView arrow;
	}
}
