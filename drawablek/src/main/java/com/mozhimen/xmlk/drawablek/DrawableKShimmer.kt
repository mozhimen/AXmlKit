package com.mozhimen.xmlk.drawablek

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import androidx.annotation.Px
import com.mozhimen.kotlin.utilk.kotlin.ranges.constraint
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

/**
 * @ClassName DrawableKShimmer
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/1/6
 * @Version 1.0
 */
class DrawableKShimmer : Drawable() {
    data class ShimmerConfig(
        var alphaShimmer: Boolean = true,
        @AShimmerDirection
        var direction: Int = AShimmerDirection.LEFT_TO_RIGHT,
        @AShape
        var shape: Int = AShape.LINEAR,
        var fixedWidth: Int = 0,
        var fixedHeight: Int = 0,
        var widthRatio: Float = 1f,
        var heightRatio: Float = 1f,
        var intensity: Float = 0f,
        var dropoff: Float = 0.3f,
        var tilt: Float = 45f,
        var clipToChildren: Boolean = true,
        var autoStart: Boolean = true,
        var repeatCount: Int = ValueAnimator.INFINITE,
        @ARepeatMode
        var repeatMode: Int = ARepeatMode.RESTART,
        var repeatDelay: Long = 0L,
        var startDelay: Long = 0,
        var animationDuration: Long = 1000L,
        @ColorInt
        var baseColor: Int = 0x4cffffff,
        @ColorInt
        var highlightColor: Int = Color.WHITE,
        val colors: IntArray = IntArray(4),
        val positions: FloatArray = FloatArray(4),
        val bounds: RectF = RectF(),
    ) {
        fun updateColors() {
            when (shape) {
                AShape.LINEAR -> {
                    colors[0] = baseColor
                    colors[1] = highlightColor
                    colors[2] = highlightColor
                    colors[3] = baseColor
                }

                AShape.RADIAL -> {
                    colors[0] = highlightColor
                    colors[1] = highlightColor
                    colors[2] = baseColor
                    colors[3] = baseColor
                }

                else -> {
                    colors[0] = baseColor
                    colors[1] = highlightColor
                    colors[2] = highlightColor
                    colors[3] = baseColor
                }
            }
        }

        fun updatePositions() {
            when (shape) {
                AShape.LINEAR -> {
                    positions[0] = max(((1f - intensity - dropoff) / 2f).toDouble(), 0.0).toFloat()
                    positions[1] = max(((1f - intensity - 0.001f) / 2f).toDouble(), 0.0).toFloat()
                    positions[2] = min(((1f + intensity + 0.001f) / 2f).toDouble(), 1.0).toFloat()
                    positions[3] = min(((1f + intensity + dropoff) / 2f).toDouble(), 1.0).toFloat()
                }

                AShape.RADIAL -> {
                    positions[0] = 0f
                    positions[1] = min(intensity.toDouble(), 1.0).toFloat()
                    positions[2] = min((intensity + dropoff).toDouble(), 1.0).toFloat()
                    positions[3] = 1f
                }

                else -> {
                    positions[0] = max(((1f - intensity - dropoff) / 2f).toDouble(), 0.0).toFloat()
                    positions[1] = max(((1f - intensity - 0.001f) / 2f).toDouble(), 0.0).toFloat()
                    positions[2] = min(((1f + intensity + 0.001f) / 2f).toDouble(), 1.0).toFloat()
                    positions[3] = min(((1f + intensity + dropoff) / 2f).toDouble(), 1.0).toFloat()
                }
            }
        }

        fun updateBounds(viewWidth: Int, viewHeight: Int) {
            val magnitude = max(viewWidth.toDouble(), viewHeight.toDouble()).toInt()
            val rad = Math.PI / 2f - Math.toRadians((tilt % 90f).toDouble())
            val hyp = magnitude / sin(rad)
            val padding = 3 * Math.round((hyp - magnitude).toFloat() / 2f)
            bounds.set(-padding.toFloat(), -padding.toFloat(), (width(viewWidth) + padding).toFloat(), (height(viewHeight) + padding).toFloat())
        }

        fun width(width: Int): Int {
            return if (fixedWidth > 0) fixedWidth else Math.round(widthRatio * width)
        }

        fun height(height: Int): Int {
            return if (fixedHeight > 0) fixedHeight else Math.round(heightRatio * height)
        }

        abstract class Builder<T : Builder<T>?> {
            val mShimmerConfig: ShimmerConfig = ShimmerConfig()

            // Gets around unchecked cast
            protected abstract val `this`: T

            /** Applies all specified options from the [AttributeSet].  *//*
            fun consumeAttributes(context: Context, attrs: AttributeSet?): T {
                val a = context.obtainStyledAttributes(attrs, R.styleable.ShimmerFrameLayout, 0, 0)
                return consumeAttributes(a)
            }

            fun consumeAttributes(a: TypedArray): T {
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_clip_to_children)) {
                    setClipToChildren(
                        a.getBoolean(
                            R.styleable.ShimmerFrameLayout_shimmer_clip_to_children, mShimmerConfig.clipToChildren
                        )
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_auto_start)) {
                    setAutoStart(
                        a.getBoolean(R.styleable.ShimmerFrameLayout_shimmer_auto_start, mShimmerConfig.autoStart)
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_base_alpha)) {
                    setBaseAlpha(a.getFloat(R.styleable.ShimmerFrameLayout_shimmer_base_alpha, 0.3f))
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_highlight_alpha)) {
                    setHighlightAlpha(a.getFloat(R.styleable.ShimmerFrameLayout_shimmer_highlight_alpha, 1f))
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_duration)) {
                    setDuration(
                        a.getInt(
                            R.styleable.ShimmerFrameLayout_shimmer_duration, mShimmerConfig.animationDuration.toInt()
                        ).toLong()
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_repeat_count)) {
                    setRepeatCount(
                        a.getInt(R.styleable.ShimmerFrameLayout_shimmer_repeat_count, mShimmerConfig.repeatCount)
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_repeat_delay)) {
                    setRepeatDelay(
                        a.getInt(
                            R.styleable.ShimmerFrameLayout_shimmer_repeat_delay, mShimmerConfig.repeatDelay.toInt()
                        ).toLong()
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_repeat_mode)) {
                    setRepeatMode(
                        a.getInt(R.styleable.ShimmerFrameLayout_shimmer_repeat_mode, mShimmerConfig.repeatMode)
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_start_delay)) {
                    setStartDelay(
                        a.getInt(
                            R.styleable.ShimmerFrameLayout_shimmer_start_delay, mShimmerConfig.startDelay.toInt()
                        ).toLong()
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_direction)) {
                    val direction =
                        a.getInt(R.styleable.ShimmerFrameLayout_shimmer_direction, mShimmerConfig.direction)
                    when (direction) {
                        AShimmerDirection.LEFT_TO_RIGHT -> setDirection(AShimmerDirection.LEFT_TO_RIGHT)
                        AShimmerDirection.TOP_TO_BOTTOM -> setDirection(AShimmerDirection.TOP_TO_BOTTOM)
                        AShimmerDirection.RIGHT_TO_LEFT -> setDirection(AShimmerDirection.RIGHT_TO_LEFT)
                        AShimmerDirection.BOTTOM_TO_TOP -> setDirection(AShimmerDirection.BOTTOM_TO_TOP)
                        else -> setDirection(AShimmerDirection.LEFT_TO_RIGHT)
                    }
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_shape)) {
                    val shape = a.getInt(R.styleable.ShimmerFrameLayout_shimmer_shape, mShimmerConfig.shape)
                    when (shape) {
                        com.facebook.shimmer.Shimmer.Shape.LINEAR -> setShape(com.facebook.shimmer.Shimmer.Shape.LINEAR)
                        com.facebook.shimmer.Shimmer.Shape.RADIAL -> setShape(com.facebook.shimmer.Shimmer.Shape.RADIAL)
                        else -> setShape(com.facebook.shimmer.Shimmer.Shape.LINEAR)
                    }
                }

                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_dropoff)) {
                    setDropoff(a.getFloat(R.styleable.ShimmerFrameLayout_shimmer_dropoff, mShimmerConfig.dropoff))
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_fixed_width)) {
                    setFixedWidth(
                        a.getDimensionPixelSize(
                            R.styleable.ShimmerFrameLayout_shimmer_fixed_width, mShimmerConfig.fixedWidth
                        )
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_fixed_height)) {
                    setFixedHeight(
                        a.getDimensionPixelSize(
                            R.styleable.ShimmerFrameLayout_shimmer_fixed_height, mShimmerConfig.fixedHeight
                        )
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_intensity)) {
                    setIntensity(
                        a.getFloat(R.styleable.ShimmerFrameLayout_shimmer_intensity, mShimmerConfig.intensity)
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_width_ratio)) {
                    setWidthRatio(
                        a.getFloat(R.styleable.ShimmerFrameLayout_shimmer_width_ratio, mShimmerConfig.widthRatio)
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_height_ratio)) {
                    setHeightRatio(
                        a.getFloat(R.styleable.ShimmerFrameLayout_shimmer_height_ratio, mShimmerConfig.heightRatio)
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_tilt)) {
                    setTilt(
                        a.getFloat(R.styleable.ShimmerFrameLayout_shimmer_tilt, mShimmerConfig.tilt)
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_base_color)) {
                    setBaseColor(
                        a.getColor(R.styleable.ShimmerFrameLayout_shimmer_base_color, mShimmer.baseColor)
                    )
                }
                if (a.hasValue(R.styleable.ShimmerFrameLayout_shimmer_highlight_color)) {
                    setHighlightColor(
                        a.getColor(R.styleable.ShimmerFrameLayout_shimmer_highlight_color, mShimmer.highlightColor)
                    )
                }
                return `this`
            }*/

            /** Copies the configuration of an already built Shimmer to this builder  */
            fun copyFrom(other: ShimmerConfig): T {
                setDirection(other.direction)
                setShape(other.shape)
                setFixedWidth(other.fixedWidth)
                setFixedHeight(other.fixedHeight)
                setWidthRatio(other.widthRatio)
                setHeightRatio(other.heightRatio)
                setIntensity(other.intensity)
                setDropoff(other.dropoff)
                setTilt(other.tilt)
                setClipToChildren(other.clipToChildren)
                setAutoStart(other.autoStart)
                setRepeatCount(other.repeatCount)
                setRepeatMode(other.repeatMode)
                setRepeatDelay(other.repeatDelay)
                setStartDelay(other.startDelay)
                setDuration(other.animationDuration)
                setBaseColor(other.baseColor)
                setHighlightColor(other.highlightColor)
                return `this`
            }

            fun copyFrom(other: Builder<*>): T {
                setDirection(other.mShimmerConfig.direction)
                setShape(other.mShimmerConfig.shape)
                setFixedWidth(other.mShimmerConfig.fixedWidth)
                setFixedHeight(other.mShimmerConfig.fixedHeight)
                setWidthRatio(other.mShimmerConfig.widthRatio)
                setHeightRatio(other.mShimmerConfig.heightRatio)
                setIntensity(other.mShimmerConfig.intensity)
                setDropoff(other.mShimmerConfig.dropoff)
                setTilt(other.mShimmerConfig.tilt)
                setClipToChildren(other.mShimmerConfig.clipToChildren)
                setAutoStart(other.mShimmerConfig.autoStart)
                setRepeatCount(other.mShimmerConfig.repeatCount)
                setRepeatMode(other.mShimmerConfig.repeatMode)
                setRepeatDelay(other.mShimmerConfig.repeatDelay)
                setStartDelay(other.mShimmerConfig.startDelay)
                setDuration(other.mShimmerConfig.animationDuration)
                setBaseColor(other.mShimmerConfig.baseColor)
                setHighlightColor(other.mShimmerConfig.highlightColor)
                return `this`
            }

            /** 设置闪烁扫过的方向。Sets the direction of the shimmer's sweep. See [Direction].  */
            fun setDirection(@AShimmerDirection direction: Int): T {
                mShimmerConfig.direction = direction
                return `this`
            }

            /** *设置闪烁的形状。Sets the shape of the shimmer. See [Shape].  */
            fun setShape(@AShape shape: Int): T {
                mShimmerConfig.shape = shape
                return `this`
            }

            /** 设置闪烁的固定宽度，以像素为单位。Sets the fixed width of the shimmer, in pixels.  */
            fun setFixedWidth(@Px fixedWidth: Int): T {
                require(fixedWidth >= 0) { "Given invalid width: $fixedWidth" }
                mShimmerConfig.fixedWidth = fixedWidth
                return `this`
            }

            /** 设置闪烁体的固定高度，以像素为单位。Sets the fixed height of the shimmer, in pixels.  */
            fun setFixedHeight(@Px fixedHeight: Int): T {
                require(fixedHeight >= 0) { "Given invalid height: $fixedHeight" }
                mShimmerConfig.fixedHeight = fixedHeight
                return `this`
            }

            /** 设置微光闪烁的宽度比例，乘以布局的总宽度。Sets the width ratio of the shimmer, multiplied against the total width of the layout.  */
            fun setWidthRatio(widthRatio: Float): T {
                require(!(widthRatio < 0f)) { "Given invalid width ratio: $widthRatio" }
                mShimmerConfig.widthRatio = widthRatio
                return `this`
            }

            /** 设置闪烁体的高度比，乘以布局的总高度。Sets the height ratio of the shimmer, multiplied against the total height of the layout.  */
            fun setHeightRatio(heightRatio: Float): T {
                require(!(heightRatio < 0f)) { "Given invalid height ratio: $heightRatio" }
                mShimmerConfig.heightRatio = heightRatio
                return `this`
            }

            /** 设置微光的强度。较大的值会导致微光变大。Sets the intensity of the shimmer. A larger value causes the shimmer to be larger.  */
            fun setIntensity(intensity: Float): T {
                require(!(intensity < 0f)) { "Given invalid intensity value: $intensity" }
                mShimmerConfig.intensity = intensity
                return `this`
            }

            /**
             * 设置微光渐变下降的速度。值越大，下降速度越快。Sets how quickly the shimmer's gradient drops-off. A larger value causes a sharper drop-off.
             */
            fun setDropoff(dropoff: Float): T {
                require(!(dropoff < 0f)) { "Given invalid dropoff value: $dropoff" }
                mShimmerConfig.dropoff = dropoff
                return `this`
            }

            /** 以度为单位设置闪烁器的倾斜角度。
             * Sets the tilt angle of the shimmer in degrees.  */
            fun setTilt(tilt: Float): T {
                mShimmerConfig.tilt = tilt
                return `this`
            }

            /**
             * 设置基数alpha，即底层子元素的alpha值，其值范围为[0，1]。
             * Sets the base alpha, which is the alpha of the underlying children, amount in the range [0,1].
             */
            fun setBaseAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): T {
                val intAlpha = (alpha.constraint(0f, 1f) * 255f).toInt()
                mShimmerConfig.baseColor = intAlpha shl 24 or (mShimmerConfig.baseColor and 0x00FFFFFF)
                return `this`
            }

            /** 将闪烁的alpha值设置在[0,1]范围内。
             * Sets the shimmer alpha amount in the range [0, 1].  */
            fun setHighlightAlpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): T {
                val intAlpha = (alpha.constraint(0f, 1f) * 255f).toInt()
                mShimmerConfig.highlightColor = intAlpha shl 24 or (mShimmerConfig.highlightColor and 0x00FFFFFF)
                return `this`
            }

