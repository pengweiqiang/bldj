package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.api.vo.User;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.order_detail);
		super.onCreate(savedInstanceState);

		order = (Order) this.getIntent().getSerializableExtra("order");

		user = MyApplication.getInstance().getCurrentUser();

		initView();

		initListener();

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
		tv_order_num = (TextView)findViewById(R.id.order_num);
		tv_order_pay = (TextView)findViewById(R.id.order_pay);
		tv_order_time = (TextView)findViewById(R.id.order_time);
		
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		tv_order_time.setText(order.getCreatetime());
		tv_order_pay.setText(order.getOrderPay());
		tv_order_num.setText(String.valueOf(order.getOrderNum()));
		
	}

	@Override
	public void initListener() {
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
