package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.HandshakeResponse
import io.reactivex.Observable

class HandshakeRepo: BaseHelper() {

    fun allHandshake(): Observable<HandshakeResponse> {
        return  ApiServiceServer.allHandshake()
    }

    fun handshakeByMember(id:String): Observable<HandshakeResponse> {
        return  ApiServiceServer.handshakeByMeber(id)
    }

    fun allHisotryHandshake(): Observable<HandshakeResponse> {
        return  ApiServiceServer.allHisotryHandshake()
    }

}