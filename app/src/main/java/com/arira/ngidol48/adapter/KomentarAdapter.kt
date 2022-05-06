package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.KomentarHolder
import com.arira.ngidol48.model.Komentar
import com.arira.ngidol48.ui.detailBlog.KomentarCallback

class KomentarAdapter(var list:ArrayList<Komentar>, val callback:KomentarCallback): RecyclerView.Adapter<KomentarHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KomentarHolder {
        return  KomentarHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_komentar, parent, false))
    }

    override fun onBindViewHolder(holder: KomentarHolder, position: Int) {
        holder.setData(list[position], callback)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}