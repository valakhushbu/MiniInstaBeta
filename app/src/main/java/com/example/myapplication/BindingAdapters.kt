package com.example.myapplication

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("bitmapSrc")
fun setBitmapSrc(imageView: ImageView, bitmap: Bitmap?) {
    bitmap?.let {
        Glide.with(imageView.context)
            .load(it)
            .into(imageView)
    }
}