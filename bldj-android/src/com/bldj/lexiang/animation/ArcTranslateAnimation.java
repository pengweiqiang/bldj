package com.bldj.lexiang.animation;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 曲线平移动画
 * 
 * @author yongbo.zhu
 * @email zhuyongb0@live.com
 */
public class ArcTranslateAnimation extends Animation {
	private int mFromXType = ABSOLUTE;
	private int mToXType = ABSOLUTE;
	
	private int mFromYType = ABSOLUTE;
	private int mToYType = ABSOLUTE;
	
	private float mFromXValue = 0.0f;
	private float mToXValue = 0.0f;
	
	private float mFromYValue = 0.0f;
	private float mToYValue = 0.0f;
	
	private float mFromXDelta;
	private float mToXDelta;
	private float mFromYDelta;
	private float mToYDelta;
	
	private PointF mStart;
	private PointF mControl;
	private PointF mEnd;
//	private Camera mCamera;
	
	/**
	 * 构造曲线平移动画
	 * 
	 * @param fromXDelta 起点的x坐标
	 * @param toXDelta 终点的x坐标
	 * @param fromYDelta 起点的y坐标
	 * @param toYDelta 终点的y坐标
	 */
	public ArcTranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
		mFromXValue = fromXDelta;
		mToXValue = toXDelta;
		mFromYValue = fromYDelta;
		mToYValue = toYDelta;
		
		mFromXType = ABSOLUTE;
		mToXType = ABSOLUTE;
		mFromYType = ABSOLUTE;
		mToYType = ABSOLUTE;
	}
	
	/**
	 * 构造曲线平移动画
	 * 
	 * @param fromXType 指定fromXValue数值的类型. 类型是Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF或者
	 *            Animation.RELATIVE_TO_PARENT之一.
	 * @param fromXValue 起点的x值.如果fromXType是ABSOLUTE，改值为坐标值；其它情况是百分比(where 1.0 is 100%)
	 * @param toXType 指定toXType数值的类型. 类型是Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF或者
	 *            Animation.RELATIVE_TO_PARENT之一.
	 * @param toXValue 终点的x值.如果fromXType是ABSOLUTE，改值为坐标值；其它情况是百分比(where 1.0 is 100%)
	 * @param fromYType 指定fromYType数值的类型. 类型是Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF或者
	 *            Animation.RELATIVE_TO_PARENT之一.
	 * @param fromYValue 起点的y值.如果fromXType是ABSOLUTE，改值为坐标值；其它情况是百分比(where 1.0 is 100%)
	 * @param toYType 指定toYType数值的类型. 类型是Animation.ABSOLUTE, Animation.RELATIVE_TO_SELF或者
	 *            Animation.RELATIVE_TO_PARENT之一.
	 * @param toYValue 终点的x值.如果fromXType是ABSOLUTE，改值为坐标值；其它情况是百分比(where 1.0 is 100%)
	 */
	public ArcTranslateAnimation(int fromXType, float fromXValue, int toXType, float toXValue, int fromYType,
			float fromYValue, int toYType, float toYValue) {
		
		mFromXValue = fromXValue;
		mToXValue = toXValue;
		mFromYValue = fromYValue;
		mToYValue = toYValue;
		
		mFromXType = fromXType;
		mToXType = toXType;
		mFromYType = fromYType;
		mToYType = toYType;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		float dx = calcBezier(interpolatedTime, mStart.x, mControl.x, mEnd.x);
		float dy = calcBezier(interpolatedTime, mStart.y, mControl.y, mEnd.y);
		final Matrix matrix = t.getMatrix();
		// final Camera camera = mCamera;
		// camera.save();
		
		// camera.translate(dx, dy, 300 * interpolatedTime);
		matrix.postScale(1 - interpolatedTime, 1 - interpolatedTime);
		matrix.postTranslate(dx, dy);
		
		// camera.getMatrix(matrix);
		// camera.restore();
	}
	
	@Override
	public void initialize(int width, int height, int parentWidth, int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mFromXDelta = resolveSize(mFromXType, mFromXValue, width, parentWidth);
		mToXDelta = resolveSize(mToXType, mToXValue, width, parentWidth);
		mFromYDelta = resolveSize(mFromYType, mFromYValue, height, parentHeight);
		mToYDelta = resolveSize(mToYType, mToYValue, height, parentHeight);
		
		mStart = new PointF(mFromXDelta, mFromYDelta);
		mEnd = new PointF(mToXDelta, mToYDelta);
		mControl = new PointF(mToXDelta, mFromYDelta);
//		mCamera = new Camera();
	}
	
	/**
	 * 按贝塞尔曲线计算位置.
	 * 
	 * @param interpolatedTime the fraction of the duration that has passed where 0 <= time <= 1
	 * @param p0 a single dimension of the starting point
	 * @param p1 a single dimension of the control point
	 * @param p2 a single dimension of the ending point
	 */
	private long calcBezier(float interpolatedTime, float p0, float p1, float p2) {
		return Math.round((Math.pow((1 - interpolatedTime), 2) * p0)
				+ (2 * (1 - interpolatedTime) * interpolatedTime * p1) + (Math.pow(interpolatedTime, 2) * p2));
	}
}
