package com.arira.ngidol48.utilities

import android.app.Activity
import com.arira.ngidol48.helper.Config.DIRECTORY_IMAGE_AVATAR
import com.arira.ngidol48.helper.Config.DIRECTORY_IMAGE_BLOG
import com.arira.ngidol48.helper.Validasi
import net.alhazmy13.mediapicker.Image.ImagePicker
import net.alhazmy13.mediapicker.Video.VideoPicker

class Picker(var activity: Activity) {

    fun ambilGambarKamera(type:String = "ava"){
        val dir = if (type == "ava"){
            DIRECTORY_IMAGE_AVATAR
        }else{
            DIRECTORY_IMAGE_BLOG
        }
        if (Validasi().ijinKamera(activity)) {
            ImagePicker.Builder(activity)
                .mode(ImagePicker.Mode.CAMERA)
                .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
                .directory(dir)
                .extension(ImagePicker.Extension.PNG)
                .scale(600, 600)
                .allowMultipleImages(false)
                .enableDebuggingMode(true)
                .build()
        }
    }

    fun ambilGambarGaleri(type:String = "ava"){
        val dir = if (type == "ava"){
            DIRECTORY_IMAGE_AVATAR
        }else{
            DIRECTORY_IMAGE_BLOG
        }
        if (Validasi().ijinDokumen(activity)){
        ImagePicker.Builder(activity)
            .mode(ImagePicker.Mode.GALLERY)
            .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
            .directory(dir)
            .extension(ImagePicker.Extension.PNG)
            .scale(600, 600)
            .allowMultipleImages(false)
            .enableDebuggingMode(true)
            .build()
            }
    }

    fun ambilGambarSemua(type:String = "ava"){
        val dir = if (type == "ava"){
            DIRECTORY_IMAGE_AVATAR
        }else{
            DIRECTORY_IMAGE_BLOG
        }
        if (Validasi().ijinDokumenAndCamera(activity)){
        ImagePicker.Builder(activity)
            .mode(ImagePicker.Mode.CAMERA_AND_GALLERY)
            .compressLevel(ImagePicker.ComperesLevel.MEDIUM)
            .directory(dir)
            .extension(ImagePicker.Extension.PNG)
            .scale(600, 600)
            .allowMultipleImages(false)
            .enableDebuggingMode(true)
            .build()

        }
    }

    fun ambilVideo(type:String = "ava"){
        val dir = if (type == "ava"){
            DIRECTORY_IMAGE_AVATAR
        }else{
            DIRECTORY_IMAGE_BLOG
        }
        if (Validasi().ijinDokumenAndCamera(activity)){
            VideoPicker.Builder(activity)
                .mode(VideoPicker.Mode.CAMERA_AND_GALLERY)
                .directory(dir)
                .extension(VideoPicker.Extension.MP4)
                .enableDebuggingMode(Validasi().isDebug())
                .build()
        }

    }
}