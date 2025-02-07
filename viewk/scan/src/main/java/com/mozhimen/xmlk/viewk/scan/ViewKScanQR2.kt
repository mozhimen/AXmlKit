package com.mozhimen.xmlk.viewk.scan

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.util.dp2pxI
import com.mozhimen.xmlk.basic.bases.BaseViewK

/**
 * @ClassName QRScanView
 * @Description TODO
 * @Author Kolin Zhao
 * @Date 2021/6/21 14:01
 * @Version 1.0
 */
class ViewKScanQR2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    BaseViewK(context, attrs, defStyleAttr) {

    //region # variate
    private var _borderWidth = 1f.dp2px()
    private var _borderColor = 0xFFFFFF
    private var _lineDrawable: Drawable? = null
    private var _lineWidth:Int = 2f.dp2pxI()
    private var _lineColor = 0xFFFFFF
    private var _animTime = 1000
    private var _distance = 2f.dp2pxI()
    private var _isLineReverse = false
    private var _successDrawable = R.drawable.viewk_qr_scan_success
    private var _successDrawableSize = 20f.dp2px()
    private var _successAnimTime = 1000
    private var _iScanListener: IScanListener? = null
    private var _logoBitmap: Bitmap? = null
    private var _lineBitmap: Bitmap? = null

    private lateinit var _linePaint: Paint
    private lateinit var _rectPaint: Paint
    private lateinit var _frameRect: RectF

    private var _moveStep:Int = _distance
    private val _moveRate = 3f
    private var _distanceSuccess = _distance * _moveRate
    private var _animInterval: Float = _animTime * _distance / 20f.dp2px()
    private var _lineScaleInterval: Float = _successAnimTime * _distanceSuccess * 2f / 60f
    private var _logoScaleInterval: Float =
        _successAnimTime * _distanceSuccess * 2f / _successDrawableSize

    private var _isSuccess = false
    private var _lineTopOffset:Float =0f
    private var _lineScale = 0f
    private var _logoScale = 0f
    //endregion

    interface IScanListener {
        fun onAnimEnd()
    }

    fun setScanCallback(listener: IScanListener) {
        _iScanListener = listener
    }

    fun requireSuccess() {
        _isSuccess = true
        _iScanListener?.onAnimEnd()
    }

