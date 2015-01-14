package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiSellerUtils;
import com.bldj.lexiang.api.vo.ConfParams;
import com.bldj.lexiang.api.vo.Evals;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.db.DatabaseUtil;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ShareUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.ActionItem;
import com.bldj.lexiang.view.CustomViewPager;
import com.bldj.lexiang.view.LoadingDialog;
import com.bldj.lexiang.view.SharePopupWindow;
import com.bldj.lexiang.view.TitlePopup;
import com.bldj.lexiang.view.TitlePopup.OnItemOnClickListener;
import com.bldj.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * 美容师个人页面
 * 
 * @author will
 * 
 */
public class SellerPersonalActivity extends BaseFragmentActivity{

	private ImageView imageHead;
	private TextView tv_username;
	private TextView tv_price;
	private TextView tv_work;
	private TextView tv_order_count;
	private TextView tv_level;
	private Button btn_collect;
	private Button btn_share;
	private Button btn_appointseller;
	private RadioGroup rg_title;
	private RadioButton rb_msg, rb_service, rb_work;

	ActionBar mActionBar;
	CustomViewPager mViewPager;
	List<Fragment> list;
	RatingBar star_ratingbar;

	Seller sellerVo;
	long sellerId;

	Evals evals;// 评价
	boolean isFav;// 是否收藏

	ShareUtil shareUtil;

	LoadingDialog loading;
	
