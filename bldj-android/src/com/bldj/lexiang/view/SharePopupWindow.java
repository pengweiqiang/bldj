package com.bldj.lexiang.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bldj.lexiang.R;

public class SharePopupWindow extends PopupWindow {
	private View view;
	private GridView gridView;
	private TextView cancel;
	private int[] logo = { R.drawable.weixin_friend,R.drawable.share_weixin,
			R.drawable.share_sina, R.drawable.tencent_weibo};
	private String[] name = {"微信好友", "朋友圈","新浪微博", "腾讯微博"};
	private List<Map<String, Object>> contents;

	public SharePopupWindow(Context context,
			OnItemClickListener listener) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		contents = new ArrayList<Map<String, Object>>();
		view = inflater.inflate(R.layout.popup_window_view, null);
		gridView = (GridView) view.findViewById(R.id.share_gridview);
		cancel = (TextView) view.findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		this.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.popup_anim);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(-00000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		view.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = view.findViewById(R.id.popup_window_view_layout)
						.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

		for (int i = 0; i < name.length; i++) {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("logo", logo[i]);
			map.put("name", name[i]);
			contents.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(context, contents,
				R.layout.popup_window_view_item,
				new String[] { "logo", "name" }, new int[] { R.id.share_iv,
						R.id.share_txt, });
		gridView.setNumColumns(3);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(listener);
	}
}
