package com.bldj.lexiang.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * y轴旋转动画
 * 
 * @author yongbo.zhu
 * @email zhuyongb0@live.com
 */
public class YFlipAnimation extends Animation {
    private final float mFromDegrees;  
    private final float mToDegrees;  
    private final float mCenterX;  
    private final float mCenterY; 
    private final float mDepthZ; 
    private Camera mCamera;  
  
    public YFlipAnimation(float fromDegrees, float toDegrees, float centerX,  
            float centerY, float depthZ ) {  
        mFromDegrees = fromDegrees;  
        mToDegrees = toDegrees;  
        mCenterX = centerX;  
        mCenterY = centerY; 
        mDepthZ = depthZ; 
    }  
  
    @Override  
    public void initialize(int width, int height, int parentWidth,  
            int parentHeight) {  
        super.initialize(width, height, parentWidth, parentHeight);  
        mCamera = new Camera();  
    }  
  
    @Override  
    protected void applyTransformation(float interpolatedTime, Transformation t) {  
        final float fromDegrees = mFromDegrees;  
        float degrees = fromDegrees  
                + ((mToDegrees - fromDegrees) * interpolatedTime);  
  
        final float centerX = mCenterX;  
        final float centerY = mCenterY;  
        final Camera camera = mCamera;  
  
        final Matrix matrix = t.getMatrix();  
  
        camera.save();  
 
        camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));  
        camera.rotateY(degrees);  
        camera.getMatrix(matrix);  
        camera.restore();  
  
        matrix.preTranslate(-centerX, -centerY);  
        matrix.postTranslate(centerX, centerY);  
    }  
}  
