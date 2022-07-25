package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.KategoriLaguHolder
import com.arira.ngidol48.model.LaguKategori
import com.arira.ngidol48.ui.activity.lagu.LaguCallback

class KategoriLaguAdapter(var list:List<LaguKategori>, val callback:LaguCallback? = null): RecyclerView.Adapter<KategoriLaguHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KategoriLaguHolder {
        return  KategoriLaguHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_kategori_lagu, parent, false))
    }

    override fun onBindViewHolder(holder: KategoriLaguHolder, position: Int) {
        holder.setData(list[position], callback)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}