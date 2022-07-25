package com.arira.ngidol48.adapter.holder

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.databinding.ItemLiveChatBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_IMAGE
import com.arira.ngidol48.model.LiveChat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

class LiveChatHolder(var item:ItemLiveChatBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: LiveChat){


        if (!data.userAva.contains("default")){
            Glide.with(itemView.context)
                .asBitmap()
                .load(BASE_STORAGE_IMAGE + data.userAva)
                .placeholder(R.drawable.ic_member)
                .error(R.drawable.ic_member)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        item.ivAvatar.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }

        item.tvKomentar.text = data.message
        item.tvNama.text = data.userName
        item.tvTanggal.text = helper.waktulalu(data.created_at)

    }
}