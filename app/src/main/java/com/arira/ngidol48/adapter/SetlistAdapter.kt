package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.SetlistHolder
import com.arira.ngidol48.model.Setlist

class SetlistAdapter(var list:List<Setlist>): RecyclerView.Adapter<SetlistHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetlistHolder {
        return  SetlistHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_setlist, parent, false))
    }

    override fun onBindViewHolder(holder: SetlistHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}