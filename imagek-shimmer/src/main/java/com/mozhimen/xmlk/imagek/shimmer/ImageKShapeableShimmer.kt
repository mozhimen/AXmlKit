package com.mozhimen.xmlk.imagek.shimmer

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.google.android.material.imageview.ShapeableImageView
import com.mozhimen.kotlin.elemk.android.os.cons.CVersCode
import com.mozhimen.xmlk.basic.commons.IXmlK
import com.mozhimen.xmlk.drawablek.DrawableKShimmer
import com.mozhimen.xmlk.drawablek.DrawableKShimmer.AShimmerDirection
import com.mozhimen.xmlk.imagek.shimmer.commons.IImageKShimmer
import com.mozhimen.xmlk.imagek.shimmer.helpers.ImageKShimmerProxy

/**
 * @ClassName ImageKShimmer
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/1/6
 * @Version 1.0
 */
/**
 * Shimmer is an Android library that provides an easy way to add a shimmer effect to any [ ]. It is useful as an unobtrusive loading indicator, and was originally
 * developed for Facebook Home.
 *
 *
 * Find more examples and usage instructions over at: facebook.github.io/shimmer-android
 */
class ImageKShapeableShimmer : ShapeableImageView, IXmlK, IImageKShimmer<ImageKShapeableShimmer> {

    private val _imageKShimmerProxy by lazy { ImageKShimmerProxy<ImageKShapeableShimmer>() }

    ///////////////////////////////////////////////////////////////////////////////

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
    }

//    @RequiresApi(CVersCode.V_21_5_L)
//    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
//        initAttrs(attrs)
//    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun setShimmerConfig(shimmerConfig: DrawableKShimmer.ShimmerConfig): ImageKShapeableShimmer {
        _imageKShimmerProxy.setShimmerConfig(this, shimmerConfig)
        return this
    }

    override fun startShimmer() {
        _imageKShimmerProxy.startShimmer(this)
    }

    override fun stopShimmer() {
        _imageKShimmerProxy.stopShimmer()
    }

    override fun showShimmer(startShimmer: Boolean) {
        _imageKShimmerProxy.showShimmer(this, startShimmer)
    }

    override fun hideShimmer() {
        _imageKShimmerProxy.hideShimmer(this)
    }

    override fun setStaticAnimationProgress(value: Float) {
        _imageKShimmerProxy.setStaticAnimationProgress(value)
    }

    override fun clearStaticAnimationProgress() {
        _imageKShimmerProxy.clearStaticAnimationProgress()
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        _imageKShimmerProxy.initAttrs(this, context, attrs)
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === _imageKShimmerProxy.mDrawableKShimmer
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        _imageKShimmerProxy.onVisibilityChanged(changedView, visibility)
    }

    public override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        _imageKShimmerProxy.onLayout(width, height)
    }

    public override fun dispatchDraw(canvas: Canvas) {
        _imageKShimmerProxy.dispatchDraw(canvas)
    }

    public override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        _imageKShimmerProxy.onAttachedToWindow()
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _imageKShimmerProxy.onDetachedFromWindow()
    }
}
