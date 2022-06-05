package com.arira.ngidol48.utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.applandeo.materialcalendarview.CalendarUtils;
import com.arira.ngidol48.R;

public final class DrawableUtils {


    public static Drawable getCircleDrawableWithText(Context context, String string) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.background_color_circle_selector);

        Typeface typeface = ResourcesCompat.getFont(context, R.font.poppins);
        Drawable text = CalendarUtils.getDrawableText(context, string, typeface, android.R.color.white, 10);

        Drawable[] layers = {background, text};
        return new LayerDrawable(layers);
    }

    private DrawableUtils() {
    }
}