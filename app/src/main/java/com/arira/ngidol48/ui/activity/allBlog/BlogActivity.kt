package com.arira.ngidol48.ui.activity.allBlog

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.BlogAdapter
import com.arira.ngidol48.adapter.BlogKategoriAdapter
import com.arira.ngidol48.app.App.Companion.pref
import com.arira.ngidol48.databinding.ActivityBlogBinding
import com.arira.ngidol48.databinding.SheetBlogKategoriBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.model.Blog
import com.arira.ngidol48.model.BlogKategori
import com.arira.ngidol48.ui.activity.addBlog.BlogKategoriCallback
import com.arira.ngidol48.ui.activity.addBlog.TambahBlogActivity
import com.arira.ngidol48.ui.activity.login.LoginActivity
import com.arira.ngidol48.utilities.Go
import com.google.android.material.bottomsheet.BottomSheetDialog

class BlogActivity : BaseActivity(), BlogKategoriCallback {

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

    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bindingSheetKategori: SheetBlogKategoriBinding
    private var listKategori:ArrayList<BlogKategori> = ArrayList()
    private lateinit var adapterKategori: BlogKategoriAdapter
    private lateinit var kategoriViewModel: BlogKategoriViewModel
    private var querySearch:String = ""
    private var kategoriSelected: BlogKategori = BlogKategori()

    override fun onKategoriSelected(data: BlogKategori) {
        kategoriSelected = data

        //update kategori update
        adapterKategori.selected = data

        //update ui
        if(data.id == "0"){
            binding.ivFilter.setImageResource(R.drawable.ic_baseline_filter_alt_24)
        }else{
            binding.ivFilter.setImageResource(R.drawable.ic_baseline_filter_alt_24_primary)
        }

        //reload data
        defaultIndex()
        viewModel.hitAll(indexLoad, kategoriSelected.id.toInt(), querySearch)


        bottomSheetDialog.dismiss()

        //update adapter
        adapterKategori.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blog)
        setNewToolbar(getString(R.string.teks_blog), binding.toolbar)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AllblogViewModel::class.java]
        viewModel.context = this

        adapterKategori = BlogKategoriAdapter(listKategori, this)

        kategoriViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BlogKategoriViewModel::class.java]
        kategoriViewModel.context = this

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
        viewModel.hitAll(indexLoad, kategoriSelected.id.toInt(), querySearch)

        action()

        observeDataKategori()
    }



    fun action(){


        binding.svPencarian.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                if (querySearch.isNotEmpty()){
                    if (newText.isEmpty()){
                        querySearch = ""
                        defaultIndex()
                        viewModel.hitAll(indexLoad, kategoriSelected.id.toInt(), querySearch)
                    }
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                querySearch = query
                defaultIndex()
                viewModel.hitAll(indexLoad, kategoriSelected.id.toInt(), querySearch)
                return false
            }

        })

        binding.ivFilter.setOnClickListener {
            showSheetKategori()
        }

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
            viewModel.hitAll(indexLoad, kategoriSelected.id.toInt(), querySearch)
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
                    toast.show(it, this)
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
                    }

                    isLoading = false
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
                    viewModel.hitAll(indexLoad, kategoriSelected.id.toInt(), querySearch)
                }
            }
        })
    }

    private fun showSheetKategori() {

        bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)

        bindingSheetKategori = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.sheet_blog_kategori,
            null,
            false
        )

        bottomSheetDialog.setContentView(bindingSheetKategori.root)


        if (listKategori.isEmpty()) {
            kategoriViewModel.hitAll()
            bottomSheetDialog.setCancelable(false)

        } else {
            bottomSheetDialog.setCancelable(true)

            bindingSheetKategori.divData.visibility = View.VISIBLE
            bindingSheetKategori.divLoading.visibility = View.GONE
        }

        bindingSheetKategori.rvKategori.apply {
            adapter = adapterKategori
            layoutManager = LinearLayoutManager(context)
        }

        bindingSheetKategori.svKota.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                adapterKategori.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                adapterKategori.filter.filter(query)
                return false
            }

        })

        bottomSheetDialog.setOnCancelListener {
            adapterKategori.filter.filter("")
        }

        bottomSheetDialog.show()

    }

    private fun observeDataKategori() {

        /*get data on viewmodel*/
        kategoriViewModel.getLoading().observe(this, Observer {
            it.let {
                if (it) {
                    bindingSheetKategori.divData.visibility = View.GONE
                    bindingSheetKategori.divLoading.visibility = View.VISIBLE
                } else {
                    bindingSheetKategori.divData.visibility = View.VISIBLE
                    bindingSheetKategori.divLoading.visibility = View.GONE
                }
            }
        })

        kategoriViewModel.getResponse().observe(this, Observer {
            it.let {
                if(it != null){
                    if (it.data.isNotEmpty()) {
                        bottomSheetDialog.setCancelable(true)
                        listKategori.clear()

                        //add default
                        val default = BlogKategori()
                        default.id = "0"
                        default.nama = getString(R.string.teks_semua_kategori)
                        listKategori.add(default)

                        listKategori.addAll(it.data)

                        adapterKategori.notifyDataSetChanged()
                    }
                }


            }
        })

        kategoriViewModel.getError().observe(this, Observer {
            it.let {
                if (it != null) {
                    toast.show(it, this)
                }

            }
        })
        /*end of get data viewmodel*/

    }

}