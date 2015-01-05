package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.OrderAdapter;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.enums.OrderStatusEnum;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;
/**
 * 商城
 * @author will
 *
 */
public class MallFragment extends BaseFragment implements IXListViewListener{
	private View infoView;
	private ActionBar mActionBar;
	
	RelativeLayout rl_loading;//进度条
	ImageView loading_ImageView;//加载动画
	RelativeLayout rl_loadingFail;//加载失败
	private LinearLayout ll_tabTitle;//表头
	private LinearLayout ll_unLogin;
	private Button btn_login;
	private XListView mListView;
	private OrderAdapter listAdapter;
	private List<Order> orders;
	
	private int pageNumber = 0;
	int type;//查询订单类型
	private int selectOrderIndex = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.my_orders, container, false);
		initView();
		onConfigureActionBar(mActionBar);

		initListener();
		
		return infoView;
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("订单");
		infoView.findViewById(R.id.actionBarLayout).setBackgroundColor(getResources().getColor(R.color.app_bg_color));
		actionBar.setTitleTextColor(R.color.white);
		actionBar.hideLeftActionButton();
		actionBar.hideRightActionButton();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		IntentFilter countFilter = new IntentFilter(Constant.ACTION_MESSAGE_COUNT);
		mActivity.registerReceiver(mCountMsgReceiver, countFilter);
		orders = new ArrayList<Order>();
		listAdapter = new OrderAdapter(mActivity, orders);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
//		getData();
		User user = MyApplication.getInstance().getCurrentUser();
		if(user ==null){
			showUnLogin();
		}
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// 相当于Fragment的onResume
			User user = MyApplication.getInstance().getCurrentUser();
			if(user!=null && !orders.isEmpty()){
				ll_unLogin.setVisibility(View.GONE);
				rl_loading.setVisibility(View.GONE);
				ll_tabTitle.setVisibility(View.VISIBLE);
				mListView.setVisibility(View.VISIBLE);
//				getData();
			}else if(user!=null && orders.isEmpty()){
				type = ApiConstants.MAX_STATUS;
				getData();
			}else if(user == null){
				if(!orders.isEmpty()){
					orders.clear();
				}
				showUnLogin();
			}
		} else {
			// 相当于Fragment的onPause
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		User user = MyApplication.getInstance().getCurrentUser();
		if(user!=null && orders.isEmpty()){
			ll_unLogin.setVisibility(View.GONE);
			rl_loading.setVisibility(View.VISIBLE);
			ll_tabTitle.setVisibility(View.VISIBLE);
//			getData();
		}else if(user == null && !orders.isEmpty()){
			orders.clear();
			showUnLogin();
		}
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		mListView = (XListView)infoView.findViewById(R.id.listview);
		rl_loading = (RelativeLayout) infoView.findViewById(R.id.progress_listView);
		rl_loadingFail = (RelativeLayout) infoView.findViewById(R.id.loading_fail);
		loading_ImageView = (ImageView)infoView.findViewById(R.id.loading_imageView);
		ll_tabTitle = (LinearLayout)infoView.findViewById(R.id.ll_tab_title);
		ll_unLogin = (LinearLayout)infoView.findViewById(R.id.un_login);
		btn_login = (Button)infoView.findViewById(R.id.login);
	}
	/**
	 * 初始化事件
	 */
	private void initListener(){
		btn_login.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(mActivity,RegisterAndLoginActivity.class);
//					startActivityForResult(intent, 22);
					startActivity(intent);
				}
			});
		//点击重试
		rl_loadingFail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pageNumber = 0;
				type = 0;
				getData();
			}
		});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(mActivity,OrderDetail2Activity.class);
				selectOrderIndex = position-1;
				intent.putExtra("order", orders.get(selectOrderIndex));
				startActivityForResult(intent, 123);
			}
			
		});
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
		if(user!=null){
			//查询未支付订单
			ApiBuyUtils.getOrders(mActivity, user.getUserId(),pageNumber,ApiConstants.LIMIT,type,
					new HttpConnectionUtil.RequestCallback() {
	
						@Override
						public void execute(ParseModel parseModel) {
							rl_loading.setVisibility(View.GONE);
							
							if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
									.getStatus())) {
								mListView.setVisibility(View.GONE);
								rl_loadingFail.setVisibility(View.VISIBLE);
								
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
//								Intent intent = new Intent(Constant.ACTION_MESSAGE_COUNT);
//								intent.putExtra("countOrders", ordersList.size());
//								mActivity.sendBroadcast(intent);
								listAdapter.notifyDataSetChanged();
								mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"加载完毕");
							}
	
						}
					});
			
		}else{
			showUnLogin();
		}
	}

	private void showUnLogin(){
		ll_tabTitle.setVisibility(View.GONE);
		rl_loading.setVisibility(View.GONE);
		mListView.setVisibility(View.GONE);
		rl_loadingFail.setVisibility(View.GONE);
		ll_unLogin.setVisibility(View.VISIBLE);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 23){//修改订单状态
			int type = data.getIntExtra("type", -1);
			if(selectOrderIndex !=-1){
				if(type == 0){//取消订单
					orders.get(selectOrderIndex).setStatus(OrderStatusEnum.CANCLED);
					orders.get(selectOrderIndex).setStatusStr("已取消");
					orders.remove(selectOrderIndex);
				}else if(type ==1){//支付成功
					orders.get(selectOrderIndex).setStatus(OrderStatusEnum.PAID_ONLINE);
					orders.get(selectOrderIndex).setStatusStr("线上已支付");
					orders.remove(selectOrderIndex);
				}
				listAdapter.notifyDataSetChanged();
			}
		}/*else if(resultCode == 24 && data != null){//登录成功
			boolean isLogin = data.getBooleanExtra("isLogin", false);
			if(isLogin){
				ll_tabTitle.setVisibility(View.VISIBLE);
				rl_loading.setVisibility(View.VISIBLE);
				ll_unLogin.setVisibility(View.GONE);
				pageNumber = 0;
				getData();
			}
		}*/
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 显示订单数量红点
	 */
	private BroadcastReceiver mCountMsgReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Constant.ACTION_MESSAGE_COUNT.equals(intent.getAction())) {
				if(orders!=null){
					orders.clear();
				}
				boolean logout = intent.getBooleanExtra("isLogout", false);
				if(logout){
					showUnLogin();
				}else{
					ArrayList<Order> ordersList  = (ArrayList<Order>)intent.getSerializableExtra("orders");
					orders.addAll(ordersList);
					rl_loading.setVisibility(View.GONE);
					listAdapter.notifyDataSetChanged();
					mListView.setVisibility(View.VISIBLE);
					mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"加载完毕");
				}
			}
		}
	};

	@Override
	public void onDestroy() {
		mActivity.unregisterReceiver(mCountMsgReceiver);
		super.onDestroy();
	}
	
}
