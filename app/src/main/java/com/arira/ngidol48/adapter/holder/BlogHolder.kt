package com.arira.ngidol48.adapter.holder

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.databinding.ItemBlogBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_IMAGE
import com.arira.ngidol48.helper.Helper
import com.arira.ngidol48.model.Blog
import com.arira.ngidol48.ui.activity.detailBlog.DetailBlogActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

class BlogHolder(var item:ItemBlogBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: Blog){
        if (!data.cover.contains("default")){
            item.ivThumb.visibility = View.VISIBLE
            Glide.with(itemView.context)
                .asBitmap()
                .load(BASE_STORAGE_IMAGE + data.cover)
                .placeholder(R.drawable.img_logo)
                .error(R.drawable.img_logo)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        item.ivThumb.setImageBitmap(resource)
                        item.ivThumb.setBackgroundColor(Helper.getDominantColor(resource))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }else{
            item.ivThumb.visibility = View.GONE
        }

        item.ivPengguna.visibility = View.VISIBLE
        Glide.with(itemView.context)
            .asBitmap()
            .load(BASE_STORAGE_IMAGE + data.avatar)
            .placeholder(R.drawable.ic_baseline_person_24)
            .error(R.drawable.ic_baseline_person_24)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    item.ivPengguna.setImageBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })

        item.tvKategori.text = data.nama_kategori
        item.tvJudul.text = data.judul
        item.tvNamaPengguna.text = data.fullname
        item.tvTanggal.text = helper.waktulalu(data.created_at)
        item.tvDeskripsi.text = data.blog

        item.tvTotalKomentar.text = "${data.total_command} Komentar"

        item.tvTanggal.rootView.setOnClickListener {
            Go(itemView.context).move(DetailBlogActivity::class.java, data = data)
        }

    }
}