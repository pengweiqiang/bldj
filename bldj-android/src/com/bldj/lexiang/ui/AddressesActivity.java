package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.AddressAdapter;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.Address;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

/**
 * 常用地址
 * 
 * @author will
 * 
 */
public class AddressesActivity extends BaseActivity implements OnClickListener{

	ActionBar mActionBar;
	
	RelativeLayout rl_loading;//进度条
	ImageView loading_ImageView;//加载动画
	RelativeLayout rl_loadingFail;//加载失败
	private ListView mListView;
	private AddressAdapter listAdapter;
	private List<Address> addresses;
	
	private int pageNumber = 0;

	private int type;//1-从上门预约界面跳入
	
	View layoutEmpty;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.address);
		super.onCreate(savedInstanceState);
		type = this.getIntent().getIntExtra("type", 0);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
		
		addresses = new ArrayList<Address>();
		listAdapter = new AddressAdapter(AddressesActivity.this, addresses,handler);
		mListView.setAdapter(listAdapter);
		
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("常用地址");
		actionBar.setLeftActionButton(R.drawable.btn_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.setRightTextActionButton("", R.drawable.address_add, true, new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AddressesActivity.this,
						AddressInfoActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void initView() {
		rl_loading = (RelativeLayout) findViewById(R.id.progress_listView);
		rl_loadingFail = (RelativeLayout) findViewById(R.id.loading_fail);
		loading_ImageView = (ImageView)findViewById(R.id.loading_imageView);
		mListView = (ListView)findViewById(R.id.jlys_listview);
		layoutEmpty = findViewById(R.id.empty_layout);
	}

	@Override
	public void initListener() {
		//点击重试
		rl_loadingFail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pageNumber = 0;
				getAddresses();
			}
		});
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
//				Intent intent = new Intent(AddressesActivity.this,AddressInfoActivity.class);
//				intent.putExtra("type", 1);
				Address address = addresses.get(position);
				if(type ==1 ){
		            Intent data=new Intent();  
		            data.putExtra("address", address.getDetailAddress());  
		            //回到上门预约界面
		            setResult(20, data);  
		            finish();  
				}
				
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
	 * 获取地址列表数据
	 */
	private void getAddresses() {
		showLoading();
		long userId = MyApplication.getInstance().getCurrentUser().getUserId();
		ApiUserUtils.addressManager(this.getApplicationContext(), 3, userId, "", "", "", 
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						rl_loading.setVisibility(View.GONE);
						
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							mListView.setVisibility(View.GONE);
							rl_loadingFail.setVisibility(View.VISIBLE);
//							 ToastUtils.showToast(AddressesActivity.this,parseModel.getMsg());
							 return;

						} else {
							if(layoutEmpty.getVisibility() == View.VISIBLE){
								findViewById(R.id.un_empty).setVisibility(View.VISIBLE);
								layoutEmpty.setVisibility(View.GONE);
							}
							mListView.setVisibility(View.VISIBLE);
							List<Address> addressList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Address>>() {
									});
							if(addressList==null || addressList.isEmpty()){
								showEmptyLayout();
							}
							addresses.clear();
							addresses.addAll(addressList);

							listAdapter.notifyDataSetChanged();
						}

					}
				});
	}

	@Override
	protected void onResume() {
		super.onResume();
		getAddresses();
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what == 1){
				addresses.remove(msg.arg1);
				listAdapter.notifyDataSetChanged();
				if(listAdapter.getCount()==0){
					showEmptyLayout();
				}
			}
		}
		
	};
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = new Intent(AddressesActivity.this,
				AddressInfoActivity.class);
		startActivity(intent);
	}
	
	private void showEmptyLayout(){
		findViewById(R.id.un_empty).setVisibility(View.GONE);
		layoutEmpty.setVisibility(View.VISIBLE);
		showEmpty(layoutEmpty,R.string.empty_address_tip,R.string.empty_address_go,R.drawable.empty_address,AddressesActivity.this);
	}
	
	
	
}
