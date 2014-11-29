package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.bldj.lexiang.adapter.MallAdapter;
import com.bldj.lexiang.api.ApiHomeUtils;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.Ad;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
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

	ArrayList<View> bannerListView;
	private BannerPagerAdapter bannerPageAdapter;

	// 广告条 end
	private int pageNumber = 0;

	private TextView tab_find, tab_company, tab_reserve;
	
	private PopupWindow popupWindow;
	private View view;
	private ListView lv_group;
	private List<TitleBarEnum> groups; 
	private Context mContext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.home_fragment, container, false);
		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		tab_find = (TextView) infoView.findViewById(R.id.tab_find);
		tab_company = (TextView) infoView.findViewById(R.id.tab_company);
		tab_reserve = (TextView) infoView.findViewById(R.id.tab_reserve);
		progressbar = (ProgressBar) infoView
				.findViewById(R.id.progress_listView);

		mContext = this.getActivity();
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
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("便利到家");
		actionBar.setLeftHomeCityActionButton(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ToastUtils.showToast(mActivity, "切换城市");
			}
		});
		// actionBar.hideLeftActionButton();
		actionBar.setRightTextActionButton("更多", new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				/*Intent intent = new Intent(mActivity,MoreActivity.class);
				startActivity(intent);*/
				buildTitleBar(view);
			}
		});
	}
	private void buildTitleBar(View parent){
		if (popupWindow == null) {  
            view = LayoutInflater.from(mContext).inflate(R.layout.group_list, null);  
            lv_group = (ListView) view.findViewById(R.id.lvGroup);  
            groups = new ArrayList<TitleBarEnum>();  
            groups.add(TitleBarEnum.ABOUT);  
            groups.add(TitleBarEnum.FEEDBACK);  
            groups.add(TitleBarEnum.SHARE);  
            groups.add(TitleBarEnum.ZHAOPIN);  
            GroupAdapter groupAdapter = new GroupAdapter(mContext, groups);  
            lv_group.setAdapter(groupAdapter);  
            popupWindow = new PopupWindow(view, 200, 250);  
        }
        popupWindow.setFocusable(true);  
        popupWindow.setOutsideTouchable(true);  
        popupWindow.setBackgroundDrawable(new BitmapDrawable());  
        WindowManager windowManager = (WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE);  
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2  
                - popupWindow.getWidth() / 2;  
        popupWindow.showAsDropDown(parent, xPos, 0);  
        lv_group.setOnItemClickListener(new OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> adapterView, View view,  
                    int position, long id) {  
                if(position == TitleBarEnum.ABOUT.getIndex()){
                	Intent intent = new Intent(mContext,
    						AboutActivity.class);
    				startActivity(intent);
                }else if(position == TitleBarEnum.FEEDBACK.getIndex()){
                	Intent intent = new Intent(mContext,
    						FeedBackActivity.class);
    				startActivity(intent);
                }else if(position == TitleBarEnum.SHARE.getIndex()){
                	Intent intent = new Intent(mContext,
    						SharedFriendActivity.class);
    				startActivity(intent);
                }else if(position == TitleBarEnum.ZHAOPIN.getIndex()){
                	Intent intent = new Intent(mContext,
    						AuthentActivity.class);
    				startActivity(intent);
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
		ApiHomeUtils.getAdList(mActivity.getApplicationContext(),
				ApiConstants.LIMIT, 1,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						infoView.findViewById(R.id.progress_banner).setVisibility(View.GONE);
						infoView.findViewById(R.id.fl_banner).setVisibility(View.VISIBLE);
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
	 * 获取精品推荐和体检接口
	 */
	private void getHotProduct() {
		ApiProductUtils.getProducts(mActivity.getApplicationContext(), "1", 2,
				0, 2, pageNumber, ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressbar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							 ToastUtils.showToast(mActivity,
							 parseModel.getMsg());
							 return;
							
						} else {
							List<Product> productsList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Product>>() {
									});

							if (pageNumber == 0) {
								products.clear();
							}
							products.addAll(productsList);

							listAdapter.notifyDataSetChanged();
							onLoad();
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

			ImageLoader.getInstance().displayImage(ad.getPicurl(), viewOne,MyApplication.getInstance().getOptions(R.drawable.default_image));
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
//		bannerPageAdapter = new BannerPagerAdapter(bannerListView);
//		bannerViewPager.setAdapter(bannerPageAdapter);
//		bannerViewPager.setCurrentItem(0);
//		mIndicator.setViewPager(bannerViewPager);
//		bannerPageAdapter.mListViews = bannerListView;
//		bannerViewPager.setAdapter(bannerPageAdapter);
//		bannerPageAdapter.notifyDataSetChanged();
//		mIndicator.setCurrentItem(0);
	}

	/**
	 * 初始化点击事件
	 */
	private void initListener() {
		
		tab_find.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*tab_find.setBackground(getResources().getDrawable(
						R.drawable.tab_btn_selected1));
				tab_company.setBackground(mActivity.getResources().getDrawable(
						R.drawable.tab_btn2));
				tab_reserve.setBackground(mActivity.getResources().getDrawable(
						R.drawable.tab_btn3));*/
				
				Intent intent = new Intent(mActivity,JLYSFragmentActivity.class);
				startActivity(intent);
				
			}
		});
		//企业专区
		tab_company.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*tab_company.setBackground(getResources().getDrawable(
						R.drawable.tab_btn_selected2));
				tab_find.setBackground(getResources().getDrawable(
						R.drawable.tab_btn1));
				tab_reserve.setBackground(getResources().getDrawable(
						R.drawable.tab_btn3));*/
				
				Intent intent = new Intent(mActivity,CompanyZoneActivity.class);
				startActivity(intent);
			}
		});
		//现在预约
		tab_reserve.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				/*tab_reserve.setBackground(getResources().getDrawable(
						R.drawable.tab_btn_selected3));
				tab_find.setBackground(getResources().getDrawable(
						R.drawable.tab_btn1));
				tab_company.setBackground(getResources().getDrawable(
						R.drawable.tab_btn2));*/
				Intent intent = new Intent(mActivity,AppointmentFragmentActivity.class);
				startActivity(intent);
			}
		});
		
		/*mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(mActivity,HealthProductDetailActivity.class);
				startActivity(intent);
			}
			
		});*/
	}

	@Override
	public void onRefresh() {
		pageNumber = 0 ;
		getHotProduct();
	}

	@Override
	public void onLoadMore() {
		if(products==null || products.isEmpty()){
			pageNumber = 0;
		}else{
			pageNumber++;
		}
		getHotProduct();
	}
	
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(DateUtils.convert2String(
				System.currentTimeMillis(), ""));
	}

}
