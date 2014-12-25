package com.bldj.lexiang.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.db.DatabaseUtil;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ShareUtil;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * 养生产品详细页
 * 
 * @author will
 * 
 */
public class HealthProductDetailActivity extends BaseActivity {

	ActionBar mActionBar;
	private Product product;
	private Seller seller;
	private ImageView product_img;
	private TextView tv_time;// 时长
	private TextView tv_price;// 当前价
	private TextView tv_buy_count;// 购买次数
	private TextView tv_shop_price;// 市场价
	private TextView btn_shared;// 分享
	private TextView btn_invite;// 邀请
	private CheckBox tv_fav;// 收藏
	private TextView tv_custom_service;// 联系客服
	private WebView webView;
	private Button btn_appointment_product;
	boolean isFav;// 是否收藏

	ShareUtil shareUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.health_product_detail);
		super.onCreate(savedInstanceState);
		product = (Product) this.getIntent().getSerializableExtra("product");
		seller = (Seller) this.getIntent().getSerializableExtra("seller");
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		shareUtil = new ShareUtil(this);
		shareUtil.initWX();

	}
	

	@Override
	protected void onResume() {
		super.onResume();
		getProduct();
	}


	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setLeftActionButton(R.drawable.btn_back,
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
		product_img = (ImageView) findViewById(R.id.product_img);
		tv_time = (TextView) findViewById(R.id.time);
		tv_price = (TextView) findViewById(R.id.price);
		btn_shared = (TextView) findViewById(R.id.share);
		tv_buy_count = (TextView) findViewById(R.id.buy_count);
		tv_shop_price = (TextView) findViewById(R.id.price_shop);
		btn_invite = (TextView) findViewById(R.id.invite);
		webView = (WebView) findViewById(R.id.webView_product_info);
		btn_appointment_product = (Button) findViewById(R.id.appointment_product);
		tv_custom_service = (TextView) findViewById(R.id.custom_service);
		tv_fav = (CheckBox) findViewById(R.id.collect);

	}

	private void initData() {
		mActionBar.setTitle(product.getName());
		// 获取此产品是否收藏过
		if (isFav) {
			tv_fav.setChecked(true);
			tv_fav.setText("已收藏");
		}

		ImageLoader.getInstance().displayImage(product.getPicurl(),
				product_img,
				MyApplication.getInstance().getOptions(R.drawable.default_image));
		tv_time.setText("时长：" + product.getTimeConsume() + "分钟");
		tv_price.setText("￥" + String.valueOf(product.getCurPrice()));
		
		tv_shop_price.setText("门店价：￥"
				+ String.valueOf(product.getMarketPrice()));
		
		String buyCount = product.getSellerNum()+"人已经购买";
		SpannableStringBuilder style=new SpannableStringBuilder(buyCount);
		style.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_title_color)),0,buyCount.indexOf("人")+1,Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
		tv_buy_count.setText(style);

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
//		webSettings.setBuiltInZoomControls(true);
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
				/*
				 * if (newProgress == 100) {
				 * progressBar.setVisibility(View.GONE); } else { if
				 * (progressBar.getVisibility() == View.GONE)
				 * progressBar.setVisibility(View.VISIBLE);
				 * progressBar.setProgress(newProgress); }
				 */
			}

		};
		webView.setWebChromeClient(webChromeClient);

	}

	/**
	 * 获取产品明细
	 */
	private void getProduct(){
		new Thread(){

			@Override
			public void run() {
				super.run();
				
				isFav = DatabaseUtil.getInstance(mContext).checkFavProduct(
						product.getId());
				if(product.getCurPrice()==0 && product.getMarketPrice()==0){
					Product productCache = DatabaseUtil.getInstance(mContext).getProductById(product.getId());
					Message msg = new Message();
					msg.what = 1;
					msg.obj = productCache;
					handler.sendMessage(msg);
				}else{
					handler.sendEmptyMessage(0);
				}
				
			}
			
		}.start();
		

	}
	/**
	 * 从接口获取产品明细
	 */
	private void getProductByIdHttp(){
		ApiProductUtils.getProductById(mContext, product.getId(), new HttpConnectionUtil.RequestCallback() {
			
			@Override
			public void execute(ParseModel parseModel) {
				if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
						.getStatus())) {
					ToastUtils.showToast(mContext, parseModel.getMsg());
					return;

				} else {
					product = JsonUtils.fromJson(
							parseModel.getData().toString(),
							Product.class);
					initData();
					
				}
			}
		});
	}
	
	@Override
	public void initListener() {
		// 预约产品
		btn_appointment_product.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(MyApplication.getInstance().getCurrentUser()==null){
					Intent intent = new Intent(HealthProductDetailActivity.this,
							RegisterAndLoginActivity.class);
					startActivity(intent);
					return;
				}
				Intent intent = new Intent(HealthProductDetailActivity.this,
						AppointmentDoor1Activity.class);
				if(seller!=null){
					intent.putExtra("seller", seller);
				}
				intent.putExtra("product", product);
				startActivity(intent);
			}
		});
		// 分享
		btn_shared.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ToastUtils.showToast(mContext, "分享微信...");
				shareUtil.sendWebPageToWX("健康送到家，方便你我他",
						SendMessageToWX.Req.WXSceneTimeline,product.getProDetailUrl());

			}
		});
		// 邀请
		btn_invite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
				sendIntent.setData(Uri.parse("smsto:"));
				sendIntent.putExtra("sms_body", "健康送到家，方便你我他");
				mContext.startActivity(sendIntent);

			}
		});
		// 联系客服
		tv_custom_service.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//用intent启动拨打电话  
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+getResources().getString(R.string.appointment_door_tips2)));  
                startActivity(intent);  
			}
		});
		// 收藏
		tv_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isCheck) {
				if(isCheck){
					tv_fav.setText("已收藏");
					long row = DatabaseUtil.getInstance(mContext)
							.insertProduct(product,0);
					if (row > 0) {
						isFav = true;
					}
				}else{
					tv_fav.setText("收藏");
					int row = DatabaseUtil.getInstance(mContext)
							.deleteFavProduct(product.getId(),0);
					if (row > 0) {
						isFav = false;
					}
				}
//				if (!isFav) {
//					long row = DatabaseUtil.getInstance(mContext)
//							.insertProduct(product,0);
//					if (row > 0) {
//						isFav = true;
////						ToastUtils.showToast(mContext, "收藏成功");
////						tv_fav.setText("已收藏");
//					} else {
////						ToastUtils.showToast(mContext, "该产品已经收藏");
//					}
//				} else {
//					int row = DatabaseUtil.getInstance(mContext)
//							.deleteFavProduct(product.getId(),0);
//					if (row > 0) {
//						isFav = false;
////						ToastUtils.showToast(mContext, "取消收藏");
////						tv_fav.setText("收藏");
//					} else {
//						ToastUtils.showToast(mContext, "取消收藏失败，稍后请重试");
//					}
//				}
			}
		});
	}
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 1){
				Product productCache = (Product) msg.obj;
				if(productCache == null){//缓存里面没有明细，从接口获取
					getProductByIdHttp();
				}else{
					product = productCache;
					initData();
				}
			}else{
				initData();
			}
			super.handleMessage(msg);
		}
		
	};

}
