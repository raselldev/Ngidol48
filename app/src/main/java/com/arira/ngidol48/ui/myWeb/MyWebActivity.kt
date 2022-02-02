package com.arira.ngidol48.ui.myWeb

import android.os.Bundle
import android.view.KeyEvent
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ActivityMyWebBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config

class MyWebActivity : BaseActivity() {

    lateinit var binding:ActivityMyWebBinding
    private var myWebFragment: MyWebFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_web)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_web)

        setToolbar(getString(R.string.app_name), binding.toolbar)

        val link = intent.getStringExtra(Config.extra_url) ?: ""

        myWebFragment = MyWebFragment(link, getString(R.string.app_name)).newInstance(link,  getString(R.string.app_name))

        myWebFragment?.let {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.frame, it)
                .commit()
        }

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