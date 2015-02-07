package com.bldj.lexiang.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.BannerPagerAdapter;
import com.bldj.lexiang.adapter.GroupAdapter;
import com.bldj.lexiang.adapter.HomeNewAdapter;
import com.bldj.lexiang.api.ApiHomeUtils;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.Ad;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.db.DatabaseUtil;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.PatternUtils;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.PageIndicator;
import com.bldj.lexiang.view.PullToRefreshView;
import com.bldj.lexiang.view.PullToRefreshView.OnFooterRefreshListener;
import com.bldj.lexiang.view.PullToRefreshView.OnHeaderRefreshListener;
import com.bldj.universalimageloader.core.ImageLoader;

/**
 * 首页
 * 
 * @author will
 * 
 */
@SuppressLint("NewApi")
public class HomeFragment extends BaseFragment implements /*IXListViewListener,*/OnHeaderRefreshListener, OnFooterRefreshListener {
	private PullToRefreshView mPullToRefreshView;
	private View infoView;
	private ActionBar mActionBar;

	private ListView mListView;
	private HomeNewAdapter listAdapter;
	private List<Product> products;
	RelativeLayout rl_loading;//进度条
	ImageView loading_ImageView;//加载动画
	RelativeLayout rl_loadingFail;//加载失败

	// 广告条 start
//	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;
	private List<Ad> ads;

	FrameLayout bannerView;
	ViewPager bannerViewPager;
	private View mTouchTarget;
	PageIndicator mIndicator;
	LocationBroadcastReciver locationBroadReceiver;
	
	ArrayList<View> bannerListView;
	private BannerPagerAdapter bannerPageAdapter;
	private FrameLayout progressBbanner;

	// 广告条 end
	private int pageNumber = 0;
	private boolean isFirst = true;// 首次进入app先从缓存加载产品数据
//	private boolean isFirstLoadAd = true;//首次进入app先从缓存加载广告栏

	private LinearLayout tab_find, tab_company, tab_reserve;

	private View view;
	private ListView lv_group;
	private List<TitleBarEnum> groups;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.home_fragment, container, false);
		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		tab_find = (LinearLayout) infoView.findViewById(R.id.tab_find);
		tab_company = (LinearLayout) infoView.findViewById(R.id.tab_company);
		tab_reserve = (LinearLayout) infoView.findViewById(R.id.tab_reserve);
		rl_loading = (RelativeLayout) infoView.findViewById(R.id.progress_listView);
		rl_loadingFail = (RelativeLayout) infoView.findViewById(R.id.loading_fail);
		loading_ImageView = (ImageView)infoView.findViewById(R.id.loading_imageView);
		mPullToRefreshView = (PullToRefreshView) infoView.findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mListView = (ListView) infoView.findViewById(R.id.home_listview);
		// bannerView =
		// (FrameLayout)LayoutInflater.from(mActivity).inflate(R.layout.home_banner,
		// null);
		bannerView = (FrameLayout) infoView.findViewById(R.id.banner);

		bannerViewPager = (ViewPager) bannerView
				.findViewById(R.id.banner_viewpager);
		progressBbanner = (FrameLayout)infoView.findViewById(R.id.progress_banner);

		LayoutParams params1 = bannerViewPager.getLayoutParams();
		params1.width = DeviceInfo.getDisplayMetricsWidth(mActivity);
		params1.height = (int) (params1.width * 1.0 / 484 * 200);
		bannerViewPager.setLayoutParams(params1);
		progressBbanner.setLayoutParams(params1);
		bannerListView = new ArrayList<View>();

		bannerPageAdapter = new BannerPagerAdapter(bannerListView);

		bannerViewPager.setAdapter(bannerPageAdapter);
		bannerViewPager.setCurrentItem(0);

		bannerViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					private int mPreviousState = ViewPager.SCROLL_STATE_IDLE;

					@Override
					public void onPageSelected(int arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPageScrollStateChanged(int state) {
						if (mPreviousState == ViewPager.SCROLL_STATE_IDLE) {
							if (state == ViewPager.SCROLL_STATE_DRAGGING) {
								mTouchTarget = bannerViewPager;
							}
						} else {
							if (state == ViewPager.SCROLL_STATE_IDLE
									|| state == ViewPager.SCROLL_STATE_SETTLING) {
								mTouchTarget = null;
							}
						}

						mPreviousState = state;
					}
				});
		bannerViewPager.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					startTimer();
				} else {
					stopTimer();
				}
				return false;
			}
		});
		mIndicator = (PageIndicator) bannerView.findViewById(R.id.indicator);
		mIndicator.setViewPager(bannerViewPager);

