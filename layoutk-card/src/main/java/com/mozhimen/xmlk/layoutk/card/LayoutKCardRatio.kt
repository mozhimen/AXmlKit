package com.mozhimen.xmlk.layoutk.card

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView
import com.mozhimen.xmlk.basic.commons.ILayoutK
import kotlin.math.min

/**
 * @ClassName LayoutKCardRatio
 * @Description TODO
 * @Author mozhimen
 * @Date 2025/4/10
 * @Version 1.0
 */
class LayoutKCardRatio @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : CardView(context, attrs, defStyleAttr), ILayoutK {

    companion object {
        const val RATIO_NONE = 0
        const val RATIO_1_1 = 1
        const val RATIO_3_4 = 2
    }

    private var _ratioMode = RATIO_NONE
    private val _aspectRatio: Float
        get() = (when (_ratioMode) {
            RATIO_3_4 -> 4f / 3f
            else -> 1f
        })

    /////////////////////////////////////////////////////////////////

    init {
        initAttrs(attrs)
    }

    /////////////////////////////////////////////////////////////////

    override fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LayoutKCardRatio)
            _ratioMode = typedArray.getInt(R.styleable.LayoutKCardRatio_layoutKCardRatio_ratioMode, _ratioMode)
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var desiredWidth = widthSize
        var desiredHeight = (desiredWidth * _aspectRatio).toInt()

        // 处理宽度为wrap_content的情况
        if (widthMode == MeasureSpec.AT_MOST) {
            // 先测量内容获取最小宽度
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            val minWidth = measuredWidth
            desiredWidth = min(widthSize.toDouble(), minWidth.toDouble()).toInt()
            desiredHeight = (desiredWidth * _aspectRatio).toInt()
        }

        // 处理高度约束
        if (heightMode == MeasureSpec.EXACTLY) {
            // 尊重精确高度要求
            desiredHeight = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            desiredHeight = min(desiredHeight.toDouble(), heightSize.toDouble()).toInt()
        }

        // 构造新的测量规格
        val newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(desiredWidth, MeasureSpec.EXACTLY)
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY)

        // 最终测量
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec)
    }
}