/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.base.BaseBean;
import com.eims.tjxl_andorid.common.CommonConfrimCancelDialog;
import com.eims.tjxl_andorid.entity.User;
import com.eims.tjxl_andorid.entity.UserAdressBean;
import com.eims.tjxl_andorid.entity.UserAdressBean.AdressBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.HeadView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:
 * @Author zd
 * @date 2015年6月29日 上午8:15:54
 *************************************************************************** 
 */
public class AdressListActivity extends BaseActivity {

	//add by kuangyong
	public static final String IS_USERINFO = "is_userinfo";
	private static final String ADRESS_ID = "adress_id";
	private static final int ADRESS_DEL = 1;// 删除收货地址
	private boolean isUserInfo;// 是否是个人信息页面条转过来
	private static final String IS_NOT_DEFAULT="is_not_default";

	private HeadView headView;
	private ListView mListView;
	private User user;
	private List<AdressBean> data = new ArrayList<UserAdressBean.AdressBean>();
	private AdressAdapter mAdapter;
	public static AdressListActivity instance;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adress_layout);
		isUserInfo = getIntent().getBooleanExtra(IS_USERINFO, false);
		findviews();
		instance = this;
		setActionBar();
		loaddata();
		setListener();
	}

	private void findviews() {
		headView = (HeadView) findViewById(R.id.headview);
		mListView = (ListView) findViewById(R.id.adress_listview);
	}

	private void setActionBar() {
		// TODO Auto-generated method stub
		headView.setText("收货信息");
		headView.setGobackVisible();
		headView.setRightGone();
		headView.showRightText("新增收货地址", new ActionRightClick());
	}

	private void setListener() {
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				if ("-1".equals(data.get(position).df_delivery_address_id)) {//点击非默认地址
					SetDefaultAdress(data.get(position).id);
				}else{//点击默认地址
					if (!isUserInfo) {//提交订单页面  change by kuangyong
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						bundle.putSerializable("adress", data.get(position));
						intent.putExtras(bundle);
						setResult(1, intent);
						AdressListActivity.this.finish();
					}
				}
			}
		});
	}

	public void loaddata() {
		user = AppContext.getLocalUserInfo(mContext);

		CustomResponseHandler mHandler = new CustomResponseHandler(this, true,
				"正在加载...") {

			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				if (statusCode == 200) {
					LogUtil.d(TAG, content);
					UserAdressBean bean = GsonUtils.json2bean(content,
							UserAdressBean.class);
					data.clear();
					data.addAll(bean.data);
					// LogUtil.d(TAG, bean.data.get(0).address_show);
					if (mAdapter == null) {
						mAdapter = new AdressAdapter();
					}
					if(null!=data&&data.size()==0){
						showToast("亲，暂无收货地址");
					}
					mListView.setAdapter(mAdapter);
				}
			}
		};
		RequestParams params = new RequestParams();
		params.put("uid", user.id);
		HttpClient.Query_Adress(mHandler, params);
	}

	class ActionRightClick implements OnClickListener {
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ActivitySwitch.openActivity(AddNewAdressActivity.class, null,
					AdressListActivity.this);
		}

	}

	public void DelItemAdress(final String id) {
		CustomResponseHandler mHandler = new CustomResponseHandler(this, true,
				"正在删除...") {
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				if (statusCode == 200) {
					LogUtil.d(TAG, content);
					delItemAndRefresh(id);// 静态刷新并删除
				}
			}
		};
		RequestParams params = new RequestParams();
		params.put("id", id);
		HttpClient.Del_Adress(mHandler, params);
	}

	private void delItemAndRefresh(String id) {
		if (null != data && data.size() != 0) {
			int delIndex = -1;
			for (int i = 0; i < data.size(); i++) {
				if (data.get(i).id.equals(id)) {
					delIndex = i;
				}
			}
			if (-1 != delIndex) {
				data.remove(delIndex);
			}
		}
		if (null != mAdapter) {
			mAdapter.notifyDataSetChanged();
		}
	}

	public void SetDefaultAdress(final String id) {
		CustomResponseHandler mHandler = new CustomResponseHandler(this, true,
				"正在修改...") {
			@Override
			public void onSuccess(int statusCode, String content) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, content);
				if (statusCode == 200) {
					LogUtil.d(TAG, "设置默认地址：" + content);
					BaseBean bean=BaseBean.explainJson(content,mContext);
					if(null!=bean){
						changeDefaultAdress(id);
					}
				}
			}
		};
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("uid", user.id);
		HttpClient.Set_defaultAdress(mHandler, params);
	}

	// private void updataCheck(String id){
	// if(data!=null&&data.size()>0){
	// for(AdressBean temp : data){
	// if(temp.id.equals(id)){
	// temp.df_delivery_address_id="-1";
	// }else{
	// temp.df_delivery_address_id="1";
	// }
	//
	// }
	// }
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case ADRESS_DEL:// 删除收货地址
				if (null != data) {
					String id = data.getStringExtra(ADRESS_ID);
					DelItemAdress(id);
				}
				break;
			}
		}
	}

	/**
	 * 静态刷新默认地址
	 * @param id
	 */
	private void changeDefaultAdress(String id){
		int updIndex = -1;
		if (null != data && data.size() != 0) {
			for (int i = 0; i < data.size(); i++) {
				data.get(i).df_delivery_address_id="-1";
				if (data.get(i).id.equals(id)) {
					updIndex = i;
				}
			}
			if (-1 != updIndex) {
				data.get(updIndex).df_delivery_address_id="1";
			}
		}
		if (null != mAdapter) {
			mAdapter.notifyDataSetChanged();
		}
		if (!isUserInfo) {//change by kuangyong
			AdressBean bean = null;
			if (-1 != updIndex) {
				bean=data.get(updIndex);
			}
			LogUtil.d("onItemClick", bean.address_show);
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("adress", bean);
			intent.putExtras(bundle);
			setResult(1, intent);
			AdressListActivity.this.finish();
		}
	}

	class AdressAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data == null ? 0 : data.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int postion, View covertview, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Viewholder holder = null;
			boolean flag = false;
			if (covertview == null) {
				holder = new Viewholder();
				covertview = View.inflate(mContext, R.layout.adress_list_item,
						null);
				holder.iv_adress = (ImageView) covertview.findViewById(R.id.iv_adress);
//				holder.cb = (CheckBox) covertview.findViewById(R.id.cb_adress);
				holder.uname = (TextView) covertview
						.findViewById(R.id.mtv_name);
				holder.uphone = (TextView) covertview
						.findViewById(R.id.mtv_phone);
				holder.udetailAdress = (TextView) covertview
						.findViewById(R.id.mtv_adress);
				holder.btn_modfiy = (TextView) covertview
						.findViewById(R.id.modfiy_adress);
				holder.btn_del = (TextView) covertview
						.findViewById(R.id.del_adress);
				covertview.setTag(holder);
			} else {
				holder = (Viewholder) covertview.getTag();
			}
			final AdressBean adressBean = data.get(postion);
			if (adressBean != null) {
				holder.uname.setText(adressBean.consignee_name);
				holder.uphone.setText(adressBean.consignee_phone);
				holder.udetailAdress.setText(adressBean.address_show
						+ adressBean.address);
				if ("-1".equals(adressBean.df_delivery_address_id)) {
//					holder.cb.setChecked(false);
//					holder.cb.setTag(IS_NOT_DEFAULT);
					holder.iv_adress.setImageResource(R.drawable.wgxx);
					// flag=false;
				} else {
					holder.iv_adress.setImageResource(R.drawable.gxx);
//					holder.cb.setChecked(true);
//					holder.cb.setTag(IS_DEFAULT);
					// flag=true;
				}
			}

			holder.btn_modfiy.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("adressBean", adressBean);
					ActivitySwitch.openActivity(AddNewAdressActivity.class,
							bundle, AdressListActivity.this);
				}
			});

			holder.btn_del.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					// data.remove(postion);
					// notifyDataSetChanged();
					// DelItemAdress(adressBean.id);

					//change by kuangyong
					Intent intentHead = new Intent(mContext,
							CommonConfrimCancelDialog.class);
					intentHead.putExtra(CommonConfrimCancelDialog.TASK,
							CommonConfrimCancelDialog.TASK_DELETE_ADRESS);
					intentHead.putExtra(ADRESS_ID, adressBean.id);
					startActivityForResult(intentHead, ADRESS_DEL);
				}
			});
			holder.iv_adress.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if ("-1".equals(adressBean.df_delivery_address_id)) {//点击非默认地址
						SetDefaultAdress(adressBean.id);
					}else{//点击默认地址
						if (!isUserInfo) {//提交订单页面  change by kuangyong
							Intent intent = new Intent();
							Bundle bundle = new Bundle();
							bundle.putSerializable("adress", adressBean);
							intent.putExtras(bundle);
							setResult(1, intent);
							AdressListActivity.this.finish();
						}
					}
				}
			});

//			holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//				@Override
//				public void onCheckedChanged(CompoundButton cb, boolean arg1) {
////					if (arg1) {
////						// updataCheck(adressBean.id);
////						LogUtil.d("adapter", arg1 + "");
////						SetDefaultAdress(adressBean.id);
////					} else {
////						arg0.setChecked(false);
////					}
//
//					//change by kuangyong
//					if (arg1) {
//						String tag = (String) cb.getTag();
//						if (IS_NOT_DEFAULT.equals(tag)) {//如果不是点击默认地址
//							SetDefaultAdress(adressBean.id);
//						}
//					} else {
//						cb.setChecked(true);
//					}
//				}
//			});

			return covertview;
		}

	}

	class Viewholder {
//		CheckBox cb;
		ImageView iv_adress;
		TextView uname;
		TextView uphone;
		TextView udetailAdress;
		TextView btn_modfiy;
		TextView btn_del;
	}

}
