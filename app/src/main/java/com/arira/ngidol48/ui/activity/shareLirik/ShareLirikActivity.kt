package com.arira.ngidol48.ui.activity.shareLirik

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.arira.ngidol48.databinding.ActivityShareLirikBinding
import com.arira.ngidol48.helper.*
import com.arira.ngidol48.helper.Config.extra_model
import com.arira.ngidol48.helper.Config.extra_other
import com.arira.ngidol48.model.Song
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream

class ShareLirikActivity : BaseActivity() {
    private lateinit var binding: ActivityShareLirikBinding
    private var song = Song()
    var mDefaultColorBackground:Int = 0
    var mDefaultColorText:Int = 0
    var selectedSong = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Validasi().ijinDokumen(this)

        binding = ActivityShareLirikBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar("Share Lirik", binding.toolbar)
        song = intent.getParcelableExtra(extra_model) ?: Song()
        selectedSong = intent.getStringExtra(extra_other) ?: ""

        binding.tvLirik.text = selectedSong
        setupSong()
        action()
    }

    fun action(){
        binding.ivEditBg.setOnClickListener {
            val colorPickerPopup = ColorPickerPopup.Builder(this)
            colorPickerPopup.initialColor(mDefaultColorBackground)
            colorPickerPopup.enableAlpha(false)
            colorPickerPopup.enableBrightness(true)
            colorPickerPopup.okTitle("Gunakan")
            colorPickerPopup.cancelTitle("Batal")
            colorPickerPopup.showIndicator(true)
            colorPickerPopup.showValue(false)
            colorPickerPopup.build().show(object : ColorPickerObserver(){
                override fun onColorPicked(color: Int) {
                    mDefaultColorBackground = color
                    binding.viewToShare.setBackgroundColor(mDefaultColorBackground)
                    binding.viewToParent.setBackgroundColor(mDefaultColorBackground)
                    binding.cardShare.setCardBackgroundColor(mDefaultColorBackground)
                }

            })
        }

        binding.ivEditText.setOnClickListener {
            val colorPickerPopup = ColorPickerPopup.Builder(this)
            colorPickerPopup.initialColor(mDefaultColorText)
            colorPickerPopup.enableAlpha(false)
            colorPickerPopup.enableBrightness(true)
            colorPickerPopup.okTitle("Gunakan")
            colorPickerPopup.cancelTitle("Batal")
            colorPickerPopup.showIndicator(true)
             colorPickerPopup.showValue(false)
            colorPickerPopup.build().show(object : ColorPickerObserver(){
                override fun onColorPicked(color: Int) {
                    mDefaultColorText = color
                    binding.tvLirik.setTextColor(mDefaultColorText)
                    binding.tvTitle.setTextColor(mDefaultColorText)
                    binding.tvSubtitle.setTextColor(mDefaultColorText)
                }

            })
        }

        binding.tvShare.setOnClickListener {
            if (Validasi().ijinDokumen(this)){
                binding.cardShare.setLayerType(View.LAYER_TYPE_SOFTWARE, null) // This is needed to enable software rendering for clipping

                val bitmap = Bitmap.createBitmap(binding.cardShare.width, binding.cardShare.height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                val path = Path()
                path.addRoundRect(RectF(0f, 0f, binding.cardShare.width.toFloat(), binding.cardShare.height.toFloat()), binding.cardShare.radius, binding.cardShare.radius, Path.Direction.CW)
                canvas.clipPath(path)

                binding.cardShare.draw(canvas)

                try{
                    saveMediaToStorage(bitmap, "share_lyrics")
                }catch (e: FileNotFoundException){
                    toast.show("Gagal mengunduh berkas, pastikan anda memberi ijin berkas aplikasi", this)
                }
            }else{
                SweetAlert.onFailure(this, "Aplikasi tidak memiliki ijin untuk membuat gambar")
            }
        }
    }

    fun setupSong() {
        binding.tvTitle.text = song.judul
        binding.tvSubtitle.text = song.nama
        Glide.with(this)
            .asBitmap()
            .load(Config.BASE_STORAGE + song.cover)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(24)))
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    binding.ivThumb.setImageBitmap(resource)
                    binding.ivThumb.setBackgroundColor(Helper.getDominantColor(resource))
//                    binding.cardShare.setCardBackgroundColor(Helper.getDominantColor(resource))
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    // this method saves the image to gallery
    private fun saveMediaToStorage(bitmap: Bitmap, prefix:String) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}_${prefix}.png"
        var imagesUri:ArrayList<Uri> = ArrayList()

        // Output stream
        var fos: OutputStream? = null

        // For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // getting the contentResolver
            this.contentResolver?.also { resolver ->

                // Content resolver will process the contentvalues
                val contentValues = ContentValues().apply {

                    // putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                // Inserting the contentValues to
                // contentResolver and getting the Uri
                val imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!

                // Opening an outputstream with the Uri that we got
                fos = imageUri.let {
                    it.let { it1 -> resolver.openOutputStream(it1) }
                }
                imagesUri.add(imageUri)
            }
        } else {
            // These for devices running on android < Q
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            val uri = Uri.fromFile(image)
            imagesUri.add(uri)
            fos = FileOutputStream(image)
        }

        fos?.use {
            // Finally writing the bitmap to the output stream that we opened
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)

            val intent = Intent()
            intent.action = Intent.ACTION_SEND_MULTIPLE
            intent.type = "*/*"
            intent.putExtra(Intent.EXTRA_TEXT, selectedSong)
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imagesUri)
            startActivity(intent)

        }
    }
}