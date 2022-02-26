package com.arira.ngidol48.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ItemHandshakeBinding
import com.arira.ngidol48.model.ParentHandshake
import com.arira.ngidol48.ui.handshake.DetailHandshakeActivity
import com.arira.ngidol48.utilities.Go

class HandshakeHolder(var item:ItemHandshakeBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: ParentHandshake){
        item.tvNama.text = data.nama_event_handshake
        item.tvTanggal.text = data.tanggal_envent_handshake
        item.tvMember.text = "${data.nama_member.size} Member"
        item.tvSesi.text = "${data.sesis.size} Sesi"
        item.tvTiket.text = "${data.total_sold}/${data.total_jadwal} Sold"

        item.tvNama.rootView.setOnClickListener {
            Go(itemView.context).move(DetailHandshakeActivity::class.java, data = data)
        }
    }
}