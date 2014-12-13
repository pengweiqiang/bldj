package com.bldj.lexiang.view;

import java.util.Random;

import com.bldj.lexiang.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {

	
	public CircleView(Context context) {
		super(context);
	}
	public CircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}


	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 创建画笔
		Paint p = new Paint();
		Random rand = new Random();
		int index = rand.nextInt(20);
		int color = Color.RED;
		switch (index) {
		case 0:
			color = this.getContext().getResources().getColor(R.color.color_random_one);
			break;
		case 1:
			color = this.getContext().getResources().getColor(R.color.color_random_two);
			break;
		case 2:
			color = this.getContext().getResources().getColor(R.color.color_random_three);
			break;
		case 3:
			color = this.getContext().getResources().getColor(R.color.color_random_four);
			break;
		case 4:
			color = this.getContext().getResources().getColor(R.color.color_random_five);
			break;
		case 5:
			color = this.getContext().getResources().getColor(R.color.color_random_six);
			break;
		case 6:
			color = this.getContext().getResources().getColor(R.color.color_random_seven);
			break;
		case 7:
			color = this.getContext().getResources().getColor(R.color.color_random_eight);
			break;
		case 8:
			color = this.getContext().getResources().getColor(R.color.color_random_nine);
			break;
		case 9:
			color = this.getContext().getResources().getColor(R.color.color_random_ten);
			break;
		case 10:
			color = this.getContext().getResources().getColor(R.color.color_random_eleven);
			break;
		case 11:
			color = this.getContext().getResources().getColor(R.color.color_random_twelve);
			break;
		case 12:
			color = this.getContext().getResources().getColor(R.color.color_random_thirteen);
			break;
		case 13:
			color = this.getContext().getResources().getColor(R.color.color_album_gray);
			break;
		case 14:
			color = this.getContext().getResources().getColor(R.color.color_font_blue);
			break;
		case 15:
			color = this.getContext().getResources().getColor(R.color.blue);
			break;
		case 16:
			color =	Color.CYAN;
			break;
		case 17:
			color = Color.LTGRAY;
			break;
		case 18:
			color = Color.CYAN;
			break;
//		case 19:
//			color = this.getContext().getResources().getColor(R.color.color_random_two);
//			break;
//		case 20:
//			color = this.getContext().getResources().getColor(R.color.color_random_two);
//			break;
		default:
			break;
		}
		p.setColor(color);// 设置红色

//		canvas.drawText("画圆：", 10, 20, p);// 画文本
//		canvas.drawCircle(60, 20, 10, p);// 小圆
		p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了
		canvas.drawCircle(60, 20, 10, p);// 大圆

	}
}
