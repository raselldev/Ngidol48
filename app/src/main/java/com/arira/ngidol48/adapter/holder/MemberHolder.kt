package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ItemMemberBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.activity.member.MemberCallback
import com.bumptech.glide.Glide

class MemberHolder(var item:ItemMemberBinding): RecyclerView.ViewHolder(item.root) {

    var detail = false

    fun setData(data: Member, callback: MemberCallback, show:Boolean = false, titleWhite:Boolean = false){
        if (titleWhite){
            item.tvNama.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
        }

        item.tvNama.text = data.nama_panggilan
        item.tvTeam.text = data.kategori
        item.tvShow.text = itemView.context.getString(R.string.teks_s_show, data.showtime)

        if (show){
            item.tvShow.visibility = View.VISIBLE
        }else{
            item.tvShow.visibility = View.GONE
        }

        Glide.with(itemView.context).load(BASE_STORAGE_JKT  + data.avatar).into(item.ivAva)

        item.tvTeam.rootView.setOnClickListener {
            callback.onSelect(data)
        }
    }
}