package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.SignHolder
import com.arira.ngidol48.model.PhotoCardSign

class SignAdapter(var list: List<PhotoCardSign>, val callback: Callback): RecyclerView.Adapter<SignHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SignHolder {
        return  SignHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_sign, parent, false))
    }

    override fun onBindViewHolder(holder: SignHolder, position: Int) {
        holder.setData(list[position], callback)
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    interface Callback {
        fun onClick(sign: PhotoCardSign)
    }
}