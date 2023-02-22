package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.ShareLirikHolder
import com.arira.ngidol48.ui.activity.shareLirik.ShareLirikCallback

class ShareLirikAdapter(var list: ArrayList<String>, var callback:ShareLirikCallback): RecyclerView.Adapter<ShareLirikHolder>() {
    var firstSelected = 0
    var selectedLyricsPosition:ArrayList<Int> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareLirikHolder {
        return  ShareLirikHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_lyrics_to_share, parent, false))
    }

    override fun onBindViewHolder(holder: ShareLirikHolder, position: Int) {
        holder.setData(list[position], callback,selectedLyricsPosition)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}