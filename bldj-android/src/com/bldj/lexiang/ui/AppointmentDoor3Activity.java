package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.view.ActionBar;

/**
 * 上门预约2
 * 
 * @author will
 * 
 */
public class AppointmentDoor3Activity extends BaseActivity {

	ActionBar mActionBar;
	private String time;
	private Seller seller;
	private Product product;

	private User user;
	private TextView tv_sellerName;
	private TextView tv_productName;
	private TextView tv_time;
	private Button btn_confirm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.appointment_door3);
		super.onCreate(savedInstanceState);

		time = this.getIntent().getStringExtra("time");
		seller = (Seller) this.getIntent().getSerializableExtra("seller");
		product = (Product) this.getIntent().getSerializableExtra("product");

		user = MyApplication.getInstance().getCurrentUser();

		initView();

		initListener();

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
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		tv_time.setText(time);
		tv_sellerName.setText(seller.getUsername());
		tv_productName.setText(product.getName());

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
	}
}
