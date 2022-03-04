package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.SongHolder
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.lagu.LaguCallback

class SongAdapter(var list:List<Song>, val callback:LaguCallback? = null, var laguSetlist:ArrayList<Song> = ArrayList()): RecyclerView.Adapter<SongHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        return  SongHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_lagu, parent, false))
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        holder.setData(list[position], callback, laguSetlist)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}