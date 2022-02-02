package com.arira.ngidol48.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemMemberBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.member.MemberCallback
import com.bumptech.glide.Glide

class MemberHolder(var item:ItemMemberBinding): RecyclerView.ViewHolder(item.root) {

    var detail = false

    fun setData(data: Member, callback:MemberCallback){
        item.tvNama.text = data.nama_lengkap
        item.tvTeam.text = data.kategori

        Glide.with(itemView.context).load(BASE_STORAGE_JKT  + data.avatar).into(item.ivAva)

        item.tvTeam.rootView.setOnClickListener {
            callback.onSelect(data)
        }
    }
}