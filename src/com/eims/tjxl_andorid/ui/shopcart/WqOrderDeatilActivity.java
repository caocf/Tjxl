/**
 * 
 */
package com.eims.tjxl_andorid.ui.shopcart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import loopj.android.http.RequestParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.entity.WqDeatilBean;
import com.eims.tjxl_andorid.entity.WqDeatilBean.WqComment;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.ImageManager;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.TipToast;
import com.eims.tjxl_andorid.utils.imageupload.Tip;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName: 
 * @Description: 维权详情
 * @Author zd
 * @date 2015年7月7日  下午3:45:31
 *************************************************************************** 
 */
public class WqOrderDeatilActivity  extends  BaseActivity implements OnClickListener{
	public static final String TAG = "WqOrderDeatilActivity";
	public static final String FROM_TYPE="from";
	public static String DDWQ="0";//订单维权
	public static String THWQ="1";//退货维权
	public static String HHWQ="2";//换货维权
	public static String PLWQ="3";//评论维权
	private  HeadView head;
	private MyListView mListView;
	private ImageView goodImage;
	private TextView goodName;
	private Button btn_contactSeller,btn_showWqList,undoWq;//联系卖家，查看维权清单，撤销维权
	private TextView wqType,qwclHandling;//维权类型，期望处理方式
	private TextView wqDesc;//维权说明
	//维权状态，维权申请时间，鞋联介入时间，维权完成时间
	private TextView wqStatus,applywqTime,xielianTime,wqCompleteTime;
	private Button btn_cxwq,btn_wqtg;//底部按钮（撤销维权,维权通过）
	private EditText mMeComment;
	private Button btnComment;
	private String wqId;
	private String thorderId;
	private WqDeatilBean bean;
	private List<WqComment> wqComments=new ArrayList<WqComment>();
	private CommentAdapter commentAdapter;
	private String flag;
	private 	Bundle  bundle;
	private LinearLayout ll_btn;
	public  static  String  WQ_REFRESH="wq_refresh.action";
	private RelativeLayout  rl_root;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wqorder_detail_layout);
		findviews();
		initActionBar();
		loaddata();
	}
      private  void  findviews(){
    	  head=(HeadView) findViewById(R.id.head);
    	  mListView=(MyListView) findViewById(R.id.comment_listview);
    	  goodImage=(ImageView) findViewById(R.id.good_image);
    	  goodName=(TextView) findViewById(R.id.good_name);
    	  btn_contactSeller=(Button) findViewById(R.id.btn_contactSeller);
    	  btn_showWqList=(Button) findViewById(R.id.btn_showWqList);
    	  undoWq=(Button) findViewById(R.id.undoWq);
    	  wqType=(TextView) findViewById(R.id.wqType);
    	  qwclHandling=(TextView) findViewById(R.id.qwclHandling);
    	  wqDesc=(TextView) findViewById(R.id.wqDesc);
    	  wqStatus=(TextView) findViewById(R.id.wqstatus);
    	  applywqTime=(TextView) findViewById(R.id.applywqTime);
    	  xielianTime=(TextView) findViewById(R.id.xielianTime);
    	  wqCompleteTime=(TextView) findViewById(R.id.wqCompleteTime);
    	  btn_cxwq=(Button) findViewById(R.id.btn_cxwq);
    	  mMeComment=(EditText) findViewById(R.id.me_comment);
    	  btnComment=(Button) findViewById(R.id.btn_commint_comment);
    	  btn_wqtg=(Button) findViewById(R.id.btn_wqtg);
    	  ll_btn=(LinearLayout) findViewById(R.id.ll_cxwq);
    	  rl_root=(RelativeLayout) findViewById(R.id.root);
    	  if(getIntent().getExtras()!=null){
    		  Bundle bundle = getIntent().getExtras();
    		  wqId= bundle.getString("wqid");
    		  thorderId=bundle.getString("thorderid");
    		  flag = bundle.getString(FROM_TYPE);
    	  }
    	  if(commentAdapter==null){
    		  commentAdapter=new CommentAdapter();
    	  }
	
    	  mListView.setAdapter(commentAdapter);
      }
	  private void initActionBar() { 
	    	 head.setText("订单详情");
	 		 head.setRightGone();
	 	}
	  
	  private  void  showUI(){
		  if(bean!=null){
			  goodName.setText(bean.map.commodity_name);
			  ImageManager.Load(bean.map.commodity_img_m, goodImage);
			  wqType.setText(bean.map.up_type_name );
			  qwclHandling.setText(bean.map.expect_way);
			  wqDesc.setText(bean.map.description);
			  wqStatus.setText(bean.map.up_status_name);
			  applywqTime.setText(bean.map.time0);
			  xielianTime.setText(bean.map.time2);
			  wqCompleteTime.setText(bean.map.time3);
		
				wqComments.clear();
				wqComments.addAll(bean.list);
				commentAdapter.notifyDataSetChanged();
				//评论维权  没有维权清单
				if(PLWQ.equals(flag)){
					btn_showWqList.setVisibility(View.GONE);
					if(!"3".equals(bean.map.uygur_power_statu)&& !"4".equals(bean.map.uygur_power_statu)){
						ll_btn.setVisibility(View.VISIBLE);
						btn_wqtg.setVisibility(View.VISIBLE);
					}else{
						ll_btn.setVisibility(View.GONE);
						btn_wqtg.setVisibility(View.GONE);
					}
					btn_cxwq.setVisibility(View.GONE);
				}else{
					ll_btn.setVisibility(View.VISIBLE);
					btn_showWqList.setVisibility(View.VISIBLE);
					btn_wqtg.setVisibility(View.GONE);		
					if(!"3".equals(bean.map.up_status)&&!"4".equals(bean.map.up_status)){
						btn_cxwq.setVisibility(View.VISIBLE);
					}else{
						btn_cxwq.setVisibility(View.GONE);
					}
				}
				
				setListener();
		  } 
	  }
	  /**协商记录做局部刷新**/
	  private  void  LocalRefresh(String contentRecord){
		     WqComment temp=new WqComment();
             Date d = new Date();  
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
             String dateNowStr = sdf.format(d);  
             temp.addtime=dateNowStr;
             temp.user_type="2";
             temp.content=contentRecord;
             wqComments.add(temp);
             commentAdapter.notifyDataSetChanged();
             mMeComment.setText("");
	  }
	  
	  /**设置监听**/
	  private void setListener(){
		  btnComment.setOnClickListener(this);
		  btn_showWqList.setOnClickListener(this);
		  btn_wqtg.setOnClickListener(this);
		  btn_cxwq.setOnClickListener(this);
	//	  rl_root.setOnClickListener(this);
	  }
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_commint_comment:
				String content = mMeComment.getText().toString();
				if(TextUtils.isEmpty(content)){
					TipToast.makeText(mContext, "请输入你要回复的内容", 0).show();
					return;
				}
				addComment(content);
				break;
			case R.id.btn_showWqList://查看维权清单			
				  ToActivity(flag);
				break;
			case R.id.btn_wqtg:
				CommentStatusOk();
				break;
			case R.id.btn_cxwq:
		     	CXWQ();
				break;
			default:
				break;
			}
		}
		
		private  void  ToActivity(String type){
			bundle = new Bundle();
			bundle.putString("wqid", wqId);
			bundle.putString("thorderid", thorderId);//订单来源id
			bundle.putString(WqOrderDeatilActivity.FROM_TYPE,type);
			ActivitySwitch.openActivity(WqOrderGoodListActivity.class, bundle,WqOrderDeatilActivity.this);
		}
	  
	  private  void  loaddata(){
		  CustomResponseHandler  mHandler=new CustomResponseHandler(mContext, true, "正在加载..."){
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				LogUtil.d(TAG, content);
				try {
					JSONObject obj=new JSONObject(content);
					String type = obj.getString("type");
					if("1".equals(type)){
						bean = GsonUtils.json2bean(content, WqDeatilBean.class);
						showUI();			
					}else{
						TipToast.makeText(mContext, obj.getString("msg"), 0).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				TipToast.makeText(mContext, "数据加载失败...",0).show();
			}
		  };
		  RequestParams params=new RequestParams();

		  params.put("uid", user.id);	
		  if(THWQ.equals(flag)){
			  params.put("id",  thorderId);//退货订单id
			  params.put("up_id", wqId);//维权表id
			  HttpClient.ReturnGood_wqDetail(mHandler, params);
		  }else if(HHWQ.equals(flag)){
			  params.put("id",  thorderId);//退货订单id
			  params.put("up_id", wqId);//维权表id
			  HttpClient.iUpdateReplaceUygurPowerInit(mHandler, params);
		  }else if(PLWQ.equals(flag)){
			  params.put("id",  wqId);//退货订单id
			  HttpClient.iUpRviewDetail(mHandler, params);
		  }else if(DDWQ.equals(flag)){
			  
		  }
		  
		 
	  }
	  /**添加协商记录**/
	  private  void  addComment(final String contentRecord){
		  CustomResponseHandler  mHandler=new CustomResponseHandler(mContext, false, ""){
				@Override
				public void onSuccess(int statusCode, String content) {
					super.onSuccess(statusCode, content);
					LogUtil.d(TAG, content);
					try {
						JSONObject obj=new JSONObject(content);
						String type = obj.getString("type");
						if(!"-1".equals(type)){	
	                        LocalRefresh(contentRecord);
						}else{
							TipToast.makeText(mContext, obj.getString("msg"), 0).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					TipToast.makeText(mContext, "回复失败...",0).show();
				}
			  };
			  RequestParams params=new RequestParams();
			  params.put("uid", user.id);
			  params.put("content",  contentRecord);//回复内容
			  params.put("up_id", wqId);//维权表id
			  HttpClient.addRecords(mHandler, params);
	  }
	  
	  /**评论维权通过**/
	  private  void  CommentStatusOk(){
		  CustomResponseHandler  mHandler=new CustomResponseHandler(mContext, true, "正在加载..."){
				@Override
				public void onSuccess(int statusCode, String content) {
					super.onSuccess(statusCode, content);
					LogUtil.d(TAG, content);
					try {
						JSONObject obj=new JSONObject(content);
						String type = obj.getString("type");
						if(!"-1".equals(type)){	
	                         //更新UI
							wqStatus.setText("维权通过");
							ll_btn.setVisibility(View.GONE);
							Intent intent=new Intent();
							intent.setAction(WQ_REFRESH);
							sendBroadcast(intent);
						}else{
							TipToast.makeText(mContext, obj.getString("msg"), 0).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					TipToast.makeText(mContext, "服务器异常",0).show();
				}
			  };
			  RequestParams params=new RequestParams();
			  params.put("uid", user.id);
			  params.put("up_id", wqId);//维权表id
			  HttpClient.iUpdateBuyUygurPowerStatusOK(mHandler, params);
	  }
	  
	  /**撤销维权**/
	  private  void  CXWQ(){
		  CustomResponseHandler  mHandler=new CustomResponseHandler(mContext, true, "正在加载..."){
				@Override
				public void onSuccess(int statusCode, String content) {
					super.onSuccess(statusCode, content);
					LogUtil.d(TAG, content);
					try {
						JSONObject obj=new JSONObject(content);
						String type = obj.getString("type");
						if(!"-1".equals(type)){	
	                         //更新UI
							wqStatus.setText("维权结束");
							ll_btn.setVisibility(View.GONE);
							Intent intent=new Intent();
							intent.setAction(WQ_REFRESH);
							sendBroadcast(intent);
						}else{
							TipToast.makeText(mContext, obj.getString("msg"), 0).show();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				
				}
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					TipToast.makeText(mContext, "服务器异常",0).show();
				}
			  };
			  RequestParams params=new RequestParams();
			  params.put("uid", user.id);
			  params.put("up_id", wqId);//维权表id
			  params.put("id", thorderId);
			  HttpClient.iUpdateBuyUygurPowerStatus(mHandler, params);
	  }
	  
	  /***
	   * 
	   ************************************************************************** 
	   * @Version 1.0
	   * @ClassName: 
	   * @Description: 卖家 买家 鞋联官方对话记录
	   * @Author zd
	   * @date 2015年7月8日  上午9:50:04
	   ***************************************************************************
	   */
	  class CommentAdapter extends BaseAdapter{

		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return wqComments==null ? 0 : wqComments.size();
		}


		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return wqComments.get(arg0);
		}


		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}


		@Override
		public View getView(int arg0, View convertview, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if(convertview==null){
				convertview=View.inflate(mContext, R.layout.wqcomment_layout, null);
			}
			TextView  name=(TextView) convertview.findViewById(R.id.name);
			TextView  time=(TextView) convertview.findViewById(R.id.comment_time);
			TextView  content=(TextView) convertview.findViewById(R.id.content);
			WqComment wqComment = wqComments.get(arg0);
			//(1卖家，2买家，3管理员)
			if(wqComment!=null){
				if("1".equals(wqComment.user_type)){
					name.setText("卖家的回复：");
				}else if("2".equals(wqComment.user_type)){
					name.setText("我的回复：");
				}else if("3".equals(wqComment.user_type)){
					name.setText("管理员的回复：");
				}
				time.setText(wqComment.addtime);
				content.setText(wqComment.content);
			}
			return convertview;
		}
		  
	  }


}
