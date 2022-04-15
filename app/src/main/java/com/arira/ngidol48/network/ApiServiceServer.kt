package com.arira.ngidol48.network

import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.model.User
import com.arira.ngidol48.network.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

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
    @GET("handshake")
    fun allHandshake(): Observable<HandshakeResponse>

    @Headers(Config.API)
    @GET("handshake/member/{id}")
    fun handshakeByMeber(
        @Path("id") id:String,
    ): Observable<HandshakeResponse>

    @Headers(Config.API)
    @GET("handshake/history")
    fun allHisotryHandshake(): Observable<HandshakeResponse>

    @Headers(Config.API)
    @GET("songlist/{id}")
    fun songlist(
        @Path("id") id:String
    ): Observable<SongResponse>

    @Headers(Config.API)
    @GET("songlist/search")
    fun cariLagu(
        @Query("q") query:String
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

    @Headers(Config.API)
    @POST("fans/login")
    fun login(
        @Body user:User
    ): Observable<AuthResponse>

    @Headers(Config.API)
    @POST("fans/register")
    fun register(
        @Body user:User
    ): Observable<AuthResponse>


    /**profil */
    @Headers(Config.API)
    @Multipart
    @POST("fans/avatar")
    fun changeAvatar(
        @Header("token") token:String,
        @Part("id") id: RequestBody,
        @Part image: MultipartBody.Part,
    ): Observable<AuthResponse>

    @Headers(Config.API)
    @POST("fans/update")
    fun updateProfil(
        @Header("token") token:String,
        @Body user:User
    ): Observable<AuthResponse>
    /** profil*/


    /** blog +*/
    @Headers(Config.API)
    @GET("blog/search/{page}")
    fun searchBlog(
        @Header("query") query:String,
        @Path("page") page:Int
    ): Observable<BlogResponse>

    @Headers(Config.API)
    @GET("blog/list/{page}")
    fun allBlog(
        @Path("page") page:Int
    ): Observable<BlogResponse>

    @Headers(Config.API)
    @Multipart
    @POST("blog/insert")
    fun addBlog(
        @Header("token") token:String,
        @Part("judul") judul: RequestBody,
        @Part("blog") blog: RequestBody,
        @Part("id_user") id_user: RequestBody,
        @Part cover: MultipartBody.Part,
    ): Observable<DefaultResponse>

    @Headers(Config.API)
    @FormUrlEncoded
    @POST("blog/insert")
    fun addBlog(
        @Header("token") token:String,
        @Field("judul") judul: String,
        @Field("blog") blog: String,
        @Field("id_user") id_user: String,
    ): Observable<DefaultResponse>

}