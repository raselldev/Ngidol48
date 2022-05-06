package com.arira.ngidol48.repo

import com.arira.ngidol48.app.App
import com.arira.ngidol48.app.App.Companion.token
import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.model.Komentar
import com.arira.ngidol48.network.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class KomentarRepo: BaseHelper() {
    fun listKomentar(id:String): Observable<KomentarResponse> {
        return  ApiServiceServer.listKomentar(token, id)
    }

    fun komentar(data: Komentar): Observable<KomentarResponse> {
        return  ApiServiceServer.komentar(token, data)
    }

    fun delete(data: Komentar): Observable<KomentarResponse> {
        return  ApiServiceServer.deleteKomentar(token, data)
    }


}