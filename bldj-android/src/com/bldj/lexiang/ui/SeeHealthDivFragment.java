package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

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
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.GroupAdapter;
import com.bldj.lexiang.adapter.JlysHealthAdapter;
import com.bldj.lexiang.api.ApiSellerUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.DeviceInfo;
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
public class SeeHealthDivFragment extends BaseFragment implements
		IXListViewListener {

	private ProgressBar progressBar;
	private View infoView;
	private XListView mListView;
	private JlysHealthAdapter listAdapter;
	private List<Seller> sellers;

	private TextView tv_order_count, tv_order_price, tv_order_work;

	private int pageNumber = 0;

	private PopupWindow popupWindow;
	private View view;
	private ListView lv_group;
	private List<TitleBarEnum> groups;

	// 筛选条件
	private int orderByTag = 0;
	private int startWorker;
	private int endWorker;
	private int startPrice;
	private int endPrice;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.see_health_div, container, false);
		ActionBar mActionBar = (ActionBar) getActivity().findViewById(
				R.id.actionBar);
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
	private void initView() {

		progressBar = (ProgressBar) infoView
				.findViewById(R.id.progress_listView);
		mListView = (XListView) infoView.findViewById(R.id.jlys_listview);

		tv_order_count = (TextView) infoView.findViewById(R.id.order_count);
		tv_order_price = (TextView) infoView.findViewById(R.id.order_price);
		tv_order_work = (TextView) infoView.findViewById(R.id.order_work);

	}

	/**
	 * 事件初始化
	 */
	private void initListener() {
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 启动美容师个人界面
				Intent intent = new Intent(mActivity,
						SellerPersonalActivity.class);
				intent.putExtra("seller", sellers.get(position - 1));
				startActivity(intent);
			}

		});
		// 接单次数
		tv_order_count.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View parent) {
				buildTitleBar(parent, 0);
			}
		});
		// 价格区间
		tv_order_price.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View parent) {
				buildTitleBar(parent, 1);
			}
		});
		// 工作年限
		tv_order_work.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View parent) {
				buildTitleBar(parent, 2);
			}
		});

	}

	/**
	 * 获取美容师数据
	 */
	private void getSellers() {
		ApiSellerUtils.getSellers(mActivity, pageNumber, ApiConstants.LIMIT,
				startPrice, endPrice, startWorker, endWorker, orderByTag,
				MyApplication.getInstance().lat,
				MyApplication.getInstance().lon,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(mActivity, parseModel.getMsg());
							sellers.clear();
							listAdapter.notifyDataSetChanged();
							return;

						} else {
							List<Seller> sellersList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Seller>>() {
									});
							if (pageNumber == 0) {
								sellers.clear();
							}
							sellers.addAll(sellersList);

							listAdapter.notifyDataSetChanged();
							mListView.onLoadFinish(pageNumber,
									sellers.size(), "亲，没有养生师");
						}

					}
				});
	}

	@Override
	public void onRefresh() {
		pageNumber = 0;
		getSellers();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getSellers();
	}

	private void buildTitleBar(final View parent, final int index) {
		DeviceInfo.setContext(mActivity);

		groups = new ArrayList<TitleBarEnum>();
		switch (index) {
		case 0:
			groups.add(TitleBarEnum.ORDER_DEFAULT);
			groups.add(TitleBarEnum.ORDER_TIME);
			groups.add(TitleBarEnum.ORDER_PRICE_SELLER);
			groups.add(TitleBarEnum.ORDER_DISTANCE);
			groups.add(TitleBarEnum.ORDER_COUNT);
			break;
		case 1:
//			groups.add(TitleBarEnum.PRICE_NONE);
			groups.add(TitleBarEnum.PRICE_ORDER0);
			groups.add(TitleBarEnum.PRICE_ORDER1);
			groups.add(TitleBarEnum.PRICE_ORDER2);
			groups.add(TitleBarEnum.PRICE_ORDER3);
			break;
		case 2:
			groups.add(TitleBarEnum.WORK_3_SMALL);
			groups.add(TitleBarEnum.WORK_3_5);
			groups.add(TitleBarEnum.WORK_5_10);
			groups.add(TitleBarEnum.WORK_10_BIG);
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
		popupWindow.showAsDropDown(parent, 0, 10);
		popupWindow.update();

		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				TitleBarEnum titleEnum = groups.get(position);
				((TextView) parent).setText(groups.get(position).getMsg());
				switch (index) {
				case 0:// 排序字段 0时间 3均价 4累计成交量 5距离
					orderByTag = titleEnum.getValue();
					break;
				case 1:// 价格区间
					startPrice = titleEnum.getStart();
					endPrice = titleEnum.getEnd();
					break;
				case 2:// 工作经验
					startWorker = titleEnum.getStart(); 
					endWorker = titleEnum.getEnd();
					break;

				default:
					break;
				}
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				pageNumber = 0;
				getSellers();
			}
		});
	}

}
