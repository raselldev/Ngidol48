package com.arira.ngidol48.ui.activity.mng

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.MngByMemberAdapter
import com.arira.ngidol48.databinding.ActivityMngByMemberBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Config.extra_id
import com.arira.ngidol48.model.DetailMng
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.model.Mng
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide

class MngByMemberActivity : BaseActivity() {
    private lateinit var binding: ActivityMngByMemberBinding
    private lateinit var viewModel: MngViewModel
    private var listDetail:ArrayList<DetailMng> = ArrayList()
    private var idMember:String = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mng_by_member)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_mng_by_member)
        setNewToolbar(getString(R.string.teks_mng_vc), binding.toolbar)
        idMember = intent.getStringExtra(extra_id) ?: "0"

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MngViewModel::class.java]
        viewModel.context = this

        observerData()

        viewModel.hitByMember(idMember)

        action()
    }

    fun action(){
        binding.tvRiwayat.setOnClickListener {
            Go(this).move(HistoryMngActivity::class.java)
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
                    if (it.mng_member.isNotEmpty()){

                        loadData(it.mng_member)
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

    private fun loadData(listMng:List<Mng>){
        listDetail.clear()

        for (data in listMng){
            if (alreadyTanggal(data.tanggal_envent_mng)){
                addDataToTanggal(data)
            }else{
                //memuat item baru
                val detailMng = DetailMng()
                detailMng.tanggal = data.tanggal_envent_mng
                detailMng.event_name = data.nama_event_mng
                detailMng.sesi = data.sesi
                detailMng.waktu = data.waktu
                detailMng.penukaran = data.penukaran
                detailMng.member_list_name = data.nama_member
                detailMng.id_mng = data.id_mng
                detailMng.is_expand = true
                detailMng.mng.add(data)
                listDetail.add(detailMng)
            }
        }

        binding.rvData.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MngByMemberAdapter(listDetail)
        }
    }

    private fun addDataToTanggal(Mng: Mng){
        for (i in listDetail.indices){
            if (listDetail[i].tanggal == Mng.tanggal_envent_mng){
                listDetail[i].mng.add(Mng)
                listDetail[i].member_list_name += ", ${Mng.nama_member}"
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