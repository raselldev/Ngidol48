package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.PhotoCardResponse
import io.reactivex.Observable

class PhotoCardRepo: BaseHelper() {

    fun getDetail(id:String): Observable<PhotoCardResponse> {
        return  ApiServiceServer.getDetailPhotoCard(id)
    }

}