package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.db.DatabaseUtil;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

/**
 * 经络养生
 * 
 * @author will
 * 
 */
public class CollectJlysFragment extends BaseFragment implements IXListViewListener,OnClickListener{

	private ProgressBar progressBar;
	private View infoView;
	private XListView mListView;
	private HomeAdapter listAdapter;
	private List<Product> products;
	
	private int pageNumber = 0;
	View layoutEmpty;
	
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
//		mListView.setAdapter(listAdapter);
		setAlphaAdapter();
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
		
		layoutEmpty = infoView.findViewById(R.id.empty_layout);
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
		List<Product> productsList = DatabaseUtil.getInstance(mActivity).queryFavProduct(pageNumber, ApiConstants.LIMIT,0);
		progressBar.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
		if(pageNumber==0){
			products.clear();
			if(productsList==null || productsList.isEmpty()){
				infoView.findViewById(R.id.un_empty).setVisibility(View.GONE);
				layoutEmpty.setVisibility(View.VISIBLE);
				showEmpty(layoutEmpty,R.string.empty_collect_tip,R.string.empty_collect_go,R.drawable.empty_collect,CollectJlysFragment.this);
			}
		}
		products.addAll(productsList);

		listAdapter.notifyDataSetChanged();
		mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"加载完毕");
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

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(mActivity,MainActivity.class);
		startActivity(intent);
	}
	private void setAlphaAdapter() {
		AnimationAdapter animAdapter = new AlphaInAnimationAdapter(listAdapter);
		animAdapter.setAbsListView(mListView);
		mListView.setAdapter(animAdapter);
	}
	
}
