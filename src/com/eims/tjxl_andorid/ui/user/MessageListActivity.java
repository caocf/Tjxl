/**
 *
 */
package com.eims.tjxl_andorid.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.adapter.MessageListAdapter;
import com.eims.tjxl_andorid.api.CustomResponseHandler;
import com.eims.tjxl_andorid.api.HttpClient;
import com.eims.tjxl_andorid.app.AppContext;
import com.eims.tjxl_andorid.app.BaseActivity;
import com.eims.tjxl_andorid.base.BaseBean;
import com.eims.tjxl_andorid.common.CommonConfrimCancelDialog;
import com.eims.tjxl_andorid.entity.IQueryLetterInfoBean;
import com.eims.tjxl_andorid.entity.IQueryLetterPageBean;
import com.eims.tjxl_andorid.entity.MessageBean;
import com.eims.tjxl_andorid.utils.ActivitySwitch;
import com.eims.tjxl_andorid.utils.LogUtil;
import com.eims.tjxl_andorid.utils.Utils;
import com.eims.tjxl_andorid.weght.HeadView;
import com.eims.tjxl_andorid.weght.MyListView;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshBase;
import com.eims.tjxl_andorid.weght.pulltorefresh.PullToRefreshScrollView;

import org.apache.http.Header;

import java.util.ArrayList;

import loopj.android.http.RequestParams;

/**
 * 消息列表
 * Created by kuangyong on 2015/6/30.
 */
public class MessageListActivity extends BaseActivity {

