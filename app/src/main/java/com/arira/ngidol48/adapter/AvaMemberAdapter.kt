package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.AvaMemberHolder
import com.arira.ngidol48.model.Member

class AvaMemberAdapter(var list: List<Member>,var size: Int): RecyclerView.Adapter<AvaMemberHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvaMemberHolder {
        return  AvaMemberHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_ava_member, parent, false))
    }

    override fun onBindViewHolder(holder: AvaMemberHolder, position: Int) {
        holder.setData(list[position], size)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}