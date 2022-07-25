package com.arira.ngidol48.adapter.holder

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.adapter.SongSmallAdapter
import com.arira.ngidol48.databinding.ItemKategoriLaguBinding
import com.arira.ngidol48.model.LaguKategori
import com.arira.ngidol48.ui.activity.lagu.LaguCallback

class KategoriLaguHolder(var item:ItemKategoriLaguBinding): RecyclerView.ViewHolder(item.root) {

    var detail = false

    fun setData(data: LaguKategori, callback: LaguCallback?){
        item.tvKategori.text = data.nama_kategori
        item.rvData.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = SongSmallAdapter(data.song, callback, true)
        }
    }
}