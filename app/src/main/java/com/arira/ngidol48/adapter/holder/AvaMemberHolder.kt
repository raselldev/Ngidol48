package com.arira.ngidol48.adapter.holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemAvaMemberBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.Member
import com.bumptech.glide.Glide

class AvaMemberHolder(var item:ItemAvaMemberBinding): RecyclerView.ViewHolder(item.root) {

    var detail = false

    fun setData(data: Member, size:Int){
        if (absoluteAdapterPosition == size - 1){
            val param = item.linMainView.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,0,0,0)
            item.linMainView.layoutParams = param
        }else{
            val param = item.linMainView.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(0,0,20,0)
            item.linMainView.layoutParams = param
        }
        Glide.with(itemView.context).load(BASE_STORAGE_JKT  + data.avatar).into(item.ivAva)
    }
}