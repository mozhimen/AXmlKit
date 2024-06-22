package com.mozhimen.xmlk.layoutk.slider

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.*
import android.widget.ScrollView
import androidx.annotation.FloatRange
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.basick.utilk.android.view.UtilKView
import com.mozhimen.basick.utilk.android.view.UtilKViewWrapper
import com.mozhimen.xmlk.bases.BaseLayoutKFrame
import com.mozhimen.xmlk.layoutk.slider.commons.ILayoutKSlider
import com.mozhimen.xmlk.layoutk.slider.commons.ISliderScrollListener
import com.mozhimen.xmlk.layoutk.slider.helpers.LayoutKSliderDelegate
import com.mozhimen.xmlk.layoutk.slider.mos.MRod
import com.mozhimen.xmlk.layoutk.slider.mos.MSlider
import com.mozhimen.basick.utilk.kotlin.UtilKLazyJVM.lazy_ofNone

/**
 * @ClassName LayoutKSlider
 * @Description
 *
 *  minLabel               maxLabel LabelTop
 *  minVal    currentVal    maxVal  ValTop
 *
 *               rod    section
 * |----------<======>----||-----| Slider
 *           ___/\___
 *          |  tip  |             Bubble
 *
 *  minLabel               maxLabel LabelBottom
 *  minVal    currentVal    maxVal  ValBottom
 *
 * @Author Kolin Zhao / Mozhimen
 * @Version 1.0
 */
class LayoutKSlider @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseLayoutKFrame(context, attrs, defStyleAttr), ILayoutKSlider {

    private val _layoutKSliderDelegate: LayoutKSliderDelegate by lazy_ofNone { LayoutKSliderDelegate(context) }

    init {
        if (!isInEditMode) {
            setWillNotDraw(false)
            _layoutKSliderDelegate.init(this)
            initAttrs(attrs, defStyleAttr)
            initPaint()
            initView()
            minimumHeight = _layoutKSliderDelegate.getHeightMeasureSpec()
        }
    }

    val rod: MRod
        get() = _layoutKSliderDelegate.getRod()

    val slider: MSlider
        get() = _layoutKSliderDelegate.getSlider()

    fun setSliderListener(sliderListener: ISliderScrollListener) {
        _layoutKSliderDelegate.setSliderListener(sliderListener)
    }

    fun setRodDefaultPercent(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
        _layoutKSliderDelegate.setRodDefaultPercent(percent)
    }

    override fun updateRodPercent(@FloatRange(from = 0.0, to = 1.0) percent: Float) {
        _layoutKSliderDelegate.updateRodPercent(percent)
    }

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        _layoutKSliderDelegate.initAttrs(attrs, defStyleAttr)
    }

    private fun initPaint() {
        _layoutKSliderDelegate.initPaint()
    }

    override fun initView() {
        _layoutKSliderDelegate.initView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (!isInEditMode) {
            val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(_layoutKSliderDelegate.getHeightMeasureSpec(), MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!isInEditMode) {
            _layoutKSliderDelegate.refreshView()
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (!isInEditMode) {
            _layoutKSliderDelegate.onDraw(canvas)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (!isInEditMode) {
            _layoutKSliderDelegate.onTouchEvent(event)
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!isInEditMode) {
            _layoutKSliderDelegate.attachScrollableParentView(UtilKViewWrapper.getParent_ofClazz(this, ScrollView::class.java, NestedScrollView::class.java, RecyclerView::class.java) as ViewGroup?)
        }
    }
}
