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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.CustomViewPager;

/**
 * 收藏界面
 * 
 * @author will
 * 
 */
@SuppressLint("NewApi")
public class MyCollectFragmentActivity extends BaseFragmentActivity {

	CustomViewPager mViewPager;
	private ActionBar mActionBar;

	private RadioGroup tab_radioGroup;
	private RadioButton tab_jlys;
	private RadioButton tab_kmrs;

	List<Fragment> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collect);

		
		initView();

		initListener();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我的收藏");
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
		
		tab_radioGroup = (RadioGroup)findViewById(R.id.rg_title);
		tab_jlys = (RadioButton) findViewById(R.id.tab_jlys);
		tab_kmrs = (RadioButton) findViewById(R.id.tab_kmrs);

		// 实例化对象
		list = new ArrayList<Fragment>();

		// 设置数据源
		CollectJlysFragment jlysFragment = new CollectJlysFragment();
		CollectKmrsFragment kmrsFragment = new CollectKmrsFragment();

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
				if(position == 0){
					tab_jlys.setChecked(true);
				}else if(position ==1){
					tab_kmrs.setChecked(true);
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
		tab_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.tab_jlys){
					mViewPager.setCurrentItem(0, false);
				}else if(checkedId == R.id.tab_kmrs){
					mViewPager.setCurrentItem(1, false);
				}
			}
		});
	}


}
