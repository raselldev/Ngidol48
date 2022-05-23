package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.MemberMngByMemberHolder
import com.arira.ngidol48.model.Mng

class MemberMngByMemberAdapter(var list:List<Mng>): RecyclerView.Adapter<MemberMngByMemberHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberMngByMemberHolder {
        return  MemberMngByMemberHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_mng_member_list_2, parent, false))
    }

    override fun onBindViewHolder(holder: MemberMngByMemberHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}