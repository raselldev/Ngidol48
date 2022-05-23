package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.MemberMngHolder
import com.arira.ngidol48.model.Mng

class MemberDetailMngAdapter(var list:List<Mng>): RecyclerView.Adapter<MemberMngHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberMngHolder {
        return  MemberMngHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_mng_member_list, parent, false))
    }

    override fun onBindViewHolder(holder: MemberMngHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}