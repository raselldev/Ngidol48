package com.arira.ngidol48.ui.addBlog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.model.User
import com.arira.ngidol48.network.response.DefaultResponse
import com.arira.ngidol48.repo.BlogRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddBlogViewModel : ViewModel() {

    private lateinit var subscription: Disposable
    var context: Context? = null
    private var repo = BlogRepo()
    private val data = MutableLiveData<DefaultResponse>()
    private var isLoading = MutableLiveData<Boolean>()

    private var errorMsg = MutableLiveData<String>()

    init {
        data.value = null
    }

    override fun onCleared() {
        super.onCleared()

        try{
            subscription.dispose()
        }catch (e: UninitializedPropertyAccessException){

        }

    }

    fun getLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun getError(): LiveData<String> {
        return errorMsg
    }

    fun getResponse(): LiveData<DefaultResponse> {
        return data
    }

    fun add(judul: String, blog:String){

        subscription = repo.addBlog(pref.getUser().id, judul, blog)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoading.value = true }
            .doOnTerminate { isLoading.value = false }
            .subscribe(
                { result ->
                    when (result.code) {
                        1 -> {
                            data.value = result
                            if (result.user.id.isNotEmpty()){
                                user = result.user
                                pref.setUser(user)
                            }
                        }
                        else -> {
                            errorMsg.value = result.message + ". 401"
                        }
                    }
                },
                { error ->
                    try{

                        when {
                            error.message!!.contains("Failed to connect", true) -> {
                                errorMsg.value = context?.getString(R.string.teks_tidak_dapat_tehubung_ke_server)
                            }
                            error.message.toString().contains("4") -> {
                                errorMsg.value = context?.getString(R.string.teks_terjadi_kesalahan_code, 4000)
                            }
                            error.message.toString().contains("5") -> {
                                errorMsg.value = context?.getString(R.string.teks_terjadi_kesalahan_code, 5000)
                            }
                            else -> {
                                errorMsg.value = context?.getString(R.string.teks_terjadi_kesalahan)
                            }
                        }

                    }catch (e:KotlinNullPointerException){
                        errorMsg.value = "Terjadi kesalahan saat memproses data. coba beberapa saat lagi. 2003"
                    }
                }

            )
    }

    fun add(cover: File, judul:String, blog:String) {
        val id = pref.getUser().id
        val docFile = cover.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val partCover = MultipartBody.Part.createFormData("cover", cover.name, docFile)
        val partIdUser = id.toRequestBody("text/plain".toMediaTypeOrNull())
        val partIdJudul = judul.toRequestBody("text/plain".toMediaTypeOrNull())
        val partBlog = blog.toRequestBody("text/plain".toMediaTypeOrNull())
        subscription = repo.addBlog(partCover, partIdUser, partIdJudul, partBlog)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoading.value = true }
            .doOnTerminate { isLoading.value = false }
            .subscribe(
                { result ->
                    when (result.code) {
                        1 -> {
                            data.value = result
                            user.avatar = result.user.avatar
                            pref.setUser(user)
                        }
                        else -> {
                            errorMsg.value = result.message + ". 401"
                        }
                    }


                },
                { error ->
                    try{

                        when {
                            error.message!!.contains("Failed to connect", true) -> {
                                errorMsg.value = context?.getString(R.string.teks_tidak_dapat_tehubung_ke_server)
                            }
                            error.message.toString().contains("4") -> {
                                errorMsg.value = context?.getString(R.string.teks_terjadi_kesalahan_code, 4000)
                            }
                            error.message.toString().contains("5") -> {
                                errorMsg.value = context?.getString(R.string.teks_terjadi_kesalahan_code, 5000)
                            }
                            else -> {
                                errorMsg.value = context?.getString(R.string.teks_terjadi_kesalahan_deskripsi)
                            }
                        }
                    }catch (e:KotlinNullPointerException){
                        errorMsg.value = "Terjadi kesalahan saat memproses data. coba beberapa saat lagi. 2003"
                    }
                }

            )
    }

}