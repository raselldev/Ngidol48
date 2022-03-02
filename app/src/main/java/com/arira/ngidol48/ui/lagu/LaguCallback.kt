package com.arira.ngidol48.ui.lagu

import com.arira.ngidol48.model.Song

interface LaguCallback {
    fun onSelectSong(lagu: Song){
    }

    fun onSelectOtherSong(lagu:Song){

    }
}