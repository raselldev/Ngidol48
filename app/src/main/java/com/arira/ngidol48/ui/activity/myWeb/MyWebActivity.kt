package com.arira.ngidol48.ui.activity.myWeb

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.*
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ActivityMyWebBinding
import com.arira.ngidol48.databinding.SheetMenuBrowserBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MyWebActivity : BaseActivity() {

    lateinit var binding:ActivityMyWebBinding
//    private var myWebFragment: MyWebFragment? = null
    private var link = ""

    private val CHROME_FULL = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:86.0) Gecko/20100101 Firefox/86.0"

    private val FILECHOOSER_RESULTCODE = 200
    private val CAMERA_PERMISSION_RESULTCODE = 201
    private val AUDIO_PERMISSION_RESULTCODE = 202
    private val VIDEO_PERMISSION_RESULTCODE = 203
    private val STORAGE_PERMISSION_RESULTCODE = 204

    private var mUploadMessage: ValueCallback<Array<Uri>>? = null
    private lateinit var mCurrentPermissionRequest: PermissionRequest

    var DEBUG_TAG = javaClass.simpleName

    lateinit var myWebView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_web)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_web)
        binding.toolbar.ivMenu.visibility = View.VISIBLE

        setToolbar(getString(R.string.app_name), binding.toolbar)

        link = intent.getStringExtra(Config.extra_url) ?: ""

//        myWebFragment = MyWebFragment(link, getString(R.string.app_name))
//
//        myWebFragment?.let {
//            supportFragmentManager
//                .beginTransaction()
//                .replace(R.id.frame, it)
//                .commit()
//        }

        binding.toolbar.ivMenu.setOnClickListener {
            sheetMenu()
        }


        //load webview

        myWebView = WebView(this)
        myWebView.loadUrl(link)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.useWideViewPort = true

        myWebView.settings.allowContentAccess = true // for camera
        myWebView.settings.setSupportZoom(true)
        myWebView.settings.setAllowFileAccess(true)
        myWebView.settings.setAllowFileAccessFromFileURLs(true)
        myWebView.settings.setAllowUniversalAccessFromFileURLs(true)
        myWebView.settings.setSaveFormData(true)
        myWebView.settings.setLoadsImagesAutomatically(true)
        myWebView.settings.setBlockNetworkImage(false)
        myWebView.settings.setBlockNetworkLoads(false)
        myWebView.settings.setJavaScriptCanOpenWindowsAutomatically(true)
        myWebView.settings.setNeedInitialFocus(false)
        myWebView.settings.setGeolocationEnabled(true)
        myWebView.settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)


        myWebView.settings.setDomStorageEnabled(true) //for html5 app

        myWebView.settings.setDatabaseEnabled(true)
        myWebView.settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK)


//            myWebView.settings.userAgentString = CHROME_FULL

//            myWebView.webViewClient = WebViewClient()


        myWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                binding.progress.progress = newProgress

                Log.e("PROG", "data : ${newProgress}")
                if (newProgress < 100){
                    binding.progress.visibility = View.VISIBLE
                }else{
                    binding.progress.visibility = View.GONE
                }
            }

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

        }

        myWebView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                CookieManager.getInstance().setAcceptCookie(true)
                CookieManager.getInstance().acceptCookie()
                CookieManager.getInstance().flush()

                binding.progress.visibility = View.GONE
            }

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
        }

        binding.frame.addView(myWebView)

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
        this.let {
            it.runOnUiThread {
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
        if (myWebView.canGoBack()) {
            myWebView.goBack()
        }
    }


        private fun sheetMenu(){
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bindingSheet: SheetMenuBrowserBinding = DataBindingUtil.inflate(layoutInflater, R.layout.sheet_menu_browser, null, false)
        bottomSheetDialog.dismissWithAnimation = true
        bottomSheetDialog.setContentView(bindingSheet.root)


        bindingSheet.linBukaBrowser.setOnClickListener {
            bottomSheetDialog.dismiss()

            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(link)
            startActivity(Intent.createChooser(openURL, getString(R.string.teks_pilih_aplikasi_untuk_membuka)))
        }

        bindingSheet.linRefresh.setOnClickListener {
            bottomSheetDialog.dismiss()

            reloadPage()

        }

        bottomSheetDialog.show()
    }

    override fun onBackPressed() {
        if (canGoBack()){
            goBack()
        }else{
            finish()
        }
    }
}