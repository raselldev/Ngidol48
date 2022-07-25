package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.model.ViewLiveStream
import com.arira.ngidol48.network.response.StreamResponse
import com.arira.ngidol48.network.response.ViewStreamResponse
import io.reactivex.Observable

class StreamRepo: BaseHelper() {
    fun detail(id:String): Observable<StreamResponse> {
            return  ApiServiceServer.detailStream(id)
    }

    fun views(id:String):Observable<ViewStreamResponse>{
        return  ApiServiceServer.viewsStream(id)
    }

    fun watch(data:ViewLiveStream):Observable<ViewStreamResponse>{
        return  ApiServiceServer.watchStream(data)
    }

    fun leave(data:ViewLiveStream):Observable<ViewStreamResponse>{
        return  ApiServiceServer.leaveStream(data)
    }


}