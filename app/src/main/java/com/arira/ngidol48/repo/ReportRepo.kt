package com.arira.ngidol48.repo

import com.arira.ngidol48.app.App
import com.arira.ngidol48.app.App.Companion.token
import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.model.Komentar
import com.arira.ngidol48.model.Report
import com.arira.ngidol48.network.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ReportRepo: BaseHelper() {
    fun listReport(): Observable<ReportResponse> {
        return  ApiServiceServer.listReport(token)
    }

    fun report(report:Report): Observable<ReportResponse> {
        return  ApiServiceServer.reportBlog(token, report)
    }



}