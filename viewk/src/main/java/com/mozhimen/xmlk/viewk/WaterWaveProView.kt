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
import android.util.TypedValue
import android.view.animation.LinearInterpolator
import com.mozhimen.basick.utilk.android.animation.cancel_removeAllListeners
import com.mozhimen.basick.utilk.android.util.UtilKLogWrapper
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
        const val defaultInColor = "#69B655" //默认里面颜色
        const val defaultWaterColor = "#0AA328" //默认水波颜色
    }

    private var mMaxNum = 100.0 //最大值
    private var mCurrentNum = 0.0 //当前的值
    private var mPercent = 0.0 //百分比
    private var mInColor = 0 //里面颜色
    private var mWaterColor = 0 //水波颜色

    //控件宽高
//    var mDefaultWidthHeight: Int = 100 //默认宽高，单位sp
    private var mStartX = 0f //开始位置
    private var mWaveWidth = 0 //水波长
    private var mWaveHeight = 0 //水波高度
    private lateinit var mPaint: Paint
    private lateinit var mPath: Path
    private var _width = 0
    private var _height = 0

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null) //关闭硬件加速
        initAttrs(attrs)
        initPaint()
    }

    override fun initAttrs(attrs: AttributeSet?) {
        // 获取自定义属性
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterWaveProView)
        mWaterColor = typedArray.getColor(R.styleable.WaterWaveProView_liys_progress_water_waterColor, Color.parseColor(defaultInColor))
        mInColor = typedArray.getColor(R.styleable.WaterWaveProView_liys_progress_water_inColor, Color.parseColor(defaultWaterColor))
        typedArray.recycle()
    }

    override fun initPaint() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.color = mWaterColor

        mPath = Path()
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

        val defaultWaterHeight = 5 //默认水波高度 单位sp

        //5. 确定波长和波高
        mWaveWidth = _width / 4
        mWaveHeight = sp2px(defaultWaterHeight)

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
        drawBessel(_width.toFloat(), mStartX, (_height * (1 - mPercent)).toInt().toFloat(), mWaveWidth.toFloat(), mWaveHeight.toFloat(), mPath, mPaint)
        canvas.drawPath(mPath, mPaint)
        //2. 设置模式
        mPaint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.DST_ATOP))
        //3. 绘制圆形bitmap
        canvas.drawBitmap(createCircleBitmap(_width / 2, mInColor), null, Rect(0, 0, _width, _height), mPaint)
        //4. 绘制文字
//        drawText(canvas, mText, mWidth, mHeight, mTextPaint)
    }

    /**
     * 绘制贝塞尔曲线
     * @param width 总共需要绘制的长度
     * @param startX 开始X点坐标(-2*startX 到 0 之间) 左右预留一个波长
     * @param startY 开始Y坐标
     * @param waveWidth 波长(半个周期)
     * @param waveHeight 波高
     * @param path
     * @param paint 画笔
     */
    private fun drawBessel(width: Float, startX: Float, startY: Float, waveWidth: Float, waveHeight: Float, path: Path, paint: Paint) {
        //Android贝塞尔曲线
        // 二阶写法：rQuadTo(float dx1, float dy1, float dx2, float dy2) 相对上一个起点的坐标
        path.reset()
        var currentWidth = 0 //当前已经绘制的宽度
        path.moveTo(startX, startY) //画笔位置
        while (currentWidth <= width + 4 * waveWidth && waveWidth > 0) {
            path.rQuadTo(waveWidth, -waveHeight, 2 * waveWidth, 0f)
            path.rQuadTo(waveWidth, waveHeight, 2 * waveWidth, 0f)
            currentWidth = (currentWidth + 2 * waveWidth).toInt()
        }
        //封闭的区域
        mPath.lineTo(getWidth() + 4 * waveWidth, height.toFloat())
        mPath.lineTo(0f, height.toFloat())
        path.close()
    }

    private fun createCircleBitmap(radius: Int, color: Int): Bitmap {
        val canvasBmp = Bitmap.createBitmap(_width, _height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(canvasBmp)
        canvas.drawColor(Color.TRANSPARENT)
        val paint = Paint()
        paint.color = color
        canvas.drawCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), paint) //确定位置
        return canvasBmp
    }

    /**
     * 设置当前进度
     * @param currentNum
     */
    fun setCurrentNum(currentNum: Double) {
        this.mCurrentNum = currentNum
        setPercent()
    }

    fun setMaxNum(maxNum: Int) {
        this.mMaxNum = maxNum.toDouble()
        setPercent()
    }

    private fun setPercent() {
        if (mCurrentNum > mMaxNum) {
            mCurrentNum = mMaxNum
        }
        mPercent = mCurrentNum / mMaxNum
    }

    private fun setStartX(startX: Float) {
        mStartX = startX
        invalidate()
    }

    private fun sp2px(sp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, sp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }

    private var _valueAnimator: ValueAnimator? = null

    private fun startAnimator() {
        if (_valueAnimator == null) {
            _valueAnimator = ValueAnimator.ofFloat((0 - 4 * mWaveWidth).toFloat(), 0f)
            _valueAnimator!!.interpolator = LinearInterpolator() //匀速插值器 解决卡顿问题
            _valueAnimator!!.setDuration(2000)
            _valueAnimator!!.repeatCount = ValueAnimator.INFINITE
            _valueAnimator!!.addUpdateListener { animation ->
                setStartX((animation.animatedValue as Float).also{ UtilKLogWrapper.d(TAG, "startAnimator: animatedValue $it") })
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