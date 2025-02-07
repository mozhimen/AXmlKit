package com.mozhimen.xmlk.viewk.effect

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.xmlk.bases.BaseViewK
import com.mozhimen.xmlk.commons.IViewKAction

/**
 * @ClassName Radar
 * @Description TODO
 * @Author Kolin Zhao / Mozhimen
 * @Date 2021/12/7 20:11
 * @Version 1.0
 */
class ViewKRadarRipple @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    BaseViewK(context, attrs, defStyleAttr), IViewKAction {

    //region # variate
    private var _bgColor = 0xFFFFFF
    private var _radarColor = 0x287FF1
    private var _lineColor = 0xFFFFFF
    private var _lineAlpha = 100//0-255
    private var _lineWidth = 2f.dp2px()
    private var _lineCount = 4
    private var _moveAngleStep = 3
    private var _animTime = 3000

    private lateinit var _radarPaint: Paint
    private lateinit var _bgPaint: Paint
    private lateinit var _circlePaint: Paint

    private var _interval = _animTime * _moveAngleStep / 360
    private val _radarMatrix = Matrix()
    private var _isStop = false
    //endregion

    override fun requireStart() {
        _isStop = false
        postInvalidate()
    }

    override fun requireStop() {
        _isStop = true
        postInvalidate()
    }

    //region # private function
    init {
        initAttrs(attrs, defStyleAttr)
    }

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewKRadarRipple)
        _bgColor =
            typedArray.getColor(R.styleable.ViewKRadarRipple_viewKRadarRipple_bgColor, _bgColor)
        _radarColor =
            typedArray.getColor(R.styleable.ViewKRadarRipple_viewKRadarRipple_radarColor, _radarColor)
        _lineColor =
            typedArray.getColor(R.styleable.ViewKRadarRipple_viewKRadarRipple_lineColor, _lineColor)
        _lineAlpha =
            typedArray.getInteger(R.styleable.ViewKRadarRipple_viewKRadarRipple_lineAlpha, _lineAlpha)
        _lineWidth =
            typedArray.getDimension(R.styleable.ViewKRadarRipple_viewKRadarRipple_lineWidth, _lineWidth)
        _lineCount =
            typedArray.getInteger(R.styleable.ViewKRadarRipple_viewKRadarRipple_lineCount, _lineCount)
        _moveAngleStep =
            typedArray.getInteger(R.styleable.ViewKRadarRipple_viewKRadarRipple_angleStep, _moveAngleStep)
        _animTime =
            typedArray.getInteger(R.styleable.ViewKRadarRipple_viewKRadarRipple_animTime, _animTime)
        typedArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initData()
        initPaint()
    }

    override fun initData() {
        _interval = _animTime * _moveAngleStep / 360
    }

    override fun initPaint() {
        super.initPaint()
        _bgPaint = Paint()
        _bgPaint.style = Paint.Style.FILL
        _bgPaint.isAntiAlias = true
        _bgPaint.color = _bgColor

        _circlePaint = Paint()
        _circlePaint.style = Paint.Style.STROKE
        _circlePaint.strokeWidth = _lineWidth
        _circlePaint.isAntiAlias = true
        _circlePaint.color = _lineColor
        _circlePaint.alpha = _lineAlpha

        _radarPaint = Paint()
        _radarPaint.style = Paint.Style.FILL_AND_STROKE
        _radarPaint.isAntiAlias = true
        _radarPaint.shader = SweepGradient(realRadius, realRadius, intArrayOf(Color.TRANSPARENT, _radarColor), null)
    }

    override fun onDraw(canvas: Canvas) {
        drawBg(canvas)
        drawCircle(canvas)
        drawRadar(canvas)
        rotateAngle()
    }

    private fun drawBg(canvas: Canvas) {
        canvas.drawCircle(centerX, centerY, realRadius, _bgPaint)
    }

    private fun drawRadar(canvas: Canvas) {
        canvas.concat(_radarMatrix)
        canvas.drawCircle(centerX, centerY, realRadius, _radarPaint)
    }

    private fun drawCircle(canvas: Canvas) {
        (1.._lineCount).forEach {
            val radius = realRadius / (_lineCount + 1) * it
            canvas.drawCircle(centerX, centerY, radius, _circlePaint)
        }
    }

    private fun rotateAngle() {
        _radarMatrix.postRotate(_moveAngleStep.toFloat(), centerX, centerY)
        if (!_isStop) {
            postInvalidateDelayed(_interval.toLong())
        }
    }
    //endregion
}