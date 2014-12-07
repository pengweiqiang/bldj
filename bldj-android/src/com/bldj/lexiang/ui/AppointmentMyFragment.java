package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.view.CustomerSpinner;

/**
 * 为自己预约
 * 
 * @author will
 * 
 */
public class AppointmentMyFragment extends BaseFragment {
	
	View infoView;
	private Button btn_next;

	Button btn_address;
	Button btn_location;
	String address;
	
	CustomerSpinner spinner;
	ArrayList<String> citys = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
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
		btn_address = (Button)infoView.findViewById(R.id.btn_address);
		btn_location = (Button)infoView.findViewById(R.id.btn_location);
		
		btn_location.setText(MyApplication.getInstance().addressStr);
		btn_address.setText(MyApplication.getInstance().addressStr);
		
		
		spinner = (CustomerSpinner)infoView.findViewById(R.id.spinner_city);
		citys.add(TitleBarEnum.CITY_BEIJING.getMsg());
		citys.add(TitleBarEnum.CITY_GUANGZHOU.getMsg());
		citys.add(TitleBarEnum.CITY_SHENZHEN.getMsg());
	    spinner.setList(citys);
	    adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, citys);
	    spinner.setAdapter(adapter);
	}
	/**
	 * 事件初始化
	 */
	private void initListener(){
		btn_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mActivity,JLYSFragmentActivity.class);
				startActivity(intent);
			}
		});
		btn_location.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(mActivity,MapLocationActivity.class);
				startActivityForResult(intent, 123);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 20){
			address = data.getStringExtra("address");
			if(!StringUtils.isEmpty(address)){
				btn_location.setText(address);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
