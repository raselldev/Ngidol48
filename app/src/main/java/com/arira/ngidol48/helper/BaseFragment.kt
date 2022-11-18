package com.arira.ngidol48.helper

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.SheetDetailMemberBinding
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.activity.detailMember.DetailMemberActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

open class BaseFragment : Fragment(){

    lateinit var root: View
    var toast = HelperToast()

    val curDay:String = SimpleDateFormat(
        "d",
        Locale("ID")
    ).format(
        Date()
    )

    fun showSheetMember(member: Member){
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)

        val bindingSheet: SheetDetailMemberBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.sheet_detail_member,
            null,
            false
        )
        bottomSheetDialog.dismissWithAnimation = true

        bottomSheetDialog.setContentView(bindingSheet.root)

        bindingSheet.tvTeam.text = member.kategori
        bindingSheet.tvNama.text = member.nama_lengkap
        bindingSheet.tvShow.text = getString(R.string.teks_s_show, member.showtime)
        Glide.with(this).load(Config.BASE_STORAGE_JKT + member.avatar).into(bindingSheet.ivAva)

        if (member.generasi != "0"){
            bindingSheet.tvGenerasi.text = "Gen ${member.generasi}"
            bindingSheet.tvGenerasi.visibility = View.VISIBLE
        }else{
            bindingSheet.tvGenerasi.visibility = View.GONE
        }


        if (member.jiko.isNotEmpty()){
            bindingSheet.tvJiko.text = "`${member.jiko}`"
            bindingSheet.tvJiko.visibility = View.VISIBLE
        }else{
            bindingSheet.tvJiko.visibility = View.GONE
        }

        bindingSheet.btnDetail.setOnClickListener {
            bottomSheetDialog.dismiss()
            Go(requireContext()).move(DetailMemberActivity::class.java, data = member)
        }


        bottomSheetDialog.show()
    }

    fun openTwitter(username:String){
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("twitter://user?screen_name=$username")
                )
            )
        } catch (e: Exception) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/#!/$username")
                )
            )
        }
    }

    fun openTikTokProfile(username:String) {
        val uri: Uri = Uri.parse("https://www.tiktok.com/${username}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.zhiliaoapp.musically")


        try {
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }else{
                val forBrowser = Intent(Intent.ACTION_VIEW, uri)
                startActivity(forBrowser)
            }
        } catch (e: ActivityNotFoundException) {
            val forBrowser = Intent(Intent.ACTION_VIEW, uri)
            startActivity(forBrowser)
        }

    }

    fun launchInsta(uname:String) {
        val uriForApp: Uri = Uri.parse("http://instagram.com/_u/${uname}")
        val forApp = Intent(Intent.ACTION_VIEW, uriForApp)

        val uriForBrowser: Uri = Uri.parse("http://instagram.com/${uname}")
        val forBrowser = Intent(Intent.ACTION_VIEW, uriForBrowser)

        forApp.component =
            ComponentName(
                "com.instagram.android",
                "com.instagram.android.activity.UrlHandlerActivity"
            )

        try {
            startActivity(forApp)
        } catch (e: ActivityNotFoundException) {
            startActivity(forBrowser)
        }
    }
    
}