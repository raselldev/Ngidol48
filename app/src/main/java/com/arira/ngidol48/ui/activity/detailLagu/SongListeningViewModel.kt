package com.arira.ngidol48.ui.activity.detailLagu

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.network.response.SongResponse
import com.arira.ngidol48.repo.SongRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SongListeningViewModel : ViewModel() {

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

    fun listening(song: Song) {
        subscription = repo.listening(song)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoading.value = true }
            .doOnTerminate { isLoading.value = false }
            .subscribe(
                { result ->

                },
                { error ->
                }

            )
    }


}