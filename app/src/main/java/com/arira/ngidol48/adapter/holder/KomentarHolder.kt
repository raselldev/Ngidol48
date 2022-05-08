package com.arira.ngidol48.adapter.holder

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.databinding.ItemKomentarBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_IMAGE
import com.arira.ngidol48.model.Komentar
import com.arira.ngidol48.ui.activity.detailBlog.KomentarCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

class KomentarHolder(var item:ItemKomentarBinding): RecyclerView.ViewHolder(item.root) {

    val height = item.divDelete.height

    fun setData(data: Komentar, callback: KomentarCallback){
        item.divDelete.visibility = View.GONE


        if(data.id_user == user.id && data.id.isNotEmpty()){
            item.ivMenu.visibility = View.VISIBLE
        }else{
            item.ivMenu.visibility = View.GONE
        }

        if (!data.avatar.contains("default")){
            Glide.with(itemView.context)
                .asBitmap()
                .load(BASE_STORAGE_IMAGE + data.avatar)
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

        item.tvKomentar.text = data.komentar
        item.tvNama.text = data.fullname
        item.tvTanggal.text = helper.waktulalu(data.created_at)


        item.ivMenu.setOnClickListener {

            ObjectAnimator.ofFloat(item.divDelete, View.TRANSLATION_Y, 0f, height.toFloat()).apply {
                duration = 1000
                start()

                item.divDelete.visibility = View.VISIBLE
                item.ivMenu.visibility = View.GONE
            }

        }

        item.ivDeleteCancel.setOnClickListener {
            ObjectAnimator.ofFloat(item.divDelete, View.TRANSLATION_Y, height.toFloat(), 0f).apply {
                duration = 1000
                start()

                item.divDelete.visibility = View.GONE
                item.ivMenu.visibility = View.VISIBLE
            }

        }

        item.ivDeleteYes.setOnClickListener {
            //delete
            ObjectAnimator.ofFloat(item.divDelete, View.TRANSLATION_Y, height.toFloat(), 0f).apply {
                duration = 1000
                start()

                item.divDelete.visibility = View.GONE
                item.ivMenu.visibility = View.VISIBLE
            }

            callback.delete(data, absoluteAdapterPosition)
        }
    }
}