package com.arira.ngidol48.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.EventHolder
import com.arira.ngidol48.model.Event

class EventAdapter(var list:ArrayList<Event>): RecyclerView.Adapter<EventHolder>(), Filterable {
    var data: ArrayList<Event> =  list
    var listFiltered: ArrayList<Event> = list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        return  EventHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_event, parent, false))
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.setData(listFiltered[position])
    }

    override fun getItemCount(): Int {
        return  listFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val result2 = java.util.ArrayList<Event>()
                listFiltered = data
                if (constraint != null) {
                    if ((listFiltered.size > 0)) {
                        for (map in listFiltered) {
                            if (map.event_name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                result2.add(map)
                            }else if (map.bulan_tahun.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                result2.add(map)
                            }

                        }

                    }
                    results.values = result2
                }

                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                listFiltered = results.values as java.util.ArrayList<Event>
                notifyDataSetChanged()
            }
        }
    }
}