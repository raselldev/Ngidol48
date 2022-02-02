package com.arira.ngidol48.helper

import android.view.View
import androidx.fragment.app.Fragment
import com.arira.ngidol48.helper.HelperToast

open class BaseFragment : Fragment(){

    lateinit var root: View
    var toast = HelperToast()
    
}