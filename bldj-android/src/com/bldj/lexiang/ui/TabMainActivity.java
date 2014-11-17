package com.bldj.lexiang.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.bldj.lexiang.R;
import com.bldj.lexiang.utils.CommonHelper;
import com.umeng.analytics.MobclickAgent;

public class TabMainActivity extends TabActivity implements
	OnCheckedChangeListener {
    protected static final String TAG = "TabMainActivity";
    private TabHost mMainHost;
    private static RadioGroup mRadioGroup;
    private List<View> views;
    private static int currentPage;
    private int mCurrentSelectedIndex;
    private ImageView bottomSelect;
    private RadioButton rb;

    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabmain_activity);
		initView();
	}
    private void initView() {
	mMainHost = this.getTabHost();
	mMainHost.getTabWidget().setStripEnabled(false);
	mRadioGroup = (RadioGroup) findViewById(R.id.rgTabBar);
	mRadioGroup.setOnCheckedChangeListener(this);
	int count = mRadioGroup.getChildCount();
	views = new ArrayList<View>();
	for (int i = 0; i < count; i++) {
	    views.add(mRadioGroup.getChildAt(i));
	}
	bottomSelect = (ImageView) findViewById(R.id.tab_bottom_select);	
	setViews();
    }

    @Override
    protected void onResume() {
	super.onResume();
		int page = getIntent().getIntExtra("page", 0);
		if (page != 0) {
		    currentPage = page;
		    setCurrentId();
		    getIntent().putExtra("page", 0);
		}
		MobclickAgent.onResume(this);
    }

    /**
     * 设置View
     * 
     */
    private void setViews() {
	TabSpec topListTab = mMainHost.newTabSpec("TabMainFindGameActivity");
	topListTab.setIndicator("TabMainFindGameActivity");
	topListTab.setContent(new Intent(TabMainActivity.this,
			WebAppActivity.class));

	TabSpec mFriendTab = mMainHost
		.newTabSpec("TabMainCategoryRankActivity");
	mFriendTab.setIndicator("TabMainCategoryRankActivity");
	mFriendTab.setContent(new Intent(TabMainActivity.this,
			WebAppActivity.class));

	TabSpec mMyDetailTab = mMainHost.newTabSpec("TabMainMyDetailActivity");
	mMyDetailTab.setIndicator("TabMainMyDetailActivity");
	mMyDetailTab.setContent(new Intent(TabMainActivity.this,
			WebAppActivity.class));

	TabSpec mHomePageTab = mMainHost.newTabSpec("TabMainPlayGameActivity");
	mHomePageTab.setIndicator("TabMainPlayGameActivity");
	mHomePageTab.setContent(new Intent(TabMainActivity.this,
			WebAppActivity.class));

	TabSpec mMyGameTab = mMainHost.newTabSpec("TabMainDiscoverActivity");
	mMyGameTab.setIndicator("TabMainDiscoverActivity");
	mMyGameTab.setContent(new Intent(TabMainActivity.this,
			WebAppActivity.class));

	mMainHost.addTab(mHomePageTab);
	mMainHost.addTab(topListTab);
	mMainHost.addTab(mFriendTab);
	mMainHost.addTab(mMyGameTab);
	mMainHost.addTab(mMyDetailTab);
	currentPage = R.id.rdHomePage;
	mCurrentSelectedIndex = 3;
	// 初始化设置一次标签背景
	mMainHost.setCurrentTabByTag("TabMainPlayGameActivity");
	ViewTreeObserver vto2 = mMainHost.getViewTreeObserver();
	vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	    @Override
	    public void onGlobalLayout() {
		mMainHost.getViewTreeObserver().removeGlobalOnLayoutListener(
			this);
		initBottomSelect();
		;
	    }
	});
    }

    public void setSelectView() {
	currentPage = R.id.rdUpload;
	mMainHost.setCurrentTabByTag("TabMainMyDetailActivity");
	moveBottomSelect(6);
	RadioButton myDetailButton = (RadioButton) this
		.findViewById(R.id.rdUpload);
	myDetailButton.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
	switch (checkedId) {
	case R.id.rdTopList:
	    currentPage = R.id.rdTopList;
	    mMainHost.setCurrentTabByTag("TabMainFindGameActivity");
	    moveBottomSelect(0);
	    break;
	case R.id.rdFriend:
	    currentPage = R.id.rdFriend;
	    mMainHost.setCurrentTabByTag("TabMainCategoryRankActivity");
	    moveBottomSelect(2);
	    break;
	case R.id.rdHomePage:
	    currentPage = R.id.rdHomePage;
	    mMainHost.setCurrentTabByTag("TabMainPlayGameActivity");
	    moveBottomSelect(3);
	    break;
	case R.id.rdMe:
	    currentPage = R.id.rdMe;
	    mMainHost.setCurrentTabByTag("TabMainDiscoverActivity");
	    moveBottomSelect(4);
	    break;
	case R.id.rdUpload:
	    currentPage = R.id.rdUpload;
	    mMainHost.setCurrentTabByTag("TabMainMyDetailActivity");
	    moveBottomSelect(6);
	    break;
	}

    }

    @Override
    protected void onNewIntent(Intent intent) {
	setIntent(intent);
	super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
	super.onPause();
	MobclickAgent.onPause(this);
    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }

    public void setCurrentId() {
	if (mRadioGroup != null) {
	    mRadioGroup.check(currentPage);
	    switch (currentPage) {
	    case R.id.rdTopList:
		moveBottomSelect(0);
		break;
	    case R.id.rdFriend:
		moveBottomSelect(2);
		break;
	    case R.id.rdHomePage:
		moveBottomSelect(3);
		break;
	    case R.id.rdMe:
		moveBottomSelect(4);
		break;
	    case R.id.rdUpload:
		moveBottomSelect(6);
		break;
	    }
	}
    }

    public static void setHomePage() {
	if (mRadioGroup != null) {
	    mRadioGroup.check(R.id.rdHomePage);
	    currentPage = R.id.rdHomePage;
	}
    }

    public static boolean canSetCurrentPage() {
	return mRadioGroup != null;
    }

    public Animation inFromBotttomAnimation() {
	Animation inFromRight = AnimationUtils.loadAnimation(this,
		R.anim.activity_bottom_in);
	return inFromRight;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
	// TODO Auto-generated method stub
	super.onWindowFocusChanged(hasFocus);
	// initBottomSelect();
    }

    private void initBottomSelect() {
	LinearLayout.LayoutParams bottomSelectLayoutParams = (LinearLayout.LayoutParams) bottomSelect
		.getLayoutParams();
	int width = mRadioGroup.getChildAt(0).getWidth();
	Log.i(TAG, "zybb:"+width+":"+mRadioGroup.getChildAt(3).getWidth());
	bottomSelectLayoutParams.width = width;

	int startLeft = (CommonHelper.getScreenWidth() - width) / 2;
	
	ImageView leftShadow = (ImageView)findViewById(R.id.main_navigation_bar_play_game_shadow_left);
	int leftShadowWidth = leftShadow.getWidth();
	int playGameLeft = mRadioGroup.getChildAt(3).getLeft();
	RelativeLayout.LayoutParams leftShadowLayoutParams = (RelativeLayout.LayoutParams) leftShadow
		.getLayoutParams();
	leftShadowLayoutParams.leftMargin = (playGameLeft - leftShadowWidth);
	leftShadow.setVisibility(View.VISIBLE);
	
	ImageView rightShadow = (ImageView)findViewById(R.id.main_navigation_bar_play_game_shadow_right);	
	int playGameRight = mRadioGroup.getChildAt(3).getRight();
	RelativeLayout.LayoutParams rightShadowLayoutParams = (RelativeLayout.LayoutParams) rightShadow
		.getLayoutParams();
	rightShadowLayoutParams.leftMargin = playGameRight;
	rightShadow.setVisibility(View.VISIBLE);
	
	//int padding = getResources().getDimensionPixelSize(R.dimen.app_padding);
	int padding = 0;
	//int startLeft = mRadioGroup.getChildAt(3).getLeft() + padding;
	TranslateAnimation animation = new TranslateAnimation(0, startLeft, 0,
		0);
	animation.setDuration(10);
	animation.setFillAfter(true);
	bottomSelect.bringToFront();
	bottomSelect.startAnimation(animation);
	animation.setAnimationListener(new AnimationListener() {

	    @Override
	    public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	    }

	    @Override
	    public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		bottomSelect.setVisibility(View.VISIBLE);
	    }
	});

    }

    /**
     * 移动tab选中标识图片
     * 
     * @param selectIndex
     * @param curIndex
     */
    private void moveBottomSelect(final int selectIndex) {

	// 起始位置中心点

	// int startMid =
	// (mRadioGroup.getChildAt(mCurrentSelectedIndex)).getLeft() +
	// (mRadioGroup.getChildAt(mCurrentSelectedIndex)).getWidth() / 2;
	// 起始位置左边位置坐标
	// int startLeft = startMid - bottomSelect.getWidth() / 2;
	bottomSelect.setVisibility(View.VISIBLE);
	//int padding = getResources().getDimensionPixelSize(R.dimen.app_padding);
	int padding = 0;
	int startLeft = mRadioGroup.getChildAt(mCurrentSelectedIndex).getLeft()
		+ padding;
	// 目标位置中心点
	// int endMid = (mRadioGroup.getChildAt(selectIndex)).getLeft() +
	// (mRadioGroup.getChildAt(selectIndex)).getWidth() / 2;
	// 目标位置左边位置坐标
	// int endLeft = endMid - bottomSelect.getWidth() / 2;
	int endLeft = mRadioGroup.getChildAt(selectIndex).getLeft() + padding;
	if (selectIndex == 3) {
	    int bottomSelectWidth = bottomSelect.getWidth();
	    endLeft = (CommonHelper.getScreenWidth() - bottomSelectWidth) / 2;
	}
	TranslateAnimation animation = new TranslateAnimation(startLeft,
		endLeft, 0, 0);
	animation.setDuration(200);
	animation.setFillAfter(true);
	bottomSelect.bringToFront();
	bottomSelect.startAnimation(animation);
	// 改变当前选中按钮索引
	mCurrentSelectedIndex = selectIndex;

	// Log.i("fs", "endMid  " + endMid + "  startLeft  " + startLeft +
	// "  endLeft" + (endLeft - bottomSelect.getLeft()));

    }
}