package com.arira.ngidol48.network

import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.network.response.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiServiceServer {

    @Headers(Config.API)
    @GET("home")
    fun home(): Observable<HomeResponse>

    @Headers(Config.API)
    @GET("member")
    fun member(): Observable<MemberResponse>

    @Headers(Config.API)
    @GET("setlist")
    fun setlist(): Observable<SetlistResponse>

    @Headers(Config.API)
    @GET("notifikasi")
    fun listnotifikasi(): Observable<NotifikasiResponse>

    @Headers(Config.API)
    @GET("songlist/{id}")
    fun songlist(
        @Path("id") id:String
    ): Observable<SongResponse>

    @Headers(Config.API)
    @GET("event")
    fun allEvent(
    ): Observable<EventResponse>

    @Headers(Config.API)
    @GET("news")
    fun allNews(
    ): Observable<NewsResponse>

    @Headers(Config.API)
    @GET("event/detail/{id}")
    fun detailEvent(
        @Path("id") id:String
    ): Observable<EventResponse>



}