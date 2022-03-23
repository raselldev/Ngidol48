package com.arira.ngidol48.ui.allBlog

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.BlogAdapter
import com.arira.ngidol48.adapter.EventAdapter
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.ActivityBlogBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.model.Blog
import com.arira.ngidol48.ui.addBlog.TambahBlogActivity
import com.arira.ngidol48.ui.login.LoginActivity
import com.arira.ngidol48.utilities.Go

class BlogActivity : BaseActivity() {

    private lateinit var viewModel: AllblogViewModel
    private lateinit var binding: ActivityBlogBinding

    private var indexLoad = 1
    private val viewThreshold = 18
    private var pastVisibleitem: Int = 0
    private var visibleItemCount:Int = 0
    private var totalItemCount:Int = 0
    private var previousTotal = 0
    private var isLoading = true
    private lateinit var blogLayoutManager: LinearLayoutManager
    private lateinit var blogAdapter: BlogAdapter
    private var listBlog:ArrayList<Blog> = ArrayList()
    private var totalPage:Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blog)
        setToolbar(getString(R.string.teks_blog), binding.toolbar)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AllblogViewModel::class.java]
        viewModel.context = this

        /** setup recyc*/
        blogLayoutManager = LinearLayoutManager(this)
        blogAdapter = BlogAdapter(listBlog)
        binding.rvData.apply {
            layoutManager = blogLayoutManager
            adapter = blogAdapter
        }

        defaultIndex()

        setupPageination()
        observerData()
        viewModel.hitAll(indexLoad)

        action()
        observerData()
    }



    fun action(){

        binding.fabTambah.setOnClickListener {
            if(pref.getIsLogin()){
                Go(this).move(TambahBlogActivity::class.java)
            }else{
                Go(this).move(LoginActivity::class.java)
            }

        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            defaultIndex()
            viewModel.hitAll(indexLoad)
        }
    }

    fun observerData(){
        viewModel.getLoading().observe(this) {
            it.let {
                if (it) {
                    binding.progress.visibility = View.VISIBLE
                    if (indexLoad == 1){
                        binding.shimmer.visibility = View.VISIBLE
                        binding.shimmer.startShimmer()
                    }

                } else {
                    binding.progress.visibility = View.GONE
                    if (indexLoad == 1){
                        binding.shimmer.visibility = View.GONE
                        binding.shimmer.stopShimmer()
                    }
                }
            }
        }

        viewModel.getError().observe(this) {
            it.let {
                if (it != null) {
                }
            }
        }

        viewModel.getResponse().observe(this) {
            it.let {
                if (it != null) {
                    totalPage = it.totalpage
                    if (it.blogs.isNotEmpty()) {
                        listBlog.addAll(it.blogs)
                        blogAdapter.notifyDataSetChanged()
                        isLoading = false
                    }
                }
            }
        }
    }

    private fun defaultIndex() {
        blogLayoutManager.removeAllViews()

        pastVisibleitem = 0
        visibleItemCount = 0
        totalItemCount = 0
        previousTotal = 0
        indexLoad = 1
        listBlog.clear()

    }

    private fun setupPageination() {
        binding.rvData.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val countItem = blogLayoutManager.itemCount
                val lastVisiblePosition = blogLayoutManager.findLastCompletelyVisibleItemPosition()
                val isLastPosition = countItem.minus(1) == lastVisiblePosition
                if (!isLoading && isLastPosition && indexLoad < totalPage) {
                    isLoading = true
                    indexLoad += 1
                    viewModel.hitAll(indexLoad)
                }
            }
        })
    }
}