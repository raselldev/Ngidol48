package com.arira.ngidol48.ui.activity.lagu

import com.arira.ngidol48.model.Song

interface LaguCallback {
    fun onSelectSong(lagu: Song){
    }

    fun onSelectSong(lagu: Song, setlist:List<Song>){
    }

    fun onSelectOtherSong(lagu:Song){

    }
}