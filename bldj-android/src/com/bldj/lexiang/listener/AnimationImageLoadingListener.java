package com.bldj.lexiang.listener;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.bldj.universalimageloader.core.assist.FailReason;
import com.bldj.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 为列表图片添加动画
 * 
 * @author yongbo.zhu
 * 
 */
public class AnimationImageLoadingListener implements ImageLoadingListener {
	//private Context mContext;
	private ImageView mImageView;

	public AnimationImageLoadingListener(Context context, ImageView imageView) {
		//this.mContext = context;
		this.mImageView = imageView;
	}

	@Override
	public void onLoadingStarted(String imageUri, View view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		// TODO Auto-generated method stub
		final ScaleAnimation animation =new ScaleAnimation(0.9f, 1.0f, 0.9f, 1.0f, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
				animation.setDuration(200);//设置动画持续时间 
		mImageView.setAnimation(animation);
		animation.start();
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		// TODO Auto-generated method stub

	}

}
