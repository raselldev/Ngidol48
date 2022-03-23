package com.arira.ngidol48.helper.uiImageSpan;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.DynamicDrawableSpan;

import java.lang.ref.WeakReference;

public class CenterImageSpan extends DynamicDrawableSpan {

    private WeakReference<Drawable> mDrawableRef;

    public CenterImageSpan(Drawable mDrawable) {
        mDrawableRef = new WeakReference<>(mDrawable);
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getCachedDrawable();
        Rect rect = d.getBounds();

        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getCachedDrawable();
        canvas.save();

        int transY = bottom - (b.getBounds().bottom / 2);
        Paint.FontMetricsInt fm = paint.getFontMetricsInt();
        transY -= fm.descent - (fm.ascent / 2);

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }

    @Override
    public Drawable getDrawable() {
        return mDrawableRef.get();
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = mDrawableRef;
        Drawable d = null;

        if (wr != null)
            d = wr.get();

        if (d == null) {
            d = getDrawable();
            mDrawableRef = new WeakReference<>(d);
        }

        return d;
    }
}
