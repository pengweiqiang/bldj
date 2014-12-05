package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.ApiSellerUtils;
import com.bldj.lexiang.api.vo.Evals;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.db.DatabaseUtil;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ShareUtil;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;
import com.bldj.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

/**
 * 美容师个人页面
 * 
 * @author will
 * 
 */
public class SellerPersonalActivity extends BaseActivity implements
		IXListViewListener {

	private ImageView imageHead;
	private TextView tv_username;
	private TextView tv_price;
	private TextView tv_work;
	private TextView tv_order_count;
	private TextView tv_level;
	private Button btn_collect;
	private Button btn_share;
	private TextView tv_goodeval, tv_mideval, tv_badeval;

	ActionBar mActionBar;
	Seller sellerVo;

	private ProgressBar progressBar;
	private XListView mListView;
	private HomeAdapter listAdapter;
	private List<Product> products;
	Evals evals;// 评价
	boolean isFav;// 是否收藏

	private int pageNumber = 0;
	
	ShareUtil shareUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.seller_personal);
		super.onCreate(savedInstanceState);

		sellerVo = (Seller) this.getIntent().getSerializableExtra("seller");
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		initData();

		products = new ArrayList<Product>();
		listAdapter = new HomeAdapter(this, products);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

		getProduct();
		getSellerEval();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle(sellerVo.getNickname());
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
		progressBar = (ProgressBar) findViewById(R.id.progress_listView);
		mListView = (XListView) findViewById(R.id.listview);

		imageHead = (ImageView) findViewById(R.id.head_img);
		tv_order_count = (TextView) findViewById(R.id.order_count);
		tv_price = (TextView) findViewById(R.id.price);
		tv_username = (TextView) findViewById(R.id.username);
		tv_level = (TextView) findViewById(R.id.level);
		tv_work = (TextView) findViewById(R.id.work);
		btn_collect = (Button) findViewById(R.id.collect);
		btn_share = (Button) findViewById(R.id.share);
		tv_badeval = (TextView) findViewById(R.id.badEval);
		tv_mideval = (TextView) findViewById(R.id.midEval);
		tv_goodeval = (TextView) findViewById(R.id.goodEval);
	}

	private void initData() {
		
		shareUtil = new ShareUtil(mContext);
		shareUtil.initWX();
		
		// 获取此美容师是否收藏过
		isFav = DatabaseUtil.getInstance(mContext).checkFavSeller(
				sellerVo.getId());
		if (isFav) {
			btn_collect.setText("已收藏");
		}

		ImageLoader.getInstance().displayImage(sellerVo.getHeadurl(),
				imageHead,
				MyApplication.getInstance().getOptions(R.drawable.ic_launcher));
		tv_order_count.setText("共接单" + sellerVo.getDealnumSum() + "次");
		tv_price.setText("均价：￥" + String.valueOf(sellerVo.getAvgPrice()));
		tv_username.setText(sellerVo.getUsername());
		tv_level.setText(String.valueOf(sellerVo.getUserGrade()));
		tv_work.setText("年龄：" + sellerVo.getWorkyear());

	}

	@Override
	public void initListener() {
		// 收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!isFav) {
					long row = DatabaseUtil.getInstance(mContext).insertSeller(
							sellerVo);
					if (row > 0) {
						isFav = true;
						ToastUtils.showToast(mContext, "收藏成功");
						btn_collect.setText("已收藏");
					} else {
						ToastUtils.showToast(mContext, "该美容师已经收藏");
					}
				} else {
					int row = DatabaseUtil.getInstance(mContext)
							.deleteFavSeller(sellerVo.getId());
					if (row > 0) {
						isFav = false;
						ToastUtils.showToast(mContext, "取消收藏");
						btn_collect.setText("收藏");
					} else {
						ToastUtils.showToast(mContext, "取消收藏失败，稍后请重试！");
					}
				}

			}
		});
		// 分享
		btn_share.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ToastUtils.showToast(mContext, "分享微信...");
				shareUtil.sendMsgToWX("健康送到家，方便你我他",
						SendMessageToWX.Req.WXSceneTimeline);
			}
		});

	}

	@Override
	public void onRefresh() {
		pageNumber = 0;
		getProduct();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getProduct();
	}


	/**
	 * 获取数据
	 */
	private void getProduct() {
		ApiProductUtils.getProducts(SellerPersonalActivity.this, "1", 2, 0, 0,
				pageNumber, ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(mContext, parseModel.getMsg());

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
							mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"加载完毕");
						}

					}
				});
	}

	/**
	 * 获取美容师的评价
	 */
	private void getSellerEval() {
		ApiSellerUtils.getSellerEvals(this, sellerVo.getId(),
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							// ToastUtils.showToast(SellerPersonalActivity.this,parseModel.getMsg());
							return;
						} else {
							evals = (Evals) JsonUtils.fromJson(parseModel
									.getData().toString(), Evals.class);
							tv_goodeval.setText("好评   " + evals.getGoodEval()
									+ "条");
							tv_mideval.setText("中评   " + evals.getMidEval()
									+ "条");
							tv_badeval.setText("差评   " + evals.getBadEval()
									+ "条");
						}
					}
				});
	}
}
