package com.arira.ngidol48.adapter.holder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.databinding.ItemKategoriNotifikasiBinding
import com.arira.ngidol48.ui.activity.notifikasi.NotificationCallback

class KategoriNotifikasiHolder(var item:ItemKategoriNotifikasiBinding): RecyclerView.ViewHolder(item.root) {


    fun setData(data: String, callback: NotificationCallback, selected:String){
        if (data == selected){
            item.tvNama.setBackgroundResource(R.drawable.background_border_primary)
            item.tvNama.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
        }else{
            item.tvNama.setBackgroundResource(R.drawable.background_border_abu)
            item.tvNama.setTextColor(ContextCompat.getColor(itemView.context, R.color.abu_pekat))
        }
       item.tvNama.text = helper.capitalizeString(data)

        item.tvNama.rootView.setOnClickListener {
            callback.onCategorySelected(data)
        }
    }
}