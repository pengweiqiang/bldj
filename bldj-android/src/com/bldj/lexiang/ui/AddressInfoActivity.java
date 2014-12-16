package com.bldj.lexiang.ui;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.Address;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.LoadingDialog;

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
	private int type;//0--增加地址  2--修改地址 
	private Address addressVo;
	private String title = "增加地址";
	private User user ;
	LoadingDialog loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		user = MyApplication.getInstance().getCurrentUser();
		setContentView(R.layout.update_address);//setContentView要放在super.onCreate前面，不然会报nullPointer
		super.onCreate(savedInstanceState);
		type = getIntent().getIntExtra("type", 0);//地址类型
		if(type==2){
			addressVo = (Address)getIntent().getSerializableExtra("address");
			initAddressData();
			title = "修改地址";
		}
		
		
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle(title);
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
				String addressId = "";
				final String curLocation = address;
				if(address!=null && type==2){//修改地址
					addressId = String.valueOf(addressVo.getId());
				}
				loading = new LoadingDialog(mContext);
				loading.show();
				ApiUserUtils.addressManager(AddressInfoActivity.this, type, user.getUserId(),curLocation , address, addressId, new HttpConnectionUtil.RequestCallback() {
					
					@Override
					public void execute(ParseModel parseModel) {
						loading.cancel();
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(AddressInfoActivity.this, parseModel.getMsg());
						}else{
							ToastUtils.showToast(AddressInfoActivity.this, parseModel.getMsg());
							SharePreferenceManager.saveBatchSharedPreference(mContext, Constant.FILE_NAME, "address",curLocation);
							finish();
						}
					}
				});
			}
		});
	}

	@Override
	public void initView() {
		et_contact_name = (EditText)findViewById(R.id.contact_name);
		et_contact_address = (EditText)findViewById(R.id.contact_address);
		et_contact_phone = (EditText)findViewById(R.id.contact_phone);
		
		et_contact_name.setText(user.getNickname());
		et_contact_phone.setText(user.getMobile());
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}
	public void initAddressData(){
		et_contact_address.setText(addressVo.getDetailAddress());
		
	}

}
