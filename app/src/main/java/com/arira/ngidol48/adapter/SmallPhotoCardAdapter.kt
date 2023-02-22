package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.SmallPhotoCardHolder
import com.arira.ngidol48.model.PhotoCardImage
import com.arira.ngidol48.ui.activity.photoCard.listPhoto.ListPhotoCallback

class SmallPhotoCardAdapter(var list:List<PhotoCardImage>, var callback:ListPhotoCallback): RecyclerView.Adapter<SmallPhotoCardHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmallPhotoCardHolder {
        return  SmallPhotoCardHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_small_photo_card, parent, false))
    }

    override fun onBindViewHolder(holder: SmallPhotoCardHolder, position: Int) {
        holder.setData(list[position], callback)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}