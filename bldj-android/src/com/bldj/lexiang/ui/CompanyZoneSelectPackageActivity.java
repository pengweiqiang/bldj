package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bldj.gson.reflect.TypeToken;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.CheepCardsAdapter;
import com.bldj.lexiang.api.ApiHomeUtils;
import com.bldj.lexiang.api.vo.CheepCards;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.XListView;
import com.bldj.lexiang.view.XListView.IXListViewListener;

/**
 * 企业专区-->选择套餐
 * 
 * @author will
 * 
 */
public class CompanyZoneSelectPackageActivity extends BaseActivity implements
		OnClickListener,IXListViewListener {

	ActionBar mActionBar;

	Button btn_next;
	private TextView tv_select_package_title;
//	LinearLayout layout_package1;
//	View layout_package2;
//	View layout_package3;
//	View layout_package4;

//	TextView tv_package1_title;
//	TextView tvpackage2_info1,tvpackage2_info2,tvpackage2_info3;
//	TextView tvpackage_3_info1,tvpackage_3_info2,tvpackage_3_info3;
//	TextView tvpackage_4_info1,tvpackage_4_info2,tvpackage_4_info3;
//	LoadingDialog loading;
	
	int pageNumber = 0;
	private XListView mListView;
	RelativeLayout rl_loading;//进度条
	ImageView loading_ImageView;//加载动画
	RelativeLayout rl_loadingFail;//加载失败
	List<CheepCards> cheepCardList;
	CheepCardsAdapter listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.company_zone_select_package);
		super.onCreate(savedInstanceState);
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);

		cheepCardList = new ArrayList<CheepCards>();
		listAdapter = new CheepCardsAdapter(this, cheepCardList);
		mListView.setAdapter(listAdapter);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		initData();
		getData();
		
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("优惠特区");
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
//		btn_next = (Button) findViewById(R.id.btn_next);

