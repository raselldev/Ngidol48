package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.HandshakeResponse
import com.arira.ngidol48.network.response.MngResponse
import io.reactivex.Observable

class MngRepo: BaseHelper() {

    fun allMng(): Observable<MngResponse> {
        return  ApiServiceServer.allMng()
    }

    fun mngByMember(id:String): Observable<MngResponse> {
        return  ApiServiceServer.mngByMeber(id)
    }

    fun allHisotryMng(): Observable<MngResponse> {
        return  ApiServiceServer.allHisotryMng()
    }

}