            /**
             * 设置闪烁是否会夹到子布局的内容，或者它是否会不透明地绘制在子布局的顶部。
             * Sets whether the shimmer will clip to the childrens' contents, or if it will opaquely draw on top of the children.Sets whether the shimmer will clip to the childrens' contents, or if it will opaquely draw on
             * top of the children.
             */
            fun setClipToChildren(status: Boolean): T {
                mShimmerConfig.clipToChildren = status
                return `this`
            }

            /** 设置闪烁动画是否自动启动。
             * Sets whether the shimmering animation will start automatically.  */
            fun setAutoStart(status: Boolean): T {
                mShimmerConfig.autoStart = status
                return `this`
            }

            /**
             * 设置闪烁动画重复的频率。
             * Sets how often the shimmering animation will repeat. See [ ][android.animation.ValueAnimator.setRepeatCount].
             */
            fun setRepeatCount(repeatCount: Int): T {
                mShimmerConfig.repeatCount = repeatCount
                return `this`
            }

            /**
             * 设置闪烁动画的重复方式。
             * Sets how the shimmering animation will repeat. See [ ][android.animation.ValueAnimator.setRepeatMode].
             */
            fun setRepeatMode(mode: Int): T {
                mShimmerConfig.repeatMode = mode
                return `this`
            }

