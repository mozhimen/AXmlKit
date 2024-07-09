package com.mozhimen.xmlk.viewk

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.DrawFilter
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.Path
import android.util.AttributeSet
import androidx.annotation.IntDef
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.util.dp2pxI
import com.mozhimen.xmlk.bases.BaseViewK
import kotlin.math.sin


/**
 * @ClassName ViewKProgressWave
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/7/8
 * @Version 1.0
 */
class ViewKProgressWave @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseViewK(context, attrs, defStyleAttr) {

    companion object {
        private const val WAVE_PAINT_COLOR = -0x37ea437d//默认波纹颜色
        private const val OUTER_RING_COLOR = -0x7ff7c71//默认外环颜色
        private const val STRETCH_FACTOR_A = 15f// y = Asin(wx+b)+h
        private const val TRANSLATE_X_SPEED_ONE = 5// 第一条水波移动速度
        private const val TRANSLATE_X_SPEED_TWO = 3// 第二条水波移动速度
    }

    @IntDef(STYLE.STYLE_ICON, STYLE.STYLE_COLOR)
    annotation class STYLE {
        companion object {
            const val STYLE_COLOR: Int = 0
            const val STYLE_ICON: Int = 1
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    private var _mode = STYLE.STYLE_COLOR
    private var _max = 100
    private var _progress = 0
    private var _waveColor = WAVE_PAINT_COLOR
    private var _waveHeight = STRETCH_FACTOR_A
    private var _strokeWidth = 5.dp2px()
    private var _strokeColor = OUTER_RING_COLOR

    ///////////////////////////////////////////////////////////////////////////////

    private val _drawFilter: DrawFilter by lazy { PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG) }
    private val _cirPath: Path by lazy { Path() }

    private lateinit var _wavePaint: Paint
    private lateinit var _outerRingPaint: Paint
    private lateinit var _yPositions: FloatArray
    private lateinit var _resetOneYPositions: FloatArray
    private lateinit var _resetTwoYPositions: FloatArray

    private var _width = 0
    private var _height = 0
    private var _xOffsetSpeedOne = 0
    private var _xOffsetSpeedTwo = 0
    private var _xOneOffset = 0
    private var _xTwoOffset = 0

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
        _mode = typedArray.getInt(R.styleable.ViewKProgressWave_viewKProgressWave_mode, _mode)
        _max = typedArray.getInt(R.styleable.ViewKProgressWave_viewKProgressWave_max, _max)
        _progress = typedArray.getInt(R.styleable.ViewKProgressWave_viewKProgressWave_progress, _progress)
        _waveColor = typedArray.getColor(R.styleable.ViewKProgressWave_viewKProgressWave_waveColor, _waveColor)
        _waveHeight = typedArray.getDimension(R.styleable.ViewKProgressWave_viewKProgressWave_waveHeight, _waveHeight)
        _strokeWidth = typedArray.getDimension(R.styleable.ViewKProgressWave_viewKProgressWave_strokeWidth, _strokeWidth)
        _strokeColor = typedArray.getColor(R.styleable.ViewKProgressWave_viewKProgressWave_strokeColor, _strokeColor)
        typedArray.recycle()
    }

    override fun initPaint() {
        _outerRingPaint = Paint()
        _outerRingPaint.isAntiAlias = true
        _outerRingPaint.strokeWidth = _strokeWidth
        _outerRingPaint.color = _strokeColor
        _outerRingPaint.style = Paint.Style.STROKE

        _wavePaint = Paint()
        _wavePaint.isAntiAlias = true
        _wavePaint.style = Paint.Style.FILL
        _wavePaint.color = _waveColor
    }

