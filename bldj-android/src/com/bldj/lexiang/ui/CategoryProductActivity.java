package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

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
	
	
	private ProgressBar progressBar;
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
		mListView.setAdapter(listAdapter);
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
		progressBar = (ProgressBar) 
				findViewById(R.id.progress_listView);
		mListView = (XListView) findViewById(R.id.listview);
	}

	@Override
	public void initListener() {
		
	}
	
	/**
	 * 获取养生服务数据
	 */
	private void getData() {
		ApiProductUtils.getProductByCategoryByid(CategoryProductActivity.this, pageNumber, ApiConstants.LIMIT,
				categoryId,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							 ToastUtils.showToast(CategoryProductActivity.this,
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


}
