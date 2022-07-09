package com.arira.ngidol48.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.ChantHolder
import com.arira.ngidol48.model.Chant

class ChantAdapter(var list:ArrayList<Chant>): RecyclerView.Adapter<ChantHolder>(), Filterable {
    var data: ArrayList<Chant> = list
    var listFiltered: ArrayList<Chant> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChantHolder {
        return  ChantHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_chant, parent, false))
    }

    override fun onBindViewHolder(holder: ChantHolder, position: Int) {
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
                val result2 = java.util.ArrayList<Chant>()
                listFiltered = data
                if (constraint != null) {
                    if ((listFiltered.size > 0)) {
                        for (map in listFiltered) {
                            if (map.title.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                result2.add(map)
                            }else if (map.chant.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                result2.add(map)
                            }

                        }

                    }
                    results.values = result2
                }

                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                listFiltered = results.values as java.util.ArrayList<Chant>
                notifyDataSetChanged()
            }
        }
    }
}