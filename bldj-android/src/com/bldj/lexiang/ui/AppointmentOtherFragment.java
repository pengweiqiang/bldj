package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.ListviewAdapter;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.utils.DateUtil;
import com.bldj.lexiang.utils.SharePreferenceManager;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.CustomerSpinner;
import com.bldj.lexiang.view.DateTimePickDialogUtil;

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
	private Button btn_location;
	private EditText et_address;
	ListView locatioListView;
	ArrayList<String> locationList;
	ListviewAdapter listadapter;
//	String address;//预约地点
	String time ;//预约时间
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
		btn_location = (Button) infoView.findViewById(R.id.btn_location);
		et_address = (EditText) infoView.findViewById(R.id.et_address);
		locatioListView = (ListView)infoView.findViewById(R.id.locations_list);
		btn_time.setTag(false);
		
		
		btn_location.setText(MyApplication.getInstance().addressStr);
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
	private void initListener() {
		locatioListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				listadapter.setCurrentItem(position);
				et_address.setText(btn_location.getText().toString()+((String)listadapter.getItem(position)));
			}
			
		});
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

}
