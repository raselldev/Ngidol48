package com.arira.ngidol48.ui.activity.photoCard.export

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.arira.ngidol48.databinding.ActivityExportPhotoCardBinding
import com.arira.ngidol48.helper.BaseActivity
import com.arira.ngidol48.helper.Config
import com.arira.ngidol48.helper.Validasi
import com.arira.ngidol48.model.PhotoCardImage
import com.arira.ngidol48.ui.activity.home.MainActivity
import com.arira.ngidol48.utilities.Go
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.OutputStream

class ExportPhotoCardActivity : BaseActivity() {
    private lateinit var binding: ActivityExportPhotoCardBinding
    private var photoCardImage: PhotoCardImage = PhotoCardImage()
    private var signImage:String = ""
    private var greatingText:String = ""
    private var exportPhotoCard:Boolean = false
    private var exportGreating:Boolean = false
    private var imagesUri:ArrayList<Uri> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExportPhotoCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar("Create Photo Card", binding.toolbar)

        Validasi().ijinDokumen(this)

        photoCardImage = intent.getParcelableExtra(Config.extra_model) ?: PhotoCardImage()
        signImage = intent.getStringExtra(Config.extra_other) ?: ""
        greatingText = intent.getStringExtra(Config.extra_url) ?: ""

        binding.ivCardPhotocard.rotation = -5.0f
        binding.ivCardGreating.rotation = 5.0f

        action()
        setData()
    }

    fun setData(){
        Glide.with(this).load(photoCardImage.image)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(
                binding.ivPhotoCard
            )

        Glide.with(this).load(signImage)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .into(
                binding.ivSelectedSign
            )

        binding.tvGreating.text = greatingText
    }

    fun action(){
        binding.btnExport.setOnClickListener {
            exportPhotCard()
            exportGreeting()
        }
    }

    private fun exportPhotCard(){
        binding.ivCardPhotocard.setLayerType(View.LAYER_TYPE_SOFTWARE, null) // This is needed to enable software rendering for clipping

        val bitmap = Bitmap.createBitmap(binding.ivCardPhotocard.width, binding.ivCardPhotocard.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val path = Path()
        path.addRoundRect(RectF(0f, 0f, binding.ivCardPhotocard.width.toFloat(), binding.ivCardPhotocard.height.toFloat()), binding.ivCardPhotocard.radius, binding.ivCardPhotocard.radius, Path.Direction.CW)
        canvas.clipPath(path)

        binding.ivCardPhotocard.draw(canvas)

        try{
            saveMediaToStorage(bitmap, "photocard", "photo")
        }catch (e: FileNotFoundException){
            toast.show("Gagal mengunduh berkas, pastikan anda memberi ijin berkas aplikasi", this)
        }
    }

    private fun exportGreeting(){
        binding.ivCardGreating.setLayerType(View.LAYER_TYPE_SOFTWARE, null) // This is needed to enable software rendering for clipping

        val bitmap = Bitmap.createBitmap(binding.ivCardGreating.width, binding.ivCardGreating.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val path = Path()
        path.addRoundRect(RectF(0f, 0f, binding.ivCardGreating.width.toFloat(), binding.ivCardGreating.height.toFloat()), binding.ivCardGreating.radius, binding.ivCardGreating.radius, Path.Direction.CW)
        canvas.clipPath(path)

        binding.ivCardGreating.draw(canvas)

        try{
            saveMediaToStorage(bitmap, "greating", "greating")
        }catch (e: FileNotFoundException){
            toast.show("Gagal mengunduh berkas, pastikan anda memberi ijin berkas aplikasi", this)
        }
    }

    // this method saves the image to gallery
    private fun saveMediaToStorage(bitmap: Bitmap, prefix:String, type:String) {
        // Generating a file name
        val filename = "${System.currentTimeMillis()}_${prefix}.png"

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
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                // Opening an outputstream with the Uri that we got
                fos = imageUri?.let {
                    resolver.openOutputStream(it)
                }

                imageUri?.let { imagesUri.add(it) }
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

            if (type == "photo"){
                exportPhotoCard = true
            }else{
                exportGreating = true
            }

            if (exportPhotoCard && exportGreating){
                val sweetDialog =  SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                sweetDialog.titleText = "Create Photo Card"
                sweetDialog.contentText = "Photocard dan ucapapan anda berhasil diunduh, silahkan perikasa galeri anda"


                if (imagesUri.isNotEmpty()){
                    sweetDialog.confirmText = "Bagikan"
                    sweetDialog.setConfirmClickListener {

                        val intent = Intent()
                        intent.action = Intent.ACTION_SEND_MULTIPLE
                        intent.type = "*/*"
                        intent.putExtra(Intent.EXTRA_TEXT, greatingText)
                        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imagesUri)
                        startActivity(intent)
                    }
                }
                sweetDialog.cancelText = "Kembali"
                sweetDialog.setCancelClickListener {
                    sweetDialog.dismissWithAnimation()
                    sweetDialog.dismiss()
                        Go(this).move(MainActivity::class.java, clearPrevious = true)
                    }

                sweetDialog.show()

            }
        }
    }
}