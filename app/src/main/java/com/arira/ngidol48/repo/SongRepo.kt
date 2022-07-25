package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.network.response.DefaultResponse
import com.arira.ngidol48.network.response.SetlistResponse
import com.arira.ngidol48.network.response.SongResponse
import io.reactivex.Observable

class SongRepo: BaseHelper() {
    fun allSetlist(): Observable<SetlistResponse> {
        return  ApiServiceServer.setlist()
    }

    fun home(): Observable<SetlistResponse> {
        return  ApiServiceServer.setlistHome()
    }

    fun songList(id:String): Observable<SongResponse> {
        return  ApiServiceServer.songlist(id)
    }

    fun detail(idUser:String, idSong:String): Observable<SongResponse> {
        return  ApiServiceServer.detailSong(idUser, idSong)
    }

    fun cari(q:String): Observable<SongResponse> {
        return  ApiServiceServer.cariLagu(q)
    }


    fun listening(song: Song): Observable<DefaultResponse> {
        return  ApiServiceServer.listeningSong(song)
    }

}