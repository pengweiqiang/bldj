package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.db.DatabaseUtil;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 经络养生
 * 
 * @author will
 * 
 */
public class CollectJlysFragment extends BaseFragment implements IXListViewListener{

	private ProgressBar progressBar;
	private View infoView;
	private XListView mListView;
	private HomeAdapter listAdapter;
	private List<Product> products;
	
	private int pageNumber = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.collect_jlys, container, false);
		
		initView();
		
		initListener();
		
		return infoView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		products = new ArrayList<Product>();
		listAdapter = new HomeAdapter(mActivity, products);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		getCollectProduct();
	}

	/**
	 * 初始化控件
	 */
	private void initView(){
		
		progressBar = (ProgressBar)infoView.findViewById(R.id.progress_listView);
		mListView = (XListView)infoView.findViewById(R.id.jlys_listview);
		
		
	}
	/**
	 * 事件初始化
	 */
	private void initListener(){
		
		
	}
	
	/**
	 * 获取收藏数据
	 */
	private void getCollectProduct() {
		List<Product> productsList = DatabaseUtil.getInstance(mActivity).queryFavProduct(pageNumber, ApiConstants.LIMIT);
		progressBar.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		if(pageNumber==0){
			products.clear();
		}
		products.addAll(productsList);

		listAdapter.notifyDataSetChanged();
		onLoad();
	}

	@Override
	public void onRefresh() {
		pageNumber=0;
		getCollectProduct();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getCollectProduct();
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(DateUtils.convert2String(System.currentTimeMillis(),""));
	}

}
