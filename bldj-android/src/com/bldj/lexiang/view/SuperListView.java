package com.bldj.lexiang.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bldj.lexiang.R;
import com.bldj.lexiang.adapter.BaseListAdapter;
import com.bldj.lexiang.utils.CommonHelper;
import com.bldj.lexiang.utils.TimeUtils;

public class SuperListView extends ListView implements OnScrollListener {

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;

	private final static int RATIO = 3;


	private LinearLayout mHeadView;
	private TextView mTipsTextview;
	private TextView mLastUpdatedTextView;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;

	private RotateAnimation mAnimation;
	private RotateAnimation mReverseAnimation;
	private boolean mIsRecored;
	private int mHeadContentHeight;
	private int mStartY;
	private int mFirstItemIndex;
	private int mCurrentState;
	private boolean mIsBack;
	private OnRefreshListener mRefreshListener;
	private boolean mIsRefreshable;

	public RelativeLayout mFooterRelative;
	public TextView mFooterTextView;
	public ProgressBar mFooterProgressBar;
	public int mLastItem;
	private boolean mIsOnLoading;
	private boolean isLastPage = false;

	private OnLoadMoreListener onLoadMoreListener;
	private Context mContext;
	public boolean mBusy = false;

	// 阻尼最大距离
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;
	private int mMaxYOverscrollDistance;

	// 上/下拉的超时处理
	private final int TIMEOUT_TIME = 8000;// 超时时间(与联网模块保持一致,5+3=8秒)
	private boolean isRefreshSuccess = false;
	private boolean isLoadmoreSuccess = false;
	private final int TIMEOUT_REFRESH = 0;// 下拉刷新超时
	private final int TIMEOUT_LOADMORE = 1;// 载入更多超时
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case TIMEOUT_REFRESH:
				if (!isRefreshSuccess) {// 下拉刷新等待数据超时
					onRefreshComplete();
					Toast.makeText(mContext, R.string.NETWORK_ERROR,
							Toast.LENGTH_SHORT).show();
				}
				break;
			case TIMEOUT_LOADMORE:
				if (!isLoadmoreSuccess) {// 载入更多等待数据超时
					onLoadMoreComplete();
					Toast.makeText(mContext, R.string.NETWORK_ERROR,
							Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	};

	public SuperListView(Context context) {
		super(context);
		mContext = context;
		init(context);
	}

	public SuperListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(context);
	}

	private void init(Context context) {
		setCacheColorHint(context.getResources().getColor(
				android.R.color.transparent));
		//mInflater = LayoutInflater.from(context);
		mHeadView = new LinearLayout(context);		
		ListView.LayoutParams headViewParams = new ListView.LayoutParams(
				ListView.LayoutParams.MATCH_PARENT,
				ListView.LayoutParams.WRAP_CONTENT);
		//headViewParams.rightMargin = 60;
		mHeadView.setLayoutParams(headViewParams);
		//mHeadView.setPadding(0, 0, 60, 0);
		
		LinearLayout content = new LinearLayout(context);
		LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		content.setLayoutParams(contentParams);		
		content.setOrientation(LinearLayout.HORIZONTAL);
		content.setGravity(Gravity.CENTER_HORIZONTAL);
		content.setPadding(0, 15, 0, 15);
		
		RelativeLayout leftPartView = new RelativeLayout(context);
		LinearLayout.LayoutParams leftViewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		leftViewParams.rightMargin = 45;
        leftViewParams.gravity = Gravity.CENTER_VERTICAL;
		leftPartView.setLayoutParams(leftViewParams);
		
		mArrowImageView = new ImageView(context);
		RelativeLayout.LayoutParams arrowViewParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		arrowViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mArrowImageView.setLayoutParams(arrowViewParams);
		mArrowImageView.setImageDrawable(CommonHelper.getAssertDrawable(context, com.bldj.lexiang.utils.R.drawable.pull_arrow_down));
		mArrowImageView.setMinimumWidth(70);
		mArrowImageView.setMinimumHeight(50);
		
		
		mProgressBar = new ProgressBar(context);
		RelativeLayout.LayoutParams pbViewParams = new RelativeLayout.LayoutParams(
				30,
				30);
		pbViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		mProgressBar.setLayoutParams(pbViewParams);
		mProgressBar.setIndeterminate(false);
		
		RotateAnimation ra = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setRepeatCount(Animation.INFINITE);
		//ra.start();
		//mProgressBar.startAnimation(ra);
		mProgressBar.setIndeterminateDrawable(CommonHelper.getAssertDrawable(context, com.bldj.lexiang.utils.R.drawable.loading));
		mProgressBar.setVisibility(View.GONE);
		
		leftPartView.addView(mArrowImageView);
		leftPartView.addView(mProgressBar);
		
		LinearLayout rightPartView = new LinearLayout(context);
		LinearLayout.LayoutParams rightViewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);		
		rightPartView.setLayoutParams(rightViewParams);
		rightPartView.setGravity(Gravity.CENTER_HORIZONTAL);
		rightPartView.setOrientation(LinearLayout.VERTICAL);
		
