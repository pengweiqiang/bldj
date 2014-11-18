package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.my_fragment, container, false);
		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		return infoView;
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我");
		actionBar.hideLeftActionButton();
		actionBar.hideRightActionButton();
	}

}
