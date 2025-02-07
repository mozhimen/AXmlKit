package com.mozhimen.xmlk.imagek.mask

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.view.UtilKMotionEvent
import com.mozhimen.kotlin.utilk.android.view.UtilKViewWrapper
import com.mozhimen.kotlin.utilk.kotlin.math.UtilKMathPoint
import com.mozhimen.kotlin.utilk.kotlin.math.UtilKMathTriangle
import com.mozhimen.kotlin.utilk.kotlin.ranges.constraint
import com.mozhimen.kotlin.utilk.wrapper.UtilKRes
import com.mozhimen.xmlk.basic.commons.IViewK
import kotlin.math.max

/**
 * @ClassName ViewKImageMask
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2022/9/14 16:03
 * @Version 1.0
 */
class ImageKMask @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr), IViewK {

    //region
    private companion object {
        private const val MASK_TYPE = 1
        private val MASK_COLOR = UtilKRes.gainColor(com.mozhimen.xmlk.R.color.cok_blue_0177fd)
        private val BORDER_WIDTH = 2f.dp2px()
        private val DES_SHAKE_DISTANCE = 10f.dp2px()
    }

    private var _maskType = MASK_TYPE
    private var _maskColor = MASK_COLOR
    private var _borderWidth = BORDER_WIDTH
        set(value) {
            field = if (value <= 0f) 2f.dp2px() else value
        }

    private lateinit var _paint: Paint
    private var _isMoveOrScale = false
    private var _maskRect: MaskRect = MaskRect(0f, 0f, 0f, 0f)
    private var _centerX = 0f
    private var _centerY = 0f

    private val _viewKParentScrollable: ViewGroup?
        get() = UtilKViewWrapper.getParent_ofClazz(this, ScrollView::class.java, NestedScrollView::class.java, RecyclerView::class.java) as ViewGroup?
    //endregion

    init {
        initAttrs(attrs, defStyleAttr)
        initPaint()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        _centerX = w / 2f
        _centerY = h / 2f
        _maskRect = MaskRect(w * 0.5f, h * 0.5f, _centerX, _centerY)
    }

