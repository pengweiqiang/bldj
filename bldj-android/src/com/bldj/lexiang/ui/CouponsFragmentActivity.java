package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Coupon;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.CustomViewPager;

/**
 * 我的优惠卷
 * 
 * @author will
 * 
 */
@SuppressLint("NewApi")
public class CouponsFragmentActivity extends BaseFragmentActivity {

	CustomViewPager mViewPager;
	private ActionBar mActionBar;

	private RadioGroup tab_radioGroup;
	private RadioButton tab_unused;
	private RadioButton tab_failure;

	List<Fragment> list;
	int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coupons);
		type = this.getIntent().getIntExtra("type", 0);

		initView();

		initListener();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我的优惠卷");
		actionBar.setLeftActionButton(R.drawable.btn_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.hideRightActionButton();
	}

	private void initView() {

		mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
		// mViewPager.setScanScroll(false);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		tab_radioGroup = (RadioGroup) findViewById(R.id.rg_title);
		tab_unused = (RadioButton) findViewById(R.id.tab_unused);
		tab_failure = (RadioButton) findViewById(R.id.tab_failure);

		// 实例化对象
		list = new ArrayList<Fragment>();

		// 设置数据源
		UnusedCouponsFragment unusedFragment = new UnusedCouponsFragment();// 未使用

		FailureCouponsFragment failureFragment = new FailureCouponsFragment();// 已失效

		list.add(unusedFragment);
		list.add(failureFragment);

		// 设置适配器
		FragmentPagerAdapter adapter = new FragmentPagerAdapter(
				getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return list.get(arg0);
			}
		};

		// 绑定适配器
		mViewPager.setAdapter(adapter);
		// 设置滑动监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				if (position == 0) {
					tab_unused.setChecked(true);
				} else if (position == 1) {
					tab_failure.setChecked(true);
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Log.i("tuzi", arg0 + "," + arg1 + "," + arg2);

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void initListener() {
		tab_radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.tab_unused) {
							mViewPager.setCurrentItem(0, false);
						} else if (checkedId == R.id.tab_failure) {
							mViewPager.setCurrentItem(1, false);
						}
					}
				});

	}

}
