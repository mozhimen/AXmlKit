package com.mozhimen.xmlk.viewk

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.annotation.IntDef
import com.mozhimen.basick.utilk.android.animation.cancel_removeAllListeners
import com.mozhimen.basick.utilk.android.graphics.applyBitmapAnyScaleRatio
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.util.dp2pxI
import com.mozhimen.basick.utilk.kotlin.intResDrawable2bitmapAny
import com.mozhimen.xmlk.bases.BaseViewK


/**
 * @ClassName ViewKProgressWave
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/7/8
 * @Version 1.0
 */
class ViewKProgressWave @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseViewK(context, attrs, defStyleAttr) {

    companion object {
        private const val WAVE_COLOR = -0x37ea437d//默认波纹颜色
        private const val STROKE_COLOR = -0x7ff7c71//默认外环颜色
        private const val STRETCH_FACTOR_A = 15f// y = Asin(wx+b)+h
//        private const val TRANSLATE_X_SPEED_BTM = 5// 第一条水波移动速度
//        private const val TRANSLATE_X_SPEED_TOP = 3// 第二条水波移动速度
        private const val ANIM_DURATION = 2000L
    }

    @IntDef(STYLE.STYLE_ICON, STYLE.STYLE_COLOR)
    annotation class STYLE {
        companion object {
            const val STYLE_COLOR: Int = 0
            const val STYLE_ICON: Int = 1
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    private var _style = STYLE.STYLE_COLOR
    private var _max = 100

    @Volatile
    private var _progress = 0
    private var _waveColor = WAVE_COLOR
    private var _waveHeight = STRETCH_FACTOR_A
    private var _waveNum = 1
    private var _strokeWidth = 2.dp2px()
    private var _strokeColor = STROKE_COLOR
    private var _src = 0

    ///////////////////////////////////////////////////////////////////////////////

    private lateinit var _wavePaint: Paint
    private lateinit var _borderPaint: Paint
    private lateinit var _iconPaint: Paint
    private lateinit var _wavePath: Path
    private var _moveAnimator: ValueAnimator? = null
    private var _waveWidth = 0f
    private var _width = 0
    private var _height = 0
    private var _moveDistance = 0f
    private val _percent: Float
        get() = _progress.toFloat() / _max
    private var _iconBitmap: Bitmap? = null//圆形遮罩

    ///////////////////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
        initPaint()
        initView()
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewKProgressWave)
        _style = typedArray.getInt(R.styleable.ViewKProgressWave_viewKProgressWave_style, _style)
        _max = typedArray.getInt(R.styleable.ViewKProgressWave_viewKProgressWave_max, _max)
        _progress = typedArray.getInt(R.styleable.ViewKProgressWave_viewKProgressWave_progress, _progress)
        _waveNum = typedArray.getColor(R.styleable.ViewKProgressWave_viewKProgressWave_waveNum, _waveNum)
        _waveColor = typedArray.getColor(R.styleable.ViewKProgressWave_viewKProgressWave_waveColor, _waveColor)
        _waveHeight = typedArray.getDimension(R.styleable.ViewKProgressWave_viewKProgressWave_waveHeight, _waveHeight)
        _strokeWidth = typedArray.getDimension(R.styleable.ViewKProgressWave_viewKProgressWave_strokeWidth, _strokeWidth)
        _strokeColor = typedArray.getColor(R.styleable.ViewKProgressWave_viewKProgressWave_strokeColor, _strokeColor)
        _src = typedArray.getResourceId(R.styleable.ViewKProgressWave_viewKProgressWave_src, _src)
        typedArray.recycle()
    }

