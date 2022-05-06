package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ItemBeritaBinding
import com.arira.ngidol48.databinding.ItemKategoriBlogBinding
import com.arira.ngidol48.helper.Config.BASE_STORAGE_JKT
import com.arira.ngidol48.model.Berita
import com.arira.ngidol48.model.BlogKategori
import com.arira.ngidol48.ui.addBlog.BlogKategoriCallback
import com.arira.ngidol48.ui.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide

class BlogKategoriHolder(var item:ItemKategoriBlogBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: BlogKategori, callback: BlogKategoriCallback, selected: BlogKategori){
        item.tvJudul.text = data.nama

        item.tvJudul.rootView.setOnClickListener {
            callback.onKategoriSelected(data)
        }

        if (data.id == selected.id){
            item.ivChecklist.visibility = View.VISIBLE
        }else{
            item.ivChecklist.visibility = View.GONE
        }

    }
}