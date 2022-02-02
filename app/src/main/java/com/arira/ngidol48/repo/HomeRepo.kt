package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.HomeResponse
import io.reactivex.Observable

class HomeRepo: BaseHelper() {
    fun home(): Observable<HomeResponse> {
        return  ApiServiceServer.home()
    }

}