package com.eims.tjxl_andorid.ui.user.complain;

import android.os.Bundle;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.ComplainReturnBean;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 * 申诉结果 Created by kuangyong on 2015/7/3.
 */
public class ComplainResultActivity extends BaseActivity {
	public static final String RETURN_BEAN = "return_bean";
	private ComplainReturnBean returnBean;
	private HeadView head;
    private ComplainSuccessFragment complainSuccessFragment;//申诉成功
    private ComplainFailedFragment complainFailedFragment;//申诉失败

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_complain_result);
		returnBean = (ComplainReturnBean) getIntent().getSerializableExtra(
				RETURN_BEAN);
		findViews();
		initView();
		initheadview();
	}

	private void initView() {
        int type=returnBean.getType();
        if(type>0){//成功
            complainSuccessFragment=ComplainSuccessFragment.newInstance(R.layout.frame_conplain_success,returnBean);
            showFragment(complainSuccessFragment,R.id.frame_complain_result);
        }else{//失败
			complainFailedFragment=ComplainFailedFragment.newInstance(R.layout.frame_conplain_failed,returnBean);
			showFragment(complainFailedFragment,R.id.frame_complain_result);
        }
	}

	private void findViews() {
		this.head = (HeadView) findViewById(R.id.head);
	}

	private void initheadview() {
		head.setText("申诉结果查询");
		head.setGobackVisible();
        head.setRightGone();
    }
}
