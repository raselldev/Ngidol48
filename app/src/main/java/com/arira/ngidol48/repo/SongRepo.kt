package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.network.response.*
import io.reactivex.Observable
import java.util.*

class SongRepo: BaseHelper() {
    fun allSetlist(): Observable<SetlistResponse> {
        return  ApiServiceServer.setlist()
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