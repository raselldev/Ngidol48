package com.arira.ngidol48.ui.profil

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.model.User
import com.arira.ngidol48.network.response.AuthResponse
import com.arira.ngidol48.repo.ProfilRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class EditProfilViewModel : ViewModel() {

    private lateinit var subscription: Disposable
    var context: Context? = null
    private var repo = ProfilRepo()
    private val data = MutableLiveData<AuthResponse>()
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

    fun getResponse(): LiveData<AuthResponse> {
        return data
    }

    fun updateDataProfil(model: User){

        subscription = repo.updateProfil(model)
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


    fun changeAvatar(avatar: File) {
        val id = pref.getUser().id
        val docFile = avatar.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val partAvatar = MultipartBody.Part.createFormData("image", avatar.name, docFile)
        val partId = id.toRequestBody("text/plain".toMediaTypeOrNull())
        subscription = repo.changeAvatar(partAvatar, partId)
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