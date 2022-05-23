package com.arira.ngidol48.ui.activity.mng

import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.DetailMngAdapter
import com.arira.ngidol48.databinding.ActivityDetailMngBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.DetailMng
import com.arira.ngidol48.model.Mng
import com.arira.ngidol48.model.ParentMng
import com.arira.ngidol48.ui.activity.myWeb.MyWebActivity
import com.arira.ngidol48.utilities.Go

class DetailMngActivity : BaseActivity() {
    lateinit var binding:ActivityDetailMngBinding
    private var parentData = ParentMng()
    private var listDetail:ArrayList<DetailMng> = ArrayList()
    private var buy_url:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_mng)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_mng)
        setToolbar(getString(R.string.teks_mng_vc), binding.toolbar)
        parentData = intent.getParcelableExtra(extra_model) ?: ParentMng()

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        load()

        binding.rvData.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = DetailMngAdapter(listDetail)
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
            buy_url = "https://jkt48.com/twoshot?id=${parentData.id_mng}"
            Go(this).move(MyWebActivity::class.java, url = buy_url)
        }
    }

    private fun setDataParentHS(){
        binding.tvNama.text = parentData.nama_event_mng
        binding.tvTanggal.text = parentData.tanggal_envent_mng
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
                val detailMng = DetailMng()
                detailMng.sesi = data.sesi
                detailMng.waktu = data.waktu
                detailMng.penukaran = data.penukaran
                detailMng.member_list_name = data.nama_member
                detailMng.mng.add(data)
                listDetail.add(detailMng)
            }
        }
    }

    private fun addDataToSesi(Mng:Mng){
        Log.e("MNG", "id ${Mng.id_member}")
        for (i in listDetail.indices){
            if (listDetail[i].sesi == Mng.sesi){
                listDetail[i].mng.add(Mng)
                listDetail[i].member_list_name += ", ${Mng.nama_member}"
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