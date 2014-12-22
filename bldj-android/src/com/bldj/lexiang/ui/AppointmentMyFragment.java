package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.ListviewAdapter;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.utils.DateUtil;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.CustomerSpinner;
import com.bldj.lexiang.view.DateTimePickDialogUtil;

/**
 * 为自己预约
 * 
 * @author will
 * 
 */
public class AppointmentMyFragment extends BaseFragment implements
OnGetPoiSearchResultListener, OnGetSuggestionResultListener,
OnItemClickListener, OnGetGeoCoderResultListener{
	
	View infoView;
	private Button btn_next;

	
	EditText et_address;
	EditText btn_location;
	Button btn_time;
//	String address;//预约地点
	ListView locatioListView;
	ArrayList<String> locationList;
	ListviewAdapter listadapter;
	
	CustomerSpinner spinner;
	ArrayList<String> citys = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	
	
//	private String time;//预约时间
	private PoiSearch mPoiSearch = null;
	GeoCoder mSearch = null;//搜索模块，也可去掉地图模块独立使用
	private SuggestionSearch mSuggestionSearch = null;
	String city ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mPoiSearch = PoiSearch.newInstance();
//		mPoiSearch.setOnGetPoiSearchResultListener(this);
//		
//		mSearch = GeoCoder.newInstance();
//		mSearch.setOnGetGeoCodeResultListener(this);
//		mSuggestionSearch = SuggestionSearch.newInstance();
//		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);

		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.appointment_my, container, false);
		
		initView();
		
		initListener();
		
		return infoView;
	}

	/**
	 * 初始化控件
	 */
	private void initView(){
		btn_next = (Button)infoView.findViewById(R.id.btn_next);
		et_address = (EditText)infoView.findViewById(R.id.btn_address);
		btn_location = (EditText)infoView.findViewById(R.id.btn_location);
		btn_time = (Button) infoView.findViewById(R.id.btn_appoint_time);
		locatioListView = (ListView)infoView.findViewById(R.id.locations_list);
		btn_time.setTag(false);
		
		btn_location.setText(MyApplication.getInstance().addressStr);
		if (!StringUtils.isEmpty((String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "city", ""))) {
			city = (String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "city", "");
		}
		/**
		 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
		 */
		mSuggestionSearch
				.requestSuggestion((new SuggestionSearchOption())
						.keyword(btn_location.getText().toString()).city(city));
		
		//城市
//		spinner = (CustomerSpinner)infoView.findViewById(R.id.spinner_city);
//		citys.add(TitleBarEnum.CITY_BEIJING.getMsg());
//		citys.add(TitleBarEnum.CITY_GUANGZHOU.getMsg());
//		citys.add(TitleBarEnum.CITY_SHENZHEN.getMsg());
//	    spinner.setList(citys);
//	    adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, citys);
//	    spinner.setAdapter(adapter);
	    
	    //常用地址
//		mPoiSearch.searchInBound(new PoiBoundSearchOption())
		
	    locationList = new ArrayList<String>();
	    listadapter = new ListviewAdapter(mActivity,locationList,1);
	    locatioListView.setAdapter(listadapter);
	    locatioListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新
	}
	/**
	 * 事件初始化
	 */
	private void initListener(){
		
		btn_location.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable cs) {
				if (cs.length() <= 0) {
					return;
				}
				if (!StringUtils.isEmpty((String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "city", ""))) {
					city = (String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "city", "");
				}
//				mPoiSearch.searchNearby(new PoiNearbySearchOption().location(new LatLng(MyApplication.lat, MyApplication.lon)).keyword("小区").radius(200));
				/**
				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
				 */
				mSuggestionSearch
						.requestSuggestion((new SuggestionSearchOption())
								.keyword(cs.toString()).city(city));
			}
		});
		
		locatioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				listadapter.setCurrentItem(position);
				if(btn_location.getText().toString().equals((String)listadapter.getItem(position))){
					et_address.setText(btn_location.getText().toString());
				}else{
					et_address.setText(btn_location.getText().toString()+((String)listadapter.getItem(position)));
				}
			}
			
		});
		
		
		btn_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				time = btn_time.getText().toString();
//				if(!(Boolean)btn_time.getTag()){
//					btn_time.requestFocus();
//					ToastUtils.showToast(mActivity, "请选择预约时间");
//					return;
//				}
				String addressLocation = et_address.getText().toString();
				if(StringUtils.isEmpty(addressLocation)){
					et_address.requestFocus();
					ToastUtils.showToast(mActivity, "请输入预约地址");
					return;
				}
//				MyApplication.getInstance().appointMap.put("time", time);
				SharePreferenceManager.saveBatchSharedPreference(mActivity, Constant.FILE_NAME, "address",addressLocation);
				MyApplication.getInstance().appointMap.put("address", addressLocation);
				Intent intent = new Intent(mActivity,JLYSFragmentActivity.class);
				startActivity(intent);
			}
		});
		btn_location.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent(mActivity,MapLocationActivity.class);
//				startActivityForResult(intent, 123);
			}
		});
		btn_time.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
						mActivity, DateUtil.getDateString(new Date(), DateUtil.CUSTOM_PATTERN2));
				dateTimePicKDialog.dateTimePicKDialog(btn_time);
			}
		});
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetPoiResult(PoiResult arg0) {
		// TODO Auto-generated method stub
		List<PoiInfo> poiInfos =arg0.getAllPoi();
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
//			Toast.makeText(AddressActivity.this, "没有更多了~", Toast.LENGTH_SHORT)
//					.show();
			locationList.clear();
			locationList.add(btn_location.getText().toString());
			listadapter.notifyDataSetChanged();
			return;
		}
		locationList.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
//			Log.e("TAG", info.key);
			if (info != null) {
//				mAdapter.add(info);
				locationList.add(info.key);
			}
//			mAdapter.notifyDataSetChanged();// 默认聚焦最后一行
//			lvAddress.setSelection(mAdapter.getCount());
		}
		listadapter.notifyDataSetChanged();
	}

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if(resultCode == 20){
//			address = data.getStringExtra("address");
//			if(!StringUtils.isEmpty(address)){
//				btn_location.setText(address);
//			}
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
	
	
	
}
