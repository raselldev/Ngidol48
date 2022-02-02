package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.BeritaHolder
import com.arira.ngidol48.model.Berita

class BeritaAdapter(var list:List<Berita>): RecyclerView.Adapter<BeritaHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeritaHolder {
        return  BeritaHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_berita, parent, false))
    }

    override fun onBindViewHolder(holder: BeritaHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}