package com.leandro.cryptoview.utils

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.leandro.cryptoview.R

fun getProgressDrawable(context: Context): CircularProgressDrawable{
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        this.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN)
        start()
    }
}
fun ImageView.loadImage(uri: String?, progressDrawble: CircularProgressDrawable){
    val options = RequestOptions()
        .placeholder(progressDrawble)
        .error(R.drawable.ic_image_grey_24)
    Glide.with(context).setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}