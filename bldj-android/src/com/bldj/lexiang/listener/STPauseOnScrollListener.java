package com.bldj.lexiang.listener;

import android.widget.AbsListView;

import com.bldj.lexiang.view.SuperListView;
import com.bldj.universalimageloader.core.ImageLoader;
import com.bldj.universalimageloader.core.listener.PauseOnScrollListener;

public class STPauseOnScrollListener extends PauseOnScrollListener {

	private SuperListView mSuperLV;

	public STPauseOnScrollListener(ImageLoader imageLoader,
			boolean pauseOnScroll, boolean pauseOnFling, SuperListView superLV) {
		super(imageLoader, pauseOnScroll, pauseOnFling);
		mSuperLV = superLV;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		super.onScrollStateChanged(view, scrollState);
		mSuperLV.onScrollStateChanged(view, scrollState);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		mSuperLV.onScroll(view, firstVisibleItem, visibleItemCount,
				totalItemCount);
	}

}
