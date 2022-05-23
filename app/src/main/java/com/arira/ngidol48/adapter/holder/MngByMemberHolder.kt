package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.MemberMngByMemberAdapter
import com.arira.ngidol48.databinding.ItemMngByMemberBinding
import com.arira.ngidol48.model.DetailMng
import com.arira.ngidol48.ui.activity.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go

class MngByMemberHolder(var item: ItemMngByMemberBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: DetailMng){
        item.tvJudul.text = "${data.tanggal}"
        item.tvDeskripsi.text = data.event_name
        item.rvMember.apply {
            layoutManager = LinearLayoutManager(itemView.context)
            adapter = MemberMngByMemberAdapter(data.mng)
        }

        item.tvTiketDll.setOnClickListener {
            val buy_url = "https://jkt48.com/twoshot?id=${data.id_mng}"
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

    private fun updateView(data:DetailMng){
        if (data.is_expand){
            item.rvMember.visibility = View.VISIBLE
            item.ivExpand.setImageResource(R.drawable.ic_baseline_expand_less_24)
        }else{
            item.rvMember.visibility = View.GONE
            item.ivExpand.setImageResource(R.drawable.ic_baseline_expand_more_24)
        }
    }
}