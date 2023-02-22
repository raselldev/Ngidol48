package com.arira.ngidol48.adapter.holder

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.isDark
import com.arira.ngidol48.databinding.ItemLyricsToShareBinding
import com.arira.ngidol48.ui.activity.shareLirik.ShareLirikCallback

class ShareLirikHolder(var item:ItemLyricsToShareBinding): RecyclerView.ViewHolder(item.root) {
    val context = itemView.context
    fun setData(lirik:String, callback:ShareLirikCallback, selectedList: ArrayList<Int>){
        item.tvLirik.text = lirik
        val active = isActive(absoluteAdapterPosition, selectedList)

        if (active){
            item.tvLirik.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryTeks))
        }else{
            if (isDark){
                item.tvLirik.setTextColor(ContextCompat.getColor(context, R.color.white))
            }else{
                item.tvLirik.setTextColor(ContextCompat.getColor(context, R.color.dark))
            }
        }


        item.tvLirik.rootView.setOnClickListener {
            if (active) {
                callback.onLirikRemoved(absoluteAdapterPosition, lirik)
            }else{
                callback.onLirikSelected(absoluteAdapterPosition, lirik)
            }
        }
    }

    private fun isActive(position:Int, selectedList:List<Int>):Boolean{
        selectedList.forEach {
            if (position == it){
                return true
            }
        }

        return false
    }
}