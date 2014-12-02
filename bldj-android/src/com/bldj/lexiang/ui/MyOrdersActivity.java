package com.bldj.lexiang.ui;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.adapter.OrderAdapter;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 我的订单
 * 
 * @author will
 * 
 */
public class MyOrdersActivity extends BaseActivity implements
IXListViewListener{

	ActionBar mActionBar;
	
	
	private ProgressBar progressBar;
	private XListView mListView;
	private OrderAdapter listAdapter;
	private List<Order> orders;

	private int pageNumber = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.my_orders);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我的订单");
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
		progressBar = (ProgressBar) 
				findViewById(R.id.progress_listView);
		mListView = (XListView) findViewById(R.id.listview);
	}

	@Override
	public void initListener() {
		
	}
	
	/**
	 * 获取用户订单
	 */
	private void getData() {
		User user = MyApplication.getInstance().getCurrentUser();
		ApiBuyUtils.getOrders(MyOrdersActivity.this, Long.parseLong(user.getUserId()),
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							 ToastUtils.showToast(MyOrdersActivity.this,
							 parseModel.getMsg());
							 return;
							
						} else {
							List<Order> ordersList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Order>>() {
									});

							if (pageNumber == 0) {
								orders.clear();
							}
							orders.addAll(ordersList);

							listAdapter.notifyDataSetChanged();
							onLoad();
						}

					}
				});
	}

	@Override
	public void onRefresh() {
		pageNumber = 1;
		getData();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getData();
	}

	private void onLoad() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mListView.setRefreshTime(DateUtils.convert2String(
				System.currentTimeMillis(), ""));
	}

}
