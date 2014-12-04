package com.bldj.lexiang.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.db.DatabaseUtil;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.universalimageloader.core.ImageLoader;

/**
 * 养生产品详细页
 * 
 * @author will
 * 
 */
public class HealthProductDetailActivity extends BaseActivity {

	ActionBar mActionBar;
	private Product product;
	
	private ImageView product_img;
	private TextView tv_time;//时长
	private TextView tv_price;//当前价
	private TextView tv_buy_count;//购买次数
	private TextView tv_shop_price;//市场价
	private Button btn_shared;//分享
	private Button btn_invite;//邀请
	private TextView tv_fav;//收藏
	private TextView tv_custom_service;//联系客服
	private WebView webView;
	private Button btn_appointment_product;
	boolean isFav;//是否收藏
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.health_product_detail);
		super.onCreate(savedInstanceState);
		product = (Product)this.getIntent().getSerializableExtra("product");
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
		
		initData();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle(product.getName());
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
				new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		actionBar.hideRightActionButton();
	}

	@Override
	public void initView() {
		product_img = (ImageView)findViewById(R.id.product_img);
		tv_time = (TextView)findViewById(R.id.time);
		tv_price = (TextView)findViewById(R.id.price);
		btn_shared = (Button)findViewById(R.id.share);
		tv_buy_count = (TextView)findViewById(R.id.buy_count);
		tv_shop_price = (TextView)findViewById(R.id.price_shop);
		btn_invite = (Button)findViewById(R.id.invite);
		webView = (WebView)findViewById(R.id.webView_product_info);
		btn_appointment_product = (Button)findViewById(R.id.appointment_product);
		tv_custom_service = (TextView)findViewById(R.id.custom_service);
		tv_fav = (TextView)findViewById(R.id.collect);
		
		
	}
	private void initData(){
		
		//获取此美容师是否收藏过
		isFav = DatabaseUtil.getInstance(mContext).checkFavProduct(product.getId());
		if(isFav){
			tv_fav.setText("已收藏");
		}
		
		ImageLoader.getInstance().displayImage(
				product.getPicurl(),
				product_img,
				MyApplication.getInstance().getOptions(
						R.drawable.ic_launcher));
		tv_time.setText("时长："+product.getTimeConsume()+"分钟");
		tv_price.setText("一休价：￥"+String.valueOf(product.getCurPrice()));
		tv_shop_price.setText("门店价：￥"+String.valueOf(product.getMarketPrice()));
		tv_buy_count.setText(product.getSuitsCrowd());//???具体哪个字段
		
		
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
		//预约产品
		btn_appointment_product.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(HealthProductDetailActivity.this,AppointmentDoor1Activity.class);
				intent.putExtra("product", product);
				startActivity(intent);
			}
		});
		//分享
		btn_shared.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		//邀请
		btn_invite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		//联系客服
		tv_custom_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		//收藏
		tv_fav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isFav){
					long row = DatabaseUtil.getInstance(mContext).insertProduct(product);
					if(row>0){
						isFav = true; 
						ToastUtils.showToast(mContext, "收藏成功");
						tv_fav.setText("已收藏");
					}else{
						ToastUtils.showToast(mContext, "该产品已经收藏");
					}
				}else{
					int row = DatabaseUtil.getInstance(mContext).deleteFavProduct(product.getId());
					if(row>0){
						isFav = false; 
						ToastUtils.showToast(mContext, "取消收藏");
						tv_fav.setText("收藏");
					}else{
						ToastUtils.showToast(mContext, "取消收藏失败，稍后请重试");
					}
				}
			}
		});
	}
	
	

}
