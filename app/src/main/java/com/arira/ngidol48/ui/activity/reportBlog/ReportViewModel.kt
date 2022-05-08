package com.arira.ngidol48.ui.activity.reportBlog

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.arira.ngidol48.R
import com.arira.ngidol48.model.Report
import com.arira.ngidol48.network.response.*
import com.arira.ngidol48.repo.ReportRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ReportViewModel : ViewModel() {

    private lateinit var subscription: Disposable
    var context: Context? = null
    var repo = ReportRepo()
    val data = MutableLiveData<ReportResponse>()
    val dataReport = MutableLiveData<ReportResponse>()
    var isLoading = MutableLiveData<Boolean>()

    var errorMsg = MutableLiveData<String>()

    init {
        data.value = null
        dataReport.value = null
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

    fun getResponse(): LiveData<ReportResponse> {
        return data
    }

    fun getResponseReport(): LiveData<ReportResponse> {
        return dataReport
    }

    fun getList() {
        subscription = repo.listReport()
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

    fun report(report:Report) {
        subscription = repo.report(report)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { isLoading.value = true }
            .doOnTerminate { isLoading.value = false }
            .subscribe(
                { result ->
                    dataReport.value = result
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




}