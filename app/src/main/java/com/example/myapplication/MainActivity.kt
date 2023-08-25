package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.permissionaccessmanager.PermissionAccess

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var permissionManager: PermissionAccess
    private var arrayList: List<ImageData> = arrayListOf()
    lateinit var recyclerView:RecyclerView
    lateinit var adapter : ImageAdapter
    lateinit var db : DBHelper

    private val getImg = registerForActivityResult(ActivityResultContracts.GetContent()) { imageUri: Uri? ->
        imageUri?.let {
            val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            binding.circleImageView.setImageBitmap(imageBitmap)
            db.dao().insertAll(ImageData(0,imageBitmap))
            val fetchedData = db.dao().getAll()
            adapter.updateData(fetchedData)
        }
    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                binding.circleImageView.setImageBitmap(it)
                db.dao().insertAll(ImageData(0,imageBitmap))
                val fetchedData = db.dao().getAll()
                adapter.updateData(fetchedData)

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DBHelper.getDatabase(this@MainActivity)
        permissionManager = PermissionAccess.with(this@MainActivity,this)

        recyclerView = binding.recyclerView
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager

        val itemList: List<ImageData> = db.dao().getAll()
        adapter = ImageAdapter(itemList)
        recyclerView.adapter = adapter
        adapter.submitList(itemList)

        binding.selectImage.setOnClickListener {
            permissionManager.sdkIntAboveOreo {
                permissionManager.isPermissionGranted(this, android.Manifest.permission.CAMERA) { granted ->
                    if (granted) {
                        showImageSelectionDialog()
                        Log.d("MainActivity", "Permission granted")
                    } else {
                        permissionManager.registerActivityForResult?.launch(android.Manifest.permission.CAMERA)
                    }
                }
            }
        }
    }

    private fun showImageSelectionDialog() {
        val options = arrayOf("Gallery", "Camera")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { dialog, which ->
            when (options[which]) {
                "Gallery" -> getImg.launch("image/*")
                "Camera" -> openCamera()
            }
        }
        builder.show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }
}