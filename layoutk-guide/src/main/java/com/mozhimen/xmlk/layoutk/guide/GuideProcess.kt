package com.mozhimen.xmlk.layoutk.guide

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.mozhimen.xmlk.layoutk.guide.commons.IOnProcessEventListener
import java.util.concurrent.TimeUnit

/**
 * Holds all of the [GuideStep]s and [LayoutKGuide] to show/hide [GuideStep], [LayoutKGuide] properly.
 * [LayoutKGuide] can be controlled with [start]/[finish].
 * All of the [GuideStep]s can be controlled with [next]/[previous]/[show].
 *
 * Once you finish the current [GuideProcess] with [finish], you can not start the [GuideProcess] again
 * unless you create a new [GuideProcess] to start again.
 */
class GuideProcess private constructor(
    private val _layoutKGuide: LayoutKGuide,
    private val _guideSteps: Array<GuideStep>,
    private val _duration: Long,
    private val _timeInterpolator: TimeInterpolator,
    private val _container: ViewGroup,
    private val _iOnProcessEventListener: IOnProcessEventListener?,
) {

    companion object {
        const val NO_POSITION = -1

        @ColorInt
        const val DEFAULT_OVERLAY_COLOR: Int = 0x6000000
    }

    //////////////////////////////////////////////////////////////////

    private var currentIndex = NO_POSITION

    //////////////////////////////////////////////////////////////////

    init {
        _container.addView(_layoutKGuide, MATCH_PARENT, MATCH_PARENT)
    }

    //////////////////////////////////////////////////////////////////

    /**
     * Starts [LayoutKGuide] and show the first [GuideStep].
     */
    fun start() {
        startProcess()
    }

    /**
     * Closes the current [GuideStep] if exists, and shows a [GuideStep] at the specified [index].
     * If target is not found at the [index], it will throw an exception.
     */
    fun show(index: Int) {
        showStep(index)
    }

    /**
     * Closes the current [GuideStep] if exists, and shows the next [GuideStep].
     * If the next [GuideStep] is not found, Spotlight will finish.
     */
    fun next() {
        showStep(currentIndex + 1)
    }

    /**
     * Closes the current [GuideStep] if exists, and shows the previous [GuideStep].
     * If the previous target is not found, it will throw an exception.
     */
    fun previous() {
        showStep(currentIndex - 1)
    }

    /**
     * Closes Spotlight and [LayoutKGuide] will remove all children and be removed from the [_container].
     */
    fun finish() {
        finishProcess()
    }

    //////////////////////////////////////////////////////////////////

    /**
     * Starts Spotlight.
     */
    private fun startProcess() {
        _layoutKGuide.startProcess(_duration, _timeInterpolator, object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                _iOnProcessEventListener?.onStart()
            }

            override fun onAnimationEnd(animation: Animator) {
                showStep(0)
            }
        })
    }

    /**
     * Closes the current [GuideStep] if exists, and show the [GuideStep] at [index].
     */
    private fun showStep(index: Int) {
        if (currentIndex == NO_POSITION) {
            val target = _guideSteps[index]
            currentIndex = index
            _layoutKGuide.startStep(target)
            target.iOnStepEventListener?.onStart()
        } else {
            _layoutKGuide.finishStep(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    val previousIndex = currentIndex
                    val previousTarget = _guideSteps[previousIndex]
                    previousTarget.iOnStepEventListener?.onEnd()
                    if (index < _guideSteps.size) {
                        val step = _guideSteps[index]
                        currentIndex = index
                        _layoutKGuide.startStep(step)
                        step.iOnStepEventListener?.onStart()
                    } else {
                        finishProcess()
                    }
                }
            })
        }
    }

    /**
     * Closes Spotlight.
     */
    private fun finishProcess() {
        _layoutKGuide.finishProcess(_duration, _timeInterpolator, object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                _layoutKGuide.release()
                _container.removeView(_layoutKGuide)
                _iOnProcessEventListener?.onEnd()
            }
        })
    }

    //////////////////////////////////////////////////////////////////

    /**
     * Builder to build [GuideProcess].
     * All parameters should be set in this [Builder].
     */
    class Builder {

        private var _guideSteps: Array<GuideStep>? = null
        private var _duration: Long = TimeUnit.SECONDS.toMillis(1)
        private var _interpolator: TimeInterpolator = DecelerateInterpolator(2f)

        @ColorInt
        private var _backgroundColor: Int = DEFAULT_OVERLAY_COLOR
        private var _container: ViewGroup? = null
        private var _iOnProcessEventListener: IOnProcessEventListener? = null

        /**
         * Sets [GuideStep]s to show on [GuideProcess].
         */
        fun setSteps(vararg guideSteps: GuideStep): Builder = apply {
            require(guideSteps.isNotEmpty()) { "targets should not be empty. " }
            this._guideSteps = arrayOf(*guideSteps)
        }

        /**
         * Sets [GuideStep]s to show on [GuideProcess].
         */
        fun setSteps(guideSteps: List<GuideStep>): Builder = apply {
            require(guideSteps.isNotEmpty()) { "targets should not be empty. " }
            this._guideSteps = guideSteps.toTypedArray()
        }

        /**
         * Sets [duration] to start/finish [GuideProcess].
         */
        fun setDuration(duration: Long): Builder = apply {
            this._duration = duration
        }

        /**
         * Sets [_backgroundColor] resource on [GuideProcess].
         */
        fun setBackgroundColorRes(@ColorRes backgroundColorRes: Int, context: Context): Builder = apply {
            this._backgroundColor = ContextCompat.getColor(context, backgroundColorRes)
        }

        /**
         * Sets [backgroundColor] on [GuideProcess].
         */
        fun setBackgroundColor(@ColorInt backgroundColor: Int): Builder = apply {
            this._backgroundColor = backgroundColor
        }

        /**
         * Sets [interpolator] to start/finish [GuideProcess].
         */
        fun setTimeInterpolator(interpolator: TimeInterpolator): Builder = apply {
            this._interpolator = interpolator
        }

        /**
         * Sets [container] to hold [LayoutKGuide]. DecoderView will be used if not specified.
         */
        fun setContainer(container: ViewGroup) = apply {
            this._container = container
        }

        /**
         * Sets [IOnProcessEventListener] to notify the state of [GuideProcess].
         */
        fun setOnProcessEventListener(listener: IOnProcessEventListener): Builder = apply {
            this._iOnProcessEventListener = listener
        }

        fun build(activity: Activity): GuideProcess {
            val spotlight = LayoutKGuide(activity, null, 0).apply {
                setOverlayBackgroundColor(_backgroundColor)
            }
            val targets = requireNotNull(_guideSteps) { "targets should not be null. " }
            val container = _container ?: activity.window.decorView as ViewGroup
            return GuideProcess(
                _layoutKGuide = spotlight,
                _guideSteps = targets,
                _duration = _duration,
                _timeInterpolator = _interpolator,
                _container = container,
                _iOnProcessEventListener = _iOnProcessEventListener
            )
        }
    }
}
