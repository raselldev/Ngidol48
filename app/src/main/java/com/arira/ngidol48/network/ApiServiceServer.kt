package com.arira.ngidol48.network

import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.model.*
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
    @GET("songlist/detail/{idUser}/{idLagu}")
    fun detailSong(
        @Path("idUser") idUser:String,
        @Path("idLagu") idLagu:String,
    ): Observable<SongResponse>

    @Headers(Config.API)
    @GET("songlist/search")
    fun cariLagu(
        @Query("q") query:String
    ): Observable<SongResponse>

    @Headers(Config.API)
    @POST("player/listening")
    fun listeningSong(
        @Body song:Song
    ): Observable<DefaultResponse>

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

    @Headers(Config.API)
    @GET("fans/my/{id}")
    fun profilById(
        @Header("token") token:String,
        @Path("id") id:Int,
    ): Observable<ProfilResponse>
    /** profil*/


    /** blog +*/
    @Headers(Config.API)
    @GET("blog/category")
    fun kategoriBlog(
    ): Observable<BlogKategoriResponse>

    @Headers(Config.API)
    @GET("blog/search/{page}")
    fun searchBlog(
        @Header("query") query:String,
        @Path("page") page:Int
    ): Observable<BlogResponse>

    @Headers(Config.API)
    @GET("blog/list/{page}/{kategori}")
    fun allBlog(
        @Path("page") page:Int,
        @Path("kategori") kategori:Int,
    ): Observable<BlogResponse>

    @Headers(Config.API)
    @GET("/blog/search")
    fun allBlogSearch(
        @Header("query") query:String,
        @Header("slug") slug:Int,
    ): Observable<BlogResponse>

    @Headers(Config.API)
    @Multipart
    @POST("blog/insert")
    fun addBlog(
        @Header("token") token:String,
        @Part("judul") judul: RequestBody,
        @Part("blog") blog: RequestBody,
        @Part("id_user") id_user: RequestBody,
        @Part("kategori_id") kategori_id: RequestBody,
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
        @Field("kategori_id") kategori_id: String,
    ): Observable<DefaultResponse>

    @Headers(Config.API)
    @FormUrlEncoded
    @POST("blog/delete")
    fun deleteBlog(
        @Header("token") token:String,
        @Field("id") id: String,
    ): Observable<DefaultResponse>

    @Headers(Config.API)
    @GET("blog/detail/{id}")
    fun detailBlog(
        @Header("token") token:String,
        @Path("id") id:String,
    ): Observable<BlogResponse>

    /**komentar*/
    @Headers(Config.API)
    @GET("comment/blog/{id}")
    fun listKomentar(
        @Header("token") token:String,
        @Path("id") id:String,
    ): Observable<KomentarResponse>

    @Headers(Config.API)
    @POST("comment/insert")
    fun komentar(
        @Header("token") token:String,
        @Body raw:Komentar
    ): Observable<KomentarResponse>

    @Headers(Config.API)
    @POST("comment/delete")
    fun deleteKomentar(
        @Header("token") token:String,
        @Body raw:Komentar
    ): Observable<KomentarResponse>

    /** report */
    @Headers(Config.API)
    @GET("report/list")
    fun listReport(
        @Header("token") token:String,
    ): Observable<ReportResponse>

    @Headers(Config.API)
    @POST("report/blog")
    fun reportBlog(
        @Header("token") token:String,
        @Body raw:Report
    ): Observable<ReportResponse>


    /** favovite */
    @Headers(Config.API)
    @GET("fav/my/{id}")
    fun listFav(
        @Header("token") token:String,
        @Path("id") id:String,
    ): Observable<SongResponse>

    @Headers(Config.API)
    @POST("fav/add")
    fun addFavorit(
        @Header("token") token:String,
        @Body raw:Song
    ): Observable<FavoritResponse>

    @Headers(Config.API)
    @POST("fav/remove")
    fun removeFavorit(
        @Header("token") token:String,
        @Body raw:Song
    ): Observable<FavoritResponse>



}