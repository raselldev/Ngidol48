package com.arira.ngidol48.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.BlogKategoriHolder
import com.arira.ngidol48.model.BlogKategori
import com.arira.ngidol48.ui.activity.addBlog.BlogKategoriCallback

class BlogKategoriAdapter(var list:ArrayList<BlogKategori>, val callback: BlogKategoriCallback): RecyclerView.Adapter<BlogKategoriHolder>(), Filterable {
    var selected:BlogKategori = BlogKategori()
    var data: ArrayList<BlogKategori> =  list
    var listBlogKategoriFiltered: ArrayList<BlogKategori> = list
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogKategoriHolder {
        return  BlogKategoriHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_kategori_blog, parent, false))
    }

    override fun onBindViewHolder(holder: BlogKategoriHolder, position: Int) {

        holder.setData(listBlogKategoriFiltered[position], callback, selected)
    }

    override fun getItemCount(): Int {
        return  listBlogKategoriFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            @SuppressLint("DefaultLocale")
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val result2 = java.util.ArrayList<BlogKategori>()
                listBlogKategoriFiltered = data
                if (constraint != null) {
                    if ((listBlogKategoriFiltered.size > 0)) {
                        for (map in listBlogKategoriFiltered) {
                            if (map.nama.toLowerCase().contains(constraint.toString().toLowerCase())) {
                                result2.add(map)
                            }
                        }

                    }
                    results.values = result2
                }

                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                listBlogKategoriFiltered = results.values as java.util.ArrayList<BlogKategori>
                notifyDataSetChanged()
            }
        }
    }
}