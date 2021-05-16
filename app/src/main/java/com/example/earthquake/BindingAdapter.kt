package com.example.earthquake

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadThumbnail")
fun loadThumbnailIntoImageView(imageView : ImageView, url : String) {
    Glide.with(imageView).load(url).into(imageView)
}