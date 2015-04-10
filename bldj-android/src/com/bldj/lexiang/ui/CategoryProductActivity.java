package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

/**
 * 分类后的产品列表
 * 
 * @author will
 * 
 */
public class CategoryProductActivity extends BaseActivity implements
IXListViewListener {

	ActionBar mActionBar;
	private int categoryId;
	private String categoryName;
	
	
	RelativeLayout rl_loading;//进度条
	ImageView loading_ImageView;//加载动画
	RelativeLayout rl_loadingFail;//加载失败
	private XListView mListView;
	private HomeAdapter listAdapter;
	private List<Product> products;

	private int pageNumber = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.category_product);
		super.onCreate(savedInstanceState);
		categoryId = this.getIntent().getIntExtra("categoryId", 0);
		categoryName = this.getIntent().getStringExtra("name");
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
		
		products = new ArrayList<Product>();
		listAdapter = new HomeAdapter(this, products);
//		mListView.setAdapter(listAdapter);
//		setAlphaAdapter();
		SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(listAdapter);
		swingBottomInAnimationAdapter.setInitialDelayMillis(300);
		swingBottomInAnimationAdapter.setAbsListView(mListView);

		mListView.setAdapter(swingBottomInAnimationAdapter);
		

		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		getData();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle(categoryName);
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
		rl_loading = (RelativeLayout) findViewById(R.id.progress_listView);
		rl_loadingFail = (RelativeLayout) findViewById(R.id.loading_fail);
		loading_ImageView = (ImageView)findViewById(R.id.loading_imageView);
		mListView = (XListView) findViewById(R.id.listview);
	}

	@Override
	public void initListener() {
		//点击重试
		rl_loadingFail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pageNumber = 0;
				getData();
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
	 * 获取养生服务数据
	 */
	private void getData() {
		showLoading();
		ApiProductUtils.getProductByCategoryByid(CategoryProductActivity.this, pageNumber, ApiConstants.LIMIT,
				categoryId,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						rl_loading.setVisibility(View.GONE);
						
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							mListView.setVisibility(View.GONE);
							rl_loadingFail.setVisibility(View.VISIBLE);
//							 ToastUtils.showToast(CategoryProductActivity.this,
//							 parseModel.getMsg());
//							 mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"点击重试");
							 return;
							
						} else {
							mListView.setVisibility(View.VISIBLE);
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

	@Override
	public void onRefresh() {
		pageNumber = 0;
		getData();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getData();
	}
	private void setAlphaAdapter() {
		AnimationAdapter animAdapter = new AlphaInAnimationAdapter(listAdapter);
		animAdapter.setAbsListView(mListView);
		mListView.setAdapter(animAdapter);
	}


}
