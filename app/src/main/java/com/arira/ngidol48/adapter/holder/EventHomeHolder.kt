package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ItemEventBinding
import com.arira.ngidol48.databinding.ItemEventHomeBinding
import com.arira.ngidol48.databinding.ItemLaguBinding
import com.arira.ngidol48.databinding.ItemSetlistBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.Event
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.detailEvent.DetailEventActivity
import com.arira.ngidol48.ui.lagu.DetailLaguActivity
import com.arira.ngidol48.ui.lagu.LaguCallback
import com.arira.ngidol48.ui.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import java.util.zip.GZIPOutputStream

class EventHomeHolder(var item:ItemEventHomeBinding): RecyclerView.ViewHolder(item.root) {


    fun setData(data: Event){
        item.tvNama.text = data.event_name
        Glide.with(itemView.context).load(BASE_STORAGE_JKT + data.badge_url).into(item.ivCover)
        item.tvMemberPerform.text = itemView.context.getString(R.string.teks_d_member, data.member_perform)

        if(data.event_time.isEmpty()){
            item.tvTanggal.text = "${data.tanggal} ${data.bulan_tahun}"
        }else{
            item.tvTanggal.text = "${data.event_time}, ${data.tanggal} ${data.bulan_tahun}"
        }

        if (data.member_perform != "0"){
            item.linMember.visibility = View.VISIBLE
        }else{
            item.linMember.visibility = View.GONE
        }


        item.tvNama.rootView.setOnClickListener {
            if (!data.event_id.contains("/")){
                Go(itemView.context).move(DetailEventActivity::class.java, data = data)
            }else{
                val url = "https://jkt48.com/${data.event_id}"
                Go(itemView.context).move(MyWebActivity::class.java, url = url)
            }
//            if (callback != null){
//                callback.onSelectSong(data)
//            }else{
//                Go(itemView.context).move(DetailLaguActivity::class.java, data = data)
//            }
        }
    }
}