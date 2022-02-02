package com.arira.ngidol48.repo

import com.arira.ngidol48.helper.BaseHelper
import com.arira.ngidol48.network.response.HomeResponse
import com.arira.ngidol48.network.response.MemberResponse
import io.reactivex.Observable

class MemberRepo: BaseHelper() {
    fun allMember(): Observable<MemberResponse> {
        return  ApiServiceServer.member()
    }

}