		mTipsTextview = new TextView(context);
		LinearLayout.LayoutParams tipViewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tipViewParams.topMargin = 7;
		mTipsTextview.setLayoutParams(tipViewParams);
		mTipsTextview.setText(R.string.PULL_DOWN);
		mTipsTextview.setTextColor(Color.parseColor("#666666"));
		mTipsTextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		
		
		mLastUpdatedTextView = new TextView(context);
		LinearLayout.LayoutParams updateViewParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		mLastUpdatedTextView.setLayoutParams(updateViewParams);
		mLastUpdatedTextView.setText(context.getString(R.string.LAST_UPDATE_TIME));
		mLastUpdatedTextView.setTextColor(Color.parseColor("#999999"));
		mLastUpdatedTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
		
		rightPartView.addView(mTipsTextview);
		rightPartView.addView(mLastUpdatedTextView);
		
		content.addView(leftPartView);
		content.addView(rightPartView);
		mHeadView.addView(content);
		
		measureView(mHeadView);
		mHeadContentHeight = mHeadView.getMeasuredHeight();
		mHeadView.setPadding(0, -1 * mHeadContentHeight, 60, 0);
		mHeadView.invalidate();
		addHeaderView(mHeadView, null, false);
		setOnScrollListener(this);
		mAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mAnimation.setInterpolator(new LinearInterpolator());
		mAnimation.setDuration(250);
		mAnimation.setFillAfter(true);

		mReverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseAnimation.setInterpolator(new LinearInterpolator());
		mReverseAnimation.setDuration(200);
		mReverseAnimation.setFillAfter(true);
		
		
		
		mCurrentState = DONE;
		mIsRefreshable = false;
		mIsOnLoading = false;
		
		
		
		mFooterRelative = new RelativeLayout(context);
		ListView.LayoutParams footViewParams = new ListView.LayoutParams(
				ListView.LayoutParams.MATCH_PARENT,
				ListView.LayoutParams.WRAP_CONTENT);
		mFooterRelative.setLayoutParams(footViewParams);
		
