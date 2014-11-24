//package com.bldj.lexiang.ui;
//
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.PrintStream;
//
//import android.annotation.SuppressLint;
//import android.os.Bundle;
//import android.view.View;
//import android.view.Window;
//import android.webkit.JavascriptInterface;
//import android.webkit.JsResult;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//
//import com.bldj.lexiang.GlobalConfig;
//import com.bldj.lexiang.utils.R;
//
///**
// * web app页面
// * 
// * @author zhuyb
// * @email zhuyongb0@live.com
// */
//public class WebAppActivity extends BaseActivity {
//	private WebView mWebAppWebView;
//	private String url;
//	private long appId;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		url = getIntent().getStringExtra("url");
//		appId = getIntent().getLongExtra("id", 0L);
//	}
//
//	@Override
//	protected void onPause() {
//		// TODO Auto-generated method stub
//		super.onPause();
//	}
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//	}
//
//	@SuppressWarnings("deprecation")
//	@SuppressLint({ "SetJavaScriptEnabled", "ResourceAsColor" })
//	@Override
//	public void initView() {
//		LinearLayout rootView = new LinearLayout(WebAppActivity.this);
//		rootView.setBackgroundColor(R.color.default_bg);
//		FrameLayout.LayoutParams rootViewParams = new FrameLayout.LayoutParams(
//				FrameLayout.LayoutParams.MATCH_PARENT,
//				FrameLayout.LayoutParams.MATCH_PARENT);
//		rootView.setLayoutParams(rootViewParams);
//		rootView.setOrientation(LinearLayout.VERTICAL);
//
//		mWebAppWebView = new WebView(WebAppActivity.this);
//		LinearLayout.LayoutParams webViewParams = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.MATCH_PARENT,
//				LinearLayout.LayoutParams.MATCH_PARENT);
//		mWebAppWebView.setLayoutParams(webViewParams);
//
//		rootView.addView(mWebAppWebView);
//		setContentView(rootView);
//
//		mWebAppWebView.setHapticFeedbackEnabled(false);
//		mWebAppWebView.setHorizontalScrollBarEnabled(false);
//		mWebAppWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
//		mWebAppWebView.setVerticalFadingEdgeEnabled(false);
//		mWebAppWebView.setVerticalScrollBarEnabled(true);
//		mWebAppWebView.setVerticalScrollbarOverlay(true);
//		mWebAppWebView.requestFocus();
//		WebSettings webSettings = mWebAppWebView.getSettings();
//		webSettings.setJavaScriptEnabled(true);
//		webSettings.setSupportZoom(true);
//		// webSettings.setLoadWithOverviewMode(true);
//		// webSettings.setUseWideViewPort(true);
//		webSettings.setLightTouchEnabled(true);
//		webSettings.setBlockNetworkImage(false);
//		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//		webSettings.setSavePassword(false);
//		webSettings.setSaveFormData(false);
//		mWebAppWebView.addJavascriptInterface(new Object(){
//			public void finishPage() {
//				WebAppActivity.this.finish();
//			}
//			
//			/**
//			 * 读取最近联系人
//			 * @param id 应用id
//			 * @return 最近联系人内容
//			 */
//			public String readFile(String id) {
//				
//				try {
//					FileInputStream fis = openFileInput(String.valueOf(id));
//					byte[] buff = new byte[1024];
//					int hasRead = 0;
//					StringBuilder sb = new StringBuilder("");
//					while ((hasRead = fis.read(buff)) > 0) {
//						sb.append(new String(buff, 0, hasRead));
//					}
//					return sb.toString();
//				} catch (Exception e) {
//				}
//				return "";
//			}						
//
//			/**
//			 * 保存最近联系人。文件是覆盖而不是追加
//			 * @param id 应用id
//			 * @param content 
//			 */
//			@JavascriptInterface
//			public void saveFile(String id,String content) {
//				try {
//					FileOutputStream fos = openFileOutput(String.valueOf(id), MODE_PRIVATE);
//					PrintStream ps = new PrintStream(fos);
//					ps.println(content);
//					ps.close();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}, "AppStore");
//
//		webSettings.setLoadsImagesAutomatically(true);
//		//mWebAppWebView.loadUrl(getIntent().getStringExtra("url"));
//		mWebAppWebView.loadUrl(url+"?appId="+appId+"&enterId="+GlobalConfig.ENTER_ID);
//		mWebAppWebView.setWebViewClient(new WebAppWebViewClient());
//		mWebAppWebView.setWebChromeClient(new WebAppWebChromeClient());
//	}
//
//	
//
//	private class WebAppWebViewClient extends WebViewClient {
//
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			// TODO Auto-generated method stub
//
//			view.loadUrl(url);
//			return true;
//		}
//
//	}
//
//	/**
//	 * 继承WebChromeClient类 对js弹出框时间进行处理
//	 * 
//	 */
//	final class WebAppWebChromeClient extends WebChromeClient {
//		/**
//		 * 处理alert弹出框
//		 */
//		@Override
//		public boolean onJsAlert(WebView view, String url, String message,
//				JsResult result) {
//			//ToastUtils.showToast(getApplicationContext(), message);
//			return false;
//		}
//	}
//
//	
//
//	@Override
//	public void initListener() {
//		// TODO Auto-generated method stub
//		
//	}
//
//	
//
//}
