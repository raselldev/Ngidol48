package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.HomeResponse
import com.arira.ngidol48.network.response.NewsResponse
import io.reactivex.Observable

class NewsRepo: BaseHelper() {
    fun allNews(): Observable<NewsResponse> {
        return  ApiServiceServer.allNews()
    }

}