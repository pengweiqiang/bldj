package com.bldj.lexiang.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bldj.lexiang.utils.CommonHelper;
import com.bldj.lexiang.utils.R;

/**
 * 直接在xml中就可以直接指定值？ 通过attr来中转 xml -> attr 在这里java通过从attr里面获取值，在设置到
 * 通过LayoutInflater 显示的View里面
 */
public class DiscountView extends LinearLayout{
	public ImageView closeBtn;
    public View bgZone;

    public DiscountView(Context context) {
		super(context);
		this.context = context;
		initView(context);
	}

	Context context;


	@SuppressLint("ResourceAsColor")
    private void initView(Context context){
        int dp10 = CommonHelper.getDp2px(context, 10);
        int dp20 = CommonHelper.getDp2px(context, 20);
        int dp30 = CommonHelper.getDp2px(context, 30);
        int dp48 = CommonHelper.getDp2px(context, 48);
        int dp60 = CommonHelper.getDp2px(context,60);

        final LinearLayout viewRoot = new LinearLayout(context);
        FrameLayout.LayoutParams viewRootParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        viewRoot.setLayoutParams(viewRootParams);
        viewRoot.setOrientation(LinearLayout.VERTICAL);
        //viewRoot.setFocusable(true);
        //viewRoot.setFocusableInTouchMode(true);

        bgZone = new View(context);
        LinearLayout.LayoutParams bgZoneParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0);
        bgZoneParams.weight = 1.0f;
        bgZone.setLayoutParams(bgZoneParams);
        bgZone.setBackgroundColor(Color.parseColor("#A0000000"));

