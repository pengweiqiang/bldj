package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

/**
 * 企业专区-->选择套餐
 * 
 * @author will
 * 
 */
public class CompanyZoneSelectPackageActivity extends BaseActivity implements
		OnClickListener {

	ActionBar mActionBar;

	Button btn_next;
	LinearLayout layout_package1;
	LinearLayout layout_package2;
	LinearLayout layout_package3;
	LinearLayout layout_package4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.company_zone_select_package);
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
		btn_next = (Button) findViewById(R.id.btn_next);

		layout_package1 = (LinearLayout) findViewById(R.id.package1);
		layout_package2 = (LinearLayout) findViewById(R.id.package2);
		layout_package3 = (LinearLayout) findViewById(R.id.package3);
		layout_package4 = (LinearLayout) findViewById(R.id.package4);
	}

	@Override
	public void initListener() {
		layout_package1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CompanyZoneSelectPackageActivity.this,CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 0);
				intent.putExtra("serviceTypeName", getResources().getString(R.string.package_1));
				startActivity(intent);
			}
		});
		layout_package2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CompanyZoneSelectPackageActivity.this,CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 1);
				intent.putExtra("serviceTypeName", getResources().getString(R.string.package_2));
				startActivity(intent);
			}
		});
		layout_package3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CompanyZoneSelectPackageActivity.this,CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 2);
				intent.putExtra("serviceTypeName", getResources().getString(R.string.package_3));
				startActivity(intent);
			}
		});
		layout_package4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(CompanyZoneSelectPackageActivity.this,CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 3);
				intent.putExtra("serviceTypeName", getResources().getString(R.string.package_4));
				startActivity(intent);
			}
		});
	}

}
