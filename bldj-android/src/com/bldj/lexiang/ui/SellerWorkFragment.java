package com.bldj.lexiang.ui;

//
//                       __
//                      /\ \   _
//    ____    ____   ___\ \ \_/ \           _____    ___     ___
//   / _  \  / __ \ / __ \ \    <     __   /\__  \  / __ \  / __ \
//  /\ \_\ \/\  __//\  __/\ \ \\ \   /\_\  \/_/  / /\ \_\ \/\ \_\ \
//  \ \____ \ \____\ \____\\ \_\\_\  \/_/   /\____\\ \____/\ \____/
//   \/____\ \/____/\/____/ \/_//_/         \/____/ \/___/  \/___/
//     /\____/
//     \/___/
//
//  Powered by BeeFramework
//

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bldj.lexiang.R;
import com.bldj.lexiang.view.ActionBar;

public class SellerWorkFragment extends BaseFragment {

	private WebView webView;
	private String url;
	private ProgressBar progressBar;
	View infoView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.pay_web, container, false);
		
		if(((SellerPersonalActivity)mActivity).getSellerVo()!=null){
			url = ((SellerPersonalActivity)mActivity).getSellerVo().getDetailUrl();
			
		}
		
		
		initView();
		return infoView;
	}


	public void initView() {

		webView = (WebView) infoView.findViewById(R.id.webview);
		progressBar = (ProgressBar)infoView.findViewById(R.id.web_progress);
		infoView.findViewById(R.id.actionBar).setVisibility(View.GONE);
		
		
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
		webSettings.setBuiltInZoomControls(false);
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


}
