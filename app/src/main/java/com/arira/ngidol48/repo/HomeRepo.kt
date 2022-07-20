package com.arira.ngidol48.repo

import com.arira.ngidol48.app.App
import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.HomeResponse
import io.reactivex.Observable

class HomeRepo: BaseHelper() {
    fun home(): Observable<HomeResponse> {
        return  ApiServiceServer.home()
    }

    fun home(fcmtoken:String): Observable<HomeResponse> {
        return if (fcmtoken.isEmpty() && App.token.isEmpty()){
            ApiServiceServer.home()
        }else if (fcmtoken.isEmpty()){
            ApiServiceServer.home()
        }else if (App.token.isEmpty()){
            ApiServiceServer.home()
        }else{
            ApiServiceServer.home(App.token, fcmtoken)
        }

    }

}