	//定义标题栏弹窗按钮
	private TitlePopup titlePopup;
	SharePopupWindow pop ;
	
	
	private String time;
	private int timeIndex;
	private Product product;
	private String address;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.seller_personal);
		super.onCreate(savedInstanceState);

		sellerVo = (Seller) this.getIntent().getSerializableExtra("seller");
		sellerId = this.getIntent().getLongExtra("sellerId", 0);
		time = this.getIntent().getStringExtra("time");
		timeIndex = this.getIntent().getIntExtra("timeIndex", -1);
		product = (Product)this.getIntent().getSerializableExtra("product");
		address = this.getIntent().getStringExtra("address");
		
		initView();
		initData();
		initListener();

		shareUtil = new ShareUtil(this);
		shareUtil.initWX();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		findViewById(R.id.actionBarLayout).setBackgroundColor(
				getResources().getColor(R.color.white));
		actionBar.setTitleTextColor(R.color.app_title_color);
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		// actionBar.hideRightActionButton();
		actionBar.setRightTextActionButton("", R.drawable.seller_more, false,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						buildTitleBar(v);
					}
				});
	}

	public void initView() {
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		mViewPager = (CustomViewPager) findViewById(R.id.viewPager);

		imageHead = (ImageView) findViewById(R.id.head_img);
		tv_order_count = (TextView) findViewById(R.id.order_count);
		tv_price = (TextView) findViewById(R.id.price);
		tv_username = (TextView) findViewById(R.id.username);
		tv_level = (TextView) findViewById(R.id.level);
		tv_work = (TextView) findViewById(R.id.work);
		btn_collect = (Button) findViewById(R.id.collect);
		btn_share = (Button) findViewById(R.id.share);
		btn_appointseller = (Button) findViewById(R.id.appointseller);
		rg_title = (RadioGroup) findViewById(R.id.rg_title);
		rb_msg = (RadioButton) findViewById(R.id.radio_msg);
		rb_service = (RadioButton) findViewById(R.id.radio_service);
		rb_work = (RadioButton) findViewById(R.id.radio_work);
		star_ratingbar = (RatingBar) findViewById(R.id.ratingBar);

	}

	private void initData() {

		if (sellerVo == null) {
			getSellerById();
			return;
		}
		mActionBar.setTitle(sellerVo.getNickname());
		// 获取此美容师是否收藏过
		isFav = DatabaseUtil.getInstance(this).checkFavSeller(sellerVo.getId());
		if (isFav) {
			btn_collect.setText("已收藏");
		}

		ImageLoader.getInstance().displayImage(
				sellerVo.getHeadurl(),
				imageHead,
				MyApplication.getInstance().getOptions(
						R.drawable.default_head_image));
		tv_order_count.setText("共接单" + sellerVo.getDealnumSum() + "次");
		tv_price.setText("均价：￥" + String.valueOf(sellerVo.getAvgPrice()));
		tv_username.setText(sellerVo.getNickname());
		int level = ConfParams.getStarRuleCount(sellerVo.getDealnumSum(), MyApplication.getInstance().getConfParams().getStarRule());
		star_ratingbar.setNumStars(level);
		star_ratingbar.setRating(level);
		tv_work.setText("年龄：" + sellerVo.getUserGrade());

		// 实例化对象
		list = new ArrayList<Fragment>();

		// 设置数据源
		// 信息概览
		SellerInfoFragment sellerInfoFragment = new SellerInfoFragment();
		// 服务项目
		SellerServiceFragment serviceFragment = new SellerServiceFragment();
		SellerWorkFragment workFragment = new SellerWorkFragment();

		list.add(sellerInfoFragment);
		list.add(serviceFragment);
		list.add(workFragment);

		// 设置适配器
		FragmentPagerAdapter adapter = new FragmentPagerAdapter(
				getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return list.get(arg0);
			}
		};

		// 绑定适配器
		mViewPager.setAdapter(adapter);
		// 设置滑动监听
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mViewPager.setCurrentItem(position, false);
				switch (position) {
				case 0:
					rb_msg.setChecked(true);
					break;
				case 1:
					rb_service.setChecked(true);
					break;
				case 2:
					rb_work.setChecked(true);
					break;

				default:
					break;
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Log.i("tuzi", arg0 + "," + arg1 + "," + arg2);

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	public void initListener() {
		imageHead.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SellerPersonalActivity.this,ImageViewActivity.class);
				intent.putExtra("url", sellerVo.getHeadurl());
				intent.putExtra("name", sellerVo.getNickname());
				startActivity(intent);
			}
		});
		// 收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isFav) {
					long row = DatabaseUtil.getInstance(
							SellerPersonalActivity.this).insertSeller(sellerVo);
					if (row > 0) {
						isFav = true;
						// ToastUtils.showToast(SellerPersonalActivity.this,
						// "收藏成功");
						btn_collect.setText("已收藏");
					} else {
						// ToastUtils.showToast(SellerPersonalActivity.this,
						// "该美容师已经收藏");
					}
				} else {
					int row = DatabaseUtil.getInstance(
							SellerPersonalActivity.this).deleteFavSeller(
							sellerVo.getId());
					if (row > 0) {
						isFav = false;
						// ToastUtils.showToast(SellerPersonalActivity.this,
						// "取消收藏");
						btn_collect.setText("收藏");
					} else {
						ToastUtils.showToast(SellerPersonalActivity.this,
								"取消收藏失败，稍后请重试！");
					}
				}

			}
		});
		// 分享
		btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				pop = new SharePopupWindow(SellerPersonalActivity.this,new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						switch (position) {
						case 0://微信分享
							MyApplication.getInstance().type = 0;
							ToastUtils.showToast(SellerPersonalActivity.this, "分享微信...");
							shareUtil.sendWebPageToWX(MyApplication.getInstance().getConfParams().getShareSellerTxt(),
									SendMessageToWX.Req.WXSceneSession,sellerVo.getDetailUrl());
							break;
						case 1://微信分享
							MyApplication.getInstance().type = 1;
							ToastUtils.showToast(SellerPersonalActivity.this, "分享微信...");
							shareUtil.sendWebPageToWX(MyApplication.getInstance().getConfParams().getShareSellerTxt(),
									SendMessageToWX.Req.WXSceneTimeline,sellerVo.getDetailUrl());
							break;
						case 2://新浪
							String shareUrlSina = shareUtil.shareSina(MyApplication.getInstance().getConfParams().getShareSellerTxt(), sellerVo.getDetailUrl(), sellerVo.getHeadurl());
							Intent intent = new Intent(SellerPersonalActivity.this,BannerWebActivity.class);
							intent.putExtra("url", shareUrlSina);
							intent.putExtra("name", "新浪微博分享");
							startActivity(intent);
							break;
						case 3://腾讯
							String shareUrlQQ = shareUtil.shareQQ(MyApplication.getInstance().getConfParams().getShareSellerTxt(), sellerVo.getDetailUrl(), sellerVo.getHeadurl());
							Intent intentQQ = new Intent(SellerPersonalActivity.this,BannerWebActivity.class);
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
		//我要预约他
		// 预约次美容师-->弹出下面的服务项目
		btn_appointseller.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(checkIsLogin()){
					if(!StringUtils.isEmpty(time) && timeIndex != -1 && product != null && !StringUtils.isEmpty(address)){
						Intent intent = new Intent(SellerPersonalActivity.this,
								AppointmentDoor3Activity.class);
						intent.putExtra("time", time);// 预约时间
						intent.putExtra("timeIndex", timeIndex);// 预约时间位置
						intent.putExtra("seller", sellerVo);// 预约美容师
						intent.putExtra("product", product);//预约产品
						intent.putExtra("address", address);//详细地址
						startActivity(intent);
						return;
					}
					Intent intent = new Intent(SellerPersonalActivity.this,
							SellerServiceActivity.class);
					intent.putExtra("seller", sellerVo);
					startActivity(intent);
				}
			}
		});

		rg_title.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_msg:
					mViewPager.setCurrentItem(0, false);
					break;
				case R.id.radio_service:
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.radio_work:
					mViewPager.setCurrentItem(2, false);
					break;
				}
			}
		});
	}

	public Seller getSellerVo() {
		return sellerVo;
	}

	private void buildTitleBar(final View parent) {
		//实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, isFav?"已收藏":"收藏", R.drawable.seller_sc));
		titlePopup.addAction(new ActionItem(this, "分享", R.drawable.seller_share));
		titlePopup.show(parent);
		titlePopup.setItemOnClickListener(new OnItemOnClickListener() {

			@Override
			public void onItemClick(ActionItem item, int position) {
				if(position==0){
					if (!isFav) {
						long row = DatabaseUtil.getInstance(
								SellerPersonalActivity.this).insertSeller(
								sellerVo);
						if (row > 0) {
							isFav = true;
							 ToastUtils.showToast(SellerPersonalActivity.this,
							 "收藏成功");
//							btn_collect.setText("已收藏");
						} else {
							// ToastUtils.showToast(SellerPersonalActivity.this,
							// "该美容师已经收藏");
						}
					} else {
						int row = DatabaseUtil.getInstance(
								SellerPersonalActivity.this).deleteFavSeller(
								sellerVo.getId());
						if (row > 0) {
							isFav = false;
							 ToastUtils.showToast(SellerPersonalActivity.this,
							 "取消收藏");
//							btn_collect.setText("收藏");
						} else {
							ToastUtils.showToast(SellerPersonalActivity.this,
									"取消收藏失败，稍后请重试！");
						}
					}
				}else{
					pop = new SharePopupWindow(SellerPersonalActivity.this,new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							switch (position) {
							case 0://微信分享
								MyApplication.getInstance().type = 0;
								ToastUtils.showToast(SellerPersonalActivity.this, "分享微信...");
								shareUtil.sendWebPageToWX(MyApplication.getInstance().getConfParams().getShareSellerTxt(),
										SendMessageToWX.Req.WXSceneSession,sellerVo.getDetailUrl());
								break;
							case 1://微信分享
								MyApplication.getInstance().type = 1;
								ToastUtils.showToast(SellerPersonalActivity.this, "分享微信...");
								shareUtil.sendWebPageToWX(MyApplication.getInstance().getConfParams().getShareSellerTxt(),
										SendMessageToWX.Req.WXSceneTimeline,sellerVo.getDetailUrl());
								break;
							case 2://新浪
								String shareUrlSina = shareUtil.shareSina(MyApplication.getInstance().getConfParams().getShareSellerTxt(), sellerVo.getDetailUrl(), sellerVo.getHeadurl());
								Intent intent = new Intent(SellerPersonalActivity.this,BannerWebActivity.class);
								intent.putExtra("url", shareUrlSina);
								intent.putExtra("name", "新浪微博分享");
								startActivity(intent);
								break;
							case 3://腾讯
								String shareUrlQQ = shareUtil.shareQQ(MyApplication.getInstance().getConfParams().getShareSellerTxt(), sellerVo.getDetailUrl(), sellerVo.getHeadurl());
								Intent intentQQ = new Intent(SellerPersonalActivity.this,BannerWebActivity.class);
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
					pop.showAtLocation(parent,Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
				}
			}
			
		});
	}

	/**
	 * 通过sellerid获取美容师信息
	 */
	private void getSellerById() {
		loading = new LoadingDialog(this);
		loading.show();
		ApiSellerUtils.getSellerById(this, sellerId,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						loading.cancel();
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(SellerPersonalActivity.this,
									parseModel.getMsg());
						} else {
							sellerVo = JsonUtils.fromJson(parseModel.getData()
									.toString(), Seller.class);
							initData();
						}
					}
				});
	}
	
	/**
	 * 判断是否登录
	 * 
	 * @return
	 */
	private boolean checkIsLogin() {
		if (MyApplication.getInstance().getCurrentUser() == null) {
			Intent intent = new Intent(this,
					RegisterAndLoginActivity.class);
			startActivity(intent);
			return false;
		}
		return true;
	}

	/**
	 * 获取养生服务数据
	 */
	private void getData() {
		// ApiSellerUtils.getSellerProduct(this, sellerVo.getId(), pageNumber,
		// ApiConstants.LIMIT,
		// new HttpConnectionUtil.RequestCallback() {
		//
		// @Override
		// public void execute(ParseModel parseModel) {
		// progressBar.setVisibility(View.GONE);
		// mListView.setVisibility(View.VISIBLE);
		// if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
		// .getStatus())) {
		// ToastUtils.showToast(this, parseModel.getMsg());
		// return;
		//
		// } else {
		// List<Product> productsList = JsonUtils.fromJson(
		// parseModel.getData().toString(),
		// new TypeToken<List<Product>>() {
		// });
		//
		// if (pageNumber == 0) {
		// products.clear();
		// }
		// products.addAll(productsList);
		//
		// listAdapter.notifyDataSetChanged();
		// mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"加载完毕");
		// }
		//
		// }
		// });
	}

	/**
	 * 获取美容师的评价
	 */
	// private void getSellerEval() {
	// ApiSellerUtils.getSellerEvals(this, sellerVo.getId(),
	// new HttpConnectionUtil.RequestCallback() {
	//
	// @Override
	// public void execute(ParseModel parseModel) {
	// if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
	// .getStatus())) {
	// // ToastUtils.showToast(SellerPersonalActivity.this,parseModel.getMsg());
	// return;
	// } else {
	// evals = (Evals) JsonUtils.fromJson(parseModel
	// .getData().toString(), Evals.class);
	// tv_goodeval.setText("好评   " + evals.getGoodEval()
	// + "条");
	// tv_mideval.setText("中评   " + evals.getMidEval()
	// + "条");
	// tv_badeval.setText("差评   " + evals.getBadEval()
	// + "条");
	// }
	// }
	// });
	// }
}
