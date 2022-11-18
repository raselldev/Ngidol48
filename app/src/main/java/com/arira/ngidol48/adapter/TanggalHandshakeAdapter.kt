package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.TanggalHandshakeHolder
import com.arira.ngidol48.model.ParentHandshake
import com.arira.ngidol48.ui.activity.handshake.HandshakeCallback

class TanggalHandshakeAdapter(var list:List<ParentHandshake>, var callbcack:HandshakeCallback): RecyclerView.Adapter<TanggalHandshakeHolder>() {
    var active_id:String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TanggalHandshakeHolder {
        return  TanggalHandshakeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_tanggal_handshake, parent, false))
    }

    override fun onBindViewHolder(holder: TanggalHandshakeHolder, position: Int) {
        holder.setData(list[position], callbcack, active_id)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}