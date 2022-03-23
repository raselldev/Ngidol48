package com.arira.ngidol48.helper.uiTextView;


import android.text.Spannable;
import android.text.Spanned;
import android.text.style.CharacterStyle;

/**
 * class to hold the selection information
 */
public class SelectionInfo {
    
	private Object mSpan;
	private int mStart;
	private int mEnd;
	private Spannable mSpannable;
    
	public SelectionInfo() {
		clear();
	}
    
	public SelectionInfo(CharSequence text, Object span, int start, int end) {
		set(text, span, start, end);
	}
    
	/**
	 * select the {@link #getSpannable()} between the offsets {@link this.getStart()} and
	 * {@link #getEnd()}
	 */
	public void select() {
		select(mSpannable);
	}
    
	public void select(Spannable text) {
		if (text != null) {
			text.removeSpan(mSpan);
			text.setSpan(mSpan, Math.min(mStart, mEnd), Math.max(mStart, mEnd), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		}
	}
    
	/**
	 * remove the the selection
	 */
	public void remove() {
		remove(mSpannable);
	}
    
	public void remove(Spannable text) {
		if (text != null) {
			text.removeSpan(mSpan);
		}
	}
    
	public void clear() {
		mSpan = null;
		mSpannable = null;
		mStart = 0;
		mEnd = 0;
	}
    
	public void set(Object span, int start, int end) {
		mSpan = span;
		mStart = start;
		mEnd = end;
	}
    
	public void set(CharSequence text, Object span, int start, int end) {
		if (text instanceof Spannable) {
			mSpannable = (Spannable) text;
		}
		set(span, start, end);
	}
    
	public CharSequence getSelectedText() {
		if (mSpannable != null) {
			int start = Math.min(mStart, mEnd);
			int end = Math.max(mStart, mEnd);
            
			if (start >= 0 && end < mSpannable.length()) {
				return mSpannable.subSequence(start, end);
			}
		}
		return "";
	}
    
	public Object getSpan() {
		return mSpan;
	}
    
	public void setSpan(CharacterStyle span) {
		mSpan = span;
	}
    
    
	/**
	 * get the starting offset of the selection. Note the the starting offset is
	 * not necessarily smaller than the ending offset
	 * @return the starting offset of the selection
	 */
	public int getStart() {
		return mStart;
	}
    
	/**
	 * set the starting offset of the selection (inclusive)
	 * @param start the starting offset. It can be larger than {@link #getEnd()}
	 */
	public void setStart(int start) {
		assert (start >= 0);
		mStart = start;
	}
    
	/**
	 * get the ending offset of the selection. Note the the ending offset is
	 * not necessarily larger than the starting offset
	 * @return the ending offset of the selection
	 */
	public int getEnd() {
		return mEnd;
	}
    
	/**
	 * set the ending offset of the selection (exclusive)
	 * @param end the ending offset. It can be smaller than {@link #getStart()}
	 */
	public void setEnd(int end) {
		assert (end >= 0);
		mEnd = end;
	}
    
	public Spannable getSpannable() {
		return mSpannable;
	}
    
	public void setSpannable(Spannable spannable) {
		mSpannable = spannable;
	}
    
	/**
	 * Checks the weather the specified offset is within the range of the selection
	 * @param offset the offset to check
	 * @return true if the offset is within the range of the selection, false otherwise.
	 */
	public boolean offsetInSelection(int offset) {
		return (offset >= mStart && offset <= mEnd) ||
        (offset >= mEnd && offset <= mStart);
	}
}