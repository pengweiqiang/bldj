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
import com.bldj.lexiang.commons.AppManager;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.utils.DateUtil;
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
	private double codePrice = 0;//电子卷价格

	private User user;
	private TextView tv_sellerName;
	private TextView tv_productName;
	private TextView tv_time;
	private Button btn_confirm;
	private LinearLayout select_coupons;
	private TextView tv_coupons;

	// private RadioButton rb_aliay, rb_weixin, rb_union;

	private double orderPay;// 最后总金额
	private TextView tv_orderPay;
	private PayType payType;

	private Coupon coupon;// 使用优惠卷
	private double couponPrice = 0;

	private List<PayType> payTypeList;// 支付方式
	private ListView mListView;
	private PayTypeAdapter listAdapter;
	
	Order order = null;

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
		initData();
		//获取支付方式
		getPayType();
	}
	private void initData(){
		tv_time.setText(DateUtil.getDateString(time,DateUtil.CUSTOM_PATTERN3,DateUtil.CUSTOM_PATTERN2));
		tv_sellerName.setText(seller.getNickname());

		orderPay = product.getCurPrice();
		showOrderPay(0);
		tv_productName.setText(product.getName());
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("上门预约");
		actionBar.setLeftActionButton(R.drawable.btn_back,
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
		tv_orderPay = (TextView)findViewById(R.id.real_pay);
		/*
		 * rb_aliay = (RadioButton) findViewById(R.id.aliay_pay); rb_weixin =
		 * (RadioButton) findViewById(R.id.weixin_pay); rb_union = (RadioButton)
		 * findViewById(R.id.union_pay);
		 */
		select_coupons = (LinearLayout) findViewById(R.id.select_coupons);
		tv_coupons = (TextView) findViewById(R.id.tips);
		mListView = (ListView) findViewById(R.id.paytype_listView);

		et_code = (EditText) findViewById(R.id.code);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

	}

	@Override
	public void initListener() {
		// 确定订单
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(order !=null){//已经有订单号，防止重复下单
					if(payType.getCode() == 1){//支付宝支付
						aliPay(order);
					}else if(payType.getCode() == 2){//银联支付
						
					}
					
					return;
				}
					if (listAdapter.getCheckPosition() == -1) {
						ToastUtils.showToast(mContext, "请选择支付方式");
						return;
					}
					payType = payTypeList.get(listAdapter.getCheckPosition());
					String serviceTime = time.substring(0, time.indexOf(" ")) + "@"
							+ timeIndex;
					long couponId = 0;
					if (coupon != null) {
						couponId = coupon.getId();
					}
					String contactor = user.getUsername();
					String mobile = user.getMobile();
					if(!MyApplication.getInstance().appointMap.isEmpty()){//从首页我要预约进入下单
						if(MyApplication.getInstance().appointMap.containsKey("address")){
							detailAddress = MyApplication.getInstance().appointMap.get("address");
						}
						if(MyApplication.getInstance().appointMap.containsKey("contactor")){
							contactor = MyApplication.getInstance().appointMap.get("contactor");
						}
						if(MyApplication.getInstance().appointMap.containsKey("mobile")){
							mobile = MyApplication.getInstance().appointMap.get("mobile");
						}
					}
	
					ApiBuyUtils.createOrder(AppointmentDoor3Activity.this,
							user.getUserId(), user.getUsername(), seller.getId(),
							seller.getNickname(), product.getId(),
							product.getName(), orderPay, user.getUsername(), 0,
							contactor, mobile, detailAddress,
							"", payType.getCode(), couponId, serviceTime,
							new HttpConnectionUtil.RequestCallback() {
								@Override
								public void execute(ParseModel parseModel) {
									if (!ApiConstants.RESULT_SUCCESS
											.equals(parseModel.getStatus())) {
										ToastUtils.showToast(
												AppointmentDoor3Activity.this,
												parseModel.getMsg());
										return;
	
									} else {//下单成功
										MyApplication.getInstance().appointMap.clear();
										order = (Order) JsonUtils.fromJson(
												parseModel.getData().toString(),
												Order.class);
										if(payType.getCode() == 1){//支付宝支付
											aliPay(order);
										}else if(payType.getCode() == 2){//银联支付
											
										}
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
					et_code.requestFocus();
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
//									System.out.println(parseModel.getData()
//											.toString());
									try{
										codePrice = Double.valueOf((parseModel.getData().getAsString()));
									}catch(Exception e){
										codePrice = 0;
										ToastUtils.showToast(mContext, e.toString());
									}
									showOrderPay(2);
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
		if (resultCode == 20) {//选择优惠卷
			coupon = (Coupon) data.getSerializableExtra("coupon");
			tv_coupons.setText(coupon.getName() + " ￥:" + coupon.getPrice());
			couponPrice = coupon.getPrice();
			showOrderPay(1);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 获取订单最后的金额
	 * @return
	 */
	private double caluOrderPay(int type){
		orderPay = product.getCurPrice();
		if(type == 1){//优惠卷
			orderPay = orderPay - couponPrice;
			codePrice = 0;
		}else if(type == 2){//电子卷
			orderPay = orderPay - codePrice;
			if(coupon!=null){
				tv_coupons.setText("无");
				couponPrice = 0;
				coupon = null;
			}

		}
		if(orderPay<=0){//总价格小于1，最低消费0.01
			orderPay = 0.01;
		}
		return orderPay;
	}
	
	private void showOrderPay(int type){
		
		tv_orderPay.setText("实际支付：￥"+String.valueOf(caluOrderPay(type)));
		
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
							List<PayType> payTypes = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<PayType>>() {
									});
							if (payTypes != null && !payTypes.isEmpty()) {
								payTypeList.addAll(payTypes);
								listAdapter.notifyDataSetChanged();
							}

						}
					}
				});

	}

	private void aliPay(final Order order) {
		try {
//			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo(order);
			String sign = Rsa.sign(info, payType.getRsaPrivateKey());
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			// start the pay.
//			Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(AppointmentDoor3Activity.this,
							mHandler);

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);

					// Log.i(TAG, "result = " + result);
					Message msg = new Message();
					msg.what = 1;
					Bundle data = new Bundle();
					data.putString("result", result);
					data.putString("orderNum", order.getOrderNum());
					msg.setData(data);
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(mContext, "支付失败,稍后请重试!", Toast.LENGTH_SHORT).show();
		}
	}

	private String getNewOrderInfo(Order order) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(payType.getPayId());
		sb.append("\"&out_trade_no=\"");
		//out_trade_no：当前登录的用户id@理疗师id@订单号@优惠券id
		sb.append(user.getUserId()+"-"+seller.getId()+"-"+order.getOrderNum()+"-"+(coupon==null?0:coupon.getId()));
		sb.append("\"&subject=\"");
		sb.append(order.getProName());
		sb.append("\"&body=\"");
		sb.append(order.getProName());
