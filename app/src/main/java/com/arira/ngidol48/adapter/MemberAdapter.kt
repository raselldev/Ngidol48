package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.MemberHolder
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.activity.member.MemberCallback

class MemberAdapter(var list:List<Member>, val callback: MemberCallback, var showtime:Boolean = false): RecyclerView.Adapter<MemberHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
        return  MemberHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_member, parent, false))
    }

    override fun onBindViewHolder(holder: MemberHolder, position: Int) {
        holder.setData(list[position], callback, showtime)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}