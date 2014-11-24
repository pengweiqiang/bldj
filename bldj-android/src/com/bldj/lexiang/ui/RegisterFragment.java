package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

/**
 * 登录
 * 
 * @author will
 * 
 */
public class RegisterFragment extends BaseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.register, container, false);
		ActionBar actionBar = (ActionBar)mActivity.findViewById(R.id.actionBar);
		actionBar.setLeftActionButton(R.drawable.ic_menu_back, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mActivity.finish();
			}
		});
		return v;
	}

}
