package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.GroupAdapter;
import com.bldj.lexiang.api.ApiSellerUtils;
import com.bldj.lexiang.api.vo.Evals;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.db.DatabaseUtil;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.ShareUtil;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.CustomViewPager;
import com.bldj.lexiang.view.LoadingDialog;
import com.bldj.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * 美容师个人页面
 * 
 * @author will
 * 
 */
public class SellerPersonalActivity extends FragmentActivity {

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

	private PopupWindow popupWindow;
	private View view;
	private ListView lv_group;
	private List<TitleBarEnum> groups;
	LoadingDialog loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.seller_personal);
		super.onCreate(savedInstanceState);

		sellerVo = (Seller) this.getIntent().getSerializableExtra("seller");
		sellerId = this.getIntent().getLongExtra("sellerId", 0);

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
		String levelStr = "";
		if (sellerVo.getDealnumSum() < 20) {
			// levelStr = "★";
			star_ratingbar.setNumStars(1);
		} else if (sellerVo.getDealnumSum() >= 20
				&& sellerVo.getDealnumSum() < 200) {
			star_ratingbar.setNumStars(2);
		} else if (sellerVo.getDealnumSum() >= 200) {
			// levelStr = "★★";
			star_ratingbar.setNumStars(3);
		} else {
			// levelStr = "★★★★";
			star_ratingbar.setNumStars(5);
		}
		// tv_level.setText(levelStr);
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
			public void onClick(View arg0) {
				ToastUtils.showToast(SellerPersonalActivity.this, "分享微信...");
				shareUtil.sendMsgToWX("健康送到家，方便你我他",
						SendMessageToWX.Req.WXSceneTimeline);
			}
		});
		// 预约次美容师-->弹出下面的服务项目
		btn_appointseller.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(checkIsLogin()){
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

	private void buildTitleBar(View parent) {
		DeviceInfo.setContext(this);
		groups = new ArrayList<TitleBarEnum>();
		groups.add(TitleBarEnum.SELLER_FAV);
		groups.add(TitleBarEnum.SELLER_SHARE);
		if (popupWindow == null) {
			view = LayoutInflater.from(this).inflate(R.layout.group_list, null);
			lv_group = (ListView) view.findViewById(R.id.lvGroup);

			popupWindow = new PopupWindow(view, DeviceInfo.getScreenWidth() / 2
					- parent.getWidth(), LayoutParams.WRAP_CONTENT);
		}
		GroupAdapter groupAdapter = new GroupAdapter(this, groups);
		lv_group.setAdapter(groupAdapter);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		// popupWindow.showAsDropDown(parent, popupWindow.getWidth(), 0);
		popupWindow.showAsDropDown(parent);
		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				if (position == 0) {
					if (!isFav) {
						long row = DatabaseUtil.getInstance(
								SellerPersonalActivity.this).insertSeller(
								sellerVo);
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
				} else {
					ToastUtils
							.showToast(SellerPersonalActivity.this, "分享微信...");
					shareUtil.sendMsgToWX("健康送到家，方便你我他",
							SendMessageToWX.Req.WXSceneTimeline);
				}
				if (popupWindow != null) {
					popupWindow.dismiss();
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
