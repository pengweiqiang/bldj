package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.CategoryAdapter;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.Category;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;
/**
 * 热门推荐（分类）
 * @author will
 *
 */
public class HotProductFragment extends BaseFragment implements IXListViewListener{
	private View infoView;
	private ActionBar mActionBar;
	
	private ProgressBar progressBar;
	private XListView mListView;
	private CategoryAdapter listAdapter;
	private List<Category> categorys;
	
	private int pageNumber = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.hot_fragment, container, false);
		
		
		initView();

		initListener();

		return infoView;
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("分类");
		actionBar.hideLeftActionButton();
		actionBar.hideRightActionButton();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		categorys = new ArrayList<Category>();
		listAdapter = new CategoryAdapter(mActivity, categorys);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		getCategory();
	}
	/**
	 * 初始化控件
	 */
	private void initView(){
		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		progressBar = (ProgressBar)infoView.findViewById(R.id.progress_listView);
		mListView = (XListView)infoView.findViewById(R.id.listview);
		
		
	}
	/**
	 * 事件初始化
	 */
	private void initListener(){
		
		
	}
	
	/**
	 * 获取收藏数据
	 */
	private void getCategory() {
		ApiProductUtils.getCategory(mActivity, pageNumber, ApiConstants.LIMIT, new HttpConnectionUtil.RequestCallback() {

			@Override
			public void execute(ParseModel parseModel) {
				progressBar.setVisibility(View.GONE);
				mListView.setVisibility(View.VISIBLE);
				if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
						.getStatus())) {
					 ToastUtils.showToast(mActivity,
					 parseModel.getMsg());
					 return;
//					List<Product> productsList = new ArrayList<Product>();
//
//					Product p1 = new Product();
//					p1.setName("商品" + (products.size() + 1));
//					p1.setPicurl("http://img02.taobaocdn.com/bao/uploaded/i3/T11iAAFoNbXXXXXXXX_!!0-item_pic.jpg_110x110.jpg");
//
//					Product p2 = new Product();
//					p2.setName("商品" + (products.size() + 2));
//					p2.setPicurl("http://img02.taobaocdn.com/bao/uploaded/i1/TB1aK_JGFXXXXXzXVXXXXXXXXXX_!!0-item_pic.jpg_110x110.jpg");
//
//					productsList.add(p1);
//					productsList.add(p2);
//					Product p3 = new Product();
//					p3.setName("商品" + (products.size() + 3));
//					p3.setPicurl("http://img.taobaocdn.com/bao/uploaded/TB10ChmGFXXXXadaXXXSutbFXXX.jpg");
//					productsList.add(p3);
//
//					Product p4 = new Product();
//					p4.setName("商品" + (products.size() + 4));
//					p4.setPicurl("http://img02.taobaocdn.com/bao/uploaded/i1/TB1aK_JGFXXXXXzXVXXXXXXXXXX_!!0-item_pic.jpg_110x110.jpg");
//					productsList.add(p4);
//
//					Product p5 = new Product();
//					p5.setName("商品" + (products.size() + 5));
//					p5.setPicurl("http://img.taobaocdn.com/bao/uploaded/TB1rpHzGpXXXXXJaXXXSutbFXXX.jpg");
//					productsList.add(p5);
//
//					Product p6 = new Product();
//					p6.setName("商品" + (products.size() + 6));
//					p6.setPicurl("http://img.taobaocdn.com/bao/uploaded/TB1T2YnGpXXXXaFaXXXSutbFXXX.jpg");
//					productsList.add(p6);
//
//					Product p7 = new Product();
//					p7.setName("商品" + (products.size() + 7));
//					p7.setPicurl("http://img01.taobaocdn.com/imgextra/i1/1713844438/TB2TXsCaXXXXXbuXXXXXXXXXXXX-1713844438.jpg");
//					productsList.add(p7);
//
//					Product p8 = new Product();
//					p8.setName("商品" + (products.size() + 8));
//					p8.setPicurl("http://img.taobaocdn.com/bao/uploaded/TB1wguNGpXXXXcgXVXXSutbFXXX.jpg");
//					productsList.add(p8);
//					
//					if(pageNumber==0){
//						products.clear();
//					}
//					products.addAll(productsList);
//
//					listAdapter.notifyDataSetChanged();
//					onLoad();

				} else {
					List<Category> categoryList = JsonUtils.fromJson(
							parseModel.getData().toString(),
							new TypeToken<List<Category>>() {
							});
					if(pageNumber==0){
						categorys.clear();
					}
					categorys.addAll(categoryList);

					listAdapter.notifyDataSetChanged();
					onLoad();
				}

			}
		});
	}

	@Override
	public void onRefresh() {
		pageNumber=0;
		getCategory();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getCategory();
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(DateUtils.convert2String(System.currentTimeMillis(),""));
	}

	
}
