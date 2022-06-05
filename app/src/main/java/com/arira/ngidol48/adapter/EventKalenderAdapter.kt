package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.EventKalenderHolder
import com.arira.ngidol48.model.Event

class EventKalenderAdapter(var list:List<Event>): RecyclerView.Adapter<EventKalenderHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventKalenderHolder {
        return  EventKalenderHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_event_kalender, parent, false))
    }

    override fun onBindViewHolder(holder: EventKalenderHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }

}