package com.mozhimen.xmlk.layoutk

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import com.mozhimen.kotlin.utilk.android.view.applyUpdateLayoutParams
import kotlin.math.abs

/**
 * @ClassName LayoutKPercent
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/3
 * @Version 1.0
 */
/**
 * the child in [PercentLayout] use [LayoutParam.leftPercent] and [LayoutParam.topPercent] to locate itself, which is a percentage of the width and height of [PercentLayout].
 *
 * [PercentLayout] must have a specific width or height, or the children view of it will have no idea where to locate.
 */

/**
 * create [PercentLayout] instance within a [ViewGroup]
 * @param style an style int value defined in xml
 * @param autoAdd whether add [PercentLayout] into [ViewGroup] automatically
 * @param init set attributes for this view in this lambda
 */
inline fun ViewGroup.layoutKPercent(style: Int? = null, autoAdd: Boolean = true, init: LayoutKPercent.() -> Unit): LayoutKPercent {
    val percentLayout = if (style != null) LayoutKPercent(ContextThemeWrapper(context, style)) else LayoutKPercent(context)
    return percentLayout.apply(init).also { if (autoAdd) addView(it) }
}

inline fun Context.layoutKPercent(style: Int? = null, init: LayoutKPercent.() -> Unit): LayoutKPercent {
    val percentLayout = if (style != null) LayoutKPercent(ContextThemeWrapper(this, style)) else LayoutKPercent(this)
    return percentLayout.apply(init)
}

inline fun Fragment.layoutKPercent(style: Int? = null, init: LayoutKPercent.() -> Unit): LayoutKPercent? =
    context?.let { if (style != null) LayoutKPercent(ContextThemeWrapper(it, style)) else LayoutKPercent(it) }?.apply(init)

////////////////////////////////////////////////////////////////////////////////////////////

class LayoutKPercent @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    companion object{
        @JvmStatic
        fun generateLayoutId(str:String): Int {
            var id = hashCode()
            if (str == "0") id = 0
            return abs(id)
        }
    }

    private val childMap = SparseArray<View>()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val parentWidth = right - left
        val parentHeight = bottom - top
        (0 until childCount).map { getChildAt(it) }.forEach { child ->
            val lp = child.layoutParams as LayoutParam
            val childLeft = getChildLeft(lp, parentWidth, child)
            val childTop = getChildTop(lp, parentHeight, child)
            child.layout(childLeft, childTop, childLeft + child.measuredWidth, childTop + child.measuredHeight)
        }
    }

    private fun getChildTop(lp: LayoutParam, parentHeight: Int, child: View): Int {
        val parentId = generateLayoutId("0")
        return when {
            lp.topPercent != -1f -> (parentHeight * lp.topPercent).toInt()
            lp.centerVerticalOf != -1 -> {
                if (lp.centerVerticalOf == parentId) {
                    (parentHeight - child.measuredHeight) / 2
                } else {
                    (childMap.get(lp.centerVerticalOf)?.let { it.top + (it.bottom - it.top) / 2 } ?: 0) - child.measuredHeight / 2
                }
            }
            lp.topToBottomOf != -1 -> {
                val b = if (lp.topToBottomOf == parentId) bottom else childMap.get(lp.topToBottomOf)?.bottom ?: 0
                (b + lp.topMargin)
            }
            lp.topToTopOf != -1 -> {
                val t = if (lp.topToTopOf == parentId) top else childMap.get(lp.topToTopOf)?.top ?: 0
                (t + lp.topMargin)
            }
            lp.bottomToTopOf != -1 -> {
                val t = if (lp.bottomToTopOf == parentId) top else childMap.get(lp.bottomToTopOf)?.top ?: 0
                (t - lp.bottomMargin) - child.measuredHeight
            }
            lp.bottomToBottomOf != -1 -> {
                val b = if (lp.bottomToBottomOf == parentId) bottom else childMap.get(lp.bottomToBottomOf)?.bottom ?: 0
                (b - lp.bottomMargin) - child.measuredHeight
            }
            else -> 0
        }
    }

    private fun getChildLeft(lp: LayoutParam, parentWidth: Int, child: View): Int {
        val parentId = generateLayoutId("0")
        return when {
            lp.leftPercent != -1f -> (parentWidth * lp.leftPercent).toInt()
            lp.centerHorizontalOf != -1 -> {
                if (lp.centerHorizontalOf == parentId) {
                    (parentWidth - child.measuredWidth) / 2
                } else {
                    (childMap.get(lp.centerHorizontalOf)?.let { it.left + (it.right - it.left) / 2 } ?: 0) - child.measuredWidth / 2
                }
            }
            lp.startToEndOf != -1 -> {
                val r = if (lp.startToEndOf == parentId) right else childMap.get(lp.startToEndOf)?.right ?: 0
                (r + lp.marginStart)
            }
            lp.startToStartOf != -1 -> {
                val l = if (lp.startToStartOf == parentId) left else childMap.get(lp.startToStartOf)?.left ?: 0
                (l + lp.marginStart)
            }
            lp.endToStartOf != -1 -> {
                val l = if (lp.endToStartOf == parentId) left else childMap.get(lp.endToStartOf)?.left ?: 0
                (l - lp.marginEnd) - child.measuredWidth
            }
            lp.endToEndOf != -1 -> {
                val r = if (lp.endToEndOf == parentId) right else childMap.get(lp.endToEndOf)?.right ?: 0
                (r - lp.marginEnd) - child.measuredWidth
            }
            else -> 0
        }
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        child?.let { childMap.put(it.id, it) }
    }

    override fun onViewRemoved(child: View?) {
        super.onViewRemoved(child)
        child?.let { childMap.remove(it.id) }
    }

    class LayoutParam(source: LayoutParams?) : MarginLayoutParams(source) {
        var leftPercent: Float = -1f
        var topPercent: Float = -1f
        var startToStartOf: Int = -1
        var startToEndOf: Int = -1
        var endToEndOf: Int = -1
        var endToStartOf: Int = -1
        var topToTopOf: Int = -1
        var topToBottomOf: Int = -1
        var bottomToTopOf: Int = -1
        var bottomToBottomOf: Int = -1
        var centerHorizontalOf: Int = -1
        var centerVerticalOf: Int = -1
    }


}

