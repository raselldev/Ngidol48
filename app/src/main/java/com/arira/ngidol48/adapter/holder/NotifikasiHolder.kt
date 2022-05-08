package com.arira.ngidol48.adapter.holder

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.databinding.ItemNotifBinding
import com.arira.ngidol48.model.Notifikasi
import com.arira.ngidol48.ui.activity.event.EventActivity
import com.arira.ngidol48.ui.activity.handshake.HandshakeActivity
import com.arira.ngidol48.ui.activity.member.MemberActivity
import com.arira.ngidol48.ui.activity.news.BeritaActivity
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
            "TIKETCOM"-> item.ivThumb.setImageResource(R.drawable.ic_tiket)
            "HANDSHAKE"-> item.ivThumb.setImageResource(R.drawable.ic_videocall)
        }

        item.ivThumb.rootView.setOnClickListener {
            if (data.url.isEmpty()){
                when(data.type){
                    "EVENT"-> Go(itemView.context).move(EventActivity::class.java)
                    "NEWS"-> Go(itemView.context).move(BeritaActivity::class.java)
                    "MEMBER"-> Go(itemView.context).move(MemberActivity::class.java)
                    "SHOWROOM"-> Go(itemView.context).move(MemberActivity::class.java)
                    "HANDSHAKE"-> Go(itemView.context).move(HandshakeActivity::class.java)
                    "TIKETCOM"-> {
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse("https://www.tiket.com${data.url}")
                        itemView.context!!.startActivity(Intent.createChooser(openURL, itemView.context.getString(R.string.teks_pilih_aplikasi_untuk_membuka)))
                    }
                }
            }else{
                if (data.type == "TIKETCOM"){
                    val openURL = Intent(Intent.ACTION_VIEW)
                    openURL.data = Uri.parse("https://www.tiket.com${data.url}")
                    itemView.context!!.startActivity(Intent.createChooser(openURL, itemView.context.getString(R.string.teks_pilih_aplikasi_untuk_membuka)))
                }else{
                    if (data.url.contains("http")){
                        val openURL = Intent(Intent.ACTION_VIEW)
                        openURL.data = Uri.parse("${data.url}")
                        itemView.context!!.startActivity(Intent.createChooser(openURL, itemView.context.getString(R.string.teks_pilih_aplikasi_untuk_membuka)))
                    }

                }

            }
        }
    }
}