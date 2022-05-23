package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemHandshakeMemberListBinding
import com.arira.ngidol48.databinding.ItemMngMemberListBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.Handshake
import com.arira.ngidol48.model.Mng
import com.arira.ngidol48.ui.activity.handshakeMember.HandshakeByMemberActivity
import com.arira.ngidol48.ui.activity.mng.MngByMemberActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide

class MemberMngHolder(var item:ItemMngMemberListBinding): RecyclerView.ViewHolder(item.root) {
    fun setData(data: Mng){
        item.tvNama.text = data.nama_member
        item.tvJalur.text = data.jalur

        if (data.is_sold == "1"){
            item.linSold.visibility = View.VISIBLE
        }else{
            item.linSold.visibility = View.GONE
        }
        if (data.avatar != null){
            Glide.with(itemView.context).load(BASE_STORAGE_JKT  + data.avatar).into(item.ivAva)
        }

        item.tvJalur.rootView.setOnClickListener {
            Go(itemView.context).move(MngByMemberActivity::class.java, id = data.id_member)
        }

    }
}