package com.yk.silence.customandroid.ui

import android.content.Context
import android.widget.ImageView
import com.yk.silence.customandroid.R
import com.yk.silence.customandroid.model.BannerModel
import com.yk.silence.customandroid.util.glide.ImageOptions
import com.yk.silence.customandroid.util.glide.loadImage
import com.youth.banner.loader.ImageLoader

/**
 * BannerLoader
 */
class BannerImageLoader : ImageLoader() {

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        val imagePath = (path as BannerModel).imagePath
        imageView?.loadImage(imagePath, ImageOptions().apply {
            placeholder = R.drawable.shape_bg_image_default
        })

    }
}