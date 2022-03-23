package com.arira.ngidol48.ui.detailBlog

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.arira.ngidol48.R
import com.arira.ngidol48.app.App
import com.arira.ngidol48.app.App.Companion.helper
import com.arira.ngidol48.databinding.ActivityDetailBlogBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.helper.Helper
import com.arira.ngidol48.model.Blog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

class DetailBlogActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBlogBinding
    private var blog:Blog = Blog()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_blog)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_blog)
        setToolbar(getString(R.string.teks_blog), binding.toolbar)
        
        blog = intent.getParcelableExtra(extra_model) ?: Blog()
        setDataBlog()
    }
    
    private fun setDataBlog(){
        if (!blog.cover.contains("default")){
            binding.ivCover.visibility = View.VISIBLE
            Glide.with(this)
                .asBitmap()
                .load(Config.BASE_STORAGE_IMAGE + blog.cover)
                .placeholder(R.drawable.img_logo)
                .error(R.drawable.img_logo)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                    ) {
                        binding.ivCover.setImageBitmap(resource)
                        binding.ivCover.setBackgroundColor(Helper.getDominantColor(resource))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })
        }else{
            binding.ivCover.visibility = View.GONE
        }

        binding.tvJudul.text = blog.judul
        binding.tvNamaPengguna.text = blog.fullname
        binding.tvTanggal.text = helper.convert(blog.created_at, "yyyy-MM-dd HH:mm:ss", "HH:mm dd MMMM yyyy")
        binding.tvTotalKomentar.text = "${blog.total_command} Komentar"
        helper.setTags(binding.tvBlog1, helper.fromHtml(blog.blog).toString(), this)
    }

}