package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.HandshakeByMemberHolder
import com.arira.ngidol48.model.DetailHandshake

class HandshakeByMemberAdapter(var list:List<DetailHandshake>): RecyclerView.Adapter<HandshakeByMemberHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandshakeByMemberHolder {
        return  HandshakeByMemberHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_handshake_by_member, parent, false))
    }

    override fun onBindViewHolder(holder: HandshakeByMemberHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}