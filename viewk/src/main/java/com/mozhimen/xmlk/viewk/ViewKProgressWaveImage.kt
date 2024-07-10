package com.mozhimen.xmlk.viewk

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.mozhimen.basick.utilk.android.util.dp2px
import com.mozhimen.basick.utilk.android.util.dp2pxI
import com.mozhimen.basick.utilk.kotlin.UtilKLazyJVM.lazy_ofNone
import com.mozhimen.xmlk.bases.BaseViewK


/**
 * @ClassName ViewKProgressWaveImage
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2024/7/8
 * @Version 1.0
 */
class ViewKProgressWaveImage @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseViewK(context, attrs, defStyleAttr) {

    companion object {
        const val ICON_STYLE: Int = 1
        const val COLOR_STYLE: Int = 2
    }

    ///////////////////////////////////////////////////////////////////////////////

    //水波纹路径
    private val mWavePath1: Path by lazy_ofNone { Path() }
    private val mWavePath2: Path by lazy_ofNone { Path() }

    private lateinit var mIconPaint: Paint
    private lateinit var mBallPaint: Paint
    private lateinit var mTextPaint: Paint
    private lateinit var mDuffXfermode: PorterDuffXfermode//混合模式

    private val mItemWidth = 120f//每节波浪的宽度
    private var mOffsetX1 = 0f//波浪的偏移量
    private var mOffsetX2 = 0f
    private var mWidth = 0f//整个View的宽高
    private var mHeight = 0f
    private var mProgress = 0//当前进度(0~100)
    private var mBallBitmap: Bitmap? = null//圆形遮罩
    private var mIconBitmap: Bitmap? = null//图标资源对象
    private var mWaterTop = 0f//当前水位高度的纵坐标
    private var mWaveHeight = 0f//每节波浪上下波动的幅度

    private var mProgressAnim: ValueAnimator? = null//动画器
    private var mWaveHeightAnim: ValueAnimator? = null
    private var mAnimList: MutableList<Animator>? = null
    private var mAnimatorSet: AnimatorSet? = null

    /**
     * 样式
     * 1.ICON_STYLE:图标样式   2.COLOR_STYLE:纯颜色样式
     */
    private var mMode = ICON_STYLE
    private var mIsAutoLoad = true//是否自动开启动画
    private var mIsShowText = true//是否展示百分比文本（只在COLOR_STYLE模式下有效）
    private var mTextSize = 18.dp2pxI()//中心文字大小（只在COLOR_STYLE模式下有效）
    private var mDuration = 5000//动画时长
    private var mIconResId = -1//图标drawable Id
    private var mBallColor = Color.parseColor("#e58c7e")//球的颜色
    private var mBallStrokeWidth = 2.dp2px()//球的边缘宽度

