package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.EventResponse
import com.arira.ngidol48.network.response.HomeResponse
import io.reactivex.Observable

class EventRepo: BaseHelper() {
    fun allEvent(): Observable<EventResponse> {
        return  ApiServiceServer.allEvent()
    }

    fun detail(id:String): Observable<EventResponse> {
        return  ApiServiceServer.detailEvent(id)
    }

}