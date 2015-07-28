/**
 * 
 */
package com.eims.tjxl_andorid.ui.product;

import java.util.ArrayList;
import java.util.List;

import loopj.android.http.RequestParams;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.BaseLazyFragment;
import com.eims.tjxl_andorid.ui.product.ProductCommentFragment.CommentDetailBean.ItemBean;
import com.eims.tjxl_andorid.ui.product.RecommendedFragment.MeBuyGoodBean;
import com.eims.tjxl_andorid.utils.GsonUtils;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.weght.MyListView;

/**
 ************************************************************************** 
 * @Version 1.0
 * @ClassName:
 * @Description:评论
 * @Author zd
 * @date 2015年4月17日 上午10:57:49
 *************************************************************************** 
 */
public class ProductCommentFragment extends android.support.v4.app.Fragment {

	protected static final String TAG = null;
	public static ProductCommentFragment productCommentFragment;
	private RadioGroup radioGroup;
	private RadioButton btnAll,btnPraise,btnZhong,btnBad; 
	private MyListView myListView;
	private TextView manyiValue;//满意度
	private CommentAdapter mAdapter;
	private List<ItemBean> dataList=new ArrayList<ItemBean>();
	private TextView tipMsgs;
    private String satisfactionValue;
    private LinearLayout ll_content;
	public static ProductCommentFragment getInstance() {
		if (productCommentFragment == null) {
			productCommentFragment = new ProductCommentFragment();
		}
		return productCommentFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.product_comment_layout, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		findviews();
		initviews();
	}
	
	private  void  findviews(){
		radioGroup=(RadioGroup) getActivity().findViewById(R.id.radiogroup);
		myListView=(MyListView) getActivity().findViewById(R.id.listview);
		btnAll=(RadioButton) getActivity().findViewById(R.id.btn_all);
		btnPraise=(RadioButton) getActivity().findViewById(R.id.btn_parise);
		btnZhong=(RadioButton) getActivity().findViewById(R.id.btn_zhong);
		btnBad=(RadioButton) getActivity().findViewById(R.id.btn_bad);
		manyiValue=(TextView) getActivity().findViewById(R.id.manyi_value);
		tipMsgs=(TextView) getActivity().findViewById(R.id.tips_msg);
		ll_content=(LinearLayout) getActivity().findViewById(R.id.ll_context);
	}
	
	private  void  initviews(){
		btnAll.setChecked(true);
		if(mAdapter==null){
			mAdapter=new CommentAdapter();
		}
		myListView.setAdapter(mAdapter);
	    loadtData("");
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==R.id.btn_all){
				  LogUtil.d(TAG,"check All");
				     loadtData("");
				}else if(checkedId==R.id.btn_parise){
					 loadtData("5");
				}else if(checkedId==R.id.btn_zhong){
					 loadtData("3");
				}else if(checkedId==R.id.btn_bad){
					 loadtData("1");
				}
			}
		});
	}
	private  void  showUi(String value){
		ll_content.removeAllViews();
		Double d = Double.parseDouble(value);
		int p=(new   Double(d)).intValue();  
		ImageView imageView;
	    for(int i=0;i<p;p++){
	    	imageView=new ImageView(getActivity());
	    	imageView.setBackgroundResource(R.drawable.pjxx);
//	    	    LayoutParams params = (LayoutParams) imageView.getLayoutParams();  
//	    	    params.height=20;  
//	    	    params.width =20;  
//	    	    imageView.setLayoutParams(params);  
//	    	    
	        	ll_content.addView(imageView);
	    }
	}
	
	private void  loadtData(String type){
		
//		 默认全部
//		好评5，中评3，差评1
		CustomResponseHandler  mHandler=new CustomResponseHandler(getActivity(), false, ""){
			@Override
		    public void onSuccess(int statusCode, String content) {
		    	// TODO Auto-generated method stub
		    	super.onSuccess(statusCode, content);
		    	LogUtil.d(TAG, content);
		    	if(content.contains("data")){
		    		myListView.setVisibility(View.VISIBLE);
		    		tipMsgs.setVisibility(View.GONE);
		    		 CommentDetailBean bean = GsonUtils.json2bean(content, CommentDetailBean.class);
		    		 satisfactionValue=bean.avgScore;
		    		 manyiValue.setText(satisfactionValue);
		    	 
		    		 dataList.clear();
		    		 dataList.addAll(bean.data);
		    		 mAdapter.notifyDataSetChanged();
		    	//	   showUi(bean.avgScore);
		    	}else{
		    		tipMsgs.setVisibility(View.VISIBLE);
		    		myListView.setVisibility(View.GONE);
		    	}
		   
		        
		    }	
		};
		RequestParams params=new RequestParams();
		params.put("commodity_id", ProductDeatil.goodId);
		params.put("score",type);
		params.put("curPage","1");
		params.put("pageSize","");
		
		HttpClient.iComCommentInit(mHandler, params);
	}
  
	
	class CommentAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataList==null?0:dataList.size();
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return dataList.get(arg0);
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
				convertview=View.inflate(getActivity(), R.layout.comment_item_layout, null);
			}
			TextView uname=(TextView) convertview.findViewById(R.id.comment_name);
			TextView content=(TextView) convertview.findViewById(R.id.comment_content);
			TextView time=(TextView) convertview.findViewById(R.id.comment_time);
			ItemBean itemBean = dataList.get(arg0);
			uname.setText(itemBean.username);
			content.setText(dataList.get(arg0).content);
			time.setText(dataList.get(arg0).create_time);
			return convertview;
		}
		
	}
	
	class CommentDetailBean{
		public  String avgScore;
		public List<ItemBean> data;
		public class ItemBean{
			public String content;
			public String create_time;
			public String username;
		}
	}

}
