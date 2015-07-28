package com.eims.tjxl_andorid.ui.user.complain;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.ComplainResultBean;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 * Created by kuangyong on 2015/7/2.
 */
public class ComplainTipActivity extends BaseActivity implements View.OnClickListener{
    public static final String BEAN="bean";
    private ComplainResultBean bean;
    private HeadView head;
	private TextView btn_comfirm;// 底部按钮
    private TextView tv_tip;//提示

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.layout_complain_tip);
        bean=(ComplainResultBean)getIntent().getSerializableExtra(BEAN);
        findViews();
        setlistener();
        initheadview();
    }

    private void setlistener() {
        btn_comfirm.setOnClickListener(this);
    }

    private void findViews() {
        this.head = (HeadView) findViewById(R.id.head);
        this.btn_comfirm = (TextView) findViewById(R.id.btn_comfirm);
        this.tv_tip = (TextView) findViewById(R.id.tv_tip);
    }

    private void initheadview() {
        head.setText("");
        head.setGobackVisible();
        head.setRightGone();
        if(null!=bean.getData()){
            tv_tip.setText("申诉编号："+bean.getData().getAppeal_code()+"\n申诉密码："+bean.getData().getAppeal_pwd()+"（请牢记！）");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_comfirm:
                finish();
                break;
        }
    }

}
