package com.mozhimen.xmlk.viewk

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import androidx.annotation.IntDef
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
        private const val TRANSLATE_X_SPEED_BTM = 5// 第一条水波移动速度
        private const val TRANSLATE_X_SPEED_TOP = 3// 第二条水波移动速度
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
    private var _width = 0
    private var _height = 0

    //    private var _maskBitmap: Bitmap? = null//圆形遮罩
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

        if (_style == STYLE.STYLE_ICON)
            createBallBitmap()

    }

    override fun onDraw(canvas: Canvas) {
    }

    private fun createBallBitmap() {
//        _maskBitmap = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(_maskBitmap!!)
//        canvas.drawCircle(_width / 2f, _height / 2f, _width / 2f - _strokeWidth * 3f / 2f, _iconPaint)
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
