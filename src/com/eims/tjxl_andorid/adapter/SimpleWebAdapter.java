package com.eims.tjxl_andorid.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader;

public class SimpleWebAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ImageLoader mImageLoader;
	public String[] stringArr;
	public ImageDownloader mDownloader;
	private Context mcontext;
	DisplayImageOptions options;

	public SimpleWebAdapter(Context mContext, String[] stringArr) {
		super();
		this.inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mImageLoader = ImageLoader.getInstance();
		this.stringArr = stringArr;
		this.mcontext = mContext;

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.showImageOnFail(R.drawable.default_icons)
				.delayBeforeLoading(0)
				.resetViewBeforeLoading(true)
				.displayer(new FadeInBitmapDisplayer(100))
				.showStubImage(R.drawable.loading)
				// .showImageOnLoading(R.drawable.loading) //设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.bitmapConfig(Bitmap.Config.RGB_565)
				// .imageScaleType(ImageScaleType.EXACTLY)
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true).build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mcontext).memoryCacheExtraOptions(200, 200)
				// 缓存在内存的图片的宽和高度
				.discCacheExtraOptions(480, 800, CompressFormat.PNG, 70, null)
				// CompressFormat.PNG类型，70质量（0-100）
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(3 * 1024 * 1024) // 缓存到内存的最大数据
				.discCacheSize(50 * 1024 * 1024)// 缓存到文件的最大数据
				.discCacheFileCount(1000) // 文件数量
				.defaultDisplayImageOptions(options). // 上面的options对象，一些属性配置
				build();

		ImageLoader.getInstance().init(config); // 初始化
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return stringArr==null?0:stringArr.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return stringArr[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return stringArr[arg0].hashCode();
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		String s = stringArr[arg0];
		ImageView simpleImage;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.simplewebadapter, null);
			simpleImage = (ImageView) convertView.findViewById(R.id.imageView1);
			convertView.setTag(simpleImage);
		} else {
			simpleImage = (ImageView) convertView.getTag();
		}
	//	System.out.println("s----------" + s);
		if (stringArr.length == 1) {
			if (stringArr[0].equals("")) {
				simpleImage.setImageResource(R.color.white);
				Toast.makeText(mcontext, "无相关数据！", Toast.LENGTH_SHORT).show();
			} else {
				simpleImage.setScaleType(ScaleType.CENTER_CROP);
			}
		}

		mImageLoader.displayImage(s, simpleImage);

		return convertView;
	}

}