    override fun initView() {
        setProgress(_progress)
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

        _yPositions = FloatArray(_width)// 用于保存原始波纹的y值
        _resetOneYPositions = FloatArray(_width)// 用于保存波纹一的y值
        _resetTwoYPositions = FloatArray(_width)// 用于保存波纹二的y值

        val mCycleFactorW = (2 * Math.PI / _width).toFloat()// 将周期定意

        for (i in 0 until _width) {// 根据view总宽度得出所有对应的y值
            _yPositions[i] = (_waveHeight * sin((mCycleFactorW * i).toDouble()) - _waveHeight).toFloat()
        }
        _cirPath.addCircle(_width / 2.0f, _height / 2.0f, _width / 2.0f - _strokeWidth + 0.3f, Path.Direction.CW)

        _xOffsetSpeedOne = TRANSLATE_X_SPEED_ONE.dp2pxI() * _width / 330.dp2pxI()// 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        _xOffsetSpeedTwo = TRANSLATE_X_SPEED_TWO.dp2pxI() * _width / 330.dp2pxI()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawFilter = _drawFilter
        canvas.save()
        // 从canvas层面去除绘制时锯齿
        canvas.clipPath(_cirPath) //裁剪
        resetPositionY()
        val proHeight = _height - _progress.toFloat() / _max * (_height - 2 * _strokeWidth) - _strokeWidth
        for (i in 0 until _width) {
            // 绘制第一条水波纹
            canvas.drawLine(i.toFloat(), proHeight - _resetOneYPositions[i], i.toFloat(), _height.toFloat(), _wavePaint)
            // 绘制第二条水波纹
            canvas.drawLine(i.toFloat(), proHeight - _resetTwoYPositions[i], i.toFloat(), _height.toFloat(), _wavePaint)
        }
        canvas.restore()

        if (_strokeWidth > 0) canvas.drawCircle(_width / 2.0f, _height / 2.0f, _width / 2.0f - _strokeWidth / 2, _outerRingPaint) //绘制外环

        // 改变两条波纹的移动点
        _xOneOffset += _xOffsetSpeedOne
        _xTwoOffset += _xOffsetSpeedTwo
        // 如果已经移动到结尾处，则重头记录
        if (_xOneOffset >= _width)
            _xOneOffset = 0
        if (_xTwoOffset > _width)
            _xTwoOffset = 0
        if (_waveHeight > 0)
            postInvalidate()
    }

    ///////////////////////////////////////////////////////////////////////////////

    fun getMax(): Int {
        return _max
    }

    fun setMax(max: Int) {
        _max = max
        invalidate()
    }

    fun getProgress(): Int {
        return _progress
    }

    /**
     * 可在子线程中刷新
     */
    fun setProgress(progress: Int) {
        _progress = progress
        postInvalidate()
    }

    fun getStrokeWidth(): Float {
        return _strokeWidth
    }

    fun setStrokeWidth(strokeWidth: Float) {
        _strokeWidth = strokeWidth
        invalidate()
    }

    fun getStrokeColor(): Int {
        return _strokeColor
    }

    fun setStrokeColor(strokeColor: Int) {
        _outerRingPaint.color = _strokeColor
        invalidate()
    }

    fun getWaveColor(): Int {
        return _waveColor
    }

    fun setWaveColor(wavePaintColor: Int) {
        _wavePaint.color = wavePaintColor
        invalidate()
    }

    ///////////////////////////////////////////////////////////////////////////////

    private fun resetPositionY() {
        // _xOneOffset代表当前第一条水波纹要移动的距离
        val oneInterval = _yPositions.size - _xOneOffset
        // 重新填充第一条波纹的数据
        System.arraycopy(_yPositions, _xOneOffset, _resetOneYPositions, 0, oneInterval)
        System.arraycopy(_yPositions, 0, _resetOneYPositions, oneInterval, _xOneOffset)

        val twoInterval = _yPositions.size - _xTwoOffset
        System.arraycopy(_yPositions, _xTwoOffset, _resetTwoYPositions, 0, twoInterval)
        System.arraycopy(_yPositions, 0, _resetTwoYPositions, twoInterval, _xTwoOffset)
    }
}
