package com.example.instaclon

import android.net.Uri
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