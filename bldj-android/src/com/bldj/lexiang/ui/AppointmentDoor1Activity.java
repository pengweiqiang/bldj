package com.bldj.lexiang.ui;

import java.util.Date;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiBuyUtils;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.Product;
import com.bldj.lexiang.api.vo.Scheduled;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtil;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

/**
 * 上门预约1
 * 
 * @author will
 * 
 */
public class AppointmentDoor1Activity extends BaseActivity {

	ActionBar mActionBar;
	Button btn_next;
	String time = "";
	Button btn_address;
	String address;
	Product product;
	User user;
	Date nowdate = new Date();

	Scheduled scheduled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.appointment_door1);
		super.onCreate(savedInstanceState);
		product = (Product) this.getIntent().getSerializableExtra("product");
		user = MyApplication.getInstance().getCurrentUser();
		initView();

		initListener();

		initScheduledView();
		getData(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED));
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("上门预约");
		actionBar.setLeftActionButton(R.drawable.ic_menu_back,
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
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_address = (Button) findViewById(R.id.btn_address);
	}

	@Override
	public void initListener() {

		btn_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(AppointmentDoor1Activity.this,
						AddressesActivity.class);
				intent.putExtra("type", 1);
				startActivityForResult(intent, 123);
			}
		});

		btn_next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(StringUtils.isEmpty(address)){
					ToastUtils.showToast(mContext, "请选择地址");
					return;
				}
				if(StringUtils.isEmpty(time)){
					ToastUtils.showToast(mContext, "请选择时间");
					return;
				}
				Intent intent = new Intent(AppointmentDoor1Activity.this,
						AppointmentDoor2Activity.class);
				intent.putExtra("time", time);
				intent.putExtra("product", product);
				intent.putExtra("address", address);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 20) {
			address = data.getStringExtra("address");
			btn_address.setText(address);
		}
	}

	private void getData(String date) {
		ApiBuyUtils.getScheduled(mContext, user.getUserId(), 0, date,
				new HttpConnectionUtil.RequestCallback() {

					@Override
					public void execute(ParseModel parseModel) {
						if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
								.getStatus())) {
							ToastUtils.showToast(mContext, parseModel.getMsg());
						} else {
							scheduled = (Scheduled) JsonUtils.fromJson(
									parseModel.getData().toString(),
									Scheduled.class);
							setTimeStatus();
						}

					}
				});
	}

	TextView tv_first_date, tv_seconde_date, tv_third_date, tv_four_date,
			tv_five_date;
	View time1, time2, time3, time4, time5, time6, time7, time8, time9, time10,
			time11, time12, time13, time14, time15, time16;
	TextView timeStatus1,timeStatus2,timeStatus3,timeStatus4,timeStatus5,timeStatus6,timeStatus7,timeStatus8,
	timeStatus9,timeStatus10,timeStatus11,timeStatus12,timeStatus13,timeStatus14,timeStatus15,timeStatus16;

	private void initScheduledView() {
		tv_first_date = (TextView) findViewById(R.id.first_date);
		tv_seconde_date = (TextView) findViewById(R.id.second_date);
		tv_third_date = (TextView) findViewById(R.id.third_date);
		tv_four_date = (TextView) findViewById(R.id.four_date);
		tv_five_date = (TextView) findViewById(R.id.five_date);
		
		tv_first_date.setOnClickListener(dateClickListener);
		tv_seconde_date.setOnClickListener(dateClickListener);
		tv_third_date.setOnClickListener(dateClickListener);
		tv_four_date.setOnClickListener(dateClickListener);
		tv_five_date.setOnClickListener(dateClickListener);
		
		time1 = findViewById(R.id.time1);
		time2 = findViewById(R.id.time2);
		time3 = findViewById(R.id.time3);
		time4 = findViewById(R.id.time4);
		time5 = findViewById(R.id.time5);
		time6 = findViewById(R.id.time6);
		time7 = findViewById(R.id.time7);
		time8 = findViewById(R.id.time8);
		time9 = findViewById(R.id.time9);
		time10 = findViewById(R.id.time10);
		time11 = findViewById(R.id.time11);
		time12 = findViewById(R.id.time12);
		time13 = findViewById(R.id.time13);
		time14 = findViewById(R.id.time14);
		time15 = findViewById(R.id.time15);
		time16 = findViewById(R.id.time16);
		
		time1.setTag("10:00");time2.setTag("10:30");
		time3.setTag("11:00");time4.setTag("12:00");
		time5.setTag("13:00");time6.setTag("14:00");
		time7.setTag("15:00");time8.setTag("15:30");
		time9.setTag("16:00");time10.setTag("17:00");
		time11.setTag("18:00");time12.setTag("19:00");
		time13.setTag("20:00");time14.setTag("21:00");
		time15.setTag("22:00");time16.setTag("22:30");
		
		time1.setOnClickListener(timeClickListener);
		time2.setOnClickListener(timeClickListener);
		time3.setOnClickListener(timeClickListener);
		time4.setOnClickListener(timeClickListener);
		time5.setOnClickListener(timeClickListener);
		time6.setOnClickListener(timeClickListener);
		time7.setOnClickListener(timeClickListener);
		time8.setOnClickListener(timeClickListener);
		time9.setOnClickListener(timeClickListener);
		time10.setOnClickListener(timeClickListener);
		time11.setOnClickListener(timeClickListener);
		time12.setOnClickListener(timeClickListener);
		time13.setOnClickListener(timeClickListener);
		time14.setOnClickListener(timeClickListener);
		time15.setOnClickListener(timeClickListener);
		time16.setOnClickListener(timeClickListener);
		
		
		timeStatus1 = (TextView)findViewById(R.id.tv_status1);
		timeStatus2 = (TextView)findViewById(R.id.tv_status2);
		timeStatus3 = (TextView)findViewById(R.id.tv_status3);
		timeStatus4 = (TextView)findViewById(R.id.tv_status4);
		timeStatus5 = (TextView)findViewById(R.id.tv_status5);
		timeStatus6 = (TextView)findViewById(R.id.tv_status6);
		timeStatus7 = (TextView)findViewById(R.id.tv_status7);
		timeStatus8 = (TextView)findViewById(R.id.tv_status8);
		timeStatus9 = (TextView)findViewById(R.id.tv_status9);
		timeStatus10 = (TextView)findViewById(R.id.tv_status10);
		timeStatus11 = (TextView)findViewById(R.id.tv_status11);
		timeStatus12 = (TextView)findViewById(R.id.tv_status12);
		timeStatus13 = (TextView)findViewById(R.id.tv_status13);
		timeStatus14 = (TextView)findViewById(R.id.tv_status14);
		timeStatus15 = (TextView)findViewById(R.id.tv_status15);
		timeStatus16 = (TextView)findViewById(R.id.tv_status16);
		

		tv_first_date.setText("今天");
		tv_first_date.setTag(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED));

		tv_seconde_date.setText("明天");
		tv_seconde_date.setTag(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED, 1));

		tv_third_date.setText(DateUtil.getDateString(nowdate,
				DateUtil.SIMPLY_DD_PATTERN2, 2));
		tv_third_date.setTag(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED, 2));

		tv_four_date.setText(DateUtil.getDateString(nowdate,
				DateUtil.SIMPLY_DD_PATTERN2, 3));
		tv_four_date.setTag(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED, 3));

		tv_five_date.setText(DateUtil.getDateString(nowdate,
				DateUtil.SIMPLY_DD_PATTERN2, 4));
		tv_five_date.setTag(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED, 4));
		

	}

	TextView currentDateView;
	View currentTimeView;
	//具体时间点
	OnClickListener timeClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(currentTimeView != null){
				currentTimeView.setBackground(null);
				
			}
			time = time+v.getTag().toString();
