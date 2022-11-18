package com.arira.ngidol48.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.BeritaAdapter
import com.arira.ngidol48.databinding.FragmentInformasiBinding
import com.arira.ngidol48.ui.activity.news.BeritaViewModel

class InformasiFragment : Fragment() {

    private lateinit var binding: FragmentInformasiBinding
    private lateinit var viewModel: BeritaViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_informasi, container, false)

        viewModel =  ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BeritaViewModel::class.java]
        viewModel.context = requireContext()

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

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            viewModel.hitAll()
        }

        binding.tvReload.setOnClickListener {
            viewModel.hitAll()
        }
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

        viewModel.getError().observe(requireActivity()) {
            it.let {
                if (it != null) {
                    binding.divKosong.visibility = View.VISIBLE

                }
            }
        }

        viewModel.getResponse().observe(requireActivity()) {
            it.let {
                if (it != null) {
                    if (it.news.isNotEmpty()) {
                        binding.rvData.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = BeritaAdapter(it.news)
                        }
                    } else {
                        binding.divKosong.visibility = View.VISIBLE
                    }
                }
            }
        }
    }


}