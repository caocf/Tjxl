package com.eims.tjxl_andorid.ui.user.complain;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.ComplainResultBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 * Created by kuangyong on 2015/7/2.
 */
public class FillInComplainActivity extends BaseActivity implements View.OnClickListener{
    private HeadView head;
	private TextView btn_commit;// 底部按钮
    private ComplainEditFragment complainEditFragment;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.layout_fill_in_complain);
        findViews();
        setlistener();
        initheadview();
    }

    private void setlistener() {
        btn_commit.setOnClickListener(this);
    }

    private void findViews() {
        this.head = (HeadView) findViewById(R.id.head);
        this.btn_commit = (TextView) findViewById(R.id.btn_commit);
        complainEditFragment=ComplainEditFragment.newInstance();
        complainEditFragment.setOnSavaUserInfoResultListener(new ComplainEditFragment.OnSavaUserInfoResultListener() {

            @Override
            public void callBackResult(ComplainResultBean bean) {
                Bundle bundle=new Bundle();
                bundle.putSerializable(ComplainTipActivity.BEAN,bean);
                ActivitySwitch.openActivity(ComplainTipActivity.class,bundle,FillInComplainActivity.this);
                finish();
            }
        });
        showFragment(complainEditFragment, R.id.frame_fill_in_complain);
    }

    private void initheadview() {
        head.setText("填写申诉内容");
        head.setGobackVisible();
        head.setRightResource();
        head.setRightGone();
        head.showRightText("查询申诉结果", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findComplainResult();
            }
        });
    }

    /**
     * 查询申诉结果
     */
    private void findComplainResult(){
        ActivitySwitch.openActivity(AppealInquiryActivity.class,null,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:// 底部按钮
                commit();
                break;
        }
    }

    /**
     * 提交申诉
     */
    private void commit(){
        complainEditFragment.commitComplain();
    }

}
