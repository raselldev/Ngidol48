package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.LiveChatHolder
import com.arira.ngidol48.model.LiveChat

class LiveChatAdapter(var list:ArrayList<LiveChat>): RecyclerView.Adapter<LiveChatHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveChatHolder {
        return  LiveChatHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_live_chat, parent, false))
    }

    override fun onBindViewHolder(holder: LiveChatHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}