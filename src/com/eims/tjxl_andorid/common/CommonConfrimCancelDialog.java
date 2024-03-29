package com.eims.tjxl_andorid.common;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eims.tjxl_andorid.R;
import com.eims.tjxl_andorid.app.BaseActivity;


public class CommonConfrimCancelDialog extends BaseActivity {

	public static final String TAG = "CommonConfrimCancelDialog";
	private TextView mTitleTV, mMessageTV;
	private Button mConfrimBtn, mCancelBtn;
	private ImageView mIconIV;
	public static String TASK = "Task";

	// 上传图片
	public static final String TASK_CHOICE_UPLOAD_TYPE = "ChoiceUploadType";
	// 删除系统消息
	public static final String TASK_DELETE_MESSAGE = "task_delete_message";
	// 删除收货地址
	public static final String TASK_DELETE_ADRESS = "task_delete_adress";
	// 删除浏览记录
	public static final String TASK_DELETE_BROWSE_HISTORY = "task_delete_browse_history";
//	// 退出应用
//	public static final String TASK_EXIT_DIALOG = "ExitApp";
//	// 删除订单
//	public static final String TASK_DELETE_ORDER = "delete_order";
//	// 取消订单
//	public static final String TASK_CANCEL_ORDER = "cancel_order";
//	// 删除地址
//	public static final String TASK_DELETE_ADDR = "delete_address";
//	// 设为默认
//	public static final String TASK_DEFAULT_ADDR = "default_address";
//	// 确认收货
//	public static final String TASK_SURE_RECEIVE = "sure_receive";
//	// 查看物流
//	public static final String TASK_CHECK_LOGISTIC = "check_logistic";
//	// 取消预约
//	public static final String TASK_CANCEL_YUYUE = "cancel_yuyue";
//	// 上门取货
//	public static final String TASK_SMQH = "task_smqh";
//	// 取消换货
//	public static final String TASK_CANCEL_CHANGE = "task_cancel_change";
//	// 确认收货
//	public static final String TASK_RECEIVE = "task_receive";
//	// 取消退款
//	public static final String TASK_CANCEL_BACK = "task_cancel_back";
//	// 确认到账
//	public static final String TASK_RECEIVE_MONEY = "task_receive_money";
//	// 确认投诉解决
//	public static final String TASK_SOLVE_COMPLAINT = "task_solve_complaint";
//	// 确认投诉取消
//	public static final String TASK_CANCEL_COMPLAINT = "task_cancel_complaint";
//	// 确认取消预购
//	public static final String CANCEL_NEWGOODS = "cancel_newgoods";
//	// 确认删除所有记录
//	public static final String TASK_DELETE_ALL_HiSTORY = "task_delete_all_history";
//	// 确认删除某条记录
//	public static final String TASK_DELETE_ONE_HiSTORY = "task_delete_one_history";

	public static final int CHOICE_UPLOAD_TYPE_CODE = 0x0002;
	public static final int EXIT_APP_CODE = 0x0005;

	public static final String TASK_CANCEL_FOCUS_TYPE = "CancelFocusType";

	public static final String UPLOAD_TYPE_CAMER = "camer";
	public static final String UPLOAD_TYPE_LOCAL = "local";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		setContentView(R.layout.goods_shopping_car_dialog_layout);

