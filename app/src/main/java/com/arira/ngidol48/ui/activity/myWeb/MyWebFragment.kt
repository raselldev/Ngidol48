package com.arira.ngidol48.ui.activity.myWeb

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.annotation.RequiresApi
import com.arira.ngidol48.helper.BaseFragment
import com.google.android.material.snackbar.Snackbar
import java.util.*


class MyWebFragment(var link: String, var title: String) : BaseFragment() {

    private val CHROME_FULL = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0"

    private val FILECHOOSER_RESULTCODE = 200
    private val CAMERA_PERMISSION_RESULTCODE = 201
    private val AUDIO_PERMISSION_RESULTCODE = 202
    private val VIDEO_PERMISSION_RESULTCODE = 203
    private val STORAGE_PERMISSION_RESULTCODE = 204

    private var mUploadMessage: ValueCallback<Array<Uri>>? = null
    private lateinit var mCurrentPermissionRequest: PermissionRequest

    var DEBUG_TAG = javaClass.simpleName

    fun MyWebFragment() {
    }

    lateinit var myWebView: WebView
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.let {

            myWebView = WebView(requireActivity())
            myWebView.loadUrl(link)
            myWebView.settings.javaScriptEnabled = true
            myWebView.settings.useWideViewPort = true
            myWebView.getSettings().setJavaScriptEnabled(true) //for wa web

            myWebView.getSettings().setAllowContentAccess(true) // for camera

            myWebView.getSettings().setAllowFileAccess(true)
            myWebView.getSettings().setAllowFileAccessFromFileURLs(true)
            myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true)
            myWebView.getSettings().setSaveFormData(true)
            myWebView.getSettings().setLoadsImagesAutomatically(true)
            myWebView.getSettings().setBlockNetworkImage(false)
            myWebView.getSettings().setBlockNetworkLoads(false)
            myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true)
            myWebView.getSettings().setNeedInitialFocus(false)
            myWebView.getSettings().setGeolocationEnabled(true)
            myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)


            myWebView.getSettings().setDomStorageEnabled(true) //for html5 app

            myWebView.getSettings().setDatabaseEnabled(true)


//            myWebView.settings.userAgentString = CHROME_FULL

            myWebView.webViewClient = WebViewClient()

            root = myWebView

            myWebView.setWebChromeClient(object : WebChromeClient() {

                override fun onPermissionRequest(request: PermissionRequest?) {
                    super.onPermissionRequest(request)
                }

                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    mUploadMessage = filePathCallback
                    val chooserIntent = fileChooserParams!!.createIntent()
                    startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE)
                    return true
                }

            })

            myWebView.setWebViewClient(object : WebViewClient() {


                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return if (url.startsWith("http://") || url.startsWith("https://")) false else try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        view.context.startActivity(intent)
                        true
                    } catch (e: Exception) {
                        Log.i(
                            "MainActiviy",
                            "shouldOverrideUrlLoading Exception:$e"
                        )
                        true
                    }
                }
            })
        }

//        myWebView.getSettings().setUserAgentString(CHROME_FULL)

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            FILECHOOSER_RESULTCODE -> if (data != null) {
                if (resultCode == Activity.RESULT_CANCELED || data.data == null) {
                    mUploadMessage!!.onReceiveValue(null)
                } else {
//                    val result = data.data!!
                    val results = arrayOf(data.data!!)
                    //                results[0] = result
                    mUploadMessage!!.onReceiveValue(results)
                }
            }
            else -> Log.d(
                DEBUG_TAG, "Got activity result with unknown request code " +
                        requestCode + " - " + data.toString()
            )
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            VIDEO_PERMISSION_RESULTCODE -> if (permissions.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                try {
                    mCurrentPermissionRequest.grant(mCurrentPermissionRequest.getResources())
                } catch (e: RuntimeException) {
                    Log.e(DEBUG_TAG, "Granting permissions failed", e)
                }
            } else {
                showSnackbar("Permission not granted, can't use video.")
                mCurrentPermissionRequest.deny()
            }
            CAMERA_PERMISSION_RESULTCODE, AUDIO_PERMISSION_RESULTCODE ->                 //same same
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        mCurrentPermissionRequest.grant(mCurrentPermissionRequest.getResources())
                    } catch (e: RuntimeException) {
                        Log.e(DEBUG_TAG, "Granting permissions failed", e)
                    }
                } else {
                    showSnackbar(
                        "Permission not granted, can't use " +
                                if (requestCode == CAMERA_PERMISSION_RESULTCODE) "camera" else "microphone"
                    )
                    mCurrentPermissionRequest.deny()
                }
            STORAGE_PERMISSION_RESULTCODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: check for current download and enqueue it
            } else {
                showSnackbar("Permission not granted, can't download to storage")
            }
            else -> Log.d(
                DEBUG_TAG, "Got permission result with unknown request code " +
                        requestCode + " - " + Arrays.asList<String>(*permissions).toString()
            )
        }
    }

    private fun showSnackbar(msg: String) {
        activity.let {
            if (it != null) {
                it.runOnUiThread(Runnable {
                    val snackbar = Snackbar.make(
                        it.findViewById<View>(android.R.id.content),
                        msg,
                        900
                    )
                    snackbar.setAction(
                        "dismiss"
                    ) { view: View? -> snackbar.dismiss() }
                    snackbar.setActionTextColor(Color.parseColor("#075E54"))
                    snackbar.show()
                })
            }
        }

    }

    fun canGoBack(): Boolean {
        return myWebView.canGoBack()
    }

    fun reloadPage(){
        myWebView.reload()
    }

    fun goBack() {
        if(myWebView.canGoBack()){
            myWebView.goBack()
        }

    }

}