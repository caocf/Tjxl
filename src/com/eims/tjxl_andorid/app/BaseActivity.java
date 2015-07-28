/**
 * 
 */
package com.eims.tjxl_andorid.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.utils.MD5;
import com.eims.tjxl_andorid.utils.NetworkUtils;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年4月14日 上午10:33:07
 *************************************************************************** 
 */
public class BaseActivity extends FragmentActivity {

	public Context mContext;
	public boolean available;
	public User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext=this;
		available = NetworkUtils.getInstance().isAvailable();
		if(AppContext.isLogin){
			user=AppContext.getLocalUserInfo(mContext);
		}
		AppManager.getAppManager().addActivity(this);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		AppManager.getAppManager().finishActivity(this);
		overridePendingTransition(
				R.anim.push_right_in, R.anim.push_right_out);
	}


	/** 当前显示的fragment **/
	protected Fragment currentFragment = null;

	/**
	 * 显示framgnet
	 *
	 * @param fragment
	 */
	protected void showFragment(Fragment fragment,int fragmentId) {
		// 非空
		if (null == fragment) {
			return;
		}
		// 已经显示
		if (fragment.isAdded() && !fragment.isHidden()) {
			return;
		}
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transation = fragmentManager.beginTransaction();
		// 隐藏当前显示的fragment
		if (null != currentFragment && currentFragment.isAdded()) {
			transation.hide(currentFragment);
		}
		// 显示传入的fragment
		if (!fragment.isAdded()) {
			transation.add(fragmentId, fragment);
			transation.commit();
		} else {
			transation.show(fragment);
			transation.commit();
		}
		// 记住当前的fragment
		currentFragment = fragment;
	}

	protected void showToast(String showText){
		if(null!=mContext){
			Toast.makeText(mContext, showText, Toast.LENGTH_SHORT).show();
		}
	}

	protected String md5(String content){
		return MD5.md5(content);
	}

}
