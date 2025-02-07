package com.mozhimen.xmlk.layoutk.oneview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import com.mozhimen.kotlin.utilk.android.util.dp2px
import com.mozhimen.kotlin.utilk.android.util.dp2pxI
import kotlin.math.abs

/**
 * @ClassName LayoutOneView
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/9/3
 * @Version 1.0
 */
fun LayoutKOneView.drawableShape(init: LayoutKOneView.Shape.() -> Unit): LayoutKOneView.Shape =
    LayoutKOneView.Shape().apply(init)

fun LayoutKOneView.Shape.corners(init: LayoutKOneView.Shape.Corners.() -> Unit): LayoutKOneView.Shape.Corners =
    LayoutKOneView.Shape.Corners().apply(init)

inline var LayoutKOneView.Drawable.drawable_top_margin: Int
    get() = 0
    set(value) {
        layoutTopMargin = value.dp2pxI()
    }

inline var LayoutKOneView.Drawable.drawable_bottom_margin: Int
    get() = 0
    set(value) {
        layoutBottomMargin = value.dp2pxI()
    }

inline var LayoutKOneView.Drawable.drawable_left_margin: Int
    get() = 0
    set(value) {
        layoutLeftMargin = value.dp2pxI()
    }

inline var LayoutKOneView.Drawable.drawable_right_margin: Int
    get() = 0
    set(value) {
        layoutRightMargin = value.dp2pxI()
    }

inline var LayoutKOneView.Drawable.drawable_padding_start: Int
    get() = 0
    set(value) {
        layoutPaddingStart = value.dp2pxI()
    }

inline var LayoutKOneView.Drawable.drawable_padding_end: Int
    get() = 0
    set(value) {
        layoutPaddingEnd = value.dp2pxI()
    }

inline var LayoutKOneView.Drawable.drawable_padding_top: Int
    get() = 0
    set(value) {
        layoutPaddingTop = value.dp2pxI()
    }

inline var LayoutKOneView.Drawable.drawable_padding_bottom: Int
    get() = 0
    set(value) {
        layoutPaddingBottom = value.dp2pxI()
    }

