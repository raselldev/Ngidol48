package com.arira.ngidol48.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.SongAdapter
import com.arira.ngidol48.databinding.FragmentLaguBinding
import com.arira.ngidol48.helper.BaseFragment
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.cariLagu.CariLaguCallback

class LaguFragment(val songList: List<Song>,val callback:CariLaguCallback) : BaseFragment() {
    private lateinit var binding: FragmentLaguBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lagu, container, false)

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        if(songList.isNotEmpty()){
            binding.divKosong.visibility = View.GONE
            binding.rvData.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = SongAdapter(songList)
            }
        }else{
            binding.divKosong.visibility = View.VISIBLE
            callback.callReload()
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            callback.callReload()
        }

        return binding.root
    }
}