    //region # private function
    init {
        initAttrs(attrs, defStyleAttr)
        initPaint()
    }

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewKScanQR2)
        _borderWidth =
            typedArray.getDimension(R.styleable.ViewKScanQR2_viewKScanQR2_borderWidth, _borderWidth)
        _borderColor =
            typedArray.getColor(R.styleable.ViewKScanQR2_viewKScanQR2_borderColor, _borderColor)
        _lineDrawable =
            typedArray.getDrawable(R.styleable.ViewKScanQR2_viewKScanQR2_lineDrawable)
        _lineWidth =
            typedArray.getDimensionPixelSize(R.styleable.ViewKScanQR2_viewKScanQR2_lineWidth, _lineWidth)
        _lineColor =
            typedArray.getColor(R.styleable.ViewKScanQR2_viewKScanQR2_lineColor, _lineColor)
        _animTime =
            typedArray.getInteger(R.styleable.ViewKScanQR2_viewKScanQR2_animTime, _animTime)
        _distance =
            typedArray.getDimensionPixelSize(R.styleable.ViewKScanQR2_viewKScanQR2_distance, _distance)
        _isLineReverse =
            typedArray.getBoolean(R.styleable.ViewKScanQR2_viewKScanQR2_isLineReverse, _isLineReverse)
        _successDrawable =
            typedArray.getResourceId(R.styleable.ViewKScanQR2_viewKScanQR2_isLineReverse, _successDrawable)
        _successDrawableSize =
            typedArray.getDimension(R.styleable.ViewKScanQR2_viewKScanQR2_successDrawableSize, _successDrawableSize)
        _successAnimTime =
            typedArray.getInteger(R.styleable.ViewKScanQR2_viewKScanQR2_successAnimTime, _successAnimTime)
        typedArray.recycle()

        _lineDrawable?.let {
            _lineBitmap = (it as BitmapDrawable).bitmap
        }
        _logoBitmap = (ContextCompat.getDrawable(context, _successDrawable) as BitmapDrawable).bitmap
    }

    override fun initPaint() {
        _linePaint = Paint()
        _linePaint.style = Paint.Style.STROKE
        _linePaint.color = _lineColor
        _linePaint.strokeCap = Paint.Cap.ROUND
        _linePaint.strokeWidth = _lineWidth.toFloat()

        _rectPaint = Paint()
        _rectPaint.style = Paint.Style.STROKE
        _rectPaint.color = _borderColor
        _rectPaint.strokeWidth = _borderWidth
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initData()
    }

    override fun initData() {
        _moveStep = _distance
        _distanceSuccess = _distance * _moveRate
        _animInterval = _animTime * _distance / height.toFloat()
        _lineScaleInterval = _successAnimTime * _distanceSuccess / realRadius
        _logoScaleInterval = _successAnimTime * _distanceSuccess * 2f / _successDrawableSize

        _frameRect = RectF(
            centerX.toInt() - realRadius.toInt() + _borderWidth,
            centerY.toInt() - realRadius.toInt() + _borderWidth,
            centerX.toInt() + realRadius.toInt() - _borderWidth,
            centerY.toInt() + realRadius.toInt() - _borderWidth
        )
    }

    override fun onDraw(canvas: Canvas) {
        drawBorderLine(canvas)
        if (!_isSuccess) {
            drawScanLine(canvas)
            moveScanLine()
        } else {
            scaleScanLine(canvas)
            scaleSuccessLogo(canvas)
            changeScaleStep()
        }
    }

    private fun scaleScanLine(canvas: Canvas) {
        if (_lineScale <= realRadius) {
            _lineBitmap?.let {
                val lineRect = RectF(
                    _frameRect.left + _lineScale, _lineTopOffset,
                    _frameRect.right - _lineScale, _lineTopOffset + it.height
                )
                canvas.drawBitmap(it, null, lineRect, _linePaint)
            } ?: kotlin.run {
                val borderRect = RectF(
                    _frameRect.left + _lineScale.toInt(), _lineTopOffset,
                    _frameRect.right - _lineScale.toInt(), (_lineTopOffset + _lineWidth)
                )
                canvas.drawRect(borderRect, _linePaint)
            }
        }
    }

    private fun scaleSuccessLogo(canvas: Canvas) {
        if (_lineScale >= realRadius) {
            val logoRect = RectF(
                width.toFloat() / 2 - _logoScale, height.toFloat() / 2 - _logoScale,
                width.toFloat() / 2 + _logoScale, height.toFloat() / 2 + _logoScale
            )
            canvas.drawBitmap(_logoBitmap!!, null, logoRect, _linePaint)
        }
    }

    private fun changeScaleStep() {
        if (_lineScale >= realRadius && _logoScale <= _successDrawableSize / 2) {
            _logoScale += _distanceSuccess
            postInvalidateDelayed(
                _logoScaleInterval.toLong(),
                _frameRect.left.toInt(),
                _frameRect.top.toInt(),
                _frameRect.right.toInt(),
                _frameRect.bottom.toInt()
            )
        } else if (_lineScale <= realRadius) {
            _lineScale += _distanceSuccess
            postInvalidateDelayed(
                _lineScaleInterval.toLong(),
                _frameRect.left.toInt(),
                _frameRect.top.toInt(),
                _frameRect.right.toInt(),
                _frameRect.bottom.toInt()
            )
        }
    }

    //画边框线
    private fun drawBorderLine(canvas: Canvas) {
        canvas.drawRect(_frameRect, _rectPaint)
    }

    //画扫描线
    private fun drawScanLine(canvas: Canvas) {
        _lineBitmap?.let {
            val lineRect = RectF(
                _frameRect.left, _lineTopOffset,
                _frameRect.right, _lineTopOffset + it.height.toFloat()
            )
            canvas.drawBitmap(it, null, lineRect, _linePaint)
        } ?: kotlin.run {
            val borderRect = RectF(
                _frameRect.left, _lineTopOffset,
                _frameRect.right, _lineTopOffset + _lineWidth
            )
            canvas.drawRect(borderRect, _linePaint)
        }
    }

    //移动扫描线的位置
    private fun moveScanLine() {
        // 处理非网格扫描图片的情况
        _lineTopOffset += _moveStep
        var scanLineSize = 0
        _lineBitmap?.let {
            scanLineSize = it.height
        } ?: kotlin.run {
            scanLineSize = _lineWidth
        }
        if (_isLineReverse) {
            if (_lineTopOffset + scanLineSize >= _frameRect.bottom || _lineTopOffset < _frameRect.top) {
                _moveStep = -_moveStep
            }
        } else {
            if (_lineTopOffset + scanLineSize >= _frameRect.bottom) {
                _lineTopOffset = _frameRect.top
            }
        }
        postInvalidateDelayed(
            _animInterval.toLong(),
            0,
            0,
            width,
            height
        )
    }
    //#endregion
}