inline var LayoutKOneView.Drawable.drawable_layout_id: String
    get() = ""
    set(value) {
        layoutIdString = value
        layoutId = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Drawable.drawable_start_to_start_of: String
    get() = ""
    set(value) {
        startToStartOf = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Drawable.drawable_start_to_end_of: String
    get() = ""
    set(value) {
        startToEndOf = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Drawable.drawable_end_to_start_of: String
    get() = ""
    set(value) {
        endToStartOf = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Drawable.drawable_end_to_end_of: String
    get() = ""
    set(value) {
        endToEndOf = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Drawable.drawable_top_to_top_of: String
    get() = ""
    set(value) {
        topToTopOf = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Drawable.drawable_top_to_bottom_of: String
    get() = ""
    set(value) {
        topToBottomOf = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Drawable.drawable_bottom_to_top_of: String
    get() = ""
    set(value) {
        bottomToTopOf = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Drawable.drawable_bottom_to_bottom_of: String
    get() = ""
    set(value) {
        bottomToBottomOf = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Drawable.drawable_center_horizontal_of: String
    get() = ""
    set(value) {
        centerHorizontalOf = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Drawable.drawable_center_vertical_of: String
    get() = ""
    set(value) {
        centerVerticalOf = LayoutKOneView.generateLayoutId(value)
    }

inline var LayoutKOneView.Shape.radius: Float
    get() =0f
    set(value) {
        radius = value.dp2px()
    }

////////////////////////////////////////////////////////////////////////////////////

class LayoutKOneView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    companion object {
        @JvmStatic
        fun generateLayoutId(str: String): Int {
            var id = hashCode()
            if (str == "0") id = 0
            return abs(id)
        }
    }

    private val drawableMap = HashMap<Int, Drawable>()
    private val drawables = mutableListOf<Drawable>()
    private var gestureDetector: GestureDetector? = null

    fun addDrawable(drawable: Drawable) {
        drawables.add(drawable)
        drawableMap[drawable.layoutId] = drawable
    }

    /**
     * find [Drawable] by id
     */
    fun <T> findDrawable(id: String): T? = drawableMap[generateLayoutId(id)] as? T

    /**
     * set item click listener for [OneViewGroup] whick detect child [Drawable]'s click event
     */
    fun setOnItemClickListener(onItemClickListener: (String) -> Unit) {
        gestureDetector = GestureDetector(context, object : GestureDetector.OnGestureListener {
            override fun onShowPress(e: MotionEvent) {
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                findDrawableUnder(e.x, e.y)?.let { onItemClickListener.invoke(it.layoutIdString) }
                return true
            }

            override fun onDown(e: MotionEvent): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                return false
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
                return false
            }

            override fun onLongPress(e: MotionEvent) {
            }
        })
    }

    /**
     * find child [Drawable] according to coordinate
     */
    private fun findDrawableUnder(x: Float, y: Float): Drawable? {
        drawables.forEach {
            if (it.rect.contains(x.toInt(), y.toInt())) {
                return it
            }
        }
        return null
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        drawables.forEach { it.doMeasure(widthMeasureSpec, heightMeasureSpec) }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val parentWidth = right - left
        val parentHeight = bottom - top
        drawables.forEach {
            val left = getChildLeft(it, parentWidth)
            val top = getChildTop(it, parentHeight)
            it.doLayout(changed, left, top, left + it.layoutMeasuredWidth, top + it.layoutMeasuredHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawables.forEach { it.doDraw(canvas) }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return event?.let { gestureDetector?.onTouchEvent(it) } ?: super.onTouchEvent(event)
    }

    /**
     * get the top of [drawable] relative to the [OneViewGroup]
     */
    private fun getChildTop(drawable: Drawable, parentHeight: Int): Int {
        val parentId = generateLayoutId("0")
        return when {
            drawable.topPercent != -1f -> (parentHeight * drawable.topPercent).toInt()
            drawable.centerVerticalOf != -1 -> {
                if (drawable.centerVerticalOf == parentId) {
                    (parentHeight - drawable.layoutMeasuredHeight) / 2
                } else {
                    (drawableMap[drawable.centerVerticalOf]?.let { it.layoutTop + (it.layoutBottom - it.layoutTop) / 2 } ?: 0) - drawable.layoutMeasuredHeight / 2
                }
            }

            drawable.topToBottomOf != -1 -> {
                val b = if (drawable.topToBottomOf == parentId) bottom else drawableMap[drawable.topToBottomOf]?.layoutBottom ?: 0
                (b + drawable.layoutTopMargin)
            }

            drawable.topToTopOf != -1 -> {
                val t = if (drawable.topToTopOf == parentId) top else drawableMap[drawable.topToTopOf]?.layoutTop ?: 0
                (t + drawable.layoutTopMargin)
            }

            drawable.bottomToTopOf != -1 -> {
                val t = if (drawable.bottomToTopOf == parentId) top else drawableMap[drawable.bottomToTopOf]?.layoutTop ?: 0
                (t - drawable.layoutBottomMargin) - drawable.layoutMeasuredHeight
            }

            drawable.bottomToBottomOf != -1 -> {
                val b = if (drawable.bottomToBottomOf == parentId) bottom else drawableMap[drawable.bottomToBottomOf]?.layoutBottom ?: 0
                (b - drawable.layoutBottomMargin) - drawable.layoutMeasuredHeight
            }

            else -> 0
        }
    }

    /**
     * get the left of [drawable] relative to the [OneViewGroup]
     */
    private fun getChildLeft(drawable: Drawable, parentWidth: Int): Int {
        val parentId = generateLayoutId("0")
        return when {
            drawable.leftPercent != -1f -> (parentWidth * drawable.leftPercent).toInt()
            drawable.centerHorizontalOf != -1 -> {
                if (drawable.centerHorizontalOf == parentId) {
                    (parentWidth - drawable.layoutMeasuredWidth) / 2
                } else {
                    (drawableMap[drawable.centerHorizontalOf]?.let { it.layoutLeft + (it.layoutRight - it.layoutLeft) / 2 } ?: 0) - drawable.layoutMeasuredWidth / 2
                }
            }

            drawable.startToEndOf != -1 -> {
                val r = if (drawable.startToEndOf == parentId) right else drawableMap[drawable.startToEndOf]?.layoutRight ?: 0
                (r + drawable.layoutLeftMargin)
            }

            drawable.startToStartOf != -1 -> {
                val l = if (drawable.startToStartOf == parentId) left else drawableMap[drawable.startToStartOf]?.layoutLeft ?: 0
                (l + drawable.layoutLeftMargin)
            }

            drawable.endToStartOf != -1 -> {
                val l = if (drawable.endToStartOf == parentId) left else drawableMap[drawable.endToStartOf]?.layoutLeft ?: 0
                (l - drawable.layoutRightMargin) - drawable.layoutMeasuredWidth
            }

            drawable.endToEndOf != -1 -> {
                val r = if (drawable.endToEndOf == parentId) right else drawableMap[drawable.endToEndOf]?.layoutRight ?: 0
                (r - drawable.layoutRightMargin) - drawable.layoutMeasuredWidth
            }

            else -> 0
        }
    }

    /**
     * anything could be draw in [OneViewGroup]
     */
    interface Drawable {
        /**
         * the measured dimension of [Drawable]
         */
        var layoutMeasuredWidth: Int
        var layoutMeasuredHeight: Int

        /**
         * the frame rect of [Drawable]
         */
        var layoutLeft: Int
        var layoutRight: Int
        var layoutTop: Int
        var layoutBottom: Int
        val rect: Rect
            get() = Rect(layoutLeft, layoutTop, layoutRight, layoutBottom)

        /**
         * the unique id of this [Drawable] in int
         */
        var layoutId: Int

        /**
         * the unique id of this [Drawable] in string
         */
        var layoutIdString: String

        /**
         * the relative position of this [Drawable] to another
         */
        var leftPercent: Float
        var topPercent: Float
        var startToStartOf: Int
        var startToEndOf: Int
        var endToEndOf: Int
        var endToStartOf: Int
        var topToTopOf: Int
        var topToBottomOf: Int
        var bottomToTopOf: Int
        var bottomToBottomOf: Int
        var centerHorizontalOf: Int
        var centerVerticalOf: Int

        /**
         * dimension of [Drawable]
         */
        var layoutWidth: Int
        var layoutHeight: Int

        /**
         * inner padding of [Drawable]
         */
        var layoutPaddingStart: Int
        var layoutPaddingEnd: Int
        var layoutPaddingTop: Int
        var layoutPaddingBottom: Int

        /**
         * out margin of [Drawable]
         */
        var layoutTopMargin: Int
        var layoutBottomMargin: Int
        var layoutLeftMargin: Int
        var layoutRightMargin: Int

        /**
         * the the frame rect of [Drawable] is set after this function
         */
        fun setRect(left: Int, top: Int, right: Int, bottom: Int) {
            this.layoutLeft = left
            this.layoutRight = right
            this.layoutTop = top
            this.layoutBottom = bottom
        }

        /**
         * the measured width and height of [Drawable] is set after this function
         */
        fun setDimension(width: Int, height: Int) {
            this.layoutMeasuredWidth = width
            this.layoutMeasuredHeight = height
        }

        fun doMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int)
        fun doLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int)
        fun doDraw(canvas: Canvas?)
    }

    /**
     * a round rect shows in background
     */
    class Shape {
        var color: String? = null
        var colors: List<String>? = null
        var radius: Float = 0f
        internal var path: Path? = null
        var corners: Corners? = null
            set(value) {
                field = value
                path = Path()
            }

        class Corners(
            var leftTopRx: Float = 0f,
            var leftTopRy: Float = 0f,
            var leftBottomRx: Float = 0f,
            var LeftBottomRy: Float = 0f,
            var rightTopRx: Float = 0f,
            var rightTopRy: Float = 0f,
            var rightBottomRx: Float = 0f,
            var rightBottomRy: Float = 0f
        ) {
            val radii: FloatArray
                get() = floatArrayOf(leftTopRx, leftTopRy, rightTopRx, rightTopRy, rightBottomRx, rightBottomRy, leftBottomRx, LeftBottomRy)
        }
    }
}
