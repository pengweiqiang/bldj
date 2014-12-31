package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bldj.lexiang.MyApplication;
import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.GroupAdapter;
import com.bldj.lexiang.api.ApiUserUtils;
import com.bldj.lexiang.api.vo.Order;
import com.bldj.lexiang.api.vo.ParseModel;
import com.bldj.lexiang.api.vo.User;
import com.bldj.lexiang.constant.api.ApiConstants;
import com.bldj.lexiang.constant.enums.TitleBarEnum;
import com.bldj.lexiang.utils.DateUtil;
import com.bldj.lexiang.utils.DeviceInfo;
import com.bldj.lexiang.utils.HttpConnectionUtil;
import com.bldj.lexiang.utils.ShareUtil;
import com.bldj.lexiang.utils.StringUtils;
import com.bldj.lexiang.utils.ToastUtils;
import com.bldj.lexiang.view.ActionBar;
import com.bldj.lexiang.view.LoadingDialog;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

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
	ShareUtil shareUtil;
	
	LoadingDialog loading;
	
	private PopupWindow popupWindow;
	private View view;
	private ListView lv_group;
	private List<TitleBarEnum> groups;
	
	private boolean evalSuccess;//评价成功
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.order_eval);
		super.onCreate(savedInstanceState);
		order = (Order)this.getIntent().getSerializableExtra("order");
		mActionBar = (ActionBar)findViewById(R.id.actionBar);
		onConfigureActionBar(mActionBar);
		
		initData();
		shareUtil = new ShareUtil(this);
		shareUtil.initWX();
	}

	// 设置activity的导航条
	protected void onConfigureActionBar(ActionBar actionBar) {
		actionBar.setTitle("用户评价");
		actionBar.setLeftActionButton(R.drawable.btn_back,
				new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent data = new Intent();
				data.putExtra("eval", evalSuccess);
				setResult(24, data); 
				finish();
			}
		});
		actionBar.setRightTextActionButton("分享",R.drawable.btn_share,true, new View.OnClickListener() {
			
			@Override
			public void onClick(View parent) {
//				shareUtil.sendWebPageToWX(order.getProName(), SendMessageToWX.Req.WXSceneTimeline);
				buildTitleBar(parent);
			}
		});
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
		tv_order_time.setText(DateUtil.getDateString(order.getCreatetime(),DateUtil.TRIM_PATTERN,DateUtil.CRITICISM_PATTERN));
	}

	@Override
	public void initListener() {
		btn_confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String suggestion = et_suggestion.getText().toString().trim();
				if(StringUtils.isEmpty(suggestion)){
					et_suggestion.requestFocus();
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
				loading = new LoadingDialog(mContext);
				loading.show();
				ApiUserUtils.unifor(OrderEvalActivity.this, userId, suggestion, 2, nickname, username, 
						String.valueOf(order.getOrderNum()), order.getProName(), order.getSellerName(),checkId , order.getSellerId(), new HttpConnectionUtil.RequestCallback(){

							@Override
							public void execute(ParseModel parseModel) {
								loading.cancel();
								if (!ApiConstants.RESULT_SUCCESS.equals(parseModel
										.getStatus())) {
									ToastUtils.showToast(OrderEvalActivity.this, parseModel.getMsg());
								}else{
									ToastUtils.showToast(OrderEvalActivity.this, "感谢您提出宝贵的意见");
									evalSuccess =  true;
									Intent data = new Intent();
									data.putExtra("eval", evalSuccess);
									setResult(24, data); 
									finish();
								}
							}
					
					
				});
			}
		});
	}
	
	
	private void buildTitleBar(View parent) {
		DeviceInfo.setContext(this);
		if (popupWindow == null) {
			view = LayoutInflater.from(this).inflate(R.layout.group_list, null);
			lv_group = (ListView) view.findViewById(R.id.lvGroup);
			groups = new ArrayList<TitleBarEnum>();
			groups.add(TitleBarEnum.SHARE_SINA);
			groups.add(TitleBarEnum.SHARE_WEIXIN);
			groups.add(TitleBarEnum.SHARE_TENCENT);
			GroupAdapter groupAdapter = new GroupAdapter(this, groups);
			lv_group.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view, DeviceInfo.getScreenWidth() / 2
					- parent.getWidth(), LayoutParams.WRAP_CONTENT);
		}
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());

		// WindowManager windowManager = (WindowManager)
		// this.getActivity().getSystemService(Context.WINDOW_SERVICE);

		// popupWindow.showAsDropDown(parent, popupWindow.getWidth(), 0);
		popupWindow.showAsDropDown(parent);
		lv_group.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {

				if (groups.get(position).getIndex() == TitleBarEnum.SHARE_SINA
						.getIndex()) {
					String shareUrl = 
							ShareUtil.shareSina("健康送到家，方便你我他", "","");
					Intent intent = new Intent(OrderEvalActivity.this,BannerWebActivity.class);
					intent.putExtra("url", shareUrl);
					intent.putExtra("name", TitleBarEnum.SHARE_SINA.getMsg());
					startActivity(intent);
				} else if (groups.get(position).getIndex() == TitleBarEnum.SHARE_WEIXIN
						.getIndex()) {
					ToastUtils.showToast(mContext, "分享微信...");
					shareUtil.sendWebPageToWX("健康送到家，方便你我他",
							SendMessageToWX.Req.WXSceneTimeline,"");
				} else if (groups.get(position).getIndex() == TitleBarEnum.SHARE_TENCENT
						.getIndex()) {
					String shareUrl = 
							ShareUtil.shareQQ("健康送到家，方便你我他", "", "");
					Intent intent = new Intent(OrderEvalActivity.this,BannerWebActivity.class);
					intent.putExtra("url", shareUrl);
					intent.putExtra("name", TitleBarEnum.SHARE_TENCENT.getMsg());
					startActivity(intent);
				}
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
			}
		});
	}

}
