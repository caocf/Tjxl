package com.eims.tjxl_andorid.ui.setting;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.utils.SharedPreferencesUtils;

public class PictQualitySettingFragment extends Fragment {

	/*
	 * 图片设置模式选项
	 */
	public static final String KEY_PIC_MODE = "pic_mode";
	public static final String PIC_MODE_SMART = "智能模式";
	public static final String PIC_MODE_TOP_SPEED = "极间模式(省流量)";
	public static final String PIC_MODE_INFLUENCY = "流畅模式(移动网络)";
	public static final String PIC_MODE_HIGH_DEFINITION = "高清模式(WiFi网络)";

	private RadioGroup radioGroup;
	private ListView listView;
	private String pic_mode;

	private ModeAdapter modeAdapter;
	private List<Mode> modes;
	private int modeIndex = 0;
	
	private LayoutInflater minInflater;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		modes = new ArrayList<Mode>();
		addModes();
		modeAdapter = new ModeAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pic_quality_setting,
				null);
		minInflater = inflater;
		getNowMode();
		// setRaidoButtoms(view);
		setListView(view);

		return view;
	}

	/**
	 * 使用ListView模式
	 */
	private void setListView(View view) {
		listView = (ListView) view.findViewById(R.id.listview);
		listView.setAdapter(modeAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				modeIndex = arg2;
				modeAdapter.notifyDataSetChanged();
			}
		});
	}

	private void addModes() {
		modes.add(new Mode(0, PIC_MODE_SMART));
		modes.add(new Mode(1, PIC_MODE_TOP_SPEED));
		modes.add(new Mode(2, PIC_MODE_INFLUENCY));
		modes.add(new Mode(3, PIC_MODE_HIGH_DEFINITION));
	}
	
	/**
	 * 使用RadioGroup模式
	 * 
	 * @param view
	 */
	private void setRaidoButtoms(View view) {
		radioGroup = (RadioGroup) view.findViewById(R.id.radiogroup);

		RadioButton btn_smart = (RadioButton) view.findViewById(R.id.btn_smart);
		RadioButton btn_top_speed = (RadioButton) view
				.findViewById(R.id.btn_top_speed);
		RadioButton btn_influency = (RadioButton) view
				.findViewById(R.id.btn_influency);
		RadioButton btn_high_difinition = (RadioButton) view
				.findViewById(R.id.btn_high_definition);
		btn_smart.setText(PIC_MODE_SMART);
		btn_top_speed.setText(PIC_MODE_TOP_SPEED);
		btn_influency.setText(PIC_MODE_INFLUENCY);
		btn_high_difinition.setText(PIC_MODE_HIGH_DEFINITION);

		if (pic_mode.equals(PIC_MODE_SMART)) {
			btn_smart.setChecked(true);
		} else if (pic_mode.equals(PIC_MODE_TOP_SPEED)) {
			btn_top_speed.setChecked(true);
		} else if (pic_mode.equals(PIC_MODE_INFLUENCY)) {
			btn_influency.setChecked(true);
		} else if (pic_mode.equals(PIC_MODE_HIGH_DEFINITION)) {
			btn_high_difinition.setChecked(true);
		}

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.btn_smart:
					pic_mode = PIC_MODE_SMART;
					break;
				case R.id.btn_top_speed:
					pic_mode = PIC_MODE_TOP_SPEED;
					break;
				case R.id.btn_influency:
					pic_mode = PIC_MODE_INFLUENCY;
					break;
				case R.id.btn_high_definition:
					pic_mode = PIC_MODE_HIGH_DEFINITION;
					break;

				default:
					break;
				}

			}
		});
	}


	@Override
	public void onPause() {
		super.onPause();
		SharedPreferencesUtils.setPicQualityMode(
				PictQualitySettingFragment.this.getActivity(), modeIndex);
	}

	private void getNowMode() {
		modeIndex = SharedPreferencesUtils.getPicQualityMode(getActivity());
	}

	class ModeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return modes.size();
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
			if(view == null){
				view = minInflater.inflate(R.layout.layout_pic_mode_item, null);
			}
			ImageView imageView = (ImageView) view.findViewById(R.id.iv_select);
			TextView textView = (TextView) view.findViewById(R.id.mode_title);
			Mode mode = modes.get(arg0);
			textView.setText(mode.getTitle());
			if(mode.getModeIndex() == modeIndex){
				textView.setTextColor(PictQualitySettingFragment.this.getActivity().getResources().getColor(R.color.black));
				imageView.setVisibility(View.VISIBLE);
			}else {
				textView.setTextColor(PictQualitySettingFragment.this.getActivity().getResources().getColor(R.color.gray));
				imageView.setVisibility(View.INVISIBLE);
			}
			return view;
		}

	}

	class Mode {
		private int modeIndex;
		private String title;
		private boolean isSelected = false;

		public Mode(int index, String title){
			this.modeIndex = index;
			this.title = title;
		}
		public int getModeIndex() {
			return modeIndex;
		}

		public void setModeIndex(int modeIndex) {
			this.modeIndex = modeIndex;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}
	}
}
