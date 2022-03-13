package com.arira.ngidol48.repo

import com.arira.ngidol48.app.App.Companion.token
import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.model.User
import com.arira.ngidol48.network.response.AuthResponse
import com.arira.ngidol48.network.response.EventResponse
import com.arira.ngidol48.network.response.HomeResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfilRepo: BaseHelper() {
    fun changeAvatar(avatar: MultipartBody.Part, id: RequestBody): Observable<AuthResponse>{
        return  ApiServiceServerFile.changeAvatar(token,id, avatar)
    }

    fun updateProfil(user:User): Observable<AuthResponse>{
        return  ApiServiceServerFile.updateProfil(token, user)
    }


}