package com.andframe.layoutbind;

import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.andframe.activity.framework.AfViewable;
import com.andframe.view.multichoice.AfMultiChoiceAdapter;
import com.andframe.view.multichoice.AfMultiChoiceAdapter.GenericityListener;
import com.andframe.widget.popupmenu.MenuEntity;
import com.andframe.widget.popupmenu.OnMenuItemClickListener;
import com.andframe.widget.popupmenu.PopupMenu;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class AfSelectorBottombar extends AfModuleAlpha implements
		OnClickListener, GenericityListener, OnMenuItemClickListener {

	public static final int MAX_ICON = 4;

	protected int function = 0;
	protected ImageView mMore;
	protected LinearLayout mContaint;
	protected OnMenuItemClickListener mListener;
	protected HashMap<String, Integer> mMeuns = new HashMap<>();

	protected abstract int getSelectorDrawableResId();
	protected abstract ImageView getFunctionViewMore(AfViewable view);
	protected abstract LinearLayout getFunctionLayout(AfViewable view);

	protected AfSelectorBottombar() {
	}

	protected AfSelectorBottombar(AfViewable view, int viewid) {
		super(view,viewid);
		initializeComponent(view);
//		if (isValid()) {
//			initView(view);
//		}
	}

	private void initView(AfViewable view) {
		mMore = getFunctionViewMore(view);
		mMore.setVisibility(GONE);
		mContaint = getFunctionLayout(view);
//			mContaint.removeAllViews();
		mMore.setOnClickListener(this);
		wrapped.setVisibility(View.GONE);
	}

	@Override
	protected void onCreated(AfViewable viewable, View view) {
		super.onCreated(viewable, view);
		initView(viewable);
	}

	public void addFunction(int id, int imageid, String detail) {
		if (mContaint.findViewById(id) == null) {
			if (function < MAX_ICON) {
				ImageView view = new ImageView(mMore.getContext());
				int backid = getSelectorDrawableResId();
				view.setId(id);
				view.setScaleType(mMore.getScaleType());
				view.setLayoutParams(mMore.getLayoutParams());
				view.setImageResource(imageid);
				view.setBackgroundResource(backid);
				view.setContentDescription(detail);
				view.setOnClickListener(this);
				mContaint.addView(view, mContaint.indexOfChild(mMore));
				if (function == MAX_ICON - 1) {
					putMeun(id, detail);
				}
			} else {
				if (mMore.getVisibility() != VISIBLE) {
					mMore.setVisibility(VISIBLE);
					mContaint.removeViewAt(mContaint.indexOfChild(mMore) - 1);
	//				mContaint.addView(mMore);
				}
				putMeun(id, detail);
			}
			function++;
		}
	}

	private void putMeun(int id, String detail) {
		if (mMeuns == null) {
            mMeuns = new HashMap<>();
        }
		mMeuns.put(detail, id);
	}

	public void setMenuItemListener(OnMenuItemClickListener listener) {
		mListener = listener;
	}
	
	public void setAdapter(AfMultiChoiceAdapter<?> adapter) {
		if (adapter != null){
			adapter.addGenericityListener(this);
			if (adapter.isMultiChoiceMode()!=isVisibility()){
				if (isVisibility()){
					this.hide();
				} else {
					this.show();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == mMore) {
			PopupMenu pm = new PopupMenu(v.getContext(), v);
			if (!pm.isValid()) {
				return;
			}
			Iterator<Entry<String, Integer>> iter = mMeuns.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Entry<String, Integer> entry = iter.next();
				pm.getMenu().add(1, entry.getValue(), 2, entry.getKey());
			}
			pm.setOnMenuItemClickListener(this);
			pm.show();
		} else if (mListener != null) {
			onMenuItemClick(new MenuEntity(v.getId(), v.getContentDescription()));
//			mListener.onMenuItemClick(new MenuEntity(v.getId(), v.getContentDescription()));
		}
	}

	@Override
	public boolean onMenuItemClick(MenuItem menu) {
		if (mListener != null) {
			return mListener.onMenuItemClick(menu);
		}
		return false;
	}

	@Override
	public void onMultiChoiceChanged(
			AfMultiChoiceAdapter<? extends Object> adapter, Object tag,
			boolean selected, int number) {

	}

	@Override
	public void onMultiChoiceChanged(
			AfMultiChoiceAdapter<? extends Object> adapter, int number,
			int total) {
	}
	
	@Override
	public void onMultiChoiceAddData(
			AfMultiChoiceAdapter<? extends Object> adapter,
			Collection<? extends Object> list) {
		
	}

	@Override
	public void onMultiChoiceStarted(
			AfMultiChoiceAdapter<? extends Object> adapter, int number) {
		if (function > 0) {
			this.show();
		}
	}

	@Override
	public void onMultiChoiceClosed(
			AfMultiChoiceAdapter<? extends Object> adapter,
			Collection<? extends Object> list) {
		if (function > 0) {
			this.hide();
		}
	}
	
}
