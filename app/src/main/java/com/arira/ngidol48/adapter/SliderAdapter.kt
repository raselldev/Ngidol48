package com.arira.ngidol48.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.viewpager.widget.PagerAdapter
import com.arira.ngidol48.R
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.Slider
import com.arira.ngidol48.ui.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import java.util.*

class SliderAdapter(var data: ArrayList<Slider>, var context: Context?) : PagerAdapter() {
    lateinit var layoutInflater: LayoutInflater

    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    @NonNull
    override fun instantiateItem(@NonNull container: ViewGroup, position: Int): Any {
        layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_slider, container, false)

        val imageView: ImageView = view.findViewById(R.id.iSlider_img_slider)
        Glide.with(context!!).load(data[position].image).into(imageView)

        imageView.setOnClickListener {
            if(data[position].value.contains("http")){
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(data[position].value)
                context!!.startActivity(openURL)
            }else{
                Go(context!!).move(MyWebActivity::class.java, url = BASE_STORAGE_JKT + data[position].value)
            }
        }

        container.addView(view, 0)

        return view
    }

    override fun destroyItem(@NonNull container: ViewGroup, position: Int, @NonNull `object`: Any) {
        container.removeView(`object` as View)
    }
}