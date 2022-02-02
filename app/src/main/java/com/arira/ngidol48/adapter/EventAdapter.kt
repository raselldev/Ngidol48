package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.EventHolder
import com.arira.ngidol48.model.Event

class EventAdapter(var list:List<Event>): RecyclerView.Adapter<EventHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        return  EventHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_event, parent, false))
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}