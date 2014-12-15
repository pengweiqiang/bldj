package com.bldj.lexiang.ui;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.GroupAdapter;
import com.bldj.lexiang.alipay.Rsa;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.ShareUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * 订单详情2
 * 
 * @author will
 * 
 */
public class OrderDetail2Activity extends BaseActivity {

	ActionBar mActionBar;
	private Order order;

	private User user;
	private TextView tv_order_time;
	private TextView tv_order_num;
	private TextView tv_order_pay;
	private Button btn_confirm;
	private Button btn_cancel_order;
	private Button btn_use_code;
	private TextView tv_code;
	private TextView tv_coupon;

	// private RadioButton rb_aliay, rb_weixin, rb_union;

	private PopupWindow popupWindow;
	private View view;
	private ListView lv_group;
	private List<TitleBarEnum> groups;

	ShareUtil shareUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.order_detail2);
		super.onCreate(savedInstanceState);

		order = (Order) this.getIntent().getSerializableExtra("order");

		user = MyApplication.getInstance().getCurrentUser();

		initData();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("订单详情");
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.setRightTextActionButton("分享", new OnClickListener() {

			@Override
			public void onClick(View parent) {
				buildTitleBar(parent);
			}
		});
	}

	@Override
	public void initView() {

		btn_confirm = (Button) findViewById(R.id.confirm);
		btn_cancel_order = (Button) findViewById(R.id.cancel);
		btn_use_code = (Button) findViewById(R.id.use_code);
		tv_order_num = (TextView) findViewById(R.id.order_num);
		tv_order_pay = (TextView) findViewById(R.id.order_real_pay);
		tv_order_time = (TextView) findViewById(R.id.order_time);
		tv_code = (TextView) findViewById(R.id.code);
		tv_coupon = (TextView)findViewById(R.id.order_coupons);
		// rb_aliay = (RadioButton) findViewById(R.id.aliay_pay);
		// rb_weixin = (RadioButton) findViewById(R.id.weixin_pay);
		// rb_union = (RadioButton) findViewById(R.id.union_pay);
		
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

	}

	private void initData() {

		shareUtil = new ShareUtil(mContext);
		shareUtil.initWX();

		if(order.getStatus()==0){//未支付的情况下才能取消订单
			btn_cancel_order.setVisibility(View.VISIBLE);
		}
		tv_order_time.setText(order.getCreatetime());
		tv_order_pay.setText(String.valueOf(order.getOrderPay()));
		tv_order_num.setText(order.getOrderNum());
		tv_coupon.setText("无");
	}

	@Override
	public void initListener() {

		/*
		 * rb_aliay.setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { if (isChecked) { rb_weixin.setChecked(false);
		 * rb_union.setChecked(false); } } }); rb_weixin
		 * .setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { if (isChecked) { rb_aliay.setChecked(false);
		 * rb_union.setChecked(false); } } });
		 * rb_union.setOnCheckedChangeListener(new
		 * CompoundButton.OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { if (isChecked) { rb_weixin.setChecked(false);
		 * rb_aliay.setChecked(false); } } });
		 */

		/*
		 * btn_use_code.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View arg0) { String vcode =
		 * et_code.getText().toString().trim(); if (StringUtils.isEmpty(vcode))
		 * { ToastUtils.showToast(OrderDetail2Activity.this, "请输入电子卷码"); return;
		 * } ApiBuyUtils.couponsManage(OrderDetail2Activity.this,
		 * Long.parseLong(user.getUserId()), 0, vcode, 4, new
		 * HttpConnectionUtil.RequestCallback() {
		 * 
		 * @Override public void execute(ParseModel parseModel) { if
		 * (!ApiConstants.RESULT_SUCCESS .equals(parseModel.getStatus())) {
		 * ToastUtils.showToast( OrderDetail2Activity.this,
		 * parseModel.getMsg()); return;
		 * 
		 * } else { System.out.println(parseModel.getData() .toString()); } }
		 * }); } });
		 */
		// 确定支付
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * ApiBuyUtils.createOrder(AppointmentDoor3Activity.this,
				 * user.getUserId(), user.getUsername(), "",
				 * seller.getUsername(), "", product.getName(), orderPay,
				 * curuser, type, contactor, mobile, detailAddress, notes,
				 * payType, new HttpConnectionUtil.RequestCallback() {
				 * 
				 * @Override public void execute(ParseModel parseModel) {
				 * 
				 * } });
				 */
			}
		});
		// 取消订单
		btn_cancel_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ApiBuyUtils.orderManager(mContext, user.getUserId(),
						order.getOrderNum(), 3,
						new HttpConnectionUtil.RequestCallback() {

							@Override
							public void execute(ParseModel parseModel) {
								if (!ApiConstants.RESULT_SUCCESS
										.equals(parseModel.getStatus())) {
									ToastUtils.showToast(
											OrderDetail2Activity.this,
											parseModel.getMsg());
								} else {
									ToastUtils.showToast(
											OrderDetail2Activity.this,
											parseModel.getMsg());
								}
							}
						});
			}
		});

	}

	private void buildTitleBar(View parent) {
		DeviceInfo.setContext(this);
		if (popupWindow == null) {
			view = LayoutInflater.from(this).inflate(R.layout.group_list, null);
			lv_group = (ListView) view.findViewById(R.id.lvGroup);
			groups = new ArrayList<TitleBarEnum>();
			groups.add(TitleBarEnum.SHARE_SINA);
			groups.add(TitleBarEnum.SHARE_WEIXIN);
			groups.add(TitleBarEnum.SHARE_TENCENT);
			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
			lv_group.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view, DeviceInfo.getScreenWidth() / 2
					- parent.getWidth(), LayoutParams.WRAP_CONTENT);
		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		// WindowManager windowManager = (WindowManager)
		// this.getActivity().getSystemService(Context.WINDOW_SERVICE);

		// popupWindow.showAsDropDown(parent, popupWindow.getWidth(), 0);
		popupWindow.showAsDropDown(parent);
		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				if (groups.get(position).getIndex() == TitleBarEnum.SHARE_SINA
						.getIndex()) {
					ToastUtils.showToast(OrderDetail2Activity.this,
							TitleBarEnum.SHARE_SINA.getMsg() + "分享成功");
					shared_addCode();
				} else if (groups.get(position).getIndex() == TitleBarEnum.SHARE_WEIXIN
						.getIndex()) {
					ToastUtils.showToast(mContext, "分享微信...");
					shareUtil.sendMsgToWX("健康送到家，方便你我他",
							SendMessageToWX.Req.WXSceneTimeline);
				} else if (groups.get(position).getIndex() == TitleBarEnum.SHARE_TENCENT
						.getIndex()) {
					ToastUtils.showToast(OrderDetail2Activity.this,
							TitleBarEnum.SHARE_TENCENT.getMsg() + "分享成功");
					shared_addCode();
				}
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

	/**
	 * 分享成功之后获取电子卷
	 */
	private void shared_addCode() {
		ApiBuyUtils.couponsManage(this, user.getUserId(), 2, "", 0, 0, 0, 0,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(OrderDetail2Activity.this,
									parseModel.getMsg());
						} else {
							ToastUtils.showToast(OrderDetail2Activity.this,
									parseModel.getMsg());
						}
					}
				});
	}
	
	
