package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.MerchHolder
import com.arira.ngidol48.model.Merch

class MerchAdapter(var list:List<Merch>): RecyclerView.Adapter<MerchHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchHolder {
        return  MerchHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_merch, parent, false))
    }

    override fun onBindViewHolder(holder: MerchHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}