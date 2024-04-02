package com.mozhimen.xmlk.imagek

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mozhimen.imagek.glide.ImageKGlide
import com.mozhimen.basick.utilk.android.util.dp2px

/**
 * @ClassName ImageKBindingAdapter
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2024/1/16 22:06
 * @Version 1.0
 */
object ImageKBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["loadImageWhen_ofGlide", "loadImageWhen_ofGlide_statusTrue", "loadImageWhen_ofGlide_statusFalse"], requireAll = true)
    fun loadImageWhen_ofGlide(imageView: ImageView, loadImageWhen_ofGlide: Boolean, loadImageWhen_ofGlide_statusTrue: Any, loadImageWhen_ofGlide_statusFalse: Any) {
        if (loadImageWhen_ofGlide) {
            ImageKGlide.loadImage_ofGlide(imageView, loadImageWhen_ofGlide_statusTrue)
        } else {
            ImageKGlide.loadImage_ofGlide(imageView, loadImageWhen_ofGlide_statusFalse)
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["loadImageRoundedCorner_ofGlide", "roundedCornerRadius"], requireAll = true)
    fun loadImageRoundedCorner_ofGlide(imageView: ImageView, loadImageRoundedCorner_ofGlide: Any, roundedCornerRadius: Int) {
        ImageKGlide.loadImageRoundedCorner_ofGlide(imageView, loadImageRoundedCorner_ofGlide, roundedCornerRadius.dp2px().toInt())
    }

    @JvmStatic
    @BindingAdapter(value = ["loadImageRoundedCorner_ofGlide", "roundedCornerRadius"], requireAll = true)
    fun loadImageRoundedCorner_ofGlide(imageView: ImageView, loadImageRoundedCorner_ofGlide: Any, roundedCornerRadius: Float) {
        ImageKGlide.loadImageRoundedCorner_ofGlide(imageView, loadImageRoundedCorner_ofGlide, roundedCornerRadius.dp2px().toInt())
    }
}