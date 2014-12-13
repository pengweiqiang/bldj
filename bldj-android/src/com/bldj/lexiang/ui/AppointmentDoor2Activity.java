package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.JlysHealthAdapter;
import com.bldj.lexiang.api.ApiSellerUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
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
	private int pageNumber = 0;
	private String time;// 预约时间
	private int timeIndex;
	private String address;
	private Product product;
	private Seller mSeletedSeller;// 预约美容师
	private View mSelectedView;
	private Button btn_previous, btn_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.appointment_door2);
		super.onCreate(savedInstanceState);
		time = this.getIntent().getStringExtra("time");
		timeIndex = this.getIntent().getIntExtra("timeIndex", 0);
		product = (Product)this.getIntent().getSerializableExtra("product");
		address = this.getIntent().getStringExtra("address");
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		initView();

		initListener();

		getSellers();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("上门预约");
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
				if (mSeletedSeller == null) {
					ToastUtils.showToast(AppointmentDoor2Activity.this,
							"请选择理疗师");
					return;
				}
				Intent intent = new Intent(AppointmentDoor2Activity.this,
						AppointmentDoor3Activity.class);
				intent.putExtra("time", time);// 预约时间
				intent.putExtra("timeIndex", timeIndex);// 预约时间位置
				intent.putExtra("seller", mSeletedSeller);// 预约美容师
				intent.putExtra("product", product);//预约产品
				intent.putExtra("address", address);//详细地址
				startActivity(intent);
			}
		});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				if (mSelectedView == null) {
                    arg1.setBackgroundColor(getResources().getColor(R.color.selected_color));
                    mSelectedView = arg1;
                } else {
                	mSelectedView.setBackgroundColor(Color.TRANSPARENT);
                	arg1.setBackgroundColor(getResources().getColor(R.color.selected_color));
                    mSelectedView = arg1;
                }
				mSeletedSeller = sellers.get(position-1);
				// 启动美容师个人界面
//				Intent intent = new Intent(AppointmentDoor2Activity.this,
//						SellerPersonalActivity.class);
//				intent.putExtra("seller", mSeletedSeller);// 美容师
//				startActivity(intent);
			}

		});
	}

	/**
	 * 获取美容师数据
	 */
	private void getSellers() {
		ApiSellerUtils.getSellers(AppointmentDoor2Activity.this, pageNumber, ApiConstants.LIMIT, 0, 0, 
				1, 5, 0,0,0, new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							 ToastUtils.showToast(AppointmentDoor2Activity.this,parseModel.getMsg());
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
							mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"加载完毕");
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

}
