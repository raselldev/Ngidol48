package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.ShowroomHistoryHolder
import com.arira.ngidol48.model.ShowroomHistory

class HistoryShowroomAdapter(var list:List<ShowroomHistory>): RecyclerView.Adapter<ShowroomHistoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowroomHistoryHolder {
        return  ShowroomHistoryHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_history_showroom, parent, false))
    }

    override fun onBindViewHolder(holder: ShowroomHistoryHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}