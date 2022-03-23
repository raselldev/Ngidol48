package com.arira.ngidol48.repo

import com.arira.ngidol48.app.App
import com.arira.ngidol48.app.App.Companion.token
import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BlogRepo: BaseHelper() {
    fun allBlog(page:Int): Observable<BlogResponse> {
        return  ApiServiceServer.allBlog(page)
    }

    fun addBlog(cover: MultipartBody.Part, id_user: RequestBody, judul: RequestBody, blog: RequestBody): Observable<DefaultResponse>{
        return  ApiServiceServerFile.addBlog(token, judul, blog, id_user, cover)
    }

    fun addBlog(id_user: String, judul: String, blog: String): Observable<DefaultResponse>{
        return  ApiServiceServerFile.addBlog(token, judul, blog, id_user)
    }

    fun searchBlog(query:String, page:Int): Observable<BlogResponse> {
        return  ApiServiceServer.searchBlog(query, page)
    }

}