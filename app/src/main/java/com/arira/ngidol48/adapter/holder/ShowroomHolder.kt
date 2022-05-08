package com.arira.ngidol48.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.databinding.ViewShowroomBinding
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.activity.viewShowroom.ViewLiveActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class ShowroomHolder(var item:ViewShowroomBinding): RecyclerView.ViewHolder(item.root) {
    var context = itemView.context
    fun setData(data: Member){

        val requestOptions = RequestOptions()
        Glide.with(context)
            .load(Config.BASE_STORAGE_JKT + data.avatar)
            .apply(requestOptions)
            .thumbnail(Glide.with(context).load(Config.BASE_STORAGE_JKT + data.avatar))
            .into(item.ivThumb)

        item.tvNamaMember.text = data.nama_panggilan

        item.divPlay.setOnClickListener {
            Go(context).move(ViewLiveActivity::class.java, url = data.stream_url, data = data, choose = true)
        }
    }
}