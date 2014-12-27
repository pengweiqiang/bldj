package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.OrderAdapter;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.enums.OrderStatusEnum;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
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
	
	RelativeLayout rl_loading;//进度条
	ImageView loading_ImageView;//加载动画
	RelativeLayout rl_loadingFail;//加载失败
	private XListView mListView;
	private OrderAdapter listAdapter;
	private List<Order> orders;

	private int pageNumber = 0;
	
	private int selectOrderIndex = -1;
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

		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(MyOrdersActivity.this,OrderDetail2Activity.class);
				selectOrderIndex = position-1;
				intent.putExtra("order", orders.get(selectOrderIndex));
				startActivityForResult(intent, 123);
			}
			
		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == 23){
			int type = data.getIntExtra("type", -1);
			if(selectOrderIndex !=-1){
				if(type == 0){//取消订单
					orders.get(selectOrderIndex).setStatus(OrderStatusEnum.CANCLED);
					orders.get(selectOrderIndex).setStatusStr("已取消");
				}else if(type ==1){//支付成功
					orders.get(selectOrderIndex).setStatus(OrderStatusEnum.PAID_ONLINE);
					orders.get(selectOrderIndex).setStatusStr("线上已支付");
				}
				listAdapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
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
	 * 获取用户订单
	 */
	private void getData() {
		showLoading();
		User user = MyApplication.getInstance().getCurrentUser();
		ApiBuyUtils.getOrders(MyOrdersActivity.this, user.getUserId(),pageNumber,ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						rl_loading.setVisibility(View.GONE);
						
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							mListView.setVisibility(View.GONE);
							rl_loadingFail.setVisibility(View.VISIBLE);
//							 ToastUtils.showToast(MyOrdersActivity.this,
//							 parseModel.getMsg());
//							 mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"点击重试");
							
						} else {
							mListView.setVisibility(View.VISIBLE);
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
