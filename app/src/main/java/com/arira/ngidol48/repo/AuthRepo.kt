package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.model.User
import com.arira.ngidol48.network.response.AuthResponse
import com.arira.ngidol48.network.response.EventResponse
import com.arira.ngidol48.network.response.HomeResponse
import io.reactivex.Observable

class AuthRepo: BaseHelper() {
    fun login(user:User): Observable<AuthResponse> {
        return  ApiServiceServer.login(user)
    }

    fun register(user:User): Observable<AuthResponse> {
        return  ApiServiceServer.register(user)
    }
}