package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.UserTokenHandler;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.GroupAdapter;
import com.bldj.lexiang.adapter.HomeAdapter;
import com.bldj.lexiang.api.ApiProductUtils;
import com.bldj.lexiang.api.ApiSellerUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.utils.DateUtils;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 美容师服务项目
 * 
 * @author will
 * 
 */
public class SellerServiceFragment extends BaseFragment implements
		IXListViewListener {

	private ProgressBar progressBar;
	private View infoView;
	private XListView mListView;
	private HomeAdapter listAdapter;
	private List<Product> products;


	private int pageNumber = 0;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.seller_service, container, false);

		initView();

		initListener();

		return infoView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		products = new ArrayList<Product>();
		listAdapter = new HomeAdapter(mActivity, products);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);

		getData();
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		progressBar = (ProgressBar) infoView
				.findViewById(R.id.progress_listView);
		mListView = (XListView) infoView.findViewById(R.id.listview);


	}

	/**
	 * 事件初始化
	 */
	private void initListener() {
	}

	/**
	 * 获取养生服务数据
	 */
	private void getData() {
		if(((SellerPersonalActivity)mActivity).getSellerVo() == null){
			
			return ;
		}
		long sellerId = ((SellerPersonalActivity)mActivity).getSellerVo().getId();
		ApiSellerUtils.getSellerProduct(mActivity.getApplicationContext(), sellerId, pageNumber, ApiConstants.LIMIT,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						progressBar.setVisibility(View.GONE);
						mListView.setVisibility(View.VISIBLE);
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(mActivity, parseModel.getMsg());
							return;

						} else {
							List<Product> productsList = JsonUtils.fromJson(
									parseModel.getData().toString(),
									new TypeToken<List<Product>>() {
									});

							if (pageNumber == 0) {
								products.clear();
							}
							products.addAll(productsList);

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
