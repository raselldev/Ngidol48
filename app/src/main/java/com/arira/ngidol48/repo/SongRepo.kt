package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.HomeResponse
import com.arira.ngidol48.network.response.MemberResponse
import com.arira.ngidol48.network.response.SetlistResponse
import com.arira.ngidol48.network.response.SongResponse
import io.reactivex.Observable
import java.util.*

class SongRepo: BaseHelper() {
    fun allSetlist(): Observable<SetlistResponse> {
        return  ApiServiceServer.setlist()
    }

    fun songList(id:String): Observable<SongResponse> {
        return  ApiServiceServer.songlist(id)
    }

    fun cari(q:String): Observable<SongResponse> {
        return  ApiServiceServer.cariLagu(q)
    }

}