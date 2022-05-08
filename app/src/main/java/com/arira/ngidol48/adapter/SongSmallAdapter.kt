package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.SongSmallHolder
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.activity.lagu.LaguCallback

class SongSmallAdapter(var list:List<Song>, val callback: LaguCallback? = null): RecyclerView.Adapter<SongSmallHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongSmallHolder {
        return  SongSmallHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_lagu_small, parent, false))
    }

    override fun onBindViewHolder(holder: SongSmallHolder, position: Int) {
        holder.setData(list[position], callback)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}