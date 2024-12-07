package com.example.project_aplikasi_mobile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_aplikasi_mobile.Adapter.ImageAdapter
import com.example.project_aplikasi_mobile.Api.ApiClient
import com.example.project_aplikasi_mobile.Model.TemuanItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class PerbaikanActivity : AppCompatActivity() {
    private lateinit var inputFeeder: EditText
    private lateinit var inputNoPole: EditText
    private lateinit var inputPeralatan: EditText
    private lateinit var inputTemuan: EditText
    private lateinit var imageRecyclerView: RecyclerView
    private val selectedImagePaths = mutableListOf<String>()
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perbaikan)

        inputFeeder = findViewById(R.id.inputFeeder)
        inputNoPole = findViewById(R.id.inputNoPole)
        inputPeralatan = findViewById(R.id.inputTipePeralatan)
        inputTemuan = findViewById(R.id.inputTemuan)
        imageRecyclerView = findViewById(R.id.imageRecyclerView)
        val imageBack: ImageView = findViewById(R.id.imageBack)
        val btnChooseImage: ImageButton = findViewById(R.id.btnChooseImage)
        val btnSubmit: Button = findViewById(R.id.btnSubmit)

        imageBack.setOnClickListener {
            onBackPressed()
        }

        btnChooseImage.setOnClickListener {
            chooseImage()
        }

        btnSubmit.setOnClickListener {
            submitData()
        }

        imageRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter = ImageAdapter(selectedImagePaths)
        imageRecyclerView.adapter = imageAdapter
    }

    private fun chooseImage() {
        val options = arrayOf("Ambil Foto", "Pilih dari Galeri")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Gambar")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> capturePhoto()
                1 -> pickFromGallery()
            }
        }
        builder.show()
    }

    private fun capturePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, 1001)
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(intent, 1002)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1001 -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    val imagePath = saveImage(imageBitmap)
                    selectedImagePaths.add(imagePath)
                    imageAdapter.notifyDataSetChanged()
                }
                1002 -> {
                    if (data?.clipData != null) {
                        val count = data.clipData!!.itemCount
                        for (i in 0 until count) {
                            val imageUri = data.clipData!!.getItemAt(i).uri
                            val imagePath = getPathFromUri(imageUri)
                            selectedImagePaths.add(imagePath)
                        }
                    } else if (data?.data != null) {
                        val imageUri = data.data!!
                        val imagePath = getPathFromUri(imageUri)
                        selectedImagePaths.add(imagePath)
                    }
                    imageAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun saveImage(bitmap: Bitmap): String {
        val file = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return file.absolutePath
    }

    private fun getPathFromUri(uri: Uri?): String {
        var path = ""
        uri?.let {
            val cursor = contentResolver.query(it, null, null, null, null)
            cursor?.moveToFirst()
            val idx = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            if (idx != null) {
                path = cursor.getString(idx)
            }
            cursor?.close()
        }
        return path
    }

    private fun submitData() {
        val temuanItem = TemuanItem(
            created_at = "", // Tanggal dan waktu akan diisi otomatis oleh server
            equipment_type = inputPeralatan.text.toString(),
            finding = inputTemuan.text.toString(),
            gambar = selectedImagePaths.toTypedArray(), // Convert to array
            lokasi_feeder = inputFeeder.text.toString(),
            no_pole = inputNoPole.text.toString(),
            status = "open", // Contoh status
            temuan_id = 0, // ID akan diisi oleh server
            updated_at = "" // Tanggal dan waktu akan diisi otomatis oleh server
        )

        // Optional: Basic input validation
        if (temuanItem.equipment_type.isEmpty() || temuanItem.finding.isEmpty() || temuanItem.lokasi_feeder.isEmpty() || temuanItem.no_pole.isEmpty()) {
            Toast.makeText(this, "Silahkan isi semua data yang diperlukan", Toast.LENGTH_SHORT).show()
            return
        }

        val files = selectedImagePaths.map { filePath ->
            val file = File(filePath)
            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
            MultipartBody.Part.createFormData("gambar[]", file.name, requestFile)
        }

        val requestBodyMap = mapOf(
            "equipment_type" to RequestBody.create("text/plain".toMediaTypeOrNull(), temuanItem.equipment_type),
            "finding" to RequestBody.create("text/plain".toMediaTypeOrNull(), temuanItem.finding),
            "lokasi_feeder" to RequestBody.create("text/plain".toMediaTypeOrNull(), temuanItem.lokasi_feeder),
            "no_pole" to RequestBody.create("text/plain".toMediaTypeOrNull(), temuanItem.no_pole),
            "status" to RequestBody.create("text/plain".toMediaTypeOrNull(), temuanItem.status)
        )

        val apiService = ApiClient.getInstance(this)
        CoroutineScope(Dispatchers.IO).launch {
            apiService.postTemuan(requestBodyMap, files).enqueue(object : Callback<TemuanItem> {
                override fun onResponse(call: Call<TemuanItem>, response: Response<TemuanItem>) {
                    if (response.isSuccessful) {
                        runOnUiThread {
                            Toast.makeText(this@PerbaikanActivity, "Data berhasil dikirim", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val errorMessage = if (errorBody != null) {
                            "Gagal mengirim data: $errorBody"
                        } else {
                            "Gagal mengirim data. Silahkan coba lagi nanti."
                        }
                        runOnUiThread {
                            Toast.makeText(this@PerbaikanActivity, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<TemuanItem>, t: Throwable) {
                    val errorMessage = when (t) {
                        is IOException -> "Kesalahan jaringan. Periksa koneksi internet anda."
                        else -> "Error: ${t.message}"
                    }
                    runOnUiThread {
                        Toast.makeText(this@PerbaikanActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    Log.e("PerbaikanActivity", "Error submitting data", t)  // Log detailed error for debugging
                }
            })
        }
    }
}
