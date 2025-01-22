package com.mozhimen.xmlk.imagek.shimmer.helpers

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.google.android.material.imageview.ShapeableImageView.LAYER_TYPE_HARDWARE
import com.google.android.material.imageview.ShapeableImageView.LAYER_TYPE_NONE
import com.google.android.material.imageview.ShapeableImageView.VISIBLE
import com.mozhimen.xmlk.basic.commons.ILayoutK
import com.mozhimen.xmlk.drawablek.DrawableKShimmer
import com.mozhimen.xmlk.drawablek.DrawableKShimmer.AShimmerDirection
import com.mozhimen.xmlk.imagek.shimmer.R
import com.mozhimen.xmlk.imagek.shimmer.commons.IImageKShimmer

/**
 * @ClassName ImageKShimmerProxy
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/1/7
 * @Version 1.0
 */
class ImageKShimmerProxy<T> {
    internal val mContentPaint = Paint()
    internal val mDrawableKShimmer: DrawableKShimmer = DrawableKShimmer()
    internal var mStoppedShimmerBecauseVisibility = false

    /** Return whether the shimmer drawable is visible.  */
    var isShimmerVisible: Boolean = true
        private set
    val shimmerConfig: DrawableKShimmer.ShimmerConfig?
        get() = mDrawableKShimmer.getShimmerConfig()
    val isShimmerRunning: Boolean
        get() = mDrawableKShimmer.isShimmerRunning()
    val isShimmerStarted: Boolean
        /** Return whether the shimmer animation has been started.  */
        get() = mDrawableKShimmer.isShimmerStarted()

    ///////////////////////////////////////////////////////////////////////////////

    fun setShimmerConfig(view: View, shimmerConfig: DrawableKShimmer.ShimmerConfig) {
        mDrawableKShimmer.setShimmerConfig(shimmerConfig)
        if (shimmerConfig.clipToChildren) {
            view.setLayerType(LAYER_TYPE_HARDWARE, mContentPaint)
        } else {
            view.setLayerType(LAYER_TYPE_NONE, null)
        }
    }

    /** Starts the shimmer animation.  */
    fun startShimmer(view: View) {
        if (view.isAttachedToWindow) {
            mDrawableKShimmer.startShimmer()
        }
    }

    /** Stops the shimmer animation.  */
    fun stopShimmer() {
        mStoppedShimmerBecauseVisibility = false
        mDrawableKShimmer.stopShimmer()
    }

    /**
     * Sets the ShimmerDrawable to be visible.
     * @param startShimmer Whether to start the shimmer again.
     */
    fun showShimmer(view: View, startShimmer: Boolean) {
        isShimmerVisible = true
        if (startShimmer) {
            startShimmer(view)
        }
        view.invalidate()
    }

    /** Sets the ShimmerDrawable to be invisible, stopping it in the process.  */
    fun hideShimmer(view: View) {
        stopShimmer()
        isShimmerVisible = false
        view.invalidate()
    }

    fun setStaticAnimationProgress(value: Float) {
        mDrawableKShimmer.setStaticAnimationProgress(value)
    }

    fun clearStaticAnimationProgress() {
        mDrawableKShimmer.clearStaticAnimationProgress()
    }

