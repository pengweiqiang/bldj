package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.UserTokenHandler;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.GroupAdapter;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.DeviceInfo;
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
	private int order_price = 0;// 价格排序
	private int user_type = 2;// 0个人1 团体 2所有

	private int pageNumber = 0;

	private PopupWindow popupWindow;
	private View view;
	private ListView lv_group;
	private List<TitleBarEnum> groups;

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
				buildTitleBar(v, 0);
			}
		});
		// 价格区间
		tv_orderPrice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				buildTitleBar(v, 1);
			}
		});
		// 团队
		tv_orderTeam.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				buildTitleBar(v, 2);
			}
		});
	}

	/**
	 * 获取养生服务数据
	 */
	private void getData() {
		ApiProductUtils.getProducts(mActivity.getApplicationContext(), "0", user_type,
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

	private void buildTitleBar(final View parent, final int index) {
		DeviceInfo.setContext(mActivity);

		groups = new ArrayList<TitleBarEnum>();
		switch (index) {
		case 0:
			groups.add(TitleBarEnum.ORDER_SALE);
			groups.add(TitleBarEnum.ORDER_TIME);
			groups.add(TitleBarEnum.ORDER_PRICE);
			break;
		case 1:
			groups.add(TitleBarEnum.PRICE_NONE);
			groups.add(TitleBarEnum.PRICE_ORDER1);
			groups.add(TitleBarEnum.PRICE_ORDER2);
			break;
		case 2:
			groups.add(TitleBarEnum.TYPE_ORDER_ALL);
			groups.add(TitleBarEnum.TYPE_ORDER_DOUBLE);
			groups.add(TitleBarEnum.TYPE_ORDER_MANY);
			groups.add(TitleBarEnum.TYPE_ORDER_SINGLE);
			break;
		}
		if (popupWindow == null) {
			view = LayoutInflater.from(mActivity).inflate(R.layout.group_list,
					null);
			lv_group = (ListView) view.findViewById(R.id.lvGroup);

			popupWindow = new PopupWindow(view,
					DeviceInfo.getScreenWidth() / 3, LayoutParams.WRAP_CONTENT,
					true);
		}

		GroupAdapter groupAdapter = new GroupAdapter(mActivity, groups, 1);
		lv_group.setAdapter(groupAdapter);

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		// WindowManager windowManager = (WindowManager)
		// this.getActivity().getSystemService(Context.WINDOW_SERVICE);
		// 计算x轴方向的偏移量，使得PopupWindow在Title的正下方显示，此处的单位是pixels
		// int xPos = DeviceInfo.getScreenWidth() / 3;

		// popupWindow.showAsDropDown(parent, xPos, 0);
		popupWindow.showAsDropDown(parent);
		popupWindow.update();

		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				((TextView) parent).setText(groups.get(position).getMsg());
				switch (index) {
				
				case 0://orderBytag
					orderByTag = groups.get(position).getValue();
					break;
				case 1://价格
					
					break;
				case 2://0个人1 团体 2所有
					user_type = groups.get(position).getValue();
					break;

				default:
					break;
				}
				pageNumber = 0;
				getData();
				
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

}
