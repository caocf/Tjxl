/**
 * 
 */
package com.eims.tjxl_andorid.ui.user;

import loopj.android.http.RequestParams;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 
 * @Author zd
 * @date 2015年6月23日  上午9:43:51
 *************************************************************************** 
 */
public class RegisterActivity extends BaseActivity {
	
	private HeadView head;
    private RadioGroup  mRegisterType;
    private FragmentManager  fm;
    private FragmentTransaction  ft;
	private ShoeStoreFragment mLeftFragment;
	private ShoeFactoryFragment mRightFragment;
	private View left_line,right_line;
	private TextView mVipNumber;
	private LinearLayout  ll_viplist;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		findviews();
		initActionBar();
		fm=getSupportFragmentManager();
		mLeftFragment=new ShoeStoreFragment();
		mRightFragment=new ShoeFactoryFragment();
		setListener();
		InitData();
		GetRegisterList();
	}
      
	private void initActionBar() {
		head.setText("注册");
		head.setRightGone();
		head.showRightText("登录", new RightBtnClick());
	}
	
	private void findviews() {
		head = (HeadView) findViewById(R.id.head);
		mRegisterType=(RadioGroup) findViewById(R.id.rigster_type);
		left_line=findViewById(R.id.left_line);
		right_line=findViewById(R.id.right_line);
		mVipNumber=(TextView) findViewById(R.id.vip_number);
		ll_viplist=(LinearLayout) findViewById(R.id.ll_viplist);
	}
	
	private void setListener(){
		mRegisterType.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				switch (checkedId) {
				case R.id.shoe_store:
					left_line.setBackgroundColor(getResources().getColor(R.color.sheng_red));
					right_line.setBackgroundColor(getResources().getColor(R.color.background_gray));
					ft = fm.beginTransaction();
					ft.replace(R.id.content, mLeftFragment);
					break;
               case R.id.shoe_factory:
            		left_line.setBackgroundColor(getResources().getColor(R.color.background_gray));
					right_line.setBackgroundColor(getResources().getColor(R.color.sheng_red));
            	   ft = fm.beginTransaction();
					ft.replace(R.id.content, mRightFragment);
					break;
				default:
					break;
				}
				ft.commit();
			}
		});
		
		ll_viplist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ActivitySwitch.openActivity(RegisterListActivity.class, null, RegisterActivity.this);
			}
		});
	}
	
	private void InitData() {
		ft = fm.beginTransaction();
		ft.replace(R.id.content, mLeftFragment);
		ft.commit();
	}
	/**查询鞋店会员数**/
	private void GetRegisterList(){
		CustomResponseHandler  mHandler=new CustomResponseHandler(mContext, false,""){
		         
		          	@Override
		          	public void onSuccess(int statusCode, String content) {
		          		// TODO Auto-generated method stub
		          		super.onSuccess(statusCode, content);
		          		LogUtil.d(TAG, content);
		          		try {
		          			JSONObject  json=new JSONObject(content);
		          			String msg = json.getString("msg");
		          			if("1".equals(json.getString("type"))){
		          			   mVipNumber.setText(msg);
		          			}else{
			          			TipToast.makeText(mContext, msg, 0).show();
		          			}
		          			
						} catch (Exception e) {
							e.printStackTrace();
						}
		          		
		          	}
		          	@Override
		          	public void onFailure(Throwable error, String content) {
		          	// TODO Auto-generated method stub
		          	super.onFailure(error, content);
		          	}
		};
		RequestParams  params=new RequestParams();
		HttpClient.QueryVIPNumber(mHandler, params);
	}
	
	class RightBtnClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ActivitySwitch.openActivity(LoginActivity.class, null, RegisterActivity.this);
		}
		
	}
	
}
