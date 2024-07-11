package com.mozhimen.xmlk.viewk

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.annotation.IntDef
import com.mozhimen.basick.utilk.android.animation.cancel_removeAllListeners
import com.mozhimen.basick.utilk.android.graphics.applyBitmapAnyScaleRatio
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.util.dp2pxI
import com.mozhimen.basick.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.basick.utilk.kotlin.intResDrawable2bitmapAny
import com.mozhimen.basick.utilk.kotlin.strColor2intColor
import com.mozhimen.xmlk.bases.BaseViewK


/**
 * @ClassName WaveView
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/7/10
 * @Version 1.0
 */
class ViewKProgressWaveIcon @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseViewK(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_COLOR_WAVE1: String = "#310177FD" //默认里面颜色
        const val DEFAULT_COLOR_WAVE2: String = "#80ffffff" //默认水波颜色
        const val DEFAULT_COLOR_WAVE3: String = "#80000000" //默认水波颜色
        const val DEFAULT_COLOR_BG: Int = Color.WHITE //默认水波颜色
        const val DEFAULT_WAVE_COUNT: Int = 1
        const val DEFAULT_WAVE_MAX: Int = 100
        const val DEFAULT_WAVE_HEIGHT: Int = 7
    }

    ///////////////////////////////////////////////////////////////////////////////

    private var _max = DEFAULT_WAVE_MAX //最大值
    private var _progress = 0 //当前的值
    private var _bgColor: Int = DEFAULT_COLOR_BG
    private val _waveColors: IntArray by lazy_ofNone { intArrayOf(DEFAULT_COLOR_WAVE1.strColor2intColor(), DEFAULT_COLOR_WAVE2.strColor2intColor(), DEFAULT_COLOR_WAVE3.strColor2intColor()) }//水波颜色
    private var _waveAnimTime: LongArray = longArrayOf(3000, 2000)
    private var _waveHeight = DEFAULT_WAVE_HEIGHT.dp2px() //水波高度
    private var _waveCount = DEFAULT_WAVE_COUNT
    private var _iconRedId = 0
    private var _splits = intArrayOf(1, 1)
    private val _paths by lazy_ofNone { mutableListOf(Path(), Path(), Path()) }

    ///////////////////////////////////////////////////////////////////////////////

    private lateinit var _bgPaint: Paint
    private lateinit var _iconPaint: Paint
    private lateinit var _porterDuffXfermode: PorterDuffXfermode//DST_ATOP

    private var _waveLocationXStarts = floatArrayOf(0f, 0f) //开始位置
    private var _percent = 0f //百分比
    private var _width = 0
    private var _height = 0
    private var _bgBitmap: Bitmap? = null
    private var _iconBitmap: Bitmap? = null

    ///////////////////////////////////////////////////////////////////////////////

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null) //关闭硬件加速
        initAttrs(attrs)
        initPaint()
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewKProgressWaveIcon)
        _waveHeight =
            typedArray.getDimension(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_waveHeight, _waveHeight)
        _max =
            typedArray.getInt(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_max, _max)
        _progress =
            typedArray.getInt(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_progress, _progress)
        _iconRedId =
            typedArray.getResourceId(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_src, _iconRedId)
        _waveCount =
            typedArray.getInt(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_waveCount, _waveCount)
        typedArray.recycle()
    }

    override fun initPaint() {
        _bgPaint = Paint()
        _bgPaint.isAntiAlias = true
        _bgPaint.isDither = true

        _iconPaint = Paint()
        _iconPaint.isFilterBitmap = true
        _iconPaint.isAntiAlias = true
        _iconPaint.isDither = true

        _porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP)
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

        _waveLocationXStarts[0] = -2f * _width.toFloat()
        _waveLocationXStarts[1] = -2f * _width.toFloat()

        createBitmaps()

        startAnimator()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimator()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimator()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (_bgBitmap != null) {
            canvas.drawBitmap(_bgBitmap!!, 0f, 0f, _iconPaint)
        }

        _iconPaint.setXfermode(_porterDuffXfermode)

        //隔离层绘制混合模式
        if (_iconBitmap != null) {
            canvas.drawBitmap(_iconBitmap!!, 0f, 0f, _iconPaint)
        }

        //1. 绘制贝塞尔曲线
        for (i in 0 until _waveCount) {
            _iconPaint.color = _waveColors[i]
            generateBesselPath(_waveLocationXStarts[i], _height.toFloat() * (1 - _percent), _width.toFloat() / _splits[i], _waveHeight, _splits[i], _paths[i])
            canvas.drawPath(_paths[i], _iconPaint)
        }
        generateMaskPath(_paths[2])
        _paths[2].op(_paths[0], Path.Op.DIFFERENCE)
        _iconPaint.color = _waveColors[2]
        canvas.drawPath(_paths[2], _iconPaint)
        _iconPaint.color = Color.BLACK


        _iconPaint.setXfermode(null)
    }

    ///////////////////////////////////////////////////////////////////////////////

    /**
     * 设置当前进度
     */
    fun setProgress(progress: Int) {
        _progress = progress
        setPercent()
    }

    fun getProgress(): Int =
        _progress

    fun setMax(max: Int) {
        _max = max
        setPercent()
    }

    fun getMax(): Int =
        _max