//		 mListView.addHeaderView(bannerView);
//		 mListView.bannerView = bannerView;
		products = new ArrayList<Product>();
		listAdapter = new HomeNewAdapter(mActivity, products);
		mListView.setAdapter(listAdapter);

//		mListView.setPullLoadEnable(false);
//		mListView.setPullRefreshEnable(false);
//		mListView.setXListViewListener(this);
		return infoView;
	}
	   
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initListener();

		getAdLists();

		getHotProduct();
		
		IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.LOCATION);
        locationBroadReceiver = new LocationBroadcastReciver();
        mActivity.registerReceiver(locationBroadReceiver, intentFilter);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle(getResources().getString(R.string.app_name));
		infoView.findViewById(R.id.actionBarLayout).setBackgroundColor(getResources().getColor(R.color.app_bg_color));
		actionBar.setTitleTextColor(R.color.white);
		actionBar.setLeftHomeCityActionButton(new OnClickListener() {

			@Override
			public void onClick(View view) {
				buildTitleBar(view,0);
			}
		});
		String city = (String) SharePreferenceManager.getSharePreferenceValue(
				mActivity, Constant.FILE_NAME, "city", "");
		actionBar.setCityName(city);
//		actionBar.setRightTextActionButton("更多", new View.OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//				buildTitleBar(view,1);
//			}
//		});
		actionBar.setHomeRightAction(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				//用intent启动拨打电话  
//                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+getResources().getString(R.string.appointment_door_tips2))); 
				String tel = MyApplication.getInstance().getConfParams().getServiceNum();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tel)); 
                startActivity(intent); 
			}
		},
		new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				buildTitleBar(view,1);
			}
		});
	}

	private void buildTitleBar(View parent,final int index) {
		DeviceInfo.setContext(mActivity);
		groups = new ArrayList<TitleBarEnum>();
		if(index == 0 ){//城市
			groups.add(TitleBarEnum.CITY_BEIJING);
			groups.add(TitleBarEnum.CITY_GUANGZHOU);
			groups.add(TitleBarEnum.CITY_SHENZHEN);
		}else{//更多
			groups.add(TitleBarEnum.ABOUT);
			groups.add(TitleBarEnum.FEEDBACK);
			groups.add(TitleBarEnum.SHARE);
			groups.add(TitleBarEnum.ZHAOPIN);
			groups.add(TitleBarEnum.COMPANY);
			groups.add(TitleBarEnum.SELLER);
		}
		final PopupWindow popupWindow;
//		if (popupWindow == null) {
			view = LayoutInflater.from(mActivity).inflate(R.layout.group_list,
					null);
			lv_group = (ListView) view.findViewById(R.id.lvGroup);
			if(index == 0){
				popupWindow = new PopupWindow(view, parent.getWidth()+20, LayoutParams.WRAP_CONTENT);
			}else{
				popupWindow = new PopupWindow(view, DeviceInfo.getScreenWidth() / 2
						- parent.getWidth(), LayoutParams.WRAP_CONTENT);
			}
			
//		}
		GroupAdapter groupAdapter = new GroupAdapter(mActivity, groups);
		lv_group.setAdapter(groupAdapter);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		// popupWindow.showAsDropDown(parent, popupWindow.getWidth(), 0);
		if(index == 0){
			popupWindow.showAsDropDown(parent);
		}else{
			popupWindow.showAsDropDown(parent);
		}
		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				if(index == 0){
					mActionBar.setCityName(groups.get(position).getMsg());
					SharePreferenceManager.saveBatchSharedPreference(mActivity, Constant.LOCATION, "city", groups.get(position).getMsg());
				}else if (index == 1){
					if (position == TitleBarEnum.ABOUT.getIndex()) {
						Intent intent = new Intent(mActivity,
								BannerWebActivity.class);
						intent.putExtra("url", MyApplication.getInstance().getConfParams().getAboutUsUrl());
						intent.putExtra("name", "关于我们");
						startActivity(intent);
					} else if (position == TitleBarEnum.FEEDBACK.getIndex()) {
						Intent intent = new Intent(mActivity,
								FeedBackActivity.class);
						startActivity(intent);
					} else if (position == TitleBarEnum.SHARE.getIndex()) {
						Intent intent = new Intent(mActivity,
								SharedFriendActivity.class);
						startActivity(intent);
					} else if (position == TitleBarEnum.ZHAOPIN.getIndex()) {
						Intent intent = new Intent(mActivity,
								BannerWebActivity.class);
						intent.putExtra("url", MyApplication.getInstance().getConfParams().getRecruitTxt());
						intent.putExtra("name", "推拿师招聘");
						startActivity(intent);
					}else if(position == TitleBarEnum.COMPANY.getIndex()){
						Intent intent = new Intent(mActivity, CompanyZoneActivity.class);
						intent.putExtra("serviceTypeName", "企业专区0元");
						intent.putExtra("price", 0);
						intent.putExtra("type", 1);
						startActivity(intent);
					}else if(position == TitleBarEnum.SELLER.getIndex()){//推拿师入口
						showSellerOrder();
					}
				
				}
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

	
	/* @Override 
	 public boolean onInterceptTouchEvent(MotionEvent ev) {
	 mGestureDetector = new GestureDetector(new YScrollDetector());
	 
	 return super.onInterceptTouchEvent(ev) &&
	mGestureDetector.onTouchEvent(ev); 
	
	 }*/
	 

	class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			if (distanceY != 0 && distanceX != 0) {

			}

			if (null != bannerView) {
				Rect rect = new Rect();
				bannerView.getHitRect(rect);

				if (null != e1) {
					if (rect.contains((int) e1.getX(), (int) e1.getY())) {
						return false;
					}
				}

				if (null != e2) {
					if (rect.contains((int) e2.getX(), (int) e2.getY())) {
						return false;
					}
				}

			}
			// if(Math.abs(distanceY) >= Math.abs(distanceX))
			// {
			// Log.e("listview", "********************** distanceX :" +
			// distanceX + "  distanceY" + distanceY + "\n");
			// return true;
			// }
			// Log.e("listview", "-------------------------- distanceX :" +
			// distanceX + "  distanceY" + distanceY + "\n");
			return true;
		}
	}

	/**
	 * 获取首页广告数据
	 */
	private void getAdLists() {
//		if(isFirstLoadAd){
//			ads = DatabaseUtil.getInstance(mActivity).queryAds();
//			if(ads!=null && !ads.isEmpty()){
//				infoView.findViewById(R.id.progress_banner)
//				.setVisibility(View.GONE);
//				infoView.findViewById(R.id.fl_banner).setVisibility(
//				View.VISIBLE);
//				bannerView.setVisibility(View.VISIBLE);
//				addBannerView();
//				return;
//			}
//		}
		ApiHomeUtils.getAdList(mActivity.getApplicationContext(),
				ApiConstants.LIMIT, 1,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBbanner.setVisibility(View.GONE);
						infoView.findViewById(R.id.fl_banner).setVisibility(
								View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							bannerView.setVisibility(View.VISIBLE);
							ToastUtils.showToast(mActivity, parseModel.getMsg());
							return;
						} else {
							bannerView.setVisibility(View.VISIBLE);
							ads = JsonUtils.fromJson(parseModel.getData()
									.toString(), new TypeToken<List<Ad>>() {
							});
						}

						addBannerView();
					}
				});

	}
	private void showLoading(){
		rl_loadingFail.setVisibility(View.GONE);
		if(pageNumber == 0){
			rl_loading.setVisibility(View.VISIBLE);
			AnimationDrawable animationDrawable = (AnimationDrawable) loading_ImageView.getBackground();
        	animationDrawable.start();
		}else{
			rl_loading.setVisibility(View.GONE);
		}
	}
	/**
	 * 获取精品推荐数据
	 */
	private void getHotProduct() {
		showLoading();
		if(isFirst){
			isFirst = false;
			//获取缓存数据
			List<Product> productsCache = DatabaseUtil.getInstance(mActivity).queryFavProduct(0, 0, 1);
			
			if(productsCache!=null && !productsCache.isEmpty()){
//				progressbar.setVisibility(View.GONE);
				rl_loading.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				products.addAll(productsCache);
				listAdapter.notifyDataSetChanged();
//				mListView.onLoadFinish();
				mPullToRefreshView.onHeaderRefreshComplete();
				return;
			}
		}
		
		ApiProductUtils.getProducts(mActivity.getApplicationContext(), "1", 2,
				0, 2, pageNumber, ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
//						progressbar.setVisibility(View.GONE);
						rl_loading.setVisibility(View.GONE);
						mPullToRefreshView.onHeaderRefreshComplete();
						mPullToRefreshView.onFooterRefreshComplete();
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							rl_loadingFail.setVisibility(View.VISIBLE);
							mListView.setVisibility(View.GONE);
//							ToastUtils.showToast(mActivity, parseModel.getMsg());
//							mListView.onLoadFinish(pageNumber,products.size(),"点击重试");
							return;

						} else {
							mListView.setVisibility(View.VISIBLE);
							final List<Product> productsList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Product>>() {
									});

							if (pageNumber == 0) {
								products.clear();
								if(productsList!=null && !productsList.isEmpty()){
									new Thread(){

										@Override
										public void run() {
											super.run();
											DatabaseUtil.getInstance(mActivity).insertProductsList(productsList,1);
										}
										
									}.start();
								}
							}
							
							products.addAll(productsList);
							listAdapter.notifyDataSetChanged();
//							mPullToRefreshView.onHeaderRefreshComplete();
//							mListView.onLoadFinish(pageNumber,products.size(),"加载完毕");
						}

					}
				});
	}

	public void addBannerView() {
		bannerListView.clear();
		for (int i = 0; i < ads.size(); i++) {
			final Ad ad = ads.get(i);
			ImageView viewOne = (ImageView) LayoutInflater.from(mActivity)
					.inflate(R.layout.b0_index_banner_cell, null);

			ImageLoader.getInstance().displayImage(
					ad.getPicurl(),
					viewOne,
					MyApplication.getInstance().getOptions(
							R.drawable.default_image));
			bannerListView.add(viewOne);

			viewOne.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if("0".equals(ad.getActionType())){// 0则表示跳转的URL是个网页
						Intent intent = new Intent(mActivity,
							BannerWebActivity.class);
						intent.putExtra("url", ad.getActionUrl());
						intent.putExtra("name", ad.getName());
						startActivity(intent);
					}else if("1".equals(ad.getActionType())){//1 则表示跳转的界面 例如注册界面
						if (MyApplication.getInstance().getCurrentUser() == null) {//未登录
							try{
								Intent intent = new Intent(mActivity,
										Class.forName(ad.getActionUrl()));
								startActivity(intent);
							}catch(Exception e){
								ToastUtils.showToast(mActivity, "访问路径出错，"+ad.getActionUrl());
							}
						}else{//已登录
							if("com.bldj.lexiang.ui.RegisterAndLoginActivity".equals(ad.getActionUrl())){
								ToastUtils.showToast(mActivity, "您已经获取到优惠券了");
							}else{
								try{
									Intent intent = new Intent(mActivity,
											Class.forName(ad.getActionUrl()));
									startActivity(intent);
								}catch(Exception e){
									ToastUtils.showToast(mActivity, "访问路径出错，"+ad.getActionUrl());
								}
							}
						}
					}

				}
			});
		}
		if(ads!=null && !ads.isEmpty()){
			startTimer();
			bannerPageAdapter = new BannerPagerAdapter(bannerListView);
	
			bannerViewPager.setAdapter(bannerPageAdapter);
			bannerViewPager.setCurrentItem(0);
			bannerPageAdapter.notifyDataSetChanged();
	
			mIndicator.setViewPager(bannerViewPager);
		}
		// bannerPageAdapter = new BannerPagerAdapter(bannerListView);
		// bannerViewPager.setAdapter(bannerPageAdapter);
		// bannerViewPager.setCurrentItem(0);
		// mIndicator.setViewPager(bannerViewPager);
		// bannerPageAdapter.mListViews = bannerListView;
		// bannerViewPager.setAdapter(bannerPageAdapter);
		// bannerPageAdapter.notifyDataSetChanged();
		// mIndicator.setCurrentItem(0);
	}

	/**
	 * 初始化点击事件
	 */
	private void initListener() {
		//点击重试
		rl_loadingFail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pageNumber = 0;
				getHotProduct();
			}
		});
		// 找理疗师
		tab_find.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(mActivity,
						JLYSFragmentActivity.class);
				intent.putExtra("showIndex", 1);//默认显示找理疗师
				startActivity(intent);

			}
		});
		// 企业专区
		tab_company.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity,
						CompanyZoneSelectPackageActivity.class);
				startActivity(intent);
			}
		});
		// 现在预约
		tab_reserve.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MyApplication.getInstance().getCurrentUser() == null) {
					Intent intent = new Intent(mActivity,
							RegisterAndLoginActivity.class);
					startActivity(intent);
					return;
				}
				Intent intent = new Intent(mActivity,
						AppointmentFragmentActivity.class);
				startActivity(intent);
			}
		});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Product productItem = (Product)listAdapter.getItem(position);
				Intent intent = new Intent();
				intent.setClass(mActivity, HealthProductDetailActivity.class);
				intent.putExtra("product", productItem);
				startActivity(intent);
			}
			
		});
	}

	/*@Override
	public void onRefresh() {
		pageNumber = 0;
		getHotProduct();
	}

	@Override
	public void onLoadMore() {
		if (products == null || products.isEmpty()) {
			pageNumber = 0;
		} else {
			pageNumber++;
		}
		getHotProduct();
	}*/

	/**
	 * 定位通知改变城市广播
	 * @author will
	 *
	 */
	private class LocationBroadcastReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.LOCATION)) {
				String city = intent.getStringExtra("city");
				
				mActionBar.setCityName(city);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mActivity.unregisterReceiver(locationBroadReceiver);
		stopTimer();
	}
	
	/**
	 * 查询推拿师的订单
	 */
	private void showSellerOrder(){
		
		if(!StringUtils.isEmpty(MyApplication.mobile) && !StringUtils.isEmpty(MyApplication.password)){
			Intent intent = new Intent(mActivity,MyOrdersActivity.class);
			intent.putExtra("mobile", MyApplication.mobile);
			intent.putExtra("password", MyApplication.password);
			startActivity(intent);
			return;
		}
		Builder dialog = new AlertDialog.Builder(mActivity);
		LayoutInflater inflater = (LayoutInflater) mActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialogview, null);
		dialog.setView(layout);
		
		try 
		{
		    Field field = dialog.getClass()
		            .getSuperclass().getDeclaredField(
		                     "mShowing" );
		    field.setAccessible(true);
		     //   将mShowing变量设为false，表示对话框已关闭 
		    field.set(dialog, false );
		    dialog.create().dismiss();

		}
		catch (Exception e)
		{

		}
		
		final EditText et_mobile = (EditText) layout.findViewById(R.id.searchC);
		final EditText et_password = (EditText) layout.findViewById(R.id.et_password);
		et_password.setVisibility(View.VISIBLE);
		et_mobile.setHint(R.string.input_phone2);
		et_mobile.setInputType(InputType.TYPE_CLASS_PHONE);
		et_mobile.requestFocus();
		dialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						final String mobile = et_mobile
								.getText().toString();
						boolean isDismiss = false;
						if (StringUtils.isEmpty(mobile)) {
							et_mobile.requestFocus();
							ToastUtils.showToast(mActivity,
									"手机号不能为空");
						}
						final String password = et_password.getText().toString();
						if(StringUtils.isEmpty(password)){
							et_password.requestFocus();
							ToastUtils.showToast(mActivity, "请输入密码");
						}
						if(!PatternUtils.checkPhoneNum(mobile)){
							ToastUtils.showToast(mActivity, "请输入正确的手机号");
							try { 
	                            Field field = dialog.getClass().getSuperclass() 
	                                    .getDeclaredField("mShowing"); 
	                            field.setAccessible(true); 
	                            field.set(dialog, false); 
	                        } catch (Exception e) { 
	                            e.printStackTrace(); 
	                        } 
							return;
						}else{
							isDismiss = true;
							Intent intent = new Intent(mActivity,MyOrdersActivity.class);
							intent.putExtra("mobile", mobile);
							intent.putExtra("password", password);
							startActivity(intent);
						}
						try { 
                            Field field = dialog.getClass().getSuperclass() 
                                    .getDeclaredField("mShowing"); 
                            field.setAccessible(true); 
                            field.set(dialog, isDismiss); 
                        } catch (Exception e) { 
                            e.printStackTrace(); 
                        } 
					}
				});

		dialog.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						try { 
	                        Field field = dialog.getClass().getSuperclass() 
	                                .getDeclaredField("mShowing"); 
	                        field.setAccessible(true); 
	                        field.set(dialog, true); 
	                    } catch (Exception e) { 
	                        e.printStackTrace(); 
	                    } 
					}

				});
		dialog.show();
		
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		if (products == null || products.isEmpty()) {
			pageNumber = 0;
		} else {
			pageNumber++;
		}
		getHotProduct();
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		// TODO Auto-generated method stub
		pageNumber = 0;
		getHotProduct();
		
	}
	Timer timer;
	public void startTimer() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				mActivity.runOnUiThread(new Runnable() {
					public void run() {
						if(!ads.isEmpty()){
							int currentIndex = bannerViewPager.getCurrentItem();
							if(currentIndex<ads.size()-1){
								currentIndex++;
							}else{
								currentIndex = 0;
							}
							
							bannerViewPager.setCurrentItem(currentIndex,true);
						}
					}
				});
			}
		}, 5000, 6000);
	}
	
	public void stopTimer(){
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

}
