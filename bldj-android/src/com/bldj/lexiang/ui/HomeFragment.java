package com.bldj.lexiang.ui;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 精品推荐
 * @author will
 *
 */
public class HomeFragment extends BaseFragment{
	private View infoView;
	private ActionBar mActionBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.home_fragment, container, false);
		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		return infoView;
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("便利到家");
		actionBar.hideLeftActionButton();
		actionBar.hideRightActionButton();
	}
}
