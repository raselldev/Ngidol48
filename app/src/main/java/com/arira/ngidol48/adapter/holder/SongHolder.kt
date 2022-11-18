package com.arira.ngidol48.adapter.holder

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ItemLaguBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE
import com.arira.ngidol48.helper.Helper
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.activity.detailLagu.DetailLaguActivity
import com.arira.ngidol48.ui.activity.lagu.LaguCallback
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget


class SongHolder(var item:ItemLaguBinding): RecyclerView.ViewHolder(item.root) {


    fun setData(data: Song, callback: LaguCallback?, laguSetlist: ArrayList<Song>){
        item.tvJudul.text = data.judul
        var requestOptions = RequestOptions()
        requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(16))

        if (data.lirik.isEmpty()){
            item.tvLirikTersedia.text = itemView.context.getString(R.string.teks_lirik_tidak_tersedia)
        }else{
            item.tvLirikTersedia.text = itemView.context.getString(R.string.teks_lirik_tersedia)
        }

        if (data.cover.isEmpty()){
            Glide.with(itemView.context)
                .load(R.drawable.img_jkt48)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                .into(item.ivCover)
        }
        else{

            Glide.with(itemView.context)
                .asBitmap()
                .load(BASE_STORAGE  + data.cover)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        item.ivCover.setImageBitmap(resource)
                        item.ivCover.setBackgroundColor(Helper.getDominantColor(resource))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }
        item.tvJudul.rootView.setOnClickListener {
            if (callback != null){
                callback.onSelectSong(data)
            }else{
                Go(itemView.context).move(DetailLaguActivity::class.java, data = data, listData = laguSetlist)
            }
        }
    }
}