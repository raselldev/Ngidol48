package com.arira.ngidol48.repo

import com.arira.ngidol48.app.App
import com.arira.ngidol48.app.App.Companion.token
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.network.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FavoritRepo: BaseHelper() {
    fun listFavorit(): Observable<SongResponse> {
        return  ApiServiceServer.listFav(token, user.id)
    }

    fun add(songId:String): Observable<FavoritResponse> {
        val song = Song()
        song.song_id = songId
        song.user_id = user.id
        return  ApiServiceServer.addFavorit(token, song)
    }

    fun remove(songId:String): Observable<FavoritResponse> {
        val song = Song()
        song.song_id = songId
        song.user_id = user.id
        return  ApiServiceServer.removeFavorit(token, song)
    }
}