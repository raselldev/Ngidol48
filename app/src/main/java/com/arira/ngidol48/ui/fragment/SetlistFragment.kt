package com.arira.ngidol48.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.SetlistAdapter
import com.arira.ngidol48.adapter.SongAdapter
import com.arira.ngidol48.databinding.FragmentLaguBinding
import com.arira.ngidol48.databinding.FragmentSetlistBinding
import com.arira.ngidol48.helper.BaseFragment
import com.arira.ngidol48.model.Setlist
import com.arira.ngidol48.model.Song
import com.arira.ngidol48.ui.cariLagu.CariLaguCallback

class SetlistFragment(val setlist: List<Setlist>, val callback:CariLaguCallback) : BaseFragment() {
    private lateinit var binding: FragmentSetlistBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setlist, container, false)

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        if(setlist.isNotEmpty()){
            binding.divKosong.visibility = View.GONE
            binding.rvData.apply {
                layoutManager = GridLayoutManager(context, 2)
                adapter = SetlistAdapter(setlist)
            }
        }else{
            binding.divKosong.visibility = View.VISIBLE
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            callback.callReload()
        }

        return binding.root
    }
}