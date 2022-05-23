package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemHandshakeMemberList2Binding
import com.arira.ngidol48.databinding.ItemMngMemberList2Binding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.Handshake
import com.arira.ngidol48.model.Mng
import com.bumptech.glide.Glide

class MemberMngByMemberHolder(var item:ItemMngMemberList2Binding): RecyclerView.ViewHolder(item.root) {
    fun setData(data: Mng){
        item.tvNama.text = data.nama_member
        item.tvJalur.text = "${data.sesi} - ${data.jalur}"
        item.tvWaktu.text = data.waktu.replace("-", " - ")
        item.tvStandby.text = data.penukaran.replace("-", " - ")

        if (data.is_sold == "1"){
            item.linSold.visibility = View.VISIBLE
        }else{
            item.linSold.visibility = View.GONE
        }
        if (data.avatar != null){
            Glide.with(itemView.context).load(BASE_STORAGE_JKT  + data.avatar).into(item.ivAva)
        }

    }
}