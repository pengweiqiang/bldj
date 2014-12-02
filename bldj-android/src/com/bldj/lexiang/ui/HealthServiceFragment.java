package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 经络养生-->养生服务
 * 
 * @author will
 * 
 */
public class HealthServiceFragment extends BaseFragment implements
		IXListViewListener {

	private ProgressBar progressBar;
	private View infoView;
	private XListView mListView;
	private HomeAdapter listAdapter;
	private List<Product> products;
	private TextView tv_orderTime;
	private TextView tv_orderPrice;
	private TextView tv_orderTeam;

	private int orderByTag = 0;// 0时间 1价格 2销量

	private int pageNumber = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.health_service, container, false);

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

		getData();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		progressBar = (ProgressBar) infoView
				.findViewById(R.id.progress_listView);
		mListView = (XListView) infoView.findViewById(R.id.jlys_listview);

		tv_orderTime = (TextView) infoView.findViewById(R.id.order_time);
		tv_orderPrice = (TextView) infoView.findViewById(R.id.order_price);
		tv_orderTeam = (TextView) infoView.findViewById(R.id.order_team);

	}

	/**
	 * 事件初始化
	 */
	private void initListener() {
		// 时间排序
		tv_orderTime.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				orderByTag = 0;
			}
		});
		// 价格区间
		tv_orderPrice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				orderByTag = 1;
			}
		});
		// 团队
		tv_orderTeam.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				orderByTag = 2;
			}
		});
	}

	/**
	 * 获取养生服务数据
	 */
	private void getData() {
		ApiProductUtils.getProducts(mActivity.getApplicationContext(), "0", 2,
				orderByTag, 2, pageNumber, ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(mActivity, parseModel.getMsg());
							return;

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
