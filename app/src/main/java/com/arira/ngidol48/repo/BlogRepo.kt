package com.arira.ngidol48.repo

import com.arira.ngidol48.app.App
import com.arira.ngidol48.app.App.Companion.token
import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BlogRepo: BaseHelper() {
    fun allBlog(page:Int, kategori:Int, querySearch:String): Observable<BlogResponse> {
        if (querySearch.isEmpty()){
            return  ApiServiceServer.allBlog(page, kategori)
        }else{
            return  ApiServiceServer.allBlogSearch(querySearch, kategori)
        }

    }

    fun addBlog(cover: MultipartBody.Part, id_user: RequestBody, judul: RequestBody, blog: RequestBody, kategoriId: RequestBody): Observable<DefaultResponse>{
        return  ApiServiceServerFile.addBlog(token, judul, blog, id_user, kategoriId, cover)
    }

    fun addBlog(id_user: String, judul: String, blog: String, kategoriId: String): Observable<DefaultResponse>{
        return  ApiServiceServerFile.addBlog(token, judul, blog, id_user, kategoriId)
    }

    fun deleteBlog(id: String,): Observable<DefaultResponse>{
        return  ApiServiceServerFile.deleteBlog(token, id)
    }

    fun kategori(): Observable<BlogKategoriResponse> {
        return  ApiServiceServer.kategoriBlog()
    }



    fun searchBlog(query:String, page:Int): Observable<BlogResponse> {
        return  ApiServiceServer.searchBlog(query, page)
    }

}