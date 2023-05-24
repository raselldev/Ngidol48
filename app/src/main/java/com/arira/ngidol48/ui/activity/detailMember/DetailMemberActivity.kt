package com.arira.ngidol48.ui.activity.detailMember

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.EventHomeAdapter
import com.arira.ngidol48.databinding.ActivityDetailMemberBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.activity.handshakeMember.HandshakeByMemberActivity
import com.arira.ngidol48.ui.activity.mng.MngByMemberActivity
import com.arira.ngidol48.ui.activity.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide

class DetailMemberActivity : BaseActivity() {
    lateinit var binding: ActivityDetailMemberBinding

    private var member: Member = Member()
    private lateinit var viewModel: DetailMemberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_member)
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_member)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[DetailMemberViewModel::class.java]
        viewModel.context = this

        setNewToolbar(getString(R.string.teks_detail_member), binding.toolbar)

        member = intent.getParcelableExtra(extra_model) ?: Member()
        setData()

        observeData()

        viewModel.hitAll(member.id_member)

    }
    
    private fun setData(){

        binding.tvTinggil.text = member.tinggi_badan
        binding.tvTanggalLahir.text = member.tanggal_lahir
        binding.tvGolDarah.text = member.gol_darah
        binding.tvHoroskop.text = member.horoskop
        binding.tvPanggilan.text = member.nama_panggilan
        binding.tvTeam.text = member.kategori
        binding.tvNama.text = member.nama_lengkap

        binding.tvShow.text = getString(R.string.teks_s_show, member.showtime)
        Glide.with(this).load(Config.BASE_STORAGE_JKT + member.avatar).into(binding.ivAva)

        if (member.generasi != "0"){
            binding.tvGenerasi.text = "Gen ${member.generasi}"
            binding.tvGenerasi.visibility = View.VISIBLE
        }else{
            binding.tvGenerasi.visibility = View.GONE
        }


        if (member.jiko.isNotEmpty()){
            binding.tvJiko.text = member.jiko
            binding.tvJiko.visibility = View.VISIBLE
        }else{
            binding.tvJiko.visibility = View.GONE
        }

        binding.linHandshake.setOnClickListener {
            Go(this).move(HandshakeByMemberActivity::class.java, id = member.id_member)
        }

        binding.linMng.setOnClickListener {
            Go(this).move(MngByMemberActivity::class.java, id = member.id_member)
        }

        //media sosial
        val showMedia = member.media_instagram.isNotEmpty()

        if (showMedia){
            binding.linMedia.visibility = View.VISIBLE
            binding.tvInstagram.text = "${member.media_instagram}"
            binding.tvTiktok.text = "${member.media_tiktok}"
            binding.tvTwitter.text = "${member.media_twitter}"

            binding.linInstagram.setOnClickListener {
                launchInsta(member.media_instagram)
            }

            binding.linTiktok.setOnClickListener {
                openTikTokProfile(member.media_tiktok)
            }

            binding.linTwitter.setOnClickListener {
                openTwitter(member.media_twitter)
            }
        }else{
            binding.linMedia.visibility = View.GONE
        }

        if(member.media_showroom.isNotEmpty()){
            binding.tvShowroom.visibility = View.VISIBLE
            binding.tvShowroom.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(member.media_showroom)
                    )
                )
            }
        }else{
            binding.tvShowroom.visibility = View.GONE
        }

        //fanbase
        if (member.fanbase_name.isNotEmpty()){
            binding.linFans.visibility = View.VISIBLE
            binding.tvFanbase.text = member.fanbase_name
            binding.tvFanbaseVisit.setOnClickListener {
                Go(this).move(MyWebActivity::class.java, url = member.fanbase_page)
            }
        }else{
            binding.linFans.visibility = View.GONE
        }
    }

    fun observeData(){
        viewModel.getLoading().observe(this){

        }

        viewModel.getResponse().observe(this){
            if (it != null){
                if (it.events.isNotEmpty()){
                    binding.linJadwalPerform.visibility = View.VISIBLE
                    binding.rvData.apply {
                        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        adapter = EventHomeAdapter(it.events)
                    }
                }else{
                    binding.linJadwalPerform.visibility = View.GONE
                }
            }
        }

        viewModel.getError().observe(this){

        }
    }
}