package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiHomeUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.company_zone);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("企业专区");
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
		et_company_name = (EditText) findViewById(R.id.company_name);
		et_contact_type = (EditText) findViewById(R.id.company_contact_type);
		et_contactor = (EditText) findViewById(R.id.company_contact);
		et_address = (EditText) findViewById(R.id.company_address);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
	}

	@Override
	public void initListener() {
		btn_confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String companyName = et_company_name.getText().toString()
						.trim();
				String concactType = et_contact_type.getText().toString()
						.trim();
				String contactor = et_contactor.getText().toString().trim();
				String address = et_address.getText().toString().trim();
				if (StringUtils.isEmpty(companyName)) {
					ToastUtils.showToast(CompanyZoneActivity.this, "请输入企业名称");
					return;
				}
				if (StringUtils.isEmpty(concactType)) {
					ToastUtils.showToast(CompanyZoneActivity.this, "请输入联系方式");
					return;
				}
				if (StringUtils.isEmpty(contactor)) {
					ToastUtils.showToast(CompanyZoneActivity.this, "请输入联系人");
					return;
				}
				if (StringUtils.isEmpty(address)) {
					ToastUtils.showToast(CompanyZoneActivity.this, "请输入详细地址");
					return;
				}
				User user = MyApplication.getInstance().getCurrentUser();
				ApiHomeUtils.createCompanyZone(CompanyZoneActivity.this,0, user.getUsername(),
						 companyName, concactType,
						contactor, address,
						new HttpConnectionUtil.RequestCallback() {

							@Override
							public void execute(ParseModel parseModel) {
								if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
										.getStatus())) {
									 ToastUtils.showToast(CompanyZoneActivity.this,
									 parseModel.getMsg());
									 return;
									
								} else {
									ToastUtils.showToast(CompanyZoneActivity.this,
											 "提交成功");
								}
							}

						});
			}
		});
	}

}