    ///////////////////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
        setLayerType(LAYER_TYPE_HARDWARE, null)
        initPaint()
        initView()
    }

    ///////////////////////////////////////////////////////////////////////////////

    fun startLoad() {
        if (mAnimList != null && mAnimList!!.size > 0 &&
            !mAnimatorSet!!.isRunning && !mAnimatorSet!!.isStarted
        ) {
            mAnimatorSet!!.playTogether(mAnimList)
            mAnimatorSet!!.start()
        }
    }

    fun stopLoad() {
        mAnimatorSet!!.cancel()
    }

    ///////////////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        attrs ?: return
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewKProgressWaveImage)
        try {
            mMode =
                typedArray.getInteger(R.styleable.ViewKProgressWaveImage_viewKProgressWaveImage_mode, mMode)
            mIsAutoLoad =
                typedArray.getBoolean(R.styleable.ViewKProgressWaveImage_viewKProgressWaveImage_autoLoad, mIsAutoLoad)
            mIsShowText =
                typedArray.getBoolean(R.styleable.ViewKProgressWaveImage_viewKProgressWaveImage_text, mIsShowText)
            mTextSize =
                typedArray.getDimensionPixelSize(R.styleable.ViewKProgressWaveImage_viewKProgressWaveImage_textSize, mTextSize)
            mDuration =
                typedArray.getInteger(R.styleable.ViewKProgressWaveImage_viewKProgressWaveImage_duration, mDuration)
            mIconResId =
                typedArray.getResourceId(R.styleable.ViewKProgressWaveImage_viewKProgressWaveImage_src, mIconResId)
            mBallColor =
                typedArray.getColor(R.styleable.ViewKProgressWaveImage_viewKProgressWaveImage_color, mBallColor)
            mBallStrokeWidth =
                typedArray.getDimension(R.styleable.ViewKProgressWaveImage_viewKProgressWaveImage_strokeWidth, mBallStrokeWidth)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun initPaint() {
        mIconPaint = Paint()
        mIconPaint.isFilterBitmap = true
        mIconPaint.isAntiAlias = true
        mIconPaint.isDither = true

        mBallPaint = Paint()
        mBallPaint.style = Paint.Style.STROKE
        mBallPaint.strokeWidth = mBallStrokeWidth
        mBallPaint.color = mBallColor
        mBallPaint.isAntiAlias = true
        mBallPaint.isDither = true

        mTextPaint = Paint()
        mTextPaint.textSize = mTextSize.toFloat()
        mTextPaint.color = Color.WHITE
    }

    override fun initView() {
        initAnim()

        mDuffXfermode = if (mMode == ICON_STYLE) {
            PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        } else {
            PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        mWidth = (right - left).toFloat()
        mHeight = (bottom - top).toFloat()
        //创建圆形遮罩图
        createBallBitmap()

        //进度动画和波浪高度动画需要Height的值
        mProgressAnim!!.setIntValues(mHeight.toInt(), 0)
        mWaveHeightAnim!!.setIntValues(0, mHeight.toInt() / 2, 0)
        mWaterTop = mHeight
        //避免layout多次调用多次启动动画
        if (mIsAutoLoad && mAnimList != null && mAnimList!!.size > 0 && !mAnimatorSet!!.isRunning && !mAnimatorSet!!.isStarted) {
            mAnimatorSet!!.playTogether(mAnimList)
            mAnimatorSet!!.start()
        }
    }

    private fun createBallBitmap() {
        mBallBitmap = Bitmap.createBitmap(mWidth.toInt(), mHeight.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mBallBitmap!!)
        canvas.drawCircle(mWidth / 2f, mHeight / 2f, mWidth / 2f - mBallStrokeWidth * 3f / 2f, mIconPaint)

        val icBitmap = BitmapFactory.decodeResource(resources, mIconResId)
        val width = icBitmap.width
        val height = icBitmap.height
        // 计算缩放比例
        val scaleWidth = mWidth / width
        val scaleHeight = mHeight / height
        // 取得想要缩放的matrix参数
        val matrix: Matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        // 得到新的图片
        mIconBitmap = Bitmap.createBitmap(icBitmap, 0, 0, width, height, matrix, true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mWavePath1.reset()
        val halfItem = mItemWidth / 2f
        //为了闭合整个View, 否则波浪遮罩顶部显示不正常
        mWavePath1.moveTo(0f, 0f)
        //必须先减去一个浪的宽度，以便第一遍动画能够刚好位移出一个波浪，形成无限波浪的效果
        mWavePath1.lineTo(-mItemWidth + mOffsetX1, mWaterTop)
        run {
            var i = -mItemWidth
            while (i < mItemWidth + mWidth) {
                mWavePath1.rQuadTo(halfItem / 2f, -mWaveHeight, halfItem, 0f)
                mWavePath1.rQuadTo(halfItem / 2f, mWaveHeight, halfItem, 0f)
                i += mItemWidth
            }
        }

        //闭合路径波浪以下区域
        mWavePath1.lineTo(mWidth, mHeight)
        mWavePath1.lineTo(0f, mHeight)
        mWavePath1.lineTo(-mItemWidth + mOffsetX1, mWaterTop)
        mWavePath1.close()

        mWavePath2.reset()
        val wave2ItemWidth = mItemWidth / 2
        val halfItem2 = wave2ItemWidth / 2
        //为了闭合整个View, 否则波浪遮罩顶部显示不正常
        mWavePath2.moveTo(0f, 0f)
        //必须先减去一个浪的宽度，以便第一遍动画能够刚好位移出一个波浪，形成无限波浪的效果
        mWavePath2.lineTo(-wave2ItemWidth + mOffsetX2, mWaterTop)
        var i = -wave2ItemWidth
        while (i < wave2ItemWidth + mWidth) {
            mWavePath2.rQuadTo(halfItem2 / 2, -mWaveHeight / 2, halfItem2, 0f)
            mWavePath2.rQuadTo(halfItem2 / 2, mWaveHeight / 2, halfItem2, 0f)
            i += wave2ItemWidth
        }

        //闭合路径波浪以下区域
        mWavePath2.lineTo(mWidth, mHeight)
        mWavePath2.lineTo(0f, mHeight)
        mWavePath2.lineTo(-wave2ItemWidth + mOffsetX2, mWaterTop)
        mWavePath2.close()

        //隔离层绘制混合模式
        val layerId = canvas.saveLayer(0f, 0f, mWidth, mHeight, null, Canvas.ALL_SAVE_FLAG)
        //绘制Icon
        if (mMode == COLOR_STYLE) {
            mBallPaint.style = Paint.Style.FILL
            //绘制水波纹1
            canvas.drawPath(mWavePath1, mBallPaint)
            //绘制水波纹2
            canvas.drawPath(mWavePath2, mBallPaint)
            mIconPaint.setXfermode(mDuffXfermode)
            //绘制圆形位图
            canvas.drawBitmap(mBallBitmap!!, 0f, 0f, mIconPaint)
            mIconPaint.setXfermode(null)
        } else {
            //绘制水波纹1
            canvas.drawPath(mWavePath1, mIconPaint)
            //绘制水波纹2
            canvas.drawPath(mWavePath2, mIconPaint)
            mIconPaint.setXfermode(mDuffXfermode)
            //绘制圆形位图
            canvas.drawBitmap(mBallBitmap!!, 0f, 0f, mIconPaint)
            canvas.drawBitmap(mIconBitmap!!, 0f, 0f, mIconPaint)
            mIconPaint.setXfermode(null)
        }
        canvas.restoreToCount(layerId)

        //绘制中心百分比文本
        if (mMode == COLOR_STYLE && mIsShowText) {
            mBallPaint.style = Paint.Style.STROKE
            canvas.drawCircle(mWidth / 2f, mHeight / 2f, mWidth / 2f - mBallStrokeWidth, mBallPaint)
            val textWidth = mTextPaint.measureText("$mProgress%")
            val fontMetrics = mTextPaint.fontMetrics
            val baseLine = mHeight / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2
            canvas.drawText("$mProgress%", mWidth / 2f - textWidth / 2, baseLine, mTextPaint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //防止内存泄漏
        if (mAnimatorSet != null) {
            mAnimatorSet!!.cancel()
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 初始化所有动画器
     */
    private fun initAnim() {
        mAnimatorSet = AnimatorSet()
        mAnimList = ArrayList()

        val mOffsetAnimator1 = ValueAnimator.ofFloat(0f, mItemWidth)
        mOffsetAnimator1.addUpdateListener { animation ->
            mOffsetX1 = animation.animatedValue as Float
            invalidate()
        }

        mOffsetAnimator1.interpolator = LinearInterpolator()

        mOffsetAnimator1.setDuration(500)
        mOffsetAnimator1.repeatCount = -1

        val mOffsetAnimator2 = ValueAnimator.ofFloat(0f, mItemWidth / 2f)
        mOffsetAnimator2.addUpdateListener { animation ->
            mOffsetX2 = animation.animatedValue as Float
            invalidate()
        }

        mOffsetAnimator2.interpolator = LinearInterpolator()

        mOffsetAnimator2.setDuration(800)
        mOffsetAnimator2.repeatCount = -1

        mProgressAnim = ValueAnimator.ofInt()
        mProgressAnim!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            mWaterTop = (animation.animatedValue as Int).toFloat()
            val progress = (-mWaterTop / mHeight + 1) * 100
            mProgress = progress.toInt()
            invalidate()
        })

        mProgressAnim!!.setInterpolator(AccelerateDecelerateInterpolator())
        mProgressAnim!!.setDuration(mDuration.toLong())

        mWaveHeightAnim = ValueAnimator.ofInt()
        mWaveHeightAnim!!.addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
            mWaveHeight = animation.animatedValue as Int / 6f
            invalidate()
        })

        mWaveHeightAnim!!.setInterpolator(AccelerateDecelerateInterpolator())
        mWaveHeightAnim!!.setDuration(mDuration.toLong())

        mAnimList!!.add(mOffsetAnimator1)
        mAnimList!!.add(mOffsetAnimator2)
        mAnimList!!.add(mProgressAnim!!)
        mAnimList!!.add(mWaveHeightAnim!!)
    }
}