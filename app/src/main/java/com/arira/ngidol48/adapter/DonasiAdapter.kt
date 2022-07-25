package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.DonasiHolder
import com.arira.ngidol48.model.Donasi

class DonasiAdapter(var list:List<Donasi>): RecyclerView.Adapter<DonasiHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonasiHolder {
        return  DonasiHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_donasi, parent, false))
    }

    override fun onBindViewHolder(holder: DonasiHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}