            /** 设置闪烁动画重复之间的等待时间。
             * Sets how long to wait in between repeats of the shimmering animation.  */
            fun setRepeatDelay(millis: Long): T {
                require(millis >= 0) { "Given a negative repeat delay: $millis" }
                mShimmerConfig.repeatDelay = millis
                return `this`
            }

            /** Sets how long to wait for starting the shimmering animation.  */
            fun setStartDelay(millis: Long): T {
                require(millis >= 0) { "Given a negative start delay: $millis" }
                mShimmerConfig.startDelay = millis
                return `this`
            }

            /** 设置闪烁动画开始的等待时间。
             * Sets how long the shimmering animation takes to do one full sweep.  */
            fun setDuration(millis: Long): T {
                require(millis >= 0) { "Given a negative duration: $millis" }
                mShimmerConfig.animationDuration = millis
                return `this`
            }

            /**
             * 设置亮片的高亮颜色。
             * Sets the highlight color for the shimmer.
             */
            fun setHighlightColor(@ColorInt color: Int): T {
                mShimmerConfig.highlightColor = color
                return `this`
            }

            /**
             * 设置微光闪烁的底色。
             * Sets the base color for the shimmer.
             */
            fun setBaseColor(@ColorInt color: Int): T {
                mShimmerConfig.baseColor = (mShimmerConfig.baseColor and -0x1000000) or (color and 0x00FFFFFF)
                return `this`
            }

