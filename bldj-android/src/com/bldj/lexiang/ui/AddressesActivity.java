package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

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
public class AddressesActivity extends BaseActivity{

	ActionBar mActionBar;
	
	private ProgressBar progressBar;
	private ListView mListView;
	private AddressAdapter listAdapter;
	private List<Address> addresses;
	
	private int pageNumber = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.address);
		super.onCreate(savedInstanceState);
		
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
		
		addresses = new ArrayList<Address>();
		listAdapter = new AddressAdapter(AddressesActivity.this, addresses,handler);
		mListView.setAdapter(listAdapter);
		
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("常用地址");
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.setRightTextActionButton("添加", new OnClickListener() {

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
		progressBar = (ProgressBar)findViewById(R.id.progress_listView);
		mListView = (ListView)findViewById(R.id.jlys_listview);
	}

	@Override
	public void initListener() {
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
//				Intent intent = new Intent(AddressesActivity.this,AddressInfoActivity.class);
//				intent.putExtra("type", 1);
			}
			
		});
	}
	

	
		
	
	/**
	 * 获取地址列表数据
	 */
	private void getAddresses() {
		String userId = MyApplication.getInstance().getCurrentUser().getUserId();
		ApiUserUtils.addressManager(this.getApplicationContext(), 3, userId, "", "", "", 
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							 ToastUtils.showToast(AddressesActivity.this,parseModel.getMsg());
							 return;

						} else {
							List<Address> sellersList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Address>>() {
									});
							addresses.clear();
							addresses.addAll(sellersList);

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
			}
		}
		
	};
	
}
