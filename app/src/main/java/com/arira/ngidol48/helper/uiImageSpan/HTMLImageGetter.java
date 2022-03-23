package com.arira.ngidol48.helper.uiImageSpan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.arira.ngidol48.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class HTMLImageGetter implements Html.ImageGetter {

    private Context context;
    private TextView textView = null;
    private int width;


    public HTMLImageGetter() {

    }

    public HTMLImageGetter(TextView target, Context ctx, int width) {
        textView = target;
        context = ctx;
        this.width = width;
    }

    @Override
    public Drawable getDrawable(String source) {
//        val vto: ViewTreeObserver = textView.rootView.getViewTreeObserver()
//        var width = 0
//        vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                textView.getViewTreeObserver().removeGlobalOnLayoutListener(this)
//                width = textView.rootView.getMeasuredWidth()
//                val height: Int = textView.rootView.getMeasuredHeight()
//
//                Log.e("HEL", "width :L ${width} || H : ${height}")
//            }
//        })

        ViewTreeObserver vto = textView.getRootView().getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
//            textView.getViewTreeObserver().removeOnGlobalFocusChangeListener(context);
            width = textView.getRootView().getMeasuredHeight();
        });

        if (width == 0){
            width = 600;
        }
        Log.e("WITDH", "widht : " + width);
        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
        Picasso.with(context)
                .load(source)
                .placeholder(R.drawable.img_upload_new)
                .resize(width, 600)
                .centerInside()
                .into(drawable);
        return drawable;
    }

    private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();

            Log.e("TES", "width : " + width);
            Log.e("TES", "height : " + height);

//            if(width > 600){
//                width = width - 100;
//                height = height - 100;
//            }else if (width > 700){
//                width = width - 200;
//                height = height - 200;
//            }else if (width > 800){
//                width = width - 300;
//                height = height - 300;
//            }else if (width > 900){
//                width = width - 400;
//                height = height - 400;
//            }else if (width > 1000 && width < 1300){
//                width = (width / 2)  - 100;
//                height = (height / 2)  - 100;
//            }else if (width > 1300 && width < 1500){
//                width = (width / 3)  - 100;
//                height = (height / 3)  - 100;
//            }else if(width > 1500){
//                width = (width / 4)  - 100;
//                height = (height / 4) - 100;
//            }

            Log.e("TES", "width : " + width);
            Log.e("TES", "height : " + height);


//            checkBounds();

            drawable.setBounds(0, 0, width, height);
            setBounds(0, 0, width, height);
            if (textView != null) {
                textView.setText(textView.getText());
            }
        }

        private void checkBounds() {
            float defaultProportion = (float) drawable.getIntrinsicWidth() / (float) drawable.getIntrinsicHeight();
            int width = Math.min(textView.getWidth(), drawable.getIntrinsicWidth());
            int height = (int) ((float) width / defaultProportion);

            if (getBounds().right != textView.getWidth() || getBounds().bottom != height) {

                setBounds(0, 0, textView.getWidth(), height); //set to full width

                int halfOfPlaceHolderWidth = (int) ((float) getBounds().right / 2f);
                int halfOfImageWidth = (int) ((float) width / 2f);

                drawable.setBounds(
                        halfOfPlaceHolderWidth - halfOfImageWidth, //centering an image
                        0,
                        halfOfPlaceHolderWidth + halfOfImageWidth,
                        height);

                textView.setText(textView.getText()); //refresh text
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setDrawable(new BitmapDrawable(context.getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
//            setDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
//            setDrawable(placeHolderDrawable);
        }

    }
}
