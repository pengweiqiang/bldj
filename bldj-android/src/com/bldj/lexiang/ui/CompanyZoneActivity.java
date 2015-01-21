package com.bldj.lexiang.ui;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.PayTypeAdapter;
import com.bldj.lexiang.alipay.Rsa;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.ApiHomeUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.PayType;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.api.ReqUrls;
import com.bldj.lexiang.utils.AlertDialogOperate;
import com.bldj.lexiang.utils.DialogUtil;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.LoadingDialog;
import com.bldj.lexiang.view.SpringScrollView;
import com.bldj.universalimageloader.core.ImageLoader;

/**
 * 企业专区
 * 
 * @author will
 * 
 */
public class CompanyZoneActivity extends BaseActivity {

	ActionBar mActionBar;
	private EditText et_company_name;
	private EditText et_contact_type;
	private EditText et_contactor;
	private EditText et_address;
	private Button btn_confirm;
	private TextView et_service_type_name;// 选择的套餐服务
	private TextView tv_price;
	private TextView tv_preferential;
	private ImageView image_zone;
	private String service_type_name;
	private int serviceTypeIndex;
	private double price;
	User user;

	InputMethodManager manager;

	LoadingDialog loading;

	String companyName;
	String concactType;
	String contactor;
	String address;
	PayType payType;// 支付方式

	private List<PayType> payTypeList;// 支付方式
	private ListView mListView;
	private PayTypeAdapter listAdapter;

	int type;//0优惠特区  1企业专区
	
