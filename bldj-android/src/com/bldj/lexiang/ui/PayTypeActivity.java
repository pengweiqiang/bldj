package com.bldj.lexiang.ui;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.PayTypeAdapter;
import com.bldj.lexiang.alipay.Rsa;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.PayType;
import com.bldj.lexiang.commons.AppManager;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

/**
 * 选择支付方式
 * 
 * @author will
 * 
 */
public class PayTypeActivity extends BaseActivity {

	ActionBar mActionBar;
	ListView mListView;
	ProgressBar progressBar ;
	PayTypeAdapter payTypeAdapter ;
	List<PayType> payTypeList;
	
	Button btn_finish;
	PayType payType;
	String subject;
	double price;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.pay_type);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		subject = this.getIntent().getStringExtra("service_type_name");
		price = this.getIntent().getDoubleExtra("price", -1);
		getPayType();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("选择支付方式");
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
		mListView = (ListView)findViewById(R.id.listview);
		progressBar = (ProgressBar) findViewById(R.id.progress_listView);
		btn_finish = (Button)findViewById(R.id.finish);
		
	}

	@Override
	public void initListener() {
		btn_finish.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (payTypeAdapter.getCheckPosition() == -1) {
					ToastUtils.showToast(mContext, "请选择支付方式");
					return;
				}
				if(price==-1){
					ToastUtils.showToast(mContext, "稍后请重试");
					return;
				}
				payType = (PayType)payTypeAdapter.getItem(payTypeAdapter.getCheckPosition());
				if(payType.getCode() == 0){//支付宝支付
					aliPay();
				}
			}
		});
	}
	
	/**
	 * 获取支付方式
	 */
	private void getPayType() {

		payTypeList = new ArrayList<PayType>();
		payTypeAdapter = new PayTypeAdapter(mContext, payTypeList, null);
		mListView.setAdapter(payTypeAdapter);

		ApiBuyUtils.getPayType(mContext,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
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
								payTypeAdapter.notifyDataSetChanged();
							}

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
//			Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(PayTypeActivity.this,
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
		//out_trade_no：当前登录的用户id@理疗师id@订单号@优惠券id
		sb.append("12312345");
		sb.append("\"&subject=\"");
		sb.append(subject);
		sb.append("\"&body=\"");
		sb.append(subject);
//		if(product.getOneword()!=null && product.getOneword().length()>512){
//			sb.append(product.getOneword().subSequence(0, 512));
//		}else{
//			sb.append(product.getOneword());
//		}
		sb.append("\"&total_fee=\"");
		sb.append(String.valueOf(price));
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
				
				String tradeStatus = "resultStatus={";
				int imemoStart = result.indexOf("resultStatus=");
				imemoStart += tradeStatus.length();
				int imemoEnd = result.indexOf("};memo=");
				
				tradeStatus = result.substring(imemoStart, imemoEnd);
				if("9000".equals(tradeStatus)){//支付成功
//					ToastUtils.showToast(mContext, "支付成功！");
					paySuccessOrCancelPay(true);
				}else if("6002".equals(tradeStatus)){//网络连接异常
					ToastUtils.showToast(mContext, getResources().getString(R.string.NETWORK_ERROR));
				}else if("4000".equals(tradeStatus)){//支付失败
					ToastUtils.showToast(mContext, "支付失败，稍后请重试！");
				}else if("6001".equals(tradeStatus)){//用户取消支付
					paySuccessOrCancelPay(false);
				}
				
				
			}
		};
	};
	
	/**
	 * 支付成功或者取消支付
	 * @param orderNum
	 */
	private void paySuccessOrCancelPay(boolean isPay){
		Intent data=new Intent();  
	    data.putExtra("isPay", isPay);  
	    data.putExtra("payType", payType.getCode());
	    //回到企业专区
	    setResult(22, data);  
	    finish(); 
	}
	

}
