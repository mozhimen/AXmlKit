package com.mozhimen.xmlk.viewk

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.text.TextPaint
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.annotation.UiThread
import com.mozhimen.basick.utilk.android.animation.cancel_removeAllListeners
import com.mozhimen.basick.utilk.android.graphics.applyBitmapAnyScaleRatio
import com.mozhimen.basick.utilk.android.os.UtilKBuildVersion
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.util.dp2pxI
import com.mozhimen.basick.utilk.android.util.sp2px
import com.mozhimen.basick.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.basick.utilk.kotlin.ifNotEmpty
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
        const val DEFAULT_COLOR_WAVE1: String = "#11ffffff" //默认里面颜色
        const val DEFAULT_COLOR_WAVE2: String = "#21ffffff" //默认水波颜色
        const val DEFAULT_COLOR_WAVE3: String = "#80000000" //默认水波颜色
        const val DEFAULT_COLOR_BG: Int = Color.WHITE //默认水波颜色
        const val DEFAULT_WAVE_COUNT: Int = 1
        const val DEFAULT_WAVE_MAX: Int = 100
        const val DEFAULT_WAVE_HEIGHT: Int = 7
        const val DEFAULT_TEXT_SIZE: Int = 20
    }

    ///////////////////////////////////////////////////////////////////////////////

    private var _max = DEFAULT_WAVE_MAX //最大值

    @Volatile
    private var _progress = 0 //当前的值
    private var _bgColor: Int = DEFAULT_COLOR_BG
    private var _waveAnimTime: LongArray = longArrayOf(3000, 2000)
    private var _waveHeight = DEFAULT_WAVE_HEIGHT.dp2px() //水波高度
    private var _waveCount = DEFAULT_WAVE_COUNT
    private var _iconRedId = 0
    private var _textEnabled = false
    private var _textSize = DEFAULT_TEXT_SIZE.sp2px()
    private var _textColor = Color.WHITE
    private var _strokeEnabled = false
    private var _strokeWidth = 4.dp2px()
    private var _strokeColor = Color.BLACK

    ///////////////////////////////////////////////////////////////////////////////

    private lateinit var _bgPaint: Paint
    private lateinit var _iconPaint: Paint
    private val _textPaint: Paint by lazy_ofNone {
        val textPaint = TextPaint()
        textPaint.style = Paint.Style.FILL
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.isFakeBoldText = true
        textPaint.isAntiAlias = true
        textPaint.textSize = _textSize
        textPaint.setColor(_textColor)
        textPaint
    }
    private val _strokePaint by lazy_ofNone {
        val paint = Paint()
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.isAntiAlias = true
        paint.strokeWidth = _strokeWidth
        paint.color = _strokeColor
        paint.color
        paint
    }
    private lateinit var _porterDuffXfermode: PorterDuffXfermode//DST_ATOP

    private val _isAfter21: Boolean = UtilKBuildVersion.isAfterV_21_5_L()
    private val _waveColors: IntArray by lazy_ofNone { intArrayOf(DEFAULT_COLOR_WAVE1.strColor2intColor(), DEFAULT_COLOR_WAVE2.strColor2intColor(), DEFAULT_COLOR_WAVE3.strColor2intColor()) }//水波颜色
    private var _splits = intArrayOf(1, 1)
    private val _paths by lazy_ofNone { mutableListOf(Path(), Path(), Path()) }
    private var _waveLocationXStarts = floatArrayOf(0f, 0f) //开始位置
    private var _waveLocationYStart = 0f //开始位置
    private var _percent = 0f //百分比
    private var _width = 0
    private var _height = 0
    private var _bgBitmap: Bitmap? = null
    @Volatile
    private var _iconBitmap: Bitmap? = null

    @Volatile
    private var _text: String = ""

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
        _textEnabled =
            typedArray.getBoolean(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_textEnabled, _textEnabled)
        _textSize =
            typedArray.getDimension(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_textSize, _textSize)
        _textColor =
            typedArray.getColor(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_textColor, _textColor)
        _strokeEnabled =
            typedArray.getBoolean(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_strokeEnabled, _strokeEnabled)
        _strokeWidth =
            typedArray.getDimension(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_strokeWidth, _strokeWidth)
        _strokeColor =
            typedArray.getColor(R.styleable.ViewKProgressWaveIcon_viewKProgressWaveIcon_strokeColor, _strokeColor)
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
        _waveLocationYStart = _height.toFloat() * (1 - _percent)

        createBitmaps()

        startAnimatorX()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimatorX()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimatorX()
        stopAnimatorY()
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
            generateBesselPath(_waveLocationXStarts[i], _waveLocationYStart/*_height.toFloat() * (1 - _percent)*/, _width.toFloat() / _splits[i], _waveHeight, _splits[i], _paths[i])
            canvas.drawPath(_paths[i], _iconPaint)
        }
        generateMaskPath(_paths[2])
        _paths[2].op(_paths[0], Path.Op.DIFFERENCE)
        _iconPaint.color = _waveColors[2]
        canvas.drawPath(_paths[2], _iconPaint)
        _iconPaint.color = Color.BLACK

        _iconPaint.setXfermode(null)

        if (_textEnabled && _text.isNotEmpty()) {
            canvas.drawText(_text, centerX, centerY + _textPaint.textSize / 2f, _textPaint)
        }

        if (_strokeEnabled && _isAfter21 && _strokeWidth != 0f) {
            canvas.drawArc(_strokeWidth / 2f, _strokeWidth / 2f, _width - (_strokeWidth / 2f), _height - (_strokeWidth / 2f), 90f, _percent * 360f, false, _strokePaint)
        }
    }

    ///////////////////////////////////////////////////////////////////////////////

    /**
     * 设置当前进度
     */
    @UiThread
    fun setProgress(progress: Int) {
        _progress = progress
        setPercent()
    }

    fun getProgress(): Int =
        _progress

    @UiThread
    fun setMax(max: Int) {
        _max = max
        setPercent()
    }

    fun getMax(): Int =
        _max

    fun setIconBitmap(bitmap: Bitmap) {
        createIconBitmap(bitmap)
    }

    fun setText(text: String) {
        text.ifNotEmpty { _text = text }
    }

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
            if (bitmap!=null){
                val scaleRatioX = _width.toFloat() / bitmap.width
                val scaleRatioY = _height.toFloat() / bitmap.height
                _iconBitmap = bitmap.applyBitmapAnyScaleRatio(scaleRatioX, scaleRatioY)
            }
        }
    }

    private fun createIconBitmap(bitmap: Bitmap) {
        val scaleRatioX = _width.toFloat() / bitmap.width
        val scaleRatioY = _height.toFloat() / bitmap.height
        _iconBitmap = bitmap.applyBitmapAnyScaleRatio(scaleRatioX, scaleRatioY)
    }

    private fun setPercent() {
        if (_progress > _max) {
            _progress = _max
        }
        _percent = _progress.toFloat() / _max
        val curY = _waveLocationYStart
        val desY = _height.toFloat() * (1f - _percent)
        if (_percent == 1f) {
            _waveLocationYStart = 0f
        }
        if (curY == desY || _valueAnimatorY != null) return
        startAnimatorY(curY, desY)
    }

    ///////////////////////////////////////////////////////////////////////////

    private var _valueAnimatorsX: MutableList<ValueAnimator> = mutableListOf()
    private var _valueAnimatorY: ValueAnimator? = null

    private fun startAnimatorY(curY: Float, desY: Float) {
        if (_height == 0) return
        if (_valueAnimatorY == null) {
            _valueAnimatorY = ValueAnimator.ofFloat(curY, desY)
            _valueAnimatorY!!.interpolator = LinearInterpolator() //匀速插值器 解决卡顿问题
            _valueAnimatorY!!.setDuration(800)
            _valueAnimatorY!!.addUpdateListener { animation ->
                _waveLocationYStart = animation.animatedValue as Float//_height.toFloat() * (1 - _percent)
            }
            _valueAnimatorY!!.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    _valueAnimatorY = null
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            _valueAnimatorY!!.start()
        }
    }

    private fun startAnimatorX() {
        if (_width == 0) return

        if (_valueAnimatorsX.isEmpty()) {
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
                _valueAnimatorsX.add(valueAnimator)
            }
        } else {
            _valueAnimatorsX.forEach { animator ->
                if (!animator.isRunning || !animator.isStarted) {
                    animator.start()
                }
            }
        }
    }

    private fun stopAnimatorX() {
        _valueAnimatorsX.forEach {
            it.cancel_removeAllListeners()
        }
        _valueAnimatorsX.clear()
    }

    private fun stopAnimatorY() {
        _valueAnimatorY?.removeAllUpdateListeners()
        _valueAnimatorY = null
    }
}