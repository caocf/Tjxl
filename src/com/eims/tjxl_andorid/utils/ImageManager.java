package com.eims.tjxl_andorid.utils;

import java.io.File;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.constans.Constans;
import com.eims.tjxl_andorid.constans.URLs;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;


public class ImageManager {

	// static DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
	// .cacheInMemory(true).cacheOnDisc(true)
	// .showStubImage(R.drawable.zairu)
	// .showImageForEmptyUri(R.drawable.zairu)
	// .showImageOnFail(R.drawable.zairu).build();
	
//	public static  void  Load(String Url,ImageView imageview, DisplayImageOptions options){}
	
	

	public static void Load(String imgUrl, ImageView imageView) {
		ImageLoader.getInstance().displayImage(imgUrl, imageView);
	}

	public static void Load(String imgUrl, ImageView imageView,
			DisplayImageOptions o) {
		ImageLoader.getInstance().displayImage(StringUtils.fixImageUrl(imgUrl),
				imageView, o);
	}

	public static void Load(String imgUrl, ImageView imageView,
			ImageLoadingListener listener) {
		ImageLoader.getInstance().displayImage(StringUtils.fixImageUrl(imgUrl),
				imageView, listener);
	}

	public static void Load(String imgUrl, ImageLoadingListener listener) {
		ImageLoader.getInstance().loadImage(StringUtils.fixImageUrl(imgUrl),
				listener);
	}

	public static void Load(String imgUrl, DisplayImageOptions o,
			ImageLoadingListener listener) {
		ImageLoader.getInstance().loadImage(StringUtils.fixImageUrl(imgUrl), o, listener);
	}
	
	 /**
     * 从服务器获取图片并显示
     * <p><b>统一获取方式，用来处理省流量的模式</b></p>
     * @param imgUrl 图片地址
     * @param imageView 展示的View
     */
	public static void LoadWithServer(String imgUrl, ImageView imageView) {
		LoadWithServer(imgUrl, imageView, null);
	}
    /**
     * 从服务器获取图片并显示
     * <p><b>统一获取方式，用来处理省流量的模式</b></p>
     * @param imgUrl 图片地址
     * @param imageView 展示的View
     * @param o DisplayImageOptions
     */
	public static void LoadWithServer(String imgUrl, ImageView imageView,
			DisplayImageOptions o) {
		LoadWithServer(imgUrl, imageView, o, null);
	}
    /**
     * 从服务器获取图片并显示
     * <p><b>统一获取方式，用来处理省流量的模式</b></p>
     * @param imgUrl 图片地址
     * @param imageView 展示的View
     * @param o DisplayImageOptions
     * @param loadingListener ImageLoadingListener  加载监听
     */
	public static void LoadWithServer(String imgUrl, final ImageView imageView,
			final DisplayImageOptions o,
			final ImageLoadingListener loadingListener) {
		final String mWrapImagUrl = wrapImagUrl(imgUrl);
//		if (shouldShowPic()) {
//			ImageLoader.getInstance().displayImage(mWrapImagUrl, imageView, o,
//					loadingListener);
//		} else {
			if (hasCache(mWrapImagUrl)) {
				ImageLoader.getInstance().displayImage(mWrapImagUrl, imageView,
						o, loadingListener);
			} else {
				imageView.setImageResource(R.drawable.image_loader_noimage);
				imageView
						.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                ImageLoader.getInstance().displayImage(
                                        mWrapImagUrl, imageView, o,
                                        loadingListener);
                                return true;
                            }
                        });
			}
		}
//	}
    /**
     * 从服务器获取图片
     * <p><b>用于下载之类的不需要展示控件</b></p>
     * @param imgUrl 图片地址
     * @param loadingListener ImageLoadingListener  加载监听
     */
    public static void LoadWithoutImageView(String imgUrl,ImageLoadingListener loadingListener){
        ImageLoader.getInstance().loadImage(wrapImagUrl(imgUrl),loadingListener);
    }

//	private static boolean shouldShowPic() {
//		SettingPreferenceUtil.SettingItem mSettingItem = AppContext
//				.getInstance().getSettingSharePreference();
//		if (mSettingItem == null)
//			return true;
//		switch (mSettingItem.interType) {
//		case 1:
//			// normal
//			return false;
//		case 2:
//			// high_quality
//			return true;
//		case 3:
//			// auto
//			return AppContext.getInstance().isWifi();
//		}
//		return true;
//	}

	/**
	 * 该图片是否已被缓存
	 *
	 * @param wrapImagUrl
	 *            被包装过的url
	 * @return boolean
	 */
	private static boolean hasCache(String wrapImagUrl) {
		List<String> keys = MemoryCacheUtil.findCacheKeysForImageUri(
				wrapImagUrl, ImageLoader.getInstance().getMemoryCache());
		if (keys == null || keys.size() < 1) {
			File cache = DiscCacheUtil.findInCache(wrapImagUrl, ImageLoader
					.getInstance().getDiscCache());
			if (cache == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 包装图片url,添加域名。 <b>如果url本身包含主域名就不用额外添加域名.<b/>
	 * <p/>
	 * "http://site.com/image.png" // from Web<br/>
	 * "file:///mnt/sdcard/image.png" // from SD card<br/>
	 * "file:///mnt/sdcard/video.mp4" // from SD card (video thumbnail)<br/>
	 * "content://media/external/images/media/13" // from content provider<br/>
	 * "content://media/external/video/media/13" // from content provider (video
	 * thumbnail)<br/>
	 * "assets://image.png" // from assets<br/>
	 * "drawable://" + R.drawable.img // from drawables (non-9patch images)<br/>
	 * <p/>
	 *
	 * @param originUrl
	 *            原始url
	 * @return String
	 */
	private static String wrapImagUrl(String originUrl) {

        if(TextUtils.isEmpty(originUrl)){
            return "";
        }

		String wrappedUrl;
		if (originUrl.startsWith("http") || originUrl.startsWith("https")
				|| originUrl.startsWith("file://")
				|| originUrl.startsWith("content://")
				|| originUrl.startsWith("assets://")
				|| originUrl.startsWith("drawable://")) {
			wrappedUrl = originUrl;
		} else {
			wrappedUrl = URLs.IMAGE_URL + originUrl;
		}
		return wrappedUrl;
	}
}
