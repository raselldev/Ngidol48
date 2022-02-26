package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemHandshakeMemberListBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.Handshake
import com.bumptech.glide.Glide

class MemberHandshakeHolder(var item:ItemHandshakeMemberListBinding): RecyclerView.ViewHolder(item.root) {
    fun setData(data: Handshake){
        item.tvNama.text = data.nama_member
        item.tvJalur.text = data.jalur

        if (data.is_sold == "1"){
            item.ivSold.visibility = View.VISIBLE
        }else{
            item.ivSold.visibility = View.GONE
        }
        if (data.avatar != null){
            Glide.with(itemView.context).load(BASE_STORAGE_JKT  + data.avatar).into(item.ivAva)
        }

    }
}