package com.mozhimen.xmlk.viewk

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.DrawFilter
import android.graphics.Paint
import android.graphics.PaintFlagsDrawFilter
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import androidx.annotation.IntDef
import com.mozhimen.basick.utilk.android.graphics.applyBitmapAnyScaleRatio
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.util.dp2pxI
import com.mozhimen.basick.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.basick.utilk.kotlin.intResDrawable2bitmapAny
import com.mozhimen.xmlk.bases.BaseViewK
import kotlin.math.sin


/**
 * @ClassName ViewKProgressWave
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/7/8
 * @Version 1.0
 */
class ViewKProgressWave2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseViewK(context, attrs, defStyleAttr) {

    companion object {
        private const val WAVE_PAINT_COLOR = -0x37ea437d//默认波纹颜色
        private const val OUTER_RING_COLOR = -0x7ff7c71//默认外环颜色
        private const val STRETCH_FACTOR_A = 15f// y = Asin(wx+b)+h
        private const val TRANSLATE_X_SPEED_BTM = 5// 第一条水波移动速度
        private const val TRANSLATE_X_SPEED_TOP = 3// 第二条水波移动速度
    }

    ///////////////////////////////////////////////////////////////////////////////

    private var _max = 100

    @Volatile
    private var _progress = 0
    private var _waveColor = WAVE_PAINT_COLOR
    private var _waveHeight = STRETCH_FACTOR_A
    private var _strokeWidth = 5.dp2px()
    private var _strokeColor = OUTER_RING_COLOR

    ///////////////////////////////////////////////////////////////////////////////

    private val _drawFilter: DrawFilter by lazy_ofNone { PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG) }
    private val _cirPath: Path by lazy_ofNone { Path() }

    private lateinit var _wavePaint: Paint
    private lateinit var _borderPaint: Paint
    private lateinit var _yPositions: FloatArray
    private lateinit var _resetYPositionsBtm: FloatArray
    private lateinit var _resetYPositionsTop: FloatArray

    private var _width = 0
    private var _height = 0
    private var _xOffsetSpeedBtm = 0
    private var _xOffsetSpeedTop = 0
    private var _xOffsetBtm = 0
    private var _xOffsetTop = 0

    ///////////////////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
        initPaint()
        initView()
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewKProgressWave2)
        _max = typedArray.getInt(R.styleable.ViewKProgressWave2_viewKProgressWave2_max, _max)
        _progress = typedArray.getInt(R.styleable.ViewKProgressWave2_viewKProgressWave2_progress, _progress)
        _waveColor = typedArray.getColor(R.styleable.ViewKProgressWave2_viewKProgressWave2_waveColor, _waveColor)
        _waveHeight = typedArray.getDimension(R.styleable.ViewKProgressWave2_viewKProgressWave2_waveHeight, _waveHeight)
        _strokeWidth = typedArray.getDimension(R.styleable.ViewKProgressWave2_viewKProgressWave2_strokeWidth, _strokeWidth)
        _strokeColor = typedArray.getColor(R.styleable.ViewKProgressWave2_viewKProgressWave2_strokeColor, _strokeColor)
        typedArray.recycle()
    }

    override fun initPaint() {
        _borderPaint = Paint()
        _borderPaint.isAntiAlias = true
        _borderPaint.strokeWidth = _strokeWidth
        _borderPaint.color = _strokeColor
        _borderPaint.style = Paint.Style.STROKE

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
        _resetYPositionsBtm = FloatArray(_width)// 用于保存波纹一的y值
        _resetYPositionsTop = FloatArray(_width)// 用于保存波纹二的y值

        val mCycleFactorW = (2 * Math.PI / _width).toFloat()// 将周期定意

        for (i in 0 until _width) {// 根据view总宽度得出所有对应的y值
            _yPositions[i] = (_waveHeight * sin((mCycleFactorW * i).toDouble()) - _waveHeight).toFloat()
        }
        _cirPath.addCircle(_width / 2.0f, _height / 2.0f, _width / 2.0f - _strokeWidth + 0.3f, Path.Direction.CW)

        _xOffsetSpeedBtm = TRANSLATE_X_SPEED_BTM.dp2pxI() * _width / 330.dp2pxI()// 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        _xOffsetSpeedTop = TRANSLATE_X_SPEED_TOP.dp2pxI() * _width / 330.dp2pxI()
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
            canvas.drawLine(i.toFloat(), proHeight - _resetYPositionsBtm[i], i.toFloat(), _height.toFloat(), _wavePaint)
            // 绘制第二条水波纹
            canvas.drawLine(i.toFloat(), proHeight - _resetYPositionsTop[i], i.toFloat(), _height.toFloat(), _wavePaint)
        }
        canvas.restore()

        if (_strokeWidth > 0f)
            canvas.drawCircle(_width / 2.0f, _height / 2.0f, _width / 2.0f - _strokeWidth / 2, _borderPaint) //绘制外环

        // 改变两条波纹的移动点
        _xOffsetBtm += _xOffsetSpeedBtm
        _xOffsetTop += _xOffsetSpeedTop
        // 如果已经移动到结尾处，则重头记录
        if (_xOffsetBtm >= _width)
            _xOffsetBtm = 0
        if (_xOffsetTop > _width)
            _xOffsetTop = 0
        if (_waveHeight > 0)
            postInvalidate()
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

    ///////////////////////////////////////////////////////////////////////////////

    private fun resetPositionY() {
        // _xOneOffset代表当前第一条水波纹要移动的距离
        val oneInterval = _yPositions.size - _xOffsetBtm
        // 重新填充第一条波纹的数据
        System.arraycopy(_yPositions, _xOffsetBtm, _resetYPositionsBtm, 0, oneInterval)
        System.arraycopy(_yPositions, 0, _resetYPositionsBtm, oneInterval, _xOffsetBtm)

        val twoInterval = _yPositions.size - _xOffsetTop
        System.arraycopy(_yPositions, _xOffsetTop, _resetYPositionsTop, 0, twoInterval)
        System.arraycopy(_yPositions, 0, _resetYPositionsTop, twoInterval, _xOffsetTop)
    }
}
