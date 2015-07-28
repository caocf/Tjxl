package com.eims.tjxl_andorid.app;

import com.eims.tjxl_andorid.ui.MainActivity;

import slidingmenu.lib.SlidingMenu;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description: Fragment懒加载，界面可见时才加载数据（ViewPager嵌套Fragment时 使用）
 * @Author zd
 * @date 2015年4月14日 上午10:34:35
 *************************************************************************** 
 */
public abstract class BaseLazyFragment extends Fragment {

	public boolean isVisible;
	// public SlidingMenu sm;
	public Context ct;

	/**
	 * 该方法 在Fragment的onCreateView（）方法前就被调用了，如果在这个方法内部直接调用
	 * lazyload()加载数据，就可能会造成空指针异常，因为视图还未创建
	 */
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInVisible();
		}
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ct = getActivity();
		// sm=((MainActivity)ct).getSlidingMenu();
	}

	/**
	 * Fragment可见时被调用
	 */
	protected void onVisible() {
	   System.out.println("可见。。。。");
		lazyload();
	}

	/**
	 * 加载数据
	 */
	protected abstract void lazyload();

	/**
	 * 界面不可见时被调用
	 */
	protected void onInVisible() {
	};

}
