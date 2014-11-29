package com.bldj.lexiang.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.CustomViewPager;
import com.bldj.lexiang.view.TabPageIndicator;

/**
 * 我要预约
 * 
 * @author will
 * 
 */
@SuppressLint("NewApi")
public class AppointmentFragmentActivity extends BaseFragmentActivity {

	CustomViewPager mViewPager;
	private ActionBar mActionBar;


	TabPageIndicator tabPageIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appointment);

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
				Intent intent = new Intent(AppointmentFragmentActivity.this,AddressesActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initView() {

		tabPageIndicator = (TabPageIndicator)findViewById(R.id.tabPageIndicator);
		mViewPager = (CustomViewPager) findViewById(R.id.viewPager);
		// mViewPager.setScanScroll(false);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
		// 绑定适配器
		mViewPager.setAdapter(adapter);
		tabPageIndicator.setViewPager(mViewPager);
	}

	private void initListener() {

	}
	
	
	public class MainFragmentAdapter extends FragmentPagerAdapter {  
		  
	    private String[] titles = new String[]{"给自己预约","给他人预约"};  
	      
	    public MainFragmentAdapter(FragmentManager fm) {  
	        super(fm);  
	    }  
	  
	    public MainFragmentAdapter(FragmentManager fm,Context context) {  
	        super(fm);  
	    }  
	  
	    @Override  
	    public Fragment getItem(int position) {  
	        switch (position) {  
	        case 0:  
	            return new AppointmentMyFragment(); 
	              
	        case 1:  
	            return new AppointmentOtherFragment();
	              
	        }  
	        return null;  
	    }  
	      
	    @Override  
	    public CharSequence getPageTitle(int position) {  
	        return titles[position];  
	    }  
	      
	    @Override  
	    public int getItemPosition(Object object) {  
	        // TODO Auto-generated method stub  
	        return POSITION_NONE;  
	    }  
	  
	    @Override  
	    public int getCount() {  
	        return titles.length;  
	    }  
	  
	}  
	

}
