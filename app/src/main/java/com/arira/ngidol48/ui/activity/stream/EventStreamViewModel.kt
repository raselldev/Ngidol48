package com.arira.ngidol48.ui.activity.stream

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App.Companion.user
import com.arira.ngidol48.model.ViewLiveStream
import com.arira.ngidol48.network.response.StreamResponse
import com.arira.ngidol48.network.response.ViewStreamResponse
import com.arira.ngidol48.repo.StreamRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EventStreamViewModel : ViewModel() {

    private lateinit var subscription: Disposable
    var context: Context? = null
    var repo = StreamRepo()
    val data = MutableLiveData<StreamResponse>()
    val dataViews = MutableLiveData<ViewStreamResponse>()
    var isLoading = MutableLiveData<Boolean>()

    var errorMsg = MutableLiveData<String>()

    init {
        data.value = null
        dataViews.value = null
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

    fun getResponse(): LiveData<StreamResponse> {
        return data
    }

    fun getViews(): LiveData<ViewStreamResponse> {
        return dataViews
    }

    fun detail(id:String) {
        subscription = repo.detail(id)
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

    fun views(id:String) {
        subscription = repo.views(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {/* isLoading.value = true */}
            .doOnTerminate { /*isLoading.value = false */}
            .subscribe { result ->
                if (result.code == 1) {
                    dataViews.value = result
                }
            }
    }

    fun watch(id:String) {
        val dataWatch = ViewLiveStream()
        dataWatch.stream_id = id
        dataWatch.user_id = if (user.id.isEmpty()){
            user.fullname
        }else{
            user.id
        }

        subscription = repo.watch(dataWatch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {/* isLoading.value = true */}
            .doOnTerminate { /*isLoading.value = false */}
            .subscribe {
            }
    }

    fun leave(id:String) {
        val dataWatch = ViewLiveStream()
        dataWatch.stream_id = id
        dataWatch.user_id = if (user.id.isEmpty()){
            user.fullname
        }else{
            user.id
        }

        subscription = repo.leave(dataWatch)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {/* isLoading.value = true */}
            .doOnTerminate { /*isLoading.value = false */}
            .subscribe {
            }
    }


}