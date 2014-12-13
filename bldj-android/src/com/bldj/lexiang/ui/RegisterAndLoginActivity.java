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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.CustomViewPager;

/**
 * 登录注册界面
 * 
 * @author will
 * 
 */
@SuppressLint("NewApi")
public class RegisterAndLoginActivity extends BaseFragmentActivity {

	CustomViewPager mViewPager;
	private ActionBar mActionBar;

	private int tabLineLength;// 1/2屏幕宽
	private int currentPage = 0;// 初始化当前页为0（第一页）
	private TextView tabline;

	private RadioGroup tab_radioGroup;
	private RadioButton register_menu;
	private RadioButton login_menu;

	List<Fragment> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_login);

		// initTabLine();

		initView();

		initListener();
	}

	/*
	 * private void initTabLine() { // 获取显示屏信息 Display display =
	 * getWindow().getWindowManager().getDefaultDisplay(); // 得到显示屏宽度
	 * DisplayMetrics metrics = new DisplayMetrics();
	 * display.getMetrics(metrics); // 1/2屏幕宽度 tabLineLength =
	 * (metrics.widthPixels - 30) / 2; // tabLineLength =
	 * register_menu.getWidth(); // 获取控件实例 tabline = (TextView)
	 * findViewById(R.id.tabline); // 控件参数 LayoutParams lp =
	 * tabline.getLayoutParams(); lp.width = tabLineLength;
	 * tabline.setLayoutParams(lp); }
	 */

	// // 登录
	// btn_login.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// ApiUserUtils.login(
	// RegisterAndLoginActivity.this.getApplicationContext(),
	// "", "", new RequestCallback() {
	//
	// public void execute(ParseModel parseModel) {
	// if (ApiConstants.RESULT_SUCCESS
	// .equals(parseModel.getStatus())) {// 登录成功
	// User user = JsonUtils.fromJson(parseModel
	// .getData().toString(), User.class);
	// user.getUsername();
	//
	// application.setUser(user);// 保存user全局
	//
	// } else if (NetUtil.NET_ERR == Integer
	// .valueOf(parseModel.getStatus())) {// 网络异常
	// DialogUtil.showToast(
	// RegisterAndLoginActivity.this,
	// parseModel.getMsg());
	// }
	// }
	// });
	// }
	// });
	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("登录");
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
		register_menu = (RadioButton) findViewById(R.id.register_menu);
		login_menu = (RadioButton) findViewById(R.id.login_menu);

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
//				currentPage = position;
//				mViewPager.setCurrentItem(position,false);
				if(position == 0){
					login_menu.setChecked(true);
				}else if(position ==1){
					register_menu.setChecked(true);
				}

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
		
		tab_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.login_menu){
					mActionBar.setTitle("登录");
					mViewPager.setCurrentItem(0, false);
				}else if(checkedId == R.id.register_menu){
					mActionBar.setTitle("注册");
					mViewPager.setCurrentItem(1, false);
				}
			}
		});

	}


}
