package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

/**
 * 我的
 * 
 * @author will
 * 
 */
public class MyFragment extends BaseFragment {

	private View infoView;
	private ActionBar mActionBar;
	private Button btn_logout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.my_fragment, container, false);
		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
		btn_logout = (Button) infoView.findViewById(R.id.btn_logout);
		
		
		initListener();
		return infoView;
	}
	
	private void initListener(){
		btn_logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity,RegisterAndLoginActivity.class);
				startActivity(intent);
			}
		});
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我");
		actionBar.hideLeftActionButton();
		actionBar.hideRightActionButton();
	}

}
