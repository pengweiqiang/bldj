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
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.PatternUtils;
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
		actionBar.setLeftActionButton(R.drawable.btn_back,
				new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		actionBar.setRightTextActionButton("", R.drawable.add_finish, true, new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				final String name = et_contact_name.getText().toString().trim();
				final String address = et_contact_address.getText().toString().trim();
				String phone = et_contact_phone.getText().toString().trim();
				if(StringUtils.isEmpty(name)){
					et_contact_name.requestFocus();
					ToastUtils.showToast(AddressInfoActivity.this, "联系人姓名不能为空！");
					return;
				}
				if(StringUtils.isEmpty(phone)){
					et_contact_phone.requestFocus();
					ToastUtils.showToast(AddressInfoActivity.this, "手机号码不能为空！");
					return;
				}
				if(StringUtils.isEmpty(address)){
					et_contact_address.requestFocus();
					ToastUtils.showToast(AddressInfoActivity.this, "详细地址不能为空！");
					return;
				}
				if(!PatternUtils.checkPhoneNum(phone)){
					et_contact_phone.requestFocus();
					ToastUtils.showToast(AddressInfoActivity.this, "请输入正确的手机号");
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
				ApiUserUtils.addressManager(AddressInfoActivity.this, type, user.getUserId(),curLocation , address, addressId,name, new HttpConnectionUtil.RequestCallback() {
					
					@Override
					public void execute(ParseModel parseModel) {
						loading.cancel();
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(AddressInfoActivity.this, parseModel.getMsg());
						}else{
							if(type == 2){//内存中默认修改	
								addressVo.setContactor(name);
								addressVo.setDetailAddress(address);
								String addressJson = (String)SharePreferenceManager.getSharePreferenceValue(mContext, Constant.FILE_NAME, "defaultAddress","");
								if(!StringUtils.isEmpty(addressJson)){
									Address defaultAddress = (Address)JsonUtils.fromJson(addressJson, Address.class);
									if(defaultAddress.getId() == addressVo.getId()){
										SharePreferenceManager.saveBatchSharedPreference(mContext, Constant.FILE_NAME, "defaultAddress",JsonUtils.toJson(addressVo));
									}
								}
							}
							ToastUtils.showToast(AddressInfoActivity.this, parseModel.getMsg());
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
		
		
		et_contact_phone.setText(user.getMobile());
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}
	public void initAddressData(){
		et_contact_name.setText(addressVo.getContactor());
		et_contact_address.setText(addressVo.getDetailAddress());
		
	}

}
