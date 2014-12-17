package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.adapter.MyFileAdapter;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.MyFiles;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 我的档案
 * 
 * @author will
 * 
 */
public class MyFilesActivity extends BaseActivity implements
IXListViewListener {

	ActionBar mActionBar;
	
	
	private ProgressBar progressBar;
	private XListView mListView;
	private MyFileAdapter listAdapter;
	private List<MyFiles> myFiles;

	private int pageNumber = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.my_files);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
		
		myFiles = new ArrayList<MyFiles>();
		listAdapter = new MyFileAdapter(this, myFiles);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		getData();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("我的档案");
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
		progressBar = (ProgressBar) 
				findViewById(R.id.progress_listView);
		mListView = (XListView) findViewById(R.id.listview);
	}

	@Override
	public void initListener() {
		
	}
	
	/**
	 * 获取养生服务数据
	 */
	private void getData() {
		User user = MyApplication.getInstance().getCurrentUser();
		String dealDate = "";
		ApiUserUtils.getMyFiles(MyFilesActivity.this,user.getUserId(), ApiConstants.LIMIT,
				dealDate,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							 ToastUtils.showToast(MyFilesActivity.this,
							 parseModel.getMsg());
							 return;
							
						} else {
							List<MyFiles> myFilesList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<MyFiles>>() {
									});

//							if (pageNumber == 0) {
//								myFilesList.clear();
//							}
							myFiles.addAll(myFilesList);

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
