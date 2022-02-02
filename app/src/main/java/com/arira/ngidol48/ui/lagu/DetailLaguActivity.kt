package com.arira.ngidol48.ui.lagu

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ActivityDetailLaguBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.model.Song

class DetailLaguActivity : BaseActivity() {
    private lateinit var binding:ActivityDetailLaguBinding
    private var lagu:Song = Song()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_lagu)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_lagu)
        setToolbar(getString(R.string.teks_lagu), binding.toolbar)

        lagu = intent.getParcelableExtra(extra_model) ?: Song()

        binding.tvJudul.text = lagu.judul
        binding.tvLirik.text = lagu.lirik
        binding.tvSetlist.text = lagu.nama
    }
}