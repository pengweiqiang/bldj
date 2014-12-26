package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.ApiSellerUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 美容师个人的服务项目
 * 
 * @author will
 * 
 */
public class SellerServiceActivity extends BaseActivity  implements
IXListViewListener{

	private ProgressBar progressBar;
	private XListView mListView;
	private HomeAdapter listAdapter;
	private List<Product> products;


	private int pageNumber = 0;
	
	Seller sellerVo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.seller_service_appoint);
		super.onCreate(savedInstanceState);
		sellerVo = (Seller)this.getIntent().getSerializableExtra("seller");
		
		
		products = new ArrayList<Product>();
		listAdapter = new HomeAdapter(this, products,2,sellerVo);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

		getData();
	}

	@Override
	public void initView() {
		progressBar = (ProgressBar)findViewById(R.id.progress_listView);
		mListView = (XListView) findViewById(R.id.listview);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 获取养生服务数据
	 */
	private void getData() {
		
		ApiSellerUtils.getSellerProduct(this, sellerVo.getId(), pageNumber, ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(SellerServiceActivity.this, parseModel.getMsg());
							mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"点击重试");

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
