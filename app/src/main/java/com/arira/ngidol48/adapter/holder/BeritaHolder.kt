package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemBeritaBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.Berita
import com.arira.ngidol48.ui.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide

class BeritaHolder(var item:ItemBeritaBinding): RecyclerView.ViewHolder(item.root) {

    var detail = false

    fun setData(data: Berita){
        item.tvJudul.text = data.judul
        item.tvTanggal.text = data.waktu

        Glide.with(itemView.context).load(BASE_STORAGE_JKT  + data.badge).into(item.ivBadge)

        item.tvTanggal.rootView.setOnClickListener {
            val url = "https://jkt48.com/news/detail/id/${data.berita_id}?lang=id"
            Go(itemView.context).move(MyWebActivity::class.java, url = url)
        }
    }
}