////////////////////////////////////////////////////////////////////////////////////////////

inline var View.left_percent: Float
    get() =-1f
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            leftPercent = value
        }
    }

inline var View.top_percent: Float
    get() =-1f
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            topPercent = value
        }
    }

inline var View.start_to_start_of_percent: String
    get() =""
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            startToStartOf = LayoutKPercent.generateLayoutId(value)
        }
    }

inline var View.start_to_end_of_percent: String
    get() =""
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            startToEndOf = LayoutKPercent.generateLayoutId(value)
        }
    }

inline var View.end_to_end_of_percent: String
    get() =""
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            endToEndOf = LayoutKPercent.generateLayoutId(value)
        }
    }

inline var View.end_to_start_of_percent: String
    get() =""
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            endToStartOf = LayoutKPercent.generateLayoutId(value)
        }
    }

inline var View.top_to_top_of_percent: String
    get() =""
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            topToTopOf = LayoutKPercent.generateLayoutId(value)
        }
    }

inline var View.top_to_bottom_of_percent: String
    get() =""
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            topToBottomOf = LayoutKPercent.generateLayoutId(value)
        }
    }

inline var View.bottom_to_bottom_of_percent: String
    get() =""
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            bottomToBottomOf = LayoutKPercent.generateLayoutId(value)
        }
    }

inline var View.bottom_to_top_of_percent: String
    get() =""
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            bottomToTopOf = LayoutKPercent.generateLayoutId(value)
        }
    }

inline var View.center_vertical_of_percent: String
    get() =""
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            centerVerticalOf = LayoutKPercent.generateLayoutId(value)
        }
    }

inline var View.center_horizontal_of_percent: String
    get() =""
    set(value) {
        applyUpdateLayoutParams<LayoutKPercent.LayoutParam> {
            centerHorizontalOf = LayoutKPercent.generateLayoutId(value)
        }
    }