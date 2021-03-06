package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

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
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.utils.DateUtil;
import com.bldj.lexiang.utils.PatternUtils;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.CustomerSpinner;
import com.bldj.lexiang.view.DateTimePickDialogUtil;
import com.bldj.lexiang.view.SpringScrollView;

/**
 * 为他人预约
 * 
 * @author will
 * 
 */
public class AppointmentOtherFragment extends BaseFragment {

	View infoView;
	private TextView btn_contact;
	private Button btn_next;
	private EditText et_contactName;
	private EditText et_contactPhone;
	private Button btn_time;
	private TextView tv_address_detail;
	private TextView btn_city;
	private EditText btn_location;
	private EditText et_address;
//	ListView locatioListView;
//	ArrayList<String> locationList;
//	ListviewAdapter listadapter;
//	String address;//预约地点
	String time ;//预约时间
	CustomerSpinner spinner;
	ArrayList<String> citys = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	
	
	
	LinearLayout layout;
	InputMethodManager manager ;
	ScrollView scrollView;
	
	View selectedView;//选中的地址列表
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		manager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE); 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		infoView = inflater.inflate(R.layout.appointment_other, container,
				false);

		initView();

		initListener();

		return infoView;
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		btn_contact = (TextView) infoView.findViewById(R.id.contacts);
		btn_next = (Button) infoView.findViewById(R.id.btn_next);
		et_contactName = (EditText) infoView.findViewById(R.id.contact_name);
		et_contactPhone = (EditText) infoView.findViewById(R.id.contact_phone);
		tv_address_detail = (TextView) infoView
				.findViewById(R.id.contact_address);
		btn_time = (Button) infoView.findViewById(R.id.btn_appoint_time);
		btn_city = (Button) infoView.findViewById(R.id.btn_city);
		btn_location = (EditText) infoView.findViewById(R.id.btn_location);
		et_address = (EditText) infoView.findViewById(R.id.et_address);
//		locatioListView = (ListView)infoView.findViewById(R.id.locations_list);
		layout = (LinearLayout)infoView.findViewById(R.id.layout);
		scrollView = (ScrollView)infoView.findViewById(R.id.scrollView);
		btn_time.setTag(false);
		
		
		btn_location.setText(MyApplication.getInstance().addressStr);
	
		
		String address = (String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "address", "");
		if(!StringUtils.isEmpty(address)){
			et_address.setText(address);
		}
		
//		spinner = (CustomerSpinner)infoView.findViewById(R.id.spinner_city);
//		citys.add(TitleBarEnum.CITY_BEIJING.getMsg());
//		citys.add(TitleBarEnum.CITY_GUANGZHOU.getMsg());
//		citys.add(TitleBarEnum.CITY_SHENZHEN.getMsg());
//	    spinner.setList(citys);
//	    adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, citys);
//	    spinner.setAdapter(adapter);
	    
	    
	  //常用地址
//	    locationList = new ArrayList<String>();
//	    listadapter = new ListviewAdapter(mActivity,locationList,1);
//	    locatioListView.setAdapter(listadapter);
//	    locatioListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新

	}

	/**
	 * 事件初始化
	 */
	private void initListener() {
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
//				if (!StringUtils.isEmpty((String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "city", ""))) {
//					city = (String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "city", "");
//				}
////				mPoiSearch.searchNearby(new PoiNearbySearchOption().location(new LatLng(MyApplication.lat, MyApplication.lon)).keyword("小区").radius(200));
//				/**
//				 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
//				 */
//				mSuggestionSearch
//						.requestSuggestion((new SuggestionSearchOption())
//								.keyword(cs.toString()).city(city));
				
			}
		});
