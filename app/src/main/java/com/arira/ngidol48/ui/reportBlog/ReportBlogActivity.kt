package com.arira.ngidol48.ui.reportBlog

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.ReportAdapter
import com.arira.ngidol48.databinding.ActivityReportBlogBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.Blog
import com.arira.ngidol48.model.Report

class ReportBlogActivity : BaseActivity(), ReportCallback{
    private lateinit var binding:ActivityReportBlogBinding
    private lateinit var viewModel: ReportViewModel
    private var blog:Blog = Blog()
    private var reportSelected = Report()
    private lateinit var reportAdapter:ReportAdapter
    private var listReportUsed:ArrayList<Report> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_blog)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_report_blog)
        setToolbar(getString(R.string.teks_laporkan), binding.toolbar)

        blog = intent.getParcelableExtra(extra_model) ?: Blog()

        binding.tvJudul.text = getString(R.string.teks_judul_report_view, blog.fullname)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[ReportViewModel::class.java]
        viewModel.context = this

        reportAdapter = ReportAdapter(listReportUsed, this)
        binding.rvData.apply {
            adapter = reportAdapter
            layoutManager = LinearLayoutManager(context)
        }

        observerData()

        viewModel.getList()

        action()
    }

    private fun action(){
        binding.btnLapor.setOnClickListener {
            if (!reportSelected.id.isEmpty()){
                viewModel.report(reportSelected)
            }else{
                toast.show(getString(R.string.teks_pilih_salah_satu_kategori), this)
            }
        }
    }

    override fun selected(report: Report) {
        reportAdapter.reportSelected = report
        reportSelected = report
        reportAdapter.notifyDataSetChanged()
    }

    private fun observerData(){
        viewModel.getLoading().observe(this){
            if (it != null){
                if (it){
                    SweetAlert.onLoading(this)
                }else{
                    SweetAlert.dismis()
                }
            }
        }

        viewModel.getResponse().observe(this){
            if (it != null){
                listReportUsed.clear()
                listReportUsed.addAll(it.reports)
                reportAdapter.notifyDataSetChanged()
            }
        }

        viewModel.getResponseReport().observe(this){
            if (it != null){
                SweetAlert.success(context = this, getString(R.string.teks_laporkan), it.message, finish = true)
            }
        }

        viewModel.getError().observe(this){
            if (it != null){
                SweetAlert.onFailure(this, it)
            }
        }
    }
}