package com.eims.tjxl_andorid.ui.setting;

import loopj.android.http.RequestParams;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.utils.JSONParseUtils;

@SuppressLint("NewApi")
public class FeedbackSettingFragment extends Fragment{
	
	private EditText editText;
	private Spinner spinner;
	private Button btnCommit;
	
	private String [] feedbackTypes = new String[]{"请选择","功能建议","功能投诉","APP异常","其他"};
	private String feedbackType;
	private String feedbackContent;
	
	private int typeIndex;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_feedback_setting, null);
		editText = (EditText) view.findViewById(R.id.edittext);
		spinner = (Spinner) view.findViewById(R.id.spinner);
		btnCommit = (Button) view.findViewById(R.id.btn_commit);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.layout_spinner_item, feedbackTypes);
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				typeIndex = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				typeIndex = 0;
			}
		});
		
		btnCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(typeIndex == 0){
					Toast.makeText(getActivity(), "请选择举报类型", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(editText.getText().toString()==null || editText.getText().toString().length()==0){
					showToast("请填写举报信息");
					return;
				}
				commitFeedback();
			}
		});
		return view;
	}
	
	private void commitFeedback(){
		CustomResponseHandler mHandler = new CustomResponseHandler(this.getActivity(),
				true, "正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				Log.d(TAG, "factory message result:" + content);
				if (JSONParseUtils.isErrorJSONResult(content)) {
					showToast("提交失败");
					return;
				} else {
					showToast("提交成功");
					spinner.setSelection(0);
					editText.setText("");
					editText.setHint("请输入举报信息");
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Log.d(TAG, "onFailure: content:" + content);
				showToast("提交失败");
			}
		};
		RequestParams params = new RequestParams();
		params.put("type", typeIndex+"");
		params.put("description", editText.getText().toString());
		HttpClient.commitFeedbackMessage(mHandler, params);
	}
	
	private void showToast(String message){
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}
}
