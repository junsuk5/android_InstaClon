package com.example.instaclon

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("imageUrl")
fun imageUrl(view: CircleImageView, uri: Uri) {
    Glide.with(view)
        .load(uri)
        .placeholder(R.mipmap.ic_launcher)
        .into(view)
}

@BindingAdapter("imageUrl2")
fun imageUrl2(view: ImageView, uri: String) {
    Glide.with(view)
        .load(uri)
        .placeholder(R.mipmap.ic_launcher)
        .into(view)
}

@BindingAdapter("imageUrl3")
fun imageUrl3(view: CircleImageView, uri: String) {
    Glide.with(view)
        .load(uri)
        .placeholder(R.mipmap.ic_launcher)
        .into(view)
}