//			time = StringUtils.isEmpty(time)?time:time.substring(0, time.indexOf(" "))+" "+v.getTag().toString();
//			ToastUtils.showToast(mContext, time);
			v.setBackgroundColor(getResources().getColor(R.color.red));
			currentTimeView = v;
		}
	};
	//日期选择
	OnClickListener dateClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(currentDateView !=null){
//				currentDateView.setBackgroundColor(null);
//				currentDateView.setBacegroundDrawable(null);
				currentDateView.setBackground(null);
			}
			time = v.getTag().toString();
			v.setBackgroundColor(getResources().getColor(R.color.light_green));
//			ToastUtils.showToast(mContext, v.getTag().toString());
			getData((String) v.getTag());
			currentDateView = (TextView)v;
		}
	};
	String canAppointment = "可预约";
	String cannotAppointment = "被预约";
	private void setTimeStatus(){
		if (scheduled != null) {
			setStatus(scheduled.getColumn1(),time1, timeStatus1);
			setStatus(scheduled.getColumn2(), time2,timeStatus2);
			setStatus(scheduled.getColumn3(), time3,timeStatus3);
			setStatus(scheduled.getColumn4(), time4,timeStatus4);
			setStatus(scheduled.getColumn5(), time5,timeStatus5);
			setStatus(scheduled.getColumn6(), time6,timeStatus6);
			setStatus(scheduled.getColumn7(), time7,timeStatus7);
			setStatus(scheduled.getColumn8(), time8,timeStatus8);
			setStatus(scheduled.getColumn9(), time9,timeStatus9);
			setStatus(scheduled.getColumn10(), time10,timeStatus10);
			setStatus(scheduled.getColumn11(), time11,timeStatus11);
			setStatus(scheduled.getColumn12(), time12,timeStatus12);
			setStatus(scheduled.getColumn13(), time13,timeStatus13);
			setStatus(scheduled.getColumn14(), time14,timeStatus14);
			setStatus(scheduled.getColumn15(), time15,timeStatus15);
			setStatus(scheduled.getColumn16(), time16,timeStatus16);
		}
	}
	private void setStatus(int status,View bgView,TextView time){
		if(status==0){
//			bgView.setBackgroundColor(getResources().getColor(
//				R.color.light_green));
			time.setText(canAppointment);
		}else if(status == 1){
			bgView.setBackgroundColor(getResources().getColor(
					R.color.grey));
				time.setText(cannotAppointment);
		}
	}

}
