package com.arira.ngidol48.ui.member

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.MemberAdapter
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

        setToolbar(getString(R.string.teks_member), binding.toolbar)

        observerData()

        viewModel.hitAll()
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