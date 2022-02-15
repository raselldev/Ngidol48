package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.NotifikasiResponse
import com.arira.ngidol48.network.response.HomeResponse
import io.reactivex.Observable

class NotifikasiRepo: BaseHelper() {
    fun all(): Observable<NotifikasiResponse> {
        return  ApiServiceServer.listnotifikasi()
    }

}