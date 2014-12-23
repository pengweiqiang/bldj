package com.bldj.lexiang.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	View layout_package2;
	View layout_package3;
	View layout_package4;

	TextView tv_package1_title;
	TextView tvpackage2_info1,tvpackage2_info2,tvpackage2_info3;
	TextView tvpackage_3_info1,tvpackage_3_info2,tvpackage_3_info3;
	TextView tvpackage_4_info1,tvpackage_4_info2,tvpackage_4_info3;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.company_zone_select_package);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		initData();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("优惠特区");
		actionBar.setLeftActionButton(R.drawable.btn_back,
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
		layout_package2 = findViewById(R.id.package2);
		layout_package3 = findViewById(R.id.package3);
		layout_package4 = findViewById(R.id.package4);
		tv_package1_title = (TextView) findViewById(R.id.package1_title);
		tvpackage2_info1 = (TextView) findViewById(R.id.package2_info1);
		tvpackage2_info2 = (TextView) findViewById(R.id.package2_info2);
		tvpackage2_info3 = (TextView) findViewById(R.id.package2_info3);
		tvpackage_3_info1 = (TextView) findViewById(R.id.package_3_info1);
		tvpackage_3_info2 = (TextView) findViewById(R.id.package_3_info2);
		tvpackage_3_info3 = (TextView) findViewById(R.id.package_3_info3);
		tvpackage_4_info1 = (TextView) findViewById(R.id.package_4_info_1);
		tvpackage_4_info2 = (TextView) findViewById(R.id.package_4_info_2);
		tvpackage_4_info3 = (TextView) findViewById(R.id.package_4_info_3);
	}

	private void initData() {
		// 企业0元体验中的0字体变红
		setIndexTextColor(tv_package1_title,R.color.color_package1_title);
		// 企业0元体验中的0字体变红
		setIndexTextColor(tvpackage2_info1,R.color.line_company_package2);
		setIndexTextColor(tvpackage2_info2,R.color.line_company_package2);
		setIndexTextColor(tvpackage2_info3,R.color.line_company_package2);
		
		setIndexTextColor(tvpackage_3_info1,R.color.line_company_package3);
		setIndexTextColor(tvpackage_3_info2,R.color.line_company_package3);
		setIndexTextColor(tvpackage_3_info3,R.color.line_company_package3);
		
		setIndexTextColor(tvpackage_4_info1,R.color.line_company_package4);
		setIndexTextColor(tvpackage_4_info2,R.color.line_company_package4);
		setIndexTextColor(tvpackage_4_info3,R.color.line_company_package4);

	}
	
	private void setIndexTextColor(TextView tv,int colorId){
		String str = tv.getText().toString();
		Pattern p = Pattern.compile("[0-9]");
		Matcher m = p.matcher(str.substring(1));
		SpannableStringBuilder style = new SpannableStringBuilder(
				str);
		while(m.find()){
			int index = m.start()+1;
			style.setSpan(
					new ForegroundColorSpan(getResources().getColor(
							colorId)), index, index+1,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		tv.setText(style);
	}

	@Override
	public void initListener() {
		layout_package1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						CompanyZoneSelectPackageActivity.this,
						CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 0);
				intent.putExtra("serviceTypeName",
						getResources().getString(R.string.package_1));
				startActivity(intent);
			}
		});
		layout_package2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						CompanyZoneSelectPackageActivity.this,
						CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 1);
				intent.putExtra("serviceTypeName", "2万元"
						+ getResources().getString(R.string.package_service));
				startActivity(intent);
			}
		});
		layout_package3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						CompanyZoneSelectPackageActivity.this,
						CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 2);
				intent.putExtra("serviceTypeName", "5万元"
						+ getResources().getString(R.string.package_service));
				startActivity(intent);
			}
		});
		layout_package4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						CompanyZoneSelectPackageActivity.this,
						CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 3);
				intent.putExtra("serviceTypeName", "10万元"
						+ getResources().getString(R.string.package_service));
				startActivity(intent);
			}
		});
	}

}
