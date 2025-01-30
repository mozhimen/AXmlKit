package com.mozhimen.xmlk.textk.progress

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.os.Parcelable
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import com.mozhimen.kotlin.lintk.annors.Dp
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.kotlin.ranges.constraint
import com.mozhimen.kotlin.utilk.kotlin.ranges.percent
import com.mozhimen.xmlk.bases.BaseTextK
import com.mozhimen.xmlk.commons.IViewK
import com.mozhimen.xmlk.textk.progress.cons.CProgressState
import com.mozhimen.xmlk.textk.progress.helpers.ProgressSavedState

/**
 * @ClassName TextKProgress
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/10/22 18:35
 * @Version 1.0s
 */
class TextKProgress2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : BaseTextK(context, attrs, defStyleAttr), IViewK {

    private var _backgroundColorIdle = Color.WHITE//背景颜色
    private var _backgroundColorLoaded = Color.parseColor("#00A667")
    private var _backgroundColorUnload = _backgroundColorIdle//下载中后半部分后面背景颜色
    private var _backgroundColorOver = _backgroundColorLoaded
    private var _textPadding = 2.dp2px()
    private var _textColorIdle = Color.parseColor("#00A667")//文字颜色
    private var _textColorLoaded = Color.WHITE//覆盖后颜色
    private var _textColorUnload = _textColorIdle
    private var _textColorOver = _textColorLoaded
    private var _borderRadius = 0f
    private var _borderWidthIdle = 0f.dp2px()//边框宽度
    private var _borderWidthLoad = 2f.dp2px()//边框宽度
    private var _borderWidthOver = _borderWidthIdle//边框宽度
    private var _borderColorIdle = Color.parseColor("#BFE8D9")//边框颜色
    private var _borderColorLoad = _backgroundColorLoaded
    private var _borderColorOver = _borderColorIdle
    private var _minProgress = 0
    val minProgress get() = _minProgress
    private var _maxProgress = 100
    val maxProgress get() = _maxProgress
    private var _progress = 0
        set(value) {
            val progress = value.constraint(_minProgress.._maxProgress)
            _percent = progress.percent(_minProgress, _maxProgress)
            field = progress
        }
    val progress get() = _progress
    private var _progressState = CProgressState.PROGRESS_STATE_IDLE
    val progressState get() = _progressState
    private var _content: CharSequence = ""//记录当前文字
    val content: CharSequence get() = _content

    //////////////////////////////////////////////////////////////////

    private var _percent = 0f
    private var _percentIntermediate = _percent
    private var _textOffsetBottomY = 0f
    private var _textOffsetRightX = 0f

    //////////////////////////////////////////////////////////////////

    private lateinit var _paintText: TextPaint//按钮文字画笔
    private lateinit var _paintBackground: Paint//背景画笔
    private var _linearGradientProgress: LinearGradient? = null
    private val _boundBorder = RectF()
    private val _boundBackground = RectF()
    private var _valueAnimatorProgress: ValueAnimator? = null

