package com.arira.ngidol48.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.EventAdapter
import com.arira.ngidol48.databinding.FragmentJadwalBinding
import com.arira.ngidol48.helper.BaseFragment
import com.arira.ngidol48.model.Event
import com.arira.ngidol48.ui.activity.event.EventViewModel
import com.arira.ngidol48.ui.activity.newKalender.KalenderActivity
import com.arira.ngidol48.utilities.Go

class JadwalFragment : BaseFragment() {
    private lateinit var binding: FragmentJadwalBinding
    private lateinit var viewModel: EventViewModel
    private lateinit var adapterEvent: EventAdapter
    private var listEvent:ArrayList<Event> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jadwal, container, false)


        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EventViewModel::class.java]
        viewModel.context = requireActivity()

        adapterEvent = EventAdapter(listEvent)

        binding.rvData.apply {
            layoutManager  = GridLayoutManager(context, 2)
            adapter = adapterEvent
        }

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        observerData()
        viewModel.hitAll()

        action()

        return binding.root
    }

    fun action(){

        binding.linNewCalender.setOnClickListener {
            Go(requireActivity()).move(KalenderActivity::class.java)
        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitAll()
        }

        binding.tvReload.setOnClickListener {
            viewModel.hitAll()
        }

        binding.svPencarian.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                adapterEvent.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
//                adapterEvent.filter.filter(query)
                return false
            }

        })
    }

    fun observerData(){
        viewModel.getLoading().observe(requireActivity()) {
            it.let {
                if (it) {
                    binding.divKosong.visibility = View.GONE
                    binding.shimmer.visibility = View.VISIBLE
                    binding.shimmer.startShimmer()
                } else {
                    binding.shimmer.visibility = View.GONE
                    binding.shimmer.stopShimmer()
                }
            }
        }

        viewModel.getError().observe(requireActivity(), Observer {
            it.let {
                if (it != null){
                    binding.divKosong.visibility = View.VISIBLE

                }
            }
        })

        viewModel.getResponse().observe(requireActivity(), Observer {
            it.let {
                if (it != null) {
                    if (it.events.isNotEmpty()){
                        listEvent.clear()
                        listEvent.addAll(it.events)
                        adapterEvent.notifyDataSetChanged()

                    }else{
                        binding.divKosong.visibility = View.VISIBLE
                    }
                }
            }
        })
    }


}