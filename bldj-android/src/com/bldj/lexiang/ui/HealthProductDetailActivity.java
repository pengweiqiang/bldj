package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.db.DatabaseUtil;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ShareUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.ElasticScrollView;
import com.bldj.lexiang.view.ElasticScrollView.OnRefreshListener;
import com.bldj.lexiang.view.SharePopupWindow;
import com.bldj.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * 养生产品详细页
 * 
 * @author will
 * 
 */
public class HealthProductDetailActivity extends BaseActivity implements OnRefreshListener{

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
	private View lineView,lineView2;
	private WebView webView;
	private ProgressBar progressBar;
	private Button btn_appointment_product;
	boolean isFav;// 是否收藏

	ShareUtil shareUtil;
	SharePopupWindow pop ;
	
	private RadioGroup rg_title,rg_title2;
	private RadioButton rb_single,rb_two,rb_three;
	private List<String> packagePrice;
	
	private double curPrice ;
	private double marketPrice;
	private String title;
	private ElasticScrollView elasticScrollView;
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
		
		getProduct();
	}
	

	@Override
	protected void onResume() {
		super.onResume();
//		getProduct();
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

	LinearLayout mBuyLayout,mTopBuyLayout;
	@Override
	public void initView() {
	   elasticScrollView = (ElasticScrollView) findViewById(R.id.scrollView);
       View view=LayoutInflater.from(this).inflate(R.layout.health_product_detail_main, null);
       mBuyLayout = (LinearLayout) view.findViewById(R.id.buy);
	   mTopBuyLayout = (LinearLayout)view.findViewById(R.id.top_buy_layout);
       
       elasticScrollView.addChild(view);
       
     //当布局的状态或者控件的可见性发生改变回调的接口
       findViewById(R.id.parent_layout).getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
 			
 			@Override
 			public void onGlobalLayout() {
 				//这一步很重要，使得上面的购买布局和下面的购买布局重合
 				onScroll(elasticScrollView.getScrollY());
 				
 				System.out.println(elasticScrollView.getScrollY());
 			}
 		});	
       	product_img = (ImageView) view.findViewById(R.id.product_img);
       	lineView = (View)findViewById(R.id.line2);
       	lineView2 = (View)view.findViewById(R.id.line);
		tv_time = (TextView) view.findViewById(R.id.time);
		tv_price = (TextView) view.findViewById(R.id.price);
		btn_shared = (TextView) view.findViewById(R.id.share);
		tv_buy_count = (TextView) view.findViewById(R.id.buy_count);
		tv_shop_price = (TextView)view.findViewById(R.id.price_shop);
		btn_invite = (TextView) view.findViewById(R.id.invite);
		
		webView = (WebView) view.findViewById(R.id.webView_product_info);
		progressBar = (ProgressBar)view.findViewById(R.id.web_progress);
		btn_appointment_product = (Button) findViewById(R.id.appointment_product);
		tv_custom_service = (TextView) findViewById(R.id.custom_service);
		tv_fav = (CheckBox) findViewById(R.id.collect);
		rg_title = (RadioGroup) view.findViewById(R.id.rg_title);
		rg_title2 = (RadioGroup)view.findViewById(R.id.rg_title2);
		rb_single = (RadioButton) view.findViewById(R.id.radio_single);
		rb_two = (RadioButton)view.findViewById(R.id.radio_two);
		rb_three = (RadioButton)findViewById(R.id.radio_three);
		
		
		elasticScrollView.setonRefreshListener(this);
	}

	private void initData() {
		title = product.getName();
		// 获取此产品是否收藏过
		if (isFav) {
			tv_fav.setChecked(true);
			tv_fav.setText("已收藏");
		}
		if(!StringUtils.isEmpty(product.getExtPrice())){
			try{
				packagePrice = new ArrayList<String>();
				packagePrice.add("一人独享@1@"+product.getCurPrice());
				String packagePriceHttp [] = product.getExtPrice().split("[|]");//二人套餐@2@236||三人套餐@3#333
				for (int i = 0; i < packagePriceHttp.length; i++) {
					if(i==0){
						rb_two.setText(packagePriceHttp[i].split("@")[0]);
					}else if(i== 1){
						rb_three.setText(packagePriceHttp[i].split("@")[0]);
					}
					packagePrice.add(packagePriceHttp[i]);
				}
				if(packagePrice.size() == 2){
					rb_three.setVisibility(View.GONE);
				}
				mActionBar.setTitle(title+"一人独享");
			}catch(Exception e){
				packagePrice = null;
			}
		}else{
			mActionBar.setTitle(title);
			lineView.setVisibility(View.GONE);
			lineView2.setVisibility(View.GONE);
			rg_title2.setVisibility(View.GONE);
			rg_title.setVisibility(View.GONE);
		}
		LayoutParams params1 = product_img.getLayoutParams();
		params1.width = DeviceInfo.getDisplayMetricsWidth((Activity)mContext);
		params1.height = (int) (params1.width * 1.0 / 4 * 2.5);
		product_img.setLayoutParams(params1);
		ImageLoader.getInstance().displayImage(product.getPicurl(),
				product_img,
				MyApplication.getInstance().getOptions(R.drawable.default_image));
		tv_time.setText("时长：" + product.getTimeConsume() + "分钟");
		curPrice = product.getCurPrice();
		tv_price.setText("￥" + String.valueOf(product.getCurPrice()));
		
		marketPrice = product.getMarketPrice();
		tv_shop_price.setText("市场价：￥"
				+ String.valueOf(product.getMarketPrice()));
		tv_shop_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
		
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

		
		webView.setInitialScale(70);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setDisplayZoomControls(false); //隐藏webview缩放按钮
		webSettings.setSupportZoom(false);
		//解决ScrollView嵌套webView下面很多空白问题
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		//将webView的横向竖向的scrollBar都禁用掉，将不再与ScrollView冲突
		/*webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		webView.setVerticalScrollBarEnabled(false);
		webView.setVerticalScrollbarOverlay(false);
		webView.setHorizontalScrollBarEnabled(false);
		webView.setHorizontalScrollbarOverlay(false);*/
		//解决ScrollView嵌套webView下面很多空白问题
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
	
	private void showPackagePrice(int count){
		
		String packageItem[] = packagePrice.get(count).split("@");//三人套餐@3@333
		String name = packageItem[0];
		int num = Integer.valueOf(packageItem[1]);
		curPrice = Double.valueOf(packageItem[2]);
		
//		curPrice = product.getCurPrice()*num;
		marketPrice = product.getMarketPrice()*num;
		tv_price.setText("￥" + String.valueOf(curPrice));
		title = product.getName() + name;
		mActionBar.setTitle(title);
		
		tv_shop_price.setText("市场价：￥"
				+ String.valueOf(marketPrice));
		
//		product.setCurPrice(curPrice);
//		product.setMarketPrice(marketPrice);
	}
	@Override
	public void initListener() {
		rg_title.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int id) {
				switch (id) {
				case R.id.radio_single://个人套餐
					showPackagePrice(0);
					break;
				case R.id.radio_two://第二个套餐
					showPackagePrice(1);
					break;
				case R.id.radio_three://第三个套餐
					showPackagePrice(2);
					break;

				default:
					break;
				}
			}
			
			
		});
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
				product.setCurPrice(curPrice);
				product.setMarketPrice(marketPrice);
				product.setName(title);
				intent.putExtra("product", product);
				startActivity(intent);
			}
		});
		// 分享
		btn_shared.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				pop = new SharePopupWindow(HealthProductDetailActivity.this,new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						switch (position) {
						case 0://微信分享
							MyApplication.getInstance().type = 0;
							ToastUtils.showToast(mContext, "分享微信...");
							shareUtil.sendWebPageToWX(MyApplication.getInstance().getConfParams().getShareProTxt(),
									SendMessageToWX.Req.WXSceneSession,product.getProDetailUrl());
							break;
						case 1://微信分享
							MyApplication.getInstance().type = 1;
							ToastUtils.showToast(mContext, "分享微信...");
							shareUtil.sendWebPageToWX(MyApplication.getInstance().getConfParams().getShareProTxt(),
									SendMessageToWX.Req.WXSceneTimeline,product.getProDetailUrl());
							break;
						case 2://新浪
							String shareUrlSina = shareUtil.shareSina(MyApplication.getInstance().getConfParams().getShareProTxt(), product.getProDetailUrl(), product.getPicurl());
							Intent intent = new Intent(HealthProductDetailActivity.this,BannerWebActivity.class);
							intent.putExtra("url", shareUrlSina);
							intent.putExtra("name", "新浪微博分享");
							startActivity(intent);
							break;
						case 3://腾讯
							String shareUrlQQ = shareUtil.shareQQ(MyApplication.getInstance().getConfParams().getShareProTxt(), product.getProDetailUrl(), product.getPicurl());
							Intent intentQQ = new Intent(HealthProductDetailActivity.this,BannerWebActivity.class);
							intentQQ.putExtra("url", shareUrlQQ);
							intentQQ.putExtra("name", "腾讯微博分享");
							startActivity(intentQQ);
							break;

						default:
							break;
						}
						pop.dismiss();
					}
					
				});  
				//显示窗口  
				pop.showAtLocation(view,Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

			}
		});
		// 邀请
		btn_invite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
				sendIntent.setData(Uri.parse("smsto:"));
				sendIntent.putExtra("sms_body", "健康送到家，方便你我他"+MyApplication.getInstance().getConfParams().getAboutUsUrl());
				mContext.startActivity(sendIntent);

			}
		});
		// 联系客服
		tv_custom_service.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//用intent启动拨打电话  
				String tel = MyApplication.getInstance().getConfParams().getServiceNum();
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tel));  
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
	@Override
	public void onRefresh() {
		
	}


	@Override
	public void onScroll(int scrollY) {
		int mBuyLayout2ParentTop = Math.max(scrollY, mBuyLayout.getTop());
		mTopBuyLayout.layout(0, mBuyLayout2ParentTop, mTopBuyLayout.getWidth(), mBuyLayout2ParentTop + mTopBuyLayout.getHeight());
	}

}
