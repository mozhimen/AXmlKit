package com.mozhimen.xmlk.layoutk.roll

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.mozhimen.animk.builder.commons.IAnimatorUpdateListener
import com.mozhimen.animk.builder.impls.AnimatorFloatType
import com.mozhimen.kotlin.lintk.annors.Sp
import com.mozhimen.kotlin.utilk.android.animation.cancel_removeAllListeners
import com.mozhimen.kotlin.utilk.android.util.UtilKLogWrapper
import com.mozhimen.kotlin.utilk.android.widget.applyTypeface
//import com.mozhimen.xmlk.interpolatork.InterpolatorKSpring
import com.mozhimen.xmlk.bases.BaseLayoutKFrame
import com.mozhimen.xmlk.layoutk.roll.annors.AAnimatorMode

/**
 * @ClassName LayoutKRollText
 * @Description TODO
 * @Author Mozhimen / Kolin Zhao
 * @Date 2023/9/12 17:51
 * @Version 1.0
 */
open class LayoutKRollText @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseLayoutKFrame(context, attrs, defStyleAttr) {

    private lateinit var _textView1: TextView
    private lateinit var _textView2: TextView
    private var _rollAnimator: Animator? = null
    private var _tvWidth = 0
    private var _tvHeight = 0
    private var _currentValue = ""

    private var _animatorMode = AAnimatorMode.UP
    private var _animatorDuration = 300L
    private var _tvTextSize = 14f
    private var _tvTextColor = Color.BLACK

    private val _animatorUpdateListener: IAnimatorUpdateListener<Float> by lazy {
        object : IAnimatorUpdateListener<Float> {
            override fun onChange(value: Float?) {
                value?.let {
                    UtilKLogWrapper.d(TAG, "onChange $it")
                    if (_animatorMode == AAnimatorMode.UP) {
                        _textView1.translationY = -_tvHeight * value
                        _textView2.translationY = -_tvHeight * value
                    } else {
                        _textView1.translationY = _tvHeight * value
                        _textView2.translationY = -2 * _tvHeight + _tvHeight * value
                    }
                }
            }
        }
    }
    //////////////////////////////////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs, defStyleAttr)
        initView()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKRollText)
        _animatorMode = typedArray.getInt(R.styleable.LayoutKRollText_layoutKRollText_animatorMode, _animatorMode)
        _animatorDuration = typedArray.getInt(R.styleable.LayoutKRollText_layoutKRollText_animatorDuration, _animatorDuration.toInt()).toLong()
        _tvTextSize = typedArray.getFloat(R.styleable.LayoutKRollText_layoutKRollText_textSize, _tvTextSize)
        _tvTextColor = typedArray.getColor(R.styleable.LayoutKRollText_layoutKRollText_textColor, _tvTextColor)
        typedArray.recycle()
    }

    override fun initView() {
        LayoutInflater.from(context).inflate(R.layout.layoutk_roll_text, this, true)
        _textView1 = findViewById(R.id.tv_number_one)
        _textView2 = findViewById(R.id.tv_number_tow)

//        _rollAnimator = ValueAnimator.ofFloat(0f, 1f)
//        _rollAnimator!!.setDuration(400)
//        _rollAnimator!!.setInterpolator(OvershootInterpolator())
//        _rollAnimator!!.setRepeatCount(0)
//        _rollAnimator!!.setRepeatMode(ValueAnimator.RESTART)
//        _rollAnimator!!.addUpdateListener { animation: ValueAnimator ->
//            val value = animation.animatedValue as Float
//            if (_animatorMode == AAnimatorMode.UP) {
//                _textView1.translationY = -mHeight * value
//                _textView2.translationY = -mHeight * value
//            } else {
//                _textView1.translationY = mHeight * value
//                _textView2.translationY = -2 * mHeight + mHeight * value
//            }
//        }
        setTextViewStyle(_tvTextSize, _tvTextColor)

        _rollAnimator = AnimatorFloatType().setFloat(0f, 1f)
            .addAnimatorUpdateListener(_animatorUpdateListener)
//            .isAutoClearListener(false)
            .setDuration(_animatorDuration)
            .setInterpolator(LinearInterpolator()/*InterpolatorKSpring()*/).build() as ValueAnimator
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setTextViewSize(width, height)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDetachedFromWindow() {
        _rollAnimator?.cancel_removeAllListeners()
        if (_rollAnimator?.isRunning == true)
            _rollAnimator!!.cancel()
        _rollAnimator = null
        super.onDetachedFromWindow()
    }

    //////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置宽、高、字体大小
     */
    fun setTextViewSize(tvWidth: Int, tvHeight: Int) {
        if (_tvWidth == tvWidth) return
        _tvWidth = tvWidth
        _tvHeight = tvHeight

        _textView1.layoutParams = LayoutParams(_tvWidth, _tvHeight)
        _textView1.gravity = Gravity.CENTER
        _textView1.applyTypeface(Typeface.BOLD)

        _textView2.layoutParams = LayoutParams(_tvWidth, _tvHeight).apply { topMargin = _tvHeight }
        _textView2.gravity = Gravity.CENTER
        _textView2.applyTypeface(Typeface.BOLD)
    }

    fun setTextViewStyle(@Sp textSize: Float, textColor: Int) {
        _tvTextSize = textSize
        _tvTextColor = textColor
        _textView1.textSize = _tvTextSize
        _textView1.setTextColor(_tvTextColor)
        _textView2.textSize = _tvTextSize
        _textView2.setTextColor(_tvTextColor)
    }

    /**
     * 设置下一位数字的值
     * @param value Int
     * @param mode Int
     */
    open fun setCurrentValue(value: String, animatorDuration: Long = _animatorDuration, @AAnimatorMode mode: Int = _animatorMode) {
        if (value == _currentValue) {
            UtilKLogWrapper.d(TAG, "value is same skip")
            return
        }
        _animatorMode = mode
        _animatorDuration = animatorDuration

        _textView1.text = _currentValue
        _textView2.text = value

        if (_rollAnimator!!.isRunning)
            _rollAnimator!!.cancel()

        _rollAnimator!!.setDuration(_animatorDuration)
        _textView1.translationY = 0f
        _textView2.translationY = 0f
        _rollAnimator!!.start()

        _currentValue = value
    }
}