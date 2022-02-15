package com.arira.ngidol48.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.databinding.ItemNotifBinding
import com.arira.ngidol48.model.Notifikasi
import com.arira.ngidol48.ui.event.EventActivity
import com.arira.ngidol48.ui.member.MemberActivity
import com.arira.ngidol48.ui.news.BeritaActivity
import com.arira.ngidol48.utilities.Go

class NotifikasiHolder(var item:ItemNotifBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: Notifikasi){
        item.tvJudul.text = data.title
        item.tvDeskripsi.text = data.message
        item.tvWaktu.text = helper.convert(data.created_at, "yyyy-MM-dd HH:mm:ss", "HH:mm dd MMM yyyy")

        when(data.type){
            "EVENT"-> item.ivThumb.setImageResource(R.drawable.ic_calendar)
            "NEWS"-> item.ivThumb.setImageResource(R.drawable.ic_news)
            "MEMBER"-> item.ivThumb.setImageResource(R.drawable.ic_member)
            "SHOWROOM"-> item.ivThumb.setImageResource(R.drawable.ic_member)
        }

        item.ivThumb.rootView.setOnClickListener {
            when(data.type){
                "EVENT"-> Go(itemView.context).move(EventActivity::class.java)
                "NEWS"-> Go(itemView.context).move(BeritaActivity::class.java)
                "MEMBER"-> Go(itemView.context).move(MemberActivity::class.java)
                "SHOWROOM"-> Go(itemView.context).move(MemberActivity::class.java)
            }
        }
    }
}