    private static final int MESSAGE_DEL=2;//删除系统消息
    private static final String MESSAGE_ID="message_id";
    private HeadView headView;
    private MyListView listView;
    private int pageIndex = 1;
    private int pageSize = 20;// 每页多少条数据
    private PullToRefreshScrollView sv_message;
    private ArrayList<MessageBean> productList;// 商品列表
    private IQueryLetterPageBean bean;
    private MessageListAdapter adapterLv;
    private static final int REQUEST_READ_MESSAGE=1;//读取详情
    public static final String MESSAGE_DETAIL_BEAN="message_detail_bean";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_list_layout);
        findview();
        setActionBar();
        setPullRefreshView();
        iQueryLetterPage();
    }

    private void setActionBar() {
        // TODO Auto-generated method stub
        headView.setText("消息中心");
        headView.setGobackVisible();
        headView.setRightGone();
        // headView.setRightResource();
    }

    private void findview() {
        headView = (HeadView) findViewById(R.id.head);
        listView = (MyListView) findViewById(R.id.listview);
        sv_message = (PullToRefreshScrollView) findViewById(R.id.sv_exhibition);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String message_id = productList.get(position).getId();
                if (!TextUtils.isEmpty(message_id)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(MessageDetailActivity.MESSAGE_ID, message_id);
                    ActivitySwitch.openActivityForResult(MessageDetailActivity.class, bundle, MessageListActivity.this, REQUEST_READ_MESSAGE);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentHead = new Intent(mContext, CommonConfrimCancelDialog.class);
                intentHead.putExtra(CommonConfrimCancelDialog.TASK, CommonConfrimCancelDialog.TASK_DELETE_MESSAGE);
                intentHead.putExtra(MESSAGE_ID, productList.get(i).getId());
                startActivityForResult(intentHead, MESSAGE_DEL);
                return true;
            }
        });
    }

    private void iDelLeave(final String mid){
        CustomResponseHandler handler=new CustomResponseHandler(mContext,true,"正在删除..."){
            @Override
            public void onSuccess(int statusCode, Header[] headers, String content) {
                super.onSuccess(statusCode, headers, content);
                BaseBean bean=BaseBean.explainJson(content,mContext);
                if(null!=bean){
                    for (int i=0;i<productList.size();i++){
                        if(mid.equals(productList.get(i).getId())){
                            productList.remove(productList.get(i));
                        }
                    }
                    adapterLv.setData(productList);
                    adapterLv.notifyDataSetChanged();
                }
            }
        };
        RequestParams params=new RequestParams();
        params.put("id", mid);
        HttpClient.iDelLeave(handler, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(RESULT_OK==resultCode){
            if(REQUEST_READ_MESSAGE==requestCode){
                if(null!=data&&null!=data.getBundleExtra("bundle")){//静态刷新页面
                    IQueryLetterInfoBean infoBean= (IQueryLetterInfoBean) data.getBundleExtra("bundle").getSerializable(MESSAGE_DETAIL_BEAN);
                    for (int i=0;i<productList.size();i++){
                        String detailId=infoBean.getData().getId();
                        if(null!=detailId&&detailId.equals(productList.get(i).getId())){
                            productList.get(i).setRead_status("1");
                        }
                    }
                    adapterLv.setData(productList);
                    adapterLv.notifyDataSetChanged();
                }
            }else if(MESSAGE_DEL==requestCode){//删除信息
                if(null!=data){
                    String id=data.getStringExtra(MESSAGE_ID);
                    iDelLeave(id);
                }
            }
        }
    }

    private void setPullRefreshView() {
        sv_message.setEnabled(false);
        // 设定上下拉刷新
        // lv_exhibition.setMode(Mode.BOTH);
        sv_message.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        sv_message.getLoadingLayoutProxy().setLastUpdatedLabel(Utils.getCurrTiem());
        sv_message.getLoadingLayoutProxy().setPullLabel("往下拉更新数据...");
        sv_message.getLoadingLayoutProxy().setRefreshingLabel("正在载入中...");
        sv_message.getLoadingLayoutProxy().setReleaseLabel("放开更新数据...");

        // 下拉刷新数据
        sv_message.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            // 下拉刷新
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                sv_message.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(
                        "最近更新: "
                                + Utils.Long2DateStr_detail(System
                                .currentTimeMillis()));
                pageIndex = 1;
                if (bean != null) {
                    if (productList == null) {
                        productList = new ArrayList<MessageBean>();
                    }
                    productList.clear();
                }
                iQueryLetterPage();
            }

            // 加载更多
            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                // 加载更多
                if (bean != null && bean.getData().size() == pageSize) {
                    pageIndex++;
                    iQueryLetterPage();
                }
            }
        });
    }

    /**
     * 获取消息列表数据
     */
    private void iQueryLetterPage() {
        CustomResponseHandler handler=new CustomResponseHandler(mContext,true,"正在加载..."){
            @Override
            public void onSuccess(String content) {
                sv_message.onRefreshComplete();
                super.onSuccess(content);
                if (LogUtil.isDebug)
                    Log.i(TAG, "消息数据：" + content);
                bean = IQueryLetterPageBean.explainJson(content,mContext);
                if(null!=bean){
                    update(bean);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                sv_message.onRefreshComplete();
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                sv_message.onRefreshComplete();
                Toast.makeText(mContext, "网络错误",
                        Toast.LENGTH_LONG).show();
            }
        };
        RequestParams params=new RequestParams();

        params.put("uid", AppContext.getLocalUserInfo(mContext).id);
        params.put("curPage",pageIndex+"");
        params.put("pageSize", pageSize + "");
        HttpClient.iQueryLetterPage(handler, params);
    }

    private void update(IQueryLetterPageBean mGoodsBean) {
        if (mGoodsBean == null || mGoodsBean.getData() == null)
            return;
        if (productList == null) {
            productList = new ArrayList<MessageBean>();
        }
        if (pageIndex == 1) {
            productList.clear();
            sv_message.setMode(PullToRefreshBase.Mode.BOTH);
        }
        productList.addAll(mGoodsBean.getData());
        if (adapterLv == null) {
            adapterLv = new MessageListAdapter(mContext, productList);
            listView.setAdapter(adapterLv);
        } else {
            adapterLv.setData(productList);
            adapterLv.notifyDataSetChanged();
        }
        if (mGoodsBean.getData().size() < pageSize) {
            Toast.makeText(mContext,"数据加载完成",Toast.LENGTH_SHORT).show();
            sv_message.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        }
    }
}
