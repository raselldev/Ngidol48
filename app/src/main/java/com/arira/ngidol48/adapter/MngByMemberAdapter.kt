package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.MngByMemberHolder
import com.arira.ngidol48.model.DetailMng

class MngByMemberAdapter(var list:List<DetailMng>): RecyclerView.Adapter<MngByMemberHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MngByMemberHolder {
        return  MngByMemberHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_mng_by_member, parent, false))
    }

    override fun onBindViewHolder(holder: MngByMemberHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}