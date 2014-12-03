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
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;
import com.bldj.universalimageloader.core.ImageLoader;

/**
 * 美容师个人页面
 * 
 * @author will
 * 
 */
public class SellerPersonalActivity extends BaseActivity implements IXListViewListener{
	
	private ImageView imageHead;
	private TextView tv_username;
	private TextView tv_price;
	private TextView tv_work;
	private TextView tv_order_count;
	private TextView tv_level;
	private Button btn_collect;
	private Button btn_share;
	private TextView tv_goodeval,tv_mideval,tv_badeval;
	
	ActionBar mActionBar;
	Seller sellerVo;
	
	private ProgressBar progressBar;
	private XListView mListView;
	private HomeAdapter listAdapter;
	private List<Product> products;
	Evals evals;
	
	private int pageNumber = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.seller_personal);
		super.onCreate(savedInstanceState);
		
		sellerVo = (Seller)this.getIntent().getSerializableExtra("seller");
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
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
		progressBar = (ProgressBar)findViewById(R.id.progress_listView);
		mListView = (XListView)findViewById(R.id.listview);
		
		imageHead = (ImageView)findViewById(R.id.head_img);
		tv_order_count = (TextView)findViewById(R.id.order_count);
		tv_price = (TextView)findViewById(R.id.price);
		tv_username = (TextView)findViewById(R.id.username);
		tv_level = (TextView)findViewById(R.id.level);
		tv_work = (TextView)findViewById(R.id.work);
		btn_collect = (Button)findViewById(R.id.collect);
		btn_share = (Button)findViewById(R.id.share);
		tv_badeval = (TextView)findViewById(R.id.badEval);
		tv_mideval = (TextView)findViewById(R.id.midEval);
		tv_goodeval = (TextView)findViewById(R.id.goodEval);
	}
	
	private void initData(){
		ImageLoader.getInstance().displayImage(
				sellerVo.getHeadurl(),
				imageHead,
				MyApplication.getInstance().getOptions(
						R.drawable.ic_launcher));
		tv_order_count.setText("共接单"+sellerVo.getDealnumSum()+"次");
		tv_price.setText("均价："+String.valueOf(sellerVo.getAvgPrice()));
		tv_username.setText(sellerVo.getUsername());
		tv_level.setText(sellerVo.getUserGrade());
		tv_work.setText("年龄："+sellerVo.getWorkyear());
		
			
	}
	
	@Override
	public void initListener() {
		//收藏
		btn_collect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		//分享
		btn_share.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	@Override
	public void onRefresh() {
		pageNumber=0;
		getProduct();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getProduct();
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(DateUtils.convert2String(System.currentTimeMillis(),""));
	}
	
	/**
	 * 获取数据
	 */
	private void getProduct() {
		ApiProductUtils.getProducts(SellerPersonalActivity.this, "1", 2,
				0, 0, pageNumber, ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							// ToastUtils.showToast(mActivity,
							// parseModel.getMsg());
							// return;
							List<Product> productsList = new ArrayList<Product>();

							Product p1 = new Product();
							p1.setName("商品" + (products.size() + 1));
							p1.setPicurl("http://img02.taobaocdn.com/bao/uploaded/i3/T11iAAFoNbXXXXXXXX_!!0-item_pic.jpg_110x110.jpg");

							Product p2 = new Product();
							p2.setName("商品" + (products.size() + 2));
							p2.setPicurl("http://img02.taobaocdn.com/bao/uploaded/i1/TB1aK_JGFXXXXXzXVXXXXXXXXXX_!!0-item_pic.jpg_110x110.jpg");

							productsList.add(p1);
							productsList.add(p2);
							Product p3 = new Product();
							p3.setName("商品" + (products.size() + 3));
							p3.setPicurl("http://img.taobaocdn.com/bao/uploaded/TB10ChmGFXXXXadaXXXSutbFXXX.jpg");
							productsList.add(p3);

							Product p4 = new Product();
							p4.setName("商品" + (products.size() + 4));
							p4.setPicurl("http://img02.taobaocdn.com/bao/uploaded/i1/TB1aK_JGFXXXXXzXVXXXXXXXXXX_!!0-item_pic.jpg_110x110.jpg");
							productsList.add(p4);

							Product p5 = new Product();
							p5.setName("商品" + (products.size() + 5));
							p5.setPicurl("http://img.taobaocdn.com/bao/uploaded/TB1rpHzGpXXXXXJaXXXSutbFXXX.jpg");
							productsList.add(p5);

							Product p6 = new Product();
							p6.setName("商品" + (products.size() + 6));
							p6.setPicurl("http://img.taobaocdn.com/bao/uploaded/TB1T2YnGpXXXXaFaXXXSutbFXXX.jpg");
							productsList.add(p6);

							Product p7 = new Product();
							p7.setName("商品" + (products.size() + 7));
							p7.setPicurl("http://img01.taobaocdn.com/imgextra/i1/1713844438/TB2TXsCaXXXXXbuXXXXXXXXXXXX-1713844438.jpg");
							productsList.add(p7);

							Product p8 = new Product();
							p8.setName("商品" + (products.size() + 8));
							p8.setPicurl("http://img.taobaocdn.com/bao/uploaded/TB1wguNGpXXXXcgXVXXSutbFXXX.jpg");
							productsList.add(p8);
							
							if(pageNumber==0){
								products.clear();
							}
							products.addAll(productsList);

							listAdapter.notifyDataSetChanged();
							onLoad();

						} else {
							List<Product> productsList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Product>>() {
									});
						}

					}
				});
	}
	
	/**
	 * 获取美容师的评价
	 */
	private void getSellerEval(){
		ApiSellerUtils.getSellerEvals(this, sellerVo.getId(), new HttpConnectionUtil.RequestCallback() {
			
			@Override
			public void execute(ParseModel parseModel) {
				if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
						.getStatus())) {
					 //ToastUtils.showToast(SellerPersonalActivity.this,parseModel.getMsg());
					 return;
				}else{
					evals = (Evals)JsonUtils.fromJson(parseModel.getData().toString(), Evals.class);
					tv_goodeval.setText("好评   "+evals.getGoodEval()+"条");
					tv_mideval.setText("中评   "+evals.getMidEval()+"条");
					tv_badeval.setText("差评   "+evals.getBadEval()+"条");
				}
			}
		});
	}
}
