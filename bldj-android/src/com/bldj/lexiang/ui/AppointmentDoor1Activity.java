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
import com.bldj.lexiang.api.vo.Seller;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.commons.Constant;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.DateUtil;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.JsonUtils;
import com.bldj.lexiang.utils.SharePreferenceManager;
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
	String date = "";// 日期
	String time = "";// 时间点
	int index = 0;// 时间 4*4 具体的位置
	TextView btn_address;
	String address;
	Product product;//选择的产品
	Seller seller;//选择理疗师
	User user;
	Date nowdate = new Date();
	// GridView gridView;

	Scheduled scheduled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.appointment_door1);
		super.onCreate(savedInstanceState);
		product = (Product) this.getIntent().getSerializableExtra("product");
		seller = (Seller) this.getIntent().getSerializableExtra("seller");
		user = MyApplication.getInstance().getCurrentUser();

		initScheduledView();
		initData();
		getData(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED));
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("上门预约");
		actionBar.setLeftActionButton(R.drawable.btn_back,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
		actionBar.hideRightActionButton();
	}
	private void initData(){
		address = (String)SharePreferenceManager.getSharePreferenceValue(mContext, Constant.FILE_NAME, "address", "");
		if(!StringUtils.isEmpty(address)){
			btn_address.setText(address);
		}
	}
	@Override
	public void initView() {
		mActionBar = (ActionBar) findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		btn_next = (Button) findViewById(R.id.btn_next);
		btn_address = (TextView) findViewById(R.id.btn_address);
		// gridView = (GridView) findViewById(R.id.gridview);
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
				// ToastUtils.showToast(mContext, date+" "+time);
				if (StringUtils.isEmpty(time)) {
					ToastUtils.showToast(mContext, "请选择时间");
					return;
				}
				if (StringUtils.isEmpty(address) || "null".equals(address)) {
					ToastUtils.showToast(mContext, "请选择地址");
					return;
				}
				Intent intent = new Intent();
				if(seller != null){//从美容师界面跳转过来，不需要进入第二步选择美容师
					intent.setClass(mContext, AppointmentDoor3Activity.class);
					intent.putExtra("seller", seller);
				}else{//直接从产品详情进入，需要进行选择美容师第二步
					intent.setClass(mContext, AppointmentDoor2Activity.class);
				}
				intent.putExtra("time", date + " " + time);
				intent.putExtra("timeIndex", index);
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
			if(!StringUtils.isEmpty(address)){
				btn_address.setText(address);
				SharePreferenceManager.saveBatchSharedPreference(mContext, Constant.FILE_NAME, "address",address);
			}
		}
	}

	private void getData(String date) {
		long sellerId = seller==null?0:seller.getId();
		ApiBuyUtils.getScheduled(mContext, sellerId, product.getId(), date,
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
	TextView timeStatus1, timeStatus2, timeStatus3, timeStatus4, timeStatus5,
			timeStatus6, timeStatus7, timeStatus8, timeStatus9, timeStatus10,
			timeStatus11, timeStatus12, timeStatus13, timeStatus14,
			timeStatus15, timeStatus16;

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

		time1.setTag("10:00");
		time2.setTag("11:00");
		time3.setTag("12:00");
		time4.setTag("12:30");
		time5.setTag("13:00");
		time6.setTag("13:30");
		time7.setTag("14:00");
		time8.setTag("14:30");
		time9.setTag("15:00");
		time10.setTag("16:00");
		time11.setTag("17:00");
		time12.setTag("17:50");
		time13.setTag("18:00");
		time14.setTag("19:00");
		time15.setTag("20:00");
		time16.setTag("21:00");

		time1.setTag(time1.getId(), 0);
		time2.setTag(time2.getId(), 1);
		time3.setTag(time3.getId(), 2);
		time4.setTag(time4.getId(), 3);
		time5.setTag(time5.getId(), 4);
		time6.setTag(time6.getId(), 5);
		time7.setTag(time7.getId(), 6);
		time8.setTag(time8.getId(), 7);
		time9.setTag(time9.getId(), 8);
		time10.setTag(time10.getId(), 9);
		time11.setTag(time11.getId(), 10);
		time12.setTag(time12.getId(), 11);
		time13.setTag(time13.getId(), 12);
		time14.setTag(time14.getId(), 13);
		time15.setTag(time15.getId(), 14);
		time16.setTag(time16.getId(), 15);

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

		timeStatus1 = (TextView) findViewById(R.id.tv_status1);
		timeStatus2 = (TextView) findViewById(R.id.tv_status2);
		timeStatus3 = (TextView) findViewById(R.id.tv_status3);
		timeStatus4 = (TextView) findViewById(R.id.tv_status4);
		timeStatus5 = (TextView) findViewById(R.id.tv_status5);
		timeStatus6 = (TextView) findViewById(R.id.tv_status6);
		timeStatus7 = (TextView) findViewById(R.id.tv_status7);
		timeStatus8 = (TextView) findViewById(R.id.tv_status8);
		timeStatus9 = (TextView) findViewById(R.id.tv_status9);
		timeStatus10 = (TextView) findViewById(R.id.tv_status10);
		timeStatus11 = (TextView) findViewById(R.id.tv_status11);
		timeStatus12 = (TextView) findViewById(R.id.tv_status12);
		timeStatus13 = (TextView) findViewById(R.id.tv_status13);
		timeStatus14 = (TextView) findViewById(R.id.tv_status14);
		timeStatus15 = (TextView) findViewById(R.id.tv_status15);
		timeStatus16 = (TextView) findViewById(R.id.tv_status16);

		tv_first_date.setText("今天");
		tv_first_date.setTag(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED));
		tv_first_date.setBackgroundColor(getResources().getColor(
				R.color.selected_color));

		tv_seconde_date.setText("明天");
		tv_seconde_date.setTag(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED, 1));
		tv_seconde_date.setBackgroundColor(getResources().getColor(
				R.color.time_unselecte));

		tv_third_date.setText(DateUtil.getScheduledTitle(nowdate,
				DateUtil.SIMPLY_DD_PATTERN2, 2));
		tv_third_date.setTag(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED, 2));
		tv_third_date.setBackgroundColor(getResources().getColor(
				R.color.time_unselecte));
