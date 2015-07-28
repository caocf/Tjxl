package com.eims.tjxl_andorid.ui.user.complain;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.ComplainReturnBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.weght.HeadView;

import org.apache.http.Header;

import loopj.android.http.RequestParams;

/**
 * 申诉查询
 * Created by kuangyong on 2015/7/3.
 */
public class AppealInquiryActivity extends BaseActivity implements View.OnClickListener{

	private com.eims.tjxl_andorid.weght.HeadView head;
	private android.widget.EditText etvalidatecode;
	private android.widget.EditText et_pwd;
	private android.widget.TextView btnnext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_appeal_inquiry);
        findView();
        setListener();
        initheadview();
    }

    private void initheadview() {
        head.setText("申诉结果查询");
        head.setGobackVisible();
        head.setRightResource();
        head.setRightGone();
        head.showRightText("填写申诉", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivitySwitch.openActivity(FillInComplainActivity.class,null,AppealInquiryActivity.this);
            }
        });
    }

    private void findView() {
        this.btnnext = (TextView) findViewById(R.id.btn_next);
        this.etvalidatecode = (EditText) findViewById(R.id.et_validate_code);
        this.et_pwd = (EditText) findViewById(R.id.et_pwd);
        this.head = (HeadView) findViewById(R.id.head);
    }
    private void setListener() {
        btnnext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next://查询
                if(TextUtils.isEmpty(etvalidatecode.getText().toString())){
                    Toast.makeText(mContext,"请输入申诉编号",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(et_pwd.getText().toString())){
                    Toast.makeText(mContext,"请输入申诉密码",Toast.LENGTH_SHORT).show();
                }else{
                    iIsExitsByCodePwd();
                }
                break;
        }
    }

    private void iIsExitsByCodePwd(){
        CustomResponseHandler handler=new CustomResponseHandler(mContext,true,"正在查询..."){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                ComplainReturnBean returnBean=ComplainReturnBean.explainJson(content,mContext);
                if(null!=returnBean){
                    if(-1==returnBean.getType()){//申诉编号或密码错误
                        showToast("申诉编号或密码错误，请核对！");
                    }else{
                        Bundle bundle=new Bundle();
                        bundle.putSerializable(ComplainResultActivity.RETURN_BEAN,returnBean);
                        ActivitySwitch.openActivity(ComplainResultActivity.class,bundle,AppealInquiryActivity.this);
                    }
                }
            }
        };
        RequestParams params=new RequestParams();
        params.put("appeal_code",etvalidatecode.getText().toString());
        params.put("appeal_pwd",et_pwd.getText().toString());
        HttpClient.iIsExitsByCodePwd(handler,params);
    }
}
