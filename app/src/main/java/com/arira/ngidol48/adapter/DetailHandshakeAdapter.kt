package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.DetailHandshakeHolder
import com.arira.ngidol48.model.DetailHandshake

class DetailHandshakeAdapter(var list:List<DetailHandshake>): RecyclerView.Adapter<DetailHandshakeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailHandshakeHolder {
        return  DetailHandshakeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_detail_handshake, parent, false))
    }

    override fun onBindViewHolder(holder: DetailHandshakeHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}