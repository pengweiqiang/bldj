package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.CouponsAdapter;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.Coupon;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;

/**
 * 已失效优惠卷
 * 
 * @author will
 * 
 */
public class FailureCouponsFragment extends BaseFragment implements IXListViewListener,OnClickListener{

	RelativeLayout rl_loading;//进度条
	ImageView loading_ImageView;//加载动画
	RelativeLayout rl_loadingFail;//加载失败
	private View infoView;
	private XListView mListView;
	private CouponsAdapter listAdapter;
	private List<Coupon> coupons;
	
	private int pageNumber = 0;
	View layoutEmpty;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.failure_coupons, container, false);
		
		initView();
		
		initListener();
		
		return infoView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		coupons = new ArrayList<Coupon>();
		listAdapter = new CouponsAdapter(mActivity, coupons);
//		mListView.setAdapter(listAdapter);
		setAlphaAdapter();
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		getCoupons();
	}

	/**
	 * 初始化控件
	 */
	private void initView(){
		
		rl_loading = (RelativeLayout) infoView.findViewById(R.id.progress_listView);
		rl_loadingFail = (RelativeLayout) infoView.findViewById(R.id.loading_fail);
		loading_ImageView = (ImageView)infoView.findViewById(R.id.loading_imageView);
		mListView = (XListView)infoView.findViewById(R.id.jlys_listview);
		layoutEmpty = infoView.findViewById(R.id.empty_layout);
		
	}
	/**
	 * 事件初始化
	 */
	private void initListener(){
		//点击重试
		rl_loadingFail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				pageNumber = 0;
				getCoupons();
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
	 * 获取失效优惠卷数据
	 */
	private void getCoupons() {
		showLoading();
		User user = MyApplication.getInstance().getCurrentUser();
		ApiBuyUtils.couponsManage(mActivity, user.getUserId(), 0, "", 3,pageNumber,ApiConstants.LIMIT,1,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						rl_loading.setVisibility(View.GONE);
						
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							mListView.setVisibility(View.GONE);
							rl_loadingFail.setVisibility(View.VISIBLE);
//							 ToastUtils.showToast(mActivity,parseModel.getMsg());
//							 mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"点击重试");
						} else {
							mListView.setVisibility(View.VISIBLE);
							List<Coupon> couponsList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Coupon>>() {
									});
							if(pageNumber==0){
								coupons.clear();
								if(couponsList==null || couponsList.isEmpty()){
									infoView.findViewById(R.id.un_empty).setVisibility(View.GONE);
									layoutEmpty.setVisibility(View.VISIBLE);
									showEmpty(layoutEmpty,R.string.empty_unfail_coupons_tip,0,R.drawable.empty_unuse_coupons,FailureCouponsFragment.this);
								}
							}
							coupons.addAll(couponsList);

							listAdapter.notifyDataSetChanged();
							mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"亲，你没有已失效优惠卷");
						}

					}
				});
	}

	@Override
	public void onRefresh() {
		pageNumber=0;
		getCoupons();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getCoupons();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	private void setAlphaAdapter() {
		AnimationAdapter animAdapter = new AlphaInAnimationAdapter(listAdapter);
		animAdapter.setAbsListView(mListView);
		mListView.setAdapter(animAdapter);
	}
	
}
