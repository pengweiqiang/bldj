package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.CouponsAdapter;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.adapter.ScoreAdapter;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.ApiSellerUtils;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.Coupon;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.SellerScores;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil.RequestCallback;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.CustomViewPager;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 美容师信息概览
 * 
 * @author will
 * 
 */
public class SellerInfoFragment extends BaseFragment implements IXListViewListener{

	
	
	private ProgressBar progressBar;
	private View infoView;
	private XListView mListView;
	private ScoreAdapter listAdapter;
	private List<SellerScores> scores;
	private TextView tv_recommend;
	
	private int pageNumber = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.seller_info, container, false);
		
		initView();
		initData();
		initListener();
		
		return infoView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		scores = new ArrayList<SellerScores>();
		listAdapter = new ScoreAdapter(mActivity, scores);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		getScores();
	}

	/**
	 * 初始化控件
	 */
	private void initView(){
		
		progressBar = (ProgressBar)infoView.findViewById(R.id.progress_listView);
		mListView = (XListView)infoView.findViewById(R.id.listview);
		tv_recommend = (TextView)infoView.findViewById(R.id.person_recommend);
		
		
	}
	private void initData(){
		if(((SellerPersonalActivity)mActivity).getSellerVo() != null && !StringUtils.isEmpty(((SellerPersonalActivity)mActivity).getSellerVo().getRecommend())){
			tv_recommend.setText("\t\t"+((SellerPersonalActivity)mActivity).getSellerVo().getRecommend());
		}else{
			tv_recommend.setText("无");
		}
	}
	/**
	 * 事件初始化
	 */
	private void initListener(){
		
		
	}
	
	/**
	 * 获取用户评价
	 */
	private void getScores() {
		if(((SellerPersonalActivity)mActivity).getSellerVo() == null){
			
			return ;
		}
		long sellerId = ((SellerPersonalActivity)mActivity).getSellerVo().getId();
		ApiSellerUtils.getSellerScores(mActivity, sellerId,pageNumber,ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							 ToastUtils.showToast(mActivity,
							 parseModel.getMsg());
							 return;

						} else {
							List<SellerScores> scoresList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<SellerScores>>() {
									});
							if(pageNumber==0){
								scores.clear();
							}
							scores.addAll(scoresList);

							listAdapter.notifyDataSetChanged();
							mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"加载完毕");
						}

					}
				});
	}

	@Override
	public void onRefresh() {
		pageNumber=0;
		getScores();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getScores();
	}
}
