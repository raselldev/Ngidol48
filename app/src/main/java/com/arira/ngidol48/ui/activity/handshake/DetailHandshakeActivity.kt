package com.arira.ngidol48.ui.activity.handshake

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.DetailHandshakeAdapter
import com.arira.ngidol48.databinding.ActivityDetailHandshakeBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.DetailHandshake
import com.arira.ngidol48.model.Handshake
import com.arira.ngidol48.model.ParentHandshake
import com.arira.ngidol48.ui.activity.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go

class DetailHandshakeActivity : BaseActivity() {
    lateinit var binding:ActivityDetailHandshakeBinding
    private var parentData = ParentHandshake()
    private var listDetail:ArrayList<DetailHandshake> = ArrayList()
    private var buy_url:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_handshake)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_handshake)
        setToolbar(getString(R.string.teks_handshake_vc), binding.toolbar)
        parentData = intent.getParcelableExtra(extra_model) ?: ParentHandshake()

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        load()

        binding.rvData.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = DetailHandshakeAdapter(listDetail)
        }

        setDataParentHS()

        action()
    }

    private fun action(){
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            load()
        }

        binding.tvTiketDll.setOnClickListener {
            buy_url = "https://jkt48.com/handshake?id=${parentData.id_handshake}"
            Go(this).move(MyWebActivity::class.java, url = buy_url)
        }
    }

    private fun setDataParentHS(){
        binding.tvNama.text = parentData.nama_event_handshake
        binding.tvTanggal.text = parentData.tanggal_envent_handshake
        binding.tvMember.text = "${parentData.nama_member.size} Member"
        binding.tvSesi.text = "${parentData.sesis.size} Sesi"
        binding.tvTiket.text = "${parentData.total_sold}/${parentData.total_jadwal} Sold"
    }

    private fun load(){
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
    }

    private fun addDataToSesi(handshake:Handshake){
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