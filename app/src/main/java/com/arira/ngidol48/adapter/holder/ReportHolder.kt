package com.arira.ngidol48.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ItemReportBinding
import com.arira.ngidol48.model.Report
import com.arira.ngidol48.ui.activity.reportBlog.ReportCallback

class ReportHolder(var item:ItemReportBinding): RecyclerView.ViewHolder(item.root) {

    fun setData(data: Report, reportSelected: Report, callback: ReportCallback){
        item.tvJudul.text = data.title
        item.tvSubjudul.text = data.subtitle

        if (data.id == reportSelected.id){
            item.ivChecklist.visibility = View.VISIBLE
            item.linData.setBackgroundResource(R.drawable.background_shape_pink)
        }else{
            item.ivChecklist.visibility = View.GONE
            item.linData.setBackgroundResource(0)
        }

        item.tvJudul.rootView.setOnClickListener {
            callback.selected(data)
        }
    }
}