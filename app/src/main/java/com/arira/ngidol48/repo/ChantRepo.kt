package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.ChantResponse
import io.reactivex.Observable

class ChantRepo: BaseHelper() {
    fun allChant(): Observable<ChantResponse> {
        return  ApiServiceServer.chants()
    }

}