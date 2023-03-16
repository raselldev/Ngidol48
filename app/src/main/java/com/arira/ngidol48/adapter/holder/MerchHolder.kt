package com.arira.ngidol48.adapter.holder

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ItemMerchBinding
import com.arira.ngidol48.model.Merch
import com.bumptech.glide.Glide

class MerchHolder(var item:ItemMerchBinding): RecyclerView.ViewHolder(item.root) {

    var detail = false
    val context = itemView.context

    fun setData(data: Merch){
        item.tvNama.text = data.name
        item.tvPrice.text = data.price

        Glide.with(itemView.context).load(data.imageUrl).into(item.ivCover)

        item.tvNama.rootView.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(data.productUrl)
            context.startActivity(Intent.createChooser(openURL, context.getString(R.string.teks_pilih_aplikasi_untuk_membuka)))

//            Go(itemView.context).move(MyWebActivity::class.java, url = data.productUrl)
        }
    }
}