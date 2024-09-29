package com.mozhimen.xmlk.viewk.progress

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.IntRange
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.kotlin.ranges.constraint
import com.mozhimen.xmlk.bases.BaseViewK

/**
 * @ClassName ViewKProgressCircle
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Date 2023/9/26 17:31
 * @Version 1.0
 */

class ViewKProgressCircle @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : BaseViewK(context, attrs, defStyle) {
    private lateinit var _paint: Paint
    private var _progress = 0f
    private var _rectF: RectF = RectF()

    init {
        initPaint()
    }

    /**
     * 设置进度。符合条件则更新进度条
     */
    fun setProgress(@IntRange(from = 0, to = 100) progress: Int) {
        _progress = progress.constraint(0, 100) * 0.01f
        invalidate()
    }

    /**
     * @return 当前进度
     */
    fun getProgress(): Double {
        return _progress.toDouble()
    }

    ///////////////////////////////////////////////////////////////////////////

    override fun initPaint() {
        _paint = Paint().apply {
            isAntiAlias = true
            strokeWidth = 2f.dp2px()
        }
    }

    override fun onDraw(canvas: Canvas) {
        //绘制背景
        _paint.color = Color.WHITE
        _paint.style = Paint.Style.STROKE
        canvas.drawCircle(centerX, centerX, centerX - 5, _paint)
        _paint.color = Color.WHITE
        if (_rectF.isEmpty) {
            val locationX: Float = 3f.dp2px() + 5f
            _rectF[locationX, locationX, width - locationX] = height - locationX
        }
        _paint.style = Paint.Style.FILL
        //奇葩的，为0时是从右下角顺时针绘制，因此减去180度
        canvas.drawArc(_rectF, -90f, _progress * 360, true, _paint)
    }
}