    //////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
        initPaint()
    }

    //////////////////////////////////////////////////////////////////

    override fun initPaint() {
        //设置背景画笔
        _paintBackground = Paint()
        _paintBackground.isAntiAlias = true
        _paintBackground.style = Paint.Style.FILL
        //设置文字画笔
        _paintText = TextPaint()
        _paintText.isAntiAlias = true
        _paintText.textSize = textSize
        _paintText.typeface = typeface
        _paintText.textAlign = Paint.Align.CENTER
        //解决文字有时候画不出问题
        setLayerType(LAYER_TYPE_SOFTWARE, _paintText)
    }

    override fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextKProgress2)
        try {
            _backgroundColorIdle =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_backgroundColorIdle, _backgroundColorIdle)
            _backgroundColorLoaded =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_backgroundColorLoaded, _backgroundColorLoaded)
            _backgroundColorUnload =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_backgroundColorUnload, _backgroundColorIdle)
            _backgroundColorOver =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_backgroundColorOver, _backgroundColorLoaded)

            //

            _textColorIdle =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_textColorIdle, _textColorIdle)
            _textColorLoaded =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_textColorLoaded, _textColorLoaded)
            _textColorUnload =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_textColorUnload, _textColorIdle)
            _textColorOver =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_textColorOver, _textColorLoaded)

            //

            _borderRadius =
                typedArray.getDimension(R.styleable.TextKProgress2_textKProgress2_borderRadius, _borderRadius)
            _borderWidthIdle =
                typedArray.getDimension(R.styleable.TextKProgress2_textKProgress2_borderWidthIdle, _borderWidthIdle)
            _borderWidthLoad =
                typedArray.getDimension(R.styleable.TextKProgress2_textKProgress2_borderWidthLoad, _borderWidthLoad)
            _borderWidthOver =
                typedArray.getDimension(R.styleable.TextKProgress2_textKProgress2_borderWidthOver, _borderWidthIdle)

            //

            _borderColorIdle =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_borderColorIdle, _borderColorIdle)
            _borderColorLoad =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_borderColorLoad, _backgroundColorLoaded)
            _borderColorOver =
                typedArray.getColor(R.styleable.TextKProgress2_textKProgress2_textColorOver, _borderColorIdle)

            //

            _minProgress =
                typedArray.getInt(R.styleable.TextKProgress2_textKProgress2_minProgress, _minProgress)
            _maxProgress =
                typedArray.getInt(R.styleable.TextKProgress2_textKProgress2_maxProgress, _maxProgress)
            _progress =
                typedArray.getInt(R.styleable.TextKProgress2_textKProgress2_progress, _progress)
            _progressState =
                typedArray.getInt(R.styleable.TextKProgress2_textKProgress2_progressState, _progressState)
            _content =
                typedArray.getString(R.styleable.TextKProgress2_textKProgress2_content) ?: ""
        } finally {
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawText(canvas)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimatorProgress()
    }

    //////////////////////////////////////////////////////////////////

    override fun onRestoreInstanceState(state: Parcelable) {
        val progressSavedState = state as ProgressSavedState
        super.onRestoreInstanceState(progressSavedState.superState)
        _progressState = progressSavedState.progressState
        _progress = progressSavedState.progress
        _content = progressSavedState.charSequence
    }

    override fun onSaveInstanceState(): Parcelable {
        return ProgressSavedState(super.onSaveInstanceState(), _progress, _progressState, _content.toString())
    }

    //////////////////////////////////////////////////////////////////

    fun setBackgroundColorIdle(@ColorInt backgroundColor: Int) {
        _backgroundColorIdle = backgroundColor
        invalidate()
    }

    fun setBackgroundColorLoad(@ColorInt backgroundColorLoaded: Int, @ColorInt backgroundColorUnload: Int) {
        _backgroundColorLoaded = backgroundColorLoaded
        _backgroundColorUnload = backgroundColorUnload
        invalidate()
    }

    fun setBackgroundColorOver(@ColorInt backgroundColorOver: Int) {
        _backgroundColorOver = backgroundColorOver
        invalidate()
    }

    fun setTextColorIdle(@ColorInt textColorIdle: Int) {
        _textColorIdle = textColorIdle
        invalidate()
    }

    fun setTextColorLoad(@ColorInt textColorLoaded: Int, @ColorInt textColorUnload: Int) {
        _textColorLoaded = textColorLoaded
        _textColorUnload = textColorUnload
        invalidate()
    }

    fun setTextColorOver(@ColorInt textColorOver: Int) {
        _textColorOver = textColorOver
        invalidate()
    }

    fun setBorderRadius(buttonRadius: Float) {
        _borderRadius = buttonRadius
        invalidate()
    }

    fun setBorderWidthIdle(@Dp borderWidthIdle: Float) {
        _borderWidthIdle = borderWidthIdle
        invalidate()
    }

    fun setBorderWidthLoad(@Dp borderWidthLoad: Float) {
        _borderWidthLoad = borderWidthLoad
        invalidate()
    }

    fun setBorderWidthOver(@Dp borderWidthOver: Float) {
        _borderWidthOver = borderWidthOver
        invalidate()
    }

    fun setBorderColorIdle(@ColorInt borderColorIdle: Int) {
        _borderColorIdle = borderColorIdle
        invalidate()
    }

    fun setBorderColorLoad(@ColorInt borderColorLoad: Int) {
        _borderColorLoad = borderColorLoad
        invalidate()
    }

    fun setBorderColorOver(@ColorInt borderColorOver: Int) {
        _borderColorOver = borderColorOver
        invalidate()
    }

    /**
     * @param immediate 是否立即执行
     */
    fun setProgress(progress: Int, immediate: Boolean) {
        _progress = progress
        if (immediate) {
            _percentIntermediate = _percent
            invalidate()
        } else {
            setPercent()
        }
    }

    fun setProgressState(state: Int) {
        _progressState = state
        invalidate()
    }

    /**
     * 设置当前按钮文字
     */
    fun setContent(charSequence: CharSequence) {
        _content = charSequence
        invalidate()
    }

    /**
     * 设置带下载进度的文字
     */
    fun setContentAndProgress(charSequence: String, progress: Int, immediate: Boolean) {
        _content = charSequence
        setProgress(progress, immediate)
    }

    //////////////////////////////////////////////////////////////////

    private fun setPercent() {
        val curPercent = _percentIntermediate
        val desPercent = _percent
        Log.d(TAG, "setPercent: curPercent $curPercent desPercent $desPercent")
        if (desPercent == 1f) {
            stopAnimatorProgress()
            startAnimatorProgress(curPercent, desPercent)
            return
        }
        if (curPercent != desPercent && _valueAnimatorProgress == null) {
            startAnimatorProgress(curPercent, desPercent)
        }
    }

    private fun startAnimatorProgress(start: Float, end: Float) {
        if (_valueAnimatorProgress == null) {
            _valueAnimatorProgress = ValueAnimator.ofFloat(start, end)
            _valueAnimatorProgress!!.interpolator = LinearInterpolator() //匀速插值器 解决卡顿问题
            _valueAnimatorProgress!!.setDuration(800)
            _valueAnimatorProgress!!.addUpdateListener { animation ->
                _percentIntermediate = animation.animatedValue as Float
                Log.d(TAG, "startAnimatorProgress: _percentIntermediate $_percentIntermediate")
                invalidate()
            }
            _valueAnimatorProgress!!.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                }

                override fun onAnimationEnd(animation: Animator) {
                    _valueAnimatorProgress = null
                }

                override fun onAnimationCancel(animation: Animator) {
                }

                override fun onAnimationRepeat(animation: Animator) {
                }
            })
            _valueAnimatorProgress!!.start()
        }
    }

    private fun stopAnimatorProgress() {
        _valueAnimatorProgress?.removeAllUpdateListeners()
        _valueAnimatorProgress = null
    }

    private fun drawText(canvas: Canvas) {
        //计算Baseline绘制的Y坐标
        val y = canvas.height / 2 - (_paintText.descent() / 2 + _paintText.ascent() / 2)
        val content = TextUtils.ellipsize(_content, _paintText, measuredWidth - 2f * _textPadding, TextUtils.TruncateAt.END).toString()
        val textWidth = _paintText.measureText(content)
        _textOffsetBottomY = y
        _textOffsetRightX = (measuredWidth + textWidth) / 2
        when (_progressState) {
            CProgressState.PROGRESS_STATE_IDLE -> {
                _paintText.setShader(null)
                _paintText.color = _textColorIdle
                canvas.drawText(content, measuredWidth  / 2f, y, _paintText)
            }

            CProgressState.PROGRESS_STATE_LOAD -> {
                val coverLength = measuredWidth * _percentIntermediate//进度条压过距离
                val indicatorLoaded = measuredWidth / 2.0f - textWidth / 2//开始渐变指示器
                val indicatorUnload = measuredWidth / 2.0f + textWidth / 2//结束渐变指示器
                //文字变色部分的距离
                val coverTextLength = textWidth / 2 - measuredWidth / 2.0f + coverLength
                val textProgress = coverTextLength / textWidth
                when {
                    coverLength <= indicatorLoaded -> {
                        _paintText.setShader(null)
                        _paintText.color = _textColorUnload
                    }


                    indicatorLoaded < coverLength && coverLength <= indicatorUnload -> {
                        _linearGradientProgress = LinearGradient(
                            (measuredWidth - textWidth) / 2,
                            0f,
                            (measuredWidth + textWidth) / 2,
                            0f,
                            intArrayOf(_textColorLoaded, _textColorUnload),
                            floatArrayOf(textProgress, textProgress + 0.001f),
                            Shader.TileMode.CLAMP
                        )//设置变色效果
                        _paintText.color = _textColorLoaded
                        _paintText.setShader(_linearGradientProgress)
                    }

                    else -> {
                        _paintText.setShader(null)
                        _paintText.color = _textColorLoaded
                    }
                }
                canvas.drawText(content, measuredWidth  / 2f, y, _paintText)
            }

            else -> {
                _paintText.setShader(null)
                _paintText.color = _textColorOver
                canvas.drawText(content, measuredWidth  / 2f, y, _paintText)
            }
        }
    }

    private fun drawBackground(canvas: Canvas) {
        when (_progressState) {
            CProgressState.PROGRESS_STATE_IDLE -> {
                generateBounds(_borderWidthIdle)
                if (_borderWidthIdle >= 1f) {
                    _paintBackground.style = Paint.Style.STROKE
                    _paintBackground.color = _borderColorIdle
                    _paintBackground.strokeWidth = _borderWidthIdle
                    canvas.drawRoundRect(_boundBorder, _borderRadius, _borderRadius, _paintBackground)
                }
                _paintBackground.style = Paint.Style.FILL
                _paintBackground.color = _backgroundColorIdle
                canvas.drawRoundRect(_boundBackground, _borderRadius, _borderRadius, _paintBackground)
            }

            CProgressState.PROGRESS_STATE_LOAD -> {
                generateBounds(_borderWidthLoad)
                if (_borderWidthLoad >= 1f) {
                    _paintBackground.style = Paint.Style.STROKE
                    _paintBackground.color = _borderColorLoad
                    _paintBackground.strokeWidth = _borderWidthLoad
                    canvas.drawRoundRect(_boundBorder, _borderRadius, _borderRadius, _paintBackground)
                }
                _paintBackground.style = Paint.Style.FILL
                _paintBackground.color = _backgroundColorUnload
                canvas.drawRoundRect(_boundBackground, _borderRadius, _borderRadius, _paintBackground)

                canvas.save()
                //画进度条
                //设置图层显示模式为 SRC_ATOP
                _paintBackground.color = _backgroundColorLoaded
                _paintBackground.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP))
                //在dst画出src矩形//计算 src 矩形的右边界
                _boundBackground.right *= _percentIntermediate
                Log.d(TAG, "drawBackground: _boundBackground.right ${_boundBackground.right}")
                canvas.drawRoundRect(_boundBackground, _borderRadius, _borderRadius, _paintBackground)
                canvas.restore()
                _paintBackground.setXfermode(null)
            }


            else -> {
                generateBounds(_borderWidthOver)
                if (_borderWidthOver >= 1f) {
                    _paintBackground.style = Paint.Style.STROKE
                    _paintBackground.color = _borderColorOver
                    _paintBackground.strokeWidth = _borderWidthOver
                    canvas.drawRoundRect(_boundBorder, _borderRadius, _borderRadius, _paintBackground)
                }
                _paintBackground.style = Paint.Style.FILL
                _paintBackground.color = _backgroundColorOver
                canvas.drawRoundRect(_boundBackground, _borderRadius, _borderRadius, _paintBackground)
            }
        }
    }

    private fun generateBounds(border: Float) {
        _boundBorder.apply {
            left = border / 2f
            top = border / 2f
            right = measuredWidth - border / 2f
            bottom = measuredHeight - border / 2f
        }
        _boundBackground.apply {
            left = border
            top = border
            right = measuredWidth - border
            bottom = measuredHeight - border
        }
    }
}