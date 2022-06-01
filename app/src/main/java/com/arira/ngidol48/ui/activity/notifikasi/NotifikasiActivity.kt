package com.arira.ngidol48.ui.activity.notifikasi

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.KategoriNotifikasiAdapter
import com.arira.ngidol48.adapter.NotifikasiAdapter
import com.arira.ngidol48.databinding.ActivityNotifikasiBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.model.Notifikasi

class NotifikasiActivity : BaseActivity(), NotificationCallback {
    private lateinit var viewModel: NotifikasiViewModel
    private lateinit var binding: ActivityNotifikasiBinding
    private var listAllNotification:ArrayList<Notifikasi> = ArrayList()
    private var listShowNotification:ArrayList<Notifikasi> = ArrayList()
    private var listkategoriNotifikasi:ArrayList<String> = ArrayList()
    private var selectedKategori:String = ""
    private lateinit var adapterNotifikasi: NotifikasiAdapter
    private lateinit var adapterKategoriNotifikasi: KategoriNotifikasiAdapter


    override fun onCategorySelected(name: String) {
        selectedKategori = name
        adapterKategoriNotifikasi.selectedCategori = selectedKategori
        adapterKategoriNotifikasi.notifyDataSetChanged()

        loadNotification()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifikasi)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notifikasi)
        setToolbar(getString(R.string.notifikasi), binding.toolbar)

        adapterNotifikasi = NotifikasiAdapter(listShowNotification)
        binding.rvData.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterNotifikasi
        }

        adapterKategoriNotifikasi = KategoriNotifikasiAdapter(listkategoriNotifikasi, this)
        binding.rvKategori.apply {
            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            }else{
                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }

            adapter = adapterKategoriNotifikasi
        }

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[NotifikasiViewModel::class.java]
        viewModel.context = this

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        observerData()

        viewModel.hitAll()

        action()
    }

    fun loadNotification(){
        listShowNotification.clear()
        if (selectedKategori != getString(R.string.teks_semua_kategori)){
            for (notifikasi in listAllNotification){
                if (notifikasi.type == selectedKategori){
                    listShowNotification.add(notifikasi)
                }
            }
        }else{
            listShowNotification.addAll(listAllNotification)
        }

        adapterNotifikasi.notifyDataSetChanged()
    }

    fun action(){

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitAll()
        }

        binding.tvReload.setOnClickListener {
            viewModel.hitAll()
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
                    binding.divKosong.visibility = View.VISIBLE

                }
            }
        })

        viewModel.getResponse().observe(this, Observer {
            it.let {
                if (it != null) {
                    if (it.notifikasi.isNotEmpty()){
                        listAllNotification.clear()
                        listAllNotification.addAll(it.notifikasi)
//                        listShowNotification.addAll(it.notifikasi)

                        //load kategori
                        listkategoriNotifikasi.clear()
                        listkategoriNotifikasi.add(getString(R.string.teks_semua_kategori))
                        listkategoriNotifikasi.addAll(it.kategori)
                        adapterKategoriNotifikasi.selectedCategori = getString(R.string.teks_semua_kategori)
                        selectedKategori = getString(R.string.teks_semua_kategori)
                        adapterKategoriNotifikasi.notifyDataSetChanged()

                        //reload notifikasi
                        loadNotification()
                    }else{
                        binding.divKosong.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

}