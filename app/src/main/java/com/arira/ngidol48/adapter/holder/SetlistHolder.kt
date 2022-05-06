package com.arira.ngidol48.adapter.holder

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ItemSetlistBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE
import com.arira.ngidol48.helper.Helper
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.ui.lagu.LaguActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

class SetlistHolder(var item:ItemSetlistBinding): RecyclerView.ViewHolder(item.root) {


    fun setData(data: Setlist){
        item.tvTotalLagu.text = "${data.kategori} . ${itemView.context.getString(R.string.teks_total_lagu, data.total_lagu)}"
        item.tvNama.text = data.nama

        if (data.cover.isEmpty()){
            item.ivCover.visibility = View.GONE
        }else{
            item.ivCover.visibility = View.VISIBLE
//            Glide.with(itemView.context).load(BASE_STORAGE  + data.cover).into(item.ivCover)

            Glide.with(itemView.context)
                .asBitmap()
                .load(BASE_STORAGE  + data.cover)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        item.ivCover.setImageBitmap(resource)
                        item.ivCover.setBackgroundColor(Helper.getDominantColor(resource))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })

//            item.ivCover.drawable?.let {
//                val mBitmap = (it as BitmapDrawable).bitmap
//                Palette.from(mBitmap).generate { palette ->
//                    // Use the generated instance
//                    item.ivCover.setBackgroundColor(palette.darkMutedSwatch)
//
//                }
//            }
        }


        item.tvNama.rootView.setOnClickListener {
            Go(itemView.context).move(LaguActivity::class.java, data = data, choose = false)
        }
    }
}