package com.eims.tjxl_andorid.ui;

import java.util.ArrayList;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年4月14日 上午10:32:29
 *************************************************************************** 
 */
public class GuideActivity extends BaseActivity {

	private ViewPager gPager;
	private ArrayList<ImageView> mGuideImages;
	private LinearLayout ll_buttom;
	private Button btn_go, btn_register;
	private ArrayList<GuideFragment> mGuideFragments;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_layout);
		gPager = (ViewPager) findViewById(R.id.guide_viwepager);
		ll_buttom = (LinearLayout) findViewById(R.id.ll_bottom);
        initpage();

	
	}
	
	private void  craeteImage(){
		mGuideFragments = new ArrayList<GuideFragment>();
		mGuideFragments.add(new GuideFragment(0));
		mGuideFragments.add(new GuideFragment(1));
		mGuideFragments.add(new GuideFragment(2));
	}
	
	private  void  initpage(){
		craeteImage();
	
		fragmentManager = getSupportFragmentManager();
		GuideImageAdapter adapter = new GuideImageAdapter(fragmentManager);
		gPager.setAdapter(adapter);
	}



	class GuideImageAdapter extends FragmentPagerAdapter {

		public GuideImageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return mGuideFragments == null ? 0 : mGuideFragments.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			return mGuideFragments.get(arg0);
		}
	}
}