	SpringScrollView scrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.company_zone);
		super.onCreate(savedInstanceState);
		serviceTypeIndex = this.getIntent().getIntExtra("serviceTypeIndex", 0);
		service_type_name = this.getIntent().getStringExtra("serviceTypeName");
		price = this.getIntent().getDoubleExtra("price", 0);
		type = this.getIntent().getIntExtra("type", 0);
		user = MyApplication.getInstance().getCurrentUser();
		initData();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle(type == 0?"优惠特区":"企业专区");
		actionBar.setLeftActionButton(R.drawable.btn_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.hideRightActionButton();
	}

	private void initData() {
		tv_preferential.setText(MyApplication.getInstance().getConfParams().getPreferential());
		et_service_type_name.setText(service_type_name);
		tv_price.setText(String.valueOf(price) + "元");
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		onConfigureActionBar(mActionBar);

		if(type == 0){//优惠特区。需要登录
			// 获取支付方式
			image_zone.setVisibility(View.GONE);
			findViewById(R.id.cheap_header).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_company_name).setVisibility(View.GONE);
			getPayType();
		}else if(type == 1){//企业专区,无需登录
			btn_confirm.setText("0元申请");
			findViewById(R.id.cheap_header).setVisibility(View.GONE);
			findViewById(R.id.ll_company_name).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_pay).setVisibility(View.GONE);
			
			ImageLoader.getInstance().displayImage(
					MyApplication.getInstance().getConfParams().getEnterZonePic(),
					image_zone,
					MyApplication.getInstance().getOptions(
							R.drawable.default_image));
		}
	}

	@Override
	public void initView() {
		et_service_type_name = (TextView) findViewById(R.id.service_type_name);
		et_company_name = (EditText) findViewById(R.id.company_name);
		et_contact_type = (EditText) findViewById(R.id.company_contact_type);
		et_contactor = (EditText) findViewById(R.id.company_contact);
		et_address = (EditText) findViewById(R.id.company_address);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		tv_price = (TextView) findViewById(R.id.price);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		mListView = (ListView) findViewById(R.id.paytype_listView);
		scrollView = (SpringScrollView)findViewById(R.id.scrollView);
		tv_preferential = (TextView)findViewById(R.id.tv_preferential);
		image_zone = (ImageView)findViewById(R.id.image_zone);

	}

	@Override
	public void initListener() {
		scrollView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (getCurrentFocus() != null
							&& getCurrentFocus().getWindowToken() != null) {
						manager.hideSoftInputFromWindow(getCurrentFocus()
								.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
				return false;
			}
		});
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				companyName = et_company_name.getText().toString().trim();;
				concactType = et_contact_type.getText().toString().trim();
				contactor = et_contactor.getText().toString().trim();
				address = et_address.getText().toString().trim();
				if(type==0){//优惠特区
					companyName = "个人购卡";
				}else{//企业专区
					if (StringUtils.isEmpty(companyName)) {
						et_company_name.requestFocus();
						ToastUtils.showToast(CompanyZoneActivity.this, "请输入企业名称");
						return;
					}
					payType = new PayType();
					payType.setCode(0);
				}
				if (StringUtils.isEmpty(concactType)) {
					et_contact_type.requestFocus();
					ToastUtils.showToast(CompanyZoneActivity.this, "请输入联系方式");
					return;
				}
				if (StringUtils.isEmpty(contactor)) {
					et_contactor.requestFocus();
					ToastUtils.showToast(CompanyZoneActivity.this, "请输入联系人");
					return;
				}
				if (StringUtils.isEmpty(address)) {
					et_address.requestFocus();
					ToastUtils.showToast(CompanyZoneActivity.this, "请输入详细地址");
					return;
				}
				/*if (StringUtils.isEmpty(concactType)) {
					et_contact_type.requestFocus();
					ToastUtils.showToast(CompanyZoneActivity.this, "请输入联系方式");
					return;
				}*/
				
				if (price == -1) {
					ToastUtils.showToast(mContext, "稍后请重试");
					return;
				}
				if(price == 0 || type == 1){//0元体验或者企业专区
					submitOrder();
					return;
				}
				if (listAdapter.getCheckPosition() == -1) {
					ToastUtils.showToast(mContext, "请选择支付方式");
					return;
				}
				payType = (PayType) listAdapter.getItem(listAdapter
						.getCheckPosition());
				if (payType.getCode() == 1) {// 支付宝支付
					aliPay();
				}else if(payType.getCode() == 0){//余额支付
					ToastUtils.showToast(mContext, "亲，优惠专区不支持"+payType.getName());
					return;
				}

			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 获取支付方式
	 */
	private void getPayType() {

		payTypeList = new ArrayList<PayType>();
		listAdapter = new PayTypeAdapter(mContext, payTypeList, null);
		mListView.setAdapter(listAdapter);
		loading = new LoadingDialog(mContext);
		loading.show();
		ApiBuyUtils.getPayType(mContext,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						loading.dismiss();
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
							setListViewHeightBasedOnChildren(mListView);

						}
					}
				});

	}

	private void submitOrder() {
		loading = new LoadingDialog(mContext);
		loading.show();
		User user = MyApplication.getInstance().getCurrentUser();
		ApiHomeUtils.createCompanyZone(CompanyZoneActivity.this,
				serviceTypeIndex, user.getUsername(), companyName, concactType,
				contactor, address, price, payType.getCode(),
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						loading.cancel();
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(CompanyZoneActivity.this,
									parseModel.getMsg());
							return;

						} else {
							// ToastUtils.showToast(CompanyZoneActivity.this,
							// "提交成功");
							final String data = parseModel.getData()
									.getAsString().trim();
							String tip = StringUtils.ToDBC("企业序列号为" + data
									+ ",请您凭此序列号消费");
							DialogUtil.createAlertDialog(mContext, -1, tip,
									R.string.copy, -1,
									new AlertDialogOperate() {

										@Override
										public void physicalclose() {

										}

										@Override
										public void operate() {
											ClipboardManager cmb = (ClipboardManager) mContext
													.getSystemService(Context.CLIPBOARD_SERVICE);
											cmb.setText(data);
											ToastUtils.showToast(mContext,
													"复制成功");
										}

										@Override
										public void cancel() {

										}
									});
						}
					}

				});
	}

	private void aliPay() {
		try {
			String info = getNewOrderInfo();
			String sign = Rsa.sign(info, payType.getRsaPrivateKey());
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			// start the pay.
			// Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(CompanyZoneActivity.this,
							mHandler);

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);

					// Log.i(TAG, "result = " + result);
					Message msg = new Message();
					msg.what = 1;
					Bundle data = new Bundle();
					data.putString("result", result);
					msg.setData(data);
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(mContext, "支付失败,稍后请重试!", Toast.LENGTH_SHORT).show();
		}
	}

	private String getNewOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(payType.getPayId());
		sb.append("\"&out_trade_no=\"");
		// out_trade_no：当前登录的用户id-理疗师id-订单号-优惠券id
		sb.append(user.getUserId() + "-0" + "-0-0");
		sb.append("\"&subject=\"");
		sb.append(service_type_name);
		sb.append("\"&body=\"");
		sb.append(service_type_name);
		sb.append("\"&total_fee=\"");
		sb.append(String.valueOf(price));
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode(payType.getCallbackUrl()));
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
			if (msg.what == 1) {// 支付返回结果
			// Result result = new Result((String) msg.obj);
				Bundle data = msg.getData();
				String result = (String) data.getString("result");

				String tradeStatus = "resultStatus={";
				int imemoStart = result.indexOf("resultStatus=");
				imemoStart += tradeStatus.length();
				int imemoEnd = result.indexOf("};memo=");

				tradeStatus = result.substring(imemoStart, imemoEnd);
				if ("9000".equals(tradeStatus)) {// 支付成功
				// ToastUtils.showToast(mContext, "支付成功！");
				 submitOrder();
				} else if ("6002".equals(tradeStatus)) {// 网络连接异常
					ToastUtils.showToast(mContext,
							getResources().getString(R.string.NETWORK_ERROR));
				} else if ("4000".equals(tradeStatus)) {// 支付失败
					ToastUtils.showToast(mContext, "支付失败，稍后请重试！");
				} else if ("6001".equals(tradeStatus)) {// 用户取消支付
				// paySuccessOrCancelPay(false);
				}

			}
		};
	};
	
	public void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();  
        if (listAdapter == null) {  
            return;  
        }  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight  
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))  
                ;  
        listView.setLayoutParams(params);  
    }  

}
