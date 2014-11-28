package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.JlysHealthAdapter;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 上门预约2
 * 
 * @author will
 * 
 */
public class AppointmentDoor2Activity extends BaseActivity implements
		IXListViewListener {

	ActionBar mActionBar;

	private ProgressBar progressBar;
	private XListView mListView;
	private JlysHealthAdapter listAdapter;
	private List<Seller> sellers;
	private int pageNumber = 1;

	private Button btn_previous, btn_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.appointment_door2);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		initView();

		initListener();

		getSellers();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("上门预约");
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
		progressBar = (ProgressBar) findViewById(R.id.progress_listView);
		mListView = (XListView) findViewById(R.id.listview);

		sellers = new ArrayList<Seller>();
		listAdapter = new JlysHealthAdapter(AppointmentDoor2Activity.this,
				sellers);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

		btn_previous = (Button) findViewById(R.id.btn_previous);
		btn_next = (Button) findViewById(R.id.btn_next);
	}

	@Override
	public void initListener() {
		btn_previous.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppointmentDoor2Activity.this,
						AppointmentDoor1Activity.class);
				startActivity(intent);
			}
		});
		btn_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AppointmentDoor2Activity.this,
						AppointmentDoor3Activity.class);
				startActivity(intent);
			}
		});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// 启动美容师个人界面
				Intent intent = new Intent(AppointmentDoor2Activity.this,
						SellerPersonalActivity.class);
				// intent.putExtra("seller", products.get(position));
				startActivity(intent);
			}

		});
	}

	/**
	 * 获取收藏美容师数据
	 */
	private void getSellers() {
		ApiProductUtils.getProducts(AppointmentDoor2Activity.this, "1", 2, 0,
				0, pageNumber, ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							// ToastUtils.showToast(mActivity,
							// parseModel.getMsg());
							// return;
							List<Seller> sellersList = new ArrayList<Seller>();

							Seller p1 = new Seller();
							p1.setUsername("美容师" + (sellersList.size() + 1));
							p1.setAddress("四川");
							p1.setRecommend("共接单12次");
							p1.setAvgPrice("33");

							Seller p2 = new Seller();
							p2.setUsername("美容师" + (sellersList.size() + 2));
							p2.setAddress("北京");
							p2.setRecommend("共接单6次");
							p2.setAvgPrice("32");

							Seller p3 = new Seller();
							p3.setUsername("美容师" + (sellersList.size() + 3));
							p3.setAddress("上海");
							p3.setRecommend("共接单123次");
							p2.setAvgPrice("54");

							sellersList.add(p1);
							sellersList.add(p2);
							sellersList.add(p3);

							if (pageNumber == 1) {
								sellers.clear();
							}
							sellers.addAll(sellersList);

							listAdapter.notifyDataSetChanged();
							onLoad();

						} else {
							List<Seller> sellersList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Seller>>() {
									});
							if (pageNumber == 1) {
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
		pageNumber = 1;
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
		mListView.setRefreshTime(DateUtils.convert2String(
				System.currentTimeMillis(), ""));
	}

}