        RelativeLayout titleDlg = new RelativeLayout(context);
        LinearLayout.LayoutParams titleDlgParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp60);
        titleDlg.setLayoutParams(titleDlgParams);
        titleDlg.setBackgroundColor(Color.parseColor("#84B0F4"));
        titleDlg.setGravity(Gravity.CENTER_VERTICAL);

        TextView titleName = new TextView(context);
        RelativeLayout.LayoutParams titleNameParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                dp60);
        titleNameParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        titleNameParams.addRule(RelativeLayout.CENTER_VERTICAL);
        titleNameParams.leftMargin = dp10;
        titleName.setLayoutParams(titleNameParams);
        titleName.setText("折扣清单");
        titleName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        titleName.setTextColor(Color.WHITE);
        titleName.setGravity(Gravity.CENTER);

        closeBtn = new ImageView(context);
        RelativeLayout.LayoutParams closeBtnParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        closeBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        closeBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        closeBtnParams.rightMargin = dp10;
        closeBtn.setLayoutParams(closeBtnParams);

        closeBtn.setImageDrawable(CommonHelper.getAssertDrawable(context, R.drawable.close));

        titleDlg.addView(titleName);
        titleDlg.addView(closeBtn);

        LinearLayout contentView = new LinearLayout(context);
        LinearLayout.LayoutParams contentViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        contentView.setLayoutParams(contentViewParams);
        contentView.setBackgroundColor(Color.WHITE);
        contentView.setGravity(Gravity.CENTER);
        contentView.setOrientation(LinearLayout.VERTICAL);


        LinearLayout table = new LinearLayout(context);
        LinearLayout.LayoutParams tableParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tableParams.leftMargin = dp30;
        tableParams.rightMargin = dp30;
        tableParams.topMargin = dp20;
        tableParams.bottomMargin = dp20;
        table.setLayoutParams(tableParams);
        table.setBackgroundColor(Color.parseColor("#DADADA"));
        table.setGravity(Gravity.CENTER);
        table.setOrientation(LinearLayout.VERTICAL);
        table.setPadding(1, 1, 1, 1);

        LinearLayout tableLL1 = new LinearLayout(context);
        LinearLayout.LayoutParams tableLL1Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp60);
        tableLL1.setLayoutParams(tableLL1Params);
        tableLL1.setOrientation(LinearLayout.HORIZONTAL);
        tableLL1.setGravity(Gravity.CENTER_VERTICAL);

        TextView table11 = new TextView(context);
        LinearLayout.LayoutParams table11Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table11Params.weight = 1;
        table11Params.rightMargin = 1;
        table11Params.bottomMargin = 1;
        table11.setLayoutParams(table11Params);
        table11.setSingleLine();
        table11.setText("档位");
        table11.setTextColor(Color.BLACK);
        table11.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table11.setGravity(Gravity.CENTER);
        table11.setBackgroundColor(Color.WHITE);

        TextView table12 = new TextView(context);
        LinearLayout.LayoutParams table12Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table12Params.weight = 2;
        table12Params.rightMargin = 1;
        table12Params.bottomMargin = 1;
        table12.setLayoutParams(table12Params);
        table12.setSingleLine();
        table12.setText("授权");
        table12.setTextColor(Color.BLACK);
        table12.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table12.setGravity(Gravity.CENTER);
        table12.setBackgroundColor(Color.WHITE);

        TextView table13 = new TextView(context);
        LinearLayout.LayoutParams table13Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table13Params.weight = 1;
        table13Params.bottomMargin = 1;
        table13.setLayoutParams(table13Params);
        table13.setSingleLine();
        table13.setText("折扣");
        table13.setTextColor(Color.BLACK);
        table13.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table13.setGravity(Gravity.CENTER);
        table13.setBackgroundColor(Color.WHITE);

        tableLL1.addView(table11);
        tableLL1.addView(table12);
        tableLL1.addView(table13);

        LinearLayout tableLL2 = new LinearLayout(context);
        LinearLayout.LayoutParams tableLL2Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp48);
        tableLL2.setLayoutParams(tableLL2Params);
        tableLL2.setOrientation(LinearLayout.HORIZONTAL);
        tableLL2.setGravity(Gravity.CENTER_VERTICAL);

        TextView table21 = new TextView(context);
        LinearLayout.LayoutParams table21Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table21Params.weight = 1;
        table21Params.rightMargin = 1;
        table21Params.bottomMargin = 1;
        table21.setLayoutParams(table21Params);
        table21.setSingleLine();
        table21.setText("A");
        table21.setTextColor(Color.parseColor("#808080"));
        table21.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table21.setGravity(Gravity.CENTER);
        table21.setBackgroundColor(Color.parseColor("#F9F9F9"));

        TextView table22 = new TextView(context);
        LinearLayout.LayoutParams table22Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table22Params.weight = 2;
        table22Params.rightMargin = 1;
        table22Params.bottomMargin = 1;
        table22.setLayoutParams(table22Params);
        table22.setSingleLine();
        table22.setText(Html.fromHtml("<font color=red>1-99</font>授权"));
        table22.setTextColor(Color.parseColor("#808080"));
        table22.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table22.setGravity(Gravity.CENTER);
        table22.setBackgroundColor(Color.parseColor("#F9F9F9"));

        TextView table23 = new TextView(context);
        LinearLayout.LayoutParams table23Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table23Params.weight = 1;
        table23Params.bottomMargin = 1;
        table23.setLayoutParams(table23Params);
        table23.setSingleLine();
        table23.setText(Html.fromHtml("赠送<font color=red>0</font>天"));
        table23.setTextColor(Color.parseColor("#808080"));
        table23.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table23.setGravity(Gravity.CENTER);
        table23.setBackgroundColor(Color.parseColor("#F9F9F9"));

        tableLL2.addView(table21);
        tableLL2.addView(table22);
        tableLL2.addView(table23);


        LinearLayout tableLL3 = new LinearLayout(context);
        LinearLayout.LayoutParams tableLL3Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp48);
        tableLL3.setLayoutParams(tableLL3Params);
        tableLL3.setOrientation(LinearLayout.HORIZONTAL);
        tableLL3.setGravity(Gravity.CENTER_VERTICAL);

        TextView table31 = new TextView(context);
        LinearLayout.LayoutParams table31Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table31Params.weight = 1;
        table31Params.rightMargin = 1;
        table31Params.bottomMargin = 1;
        table31.setLayoutParams(table31Params);
        table31.setSingleLine();
        table31.setText("B");
        table31.setTextColor(Color.parseColor("#808080"));
        table31.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table31.setGravity(Gravity.CENTER);
        table31.setBackgroundColor(Color.WHITE);

        TextView table32 = new TextView(context);
        LinearLayout.LayoutParams table32Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table32Params.weight = 2;
        table32Params.rightMargin = 1;
        table32Params.bottomMargin = 1;
        table32.setLayoutParams(table32Params);
        table32.setSingleLine();
        table32.setText(Html.fromHtml("<font color=red>100-499</font>授权"));
        table32.setTextColor(Color.parseColor("#808080"));
        table32.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table32.setGravity(Gravity.CENTER);
        table32.setBackgroundColor(Color.WHITE);

        TextView table33 = new TextView(context);
        LinearLayout.LayoutParams table33Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table33Params.weight = 1;
        table33Params.bottomMargin = 1;
        table33.setLayoutParams(table33Params);
        table33.setSingleLine();
        table33.setText(Html.fromHtml("赠送<font color=red>10</font>天"));
        table33.setTextColor(Color.parseColor("#808080"));
        table33.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table33.setGravity(Gravity.CENTER);
        table33.setBackgroundColor(Color.WHITE);

        tableLL3.addView(table31);
        tableLL3.addView(table32);
        tableLL3.addView(table33);

        LinearLayout tableLL4 = new LinearLayout(context);
        LinearLayout.LayoutParams tableLL4Params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                dp48);
        tableLL4.setLayoutParams(tableLL4Params);
        tableLL4.setOrientation(LinearLayout.HORIZONTAL);
        tableLL4.setGravity(Gravity.CENTER_VERTICAL);

        TextView table41 = new TextView(context);
        LinearLayout.LayoutParams table41Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table41Params.weight = 1;
        table41Params.rightMargin = 1;
        table41.setLayoutParams(table41Params);
        table41.setSingleLine();
        table41.setText("C");
        table41.setTextColor(Color.parseColor("#808080"));
        table41.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table41.setGravity(Gravity.CENTER);
        table41.setBackgroundColor(Color.parseColor("#F9F9F9"));

        TextView table42 = new TextView(context);
        LinearLayout.LayoutParams table42Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table42Params.weight = 2;
        table42Params.rightMargin = 1;
        table42.setLayoutParams(table42Params);
        table42.setSingleLine();
        table42.setText(Html.fromHtml("<font color=red>500</font>及以上授权"));
        table42.setTextColor(Color.parseColor("#808080"));
        table42.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table42.setGravity(Gravity.CENTER);
        table42.setBackgroundColor(Color.parseColor("#F9F9F9"));

        TextView table43 = new TextView(context);
        LinearLayout.LayoutParams table43Params = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.MATCH_PARENT);
        table43Params.weight = 1;
        table43.setLayoutParams(table43Params);
        table43.setSingleLine();
        table43.setText(Html.fromHtml("赠送<font color=red>20</font>天"));
        table43.setTextColor(Color.parseColor("#808080"));
        table43.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        table43.setGravity(Gravity.CENTER);
        table43.setBackgroundColor(Color.parseColor("#F9F9F9"));

        tableLL4.addView(table41);
        tableLL4.addView(table42);
        tableLL4.addView(table43);

        table.addView(tableLL1);
        table.addView(tableLL2);
        table.addView(tableLL3);
        table.addView(tableLL4);

        contentView.addView(table);

        viewRoot.addView(bgZone);
        viewRoot.addView(titleDlg);
        viewRoot.addView(contentView);
		addView(viewRoot);
	}
}
