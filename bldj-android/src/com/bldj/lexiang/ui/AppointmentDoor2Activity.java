package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.JlysHealthAdapter;
import com.bldj.lexiang.api.ApiSellerUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.constant.api.ApiConstants;
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

	RelativeLayout rl_loading;//进度条
	ImageView loading_ImageView;//加载动画
	RelativeLayout rl_loadingFail;//加载失败
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
	private TextView tv_callCustom;
	private LinearLayout ll_busy;

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
		
		tv_callCustom.setText(MyApplication.getInstance().getConfParams().getServiceNum());
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
		rl_loading = (RelativeLayout) findViewById(R.id.progress_listView);
		rl_loadingFail = (RelativeLayout) findViewById(R.id.loading_fail);
		loading_ImageView = (ImageView)findViewById(R.id.loading_imageView);
		mListView = (XListView) findViewById(R.id.listview);
		ll_busy = (LinearLayout) findViewById(R.id.ll_busy);
		ll_busy.setVisibility(View.GONE);

		sellers = new ArrayList<Seller>();
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("time", time);
		params.put("timeIndex", timeIndex);
		params.put("product", product);
		params.put("address", address);
		listAdapter = new JlysHealthAdapter(AppointmentDoor2Activity.this,
				sellers,params);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

		btn_previous = (Button) findViewById(R.id.btn_previous);
		btn_next = (Button) findViewById(R.id.btn_next);
		tv_callCustom = (TextView) findViewById(R.id.call_custom);
	}

	@Override
	public void initListener() {
		//点击重试
		rl_loadingFail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pageNumber = 0;
				getSellers();
			}
		});
		tv_callCustom.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//用intent启动拨打电话  
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+tv_callCustom.getText().toString()));  
                startActivity(intent);  
			}
		});
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
							"请选择推拿师");
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
//                    arg1.setBackgroundColor(getResources().getColor(R.color.selected_color));
                    mSelectedView = arg1;
                }else if(arg1 == mSelectedView){
//                	mSelectedView.setBackgroundColor(Color.TRANSPARENT);
                	mSeletedSeller = null;
                	mSelectedView = null;
                	listAdapter.setSelectedIndex(-1);
                	listAdapter.notifyDataSetChanged();
                	return;
                }else {
//                	mSelectedView.setBackgroundColor(Color.TRANSPARENT);
//                	arg1.setBackgroundColor(getResources().getColor(R.color.selected_color));
                	mSelectedView = arg1;
                }
				listAdapter.setSelectedIndex(position-1);
				mSeletedSeller = sellers.get(position-1);
				listAdapter.notifyDataSetChanged();
				// 启动美容师个人界面
//				Intent intent = new Intent(AppointmentDoor2Activity.this,
//						SellerPersonalActivity.class);
//				intent.putExtra("seller", mSeletedSeller);// 美容师
//				startActivity(intent);
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
	 * 获取美容师数据
	 */
	private void getSellers() {
		showLoading();
		ApiSellerUtils.getSellerByProIdAndDate(AppointmentDoor2Activity.this,product.getId(),time.substring(0,time.indexOf(" ")) ,pageNumber,ApiConstants.LIMIT, new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						rl_loading.setVisibility(View.GONE);
						
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							mListView.setVisibility(View.GONE);
							rl_loadingFail.setVisibility(View.VISIBLE);
//							 ToastUtils.showToast(AppointmentDoor2Activity.this,parseModel.getMsg());
							 return;

						} else {
							mListView.setVisibility(View.VISIBLE);
							List<Seller> sellersList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Seller>>() {
									});
							if (pageNumber == 0) {
								sellers.clear();
								if(sellersList == null || sellersList.isEmpty()){
									ll_busy.setVisibility(View.VISIBLE);
								}
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