//		layout_package1 = (LinearLayout) findViewById(R.id.package1);
//		layout_package2 = findViewById(R.id.package2);
//		layout_package3 = findViewById(R.id.package3);
//		layout_package4 = findViewById(R.id.package4);
//		tv_package1_title = (TextView) findViewById(R.id.package1_title);
//		tvpackage2_info1 = (TextView) findViewById(R.id.package2_info1);
//		tvpackage2_info2 = (TextView) findViewById(R.id.package2_info2);
//		tvpackage2_info3 = (TextView) findViewById(R.id.package2_info3);
//		tvpackage_3_info1 = (TextView) findViewById(R.id.package_3_info1);
//		tvpackage_3_info2 = (TextView) findViewById(R.id.package_3_info2);
//		tvpackage_3_info3 = (TextView) findViewById(R.id.package_3_info3);
//		tvpackage_4_info1 = (TextView) findViewById(R.id.package_4_info_1);
//		tvpackage_4_info2 = (TextView) findViewById(R.id.package_4_info_2);
//		tvpackage_4_info3 = (TextView) findViewById(R.id.package_4_info_3);
		
		rl_loading = (RelativeLayout) findViewById(R.id.progress_listView);
		rl_loadingFail = (RelativeLayout) findViewById(R.id.loading_fail);
		loading_ImageView = (ImageView)findViewById(R.id.loading_imageView);
		mListView = (XListView)findViewById(R.id.listview);
		tv_select_package_title = (TextView)findViewById(R.id.select_package_title);
	}

	private void initData() {
		tv_select_package_title.setText(MyApplication.getInstance().getConfParams().getPreferential());
		// 企业0元体验中的0字体变红
//		setIndexTextColor(tv_package1_title,R.color.color_package1_title);
//		// 企业0元体验中的0字体变红
//		setIndexTextColor(tvpackage2_info1,R.color.line_company_package2);
//		setIndexTextColor(tvpackage2_info2,R.color.line_company_package2);
//		setIndexTextColor(tvpackage2_info3,R.color.line_company_package2);
//		
//		setIndexTextColor(tvpackage_3_info1,R.color.line_company_package3);
//		setIndexTextColor(tvpackage_3_info2,R.color.line_company_package3);
//		setIndexTextColor(tvpackage_3_info3,R.color.line_company_package3);
//		
//		setIndexTextColor(tvpackage_4_info1,R.color.line_company_package4);
//		setIndexTextColor(tvpackage_4_info2,R.color.line_company_package4);
//		setIndexTextColor(tvpackage_4_info3,R.color.line_company_package4);

	}
	
	private void setIndexTextColor(TextView tv,int colorId){
		String str = tv.getText().toString();
		Pattern p = Pattern.compile("[0-9]");
		Matcher m = p.matcher(str.substring(1));
		SpannableStringBuilder style = new SpannableStringBuilder(
				str);
		while(m.find()){
			int index = m.start()+1;
			style.setSpan(
					new ForegroundColorSpan(getResources().getColor(
							colorId)), index, index+1,
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		tv.setText(style);
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
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if (MyApplication.getInstance().getCurrentUser() == null) {
					Intent intent = new Intent(CompanyZoneSelectPackageActivity.this,
							RegisterAndLoginActivity.class);
					startActivity(intent);
					return;
				}
				CheepCards cheepCard = (CheepCards)listAdapter.getItem(position-1);
				Intent intent = new Intent(CompanyZoneSelectPackageActivity.this,CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", position);
				intent.putExtra("serviceTypeName",cheepCard.getName());
				intent.putExtra("price", cheepCard.getPrice());
				startActivity(intent);
			}
			
		});
		/*layout_package1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						CompanyZoneSelectPackageActivity.this,
						CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 0);
				intent.putExtra("serviceTypeName",
						getResources().getString(R.string.package_1));
				startActivity(intent);
			}
		});
		layout_package2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						CompanyZoneSelectPackageActivity.this,
						CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 1);
				intent.putExtra("serviceTypeName", "2万元"
						+ getResources().getString(R.string.package_service));
				startActivity(intent);
			}
		});
		layout_package3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						CompanyZoneSelectPackageActivity.this,
						CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 2);
				intent.putExtra("serviceTypeName", "5万元"
						+ getResources().getString(R.string.package_service));
				startActivity(intent);
			}
		});
		layout_package4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(
						CompanyZoneSelectPackageActivity.this,
						CompanyZoneActivity.class);
				intent.putExtra("serviceTypeIndex", 3);
				intent.putExtra("serviceTypeName", "10万元"
						+ getResources().getString(R.string.package_service));
				startActivity(intent);
			}
		});*/
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
	 * 获取优惠特区列表
	 */
	private void getData(){
//		loading = new LoadingDialog(mContext);
//		loading.show();
		showLoading();
		ApiHomeUtils.getCheapCards(mContext, pageNumber, ApiConstants.LIMIT, new HttpConnectionUtil.RequestCallback() {
			
			@Override
			public void execute(ParseModel parseModel) {
				rl_loading.setVisibility(View.GONE);
				
				if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
						.getStatus())) {
					mListView.setVisibility(View.GONE);
					rl_loadingFail.setVisibility(View.VISIBLE);
//					 ToastUtils.showToast(CompanyZoneSelectPackageActivity.this,parseModel.getMsg());
//					 mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"点击重试");
				} else {
					mListView.setVisibility(View.VISIBLE);
					List<CheepCards> cheepCardNewList = JsonUtils.fromJson(
							parseModel.getData().toString(),
							new TypeToken<List<CheepCards>>() {
							});
					if(pageNumber==0){
						cheepCardList.clear();
					}
					cheepCardList.addAll(cheepCardNewList);

					listAdapter.notifyDataSetChanged();
					mListView.onLoadFinish(pageNumber,listAdapter.getCount(),"");
				}
			}
		});
	}

	@Override
	public void onRefresh() {
		pageNumber=0;
		getData();
	}

	@Override
	public void onLoadMore() {
		pageNumber++;
		getData();
	}

}
