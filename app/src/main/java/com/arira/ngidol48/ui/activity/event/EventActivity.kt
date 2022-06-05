package com.arira.ngidol48.ui.activity.event

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.EventAdapter
import com.arira.ngidol48.databinding.ActivityEventBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.model.Event
import com.arira.ngidol48.ui.activity.newKalender.KalenderActivity
import com.arira.ngidol48.utilities.Go

class EventActivity : BaseActivity() {
    private lateinit var binding: ActivityEventBinding
    private lateinit var viewModel: EventViewModel
    private lateinit var adapterEvent:EventAdapter
    private var listEvent:ArrayList<Event> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_event)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[EventViewModel::class.java]
        viewModel.context = this

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

        setToolbar(getString(R.string.teks_kalender), binding.toolbar)
        observerData()
        viewModel.hitAll()

        action()
    }

    fun action(){
        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitAll()
        }

        binding.tvReload.setOnClickListener {
            viewModel.hitAll()
        }

        binding.linNewCalender.setOnClickListener {
            Go(this).move(KalenderActivity::class.java)
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
        viewModel.getLoading().observe(this) {
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