		LinearLayout linView = new LinearLayout(context);
		RelativeLayout.LayoutParams linParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);	
		linParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		linView.setLayoutParams(linParams);
		linView.setOrientation(LinearLayout.HORIZONTAL);
		linView.setPadding(0, 15, 0, 15);
		
		mFooterTextView = new TextView(context);
		LinearLayout.LayoutParams footTextParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);	
		footTextParams.leftMargin = 15;
		footTextParams.topMargin = 3;
		footTextParams.bottomMargin = 3;
		footTextParams.gravity = Gravity.CENTER_VERTICAL;
		mFooterTextView.setLayoutParams(footTextParams);
		mFooterTextView.setText(R.string.loading);
		
		mFooterProgressBar = new ProgressBar(context,null, android.R.attr.progressBarStyleSmall);
		LinearLayout.LayoutParams footPBParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);	
		footPBParams.topMargin = 3;
		footPBParams.bottomMargin = 3;
		mFooterProgressBar.setLayoutParams(footPBParams);
		
		linView.addView(mFooterProgressBar);
		linView.addView(mFooterTextView);
		mFooterRelative.addView(linView);
		
		// 阻尼滑动相关
		initBounceListView();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mIsRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (mFirstItemIndex == 0 && !mIsRecored) {
					mIsRecored = true;
					mStartY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (mCurrentState != REFRESHING && mCurrentState != LOADING) {
					if (mCurrentState == DONE) {
					}
					if (mCurrentState == PULL_To_REFRESH) {
						mCurrentState = DONE;
						changeHeaderViewByState();
					}
					if (mCurrentState == RELEASE_To_REFRESH) {
						mCurrentState = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}
				mIsRecored = false;
				mIsBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!mIsRecored && mFirstItemIndex == 0) {
					mIsRecored = true;
					mStartY = tempY;
				}
				if (mCurrentState != REFRESHING && mIsRecored
						&& mCurrentState != LOADING) {
					if (mCurrentState == RELEASE_To_REFRESH) {
						setSelection(0);
						if (((tempY - mStartY) / RATIO < mHeadContentHeight)
								&& (tempY - mStartY) > 0) {
							mCurrentState = PULL_To_REFRESH;
							changeHeaderViewByState();
						} else if (tempY - mStartY <= 0) {
							mCurrentState = DONE;
							changeHeaderViewByState();
						}
					}
					if (mCurrentState == PULL_To_REFRESH) {
						setSelection(0);
						if ((tempY - mStartY) / RATIO >= mHeadContentHeight) {
							mCurrentState = RELEASE_To_REFRESH;
							mIsBack = true;
							changeHeaderViewByState();
						} else if (tempY - mStartY <= 0) {
							mCurrentState = DONE;
							changeHeaderViewByState();
						}
					}
					if (mCurrentState == DONE) {
						if (tempY - mStartY > 0) {
							mCurrentState = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}
					if (mCurrentState == PULL_To_REFRESH) {
						mHeadView.setPadding(0, -1 * mHeadContentHeight
								+ (tempY - mStartY) / RATIO, 0, 0);
					}
					if (mCurrentState == RELEASE_To_REFRESH) {
						mHeadView.setPadding(0, (tempY - mStartY) / RATIO
								- mHeadContentHeight, 0, 0);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	private void changeHeaderViewByState() {
		switch (mCurrentState) {
		case RELEASE_To_REFRESH:
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			mTipsTextview.setVisibility(View.VISIBLE);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.startAnimation(mAnimation);
			mTipsTextview.setText(R.string.REMIND_PULL);
			break;
		case PULL_To_REFRESH:
			mProgressBar.setVisibility(View.GONE);
			mTipsTextview.setVisibility(View.VISIBLE);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.VISIBLE);
			if (mIsBack) {
				mIsBack = false;
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mReverseAnimation);
				mTipsTextview.setText(R.string.PULL_DOWN);
			} else {
				mTipsTextview.setText(R.string.PULL_DOWN);
			}
			break;
		case REFRESHING:
			mHeadView.setPadding(0, 0, 0, 0);
			mProgressBar.setVisibility(View.VISIBLE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			mTipsTextview.setText(R.string.REFLESHING);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		case DONE:
			mHeadView.setPadding(0, -1 * mHeadContentHeight, 0, 0);
			mProgressBar.setVisibility(View.GONE);
			mArrowImageView.clearAnimation();
			mArrowImageView.setImageDrawable(CommonHelper.getAssertDrawable(mContext, com.bldj.lexiang.utils.R.drawable.pull_arrow_down));
			mTipsTextview.setText(R.string.REFLESH_DONE);
			mLastUpdatedTextView.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.mRefreshListener = refreshListener;
		if (refreshListener != null)
			mIsRefreshable = true;
		else
			mIsRefreshable = false;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public void onRefreshComplete() {
		mHandler.removeMessages(TIMEOUT_REFRESH);
		mIsOnLoading = false;
		isRefreshSuccess = true;
		mCurrentState = DONE;
		mLastUpdatedTextView.setText(mContext.getString(R.string.LAST_UPDATE_TIME)
				+ TimeUtils.getCurrentDateTime());
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (mRefreshListener != null && !mIsOnLoading) {
			mIsOnLoading = true;
			mRefreshListener.onRefresh();
			isRefreshSuccess = false;
			Message msg = Message.obtain();
			msg.what = TIMEOUT_REFRESH;
			mHandler.sendMessageDelayed(msg, TIMEOUT_TIME);
		}
	}

	public interface OnLoadMoreListener {
		public void onLoadMore();
	}

	public void setOnLoadMoreListener(OnLoadMoreListener loadMore) {
		this.onLoadMoreListener = loadMore;
	}

	private void onLoadMore() {
		if (getFooterViewsCount() == 0) {
			addFooterView(mFooterRelative);
		}

		if (onLoadMoreListener != null && !mIsOnLoading) {
			mIsOnLoading = true;
			onLoadMoreListener.onLoadMore();
			isLoadmoreSuccess = false;
			Message msg = Message.obtain();
			msg.what = TIMEOUT_LOADMORE;
			mHandler.sendMessageDelayed(msg, TIMEOUT_TIME);
		}
	}

	public void onLoadMoreComplete() {
		mHandler.removeMessages(TIMEOUT_LOADMORE);
		isLoadmoreSuccess = true;
		mIsOnLoading = false;
		if (getFooterViewsCount() > 0) {
			removeFooterView(mFooterRelative);
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}

		child.measure(childWidthSpec, childHeightSpec);
	}

	public void setAdapter(BaseAdapter adapter) {
		mLastUpdatedTextView.setText(mContext.getString(R.string.LAST_UPDATE_TIME)
				+ TimeUtils.getCurrentDateTime());
		super.setAdapter(adapter);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		mFirstItemIndex = firstVisibleItem;
		mLastItem = firstVisibleItem + visibleItemCount;
	}

	private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (this.getAdapter() == null) {
			return;
		}
		if (this.getAdapter().getCount() == 0) {
			return;
		}
		if (mIsOnLoading) {
			return;
		}
		if (mLastItem == this.getAdapter().getCount()
				&& scrollState == SCROLL_STATE_IDLE && !isLastPage) {
			onLoadMore();
		}
		// TODO mBusy状态暂时未使用,先注释掉scrollState中的操作,先不要删掉
		switch (scrollState) {
		case OnScrollListener.SCROLL_STATE_IDLE:
			checkIsScrollToBottom();
			// mBusy = false;
			// if (getAdapter() instanceof HeaderViewListAdapter) {
			// if (((HeaderViewListAdapter) getAdapter()).getWrappedAdapter()
			// instanceof BaseListAdapter) {
			// ((BaseListAdapter) ((HeaderViewListAdapter)
			// getAdapter()).getWrappedAdapter()).setBusyState(mBusy);
			// post(loadDateRunnable);
			// }
			// }
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			// mBusy = false;
			// if (getAdapter() instanceof HeaderViewListAdapter) {
			// if (((HeaderViewListAdapter) getAdapter()).getWrappedAdapter()
			// instanceof BaseListAdapter) {
			// ((BaseListAdapter) ((HeaderViewListAdapter)
			// getAdapter()).getWrappedAdapter()).setBusyState(mBusy);
			// }
			// }
			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			// mBusy = true;
			// if (getAdapter() instanceof HeaderViewListAdapter) {
			// if (((HeaderViewListAdapter) getAdapter()).getWrappedAdapter()
			// instanceof BaseListAdapter) {
			// ((BaseListAdapter) ((HeaderViewListAdapter)
			// getAdapter()).getWrappedAdapter()).setBusyState(mBusy);
			// }
			// }
			break;
		default:
			break;
		}
	}

	public void setLastPage() {
		isLastPage = true;
		removeFooterView(mFooterRelative);
	}

	public void cancelLastPage() {
		isLastPage = false;
		if (getFooterViewsCount() == 0)
			addFooterView(mFooterRelative);
	}

	@SuppressWarnings("unused")
	private Runnable loadDateRunnable = new Runnable() {
		public void run() {
			loadData();
		}
	};

	private void loadData() {
		((BaseListAdapter) ((HeaderViewListAdapter) getAdapter())
				.getWrappedAdapter()).notifyDataSetChanged();
	}

	private void checkIsScrollToBottom() {
		// 滚动到底部
		if (getLastVisiblePosition() == (getCount() - 1)) {
			View itemview = (View) getChildAt(getChildCount() - 1);
			int[] location = new int[2];
			itemview.getLocationOnScreen(location);// 获取在整个屏幕内的绝对坐标
			int y = location[1];
			if (getLastVisiblePosition() != getLastVisiblePosition
					&& lastVisiblePositionY != y)// 第一次拖至底部
			{
				getLastVisiblePosition = getLastVisiblePosition();
				lastVisiblePositionY = y;
                if(isLastPage){
                    Toast.makeText(mContext, R.string.LIST_LAST_PAGE,
                            Toast.LENGTH_SHORT).show();
                }
				return;
			} else if (getLastVisiblePosition() == getLastVisiblePosition
					&& lastVisiblePositionY == y)// 第二次拖至底部
			{
				Toast.makeText(mContext, R.string.LIST_LAST_PAGE,
						Toast.LENGTH_SHORT).show();
			}
		}
		// 未滚动到底部，第二次拖至底部都初始化
		getLastVisiblePosition = 0;
		lastVisiblePositionY = 0;
	}

	private void initBounceListView() {
		final DisplayMetrics metrics = mContext.getResources()
				.getDisplayMetrics();
		final float density = metrics.density;
		mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		if (isTouchEvent) {
			if (0 <= scrollY && 0 < deltaY) {
				return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
						scrollRangeX, scrollRangeY, maxOverScrollX,
						mMaxYOverscrollDistance, isTouchEvent);
			}
			return false;
		} else {
			return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
					scrollRangeX, scrollRangeY, maxOverScrollX,
					mMaxYOverscrollDistance, isTouchEvent);
		}
	}
}