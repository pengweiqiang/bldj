package com.bldj.lexiang.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.universalimageloader.core.ImageLoader;
import com.bldj.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.bldj.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 显示图片
 * 
 * @author will
 * 
 */
public class ImageViewActivity extends BaseActivity {

	ActionBar mActionBar;
	private ImageView imageView;
	private ProgressBar progressBar;
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
		
		
		
		ImageLoader.getInstance()
		.displayImage(imageUrl, imageView, 
				MyApplication.getInstance().getOptions(R.drawable.default_head_image),
				new SimpleImageLoadingListener(){

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						progressBar.setVisibility(View.GONE);
						super.onLoadingComplete(imageUri, view, loadedImage);
					}
					
			
		},
		//显示下载图片的进度条
		new ImageLoadingProgressListener() {
			
			@Override
			public void onProgressUpdate(String imageUri, View view, int current, int total) {
				progressBar.setVisibility(View.VISIBLE);
				int progress = Math.round(100.0f* current / total);
				progressBar.setProgress(progress);
			}
		});
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
		progressBar = (ProgressBar)findViewById(R.id.web_progress);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

}
