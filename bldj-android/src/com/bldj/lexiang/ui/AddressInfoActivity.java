package com.bldj.lexiang.ui;

import com.bldj.lexiang.R;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * 详细地址
 * 
 * @author will
 * 
 */
public class AddressInfoActivity extends BaseActivity {

	ActionBar mActionBar;
	private EditText et_contact_name,et_contact_address,et_contact_phone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update_address);
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("添加地址");
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
				new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		actionBar.setRightTextActionButton("完成", new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String name = et_contact_name.getText().toString().trim();
				String address = et_contact_address.getText().toString().trim();
				String phone = et_contact_phone.getText().toString().trim();
				if(StringUtils.isEmpty(name)){
					ToastUtils.showToast(AddressInfoActivity.this, "联系人姓名不能为空！");
					return;
				}
				if(StringUtils.isEmpty(phone)){
					ToastUtils.showToast(AddressInfoActivity.this, "手机号码不能为空！");
					return;
				}
				if(StringUtils.isEmpty(address)){
					ToastUtils.showToast(AddressInfoActivity.this, "详细地址不能为空！");
					return;
				}
				//TODO 调用接口
			}
		});
	}

	@Override
	public void initView() {
		et_contact_name = (EditText)findViewById(R.id.contact_name);
		et_contact_address = (EditText)findViewById(R.id.contact_address);
		et_contact_phone = (EditText)findViewById(R.id.contact_phone);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

}
