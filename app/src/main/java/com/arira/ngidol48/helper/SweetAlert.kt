package com.arira.ngidol48.helper

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog

object SweetAlert {

    @SuppressLint("StaticFieldLeak")
    private var asd: SweetAlertDialog? = null
    private var alertDialog: AlertDialog? = null

    fun onFailure(context: Activity?, pesan: String?, close: Boolean = false) {
        if (context != null) {
            if (close) {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops")
                    .setContentText(pesan)
                    .setConfirmClickListener {
                        context.finish()
                    }
                    .show()
            } else {
                SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oops")
                    .setContentText(pesan)
                    .show()
            }

        }
    }

    fun success(context: Activity, title: String, pesan: String, finish: Boolean = false) {
        if (finish){
            SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(pesan)
                .setConfirmClickListener {
                    it.dismissWithAnimation()
                    it.dismiss()

                    context.finish()
                }
                .show()
        }else{
            SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(pesan)
                .show()
        }

    }

    fun success(context: Activity, title: String, pesan: String, callback: Callbacks) {
        val pDialog = SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
        pDialog.setTitleText(title)
            .setContentText(pesan)
            .setConfirmClickListener {
                callback.onPositiveCliked()
                pDialog.dismiss()
            }
            .show()
    }

    fun warning(context: Activity, title: String, pesan: String) {
        SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
            .setTitleText(title)
            .setContentText(pesan)
            .show()
    }

    fun onLoading(context: Activity?) {
        asd = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        asd?.progressHelper?.barColor = Color.parseColor("#A5DC86")
        asd?.titleText = "Loading"
        asd?.setCancelable(false)
        asd?.show()
    }

    fun onLoadingChat(context: Activity?) {
        asd = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        asd?.progressHelper?.barColor = Color.parseColor("#A5DC86")
        asd?.titleText = "Loading"
        asd?.show()
    }

    fun error(context: Activity, title: String, _pesan: String) {
        val pesan = _pesan
        SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(title)
            .setContentText(pesan)
            .show()
    }

    fun error(context: Activity, title: String, _pesan: String, listener: Callbacks) {
        val pesan = _pesan
        val sweet = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
        sweet.titleText = title
        sweet.contentText = pesan
        sweet.titleText = title
        sweet.setConfirmClickListener {
            listener.onPositiveCliked()
            sweet.dismiss()
        }.show()
    }

    fun dismis() {
        if (asd != null) {
            if (asd!!.isShowing) {
                asd?.dismiss()

            }
        }
    }

    fun alertDismis() {
        if (alertDialog != null) {
            if (alertDialog!!.isShowing) {
                alertDialog?.dismiss()
            }
        }
    }


    interface Callbacks {

        fun onPositiveCliked()
    }

    open class DefaultCallback : Callbacks {


        override fun onPositiveCliked() {
        }

    }


}