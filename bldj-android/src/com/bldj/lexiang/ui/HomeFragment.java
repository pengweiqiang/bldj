package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.BannerPagerAdapter;
import com.bldj.lexiang.adapter.GroupAdapter;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.ApiHomeUtils;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.Ad;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.db.DatabaseUtil;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.MyListView;
import com.bldj.lexiang.view.PageIndicator;
import com.bldj.lexiang.view.XListView.IXListViewListener;
import com.bldj.universalimageloader.core.ImageLoader;

/**
 * 精品推荐
 * 
 * @author will
 * 
 */
@SuppressLint("NewApi")
public class HomeFragment extends BaseFragment implements IXListViewListener {
	private View infoView;
	private ActionBar mActionBar;

	private MyListView mListView;
	private HomeAdapter listAdapter;
	private List<Product> products;
	private ProgressBar progressbar;

	// 广告条 start
	private GestureDetector mGestureDetector;
	View.OnTouchListener mGestureListener;
	private List<Ad> ads;

	FrameLayout bannerView;
	ViewPager bannerViewPager;
	private View mTouchTarget;
	PageIndicator mIndicator;
	LocationBroadcastReciver locationBroadReceiver;
	
	ArrayList<View> bannerListView;
	private BannerPagerAdapter bannerPageAdapter;

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
		progressbar = (ProgressBar) infoView
				.findViewById(R.id.progress_listView);

		mListView = (MyListView) infoView.findViewById(R.id.home_listview);
		// bannerView =
		// (FrameLayout)LayoutInflater.from(mActivity).inflate(R.layout.home_banner,
		// null);
		bannerView = (FrameLayout) infoView.findViewById(R.id.banner);

		bannerViewPager = (ViewPager) bannerView
				.findViewById(R.id.banner_viewpager);

		LayoutParams params1 = bannerViewPager.getLayoutParams();
		params1.width = DeviceInfo.getDisplayMetricsWidth(mActivity);
		params1.height = (int) (params1.width * 1.0 / 484 * 200);

		bannerViewPager.setLayoutParams(params1);
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
		mIndicator = (PageIndicator) bannerView.findViewById(R.id.indicator);
		mIndicator.setViewPager(bannerViewPager);

		// mListView.addHeaderView(bannerView);
		// mListView.bannerView = bannerView;
		products = new ArrayList<Product>();
		listAdapter = new HomeAdapter(mActivity, products);
		mListView.setAdapter(listAdapter);

		mListView.setPullLoadEnable(true);
		mListView.setPullRefreshEnable(true);
		mListView.setXListViewListener(this);
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
		actionBar.setTitle("便利到家");
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
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+getResources().getString(R.string.appointment_door_tips2)));  
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
						Intent intent = new Intent(mActivity, AboutActivity.class);
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
						Intent intent = new Intent(mActivity, AuthentActivity.class);
						startActivity(intent);
					}
				
				}
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

	/*
	 * @Override public boolean onInterceptTouchEvent(MotionEvent ev) {
	 * 
	 * mGestureDetector = new GestureDetector(new YScrollDetector());
	 * 
	 * return super.onInterceptTouchEvent(ev) &&
	 * mGestureDetector.onTouchEvent(ev); }
	 */

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
						infoView.findViewById(R.id.progress_banner)
								.setVisibility(View.GONE);
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

	/**
	 * 获取精品推荐数据
	 */
	private void getHotProduct() {
		if(isFirst){
			isFirst = false;
			//获取缓存数据
			List<Product> productsCache = DatabaseUtil.getInstance(mActivity).queryFavProduct(0, 0, 1);
			
			if(productsCache!=null && !productsCache.isEmpty()){
				progressbar.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				products.addAll(productsCache);
				listAdapter.notifyDataSetChanged();
				mListView.onLoadFinish();
				return;
			}
		}
		
		ApiProductUtils.getProducts(mActivity.getApplicationContext(), "1", 2,
				0, 2, pageNumber, ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressbar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(mActivity, parseModel.getMsg());
							return;

						} else {
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
							mListView.onLoadFinish(pageNumber,products.size(),"加载完毕");
							
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
					Intent intent = new Intent(mActivity,
							BannerWebActivity.class);
					intent.putExtra("url", ad.getActionUrl());
					intent.putExtra("name", ad.getName());
					startActivity(intent);

				}
			});

		}

		bannerPageAdapter = new BannerPagerAdapter(bannerListView);

		bannerViewPager.setAdapter(bannerPageAdapter);
		bannerViewPager.setCurrentItem(0);
		bannerPageAdapter.notifyDataSetChanged();

		mIndicator.setViewPager(bannerViewPager);
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
				if (MyApplication.getInstance().getCurrentUser() == null) {
					Intent intent = new Intent(mActivity,
							RegisterAndLoginActivity.class);
					startActivity(intent);
					return;
				}
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

	}

	@Override
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
	}

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
	}
	

}
