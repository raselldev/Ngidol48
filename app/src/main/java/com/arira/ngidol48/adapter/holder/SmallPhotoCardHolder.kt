package com.arira.ngidol48.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemSmallPhotoCardBinding
import com.arira.ngidol48.model.PhotoCardImage
import com.arira.ngidol48.ui.activity.photoCard.listPhoto.ListPhotoCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class SmallPhotoCardHolder(var item:ItemSmallPhotoCardBinding): RecyclerView.ViewHolder(item.root) {

    val context = itemView.context
    fun setData(data: PhotoCardImage, callback: ListPhotoCallback){

        Glide.with(context).load(data.image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(
                item.ivPhoto
            )
        item.ivPhoto.setOnClickListener {
            callback.onClick(data)
        }

        item.ivPhoto.setOnLongClickListener {
            callback.onLongClick(data)
            true
        }

    }
}