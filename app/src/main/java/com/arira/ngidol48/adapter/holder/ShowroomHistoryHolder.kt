package com.arira.ngidol48.adapter.holder

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemHistoryShowroomBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.ShowroomHistory
import com.bumptech.glide.Glide

class ShowroomHistoryHolder(var item:ItemHistoryShowroomBinding): RecyclerView.ViewHolder(item.root) {

    val context = itemView.context
    fun setData(data: ShowroomHistory){
        item.tvJudul.text = data.title
        item.tvNamaMember.text = data.nama_lengkap
        Glide.with(itemView.context).load(BASE_STORAGE_JKT  + data.avatar).into(item.ivAvaMember)

        Glide.with(itemView.context).load(data.thumbnail_url).into(item.ivCover)

        item.tvJudul.rootView.setOnClickListener {
            val url = "https://www.youtube.com/watch?v=${data.videoId}"
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(url)
            context!!.startActivity(openURL)
        }
    }
}