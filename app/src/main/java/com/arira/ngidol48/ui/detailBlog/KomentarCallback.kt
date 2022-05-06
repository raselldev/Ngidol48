package com.arira.ngidol48.ui.detailBlog

import com.arira.ngidol48.model.Komentar

interface KomentarCallback {
    fun delete(komentar: Komentar, position:Int){

    }
}