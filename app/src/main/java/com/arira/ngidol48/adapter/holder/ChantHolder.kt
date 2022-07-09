package com.arira.ngidol48.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemChantBinding
import com.arira.ngidol48.model.Chant
import com.arira.ngidol48.ui.activity.chants.DetailChantActivity
import com.arira.ngidol48.utilities.Go

class ChantHolder(var item:ItemChantBinding): RecyclerView.ViewHolder(item.root) {

    var detail = false

    fun setData(data: Chant){
        item.tvJudul.text = data.title

        item.tvJudul.rootView.setOnClickListener {
            Go(itemView.context).move(DetailChantActivity::class.java, data = data)
        }
    }
}