		initViews();
		showUI();

	}

	private void showUI() {

		TASK = getIntent().getStringExtra(TASK);
		Drawable warrnDrawable = getResources().getDrawable(R.drawable.green_warrn);

		if (TASK.equals(TASK_CHOICE_UPLOAD_TYPE)) {

			setTitle(R.string.choice_image_tip);
			setIcon(warrnDrawable);
			setMessage(R.string.wenxin_tip_7);
			mConfrimBtn.setText("拍照");
			mConfrimBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					setResult(RESULT_OK, new Intent().putExtra(TAG, UPLOAD_TYPE_CAMER));
					dismissDialog();
				}
			});

			mCancelBtn.setText("图库");
			mCancelBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					setResult(RESULT_OK, new Intent().putExtra(TAG, UPLOAD_TYPE_LOCAL));
					dismissDialog();
				}
			});
		} else if (TASK.equals(TASK_DELETE_MESSAGE)) {
			/* 删除该消息 */
			setTitle("删除该消息");
			mCancelBtn.setText("取消");
			mCancelBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					setResult(RESULT_CANCELED);
					dismissDialog();
				}
			});
			mConfrimBtn.setText("确定");
			mConfrimBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					setResult(RESULT_OK,getIntent());
					dismissDialog();
				}
			});
		}else if (TASK.equals(TASK_DELETE_ADRESS)) {
			/* 删除该消息 */
			setTitle("删除该地址");
			mCancelBtn.setText("取消");
			mCancelBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setResult(RESULT_CANCELED);
					dismissDialog();
				}
			});
			mConfrimBtn.setText("确定");
			mConfrimBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(RESULT_OK,getIntent());
					dismissDialog();
				}
			});
		}else if (TASK.equals(TASK_DELETE_BROWSE_HISTORY)) {
			/* 删除该消息 */
			setTitle("删除该浏览记录");
			mCancelBtn.setText("取消");
			mCancelBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setResult(RESULT_CANCELED);
					dismissDialog();
				}
			});
			mConfrimBtn.setText("确定");
			mConfrimBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(RESULT_OK,getIntent());
					dismissDialog();
				}
			});
		}
