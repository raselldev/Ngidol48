package com.arira.ngidol48.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.databinding.ItemDonasiBinding
import com.arira.ngidol48.model.Donasi

class DonasiHolder(var item:ItemDonasiBinding): RecyclerView.ViewHolder(item.root) {

    var detail = false

    fun setData(data: Donasi){
        item.tvDonasi.text = helper.convertRupiah(data.amount_raw)
        item.tvNama.text = data.donator_name
        item.tvPesan.text = data.message
    }
}