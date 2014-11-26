package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.CustomViewPager;

/**
 * 我要预约
 * 
 * @author will
 * 
 */
@SuppressLint("NewApi")
public class AppointmentFragmentActivity extends FragmentActivity {

	CustomViewPager mViewPager;
	private ActionBar mActionBar;

	private int tabLineLength;// 1/2屏幕宽
	private int currentPage = 0;// 初始化当前页为0（第一页）
	private TextView tabline;

	private TextView register_menu;
	private TextView login_menu;

	List<Fragment> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_login);

		// initTabLine();

		initView();

		initListener();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我要预约");
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.setRightTextActionButton("常用地址", new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initView() {

		mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
		// mViewPager.setScanScroll(false);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		register_menu = (TextView) findViewById(R.id.register_menu);
		login_menu = (TextView) findViewById(R.id.login_menu);

		// 实例化对象
		list = new ArrayList<Fragment>();

		// 设置数据源
		LoginFragment loginFragment = new LoginFragment();
		RegisterFragment registerFragment = new RegisterFragment();

		list.add(loginFragment);
		list.add(registerFragment);

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
				// 当页面被选择时，先讲3个textview的字体颜色初始化成黑
				// tv1.setTextColor(Color.BLACK);
				// tv2.setTextColor(Color.BLACK);
				// tv3.setTextColor(Color.BLACK);
				//
				// // 再改变当前选择页（position）对应的textview颜色
				setCurrentTitle(position);
				currentPage = position;

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Log.i("tuzi", arg0 + "," + arg1 + "," + arg2);

				// // 取得该控件的实例
				// LinearLayout.LayoutParams ll =
				// (android.widget.LinearLayout.LayoutParams) tabline
				// .getLayoutParams();
				//
				// if (currentPage == 0 && arg0 == 0) { // 0->1移动(第一页到第二页)
				// ll.leftMargin = (int) (currentPage * tabLineLength + arg1
				// * tabLineLength);
				// } else if (currentPage == 1 && arg0 == 1) { //
				// 1->2移动（第二页到第三页）
				// ll.leftMargin = (int) (currentPage * tabLineLength + arg1
				// * tabLineLength);
				// } else if (currentPage == 1 && arg0 == 0) { //
				// 1->0移动（第二页到第一页）
				// ll.leftMargin = (int) (currentPage * tabLineLength - ((1 -
				// arg1) *
				// tabLineLength));
				// } else if (currentPage == 2 && arg0 == 1) { //
				// 2->1移动（第三页到第二页）
				// ll.leftMargin = (int) (currentPage * tabLineLength - (1 -
				// arg1)
				// * tabLineLength);
				// }
				//
				// tabline.setLayoutParams(ll);

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	private void initListener() {
		login_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setCurrentTitle(0);

			}
		});

		register_menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setCurrentTitle(1);
			}
		});

	}

	public void setCurrentTitle(int position) {

		int height = login_menu.getHeight();
		register_menu.setHeight(height);
		login_menu.setHeight(height);
		if (position == 0) {
			mActionBar.setTitle("给自己预约");
			login_menu.setBackground(getResources().getDrawable(
					R.drawable.select_left));
			register_menu.setBackground(getResources().getDrawable(
					R.drawable.unselect_right));
			mViewPager.setCurrentItem(0, false);
		} else if (position == 1) {
			mActionBar.setTitle("给他人预约");
			login_menu.setBackground(getResources().getDrawable(
					R.drawable.unselect_left));
			register_menu.setBackground(getResources().getDrawable(
					R.drawable.select_right));
		}
		mViewPager.setCurrentItem(position, false);

	}

}
