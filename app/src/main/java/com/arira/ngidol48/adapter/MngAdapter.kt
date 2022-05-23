package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.MngHolder
import com.arira.ngidol48.model.ParentMng

class MngAdapter(var list:List<ParentMng>): RecyclerView.Adapter<MngHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MngHolder {
        return  MngHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_mng, parent, false))
    }

    override fun onBindViewHolder(holder: MngHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}