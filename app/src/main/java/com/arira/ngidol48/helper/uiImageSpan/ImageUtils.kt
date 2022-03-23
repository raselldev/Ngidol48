package com.arira.ngidol48.helper.uiImageSpan

import android.text.Spannable
import android.text.style.ImageSpan
import android.text.style.URLSpan
import android.view.View


class ImageUtils {
    fun setClickListenerOnHtmlImageGetter(html: Spannable, callback: CallbackImageURL) {
        for (span in html.getSpans(0, html.length, ImageSpan::class.java)) {
            val flags = html.getSpanFlags(span)
            val start = html.getSpanStart(span)
            val end = html.getSpanEnd(span)
            html.setSpan(object : URLSpan(span.source) {
                override fun onClick(v: View) {
                    callback.onImageClick(span.source)
                }
            }, start, end, flags)
        }
    }

    interface CallbackImageURL {
        fun onImageClick(imageUrl: String?)
    }
}