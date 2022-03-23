package com.arira.ngidol48.ui.addBlog

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.arira.ngidol48.R
import com.arira.ngidol48.databinding.ActivityTambahBlogBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.SweetAlert
import com.arira.ngidol48.utilities.Picker
import com.bumptech.glide.Glide
import net.alhazmy13.mediapicker.Image.ImagePicker
import java.io.File

class TambahBlogActivity : BaseActivity() {
    private lateinit var binding:ActivityTambahBlogBinding
    private lateinit var viewModel: AddBlogViewModel
    /*status*/
    private var isBold:Boolean = false
    private var isItalic:Boolean = false
    private var isStrike:Boolean = false
    private var isUnderline:Boolean = false
    private var isAlignRight:Boolean = false
    private var isAlignCenter:Boolean = false
    private var isAlignLeft:Boolean = false
    private var textDescription:String = ""

    private var coverFile: File? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_blog)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tambah_blog)
        setToolbar(getString(R.string.teks_tulis_blog), binding.toolbar)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[AddBlogViewModel::class.java]
        viewModel.context = this

        statusPutih()


        action()
        observeBlog()
    }

    fun action(){
//        binding.edtIsi.setOnChan
        binding.edtIsi.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null){
                    val wordCount =   s.split(" ").toTypedArray().size
                    binding.tBagianTvJmlKata.text = "${wordCount} Kata"
                }

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.ivDeleteCover.setOnClickListener {
            binding.tBagianIvCover.setImageResource(R.drawable.img_upload_new)
            coverFile = null
            binding.ivDeleteCover.visibility = View.GONE
        }

        binding.tBagianIvCover.setOnClickListener {
            Picker(this).ambilGambarSemua(type = "blog")
        }

        binding.btnPublish.setOnClickListener {
            val judul = binding.edtJudul.text.toString()
            textDescription = binding.edtIsi.text.toString()

            textDescription = textDescription.replace("width=\"200\"><br>","width=\"200\"/><br>")
            textDescription = textDescription.replace("width=\"200\"></div>","width=\"200\"/></div>")
            textDescription = textDescription.replace("<br>","<br></br>")

            if (judul.isEmpty()){
                toast.show("Judul tidak boleh kosong", this)
            }else if (textDescription.isEmpty()){
                toast.show("konten blog tidak boleh kosong", this)
            }else{
                if (coverFile != null){
                    viewModel.add(coverFile!!, judul, textDescription)
                }else{
                    viewModel.add(judul, textDescription)
                }
            }

        }
    }

    fun observeBlog(){
        viewModel.getLoading().observe(this){
            it.let {
                if (it != null){
                    if (it){
                        SweetAlert.onLoading(this)
                    }else{
                        SweetAlert.dismis()
                    }
                }
            }
        }

        viewModel.getResponse().observe(this){
            it.let {
                if (it != null){
                    SweetAlert.dismis()
                    if (it.code == 1){
                        SweetAlert.success(this, getString(R.string.teks_blog), getString(R.string.teks_blog_tersimpan), finish = true)
                    }else{
                        SweetAlert.onFailure(this, it.message)
                    }
                }
            }
        }

        viewModel.getError().observe(this){
            it.let {
                if (it != null){
                    SweetAlert.onFailure(this, it)
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImagePicker.IMAGE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            val mpath: java.util.ArrayList<String> = data!!.getStringArrayListExtra(ImagePicker.EXTRA_IMAGE_PATH)!!
            coverFile = File(mpath[0])

            Glide.with(this)
                .load(coverFile)
                .placeholder(R.drawable.img_upload_new)
                .error(R.drawable.img_upload_new)
                .into(binding.tBagianIvCover)

            binding.ivDeleteCover.visibility = View.VISIBLE

        }
    }
}