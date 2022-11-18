package com.arira.ngidol48.adapter.holder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ItemTanggalHandshakeBinding
import com.arira.ngidol48.model.ParentHandshake
import com.arira.ngidol48.ui.activity.handshake.HandshakeCallback

class TanggalHandshakeHolder(var item:ItemTanggalHandshakeBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: ParentHandshake,callback:HandshakeCallback, active_id:String){
        if (active_id != data.id_handshake){
            item.tvTanggal.setBackgroundResource(R.drawable.background_border_abu)
            item.tvTanggal.setTextColor(ContextCompat.getColor(itemView.context, R.color.abu_pekat))
        }else{
            item.tvTanggal.setBackgroundResource(R.drawable.background_border_primary)
            item.tvTanggal.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimaryTeks))
        }

        item.tvTanggal.text = data.tanggal_envent_handshake

        item.tvTanggal.rootView.setOnClickListener {
            callback.handshakeShow(data)
//            Go(itemView.context).move(DetailHandshakeActivity::class.java, data = data)
        }
    }
}