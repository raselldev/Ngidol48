package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.MemberHandshakeByMemberAdapter
import com.arira.ngidol48.databinding.ItemHandshakeByMemberBinding
import com.arira.ngidol48.model.DetailHandshake
import com.arira.ngidol48.ui.activity.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go

class HandshakeByMemberHolder(var item:ItemHandshakeByMemberBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: DetailHandshake){
        item.tvJudul.text = "${data.tanggal}"
        item.tvDeskripsi.text = data.event_name
        item.rvMember.apply {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = MemberHandshakeByMemberAdapter(data.handshake)
        }

        item.tvTiketDll.setOnClickListener {
            val buy_url = "https://jkt48.com/handshake?id=${data.id_handshake}"
            Go(itemView.context).move(MyWebActivity::class.java, url = buy_url)
        }

        updateView(data)
        item.ivExpand.setOnClickListener {
            data.is_expand = !data.is_expand
            updateView(data)
        }
        item.tvJudul.setOnClickListener {
            data.is_expand = !data.is_expand
            updateView(data)
        }
    }

    private fun updateView(data:DetailHandshake){
        if (data.is_expand){
            item.rvMember.visibility = View.VISIBLE
            item.ivExpand.setImageResource(R.drawable.ic_baseline_expand_less_24)
        }else{
            item.rvMember.visibility = View.GONE
            item.ivExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
        }
    }
}