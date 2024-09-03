package com.mozhimen.xmlk.viewk

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * @ClassName ViewKProgress
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/3
 * @Version 1.0
 */
/**
 * a linear progress bar
 */
inline fun ViewGroup.viewKProgress(autoAdd: Boolean = true, init: ViewKProgress.() -> Unit) =
    ViewKProgress(context).apply(init).also { if (autoAdd) addView(it) }

inline fun Context.viewKProgress(init: ViewKProgress.() -> Unit): ViewKProgress =
    ViewKProgress(this).apply(init)

inline fun Fragment.viewKProgress(init: ViewKProgress.() -> Unit) =
    context?.let { ViewKProgress(it).apply(init) }

////////////////////////////////////////////////////////////////////////////

class ViewKProgress @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    var progressBackgroundColor: Int = Color.parseColor("#00ff00")
        set(value) {
            field = value
            backgroundPaint.color = value
        }
    var progressForegroundColor: Int = Color.parseColor("#ff00ff")
        set(value) {
            field = value
            foreGroundPaint.color = value
        }

    var colors = intArrayOf()

    var backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = progressBackgroundColor
        style = Paint.Style.FILL
    }

    var foreGroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = progressForegroundColor
        style = Paint.Style.FILL
    }

    var foregroundRectF = RectF()
    var backgroundRectF = RectF()

    var rx: Float = 0f
    var ry: Float = 0f

    var progress: Float = 0f

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        foreGroundPaint.shader =
            if (colors.isEmpty()) null else LinearGradient(0f, 0f, width * progress, height.toFloat(), colors, null, Shader.TileMode.CLAMP)
        backgroundRectF.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(backgroundRectF, rx, ry, backgroundPaint)
        foregroundRectF.set(0f, 0f, width * progress, height.toFloat())
        canvas.drawRoundRect(foregroundRectF, rx, ry, foreGroundPaint)
    }
}