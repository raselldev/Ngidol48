package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.NotifikasiHolder
import com.arira.ngidol48.model.Notifikasi

class NotifikasiAdapter(var list:List<Notifikasi>): RecyclerView.Adapter<NotifikasiHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifikasiHolder {
        return  NotifikasiHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_notif, parent, false))
    }

    override fun onBindViewHolder(holder: NotifikasiHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}