    fun initAttrs(view: View, context: Context, attrs: AttributeSet?) {
        view.setWillNotDraw(false)
        mDrawableKShimmer.setCallback(view)

        if (attrs == null) {
            setShimmerConfig(view, DrawableKShimmer.ShimmerConfig.AlphaHighlightBuilder().build())
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageKShimmer, 0, 0)
        try {
            val shimmerBuilder: DrawableKShimmer.ShimmerConfig.Builder<*> =
                if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_colored) && typedArray.getBoolean(R.styleable.ImageKShimmer_imageKShimmer_colored, false))
                    DrawableKShimmer.ShimmerConfig.ColorHighlightBuilder()
                else
                    DrawableKShimmer.ShimmerConfig.AlphaHighlightBuilder()
            setShimmerConfig(view, consumeAttributes(typedArray, shimmerBuilder).build())
        } finally {
            typedArray.recycle()
        }
    }

    fun onVisibilityChanged(changedView: View, visibility: Int) {
        // View's constructor directly invokes this method, in which case no fields on
        // this class have been fully initialized yet.
        if (mDrawableKShimmer == null) {
            return
        }
        if (visibility != VISIBLE) {
            // GONE or INVISIBLE
            if (isShimmerStarted) {
                stopShimmer()
                mStoppedShimmerBecauseVisibility = true
            }
        } else if (mStoppedShimmerBecauseVisibility) {
            mDrawableKShimmer.maybeStartShimmer()
            mStoppedShimmerBecauseVisibility = false
        }
    }

    fun onLayout(width: Int, height: Int) {
        mDrawableKShimmer.setBounds(0, 0, width, height)
    }

    fun onAttachedToWindow() {
        mDrawableKShimmer.maybeStartShimmer()
    }

    fun onDetachedFromWindow() {
        stopShimmer()
    }

    fun dispatchDraw(canvas: Canvas) {
        if (isShimmerVisible) {
            mDrawableKShimmer.draw(canvas)
        }
    }

    /** Applies all specified options from the [AttributeSet].  */
    fun consumeAttributes(context: Context, attrs: AttributeSet?, builder: DrawableKShimmer.ShimmerConfig.Builder<*>): DrawableKShimmer.ShimmerConfig.Builder<*> {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageKShimmer, 0, 0)
        return consumeAttributes(typedArray, builder)
    }

    fun consumeAttributes(typedArray: TypedArray, builder: DrawableKShimmer.ShimmerConfig.Builder<*>): DrawableKShimmer.ShimmerConfig.Builder<*> {
        //direction
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_direction)) {
            val direction =
                typedArray.getInt(R.styleable.ImageKShimmer_imageKShimmer_direction, builder.mShimmerConfig.direction)
            when (direction) {
                AShimmerDirection.LEFT_TO_RIGHT -> builder.setDirection(AShimmerDirection.LEFT_TO_RIGHT)
                AShimmerDirection.TOP_TO_BOTTOM -> builder.setDirection(AShimmerDirection.TOP_TO_BOTTOM)
                AShimmerDirection.RIGHT_TO_LEFT -> builder.setDirection(AShimmerDirection.RIGHT_TO_LEFT)
                AShimmerDirection.BOTTOM_TO_TOP -> builder.setDirection(AShimmerDirection.BOTTOM_TO_TOP)
                else -> builder.setDirection(AShimmerDirection.LEFT_TO_RIGHT)
            }
        }
        //shape
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_shape)) {
            val shape =
                typedArray.getInt(R.styleable.ImageKShimmer_imageKShimmer_shape, builder.mShimmerConfig.shape)
            when (shape) {
                DrawableKShimmer.AShape.LINEAR -> builder.setShape(DrawableKShimmer.AShape.LINEAR)
                DrawableKShimmer.AShape.RADIAL -> builder.setShape(DrawableKShimmer.AShape.RADIAL)
                else -> builder.setShape(DrawableKShimmer.AShape.LINEAR)
            }
        }
        //fixed_width
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_fixed_width)) {
            builder.setFixedWidth(
                typedArray.getDimensionPixelSize(R.styleable.ImageKShimmer_imageKShimmer_fixed_width, builder.mShimmerConfig.fixedWidth)
            )
        }
        //fixed_height
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_fixed_height)) {
            builder.setFixedHeight(
                typedArray.getDimensionPixelSize(R.styleable.ImageKShimmer_imageKShimmer_fixed_height, builder.mShimmerConfig.fixedHeight)
            )
        }
        //width_ratio
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_width_ratio)) {
            builder.setWidthRatio(
                typedArray.getFloat(R.styleable.ImageKShimmer_imageKShimmer_width_ratio, builder.mShimmerConfig.widthRatio)
            )
        }
        //height_ratio
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_height_ratio)) {
            builder.setHeightRatio(
                typedArray.getFloat(R.styleable.ImageKShimmer_imageKShimmer_height_ratio, builder.mShimmerConfig.heightRatio)
            )
        }
        //intensity
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_intensity)) {
            builder.setIntensity(
                typedArray.getFloat(R.styleable.ImageKShimmer_imageKShimmer_intensity, builder.mShimmerConfig.intensity)
            )
        }
        //dropoff
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_dropoff)) {
            builder.setDropoff(
                typedArray.getFloat(R.styleable.ImageKShimmer_imageKShimmer_dropoff, builder.mShimmerConfig.dropoff)
            )
        }
        //tilt
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_tilt)) {
            builder.setTilt(
                typedArray.getFloat(R.styleable.ImageKShimmer_imageKShimmer_tilt, builder.mShimmerConfig.tilt)
            )
        }
        //clip_to_children
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_clip_to_children)) {
            builder.setClipToChildren(
                typedArray.getBoolean(R.styleable.ImageKShimmer_imageKShimmer_clip_to_children, builder.mShimmerConfig.clipToChildren)
            )
        }
        //auto_start
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_auto_start)) {
            builder.setAutoStart(
                typedArray.getBoolean(R.styleable.ImageKShimmer_imageKShimmer_auto_start, builder.mShimmerConfig.autoStart)
            )
        }
        //repeat_count
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_repeat_count)) {
            builder.setRepeatCount(
                typedArray.getInt(R.styleable.ImageKShimmer_imageKShimmer_repeat_count, builder.mShimmerConfig.repeatCount)
            )
        }
        //repeat_mode
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_repeat_mode)) {
            builder.setRepeatMode(
                typedArray.getInt(R.styleable.ImageKShimmer_imageKShimmer_repeat_mode, builder.mShimmerConfig.repeatMode)
            )
        }
        //repeat_delay
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_repeat_delay)) {
            builder.setRepeatDelay(
                typedArray.getInt(R.styleable.ImageKShimmer_imageKShimmer_repeat_delay, builder.mShimmerConfig.repeatDelay.toInt()).toLong()
            )
        }
        //start_delay
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_start_delay)) {
            builder.setStartDelay(
                typedArray.getInt(R.styleable.ImageKShimmer_imageKShimmer_start_delay, builder.mShimmerConfig.startDelay.toInt()).toLong()
            )
        }
        //duration
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_duration)) {
            builder.setDuration(
                typedArray.getInt(R.styleable.ImageKShimmer_imageKShimmer_duration, builder.mShimmerConfig.animationDuration.toInt()).toLong()
            )
        }
        //base_color
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_base_color)) {
            builder.setBaseColor(
                typedArray.getColor(R.styleable.ImageKShimmer_imageKShimmer_base_color, builder.mShimmerConfig.baseColor)
            )
        }
        //highlight_color
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_highlight_color)) {
            builder.setHighlightColor(
                typedArray.getColor(R.styleable.ImageKShimmer_imageKShimmer_highlight_color, builder.mShimmerConfig.highlightColor)
            )
        }
        //base_alpha
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_base_alpha)) {
            builder.setBaseAlpha(typedArray.getFloat(R.styleable.ImageKShimmer_imageKShimmer_base_alpha, 0.3f))
        }
        //highlight_alpha
        if (typedArray.hasValue(R.styleable.ImageKShimmer_imageKShimmer_highlight_alpha)) {
            builder.setHighlightAlpha(typedArray.getFloat(R.styleable.ImageKShimmer_imageKShimmer_highlight_alpha, 1f))
        }
        return builder
    }
}