//    fun getStrokeColor(): Int =
//        _strokeColor
//
//    fun setStrokeColor(strokeColor: Int) {
//        _borderPaint.color = strokeColor.also { _strokeColor = it }
//        invalidate()
//    }

//    fun getStrokeWidth(): Float =
//        _strokeWidth
//
//    fun setStrokeWidth(strokeWidth: Float) {
//        _strokeWidth = strokeWidth
//    }

    ///////////////////////////////////////////////////////////////////////////////

    /**
     * 绘制贝塞尔曲线
     * @param width 总共需要绘制的长度
     * @param startX 开始X点坐标(-2*startX 到 0 之间) 左右预留一个波长
     * @param startY 开始Y坐标
     * @param waveWidth 波长(半个周期)
     * @param waveHeight 波高
     * @param path
     */
    private fun generateBesselPath(startX: Float, startY: Float, waveWidth: Float, waveHeight: Float, split: Int, path: Path): Path {
        //Android贝塞尔曲线
        // 二阶写法：rQuadTo(float dx1, float dy1, float dx2, float dy2) 相对上一个起点的坐标
        path.reset()
        path.moveTo(startX, startY) //画笔位置
        for (i in 1..3 * split) {
            if (i % 2 == 1) {
                path.rQuadTo(waveWidth / 2f, -waveHeight, waveWidth, 0f)
            } else {
                path.rQuadTo(waveWidth / 2f, waveHeight, waveWidth, 0f)
            }
        }
        //封闭的区域
        path.lineTo(getWidth().toFloat(), getHeight().toFloat())
        path.lineTo(0f, getHeight().toFloat())
        path.close()
        return path
    }

    private fun generateMaskPath(path: Path): Path {
        path.reset()
        path.addRect(0f, 0f, _width.toFloat(), _height.toFloat(), Path.Direction.CW)
        return path
    }

    private fun createBitmaps() {
        _bgBitmap = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(_bgBitmap!!)
        _bgPaint.color = _bgColor
        canvas.drawCircle(_width / 2f, _height / 2f, _width / 2f, _bgPaint) //确定位置

        if (_iconRedId != 0) {
            val bitmap = _iconRedId.intResDrawable2bitmapAny(context)
            val scaleRatioX = _width.toFloat() / bitmap.width
            val scaleRatioY = _height.toFloat() / bitmap.height
            _iconBitmap = bitmap.applyBitmapAnyScaleRatio(scaleRatioX, scaleRatioY)
        }
    }

    private fun setPercent() {
        if (_progress > _max) {
            _progress = _max
        }
        _percent = _progress.toFloat() / _max
    }

    ///////////////////////////////////////////////////////////////////////////

    private var _valueAnimators: MutableList<ValueAnimator> = mutableListOf()

    private fun startAnimator() {
        if (_width == 0) return

        if (_valueAnimators.isEmpty()) {
            repeat(_waveCount) { index ->
                val valueAnimator = ValueAnimator.ofFloat(-2f * _width.toFloat(), 0f)
                valueAnimator.interpolator = LinearInterpolator() //匀速插值器 解决卡顿问题
                valueAnimator.setDuration(_waveAnimTime[index])
                valueAnimator.repeatCount = ValueAnimator.INFINITE
                valueAnimator.addUpdateListener { animation ->
                    _waveLocationXStarts[index] = animation.animatedValue as Float
                    invalidate()
                }
                valueAnimator.start()
                _valueAnimators.add(valueAnimator)
            }
        } else {
            _valueAnimators.forEach { animator ->
                if (!animator.isRunning || !animator.isStarted) {
                    animator.start()
                }
            }
        }
    }

    private fun stopAnimator() {
        _valueAnimators.forEach {
            it.cancel_removeAllListeners()
        }
        _valueAnimators.clear()
    }
}