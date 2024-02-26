package com.mozhimen.uicorek.imagek.blur

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mozhimen.imagek.glide.blur.ImageKGlideBlur

/**
 * @ClassName ImageKBlurBindingAdapter
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/2/26
 * @Version 1.0
 */
object ImageKBlurBindingAdapter {
    @JvmStatic
    @BindingAdapter(value = ["loadImageGlideBlur", "placeholder"], requireAll = true)
    fun loadImageGlideBlur(imageView: ImageView, loadImageBlurGlide: Any, placeholder: Int) {
        ImageKGlideBlur.loadImageGlideBlur(imageView, loadImageBlurGlide, placeholder)
    }
}