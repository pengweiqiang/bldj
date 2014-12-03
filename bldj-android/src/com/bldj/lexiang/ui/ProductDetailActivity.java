package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.universalimageloader.core.ImageLoader;

/**
 * 商品详细页
 * 
 * @author will
 * 
 */
public class ProductDetailActivity extends BaseActivity {

	ActionBar mActionBar;
	Product product;
		
	private ImageView imageLogo;
	private Button btn_buy;
	private Button btn_share;
	private TextView tv_product_title;
	private TextView tv_product_info;
	private TextView tv_product_info_word;
	private TextView tv_price_platform;
	private TextView tv_price_normal;
	private EditText et_buy_count;
	private TextView tv_custom_service;
	private TextView tv_add_cart;
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.product_detail);
		super.onCreate(savedInstanceState);
		product = (Product)this.getIntent().getSerializableExtra("product");
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		intData();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("商品详情页");
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.setRightTextActionButton("购物车", new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});
	}

	@Override
	public void initView() {
		imageLogo = (ImageView) findViewById(R.id.logo);
		btn_buy = (Button) findViewById(R.id.buy);
		btn_share = (Button)findViewById(R.id.share);
		tv_product_title = (TextView) findViewById(R.id.product_title);
		tv_product_info = (TextView) findViewById(R.id.product_info);
		tv_product_info_word = (TextView) findViewById(R.id.product_info_word);
		tv_price_platform = (TextView) findViewById(R.id.price_platform);
		tv_price_normal = (TextView)findViewById(R.id.price_normal);
		et_buy_count = (EditText) findViewById(R.id.count);
		tv_custom_service = (TextView)findViewById(R.id.custom_service);
		tv_add_cart = (TextView)findViewById(R.id.add_cart);
		webView = (WebView)findViewById(R.id.webView_product);

	}

	private void intData() {
		ImageLoader.getInstance().displayImage(
				product.getPicurl(),
				imageLogo,
				MyApplication.getInstance()
						.getOptions(R.drawable.default_image));
		tv_product_title.setText(product.getName());
		tv_product_info.setText(product.getName());
		tv_product_info_word.setText(product.getName());
		tv_price_normal.setText(String.valueOf(product.getMarketPrice()));
		tv_price_platform.setText(String.valueOf(product.getCurPrice()));
		
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

		webView.loadUrl(product.getProDetailUrl());

		WebChromeClient webChromeClient = new WebChromeClient() {

			@Override
			public void onReceivedTitle(WebView view, String str) {
				super.onReceivedTitle(view, str);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				/*if (newProgress == 100) {
	                progressBar.setVisibility(View.GONE);
	            } else {
	                if (progressBar.getVisibility() == View.GONE)
	                	progressBar.setVisibility(View.VISIBLE);
	                progressBar.setProgress(newProgress);
	            }*/
			}
			
			
		};
		webView.setWebChromeClient(webChromeClient);
	}

	@Override
	public void initListener() {
		//购买
		btn_buy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		//分享
		btn_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		//联系客服
		tv_custom_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		//加入购物车
		tv_add_cart.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
				
				}
		});
		
		
	}

}
