package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.MemberHandshakeByMemberHolder
import com.arira.ngidol48.model.Handshake

class MemberHandshakeByMemberAdapter(var list:List<Handshake>): RecyclerView.Adapter<MemberHandshakeByMemberHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHandshakeByMemberHolder {
        return  MemberHandshakeByMemberHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_handshake_member_list_2, parent, false))
    }

    override fun onBindViewHolder(holder: MemberHandshakeByMemberHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}