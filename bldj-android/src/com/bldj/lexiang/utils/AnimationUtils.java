package com.bldj.lexiang.utils;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.bldj.lexiang.animation.ArcTranslateAnimation;
import com.bldj.lexiang.animation.XFlipAnimation;
import com.bldj.lexiang.animation.YFlipAnimation;

/**
 * 动画
 * 
 * @author yongbo.zhu
 * 
 */
public class AnimationUtils {
    public static XFlipAnimation getXFlipAnimation(float start, float end,
	    float centerX, float centerY) {
	XFlipAnimation flipAnimation = new XFlipAnimation(start, end, centerX,
		centerY);
	flipAnimation.setDuration(300);
	flipAnimation.setFillAfter(true);
	flipAnimation.setInterpolator(new AccelerateInterpolator());
	return flipAnimation;
    }

    public static YFlipAnimation getYFlipAnimation(float start, float end,
	    float centerX, float centerY) {
	YFlipAnimation flipAnimation = new YFlipAnimation(start, end, centerX,
		centerY, 200.0f);
	flipAnimation.setDuration(300);
	flipAnimation.setFillAfter(true);
	flipAnimation.setInterpolator(new AccelerateInterpolator());
	return flipAnimation;
    }

    public static void downloadAnimation(final Context context,
	    final ImageView iv, final float fromX, final float toX,
	    final float fromY, final float toY,final DownloadClickOperate downloadClickOperate) {
	TranslateAnimation translateAnimation = new TranslateAnimation(0, 10, 0, 0);

	// 利用 CycleInterpolator 参数 为float 的数 表示
	// 抖动的次数，而抖动的快慢是由 duration 和 CycleInterpolator
	// 的参数的大小 联合确定的
	translateAnimation.setInterpolator(new CycleInterpolator(3f));
	translateAnimation.setDuration(300);
	translateAnimation.setAnimationListener(new AnimationListener() {

	    @Override
	    public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		ArcTranslateAnimation arcTranslateAnimation = new ArcTranslateAnimation(0, toX
			- fromX, 0, toY - fromY);
		arcTranslateAnimation.setInterpolator(new LinearInterpolator());
		arcTranslateAnimation.setDuration(500);
		arcTranslateAnimation.setFillAfter(true);
		arcTranslateAnimation.setAnimationListener(new AnimationListener() {

		    @Override
		    public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub

		    }

		    @Override
		    public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			downloadClickOperate.operate(iv);
		    }
		});
		iv.startAnimation(arcTranslateAnimation);
	    }
	});
	iv.startAnimation(translateAnimation);

    }

}
