package com.arira.ngidol48.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.MemberAdapter
import com.arira.ngidol48.adapter.ShowroomAdapter
import com.arira.ngidol48.databinding.FragmentMemberBinding
import com.arira.ngidol48.helper.BaseFragment
import com.arira.ngidol48.model.Member
import com.arira.ngidol48.ui.activity.member.MemberCallback
import com.arira.ngidol48.ui.activity.member.MemberViewModel

class MemberFragment : BaseFragment(), MemberCallback {

    private lateinit var binding: FragmentMemberBinding
    private lateinit var viewModel: MemberViewModel


    override fun onSelect(member: Member) {
        showSheetMember(member)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_member, container, false)

        viewModel =  ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MemberViewModel::class.java]
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
        viewModel.getLoading().observe(requireActivity(), Observer {
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
                    if(it.live_showroom.isEmpty()){
                        binding.linViewKosongShowroom.visibility = View.VISIBLE
                    }else{
                        binding.tvJmlMember.text = "( ${it.live_showroom.size} Member )"
                        binding.linViewKosongShowroom.visibility = View.GONE
                        binding.rvShowroom.apply {
                            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            adapter = ShowroomAdapter(it.live_showroom)
                        }
                    }

                    if (it.members.isNotEmpty()){
                        binding.rvData.apply {
                            layoutManager = if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                                GridLayoutManager(context, 5)
                            }else{
                                GridLayoutManager(context, 4)
                            }
                            adapter = MemberAdapter(it.members, this@MemberFragment, true)
                        }
                    }else{
                        binding.divKosong.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

}