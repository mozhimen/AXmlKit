package com.mozhimen.xmlk.layoutk

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.mozhimen.kotlin.utilk.android.util.dp2pxI

/**
 * layout dsl for customized view
 */
inline fun ViewGroup.layoutKLineFeed(autoAdd: Boolean = true, init: LayoutKLineFeed.() -> Unit) =
    LayoutKLineFeed(context).apply(init).also { if (autoAdd) addView(it) }

inline fun Context.layoutKLineFeed(init: LayoutKLineFeed.() -> Unit): LayoutKLineFeed =
    LayoutKLineFeed(this).apply(init)

inline fun Fragment.layoutKLineFeed(init: LayoutKLineFeed.() -> Unit) =
    context?.let { LayoutKLineFeed(it).apply(init) }

////////////////////////////////////////////////////////////////////////

inline var LayoutKLineFeed.horizontal_gap: Int
    get() =-1
    set(value) {
        horizontalGap = value.dp2pxI()
    }

inline var LayoutKLineFeed.vertical_gap: Int
    get() =-1
    set(value) {
        verticalGap = value.dp2pxI()
    }

////////////////////////////////////////////////////////////////////////

/**
 * a special [ViewGroup] acts like [LinearLayout],
 * it spreads the children from left to right until there is not enough horizontal space for them,
 * then the next child will be placed at a new line
 */
class LayoutKLineFeed @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var horizontalGap: Int = 0
    var verticalGap: Int = 0

    /**
     * the height of [LayoutKLineFeed] depends on how much lines it has
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var height = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec)
        } else {
            var remainWidth = width
            (0 until childCount).map { getChildAt(it) }.forEach { child ->
                val lp = child.layoutParams as MarginLayoutParams
                if (isNewLine(lp, child, remainWidth)) {
                    remainWidth = width - child.measuredWidth
                    height += (lp.topMargin + lp.bottomMargin + child.measuredHeight + verticalGap)
                } else {
                    remainWidth -= child.measuredWidth
                    if (height == 0) height =
                        (lp.topMargin + lp.bottomMargin + child.measuredHeight + verticalGap)
                }
                remainWidth -= (lp.leftMargin + lp.rightMargin + horizontalGap)
            }
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left = 0
        var top = 0
        var lastBottom = 0
        var count = 0
        (0 until childCount).map { getChildAt(it) }.forEach { child ->
            val lp = child.layoutParams as MarginLayoutParams
            if (isNewLine(lp, child, r - l - left)) {
                left = -lp.leftMargin
                top = lastBottom
                lastBottom = 0
            }
            val childLeft = left + lp.leftMargin
            val childTop = top + lp.topMargin
            child.layout(
                childLeft,
                childTop,
                childLeft + child.measuredWidth,
                childTop + child.measuredHeight
            )
            if (lastBottom == 0) lastBottom = child.bottom + lp.bottomMargin + verticalGap
            left += child.measuredWidth + lp.leftMargin + lp.rightMargin + horizontalGap
            count++
        }
    }

    /**
     * place the [child] in a new line or not
     *
     * @param lp LayoutParams of [child]
     * @param child child view of [LayoutKLineFeed]
     * @param remainWidth the remain space of one line in [LayoutKLineFeed]
     * @param horizontalGap the horizontal gap for the children of [LayoutKLineFeed]
     */
    private fun isNewLine(
        lp: MarginLayoutParams,
        child: View,
        remainWidth: Int,
    ): Boolean {
        val childOccupation = lp.leftMargin + child.measuredWidth + lp.rightMargin
        return childOccupation > remainWidth
    }
}