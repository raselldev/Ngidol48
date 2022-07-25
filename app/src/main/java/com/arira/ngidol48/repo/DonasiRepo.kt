package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.DonasiResponse
import io.reactivex.Observable

class DonasiRepo: BaseHelper() {
    fun all(): Observable<DonasiResponse> {
            return  ApiServiceServer.donasi()
    }


}