package com.arira.ngidol48.ui.activity.handshake

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.DetailHandshakeAdapter
import com.arira.ngidol48.adapter.TanggalHandshakeAdapter
import com.arira.ngidol48.databinding.ActivityHandshakeBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.model.DetailHandshake
import com.arira.ngidol48.model.Handshake
import com.arira.ngidol48.model.ParentHandshake
import com.arira.ngidol48.ui.activity.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go

class HandshakeActivity : BaseActivity(), HandshakeCallback{
    private lateinit var binding: ActivityHandshakeBinding
    private lateinit var viewModel: HandshakeViewModel

    private var active_id:String = ""
    private lateinit var tanggalHandshakeAdapter: TanggalHandshakeAdapter

    //for detail data
    private var parentData = ParentHandshake()
    private var listDetail:ArrayList<DetailHandshake> = ArrayList()
    private var buy_url:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_handshake)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_handshake)
        setToolbar(getString(R.string.teks_handshake_vc), binding.toolbar)

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[HandshakeViewModel::class.java]
        viewModel.context = this

        observerData()

        viewModel.hitAll()

        action()
    }

    fun action(){
        binding.tvRiwayat.setOnClickListener {
            Go(this).move(HistoryHandshakeActivity::class.java)
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitAll()
        }

        binding.tvReload.setOnClickListener {
            viewModel.hitAll()
        }

        binding.tvTiketDll.setOnClickListener {
            buy_url = "https://jkt48.com/handshake?id=${parentData.id_handshake}"
            Go(this).move(MyWebActivity::class.java, url = buy_url)
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


                    if (it.handshakes.isNotEmpty()){
//                        binding.rvData.apply {
//                            layoutManager  = LinearLayoutManager(context)
//                            adapter = HandshakeAdapter(it.handshakes)
//                        }

                        tanggalHandshakeAdapter = TanggalHandshakeAdapter(it.handshakes.reversed(), this@HandshakeActivity)
//                        val newlist = it.handshakes.reversed()
                        binding.rvTanggal.apply {
                            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            adapter = tanggalHandshakeAdapter
                        }



                        parentData = it.handshakes[it.handshakes.size - 1]
                        load()
                        setDataParentHS()

                        tanggalHandshakeAdapter.active_id = parentData.id_handshake
                        tanggalHandshakeAdapter.notifyDataSetChanged()

                    }else{
                        binding.divKosong.visibility = View.VISIBLE

                        binding.tvRiwayat.visibility = View.VISIBLE
                        binding.tvReload.visibility = View.GONE
                    }
                }
            }
        })
    }

    override fun handshakeShow(data: ParentHandshake) {
        //show detail
        active_id = data.id_handshake
        tanggalHandshakeAdapter.active_id = active_id

        parentData = data
        load()
        setDataParentHS()

        tanggalHandshakeAdapter.notifyDataSetChanged()
    }


    private fun setDataParentHS(){
        binding.tvNama.text = parentData.nama_event_handshake
        binding.tvTanggal.text = parentData.tanggal_envent_handshake
        binding.tvMember.text = "${parentData.nama_member.size} Member"
        binding.tvSesi.text = "${parentData.sesis.size} Sesi"
        binding.tvTiket.text = "${parentData.total_sold}/${parentData.total_jadwal} Sold"
    }

    private fun load(){
        listDetail.clear()

        for (data in parentData.details){
            if (alreadySesi(data.sesi)){
                addDataToSesi(data)
            }else{
                //memuat item baru
                val detailHandshake = DetailHandshake()
                detailHandshake.sesi = data.sesi
                detailHandshake.waktu = data.waktu
                detailHandshake.standby = data.standby
                detailHandshake.member_list_name = data.nama_member
                detailHandshake.handshake.add(data)
                listDetail.add(detailHandshake)
            }
        }

        binding.rvData.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = DetailHandshakeAdapter(listDetail)
        }

    }

    private fun addDataToSesi(handshake: Handshake){
        Log.e("HS", "id ${handshake.id_member}")
        for (i in listDetail.indices){
            if (listDetail[i].sesi == handshake.sesi){
                listDetail[i].handshake.add(handshake)
                listDetail[i].member_list_name += ", ${handshake.nama_member}"
            }
        }
    }

    private fun alreadySesi(sesi:String):Boolean{
        for (detail in listDetail){
            if (detail.sesi == sesi){
                return true
            }
        }

        return false
    }
}