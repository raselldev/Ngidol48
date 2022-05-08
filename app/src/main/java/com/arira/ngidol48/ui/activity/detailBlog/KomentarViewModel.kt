package com.arira.ngidol48.ui.activity.detailBlog

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arira.ngidol48.R
import com.arira.ngidol48.model.Komentar
import com.arira.ngidol48.network.response.KomentarResponse
import com.arira.ngidol48.repo.KomentarRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class KomentarViewModel : ViewModel() {

    private lateinit var subscription: Disposable
    @SuppressLint("StaticFieldLeak")
    var context: Context? = null
    var repo = KomentarRepo()
    val data = MutableLiveData<KomentarResponse>()
    val dataInsert = MutableLiveData<KomentarResponse>()
    val dataDelete = MutableLiveData<KomentarResponse>()
    var isLoading = MutableLiveData<Boolean>()

    var errorMsg = MutableLiveData<String>()

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

    fun getResponse(): LiveData<KomentarResponse> {
        return data
    }

    fun getResponseDelete(): LiveData<KomentarResponse> {
        return dataDelete
    }

    fun hitAll(id:String) {
        subscription = repo.listKomentar(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoading.value = true }
            .doOnTerminate { isLoading.value = false }
            .subscribe(
                { result ->
                    if (result.code == 1){
                        data.value = result
                    }else{
                        errorMsg.value = result.message + ". 2002"
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

    fun komentar(komentar:Komentar) {
        subscription = repo.komentar(komentar)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {}
            .doOnTerminate {}
            .subscribe(
                { result ->
                    if (result.code == 1){
                        dataInsert.value = result
                    }else{
//                        errorMs'g.value = result.message + ". 2002"
                    }
                },
                { error ->

                }

            )
    }

    fun delete(komentar:Komentar, position:Int) {
        subscription = repo.delete(komentar)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {}
            .doOnTerminate {}
            .subscribe(
                { result ->
                    if (result.code == 1) {
                        result.position = position
                        dataDelete.value = result

                    }else{
                        errorMsg.value = result.message + ". 2002"
                    }
                },
                { error ->

                }

            )
    }


}