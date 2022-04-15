package com.arira.ngidol48.ui.handshakeMember

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.HandshakeByMemberAdapter
import com.arira.ngidol48.databinding.ActivityHandshakeByMemberBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Config.extra_id
import com.arira.ngidol48.model.DetailHandshake
import com.arira.ngidol48.model.Handshake
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.handshake.HandshakeViewModel
import com.arira.ngidol48.ui.handshake.HistoryHandshakeActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide

class HandshakeByMemberActivity : BaseActivity() {
    private lateinit var binding: ActivityHandshakeByMemberBinding
    private lateinit var viewModel: HandshakeViewModel
    private var listDetail:ArrayList<DetailHandshake> = ArrayList()
    private var idMember:String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_handshake_by_member)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_handshake_by_member)
        setToolbar(getString(R.string.teks_handshake_vc), binding.toolbar)
        idMember = intent.getStringExtra(extra_id) ?: "0"

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HandshakeViewModel::class.java]
        viewModel.context = this

        observerData()

        viewModel.hitByMember(idMember)

        action()
    }

    fun action(){
        binding.tvRiwayat.setOnClickListener {
            Go(this).move(HistoryHandshakeActivity::class.java)
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitByMember(idMember)
        }

        binding.tvReload.setOnClickListener {
            viewModel.hitByMember(idMember)
        }
    }

    fun observerData(){
        viewModel.getLoading().observe(this, Observer {
            it.let {
                if (it){
                    binding.divKosong.visibility = View.GONE
                    binding.shimmer.visibility = View.VISIBLE
                    binding.shimmer.startShimmer()
                }else{
                    binding.shimmer.visibility = View.GONE
                    binding.shimmer.stopShimmer()
                }
            }
        })

        viewModel.getError().observe(this, Observer {
            it.let {
                if (it != null){
                    binding.tvRiwayat.visibility = View.GONE
                    binding.tvReload.visibility = View.VISIBLE

                    binding.divKosong.visibility = View.VISIBLE

                }
            }
        })

        viewModel.getResponse().observe(this, Observer {
            it.let {
                if (it != null) {
                    loadProfil(it.member)
                    if (it.handshake_member.isNotEmpty()){

                        loadData(it.handshake_member)
                    }else{
                        binding.divKosong.visibility = View.VISIBLE

                        binding.tvRiwayat.visibility = View.VISIBLE
                        binding.tvReload.visibility = View.GONE
                    }
                }
            }
        })
    }
    
    private fun loadProfil(member: Member){
        binding.tvTeam.text = member.kategori
        binding.tvNama.text = member.nama_lengkap
        Glide.with(this).load(Config.BASE_STORAGE_JKT + member.avatar).into(binding.ivAva)

        if (member.generasi != "0"){
            binding.tvGenerasi.text = "Gen ${member.generasi}"
            binding.tvGenerasi.visibility = View.VISIBLE
        }else{
            binding.tvGenerasi.visibility = View.GONE
        }
    }

    private fun loadData(listHandshake:List<Handshake>){
        listDetail.clear()

        for (data in listHandshake){
            if (alreadyTanggal(data.tanggal_envent_handshake)){
                addDataToTanggal(data)
            }else{
                //memuat item baru
                val detailHandshake = DetailHandshake()
                detailHandshake.tanggal = data.tanggal_envent_handshake
                detailHandshake.event_name = data.nama_event_handshake
                detailHandshake.sesi = data.sesi
                detailHandshake.waktu = data.waktu
                detailHandshake.standby = data.standby
                detailHandshake.member_list_name = data.nama_member
                detailHandshake.id_handshake = data.id_handshake
                detailHandshake.is_expand = true
                detailHandshake.handshake.add(data)
                listDetail.add(detailHandshake)
            }
        }

        binding.rvData.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = HandshakeByMemberAdapter(listDetail)
        }
    }

    private fun addDataToTanggal(handshake: Handshake){
        for (i in listDetail.indices){
            if (listDetail[i].tanggal == handshake.tanggal_envent_handshake){
                listDetail[i].handshake.add(handshake)
                listDetail[i].member_list_name += ", ${handshake.nama_member}"
            }
        }
    }

    private fun alreadyTanggal(tanggal:String):Boolean{
        for (detail in listDetail){
            if (detail.tanggal == tanggal){
                return true
            }
        }

        return false
    }
}