            fun build(): ShimmerConfig {
                mShimmerConfig.updateColors()
                mShimmerConfig.updatePositions()
                return mShimmerConfig
            }
        }

        class AlphaHighlightBuilder : Builder<AlphaHighlightBuilder>() {
            init {
                mShimmerConfig.alphaShimmer = true
            }

            override val `this`: AlphaHighlightBuilder
                get() = this
        }

        class ColorHighlightBuilder : Builder<ColorHighlightBuilder>() {
            init {
                mShimmerConfig.alphaShimmer = false
            }

            override val `this`: ColorHighlightBuilder
                get() = this
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(AShimmerDirection.LEFT_TO_RIGHT, AShimmerDirection.TOP_TO_BOTTOM, AShimmerDirection.RIGHT_TO_LEFT, AShimmerDirection.BOTTOM_TO_TOP)
    annotation class AShimmerDirection {
        companion object {
            const val LEFT_TO_RIGHT: Int = 0
            const val TOP_TO_BOTTOM: Int = 1
            const val RIGHT_TO_LEFT: Int = 2
            const val BOTTOM_TO_TOP: Int = 3
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(ARepeatMode.RESTART, ARepeatMode.REVERSE)
    annotation class ARepeatMode {
        companion object {
            const val RESTART: Int = ValueAnimator.RESTART
            const val REVERSE: Int = ValueAnimator.REVERSE
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(AShape.LINEAR, AShape.RADIAL)
    annotation class AShape {
        companion object {
            const val LINEAR: Int = 0///** Linear gives a ray reflection effect.  */
            const val RADIAL: Int = 1///** Radial gives a spotlight effect.  */
        }
    }

    //////////////////////////////////////////////////////////////////////////////////

    private val mUpdateListener = ValueAnimator.AnimatorUpdateListener { invalidateSelf() }
    private val mShimmerPaint = Paint()
    private val mDrawRect = Rect()
    private val mShaderMatrix = Matrix()
    private var mValueAnimator: ValueAnimator? = null
    private var mStaticAnimationProgress = -1f
    private var mShimmerConfig: ShimmerConfig? = null

    //////////////////////////////////////////////////////////////////////////////////

    init {
        mShimmerPaint.isAntiAlias = true
    }

    //////////////////////////////////////////////////////////////////////////////////

    fun setShimmerConfig(shimmerConfig: ShimmerConfig) {
        mShimmerConfig = shimmerConfig
        mShimmerPaint.setXfermode(
            PorterDuffXfermode(
                if (mShimmerConfig!!.alphaShimmer) PorterDuff.Mode.DST_IN else PorterDuff.Mode.SRC_ATOP//PorterDuff.Mode.SRC_IN
            )
        )
        updateShader()
        updateValueAnimator()
        invalidateSelf()
    }

    fun getShimmerConfig(): ShimmerConfig? {
        return mShimmerConfig
    }

    /** Starts the shimmer animation.  */
    fun startShimmer() {
        if (mValueAnimator != null && !isShimmerStarted() && callback != null) {
            mValueAnimator!!.start()
        }
    }

    /** Stops the shimmer animation.  */
    fun stopShimmer() {
        if (mValueAnimator != null && isShimmerStarted()) {
            mValueAnimator!!.cancel()
        }
    }

    /** Return whether the shimmer animation has been started.  */
    fun isShimmerStarted(): Boolean {
        return mValueAnimator != null && mValueAnimator!!.isStarted
    }

    /** Return whether the shimmer animation is running.  */
    fun isShimmerRunning(): Boolean {
        return mValueAnimator != null && mValueAnimator!!.isRunning
    }

    fun maybeStartShimmer() {
        if (mValueAnimator != null && !mValueAnimator!!.isStarted
            && mShimmerConfig != null && mShimmerConfig!!.autoStart
            && callback != null
        ) {
            mValueAnimator!!.start()
        }
    }

    fun setStaticAnimationProgress(value: Float) {
        if (java.lang.Float.compare(value, mStaticAnimationProgress) == 0
            || (value < 0f && mStaticAnimationProgress < 0f)
        ) {
            return
        }
        mStaticAnimationProgress = min(value.toDouble(), 1.0).toFloat()
        invalidateSelf()
    }

    fun clearStaticAnimationProgress() {
        setStaticAnimationProgress(-1f)
    }

    //////////////////////////////////////////////////////////////////////////////////

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        mDrawRect.set(bounds)
        updateShader()
        maybeStartShimmer()
    }

    override fun draw(canvas: Canvas) {
        if (mShimmerConfig == null || mShimmerPaint.shader == null) return

        val tiltTan = tan(Math.toRadians(mShimmerConfig!!.tilt.toDouble())).toFloat()
        val translateHeight = mDrawRect.height() + tiltTan * mDrawRect.width()
        val translateWidth = mDrawRect.width() + tiltTan * mDrawRect.height()
        val dx: Float
        val dy: Float

        val animatedValue = if (mStaticAnimationProgress < 0f) {
            if (mValueAnimator != null) mValueAnimator!!.animatedValue as Float else 0f
        } else {
            mStaticAnimationProgress
        }

        when (mShimmerConfig!!.direction) {
            AShimmerDirection.LEFT_TO_RIGHT -> {
                dx = offset(-translateWidth, translateWidth, animatedValue)
                dy = 0f
            }

            AShimmerDirection.RIGHT_TO_LEFT -> {
                dx = offset(translateWidth, -translateWidth, animatedValue)
                dy = 0f
            }

            AShimmerDirection.TOP_TO_BOTTOM -> {
                dx = 0f
                dy = offset(-translateHeight, translateHeight, animatedValue)
            }

            AShimmerDirection.BOTTOM_TO_TOP -> {
                dx = 0f
                dy = offset(translateHeight, -translateHeight, animatedValue)
            }

            else -> {
                dx = offset(-translateWidth, translateWidth, animatedValue)
                dy = 0f
            }
        }

        mShaderMatrix.reset()
        mShaderMatrix.setRotate(mShimmerConfig!!.tilt, mDrawRect.width() / 2f, mDrawRect.height() / 2f)
        mShaderMatrix.preTranslate(dx, dy)
        mShimmerPaint.shader.setLocalMatrix(mShaderMatrix)
        canvas.drawRect(mDrawRect, mShimmerPaint)
    }

    override fun setAlpha(alpha: Int) {
        // No-op, modify the Shimmer object you pass in instead
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // No-op, modify the Shimmer object you pass in instead
    }

    override fun getOpacity(): Int {
        return if (mShimmerConfig != null && (mShimmerConfig!!.clipToChildren || mShimmerConfig!!.alphaShimmer))
            PixelFormat.TRANSLUCENT
        else
            PixelFormat.OPAQUE
    }

    //////////////////////////////////////////////////////////////////////////////////

    private fun offset(start: Float, end: Float, percent: Float): Float {
        return start + (end - start) * percent
    }

    private fun updateValueAnimator() {
        if (mShimmerConfig == null) return

        val started: Boolean
        if (mValueAnimator != null) {
            started = mValueAnimator!!.isStarted
            mValueAnimator!!.cancel()
            mValueAnimator!!.removeAllUpdateListeners()
        } else {
            started = false
        }

        mValueAnimator =
            ValueAnimator.ofFloat(0f, 1f + (mShimmerConfig!!.repeatDelay / mShimmerConfig!!.animationDuration).toFloat())
        mValueAnimator!!.apply {
            setInterpolator(LinearInterpolator())
            setRepeatMode(mShimmerConfig!!.repeatMode)
            setStartDelay(mShimmerConfig!!.startDelay)
            setRepeatCount(mShimmerConfig!!.repeatCount)
            setDuration(mShimmerConfig!!.animationDuration + mShimmerConfig!!.repeatDelay)
            addUpdateListener(mUpdateListener)
            if (started) {
                start()
            }
        }
    }

    private fun updateShader() {
        val bounds = bounds
        val boundsWidth = bounds.width()
        val boundsHeight = bounds.height()
        if (boundsWidth == 0 || boundsHeight == 0 || mShimmerConfig == null) {
            return
        }
        val width: Int = mShimmerConfig!!.width(boundsWidth)
        val height: Int = mShimmerConfig!!.height(boundsHeight)

        val shader: Shader
        when (mShimmerConfig?.shape) {
            AShape.LINEAR -> {
                val vertical = mShimmerConfig!!.direction == AShimmerDirection.TOP_TO_BOTTOM || mShimmerConfig!!.direction == AShimmerDirection.BOTTOM_TO_TOP
                val endX = if (vertical) 0 else width
                val endY = if (vertical) height else 0
                shader = LinearGradient(0f, 0f, endX.toFloat(), endY.toFloat(), mShimmerConfig!!.colors, mShimmerConfig!!.positions, Shader.TileMode.CLAMP)
            }

            AShape.RADIAL -> shader =
                RadialGradient(width / 2f, height / 2f, (max(width.toDouble(), height.toDouble()) / sqrt(2.0)).toFloat(), mShimmerConfig!!.colors, mShimmerConfig!!.positions, Shader.TileMode.CLAMP)

            else -> {
                val vertical = mShimmerConfig!!.direction == AShimmerDirection.TOP_TO_BOTTOM || mShimmerConfig!!.direction == AShimmerDirection.BOTTOM_TO_TOP
                val endX = if (vertical) 0 else width
                val endY = if (vertical) height else 0
                shader = LinearGradient(0f, 0f, endX.toFloat(), endY.toFloat(), mShimmerConfig!!.colors, mShimmerConfig!!.positions, Shader.TileMode.CLAMP)
            }
        }

        mShimmerPaint.setShader(shader)
    }
}
