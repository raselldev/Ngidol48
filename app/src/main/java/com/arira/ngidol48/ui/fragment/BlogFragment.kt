package com.arira.ngidol48.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arira.ngidol48.R
import com.arira.ngidol48.adapter.BlogAdapter
import com.arira.ngidol48.adapter.BlogKategoriAdapter
import com.arira.ngidol48.app.App
import com.arira.ngidol48.databinding.FragmentBlogBinding
import com.arira.ngidol48.databinding.SheetBlogKategoriBinding
import com.arira.ngidol48.helper.BaseFragment
import com.arira.ngidol48.model.Blog
import com.arira.ngidol48.model.BlogKategori
import com.arira.ngidol48.ui.activity.addBlog.BlogKategoriCallback
import com.arira.ngidol48.ui.activity.addBlog.TambahBlogActivity
import com.arira.ngidol48.ui.activity.allBlog.AllblogViewModel
import com.arira.ngidol48.ui.activity.allBlog.BlogKategoriViewModel
import com.arira.ngidol48.ui.activity.login.LoginActivity
import com.arira.ngidol48.utilities.Go
import com.google.android.material.bottomsheet.BottomSheetDialog

class BlogFragment : BaseFragment(), BlogKategoriCallback {
    private lateinit var binding: FragmentBlogBinding
    private lateinit var viewModel: AllblogViewModel

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blog, container, false)


        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AllblogViewModel::class.java]
        viewModel.context = requireActivity()

        adapterKategori = BlogKategoriAdapter(listKategori, this)

        kategoriViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[BlogKategoriViewModel::class.java]
        kategoriViewModel.context = requireActivity()

        /** setup recyc*/
        blogLayoutManager = LinearLayoutManager(requireActivity())
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

        return binding.root
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
            if(App.pref.getIsLogin()){
                Go(requireActivity()).move(TambahBlogActivity::class.java)
            }else{
                Go(requireActivity()).move(LoginActivity::class.java)
            }

        }

        binding.swipe.setOnRefreshListener {
            binding.swipe.isRefreshing = false
            defaultIndex()
            viewModel.hitAll(indexLoad, kategoriSelected.id.toInt(), querySearch)
        }
    }

    fun observerData(){
        viewModel.getLoading().observe(requireActivity()) {
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

        viewModel.getError().observe(requireActivity()) {
            it.let {
                if (it != null) {
                    toast.show(it, requireActivity())
                }
            }
        }

        viewModel.getResponse().observe(requireActivity()) {
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

        bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme)

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
        kategoriViewModel.getLoading().observe(requireActivity()) {
            it.let {
                if (it) {
                    bindingSheetKategori.divData.visibility = View.GONE
                    bindingSheetKategori.divLoading.visibility = View.VISIBLE
                } else {
                    bindingSheetKategori.divData.visibility = View.VISIBLE
                    bindingSheetKategori.divLoading.visibility = View.GONE
                }
            }
        }

        kategoriViewModel.getResponse().observe(requireActivity()) {
            it.let {
                if (it != null) {
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
        }

        kategoriViewModel.getError().observe(requireActivity()) {
            it.let {
                if (it != null) {
                    toast.show(it, requireActivity())
                }

            }
        }
        /*end of get data viewmodel*/

    }

}