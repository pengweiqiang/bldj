package com.bldj.lexiang.ui;

import java.util.HashMap;
import java.util.Map;


import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 所有 Fragment 的基类
 * 
 * @author Will
 *
 */
public abstract class BaseFragment extends Fragment {

	protected static final int FILL_PARENT = -1;
	protected static final int WRAP_CONTENT = -2;

	protected Activity mActivity;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (Activity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("MainScreen"); 
	}
	/**
	 * 展示空白页面
	 * @param view
	 * @param tipStr
	 * @param goStr
	 * @param imageId
	 * @param emptyClick
	 */
	public void showEmpty(View view,int tipId,int goId,int imageId,OnClickListener emptyClick){
		ImageView imageView = (ImageView)view.findViewById(R.id.empty_imageView);
		imageView.setImageDrawable(this.getResources().getDrawable(imageId));
		TextView textTip = (TextView)view.findViewById(R.id.empty_text_tip);
		textTip.setText(getResources().getString(tipId));
		TextView textGo = (TextView)view.findViewById(R.id.empty_text_go);
		if(goId == 0){
			textGo.setVisibility(View.GONE);
		}else{
			textGo.setText(getResources().getString(goId));
		}
		textGo.setOnClickListener(emptyClick);
	}
}
