package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
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
import com.bldj.lexiang.view.SpringScrollView;

/**
 * 为自己预约
 * 
 * @author will
 * 
 */
public class AppointmentMyFragment extends BaseFragment implements
 OnGetSuggestionResultListener{
	
	View infoView;
	private Button btn_next;

	
	EditText et_address;
	EditText btn_location;
	Button btn_time;
	LinearLayout layout;
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
	
	InputMethodManager manager ;
	ScrollView scrollView;
	
	private View selectedView;//选中的地址
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
		
//		mSearch = GeoCoder.newInstance();
//		mSearch.setOnGetGeoCodeResultListener(this);

//		mPoiSearch = PoiSearch.newInstance();
//		mPoiSearch.setOnGetPoiSearchResultListener(this);
		if (!StringUtils.isEmpty((String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "city", ""))) {
			city = (String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "city", "");
		}
		/**
		 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
		 */
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		mSuggestionSearch
				.requestSuggestion((new SuggestionSearchOption())
						.keyword(MyApplication.getInstance().street).city(city));
	
		manager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE); 
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.appointment_my, container, false);
		
		initView();
		
		initListener();
		
		return infoView;
	}
	
	public void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();  
        if (listAdapter == null) {  
            return;  
        }  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();  
        }  
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight  
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))  
                + 15;  
        listView.setLayoutParams(params);  
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
		layout = (LinearLayout)infoView.findViewById(R.id.layout);
		scrollView = (ScrollView)infoView.findViewById(R.id.scrollView);
		btn_time.setTag(false);
		
		btn_location.setText(MyApplication.getInstance().addressStr);
		
		
		String address = (String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "address", "");
		if(!StringUtils.isEmpty(address)){
			et_address.setText(address);
		}
		
		
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
		scrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				 if(event.getAction() == MotionEvent.ACTION_DOWN){  
				     if(mActivity.getCurrentFocus()!=null && mActivity.getCurrentFocus().getWindowToken()!=null){  
				       manager.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);  
				     }  
				  }  
				  return false;
			}
		});
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
//				listadapter.setCurrentItem(position);
//				if(btn_location.getText().toString().equals((String)listadapter.getItem(position))){
//					et_address.setText(btn_location.getText().toString());
//				}else if(et_address.getText().toString().equals(btn_location.getText().toString()+(String)listadapter.getItem(position))){
//					et_address.setText("");
//				}else {
//					et_address.setText(btn_location.getText().toString()+((String)listadapter.getItem(position)));
//				}
				
				if (selectedView == null) {
		            view.setBackgroundColor(getResources().getColor(R.color.app_bg_color));
		            ((TextView)view.findViewById(R.id.itemText)).setTextColor(Color.WHITE);
		            selectedView = view; 
		            if(btn_location.getText().toString().equals((String)listadapter.getItem(position))){
		            	et_address.setText(btn_location.getText().toString());
		            }else{
		            	et_address.setText(btn_location.getText().toString()+((String)listadapter.getItem(position)));
		            }
		        }else if(selectedView == view){//点击第二次取消
		        	et_address.setText("");
					view.setBackgroundColor(Color.TRANSPARENT);
					((TextView)view.findViewById(R.id.itemText)).setTextColor(getResources().getColor(R.color.grey));
					selectedView = null;
		        }
				else {
					((TextView)selectedView.findViewById(R.id.itemText)).setTextColor(getResources().getColor(R.color.grey));
					selectedView.setBackgroundColor(Color.TRANSPARENT);
		            view.setBackgroundColor(getResources().getColor(R.color.app_bg_color));
		            ((TextView)view.findViewById(R.id.itemText)).setTextColor(Color.WHITE);
		            selectedView = view;
		            if(btn_location.getText().toString().equals((String)listadapter.getItem(position))){
		            	et_address.setText(btn_location.getText().toString());
		            }else{
		            	et_address.setText(btn_location.getText().toString()+((String)listadapter.getItem(position)));
		            }
		        }
				
//				if(btn_location.getText().toString().equals((String)listadapter.getItem(position))){
//					et_address.setText(btn_location.getText().toString());
//					if(selectedView!=null){
//						selectedView.setBackgroundColor(Color.TRANSPARENT);
//					}
//					selectedView = view;
//					view.setBackgroundColor(mActivity.getResources().getColor(R.color.app_bg_color));
//					((TextView)view.findViewById(R.id.itemText)).setTextColor(Color.WHITE);
//				}else if(et_address.getText().toString().equals(btn_location.getText().toString()+(String)listadapter.getItem(position))){
//					et_address.setText("");
//					((TextView)view.findViewById(R.id.itemText)).setTextColor(getResources().getColor(R.color.grey));
//					view.setBackgroundColor(Color.TRANSPARENT);
//				}else{
//					view.setBackgroundColor(mActivity.getResources().getColor(R.color.app_bg_color));
//					((TextView)view.findViewById(R.id.itemText)).setTextColor(Color.WHITE);
//					et_address.setText(btn_location.getText().toString()+((String)listadapter.getItem(position)));
//					selectedView = view;
//				}
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
			if (info != null) {
//				mAdapter.add(info);
				if(!locationList.contains(info.key)){
					locationList.add(info.key);
				}
			}
//			mAdapter.notifyDataSetChanged();// 默认聚焦最后一行
//			lvAddress.setSelection(mAdapter.getCount());
		}
		listadapter.notifyDataSetChanged();
//		setListViewHeightBasedOnChildren(locatioListView);
	}

	@Override
	public void onResume() {
		super.onResume();
		String address = (String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "address", "");
		if(!StringUtils.isEmpty(address)){
			et_address.setText(address);
		}
	}

	@Override
	public void onDestroy() {
		mSuggestionSearch.destroy();
		super.onDestroy();
	}
	

	
	
}
