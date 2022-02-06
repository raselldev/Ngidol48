package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.EventHomeHolder
import com.arira.ngidol48.model.Event

class EventHomeAdapter(var list:List<Event>): RecyclerView.Adapter<EventHomeHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHomeHolder {
        return  EventHomeHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_event_home, parent, false))
    }

    override fun onBindViewHolder(holder: EventHomeHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}