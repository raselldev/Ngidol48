package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.KategoriNotifikasiHolder
import com.arira.ngidol48.ui.activity.notifikasi.NotificationCallback

class KategoriNotifikasiAdapter(var list: ArrayList<String>, var callback: NotificationCallback): RecyclerView.Adapter<KategoriNotifikasiHolder>() {
    var selectedCategori:String = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriNotifikasiHolder {
        return  KategoriNotifikasiHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_kategori_notifikasi, parent, false))
    }

    override fun onBindViewHolder(holder: KategoriNotifikasiHolder, position: Int) {
        holder.setData(list[position], callback, selectedCategori)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}