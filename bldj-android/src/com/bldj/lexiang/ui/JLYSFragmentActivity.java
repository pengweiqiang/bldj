package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.CustomViewPager;

/**
 * 经络养生界面
 * 
 * @author will
 * 
 */
@SuppressLint("NewApi")
public class JLYSFragmentActivity extends FragmentActivity {

	CustomViewPager mViewPager;
	private ActionBar mActionBar;


	private TextView tab_health_service;
	private TextView tab_see_health_div;

	List<Fragment> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jlys);

		
		initView();

		initListener();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("经络养生");
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
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
		
		tab_health_service = (TextView) findViewById(R.id.tab_health_service);
		tab_see_health_div = (TextView) findViewById(R.id.tab_see_health_div);

		// 实例化对象
		list = new ArrayList<Fragment>();

		// 设置数据源
		//养生服务
		HealthServiceFragment jlysFragment = new HealthServiceFragment();
		//看养生师
		SeeHealthDivFragment kmrsFragment = new SeeHealthDivFragment();

		list.add(jlysFragment);
		list.add(kmrsFragment);

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
				setCurrentTitle(position);

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
		tab_health_service.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setCurrentTitle(0);

			}
		});

		tab_see_health_div.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setCurrentTitle(1);
			}
		});

	}

	public void setCurrentTitle(int position) {

		int height = tab_health_service.getHeight();
		tab_health_service.setHeight(height);
		tab_see_health_div.setHeight(height);
		if (position == 0) {
			tab_health_service.setBackground(getResources().getDrawable(
					R.drawable.select_left));
			tab_see_health_div.setBackground(getResources().getDrawable(
					R.drawable.unselect_right));
			mViewPager.setCurrentItem(0, false);
		} else if (position == 1) {
			tab_health_service.setBackground(getResources().getDrawable(
					R.drawable.unselect_left));
			tab_see_health_div.setBackground(getResources().getDrawable(
					R.drawable.select_right));
		}
		mViewPager.setCurrentItem(position, false);

	}

}
