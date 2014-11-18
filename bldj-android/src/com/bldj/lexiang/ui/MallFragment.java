package com.bldj.lexiang.ui;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 商城
 * @author will
 *
 */
public class MallFragment extends Fragment{
	private View infoView;
	private ActionBar mActionBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.mall_fragment, container, false);
		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		return infoView;
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("商城");
		actionBar.hideLeftActionButton();
		actionBar.hideRightActionButton();
	}
}
