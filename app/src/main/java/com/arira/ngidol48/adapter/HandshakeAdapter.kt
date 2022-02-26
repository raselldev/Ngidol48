package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.HandshakeHolder
import com.arira.ngidol48.model.ParentHandshake

class HandshakeAdapter(var list:List<ParentHandshake>): RecyclerView.Adapter<HandshakeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HandshakeHolder {
        return  HandshakeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_handshake, parent, false))
    }

    override fun onBindViewHolder(holder: HandshakeHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}