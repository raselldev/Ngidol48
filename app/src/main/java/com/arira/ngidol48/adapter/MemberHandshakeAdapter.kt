package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.MemberHandshakeHolder
import com.arira.ngidol48.model.Handshake

class MemberDetailHandshakeAdapter(var list:List<Handshake>): RecyclerView.Adapter<MemberHandshakeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHandshakeHolder {
        return  MemberHandshakeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_handshake_member_list, parent, false))
    }

    override fun onBindViewHolder(holder: MemberHandshakeHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}