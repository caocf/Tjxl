package com.eims.tjxl_andorid.ui;

import java.io.InputStream;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.ui.user.RegisterActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
public class GuideFragment extends Fragment{
	
	private static final String TAG = "GuideFragment";
	
	private int pageIndex;
	public GuideFragment() {};
	public GuideFragment(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("zhiheng"," oncreateView");
		View view = inflater.inflate(R.layout.fragment_guide_item, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.image_guide);
		View btnView = view.findViewById(R.id.ll_bottom);
		switch (pageIndex) {
		case 0:
			imageView.setImageBitmap(getBitmap(R.drawable.gudie1));
			break;
		case 1:
			imageView.setImageBitmap(getBitmap(R.drawable.gudie2));
			break;
		case 2:
			imageView.setImageBitmap(getBitmap(R.drawable.gudie03_new));
			btnView.setVisibility(View.VISIBLE);
			setOnClick(view);
			break;
			
		default:
			break;
		}
		return view;
	}
	
	public Bitmap getBitmap(int resId) {
		 InputStream is = getActivity().getResources().openRawResource(resId);
		 return BitmapFactory.decodeStream(is);
	}
	
	public void setOnClick(View view) {
		ImageView guanguan = (ImageView) view.findViewById(R.id.go_guangguangbtn);
		ImageView regButton = (ImageView) view.findViewById(R.id.go_register);
		guanguan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ActivitySwitch.openActivity(MainActivity.class, null, GuideFragment.this.getActivity());
			 	GuideFragment.this.getActivity().finish();
			}
		});
		
		regButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ActivitySwitch.openActivity(RegisterActivity.class, null, GuideFragment.this.getActivity());
			 	GuideFragment.this.getActivity().finish();
			}
		});
	}
}
