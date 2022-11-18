package com.arira.ngidol48.ui.activity.detailLagu

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.network.response.SongResponse
import com.arira.ngidol48.repo.SongRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SongDetailViewModel : ViewModel() {

    private lateinit var subscription: Disposable
    var context: Context? = null
    var repo = SongRepo()
    val data = MutableLiveData<SongResponse>()
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

    fun getResponse(): LiveData<SongResponse> {
        return data
    }

    fun detail(idLagu:String) {
        val iduser = if (user.id.isEmpty()){
            "0"
        }else{
            user.id
        }
        subscription = repo.detail(iduser, idLagu)
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

    fun  countStart(start:Int, html:String):Int{
        var iStart = 0

        if (start > 0){
            val c = html.take(start)
            iStart = if (c.contains("\n\n")){
                c.lastIndexOf("\n\n")
            }else{
                0
            }



            if (iStart < 0){
                iStart = 0
            }
        }

        return  iStart
    }

    fun coutToNextParaph(start:Int, html:String):Int{
        val c = html.removeRange(0, start)
        var nextParaph = 0

        nextParaph = if (c.contains("\n\n")){
    //            Log.e("FIND", "contains with start ${start}")
            start + c.indexOf("\n\n", 0)
        }else{
    //            Log.e("FIND", "not have  with start ${start}")
            start + c.length
        }





        return  nextParaph
    }


}