//	private void aliPay(final Order order) {
//		try {
//			Log.i("ExternalPartner", "onItemClick");
//			String info = getNewOrderInfo(order);
//			String sign = Rsa.sign(info, payType.getRsaPrivateKey());
//			sign = URLEncoder.encode(sign);
//			info += "&sign=\"" + sign + "\"&" + getSignType();
//			// start the pay.
////			Log.i(TAG, "info = " + info);
//
//			final String orderInfo = info;
//			new Thread() {
//				public void run() {
//					AliPay alipay = new AliPay(OrderDetail2Activity.this,
//							mHandler);
//
//					// 设置为沙箱模式，不设置默认为线上环境
//					// alipay.setSandBox(true);
//
//					String result = alipay.pay(orderInfo);
//
//					// Log.i(TAG, "result = " + result);
//					Message msg = new Message();
//					msg.what = 1;
//					Bundle data = new Bundle();
//					data.putString("result", result);
//					data.putString("orderNum", order.getOrderNum());
//					msg.setData(data);
//					mHandler.sendMessage(msg);
//				}
//			}.start();
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			Toast.makeText(mContext, "支付失败,稍后请重试!", Toast.LENGTH_SHORT).show();
//		}
//	}
//
//	private String getNewOrderInfo(Order order) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("partner=\"");
//		sb.append(payType.getPayId());
//		sb.append("\"&out_trade_no=\"");
//		sb.append(order.getOrderNum());
//		sb.append("\"&subject=\"");
//		sb.append(order.getProName());
//		sb.append("\"&body=\"");
//		if(product.getOneword()!=null && product.getOneword().length()>512){
//			sb.append(product.getOneword().subSequence(0, 512));
//		}else{
//			sb.append(product.getOneword());
//		}
//		sb.append("\"&total_fee=\"");
//		sb.append(String.valueOf(order.getOrderPay()));
//		sb.append("\"&notify_url=\"");
//
//		// 网址需要做URL编码
//		sb.append(URLEncoder
//				.encode(ReqUrls.ALIPAY_NOTIFY_URL));
//		sb.append("\"&service=\"mobile.securitypay.pay");
//		sb.append("\"&_input_charset=\"UTF-8");
//		sb.append("\"&return_url=\"");
//		sb.append(URLEncoder.encode("http://m.alipay.com"));
//		sb.append("\"&payment_type=\"1");
//		sb.append("\"&seller_id=\"");
//		sb.append(payType.getPayNum());
//
//		// 如果show_url值为空，可不传
//		// sb.append("\"&show_url=\"");
//		sb.append("\"&it_b_pay=\"15m");
//		sb.append("\"");
//
//		return new String(sb);
//	}
//
//	private String getSignType() {
//		return "sign_type=\"RSA\"";
//	}
	

}
