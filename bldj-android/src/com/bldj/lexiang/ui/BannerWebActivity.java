package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

public class BannerWebActivity extends BaseActivity {

	private WebView webView;
	private ActionBar mActionBar;
	private String name;
	private String url;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.pay_web);
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		name = intent.getStringExtra("name");
		onConfigureActionBar(mActionBar);

		webView.setWebViewClient(new WebViewClient() { // 通过webView打开链接，不调用系统浏览器

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				
				return true;
			}
			
			
			
			
		});

		webView.setInitialScale(25);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);

		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);

		webView.loadUrl(url);

		WebChromeClient webChromeClient = new WebChromeClient() {

			@Override
			public void onReceivedTitle(WebView view, String str) {
				super.onReceivedTitle(view, str);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
	                progressBar.setVisibility(View.GONE);
	            } else {
	                if (progressBar.getVisibility() == View.GONE)
	                	progressBar.setVisibility(View.VISIBLE);
	                progressBar.setProgress(newProgress);
	            }
			}
			
			
		};
		webView.setWebChromeClient(webChromeClient);

	}

	@Override
	public void initView() {
		mActionBar = (ActionBar) findViewById(R.id.actionBar);

		webView = (WebView) findViewById(R.id.webview);
		progressBar = (ProgressBar)findViewById(R.id.web_progress);
	}

	@Override
	public void initListener() {

	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle(name);
		actionBar.setLeftActionButton(R.drawable.btn_back, new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		actionBar.hideRightActionButton();
	}
}
