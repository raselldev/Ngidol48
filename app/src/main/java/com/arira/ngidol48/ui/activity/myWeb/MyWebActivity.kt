package com.arira.ngidol48.ui.activity.myWeb

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ActivityMyWebBinding
import com.arira.ngidol48.databinding.SheetMenuBrowserBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.google.android.material.bottomsheet.BottomSheetDialog

class MyWebActivity : BaseActivity() {

    lateinit var binding:ActivityMyWebBinding
    private var myWebFragment: MyWebFragment? = null
    private var link = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_web)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_web)
        binding.toolbar.ivMenu.visibility = View.VISIBLE

        setToolbar(getString(R.string.app_name), binding.toolbar)

        link = intent.getStringExtra(Config.extra_url) ?: ""

        myWebFragment = MyWebFragment(link, getString(R.string.app_name))

        myWebFragment?.let {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame, it)
                .commit()
        }

        binding.toolbar.ivMenu.setOnClickListener {
            sheetMenu()
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

            myWebFragment?.reloadPage()

        }

        bottomSheetDialog.show()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event!!.action === KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {

                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        if (myWebFragment!!.canGoBack()){
            myWebFragment!!.goBack()
        }else{
            finish()
        }
    }
}