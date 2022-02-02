package com.arira.ngidol48.adapter.holder

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ItemLaguBinding
import com.arira.ngidol48.databinding.ItemSetlistBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE
import com.arira.ngidol48.helper.Helper
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.lagu.DetailLaguActivity
import com.arira.ngidol48.ui.lagu.LaguCallback
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import java.util.zip.GZIPOutputStream

class SongHolder(var item:ItemLaguBinding): RecyclerView.ViewHolder(item.root) {


    fun setData(data: Song, callback:LaguCallback?){
        item.tvJudul.text = data.judul
        if (data.lirik.isEmpty()){
            item.tvLirikTersedia.text = itemView.context.getString(R.string.teks_tidak_tersedia)
        }else{
            item.tvLirikTersedia.text = itemView.context.getString(R.string.teks_tersedia)
        }

        if (data.cover.isEmpty()){
            Glide.with(itemView.context).load(R.drawable.img_jkt48).into(item.ivCover)
        }else{
//            Glide.with(itemView.context).load(BASE_STORAGE  + data.cover).into(item.ivCover)

            Glide.with(itemView.context)
                .asBitmap()
                .load(BASE_STORAGE  + data.cover)
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
                Go(itemView.context).move(DetailLaguActivity::class.java, data = data)
            }
        }
    }
}