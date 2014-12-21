package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
public class AppointmentMyFragment extends BaseFragment {
	
	View infoView;
	private Button btn_next;

	
	EditText et_address;
	Button btn_location;
	Button btn_time;
//	String address;//预约地点
	ListView locatioListView;
	ArrayList<String> locationList;
	ListviewAdapter listadapter;
	
	CustomerSpinner spinner;
	ArrayList<String> citys = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	
	
	private String time;//预约时间
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		btn_location = (Button)infoView.findViewById(R.id.btn_location);
		btn_time = (Button) infoView.findViewById(R.id.btn_appoint_time);
		locatioListView = (ListView)infoView.findViewById(R.id.locations_list);
		btn_time.setTag(false);
		
		btn_location.setText(MyApplication.getInstance().addressStr);
//		btn_address.setText(MyApplication.getInstance().addressStr);
		
		//城市
//		spinner = (CustomerSpinner)infoView.findViewById(R.id.spinner_city);
//		citys.add(TitleBarEnum.CITY_BEIJING.getMsg());
//		citys.add(TitleBarEnum.CITY_GUANGZHOU.getMsg());
//		citys.add(TitleBarEnum.CITY_SHENZHEN.getMsg());
//	    spinner.setList(citys);
//	    adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, citys);
//	    spinner.setAdapter(adapter);
	    
	    //常用地址
	    locationList = new ArrayList<String>();
	    locationList.add("上地城铁");
	    locationList.add("长城大厦");
	    locationList.add("来广营");
	    locationList.add("西直门");
	    listadapter = new ListviewAdapter(mActivity,locationList,1);
	    locatioListView.setAdapter(listadapter);
	    locatioListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新
	}
	/**
	 * 事件初始化
	 */
	private void initListener(){
		locatioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				listadapter.setCurrentItem(position);
				et_address.setText(btn_location.getText().toString()+((String)listadapter.getItem(position)));
			}
			
		});
		
		
		btn_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				time = btn_time.getText().toString();
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
