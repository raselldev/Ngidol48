package com.arira.ngidol48.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.holder.ReportHolder
import com.arira.ngidol48.model.Report
import com.arira.ngidol48.ui.reportBlog.ReportCallback

class ReportAdapter(var list:List<Report>, var callback:ReportCallback): RecyclerView.Adapter<ReportHolder>() {
    var reportSelected:Report = Report()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportHolder {
        return  ReportHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_report, parent, false))
    }

    override fun onBindViewHolder(holder: ReportHolder, position: Int) {
        holder.setData(list[position],reportSelected, callback)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}