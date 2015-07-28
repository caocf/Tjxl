/**
 *
 */
package com.eims.tjxl_andorid.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.IQueryLetterInfoBean;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.HeadView;

import loopj.android.http.RequestParams;

/**
 * 消息详情 Created by kuangyong on 2015/6/30.
 */
public class MessageDetailActivity extends BaseActivity {

	public static final String MESSAGE_ID = "message_id";
	private HeadView headView;
	private IQueryLetterInfoBean bean;
	private String id;// 消息id
	private android.widget.TextView tvmsgcontent;
	private android.widget.TextView tvmsgtime;
	private android.widget.ScrollView svmain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail_layout);
		id = getIntent().getStringExtra(MESSAGE_ID);
		findview();
		iQueryLetterInfo();
	}

	private void setActionBar() {
		// TODO Auto-generated method stub
		headView.setText(bean.getData().getSend_title());
		headView.setGobackVisible();
		headView.setRightGone();
	}

	private void findview() {
		headView = (HeadView) findViewById(R.id.head);
        this.svmain = (ScrollView) findViewById(R.id.sv_main);
        this.tvmsgtime = (TextView) findViewById(R.id.tv_msg_time);
        this.tvmsgcontent = (TextView) findViewById(R.id.tv_msg_content);
	}

	private void initView() {
        svmain.setVisibility(View.VISIBLE);
        tvmsgcontent.setText(bean.getData().getSend_content());
        tvmsgtime.setText(bean.getData().getSend_time());
        setActionBar();
	}


	/**
	 * 获取消息详情
	 */
	private void iQueryLetterInfo() {
		CustomResponseHandler handler = new CustomResponseHandler(mContext,
				true, "正在加载...") {
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
				if (LogUtil.isDebug)
					Log.i(TAG, "获取消息详情数据：" + content);
				bean = IQueryLetterInfoBean.explainJson(content, mContext);
				if (null != bean) {
					initView();
					Intent intent=getIntent();
					Bundle bundle=new Bundle();
					bundle.putSerializable(MessageListActivity.MESSAGE_DETAIL_BEAN,bean);
					intent.putExtra("bundle",bundle);
					setResult(RESULT_OK, intent);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				Toast.makeText(mContext, "网络错误", Toast.LENGTH_LONG).show();
			}
		};
		RequestParams params = new RequestParams();

		 params.put("uid", AppContext.getLocalUserInfo(mContext).id);//TODO
		params.put("id", id + "");
		HttpClient.iQueryLetterInfo(handler, params);
    }
}