//		else if (TASK.equals(TASK_EXIT_DIALOG)) {
//			/* 是否退出登录 */
//			setTitle("退出登录");
//			setMessage("确认退出登录?");
//			setIcon(warrnDrawable);
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.surelogout);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//		} else if (TASK.equals(TASK_DELETE_SHOPCAR_DIALOG)) {
//			/* 删除购物车 */
//			setTitle("删除该商品");
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.ensure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//		} else if (TASK.equals(TASK_CANCEL_FOCUS_TYPE)) {
//			/* 取消收藏和商品详情 */
//			setTitle("收藏信息");
//			mCancelBtn.setText(R.string.focus_cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_FIRST_USER);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.good_detail);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//		} else if (TASK.equals(TASK_DELETE_ORDER)) {
//			/* 取消关注和商品详情 */
//			setTitle("删除该订单");
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					setResult(RESULT_OK,getIntent());
//					dismissDialog();
//				}
//			});
//
//		}  else if (TASK.equals(TASK_SOLVE_COMPLAINT)) {
//			/* 您确定反馈的内容已解决 */
//            setTitle("您确定反馈的内容已解决？");
//            mCancelBtn.setText(R.string.cancel);
//            mCancelBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    setResult(RESULT_CANCELED);
//                    dismissDialog();
//                }
//            });
//            mConfrimBtn.setText(R.string.sure);
//            mConfrimBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setResult(RESULT_OK,getIntent());
//                    dismissDialog();
//                }
//            });
//        } else if (TASK.equals(TASK_CANCEL_COMPLAINT)) {
//			/* 您确定取消投诉 */
//            setTitle("您确定取消投诉吗？");
//            mCancelBtn.setText(R.string.cancel);
//            mCancelBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    setResult(RESULT_CANCELED);
//                    dismissDialog();
//                }
//            });
//            mConfrimBtn.setText(R.string.sure);
//            mConfrimBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setResult(RESULT_OK,getIntent());
//                    dismissDialog();
//                }
//            });
//
//        } else if (TASK.equals(CANCEL_NEWGOODS)) {
//			/* 您确定取消预购 */
//            setTitle("您确定取消预购吗？");
//            mCancelBtn.setText(R.string.cancel);
//            mCancelBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    setResult(RESULT_CANCELED);
//                    dismissDialog();
//                }
//            });
//            mConfrimBtn.setText(R.string.sure);
//            mConfrimBtn.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    setResult(RESULT_OK,getIntent());
//                    dismissDialog();
//                }
//            });
//
//        } else if (TASK.equals(TASK_DELETE_ADDR)) {
//			/* 删除地址 */
//			setTitle("删除该地址");
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		} else if (TASK.equals(TASK_DEFAULT_ADDR)) {
//			/* 删除地址 */
//			setTitle("设为默认");
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		} else if (TASK.equals(TASK_CANCEL_ORDER)) {
//			/** 取消订单 */
//			setTitle("取消该订单");
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		} else if (TASK.equals(TASK_SURE_RECEIVE)) {
//
//			/* 是否确认收货 */
//			setTitle("是否确认收货");
//			setMessage("确认收货?");
//			setIcon(warrnDrawable);
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//                    String orderId = getIntent().getStringExtra("orderId");
//                    Intent intent=new Intent();
//                    intent.putExtra(AllOrderActivity.CONFIRM_RECEIPT_ORDERID,orderId);
//                    setResult(RESULT_OK,intent);
//					dismissDialog();
//				}
//			});
//
//		} else if (TASK.equals(TASK_CANCEL_YUYUE)) {
//
//			/* 是否取消预约 */
//			setTitle("是否取消预约");
//			setIcon(warrnDrawable);
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		} else if (TASK.equals(TASK_SMQH)) {
//
//			/* 是否上门取货 */
//			setTitle("是否确定上门取货");
//			setIcon(warrnDrawable);
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		} else if (TASK.equals(TASK_CANCEL_CHANGE)) {
//			/* 取消换货 */
//			setTitle("是否确定取消该换货订单");
//			setIcon(warrnDrawable);
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		} else if (TASK.equals(TASK_RECEIVE)) {
//			/* 取消换货 */
//			setTitle("是否确认收货");
//			setIcon(warrnDrawable);
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		} else if (TASK.equals(TASK_CANCEL_BACK)) {
//			/* 取消退款 */
//			setTitle("是否取消退款");
//			setIcon(warrnDrawable);
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		} else if (TASK.equals(TASK_RECEIVE_MONEY)) {
//			/* 确认到账 */
//			setTitle("是否确认到账");
//			setIcon(warrnDrawable);
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		}else if (TASK.equals(TASK_DELETE_ALL_HiSTORY)) {
//			/* 清空历史 */
//			setTitle("是否清空浏览历史");
//			setIcon(warrnDrawable);
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		}else if (TASK.equals(TASK_DELETE_ONE_HiSTORY)) {
//			/* 删除该条记录 */
//			setTitle("是否删除该条记录");
//			setIcon(warrnDrawable);
//			mCancelBtn.setText(R.string.cancel);
//			mCancelBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_CANCELED);
//					dismissDialog();
//				}
//			});
//			mConfrimBtn.setText(R.string.sure);
//			mConfrimBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					setResult(RESULT_OK);
//					dismissDialog();
//				}
//			});
//
//		}

	}

	@Override
	public void setTitle(CharSequence title) {

		if (mTitleTV.getVisibility() == View.GONE) {
			mTitleTV.setVisibility(View.VISIBLE);
		}
		mTitleTV.setText(title);
	}

	@Override
	public void setTitle(int resId) {

		if (mTitleTV.getVisibility() == View.GONE) {
			mTitleTV.setVisibility(View.VISIBLE);
		}
		mTitleTV.setText(resId);
	}

	private void setMessage(int resId) {
		if (mMessageTV.getVisibility() == View.GONE) {
			mMessageTV.setVisibility(View.VISIBLE);
		}
		mMessageTV.setText(resId);
	}

	private void setMessage(String message) {
		if (mMessageTV.getVisibility() == View.GONE) {
			mMessageTV.setVisibility(View.VISIBLE);
		}
		mMessageTV.setText(message);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
		}
		return super.onKeyDown(keyCode, event);
	}

	private void dismissDialog() {
		finish();
	}

	private void setIcon(Drawable icon) {
		mIconIV.setImageDrawable(icon);
	}

	private void initViews() {

		mTitleTV = (TextView) findViewById(R.id.com_tip_tv);
		mIconIV = (ImageView) findViewById(R.id.com_icon_iv);
		mMessageTV = (TextView) findViewById(R.id.com_message_tv);
		mCancelBtn = (Button) findViewById(R.id.com_cancel_btn);
		mCancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setResult(RESULT_CANCELED);
				dismissDialog();
			}
		});
		mConfrimBtn = (Button) findViewById(R.id.com_ok_btn);
	}

}
