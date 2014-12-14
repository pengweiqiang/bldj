package com.bldj.lexiang.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;

/**
 * 订单评价
 * 
 * @author will
 * 
 */
public class OrderEvalActivity extends BaseActivity {

	ActionBar mActionBar;
	private Button btn_confirm;
	private EditText et_suggestion;
	private Order order;
	RadioGroup rg;//评价类型
	private TextView tv_orderNum;
	private TextView tv_productName;
	private TextView tv_sellerName;
	private TextView tv_order_time;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.order_eval);
		super.onCreate(savedInstanceState);
		order = (Order)this.getIntent().getSerializableExtra("order");
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
		initData();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("用户评价");
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
		btn_confirm = (Button)findViewById(R.id.btn_confirm);
		et_suggestion = (EditText)findViewById(R.id.suggestion);
		rg = (RadioGroup)findViewById(R.id.eval_type);
		tv_orderNum = (TextView)findViewById(R.id.order_num);
		tv_productName = (TextView)findViewById(R.id.product_name);
		tv_sellerName = (TextView) findViewById(R.id.service_name);
		tv_order_time = (TextView)findViewById(R.id.order_time);
	}
	private void initData(){
		
		tv_orderNum.setText(order.getOrderNum());
		tv_productName.setText(order.getProName());
		tv_sellerName.setText(order.getSellerName());
		tv_order_time.setText(order.getCreatetime());
	}

	@Override
	public void initListener() {
		btn_confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String suggestion = et_suggestion.getText().toString().trim();
				if(StringUtils.isEmpty(suggestion)){
					ToastUtils.showToast(OrderEvalActivity.this, "请输入您的宝贵意见");
					return ;
				}
				User user = MyApplication.getInstance().getCurrentUser();
				long userId = 0;
				String nickname = "",username = "";
				if(user != null){
					userId = Long.parseLong(user.getUserId()+"");
					nickname= user.getNickname();
					username= user.getUsername();
				}
				int checkId = rg.getCheckedRadioButtonId();
				if(checkId == R.id.radio_good){
					checkId = 0;
				}else if(checkId == R.id.radio_nor){
					checkId = 1;
				}else if(checkId ==R.id.radio_bad){
					checkId = 2;
				}
					
				ApiUserUtils.unifor(OrderEvalActivity.this, userId, suggestion, 2, nickname, username, 
						String.valueOf(order.getOrderNum()), order.getProName(), order.getSellerName(),checkId , order.getSellerId(), new HttpConnectionUtil.RequestCallback(){

							@Override
							public void execute(ParseModel parseModel) {
								if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
										.getStatus())) {
									ToastUtils.showToast(OrderEvalActivity.this, parseModel.getMsg());
								}else{
									ToastUtils.showToast(OrderEvalActivity.this, "感谢您提出宝贵的意见");
								}
							}
					
					
				});
			}
		});
	}

}
