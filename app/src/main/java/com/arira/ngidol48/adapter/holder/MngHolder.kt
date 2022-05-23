package com.arira.ngidol48.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemMngBinding
import com.arira.ngidol48.model.ParentMng
import com.arira.ngidol48.ui.activity.mng.DetailMngActivity
import com.arira.ngidol48.utilities.Go

class MngHolder(var item:ItemMngBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: ParentMng){
        item.tvNama.text = data.nama_event_mng
        item.tvTanggal.text = data.tanggal_envent_mng
        item.tvMember.text = "${data.nama_member.size} Member"
        item.tvSesi.text = "${data.sesis.size} Sesi"
        item.tvTiket.text = "${data.total_sold}/${data.total_jadwal} Sold"

        item.tvNama.rootView.setOnClickListener {
            Go(itemView.context).move(DetailMngActivity::class.java, data = data)
        }
    }
}