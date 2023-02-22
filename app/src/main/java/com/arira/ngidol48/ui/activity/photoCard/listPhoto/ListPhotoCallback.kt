package com.arira.ngidol48.ui.activity.photoCard.listPhoto

import com.arira.ngidol48.model.PhotoCardImage

interface ListPhotoCallback {
    fun onLongClick(data:PhotoCardImage)
    fun onClick(data: PhotoCardImage)
}