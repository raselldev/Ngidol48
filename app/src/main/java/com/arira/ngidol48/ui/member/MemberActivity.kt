package com.arira.ngidol48.ui.member

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.MemberAdapter
import com.arira.ngidol48.adapter.ShowroomAdapter
import com.arira.ngidol48.databinding.ActivityMemberBinding
import com.arira.ngidol48.databinding.SheetDetailMemberBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.model.Member
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog

class MemberActivity : BaseActivity(), MemberCallback {
    private lateinit var viewModel: MemberViewModel
    private lateinit var binding: ActivityMemberBinding

    override fun onSelect(member: Member) {
        showSheetMember(member)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_member)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MemberViewModel::class.java]
        viewModel.context = this

        /*menambakan warna untuk swipe refresh*/
        binding.swipe.setColorSchemeResources(R.color.colorPrimaryTeks,
            R.color.colorPrimary,
            R.color.colorPrimaryDark,
            R.color.colorAccent)

        setToolbar(getString(R.string.teks_member), binding.toolbar)

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
                            layoutManager  = GridLayoutManager(context, 3)
                            adapter = MemberAdapter(it.members, this@MemberActivity)
                        }
                    }else{
                        binding.divKosong.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
}