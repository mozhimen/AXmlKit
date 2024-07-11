package com.mozhimen.xmlk.viewk

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import com.mozhimen.basick.utilk.android.animation.cancel_removeAllListeners
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.util.dp2pxI
import com.mozhimen.xmlk.bases.BaseViewK


/**
 * @ClassName WaveView
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/7/10
 * @Version 1.0
 */
class WaterWaveProView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseViewK(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_WAVE_COLOR1: Int = -0x7ff7c71 //默认里面颜色
        const val DEFAULT_WAVE_COLOR2: Int = -0x37ea437d //默认水波颜色
        const val DEFAULT_WAVE_COUNT: Int = 2
        const val DEFAULT_WAVE_MAX: Int = 100
        const val DEFAULT_WAVE_HEIGHT: Int = 5

//        const val DEFAULT_WAVE_SPLIT_ONE = 4
//        const val DEFAULT_WAVE_SPLIT_TWO = 3
    }

    ///////////////////////////////////////////////////////////////////////////////

    private var _max = DEFAULT_WAVE_MAX //最大值
    private var _progress = 0 //当前的值

    //    private var _bgColor: Int = DEFAULT_BG_COLOR //里面颜色
    private var _waveColors: IntArray = intArrayOf(DEFAULT_WAVE_COLOR1, DEFAULT_WAVE_COLOR2) //水波颜色
    private var _waveHeight = DEFAULT_WAVE_HEIGHT.dp2px() //水波高度
    private var _waveCount = DEFAULT_WAVE_COUNT

    ///////////////////////////////////////////////////////////////////////////////

    private lateinit var _paint: Paint
    private lateinit var _porterDuffXfermode: PorterDuffXfermode//DST_ATOP
    private lateinit var _rect: Rect
    private var _splits = intArrayOf(4, 3)
    private val _paths = mutableListOf<Path>()
    private var _waveLocationXStart = 0f //开始位置
    private var _percent = 0f //百分比
    private var _width = 0
    private var _height = 0
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
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterWaveProView)
//        _waveColor = typedArray.getColor(R.styleable.WaterWaveProView_waveColor, _waveColor)
//        _bgColor = typedArray.getColor(R.styleable.WaterWaveProView_bgColor, _bgColor)
        typedArray.recycle()
    }

    override fun initPaint() {
        _paint = Paint()
        _paint.isAntiAlias = true
//        _paint.color = _waveColor

        repeat(_waveCount) {
            _paths.add(Path())
        }

        _porterDuffXfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
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

        // 确定波长和波高
        _rect = Rect(0, 0, _width, _height)
        _paint.color = Color.TRANSPARENT
        _iconBitmap = createCircleBitmap(_paint, _width / 2)
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

        //1. 绘制贝塞尔曲线
        for (i in 0 until _waveCount) {
            _paint.color = _waveColors[i]
            generateBesselPath(_width.toFloat(), _waveLocationXStart, (_height * (1 - _percent)).toInt().toFloat(), _width.toFloat() / _splits[i], _waveHeight,_splits[i], _paths[i])
            canvas.drawPath(_paths[i], _paint)
        }
        //2. 设置模式
//        _paint.setXfermode(_porterDuffXfermode)
        //3. 绘制圆形bitmap
//        if (_iconBitmap != null) {
//            canvas.drawBitmap(_iconBitmap!!, null, _rect, _paint)
//        }
        //4. 绘制文字
//        drawText(canvas, mText, mWidth, mHeight, mTextPaint)
    }

    ///////////////////////////////////////////////////////////////////////////////

    /**
     * 设置当前进度
     */
    fun setProgress(progress: Int) {
        _progress = progress
        setPercent()
    }

    fun setMax(max: Int) {
        _max = max
        setPercent()
    }

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
    private fun generateBesselPath(width: Float, startX: Float, startY: Float, waveWidth: Float, waveHeight: Float, split: Int, path: Path): Path {
        //Android贝塞尔曲线
        // 二阶写法：rQuadTo(float dx1, float dy1, float dx2, float dy2) 相对上一个起点的坐标
        path.reset()
        var currentWidth = 0 //当前已经绘制的宽度
        path.moveTo(startX, startY) //画笔位置
        while (currentWidth <= width + split * waveWidth && waveWidth > 0) {
            path.rQuadTo(waveWidth, -waveHeight, 2 * waveWidth, 0f)
            path.rQuadTo(waveWidth, waveHeight, 2 * waveWidth, 0f)
            currentWidth = (currentWidth + 2 * waveWidth).toInt()
        }
        //封闭的区域
        path.lineTo(getWidth() + split * waveWidth, height.toFloat())
        path.lineTo(0f, height.toFloat())
        path.close()
        return path
    }

    private fun createCircleBitmap(paint: Paint, radius: Int): Bitmap {
        val canvasBmp = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(canvasBmp)
        canvas.drawColor(Color.TRANSPARENT)
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), paint) //确定位置
        return canvasBmp
    }

    private fun setPercent() {
        if (_progress > _max) {
            _progress = _max
        }
        _percent = _progress.toFloat() / _max
    }

    ///////////////////////////////////////////////////////////////////////////

    private var _valueAnimator: ValueAnimator? = null

    private fun startAnimator() {
        if (_width == 0) return

        if (_valueAnimator == null) {
            _valueAnimator = ValueAnimator.ofFloat(-_width.toFloat(), 0f)
            _valueAnimator!!.interpolator = LinearInterpolator() //匀速插值器 解决卡顿问题
            _valueAnimator!!.setDuration(2000)
            _valueAnimator!!.repeatCount = ValueAnimator.INFINITE
            _valueAnimator!!.addUpdateListener { animation ->
                _waveLocationXStart = animation.animatedValue as Float
                invalidate()
            }
            _valueAnimator!!.start()
        } else if (!_valueAnimator!!.isRunning || !_valueAnimator!!.isStarted) {
            _valueAnimator?.start()
        }
    }

    private fun stopAnimator() {
        _valueAnimator?.cancel_removeAllListeners()
        _valueAnimator = null
    }
}