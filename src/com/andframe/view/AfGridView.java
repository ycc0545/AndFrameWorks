package com.andframe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.andframe.activity.framework.AfViewable;
import com.andframe.view.pulltorefresh.PullRefreshFooterImpl;
import com.andframe.view.pulltorefresh.PullRefreshHeaderImpl;

public class AfGridView extends AfRefreshAbsListView<GridView>{

	private static GridView mGridView = null;

	public AfGridView(GridView GridView) {
		super((mGridView=GridView).getContext());
		setPullFooterLayout(new PullRefreshFooterImpl(GridView.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(GridView.getContext()));
	}
	
	public AfGridView(AfViewable viewable,int res) {
		super((mGridView=viewable.findViewByID(res)).getContext());
		setPullFooterLayout(new PullRefreshFooterImpl(viewable.getContext()));
		setPullHeaderLayout(new PullRefreshHeaderImpl(viewable.getContext()));
	}
	
	public AfGridView(Context context) {
		super(context);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	public AfGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	public AfGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setPullFooterLayout(new PullRefreshFooterImpl(context));
		setPullHeaderLayout(new PullRefreshHeaderImpl(context));
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		mTargetView.setAdapter(adapter);
	}

	@Override
	protected GridView onCreateTargetView(Context context, AttributeSet attrs) {
		if (mGridView != null) {
			if (getParent() == null && mGridView.getParent() instanceof ViewGroup) {
				ViewGroup parent = ViewGroup.class.cast(mGridView.getParent());
				int index = parent.indexOfChild(mGridView);
				parent.removeView(mGridView);
				parent.addView(this, index,mGridView.getLayoutParams());
				mTargetView = mGridView;
				mGridView = null;
			}
			return mTargetView;
		}
		return new GridView(context);
	}

}
