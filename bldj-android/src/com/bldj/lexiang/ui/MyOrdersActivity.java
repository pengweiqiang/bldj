package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.OrderAdapter;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
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
		
		orders = new ArrayList<Order>();
		listAdapter = new OrderAdapter(this, orders);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		getData();
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
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(MyOrdersActivity.this,OrderDetail2Activity.class);
				intent.putExtra("order", orders.get(position-1));
				startActivity(intent);
			}
			
		});
	}
	
	/**
	 * 获取用户订单
	 */
	private void getData() {
		User user = MyApplication.getInstance().getCurrentUser();
		ApiBuyUtils.getOrders(MyOrdersActivity.this, user.getUserId(),pageNumber,ApiConstants.LIMIT,
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

//							if (pageNumber == 0) {
								orders.clear();
//							}
							orders.addAll(ordersList);

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