//		if(product.getOneword()!=null && product.getOneword().length()>512){
//			sb.append(product.getOneword().subSequence(0, 512));
//		}else{
//			sb.append(product.getOneword());
//		}
		sb.append("\"&total_fee=\"");
		sb.append(String.valueOf(order.getOrderPay()));
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder
				.encode(payType.getCallbackUrl()));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(payType.getPayNum());

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"15m");
		sb.append("\"");

		return new String(sb);
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {//支付返回结果
//				Result result = new Result((String) msg.obj);
				Bundle data = msg.getData();
				String result = (String)data.getString("result");
				String orderNum = (String)data.getString("orderNum");
				
				String tradeStatus = "resultStatus={";
				int imemoStart = result.indexOf("resultStatus=");
				imemoStart += tradeStatus.length();
				int imemoEnd = result.indexOf("};memo=");
				
				tradeStatus = result.substring(imemoStart, imemoEnd);
//				MyApplication.getInstance().sellerVo = null;
				if("9000".equals(tradeStatus)){//支付成功
//					paySuccess(orderNum);
					ToastUtils.showToast(mContext, "支付成功！");
					paySuccessOrCancelPay();
				}else if("6002".equals(tradeStatus)){//网络连接异常
					ToastUtils.showToast(mContext, getResources().getString(R.string.NETWORK_ERROR));
				}else if("4000".equals(tradeStatus)){//支付失败
					ToastUtils.showToast(mContext, "支付失败，稍后请重试！");
				}else if("6001".equals(tradeStatus)){//用户取消支付
					paySuccessOrCancelPay();
				}
				
				
			}
		};
	};
	
	/**
	 * 支付成功或者取消支付
	 * @param orderNum
	 */
	private void paySuccessOrCancelPay(){
		AppManager.getAppManager().finishActivity(AppointmentDoor1Activity.class);
		AppManager.getAppManager().finishActivity(AppointmentDoor2Activity.class);
		Intent intent = new Intent(mContext,MyOrdersActivity.class);
		startActivity(intent);
		finish();
	}

}
