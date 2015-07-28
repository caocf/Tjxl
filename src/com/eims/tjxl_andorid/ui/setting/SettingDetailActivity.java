package com.eims.tjxl_andorid.ui.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.weght.HeadView;

public class SettingDetailActivity extends BaseActivity {

	public static final String KEY_TITLE = "key_title";
	public static final String KEY_TAG = "key_tag";
	/*
	 * 不同设置项对应的编号
	 */
	public static final int TAG_ERROR = -1;
	public static final int TAG_PIC_QUALITY = 1;
	public static final int TAG_FUNCTION_INTRO = 2;
	public static final int TAG_USE_GUIDE = 3;
	public static final int TAG_FEEDBACK = 4;
	public static final int TAG_ABOUT_US = 5;
	public static final int TAG_CLEAR_CACHE = 6;
	public static final int TAG_VERSION_CHECK = 7;
	public static final int TAG_ONLINE_SERVICE = 8;
	public static final int TAG_WULIU_SETTING= 9;
	private HeadView headView;

	private FragmentManager fragmentManager;
	private PictQualitySettingFragment pictSettingFragment;

	private String title;
	private int tag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_detail);
		init();
		setContent();
	}

	public void init() {
		Bundle bundle = getIntent().getExtras();
		title = bundle.getString(KEY_TITLE);
		tag = bundle.getInt(KEY_TAG);
		headView = (HeadView) findViewById(R.id.headview);

		fragmentManager = getSupportFragmentManager();

		headView.setText(title);
	}

	public void setContent() {
		switch (tag) {
		case TAG_PIC_QUALITY:
			replaceFragment(new PictQualitySettingFragment());
			break;
		case TAG_FUNCTION_INTRO:
		case TAG_USE_GUIDE:
			WebviewSettingFragment fragment = new WebviewSettingFragment(tag);
			replaceFragment(fragment);
			break;
		case TAG_ABOUT_US:
			replaceFragment(new AboutUsFragment());
			break;
		case TAG_FEEDBACK:
			replaceFragment(new FeedbackSettingFragment());
			break;
		case TAG_WULIU_SETTING:
			replaceFragment(new FragmentWuliuSetting());
			break;

		default:
			break;
		}
	}

	private void replaceFragment(Fragment fragment) {
		fragmentManager.beginTransaction().replace(R.id.container, fragment)
				.commit();
	}
}
