package com.bldj.lexiang.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class SelectDialog extends Dialog {
	public SelectDialog(Context context, int theme) {
		super(context, theme);
	}

	public SelectDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.slt_cnt_type);
	}
}
