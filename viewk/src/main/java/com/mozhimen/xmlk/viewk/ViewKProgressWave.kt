package com.mozhimen.xmlk.viewk

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DrawFilter
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import androidx.annotation.IntDef
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.util.dp2pxI
import com.mozhimen.basick.utilk.android.util.sp2px
import com.mozhimen.xmlk.bases.BaseViewK
import kotlin.math.sin


/**
 * @ClassName ViewKProgressWave
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/7/8
 * @Version 1.0
 */
class ViewKProgressWave @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleRes: Int = 0) : BaseViewK(context, attrs, defStyleRes) {

    companion object {
        //默认波纹颜色
        private const val WAVE_PAINT_COLOR = -0x37ea437d

        //默认外环颜色
        private const val OUTER_RING_COLOR = -0x7ff7c71

        // y = Asin(wx+b)+h
        private const val STRETCH_FACTOR_A = 15f

        // 第一条水波移动速度
        private const val TRANSLATE_X_SPEED_ONE = 5

        // 第二条水波移动速度
        private const val TRANSLATE_X_SPEED_TWO = 3
    }

    ///////////////////////////////////////////////////////////////////////////////

    private var _max = 100
    private var _progress = 0
    private var _waveColor = WAVE_PAINT_COLOR
    private var _waveHeight = STRETCH_FACTOR_A
    private var _strokeWidth = 5.dp2px()
    private var _strokeColor = OUTER_RING_COLOR
    private var _submergedTextColor = 0xffffff0

    ///////////////////////////////////////////////////////////////////////////////

    private lateinit var _wavePaint: Paint
    private lateinit var _outerRingPaint: Paint

    private lateinit var _yPositions: FloatArray
    private lateinit var _resetOneYPositions: FloatArray
    private lateinit var _resetTwoYPositions: FloatArray
    private val _drawFilter: DrawFilter by lazy { PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG) }
    private val _cirPath: Path by lazy { Path() }

    private var width = 0
    private var height = 0

    private var mXOffsetSpeedOne = 0
    private var mXOffsetSpeedTwo = 0
    private var mXOneOffset = 0
    private var mXTwoOffset = 0

    ///////////////////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
        initPaint()
        initView()
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewKProgressWave)

        _max = typedArray.getInt(R.styleable.ViewKProgressWave_viewKProgressWave_max, _max)
        _progress = typedArray.getInt(R.styleable.ViewKProgressWave_viewKProgressWave_progress, _progress)
        _waveColor = typedArray.getColor(R.styleable.ViewKProgressWave_viewKProgressWave_waveColor, _waveColor)
        _waveHeight = typedArray.getDimension(R.styleable.ViewKProgressWave_viewKProgressWave_waveHeight, _waveHeight)
        _strokeWidth = typedArray.getDimension(R.styleable.ViewKProgressWave_viewKProgressWave_strokeWidth, _strokeWidth)
        _strokeColor = typedArray.getColor(R.styleable.ViewKProgressWave_viewKProgressWave_strokeColor, _strokeColor)
        _submergedTextColor = typedArray.getColor(R.styleable.ViewKProgressWave_viewKProgressWave_submergedTextColor, _submergedTextColor)

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
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            val size = 150.dp2pxI()
            setMeasuredDimension(size, size)
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(150.dp2pxI(), MeasureSpec.getSize(heightMeasureSpec))
        } else if (heightMode == MeasureSpec.AT_MOST) {
            val size = MeasureSpec.getSize(widthMeasureSpec)
            setMeasuredDimension(size, size)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w < h) {
            height = w
            width = height
        } else {
            height = h
            width = height
        }
        // 用于保存原始波纹的y值
        _yPositions = FloatArray(width)
        // 用于保存波纹一的y值
        _resetOneYPositions = FloatArray(width)
        // 用于保存波纹二的y值
        _resetTwoYPositions = FloatArray(width)
        // 将周期定意
        val mCycleFactorW = (2 * Math.PI / width).toFloat()
        // 根据view总宽度得出所有对应的y值
        for (i in 0 until width) {
            _yPositions[i] = (_waveHeight * sin((mCycleFactorW * i).toDouble()) - _waveHeight).toFloat()
        }
        _cirPath.addCircle(width / 2.0f, height / 2.0f, width / 2.0f - _strokeWidth + 0.3f, Path.Direction.CW)
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = TRANSLATE_X_SPEED_ONE.dp2pxI() * width / 330.dp2pxI()
        mXOffsetSpeedTwo = TRANSLATE_X_SPEED_TWO.dp2pxI() * width / 330.dp2pxI()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawFilter = _drawFilter
        canvas.save()
        // 从canvas层面去除绘制时锯齿
        canvas.clipPath(_cirPath) //裁剪
        resetPositionY()
        val proHeight = height - _progress.toFloat() / _max * (height - 2 * _strokeWidth) - _strokeWidth
        for (i in 0 until width) {
            // 绘制第一条水波纹
            canvas.drawLine(i.toFloat(), proHeight - _resetOneYPositions[i], i.toFloat(), height.toFloat(), _wavePaint)
            // 绘制第二条水波纹
            canvas.drawLine(i.toFloat(), proHeight - _resetTwoYPositions[i], i.toFloat(), height.toFloat(), _wavePaint)
        }
        canvas.restore()

        if (_strokeWidth > 0) canvas.drawCircle(width / 2.0f, height / 2.0f, width / 2.0f - _strokeWidth / 2, _outerRingPaint) //绘制外环

        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne
        mXTwoOffset += mXOffsetSpeedTwo
        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= width) mXOneOffset = 0
        if (mXTwoOffset > width) mXTwoOffset = 0
        if (_waveHeight > 0) postInvalidate()
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
        // mXOneOffset代表当前第一条水波纹要移动的距离
        val oneInterval = _yPositions.size - mXOneOffset
        // 重新填充第一条波纹的数据
        System.arraycopy(_yPositions, mXOneOffset, _resetOneYPositions, 0, oneInterval)
        System.arraycopy(_yPositions, 0, _resetOneYPositions, oneInterval, mXOneOffset)

        val twoInterval = _yPositions.size - mXTwoOffset
        System.arraycopy(_yPositions, mXTwoOffset, _resetTwoYPositions, 0, twoInterval)
        System.arraycopy(_yPositions, 0, _resetTwoYPositions, twoInterval, mXTwoOffset)
    }
}
