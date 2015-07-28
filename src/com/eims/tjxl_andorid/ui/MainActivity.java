package com.eims.tjxl_andorid.ui;
import java.util.ArrayList;

import loopj.android.http.RequestParams;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.AppManager;
import com.eims.tjxl_andorid.ui.exhibition.ExhibitionFragment;
import com.eims.tjxl_andorid.ui.home.HomeFragment;
import com.eims.tjxl_andorid.ui.personcenter.PersonalCenterFragment;
import com.eims.tjxl_andorid.ui.receipt.ReceiptFragment;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.MyViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener {

	public static RadioGroup radioGroup;
	public static MyViewPager mainPager;
	private static final int INFOHALL_TAG = 0;
	private static final int TRACK_TAG = 1;
	private static final int TOOLS_TAG = 2;
	private static final int ASSISTANT_TAG = 3;
	private FragmentManager fm;
	private ArrayList<Fragment> fragments;
	private HomeFragment hallInfoFragment;
	private ExhibitionFragment trackFragment;
	private ReceiptFragment toolsFragment;
	private PersonalCenterFragment mineFragment;
	private long firstTime;
	private long secondTime;
	private long spaceTime;

	// private SlidingMenu sm;

	@SuppressWarnings("null")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		mainPager = (MyViewPager) findViewById(R.id.vPager);
		radioGroup = (RadioGroup) findViewById(R.id.bottom_bar);
		initViews();
		// initmenu();
		radioGroup.setOnCheckedChangeListener(this);
		((RadioButton) radioGroup.getChildAt(0)).performClick();// 默认RadioGroup的第一个RadioBottom被选中
		  if(AppContext.userInfo!=null){
			  String check_status = AppContext.userInfo.check_status;
			  LogUtil.d("zd", "user_id  :"+   AppContext.userInfo.id   );
		  }
		
	}

	// private void initmenu() {
	// setBehindContentView(R.layout.menu);
	// sm = getSlidingMenu();
	// sm.setMode(SlidingMenu.RIGHT);
	// // 设置滑动菜单出来之后，内容页面的剩余宽度
	// sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
	// // 设置滑动菜单Touch的模式
	// /**
	// * SlidingMenu.TOUCHMODE_FULLSCREEN 设置滑动菜单全屏模式
	// * SlidingMenu.TOUCHMODE_MARGIN 设置滑动菜单智能在边沿进行滑动
	// * SlidingMenu.TOUCHMODE_NONE 设置滑动菜单不能滑动
	// */
	// sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	// // 设置阴影的颜色
	// // sm.setShadowDrawable(R.drawable.shadow);
	// // 设置阴影的宽度
	// sm.setShadowWidthRes(R.dimen.shadow_width);
	// FragmentTransaction fragmentTransaction = fm.beginTransaction();
	// MenuFragment menuFragment = new MenuFragment();
	// fragmentTransaction.replace(R.id.menu_frame, menuFragment, "menu")
	// .commit();
	// }

	private void initViews() {
		fm = getSupportFragmentManager();
		fragments = new ArrayList<Fragment>();
		hallInfoFragment = HomeFragment.getInstance();
		trackFragment = ExhibitionFragment.getInstance();
		toolsFragment = ReceiptFragment.getInstance();
		mineFragment = PersonalCenterFragment.getInstance();
		fragments.add(hallInfoFragment);
		fragments.add(trackFragment);
		fragments.add(toolsFragment);
		fragments.add(mineFragment);

		mainPager.setAdapter(new MainPageAdpter(fm));

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		RadioButton btn = (RadioButton) group.findViewById(checkedId);
		int modle = Integer.parseInt((String) btn.getTag());
		switch (modle) {
		case INFOHALL_TAG:// 信息大厅
			// 设置字体颜色
			((RadioButton) group.getChildAt(0)).setTextColor(getResources()
					.getColor(R.color.sheng_red));// 红色
			((RadioButton) group.getChildAt(1))
					.setTextColor(Color.rgb(0, 0, 0));
			((RadioButton) group.getChildAt(2))
					.setTextColor(Color.rgb(0, 0, 0));
			((RadioButton) group.getChildAt(3))
					.setTextColor(Color.rgb(0, 0, 0));
			mainPager.setCurrentItem(INFOHALL_TAG);
			// 设置滑动菜单可以滑动
			// sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			break;
		case TRACK_TAG:
			((RadioButton) group.getChildAt(0))
					.setTextColor(Color.rgb(0, 0, 0));
			((RadioButton) group.getChildAt(1)).setTextColor(getResources()
					.getColor(R.color.sheng_red));// 红色
			((RadioButton) group.getChildAt(2))
					.setTextColor(Color.rgb(0, 0, 0));
			((RadioButton) group.getChildAt(3))
					.setTextColor(Color.rgb(0, 0, 0));
			mainPager.setCurrentItem(TRACK_TAG);
			// 设置滑动菜单不可以滑动
			// sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			break;
		case TOOLS_TAG:
			((RadioButton) group.getChildAt(0))
					.setTextColor(Color.rgb(0, 0, 0));
			((RadioButton) group.getChildAt(1))
					.setTextColor(Color.rgb(0, 0, 0));
			((RadioButton) group.getChildAt(2)).setTextColor(getResources()
					.getColor(R.color.sheng_red));// 红色
			((RadioButton) group.getChildAt(3))
					.setTextColor(Color.rgb(0, 0, 0));
			mainPager.setCurrentItem(TOOLS_TAG);
			// 设置滑动菜单不可以滑动
			// sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			break;
		case ASSISTANT_TAG:
			((RadioButton) group.getChildAt(0))
					.setTextColor(Color.rgb(0, 0, 0));
			((RadioButton) group.getChildAt(1))
					.setTextColor(Color.rgb(0, 0, 0));
			((RadioButton) group.getChildAt(2))
					.setTextColor(Color.rgb(0, 0, 0));
			((RadioButton) group.getChildAt(3)).setTextColor(getResources()
					.getColor(R.color.sheng_red));// 红色
			mainPager.setCurrentItem(ASSISTANT_TAG);
			// 设置滑动菜单不可以滑动
			// sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			break;
		default:
			break;
		}
	}

	public void switchPager(int poisiton) {
		initViews();
		mainPager.setCurrentItem(poisiton);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			firstTime = System.currentTimeMillis();
			spaceTime = firstTime - secondTime;
			secondTime = firstTime;
			if (spaceTime > 2000) {
				Toast.makeText(this, "再按一次退出" + getString(R.string.app_name),
						Toast.LENGTH_SHORT).show();
				return false;
			} else {
				AppManager.getAppManager().AppExit(this);
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	class MainPageAdpter extends FragmentStatePagerAdapter {

		public MainPageAdpter(FragmentManager fm) {
			super(fm);

		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return fragments.get(arg0);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fragments == null ? 0 : fragments.size();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			/**
			 * 这里千万不能加下面这句话，如果加了 界面来回切换时 会出现空白
			 */
			// container.removeView(fragments.get(position).getView()); //
			// 移出viewpager两边之外的page布局
		}
	}
	
	
	private void  loadAdress(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(this, false, ""){
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				if(statusCode==200){
					SharedPreferences mPreferences = getSharedPreferences(
							"adress", Context.MODE_PRIVATE);
					Editor mEditor = mPreferences.edit();
					mEditor.putString("adress", content);
					mEditor.commit();
					
				}
			}
		};
		RequestParams params=new RequestParams();
		HttpClient.QueryPrvionceCity(mHandler, params);
	}

}
