package com.arira.ngidol48.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.BlogHolder
import com.arira.ngidol48.model.Blog

class BlogAdapter(var list:ArrayList<Blog>): RecyclerView.Adapter<BlogHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogHolder {
        return  BlogHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_blog, parent, false))
    }

    override fun onBindViewHolder(holder: BlogHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        Log.e("SIZE", "sie : ${list.size}")
        return  list.size
    }
}