    override fun initPaint() {
        _wavePaint = Paint()
        _wavePaint.isAntiAlias = true
        _wavePaint.style = Paint.Style.FILL
        _wavePaint.color = _waveColor
        _wavePaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))

        _borderPaint = Paint()
        _borderPaint.isAntiAlias = true
        _borderPaint.strokeWidth = _strokeWidth
        _borderPaint.color = _strokeColor
        _borderPaint.style = Paint.Style.STROKE

        if (_style == STYLE.STYLE_ICON) {
            _iconPaint = Paint()
            _iconPaint.isFilterBitmap = true
            _iconPaint.isAntiAlias = true
            _iconPaint.isDither = true
        }

        _wavePath = Path()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val maxSize = 150.dp2pxI()
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(maxSize, maxSize)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(maxSize, heightSize)
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, widthSize)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w < h) {
            _height = w
            _width = _height
        } else {
            _height = h
            _width = _height
        }

        _waveWidth = _width.toFloat() / _waveNum
        _moveDistance = _width.toFloat()

        if (_style == STYLE.STYLE_ICON&&_src!=0)
            generateIconBitmap()
    }

    override fun onDraw(canvas: Canvas) {
        if (_style == STYLE.STYLE_ICON && _iconBitmap != null) {
            canvas.drawBitmap(_iconBitmap!!, 0f, 0f, _iconPaint)
            canvas.drawPath(generateWavePath(), _wavePaint)
        }

        if (_strokeWidth > 0f)
            canvas.drawCircle(_width / 2f, _height / 2f, _width / 2f - _strokeWidth / 2f, _borderPaint) //绘制外环
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startWaveAnimMove()
    }

    override fun onDetachedFromWindow() {
        releaseWaveAnimMove()
        super.onDetachedFromWindow()
    }

    private fun startWaveAnimMove() {//波浪平移动画
        if (_moveAnimator == null) {
            _moveAnimator = ValueAnimator.ofFloat(1f, 0f)
            _moveAnimator!!.apply {
                setDuration(ANIM_DURATION)
                interpolator = LinearInterpolator()
                repeatCount = ValueAnimator.INFINITE //无限循环
                addUpdateListener { animation ->
                    _moveDistance = animation.animatedValue as Float * _width
                    postInvalidate()
                }
            }
            _moveAnimator!!.start()
        } else if (!_moveAnimator!!.isRunning || !_moveAnimator!!.isStarted) {
            _moveAnimator!!.start()
        }
    }

    private fun releaseWaveAnimMove() {
        _moveAnimator?.cancel_removeAllListeners()
        _moveAnimator = null
    }

    private fun generateWavePath(): Path {
        _wavePath.reset()

        _wavePath.moveTo(-_moveDistance, _height * (1 - _percent)) //起始点，y值为水流的高度

        //绘制多段波浪
        for (i in 0 until _waveNum * 2) {
            _wavePath.rQuadTo(_waveWidth / 2f, _waveHeight, _waveWidth, 0f)
            _wavePath.rQuadTo(_waveWidth / 2f, -_waveHeight, _waveWidth, 0f)
        }

        _wavePath.lineTo(_width.toFloat(), _width.toFloat())
        _wavePath.lineTo(0f, _width.toFloat())
        _wavePath.close()

        return _wavePath
    }

    private fun generateIconBitmap() {
        val icBitmap = _src.intResDrawable2bitmapAny(context)
        val scaleWidth = _width.toFloat() / icBitmap.width
        val scaleHeight = _height.toFloat() / icBitmap.height
        _iconBitmap = icBitmap.applyBitmapAnyScaleRatio(scaleWidth, scaleHeight)
    }

    ///////////////////////////////////////////////////////////////////////////////

    fun getMax(): Int =
        _max

    fun setMax(max: Int) {
        _max = max
        invalidate()
    }

    fun getProgress(): Int =
        _progress

    /**
     * 可在子线程中刷新
     */
    fun setProgress(progress: Int) {
        _progress = progress
        invalidate()
    }

    fun postProgress(progress: Int) {
        _progress = progress
        postInvalidate()
    }

    fun getStrokeWidth(): Float =
        _strokeWidth

    fun setStrokeWidth(strokeWidth: Float) {
        _strokeWidth = strokeWidth
        invalidate()
    }

    fun getStrokeColor(): Int =
        _strokeColor

    fun setStrokeColor(strokeColor: Int) {
        _borderPaint.color = strokeColor.also { _strokeColor = it }
        invalidate()
    }

    fun getWaveColor(): Int =
        _waveColor

    fun setWaveColor(wavePaintColor: Int) {
        _wavePaint.color = wavePaintColor.also { _waveColor = it }
        invalidate()
    }
}
