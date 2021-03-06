package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.MyFileAdapter;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.MyFiles;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.listener.EmptyClickListener;
import com.bldj.lexiang.utils.DateUtil;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
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
IXListViewListener,OnClickListener {

	ActionBar mActionBar;
	
	
	RelativeLayout rl_loading;//进度条
	ImageView loading_ImageView;//加载动画
	RelativeLayout rl_loadingFail;//加载失败
	private XListView mListView;
	private MyFileAdapter listAdapter;
	private List<MyFiles> myFiles;

	private int pageNumber = 0;
	String dealDate = DateUtil.getDateString(new Date(), DateUtil.CUSTOM_PATTERN_SCHEDULED);
	
	View layoutEmpty;
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
		rl_loading = (RelativeLayout) findViewById(R.id.progress_listView);
		rl_loadingFail = (RelativeLayout) findViewById(R.id.loading_fail);
		loading_ImageView = (ImageView)findViewById(R.id.loading_imageView);
		mListView = (XListView) findViewById(R.id.listview);
		layoutEmpty = findViewById(R.id.empty_layout);
	}

	@Override
	public void initListener() {
		//点击重试
		rl_loadingFail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pageNumber = 0;
				getData();
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
	 * 获取养生服务数据
	 */
	private void getData() {
		showLoading();
		User user = MyApplication.getInstance().getCurrentUser();
		ApiUserUtils.getMyFiles(MyFilesActivity.this,user.getUserId(), 5,
				dealDate,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						rl_loading.setVisibility(View.GONE);
						
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							mListView.setVisibility(View.GONE);
							rl_loadingFail.setVisibility(View.VISIBLE);
//							 ToastUtils.showToast(MyFilesActivity.this,
//							 parseModel.getMsg());
//							 mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"点击重试");
						} else {
							mListView.setVisibility(View.VISIBLE);
							List<MyFiles> myFilesList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<MyFiles>>() {
									});

							if (pageNumber == 0) {
								myFiles.clear();
								if(myFilesList==null || myFilesList.isEmpty()){
									findViewById(R.id.un_empty).setVisibility(View.GONE);
									layoutEmpty.setVisibility(View.VISIBLE);
									showEmpty(layoutEmpty,R.string.empty_file_tip,R.string.empty_file_go,R.drawable.empty_myfile,MyFilesActivity.this);
								}
							}
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
		dealDate = DateUtil.getDateString(new Date(), DateUtil.CUSTOM_PATTERN_SCHEDULED);
		getData();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		dealDate = DateUtils.getDateAfterSomeDay(dealDate, -5);//?????日期从哪里选择，还有分页数据
		getData();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}

	


}
