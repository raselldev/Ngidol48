package com.arira.ngidol48.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.adapter.SignAdapter
import com.arira.ngidol48.databinding.ItemSignBinding
import com.arira.ngidol48.model.PhotoCardSign
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class SignHolder(var item:ItemSignBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(sign: PhotoCardSign, callback: SignAdapter.Callback){
        Glide.with(itemView.context).load(sign.image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(
                item.ivSign
            )

        item.ivSign.rootView.setOnClickListener {
            callback.onClick(sign)
        }
    }
}