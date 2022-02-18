package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.ShowroomHolder
import com.arira.ngidol48.model.Member

class ShowroomAdapter(var list: List<Member>): RecyclerView.Adapter<ShowroomHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowroomHolder {
        return  ShowroomHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.view_showroom, parent, false))
    }

    override fun onBindViewHolder(holder: ShowroomHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}