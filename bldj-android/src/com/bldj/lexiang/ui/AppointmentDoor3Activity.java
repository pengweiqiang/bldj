package com.bldj.lexiang.ui;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.PayTypeAdapter;
import com.bldj.lexiang.alipay.Keys;
import com.bldj.lexiang.alipay.Result;
import com.bldj.lexiang.alipay.Rsa;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.Coupon;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.PayType;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

/**
 * 上门预约3
 * 
 * @author will
 * 
 */
public class AppointmentDoor3Activity extends BaseActivity {

	ActionBar mActionBar;
	private String time;// 预约时间
	private int timeIndex;
	private String detailAddress;
	private Seller seller;
	private Product product;
	private Button btn_use_code;
	private EditText et_code;

	private User user;
	private TextView tv_sellerName;
	private TextView tv_productName;
	private TextView tv_time;
	private Button btn_confirm;
	private LinearLayout select_coupons;
	private TextView tv_coupons;

//	private RadioButton rb_aliay, rb_weixin, rb_union;

	private double orderPay;// 总金额
	private int payType = 1;

	private Coupon coupon;// 使用优惠卷

	private List<PayType> payTypeList;// 支付方式
	private ListView mListView;
	private PayTypeAdapter listAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.appointment_door3);
		super.onCreate(savedInstanceState);

		time = this.getIntent().getStringExtra("time");
		timeIndex = this.getIntent().getIntExtra("timeIndex", 0);
		seller = (Seller) this.getIntent().getSerializableExtra("seller");
		product = (Product) this.getIntent().getSerializableExtra("product");
		detailAddress = this.getIntent().getStringExtra("address");

		user = MyApplication.getInstance().getCurrentUser();
		initView();

		initListener();

		tv_time.setText(time);
		tv_sellerName.setText(seller.getUsername());

		orderPay = product.getCurPrice() + seller.getAvgPrice();
		tv_productName.setText(product.getName());

		getPayType();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("上门预约");
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.hideRightActionButton();
	}

	@Override
	public void initView() {

		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_use_code = (Button) findViewById(R.id.btn_use_code);
		tv_time = (TextView) findViewById(R.id.time);
		tv_productName = (TextView) findViewById(R.id.product_name);
		tv_sellerName = (TextView) findViewById(R.id.seller_name);
		/*rb_aliay = (RadioButton) findViewById(R.id.aliay_pay);
		rb_weixin = (RadioButton) findViewById(R.id.weixin_pay);
		rb_union = (RadioButton) findViewById(R.id.union_pay);*/
		select_coupons = (LinearLayout) findViewById(R.id.select_coupons);
		tv_coupons = (TextView) findViewById(R.id.tips);
		mListView = (ListView) findViewById(R.id.paytype_listView);

		et_code = (EditText) findViewById(R.id.code);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

	}

	@Override
	public void initListener() {
		/*rb_aliay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					payType = 0;
					rb_weixin.setChecked(false);
					rb_union.setChecked(false);
				}
			}
		});
		rb_weixin
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							payType = 1;
							rb_aliay.setChecked(false);
							rb_union.setChecked(false);
						}
					}
				});
		rb_union.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					payType = 2;
					rb_weixin.setChecked(false);
					rb_aliay.setChecked(false);
				}
			}
		});*/
		// 确定订单
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(listAdapter.getCheckPosition()==-1){
					ToastUtils.showToast(mContext, "请选择支付方式");
					return;
				}
				String serviceTime = time.substring(0, time.indexOf(" ")) + "@"
						+ timeIndex;
				long couponId = 0;
				if (coupon != null) {
					couponId = coupon.getId();
				}

				ApiBuyUtils.createOrder(AppointmentDoor3Activity.this,
						user.getUserId(), user.getUsername(), seller.getId(),
						seller.getNickname(), product.getId(),
						product.getName(), orderPay, user.getUsername(), 1,
						user.getUsername(), user.getMobile(), detailAddress,
						"", payType, couponId, serviceTime,
						new HttpConnectionUtil.RequestCallback() {
							@Override
							public void execute(ParseModel parseModel) {
								if (!ApiConstants.RESULT_SUCCESS
										.equals(parseModel.getStatus())) {
									ToastUtils.showToast(
											AppointmentDoor3Activity.this,
											parseModel.getMsg());
									return;

								} else {
									Order order = (Order) JsonUtils.fromJson(
											parseModel.getData().toString(),
											Order.class);
									aliPay(order);
									ToastUtils.showToast(
											AppointmentDoor3Activity.this,
											parseModel.getMsg() + ",订单号是:"
													+ order.getOrderNum());
									return;
								}
							}
						});

			}
		});

		// 使用电子卷
		btn_use_code.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String vcode = et_code.getText().toString().trim();
				if (StringUtils.isEmpty(vcode)) {
					ToastUtils.showToast(AppointmentDoor3Activity.this,
							"请输入电子卷码");
					return;
				}
				ApiBuyUtils.couponsManage(AppointmentDoor3Activity.this,
						user.getUserId(), 0, vcode, 4, 0, 0, 0,
						new HttpConnectionUtil.RequestCallback() {

							@Override
							public void execute(ParseModel parseModel) {
								if (!ApiConstants.RESULT_SUCCESS
										.equals(parseModel.getStatus())) {
									ToastUtils.showToast(
											AppointmentDoor3Activity.this,
											parseModel.getMsg());
									return;

								} else {
									System.out.println(parseModel.getData()
											.toString());
								}
							}
						});
			}
		});
		// 选择优惠卷
		select_coupons.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,
						CouponsFragmentActivity.class);
				intent.putExtra("type", 1);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 20) {
			coupon = (Coupon) data.getSerializableExtra("coupon");
			tv_coupons.setText(coupon.getName() + " ￥:" + coupon.getPrice());

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 获取支付方式
	 */
	private void getPayType() {

		payTypeList = new ArrayList<PayType>();
		listAdapter = new PayTypeAdapter(mContext, payTypeList, null);
		mListView.setAdapter(listAdapter);

		ApiBuyUtils.getPayType(mContext,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(mContext, parseModel.getMsg());
						} else {
							List<PayType> payTypes = JsonUtils.fromJson(parseModel
									.getData().toString(),
									new TypeToken<List<PayType>>() {
									});
							payTypeList.addAll(payTypes);
							listAdapter.notifyDataSetChanged();

						}
					}
				});

	}

	private void aliPay(Order order) {
		try {
			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo(order);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i("ExternalPartner", "start pay");
			// start the pay.
			Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(AppointmentDoor3Activity.this,
							mHandler);

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);

					Log.i(TAG, "result = " + result);
					Message msg = new Message();
					msg.what = 1;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(mContext, "支付失败！", Toast.LENGTH_SHORT).show();
		}
	}

	private String getNewOrderInfo(Order order) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(order.getOrderNum());
		sb.append("\"&subject=\"");
		sb.append(order.getProName());
		sb.append("\"&body=\"");
		sb.append(order.getProName());
		sb.append("\"&total_fee=\"");
		sb.append(String.valueOf(order.getOrderPay()));
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"30m");
		sb.append("\"");

		return new String(sb);
	}

	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		Log.d(TAG, "outTradeNo: " + key);
		return key;
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);
			Toast.makeText(mContext, result.getResult(), Toast.LENGTH_SHORT)
			.show();
//			switch (msg.what) {
//			case 1:
//			case 2: {
//				Toast.makeText(mContext, result.getResult(), Toast.LENGTH_SHORT)
//						.show();
//
//			}
//				break;
//			default:
//				break;
//			}
		};
	};

}