    override fun initFlag() {
    }

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageKMask)
        _maskType = typedArray.getInt(R.styleable.ImageKMask_imageKMask_maskType, _maskType)
        _maskColor = typedArray.getColor(R.styleable.ImageKMask_imageKMask_maskColor, _maskColor)
        _borderWidth = typedArray.getDimension(R.styleable.ImageKMask_imageKMask_borderWidth, _borderWidth)
        typedArray.recycle()
    }

    override fun initPaint() {
        _paint = Paint()
        _paint.color = _maskColor
        _paint.style = if (_maskType == 0) Paint.Style.FILL else Paint.Style.STROKE
        _paint.strokeWidth = _borderWidth
    }

    override fun initData() {

    }

    override fun initView() {
    }

    override fun onDraw(canvas: Canvas) {
        drawRect(canvas)
    }

    private var _lastFingerDistance = 0f
    private var _lastAnchorTwoFinger: Pair<Float, Float>? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                _viewKParentScrollable?.requestDisallowInterceptTouchEvent(false)
                _isMoveOrScale = false
                _lastAnchorTwoFinger = null
                _lastFingerDistance = 0f
            }

            MotionEvent.ACTION_DOWN -> {
                _isMoveOrScale = if (!_maskRect.isTapInArea(event)) {
                    return true
                } else {
                    true
                }
                _viewKParentScrollable?.requestDisallowInterceptTouchEvent(true)
            }

            MotionEvent.ACTION_MOVE -> {
                if (_isMoveOrScale) {
                    if (event.pointerCount == 1) {
                        val ax = event.getX(0)
                        val ay = event.getY(0)
                        _lastAnchorTwoFinger = updateLocSingleFinger(ax, ay)
                    } else if (event.pointerCount == 2) {
                        val ax = event.getX(0)
                        val ay = event.getY(0)
                        val bx = event.getX(1)
                        val by = event.getY(1)
                        _lastAnchorTwoFinger = updateLocTwoFinger(ax, ay, bx, by)
                        _lastFingerDistance = updateScale(ax, ay, bx, by)
                        //还可添加旋转
                    }
                    postInvalidate()
                }
            }
        }
        return true
    }

    private fun updateLocSingleFinger(ax: Float, ay: Float): Pair<Float, Float> {
        if (_lastAnchorTwoFinger == null) return ax to ay
        val distance = UtilKMathPoint.distance(ax, ay, _lastAnchorTwoFinger!!.first, _lastAnchorTwoFinger!!.second)
        if (distance > DES_SHAKE_DISTANCE) return ax to ay
        val changeX = ax - _lastAnchorTwoFinger!!.first
        val changeY = ay - _lastAnchorTwoFinger!!.second
        _maskRect.centerX = (_maskRect.centerX + changeX).constraint(0f, width.toFloat())
        _maskRect.centerY = (_maskRect.centerY + changeY).constraint(0f, height.toFloat())
        return ax to ay
    }

    private fun updateLocTwoFinger(ax: Float, ay: Float, bx: Float, by: Float): Pair<Float, Float> {
        val point = UtilKMathPoint.center(ax, ay, bx, by)
        if (_lastAnchorTwoFinger == null) return point
        val distance = UtilKMathPoint.distance(point.first, point.second, _lastAnchorTwoFinger!!.first, _lastAnchorTwoFinger!!.second)
        if (distance > DES_SHAKE_DISTANCE) return point
        val changeX = point.first - _lastAnchorTwoFinger!!.first
        val changeY = point.second - _lastAnchorTwoFinger!!.second
        _maskRect.centerX = (_maskRect.centerX + changeX).constraint(0f, width.toFloat())
        _maskRect.centerY = (_maskRect.centerY + changeY).constraint(0f, height.toFloat())
        return point
    }

    private fun updateScale(ax: Float, ay: Float, bx: Float, by: Float): Float {
        val fingerDistance = UtilKMathPoint.distance(ax, ay, bx, by)
        if (_lastFingerDistance == 0f || _lastAnchorTwoFinger == null) return fingerDistance
        //夹角计算
        //|  /
        //| /
        //|/计算缩放width还是height
        val angle: Float = if (ax != bx) {
            val rightX = max(ax, bx)
            val rightY = if (rightX == ax) ay else by
            if (rightY == _lastAnchorTwoFinger!!.second) {
                90f
            } else {
                val opposite = rightX - _lastAnchorTwoFinger!!.first
                val hypotenuse = UtilKMathPoint.distance(rightX, rightY, _lastAnchorTwoFinger!!.first, _lastAnchorTwoFinger!!.second)
                val tempAngle = UtilKMathTriangle.angleSin(opposite, hypotenuse)
                if (rightY < _lastAnchorTwoFinger!!.second) {
                    tempAngle
                } else {
                    180f - tempAngle
                }
            }
        } else {
            0f
        }.constraint(0f, 180f)
        val scale: Float = fingerDistance / _lastFingerDistance
        when (angle) {
            in 0f..15f, in 165f..180f -> {
                _maskRect.height = (_maskRect.height * scale).constraint(0f, height.toFloat())
            }
            in 30f..60f -> {
                _maskRect.width = (_maskRect.width * scale).constraint(0f, width.toFloat())
            }
            else -> {
                _maskRect.height = (_maskRect.height * scale).constraint(0f, height.toFloat())
                _maskRect.width = (_maskRect.width * scale).constraint(0f, width.toFloat())
            }
        }
        return fingerDistance
    }

    private fun drawRect(canvas: Canvas) {
        canvas.drawRect(_maskRect.leftX, _maskRect.topY, _maskRect.rightX, _maskRect.bottomY, _paint)
    }

    private class MaskRect {
        var width: Float = 0f
            set(value) {
                field = value
                refreshX()
            }
        var height: Float = 0f
            set(value) {
                field = value
                refreshY()
            }
        var centerX: Float = 0f
            set(value) {
                field = value
                refreshX()
            }
        var centerY: Float = 0f
            set(value) {
                field = value
                refreshY()
            }

        var widthHalf: Float = 0f
        var heightHalf: Float = 0f
        var leftX: Float = 0f
        var rightX: Float = 0f
        var topY: Float = 0f
        var bottomY: Float = 0f

        constructor(width: Float, height: Float, centerX: Float, centerY: Float) {
            this.width = width
            this.height = height
            this.centerX = centerX
            this.centerY = centerY
        }

        fun refreshX() {
            widthHalf = width / 2f
            leftX = centerX - widthHalf
            rightX = centerX + widthHalf
        }

        fun refreshY() {
            heightHalf = height / 2f
            topY = centerY - heightHalf
            bottomY = centerY + heightHalf
        }

        fun isTapInArea(e: MotionEvent): Boolean =
            UtilKMotionEvent.isTapInArea(e, leftX, topY, rightX, bottomY)
    }
}