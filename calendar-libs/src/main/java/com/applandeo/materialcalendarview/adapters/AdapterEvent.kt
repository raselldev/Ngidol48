package com.applandeo.materialcalendarview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.R
import com.applandeo.materialcalendarview.databinding.ItemEventDateBinding


class AdapterEvent(private var data: List<String>) : RecyclerView.Adapter<AdapterEvent.Holder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return  Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_event_date, parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.items.tvEvent.text = data[position]

    }

    inner class Holder(var items: ItemEventDateBinding) : RecyclerView.ViewHolder(items.root) {
    }

}