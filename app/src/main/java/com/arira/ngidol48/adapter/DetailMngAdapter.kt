package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.DetailMngHolder
import com.arira.ngidol48.model.DetailMng

class DetailMngAdapter(var list:List<DetailMng>): RecyclerView.Adapter<DetailMngHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailMngHolder {
        return  DetailMngHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_detail_mng, parent, false))
    }

    override fun onBindViewHolder(holder: DetailMngHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}