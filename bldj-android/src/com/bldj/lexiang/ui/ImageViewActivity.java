package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.universalimageloader.core.ImageLoader;

/**
 * 显示图片
 * 
 * @author will
 * 
 */
public class ImageViewActivity extends BaseActivity {

	ActionBar mActionBar;
	private ImageView imageView;
	String imageUrl;
	String name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.image_view);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		imageUrl = this.getIntent().getStringExtra("url");
		name = this.getIntent().getStringExtra("name");
		onConfigureActionBar(mActionBar);
		
		
		ImageLoader.getInstance().displayImage(
				imageUrl,
				imageView,
				MyApplication.getInstance().getOptions(
						R.drawable.default_head_image));
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle(name);
		actionBar.setLeftActionButton(R.drawable.btn_back,
				new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		actionBar.hideRightActionButton();
	}

	@Override
	public void initView() {
		imageView = (ImageView)findViewById(R.id.image);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

}