//		tv_third_date.setBackground(null);

		tv_four_date.setText(DateUtil.getScheduledTitle(nowdate,
				DateUtil.SIMPLY_DD_PATTERN2, 3));
		tv_four_date.setTag(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED, 3));
		tv_four_date.setBackgroundColor(getResources().getColor(
				R.color.time_unselecte));

		tv_five_date.setText(DateUtil.getScheduledTitle(nowdate,
				DateUtil.SIMPLY_DD_PATTERN2, 4));
		tv_five_date.setTag(DateUtil.getDateString(nowdate,
				DateUtil.CUSTOM_PATTERN_SCHEDULED, 4));
		tv_five_date.setBackgroundColor(getResources().getColor(
				R.color.time_unselecte));

		currentDateView = tv_first_date;// 默认选中今天
		date = tv_first_date.getTag().toString();
	}

	TextView currentDateView;// 当前选中的日期
	View currentTimeView;// 当前选中的时间
	// 具体时间点
	OnClickListener timeClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (currentTimeView != null) {
				currentTimeView.setBackgroundColor(getResources().getColor(
						R.color.scheduled_green));

			}
			time = v.getTag().toString();

			v.setBackgroundColor(getResources()
					.getColor(R.color.selected_color));
			currentTimeView = v;
			index = (Integer) v.getTag(v.getId());
		}
	};
	// 日期选择
	OnClickListener dateClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (currentDateView == (TextView) v) {
				return;
			}
			if (currentDateView != null) {
				currentDateView.setBackgroundColor(getResources().getColor(
						R.color.time_unselecte));
			}
			time = "";
			date = v.getTag().toString();
			v.setBackgroundColor(getResources()
					.getColor(R.color.selected_color));
			// ToastUtils.showToast(mContext, date);
			getData((String) v.getTag());
			currentDateView = (TextView) v;
		}
	};
	String canAppointment = "可预约";
	String cannotAppointment = "不可预约";

	private void setTimeStatus() {
		if (scheduled != null) {
			setStatus(scheduled.getColumn1(), time1, timeStatus1);
			setStatus(scheduled.getColumn2(), time2, timeStatus2);
			setStatus(scheduled.getColumn3(), time3, timeStatus3);
			setStatus(scheduled.getColumn4(), time4, timeStatus4);
			setStatus(scheduled.getColumn5(), time5, timeStatus5);
			setStatus(scheduled.getColumn6(), time6, timeStatus6);
			setStatus(scheduled.getColumn7(), time7, timeStatus7);
			setStatus(scheduled.getColumn8(), time8, timeStatus8);
			setStatus(scheduled.getColumn9(), time9, timeStatus9);
			setStatus(scheduled.getColumn10(), time10, timeStatus10);
			setStatus(scheduled.getColumn11(), time11, timeStatus11);
			setStatus(scheduled.getColumn12(), time12, timeStatus12);
			setStatus(scheduled.getColumn13(), time13, timeStatus13);
			setStatus(scheduled.getColumn14(), time14, timeStatus14);
			setStatus(scheduled.getColumn15(), time15, timeStatus15);
			setStatus(scheduled.getColumn16(), time16, timeStatus16);
		}
	}

	private void setStatus(int status, View bgView, TextView time) {
		if (status == 0) {// 可预约
			bgView.setBackgroundColor(getResources().getColor(
					R.color.scheduled_green));
			// bgView.setBackground(null);
			time.setText(canAppointment);
		} else if (status == 1) {// 不可预约
			bgView.setBackgroundColor(getResources().getColor(
					R.color.scheduled_grey));
			time.setText(cannotAppointment);
			bgView.setClickable(false);
			bgView.setEnabled(false);
		}
	}

	// private void setTimeStatus() {
	// List<Time> times = new ArrayList<Time>();
	// time1.setTag("10:00");time2.setTag("11:00");
	// time3.setTag("12:00");time4.setTag("12:30");
	// time5.setTag("14:30");time6.setTag("15:00");
	// time7.setTag("16:00");time8.setTag("17:30");
	// time9.setTag("18:00");time10.setTag("19:00");
	// time11.setTag("20:00");time12.setTag("21:00");
	// time13.setTag("21:30");time14.setTag("22:00");
	// time15.setTag("22:30");time16.setTag("23:00");

	// Time t1 = new Time();
	// t1.setStatus(scheduled.getColumn1());
	// t1.setTime("10:00");
	//
	// Time t2 = new Time();
	// t2.setStatus(scheduled.getColumn1());
	// t2.setTime("10:00");
	//
	// Time t3 = new Time();
	// t3.setStatus(scheduled.getColumn1());
	// t3.setTime("10:00");
	//
	// Time t4 = new Time();
	// t4.setStatus(scheduled.getColumn1());
	// t4.setTime("10:00");
	//
	// Time t5 = new Time();
	// t5.setStatus(scheduled.getColumn1());
	// t5.setTime("10:00");
	//
	// Time t6 = new Time();
	// t6.setStatus(scheduled.getColumn1());
	// t6.setTime("10:00");
	//
	// Time t7 = new Time();
	// t7.setStatus(scheduled.getColumn1());
	// t7.setTime("10:00");
	//
	// Time t8 = new Time();
	// t8.setStatus(scheduled.getColumn1());
	// t8.setTime("10:00");
	//
	// Time t9 = new Time();
	// t9.setStatus(scheduled.getColumn1());
	// t9.setTime("10:00");
	//
	// Time t10 = new Time();
	// t10.setStatus(scheduled.getColumn1());
	// t10.setTime("10:00");
	//
	// Time t11 = new Time();
	// t11.setStatus(scheduled.getColumn1());
	// t11.setTime("10:00");
	//
	// Time t12 = new Time();
	// t12.setStatus(scheduled.getColumn1());
	// t12.setTime("10:00");
	//
	// Time t13 = new Time();
	// t13.setStatus(scheduled.getColumn1());
	// t13.setTime("10:00");
	//
	// Time t14 = new Time();
	// t14.setStatus(scheduled.getColumn1());
	// t14.setTime("10:00");
	//
	// Time t15 = new Time();
	// t15.setStatus(scheduled.getColumn1());
	// t15.setTime("10:00");
	//
	// Time t16 = new Time();
	// t16.setStatus(scheduled.getColumn1());
	// t16.setTime("10:00");
	//
	// }

}
