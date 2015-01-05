package com.bldj.lexiang.ui;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.bldj.lexiang.api.vo.PayType;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.constant.enums.OrderStatusEnum;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.utils.DateUtil;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ShareUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.LoadingDialog;
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
	private TextView tv_order_time;//订单时间
	private TextView tv_order_num;//订单号
	private TextView tv_order_pay;//订单金额
	private Button btn_confirm;
	private Button btn_cancel_order;
	private TextView tv_order_product;
	private TextView tv_order_sellerName;
	private TextView tv_orderStatus;
	private LinearLayout ll_btn;
	PayType payType;

	// private RadioButton rb_aliay, rb_weixin, rb_union;

	private PopupWindow popupWindow;
	private View view;
	private ListView lv_group;
	private List<TitleBarEnum> groups;
	LoadingDialog loading;

	ShareUtil shareUtil;

	int type =-1;//0取消订单成功  1 支付成功
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
		actionBar.setLeftActionButton(R.drawable.btn_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent data = new Intent();
						data.putExtra("type", type);
						setResult(23, data); 
						finish();
					}
				});
		actionBar.setRightTextActionButton("", R.drawable.btn_share, true, new View.OnClickListener() {
			
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
		tv_order_product = (TextView) findViewById(R.id.order_product);
		tv_order_num = (TextView) findViewById(R.id.order_num);
		tv_order_pay = (TextView) findViewById(R.id.order_pay);
		tv_order_time = (TextView) findViewById(R.id.order_time);
		tv_order_sellerName = (TextView) findViewById(R.id.order_seller);
		tv_orderStatus = (TextView) findViewById(R.id.order_status);
		ll_btn = (LinearLayout)findViewById(R.id.ll_btn);
		
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

	}

	private void initData() {

		shareUtil = new ShareUtil(mContext);
		shareUtil.initWX();

		if(order.getStatus()==OrderStatusEnum.NO_PAID){//未支付的情况下才能取消订单
			btn_cancel_order.setVisibility(View.VISIBLE);
		}else{
			ll_btn.setVisibility(View.GONE);
		}
		tv_order_time.setText(DateUtil.getDateString(order.getCreatetime(),DateUtil.TRIM_PATTERN,DateUtil.CUSTOM_PATTERN2));
		tv_order_pay.setText(String.valueOf("￥"+order.getOrderPay()));
		tv_order_num.setText(order.getOrderNum());
		tv_order_product.setText(order.getProName());
		tv_order_sellerName.setText(order.getSellerName());
		tv_orderStatus.setText(order.getStatusStr());
	}

	@Override
	public void initListener() {
		//点击产品进入产品明细
		tv_order_product.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(OrderDetail2Activity.this,HealthProductDetailActivity.class);
				Product product = new Product();
				product.setId(order.getProductId());
				intent.putExtra("product", product);
				startActivity(intent);
			}
		});
		//点击美容师进入个人页面
		tv_order_sellerName.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(OrderDetail2Activity.this,SellerPersonalActivity.class);
				intent.putExtra("sellerId", order.getSellerId());
				startActivity(intent);
			}
		});
		// 确定支付
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getPayType();
			}
		});
		// 取消订单
		btn_cancel_order.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loading = new LoadingDialog(OrderDetail2Activity.this);
				loading.show();
				ApiBuyUtils.orderManager(mContext, user.getUserId(),
						order.getOrderNum(), 3,
						new HttpConnectionUtil.RequestCallback() {

							@Override
							public void execute(ParseModel parseModel) {
								loading.cancel();
								if (!ApiConstants.RESULT_SUCCESS
										.equals(parseModel.getStatus())) {
									ToastUtils.showToast(
											OrderDetail2Activity.this,
											parseModel.getMsg());
								} else {
//									ToastUtils.showToast(
//											OrderDetail2Activity.this,
//											parseModel.getMsg());
									type = 0;
									tv_orderStatus.setText("已取消");
									ll_btn.setVisibility(View.GONE);
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
					String shareUrl = 
							shareUtil.shareSina(MyApplication.getInstance().getConfParams().getShareAppTxt(), "", "");
					Intent intent = new Intent(OrderDetail2Activity.this,BannerWebActivity.class);
					intent.putExtra("url", shareUrl);
					intent.putExtra("name", TitleBarEnum.SHARE_SINA.getMsg());
					startActivity(intent);
				} else if (groups.get(position).getIndex() == TitleBarEnum.SHARE_WEIXIN
						.getIndex()) {
					ToastUtils.showToast(mContext, "分享微信...");
					shareUtil.sendWebPageToWX(MyApplication.getInstance().getConfParams().getShareAppTxt(),
							SendMessageToWX.Req.WXSceneTimeline,"");
				} else if (groups.get(position).getIndex() == TitleBarEnum.SHARE_TENCENT
						.getIndex()) {
					String shareUrl = 
							shareUtil.shareQQ(MyApplication.getInstance().getConfParams().getShareAppTxt(), "", "");
					Intent intent = new Intent(OrderDetail2Activity.this,BannerWebActivity.class);
					intent.putExtra("url", shareUrl);
					intent.putExtra("name", TitleBarEnum.SHARE_TENCENT.getMsg());
					startActivity(intent);
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
	
	
	private void aliPay(final Order order) {
		try {
//			Log.i("ExternalPartner", "onItemClick");
			String info = getNewOrderInfo(order);
			String sign = Rsa.sign(info, payType.getRsaPrivateKey());
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(OrderDetail2Activity.this,
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
//		sb.append(order.getOrderNum());
		sb.append(URLEncoder.encode(user.getUserId()+"-"+order.getSellerId()+"-"+order.getOrderNum()+"-"+(StringUtils.isEmpty(order.getCouponsId())?0:order.getCouponsId())));
		sb.append("\"&subject=\"");
		sb.append(order.getProName());
		sb.append("\"&body=\"");
		sb.append(order.getProName());
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
	/**
	 * 获取支付参数
	 */
	private void getPayType(){
		loading = new LoadingDialog(this);
		loading.show();
		ApiBuyUtils.getPayTypeById(this,order.getPayType(),
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(OrderDetail2Activity.this,
									parseModel.getMsg());
						} else {
							payType = (PayType)JsonUtils.fromJson(parseModel.getData().toString(), PayType.class);
							aliPay(order);
							loading.cancel();
						}
					}
				});
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
				if("9000".equals(tradeStatus)){//支付成功
//					paySuccess(orderNum);
//					ToastUtils.showToast(mContext, "支付成功！");
					paySuccessOrCancelPay();
					type = 1; 
				}else if("6002".equals(tradeStatus)){//网络连接异常
					ToastUtils.showToast(mContext, getResources().getString(R.string.NETWORK_ERROR));
				}else if("4000".equals(tradeStatus)){//支付失败
					ToastUtils.showToast(mContext, "支付失败，稍后请重试！");
				}else if("6001".equals(tradeStatus)){//用户取消支付
//					ToastUtils.showToast(mContext, "取消支付");
				}
				
				
			}
		};
	};
	
	private void paySuccessOrCancelPay(){
		tv_orderStatus.setText("已支付");
		ll_btn.setVisibility(View.GONE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_BACK == keyCode){
			Intent data = new Intent();
			data.putExtra("type", type);
			setResult(23, data); 
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	
	
	
	

}
