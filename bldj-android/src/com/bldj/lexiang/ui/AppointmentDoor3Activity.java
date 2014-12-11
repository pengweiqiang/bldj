package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.Coupon;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
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
	private String time;//预约时间
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

	private RadioButton rb_aliay, rb_weixin, rb_union;
	
	private double orderPay;//总金额
	private int payType = 0;
	
	private Coupon coupon;//使用优惠卷

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
		
		orderPay = product.getCurPrice()+seller.getAvgPrice();
		 tv_productName.setText(product.getName());

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
		rb_aliay = (RadioButton) findViewById(R.id.aliay_pay);
		rb_weixin = (RadioButton) findViewById(R.id.weixin_pay);
		rb_union = (RadioButton) findViewById(R.id.union_pay);
		select_coupons = (LinearLayout)findViewById(R.id.select_coupons);
		tv_coupons = (TextView)findViewById(R.id.tips);
		
		et_code = (EditText) findViewById(R.id.code);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

	}

	@Override
	public void initListener() {
		rb_aliay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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
		});
		// 确定订单
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String serviceTime = time.substring(0,time.indexOf(" "))+"@"+timeIndex;
				long couponId = 0 ;
				if(coupon!=null){
					couponId = coupon.getId();
				}
				
				ApiBuyUtils.createOrder(AppointmentDoor3Activity.this,
						user.getUserId(), user.getUsername(), seller.getId(),
						seller.getNickname(), product.getId(), product.getName(), orderPay,
						user.getUsername(), 1, user.getUsername(), user.getMobile(), detailAddress, "",
						payType,couponId,serviceTime, new HttpConnectionUtil.RequestCallback() {
							@Override
							public void execute(ParseModel parseModel) {
								if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
										.getStatus())) {
									ToastUtils.showToast(AppointmentDoor3Activity.this, parseModel.getMsg());
									return;

								} else {
									Order order = (Order)JsonUtils.fromJson(parseModel.getData().toString(), Order.class);
									ToastUtils.showToast(AppointmentDoor3Activity.this, parseModel.getMsg()+",订单号是:"+order.getOrderNum());
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
						user.getUserId(), 0, vcode, 4,0,0,0,
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
		//选择优惠卷
		select_coupons.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mContext,CouponsFragmentActivity.class);
				intent.putExtra("type", 1);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 20){
			coupon = (Coupon)data.getSerializableExtra("coupon");
			tv_coupons.setText(coupon.getName()+" ￥:"+coupon.getPrice());
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
