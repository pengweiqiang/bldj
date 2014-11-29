package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.JlysHealthAdapter;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.ApiSellerUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 经络养生-->看养生师
 * 
 * @author will
 * 
 */
public class SeeHealthDivFragment extends BaseFragment implements IXListViewListener{

	private ProgressBar progressBar;
	private View infoView;
	private XListView mListView;
	private JlysHealthAdapter listAdapter;
	private List<Seller> sellers;
	
	private int pageNumber = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.see_health_div, container, false);
		ActionBar mActionBar = (ActionBar)getActivity().findViewById(R.id.actionBar);
		mActionBar.hideRightActionButton();
		initView();
		
		initListener();
		
		return infoView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		sellers = new ArrayList<Seller>();
		listAdapter = new JlysHealthAdapter(mActivity, sellers);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		getSellers();
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
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// 启动美容师个人界面
				Intent intent = new Intent(mActivity,
						SellerPersonalActivity.class);
//				intent.putExtra("seller", products.get(position));
				startActivity(intent);
			}
			
		});
		
	}
	
	/**
	 * 获取收藏美容师数据
	 */
	private void getSellers() {
		ApiSellerUtils.getSellers(mActivity, pageNumber, ApiConstants.LIMIT, 0, 1000, 0, 4, 0,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							 ToastUtils.showToast(mActivity,
							 parseModel.getMsg());
							 return;
							
						} else {
							List<Seller> sellersList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Seller>>() {
									});
							if(pageNumber==0){
								sellers.clear();
							}
							sellers.addAll(sellersList);

							listAdapter.notifyDataSetChanged();
							onLoad();
						}

					}
				});
	}

	@Override
	public void onRefresh() {
		pageNumber=0;
		getSellers();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getSellers();
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(DateUtils.convert2String(System.currentTimeMillis(),""));
	}


}
