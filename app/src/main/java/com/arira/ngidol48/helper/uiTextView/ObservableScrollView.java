package com.arira.ngidol48.helper.uiTextView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import java.util.ArrayList;

public class ObservableScrollView extends ScrollView {

	private ArrayList<OnScrollChangedListener> mOnScrollChangedListeners;
	
	@SuppressWarnings("unused")
	public ObservableScrollView(Context context) {
		super(context);
		init();
	}

	@SuppressWarnings("unused")
	public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	@SuppressWarnings("unused")
	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mOnScrollChangedListeners = new ArrayList<OnScrollChangedListener>(2);
	}

	public void addOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
		mOnScrollChangedListeners.add(onScrollChangedListener);
	}

	@SuppressWarnings("unused")
	public void removeOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
		mOnScrollChangedListeners.remove(onScrollChangedListener);
	}
	
	/**
	 * google should make this method public and add a setOnScrollChangedListener
	 * override to allow listener
	 */
	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		for (OnScrollChangedListener listener : mOnScrollChangedListeners) {
			listener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}
	
}
