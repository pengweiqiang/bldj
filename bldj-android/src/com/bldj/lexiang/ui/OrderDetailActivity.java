package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

/**
 * 订单详情
 * 
 * @author will
 * 
 */
public class OrderDetailActivity extends BaseActivity {

	ActionBar mActionBar;
	private Order order;
	
	private User user;
	private TextView tv_order_time;
	private TextView tv_order_num;
	private TextView tv_order_pay;
	private Button btn_confirm;
	private Button btn_cancel_order;
	private Button btn_use_code;
	private EditText et_code;
	
	private RadioButton rb_aliay, rb_weixin, rb_union;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.order_detail);
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
			public void onClick(View arg0) {
				
			}
		});
	}

	@Override
	public void initView() {

		btn_confirm = (Button) findViewById(R.id.confirm);
		btn_cancel_order = (Button)findViewById(R.id.cancel);
		btn_use_code = (Button)findViewById(R.id.use_code);
		tv_order_num = (TextView)findViewById(R.id.order_num);
		tv_order_pay = (TextView)findViewById(R.id.order_pay);
		tv_order_time = (TextView)findViewById(R.id.order_time);
		et_code = (EditText)findViewById(R.id.code);
		rb_aliay = (RadioButton) findViewById(R.id.aliay_pay);
		rb_weixin = (RadioButton) findViewById(R.id.weixin_pay);
		rb_union = (RadioButton) findViewById(R.id.union_pay);
		
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
	}
	
	private void initData(){
		tv_order_time.setText(order.getCreatetime());
		tv_order_pay.setText(String.valueOf(order.getOrderPay()));
		tv_order_num.setText(order.getOrderNum());
	}

	@Override
	public void initListener() {
		
		rb_aliay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
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
					rb_weixin.setChecked(false);
					rb_aliay.setChecked(false);
				}
			}
		});
		
		btn_use_code.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String vcode = et_code.getText().toString().trim();
				if (StringUtils.isEmpty(vcode)) {
					ToastUtils.showToast(OrderDetailActivity.this,
							"请输入电子卷码");
					return;
				}
				ApiBuyUtils.couponsManage(OrderDetailActivity.this,
						user.getUserId(), 0, vcode, 4,0,0,
						new HttpConnectionUtil.RequestCallback() {

							@Override
							public void execute(ParseModel parseModel) {
								if (!ApiConstants.RESULT_SUCCESS
										.equals(parseModel.getStatus())) {
									ToastUtils.showToast(
											OrderDetailActivity.this,
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
		//确定支付
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*ApiBuyUtils.createOrder(AppointmentDoor3Activity.this,
						user.getUserId(), user.getUsername(), "",
						seller.getUsername(), "", product.getName(), orderPay,
						curuser, type, contactor, mobile, detailAddress, notes,
						payType, new HttpConnectionUtil.RequestCallback() {

							@Override
							public void execute(ParseModel parseModel) {

							}
						});*/
			}
		});
		//取消订单
		btn_cancel_order.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		
	}
}
