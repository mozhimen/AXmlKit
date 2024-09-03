package com.mozhimen.xmlk.layoutk.oneview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.annotation.RequiresApi
import com.mozhimen.kotlin.elemk.android.os.cons.CVersCode
import com.mozhimen.kotlin.utilk.android.os.UtilKBuildVersion
import com.mozhimen.kotlin.utilk.android.util.dp2pxI
import com.mozhimen.kotlin.utilk.android.util.sp2px
import com.mozhimen.kotlin.utilk.android.util.sp2pxI
import com.mozhimen.xmlk.layoutk.LayoutKSelector
import kotlin.math.min

/**
 * @ClassName DrawableText
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/3
 * @Version 1.0
 */
/**
 *  one kind of [OneViewGroup.Drawable] shows text
 */
inline fun LayoutKOneView.drawableText(init: DrawableText.() -> Unit) :DrawableText=
    DrawableText().apply(init).also { addDrawable(it) }

inline var DrawableText.drawable_align: Layout.Alignment
    get() =Layout.Alignment.ALIGN_NORMAL
    set(value) {
        gravity = value
    }

inline var DrawableText.drawable_text_size: Float
    get() =0f
    set(value) {
        textSize = value.sp2px()
    }

inline var DrawableText.drawable_max_width: Int
    get() =0
    set(value) {
        maxWidth = value.dp2pxI()
    }

inline var DrawableText.drawable_layout_width: Int
    get() =0
    set(value) {
        layoutWidth = value.dp2pxI()
    }

inline var DrawableText.drawable_layout_height: Int
    get() =0
    set(value) {
        layoutHeight = value.dp2pxI()
    }

inline var DrawableText.drawable_text_color: String
    get()=""
    set(value) {
        textColor = Color.parseColor(value)
    }

inline var DrawableText.drawable_text: CharSequence
    get()= ""
    set(value) {
        text = value
    }

inline var DrawableText.drawable_max_lines: Int
    get() =1
    set(value) {
        maxLines = value
    }

inline var DrawableText.drawable_shape: LayoutKOneView.Shape
    get() =LayoutKOneView.Shape()
    set(value) {
        drawableShape = value
    }
/////////////////////////////////////////////////////////////////////

class DrawableText : LayoutKOneView.Drawable {
    private var textPaint: TextPaint? = null
    private var staticLayout: StaticLayout? = null

    var text: CharSequence = ""
    var textSize: Float = 0f
    var textColor: Int = Color.parseColor("#ffffff")
    var spaceAdd: Float = 0f
    var spaceMulti: Float = 1.0f
    var maxLines = 1
    var maxWidth = 0
    var gravity: Layout.Alignment = Layout.Alignment.ALIGN_NORMAL
    var shapePaint: Paint? = null
    var drawableShape: LayoutKOneView.Shape? = null
        set(value) {
            field = value
            shapePaint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = Color.parseColor(value?.color)
            }
        }
    override var layoutMeasuredWidth: Int = 0
    override var layoutMeasuredHeight: Int = 0
    override var layoutLeft: Int = 0
    override var layoutRight: Int = 0
    override var layoutTop: Int = 0
    override var layoutBottom: Int = 0
    override var layoutId: Int = 0
    override var layoutIdString: String = ""
    override var leftPercent: Float = -1f
    override var topPercent: Float = -1f
    override var startToStartOf: Int = -1
    override var startToEndOf: Int = -1
    override var endToEndOf: Int = -1
    override var endToStartOf: Int = -1
    override var topToTopOf: Int = -1
    override var topToBottomOf: Int = -1
    override var bottomToTopOf: Int = -1
    override var bottomToBottomOf: Int = -1
    override var centerHorizontalOf: Int = -1
    override var centerVerticalOf: Int = -1
    override var layoutWidth: Int = 0
    override var layoutHeight: Int = 0
    override var layoutPaddingStart: Int = 0
    override var layoutPaddingEnd: Int = 0
    override var layoutPaddingTop: Int = 0
    override var layoutPaddingBottom: Int = 0
    override var layoutTopMargin: Int = 0
    override var layoutBottomMargin: Int = 0
    override var layoutLeftMargin: Int = 0
    override var layoutRightMargin: Int = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun doMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (textPaint == null) {
            textPaint = TextPaint().apply {
                isAntiAlias = true
                textSize = this@DrawableText.textSize
                color = textColor
            }
        }

        val measureWidth = if (layoutWidth != 0) layoutWidth else min(textPaint!!.measureText(text.toString()).toInt(), maxWidth)
        if (staticLayout == null) {
            staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, textPaint!!, measureWidth)
                .setAlignment(gravity)
                .setLineSpacing(spaceAdd, spaceMulti)
                .setIncludePad(false)
                .setMaxLines(maxLines).build()
        }

        val measureHeight = staticLayout!!.height
        setDimension(measureWidth + layoutPaddingEnd + layoutPaddingStart, measureHeight + layoutPaddingTop + layoutPaddingBottom)
    }

    override fun doLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (UtilKBuildVersion.isAfterV_23_6_M())
            setRect(left, top, right, bottom)
    }

    override fun doDraw(canvas: Canvas?) {
        canvas?.save()
        canvas?.translate(layoutLeft.toFloat(), layoutTop.toFloat())
        drawBackground(canvas)
        canvas?.translate(layoutPaddingStart.toFloat(), layoutPaddingTop.toFloat())
        staticLayout?.draw(canvas)
        canvas?.restore()
    }

    private fun drawBackground(canvas: Canvas?) {
        if (drawableShape == null) return
        val _shape = drawableShape!!
        if (_shape.radius != 0f) {
            canvas?.drawRoundRect(RectF(0f, 0f, layoutMeasuredWidth.toFloat(), layoutMeasuredHeight.toFloat()), _shape.radius, _shape.radius, shapePaint!!)
        } else if (_shape.corners != null) {
            _shape.path!!.apply {
                addRoundRect(
                    RectF(0f, 0f, layoutMeasuredWidth.toFloat(), layoutMeasuredHeight.toFloat()),
                    _shape.corners!!.radii,
                    Path.Direction.CCW
                )
            }
            canvas?.drawPath(_shape.path!!, shapePaint!!)
        }
    }
}