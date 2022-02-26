package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.MemberDetailHandshakeAdapter
import com.arira.ngidol48.databinding.ItemDetailHandshakeBinding
import com.arira.ngidol48.databinding.ItemHandshakeBinding
import com.arira.ngidol48.model.DetailHandshake
import com.arira.ngidol48.model.ParentHandshake

class DetailHandshakeHolder(var item:ItemDetailHandshakeBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: DetailHandshake){
        item.tvSesi.text = data.sesi
        item.tvWaktu.text = data.waktu
        item.tvStandby.text = data.standby
        item.tvMemberList.text = data.member_list_name
        item.rvMember.apply {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = MemberDetailHandshakeAdapter(data.handshake)
        }

        updateView(data)
        item.ivExpand.setOnClickListener {
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