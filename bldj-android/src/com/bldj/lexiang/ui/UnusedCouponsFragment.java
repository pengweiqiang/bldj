package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.CouponsAdapter;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.Coupon;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 未使用的优惠卷
 * 
 * @author will
 * 
 */
public class UnusedCouponsFragment extends BaseFragment implements IXListViewListener{

	private ProgressBar progressBar;
	private View infoView;
	private XListView mListView;
	private CouponsAdapter listAdapter;
	private List<Coupon> coupons;
	
	private int pageNumber = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.unused_coupons, container, false);
		
		initView();
		
		initListener();
		
		return infoView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		coupons = new ArrayList<Coupon>();
		listAdapter = new CouponsAdapter(mActivity, coupons);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		getCoupons();
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
	 * 获取优惠卷
	 */
	private void getCoupons() {
		User user = MyApplication.getInstance().getCurrentUser();
		ApiBuyUtils.couponsManage(mActivity, user.getUserId(), -1, "", 3,pageNumber,ApiConstants.LIMIT,
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
							List<Coupon> productsList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Coupon>>() {
									});
							if(pageNumber==0){
								coupons.clear();
							}
							coupons.addAll(productsList);

							listAdapter.notifyDataSetChanged();
							onLoad();
						}

					}
				});
	}

	@Override
	public void onRefresh() {
		pageNumber=0;
		getCoupons();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getCoupons();
	}
	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(DateUtils.convert2String(System.currentTimeMillis(),""));
	}

}