//		locatioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View view, int position,
//					long arg3) {
////				listadapter.setCurrentItem(position);
//				
//				if (selectedView == null) {
//		            view.setBackgroundColor(getResources().getColor(R.color.app_bg_color));
//		            ((TextView)view.findViewById(R.id.itemText)).setTextColor(Color.WHITE);
//		            selectedView = view; 
//		            if(btn_location.getText().toString().equals((String)listadapter.getItem(position))){
//		            	et_address.setText(btn_location.getText().toString());
//		            }else{
//		            	et_address.setText(btn_location.getText().toString()+((String)listadapter.getItem(position)));
//		            }
//		        }else if(selectedView == view){//点击第二次取消
//		        	et_address.setText("");
//					((TextView)view.findViewById(R.id.itemText)).setTextColor(getResources().getColor(R.color.grey));
//					view.setBackgroundColor(Color.TRANSPARENT);
//					selectedView = null;
//		        }
//				else {
//					((TextView)selectedView.findViewById(R.id.itemText)).setTextColor(getResources().getColor(R.color.grey));
//					selectedView.setBackgroundColor(Color.TRANSPARENT);
//		            view.setBackgroundColor(getResources().getColor(R.color.app_bg_color));
//		            ((TextView)view.findViewById(R.id.itemText)).setTextColor(Color.WHITE);
//		            selectedView = view;
//		            if(btn_location.getText().toString().equals((String)listadapter.getItem(position))){
//		            	et_address.setText(btn_location.getText().toString());
//		            }else{
//		            	et_address.setText(btn_location.getText().toString()+((String)listadapter.getItem(position)));
//		            }
//		        }
//			}
			
//		});
		btn_contact.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI), 0);

			}
		});
		btn_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String contactName = et_contactName.getText().toString().trim();
				if(StringUtils.isEmpty(contactName)){
					et_contactName.requestFocus();
					ToastUtils.showToast(mActivity, "请输入联系人的姓名");
					return;
				}
				String contactPhone = et_contactPhone.getText().toString().trim();
				if(StringUtils.isEmpty(contactPhone)){
					et_contactPhone.requestFocus();
					ToastUtils.showToast(mActivity, "请输入联系人的手机号码");
					return;
				}
				
				if(StringUtils.isEmpty(et_address.getText().toString().trim())){
					et_address.requestFocus();
					ToastUtils.showToast(mActivity, "请输入预约地址");
					return;
				}
				if(!PatternUtils.checkPhoneNum(contactPhone)){
					et_contactPhone.requestFocus();
					ToastUtils.showToast(mActivity, "请输入正确的手机号码");
					return;
				}
				String addressLocation = et_address.getText().toString();
				/*if(!(Boolean)btn_time.getTag()){
					btn_time.requestFocus();
					ToastUtils.showToast(mActivity, "请选择预约时间");
					return;
				}*/
//				MyApplication.getInstance().appointMap.put("time", time);
				SharePreferenceManager.saveBatchSharedPreference(mActivity, Constant.FILE_NAME, "address",addressLocation);
				MyApplication.getInstance().appointMap.put("address", addressLocation);
				MyApplication.getInstance().appointMap.put("contactor", contactName);
				MyApplication.getInstance().appointMap.put("mobile", contactPhone);
//				MyApplication.getInstance().appointMap.put("", arg1);
				Intent intent = new Intent(mActivity,
						JLYSFragmentActivity.class);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 20){
			/*address = data.getStringExtra("address");
			if(!StringUtils.isEmpty(address)){
				btn_location.setText(address);
			}*/
		}else if (resultCode == Activity.RESULT_OK) {
			final Uri uriRet = data.getData();
			if (uriRet != null) {
				try {
					Cursor c = mActivity.managedQuery(uriRet, null, null, null,
							null);
					c.moveToFirst();
					/* 取得联络人的姓名 */
					String strName = c
							.getString(c
									.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
					/* 将姓名写入EditText01中 */
					et_contactName.setText(strName);
					/* 取得联络人的电话 */
					int contactId = c.getInt(c
							.getColumnIndex(ContactsContract.Contacts._ID));
					Cursor phones = mActivity.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					String numPhone;
					if (phones.getCount() > 0) {
						phones.moveToFirst();
						/* 2.0可以允许User设定多组电话号码 */
						/*
						 * typePhone = phones .getInt(phones
						 * .getColumnIndex(ContactsContract
						 * .CommonDataKinds.Phone.TYPE));
						 */
						numPhone = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						et_contactPhone.setText(numPhone);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}
	
//	public void setSearchList(ArrayList<String> list){
//		if(locationList==null){
//			locationList = new ArrayList<String>();
//		}
//		locationList.clear();
//		locationList.addAll(list);
//		listadapter.notifyDataSetChanged();
////		setListViewHeightBasedOnChildren(locatioListView);
//	}

	
	@Override
	public void onResume() {
		super.onResume();
		String address = (String)SharePreferenceManager.getSharePreferenceValue(mActivity, Constant.FILE_NAME, "address", "");
		if(!StringUtils.isEmpty(address)){
			et_address.setText(address);
		}
	}

}
