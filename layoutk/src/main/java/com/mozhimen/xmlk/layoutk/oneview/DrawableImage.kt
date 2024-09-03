package com.mozhimen.xmlk.layoutk.oneview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import com.mozhimen.kotlin.elemk.android.os.cons.CVersCode
import com.mozhimen.kotlin.elemk.commons.IExt_Listener
import com.mozhimen.xmlk.layoutk.LayoutKPercent

/**
 * @ClassName DrawableImage
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/3
 * @Version 1.0
 */
/**
 * one kind of [OneViewGroup.Drawable] shows image
 * and it is a special [ImageView] implemented [OneViewGroup.Drawable]
 */

inline fun LayoutKOneView.image(init: IExt_Listener<DrawableImage>): DrawableImage =
    DrawableImage(context).apply(init).also {
        addView(it)
        addDrawable(it)
    }

/////////////////////////////////////////////////////////////////////

class DrawableImage @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatImageView(context, attrs, defStyleAttr), LayoutKOneView.Drawable {
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

    override var layoutMeasuredWidth: Int = 0
        get() = measuredWidth
    override var layoutMeasuredHeight: Int = 0
        get() = measuredHeight
    override var layoutLeft: Int = 0
        get() = left
    override var layoutRight: Int = 0
        get() = right
    override var layoutTop: Int = 0
        get() = top
    override var layoutBottom: Int = 0
        get() = bottom
    override var layoutId: Int = 0
        get() = id
    override var layoutIdString: String = ""
    override var layoutPaddingStart: Int = 0
        get() = paddingStart
    override var layoutPaddingEnd: Int = 0
        get() = paddingEnd
    override var layoutPaddingTop: Int = 0
        get() = paddingTop
    override var layoutPaddingBottom: Int = 0
        get() = paddingBottom
    override var layoutTopMargin: Int = 0
        get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin ?: 0
    override var layoutBottomMargin: Int = 0
        get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin ?: 0
    override var layoutLeftMargin: Int = 0
        get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin ?: 0
    override var layoutRightMargin: Int = 0
        get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin ?: 0

    override fun doMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    }

    override fun doLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        layout(left, top, right, bottom)
    }

    override fun doDraw(canvas: Canvas?) {
    }
}