package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Scheduled;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

/**
 * 上门预约1
 * 
 * @author will
 * 
 */
public class AppointmentDoor1Activity extends BaseActivity{

	ActionBar mActionBar;
	Button btn_next;
	String time = "2014-12-2 10:30:00";
	String dealDate = "20141202";
	Button btn_address;
	private String address;
	Product product;
	User user;
	
	Scheduled scheduled;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.appointment_door1);
		super.onCreate(savedInstanceState);
		product = (Product)this.getIntent().getSerializableExtra("product");
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
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		btn_next = (Button)findViewById(R.id.btn_next);
		btn_address = (Button)findViewById(R.id.btn_address);
	}

	@Override
	public void initListener() {
		
		btn_address.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AppointmentDoor1Activity.this,AddressesActivity.class);
				intent.putExtra("type", 1);
				startActivityForResult(intent, 123);
			}
		});
		
		btn_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppointmentDoor1Activity.this,AppointmentDoor2Activity.class);
				intent.putExtra("time", time);
				intent.putExtra("product", product);
				intent.putExtra("address", address);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 20){
			address = data.getStringExtra("address");
			btn_address.setText(address);
		}
	}
	
	private void getData(){
		ApiBuyUtils.getScheduled(mContext, user.getUserId(), product.getId(),
				dealDate, new HttpConnectionUtil.RequestCallback() {
			
			@Override
			public void execute(ParseModel parseModel) {
				if(!ApiConstants.RESULT_SUCCESS.equals(parseModel.getStatus())){
					ToastUtils.showToast(mContext, parseModel.getMsg());
				}else{
					scheduled = (Scheduled)JsonUtils.fromJson(parseModel.getData().toString(), Scheduled.class);
				}
				
			}
		});
	}
	

}
