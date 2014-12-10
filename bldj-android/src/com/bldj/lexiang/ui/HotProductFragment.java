package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.CategoryAdapter;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.vo.Category;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;
/**
 * 热门推荐（分类）
 * @author will
 *
 */
public class HotProductFragment extends BaseFragment implements IXListViewListener{
	private View infoView;
	private ActionBar mActionBar;
	
	private ProgressBar progressBar;
	private XListView mListView;
	private CategoryAdapter listAdapter;
	private List<Category> categorys;
	
	private int pageNumber = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		infoView = inflater.inflate(R.layout.hot_fragment, container, false);
		
		
		initView();

		initListener();

		return infoView;
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		infoView.findViewById(R.id.actionBarLayout).setBackgroundColor(getResources().getColor(R.color.white));
		actionBar.setTitle("分类");
		actionBar.setTitleTextColor(R.color.app_title_color);
		actionBar.hideLeftActionButton();
		actionBar.hideRightActionButton();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		categorys = new ArrayList<Category>();
		listAdapter = new CategoryAdapter(mActivity, categorys);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		getCategory();
	}
	/**
	 * 初始化控件
	 */
	private void initView(){
		mActionBar = (ActionBar) infoView.findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		progressBar = (ProgressBar)infoView.findViewById(R.id.progress_listView);
		mListView = (XListView)infoView.findViewById(R.id.listview);
		
		
	}
	/**
	 * 事件初始化
	 */
	private void initListener(){
		
		
	}
	
	/**
	 * 获取数据
	 */
	private void getCategory() {
		ApiProductUtils.getCategory(mActivity, pageNumber, ApiConstants.LIMIT, new HttpConnectionUtil.RequestCallback() {

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
					List<Category> categoryList = JsonUtils.fromJson(
							parseModel.getData().toString(),
							new TypeToken<List<Category>>() {
							});
					if(pageNumber==0){
						categorys.clear();
					}
					categorys.addAll(categoryList);

					listAdapter.notifyDataSetChanged();
					mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"加载完毕");
				}

			}
		});
	}

	@Override
	public void onRefresh() {
		pageNumber=0;
		getCategory();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getCategory();
	}

	
}
