package com.mozhimen.xmlk.layoutk.guide

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.animation.ValueAnimator.INFINITE
import android.animation.ValueAnimator.ofFloat
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.mozhimen.kotlin.utilk.android.animation.cancel_removeAll_AllUpdateListeners
import com.mozhimen.kotlin.utilk.android.animation.removeAll_AllUpdateListeners
import com.mozhimen.xmlk.layoutk.guide.GuideProcess.Companion.DEFAULT_OVERLAY_COLOR

/**
 * [LayoutKGuide] starts/finishes [GuideProcess], and starts/finishes a current [GuideStep].
 */
internal class LayoutKGuide @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    @ColorInt
    private var _overlayBackgroundColor: Int = DEFAULT_OVERLAY_COLOR

    private val _overlayBackgroundPaint by lazy {
        Paint().apply { color = _overlayBackgroundColor }
    }

    private val _shapePaint by lazy {
        Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    }

    private val _effectPaint by lazy { Paint() }

    private val _invalidator = AnimatorUpdateListener { invalidate() }

    private var _valueAnimatorShape: ValueAnimator? = null
    private var _valueAnimatorEffect: ValueAnimator? = null
    private var _guideStep: GuideStep? = null

    ///////////////////////////////////////////////////////////////////////

    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_HARDWARE, null)
    }

    ///////////////////////////////////////////////////////////////////////

    fun setOverlayBackgroundColor(backgroundColor: Int) {
        _overlayBackgroundColor = backgroundColor
    }

    /**
     * Starts [GuideProcess].
     */
    fun startProcess(duration: Long, interpolator: TimeInterpolator, listener: Animator.AnimatorListener) {
        val objectAnimator = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).apply {
            setDuration(duration)
            setInterpolator(interpolator)
            addListener(listener)
        }
        objectAnimator.start()
    }

    /**
     * Finishes [GuideProcess].
     */
    fun finishProcess(duration: Long, interpolator: TimeInterpolator, listener: Animator.AnimatorListener) {
        val objectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f).apply {
            setDuration(duration)
            setInterpolator(interpolator)
            addListener(listener)
        }
        objectAnimator.start()
    }

    /**
     * Starts the provided [GuideStep].
     */
    fun startStep(guideStep: GuideStep) {
        removeAllViews()
        addView(guideStep.overlay, MATCH_PARENT, MATCH_PARENT)
        _guideStep = guideStep.apply {
            // adjust anchor in case where custom container is set.
            val location = IntArray(2)
            getLocationInWindow(location)
            val offset = PointF(location[0].toFloat(), location[1].toFloat())
            anchor.offset(-offset.x, -offset.y)
        }
        _valueAnimatorShape?.cancel_removeAll_AllUpdateListeners()
        _valueAnimatorShape = ofFloat(0f, 1f).apply {
            duration = guideStep.shape.duration
            interpolator = guideStep.shape.timeInterpolator
            addUpdateListener(_invalidator)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    removeAll_AllUpdateListeners()
                }

                override fun onAnimationCancel(animation: Animator) {
                    removeAll_AllUpdateListeners()
                }
            })
        }
        _valueAnimatorEffect?.cancel_removeAll_AllUpdateListeners()
        _valueAnimatorEffect = ofFloat(0f, 1f).apply {
            startDelay = guideStep.shape.duration
            duration = guideStep.effect.duration
            interpolator = guideStep.effect.timeInterpolator
            repeatMode = guideStep.effect.repeatMode
            repeatCount = INFINITE
            addUpdateListener(_invalidator)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    removeAll_AllUpdateListeners()
                }

                override fun onAnimationCancel(animation: Animator) {
                    removeAll_AllUpdateListeners()
                }
            })
        }
        _valueAnimatorShape?.start()
        _valueAnimatorEffect?.start()
    }

    /**
     * Finishes the current [GuideStep].
     */
    fun finishStep(listener: Animator.AnimatorListener) {
        val currentTarget = _guideStep ?: return
        val currentAnimatedValue = _valueAnimatorShape?.animatedValue ?: return
        _valueAnimatorShape?.cancel_removeAll_AllUpdateListeners()
        _valueAnimatorShape = ofFloat(currentAnimatedValue as Float, 0f).apply {
            duration = currentTarget.shape.duration
            interpolator = currentTarget.shape.timeInterpolator
            addUpdateListener(_invalidator)
            addListener(listener)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    removeAll_AllUpdateListeners()
                }

                override fun onAnimationCancel(animation: Animator) {
                    removeAll_AllUpdateListeners()
                }
            })
        }
        _valueAnimatorEffect?.cancel_removeAll_AllUpdateListeners()
        _valueAnimatorEffect = null
        _valueAnimatorShape?.start()
    }

    fun release() {
        _valueAnimatorEffect?.cancel_removeAll_AllUpdateListeners()
        _valueAnimatorEffect = null
        _valueAnimatorShape?.cancel_removeAll_AllUpdateListeners()
        _valueAnimatorShape = null
        removeAllViews()
    }

    ///////////////////////////////////////////////////////////////////////

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), _overlayBackgroundPaint)
        val currentTarget = _guideStep
        val currentShapeAnimator = _valueAnimatorShape
        val currentEffectAnimator = _valueAnimatorEffect
        if (currentTarget != null && currentEffectAnimator != null && currentShapeAnimator != null && !currentShapeAnimator.isRunning) {
            currentTarget.effect.draw(
                canvas = canvas,
                point = currentTarget.anchor,
                value = currentEffectAnimator.animatedValue as Float,
                paint = _effectPaint
            )
        }
        if (currentTarget != null && currentShapeAnimator != null) {
            currentTarget.shape.draw(
                canvas = canvas,
                point = currentTarget.anchor,
                value = currentShapeAnimator.animatedValue as Float,
                paint = _shapePaint
            )
        }
    }
}
