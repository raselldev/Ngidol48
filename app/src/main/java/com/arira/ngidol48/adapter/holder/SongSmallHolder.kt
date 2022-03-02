package com.arira.ngidol48.adapter.holder

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ItemLaguSmallBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE
import com.arira.ngidol48.helper.Helper
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.lagu.DetailLaguActivity
import com.arira.ngidol48.ui.lagu.LaguCallback
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

class SongSmallHolder(var item:ItemLaguSmallBinding): RecyclerView.ViewHolder(item.root) {


    fun setData(data: Song, callback:LaguCallback?){
        item.tvJudul.text = data.judul

        if (data.cover.isEmpty()){
            Glide.with(itemView.context).load(R.drawable.img_jkt48).into(item.ivCover)
        }else{
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
                callback.onSelectOtherSong(data)
            }
        }
    }
}