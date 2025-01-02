package com.mozhimen.xmlk.recyclerk.decoration


import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.kotlin.utilk.wrapper.gainColor
import com.mozhimen.kotlin.utilk.wrapper.gainDrawable

/**
 * @ClassName RecyclerKDecorationFlexibleDivider
 * @Description https://github.com/yqritc/RecyclerView-FlexibleDivider/tree/master
 * @Author mozhimen
 * @Date 2024/12/31
 * @Version 1.0
 */
/**
 * Created by yqritc on 2015/01/08.
 */
abstract class RecyclerKDecorationDividerFlexible protected constructor(builder: Builder<*>) : RecyclerView.ItemDecoration() {
    /**
     * Interface for controlling divider visibility
     */
    interface VisibilityProvider {
        /**
         * Returns true if divider should be hidden.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return True if the divider at position should be hidden
         */
        fun shouldHideDivider(position: Int, parent: RecyclerView?): Boolean
    }

    /**
     * Interface for controlling paint instance for divider drawing
     */
    interface PaintProvider {
        /**
         * Returns [android.graphics.Paint] for divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Paint instance
         */
        fun dividerPaint(position: Int, parent: RecyclerView?): Paint
    }

    /**
     * Interface for controlling divider color
     */
    interface ColorProvider {
        /**
         * Returns [android.graphics.Color] value of divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Color value
         */
        fun dividerColor(position: Int, parent: RecyclerView?): Int
    }

    /**
     * Interface for controlling drawable object for divider drawing
     */
    interface DrawableProvider {
        /**
         * Returns drawable instance for divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Drawable instance
         */
        fun drawableProvider(position: Int, parent: RecyclerView?): Drawable
    }

    /**
     * Interface for controlling divider size
     */
    interface SizeProvider {
        /**
         * Returns size value of divider.
         * Height for horizontal divider, width for vertical divider
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return Size of divider
         */
        fun dividerSize(position: Int, parent: RecyclerView?): Int
    }

    ////////////////////////////////////////////////////////////////////////////////////

    companion object {
        private const val DEFAULT_SIZE = 2
        private val ATTRS = intArrayOf(android.R.attr.listDivider)
    }

    protected enum class DividerType {
        DRAWABLE, PAINT, COLOR
    }

    ////////////////////////////////////////////////////////////////////////////////////

    protected var mDividerType: DividerType = DividerType.DRAWABLE
    protected var mVisibilityProvider: VisibilityProvider
    protected var mPaintProvider: PaintProvider? = null
    protected var mColorProvider: ColorProvider? = null
    protected var mDrawableProvider: DrawableProvider? = null
    protected var mSizeProvider: SizeProvider? = null
    protected var mShowLastDivider: Boolean
    protected var mPositionInsideItem: Boolean
    private var mPaint: Paint? = null

    ////////////////////////////////////////////////////////////////////////////////////

    init {
        if (builder.mPaintProvider != null) {
            mDividerType = DividerType.PAINT
            mPaintProvider = builder.mPaintProvider
        } else if (builder.mColorProvider != null) {
            mDividerType = DividerType.COLOR
            mColorProvider = builder.mColorProvider
            mPaint = Paint()
            setSizeProvider(builder)
        } else {
            mDividerType = DividerType.DRAWABLE
            if (builder.mDrawableProvider == null) {
                val a = builder.mContext.obtainStyledAttributes(ATTRS)
                val divider: Drawable? = a.getDrawable(0)
                divider?.let {
                    mDrawableProvider = object : DrawableProvider {
                        override fun drawableProvider(position: Int, parent: RecyclerView?): Drawable {
                            return it
                        }
                    }
                }
                a.recycle()
            } else {
                mDrawableProvider = builder.mDrawableProvider
            }
            mSizeProvider = builder.mSizeProvider
        }

        mVisibilityProvider = builder.mVisibilityProvider
        mShowLastDivider = builder.mShowLastDivider
        mPositionInsideItem = builder.mPositionInsideItem
    }

    ////////////////////////////////////////////////////////////////////////////////////

    protected abstract fun getDividerBound(position: Int, parent: RecyclerView, child: View): Rect

    protected abstract fun setItemOffsets(outRect: Rect, position: Int, parent: RecyclerView)

    ////////////////////////////////////////////////////////////////////////////////////

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter ?: return

        val itemCount = adapter.itemCount
        val lastDividerOffset = getLastDividerOffset(parent)
        val validChildCount = parent.childCount
        var lastChildPosition = -1
        for (i in 0 until validChildCount) {
            val child = parent.getChildAt(i)
            val childPosition = parent.getChildAdapterPosition(child)

            if (childPosition < lastChildPosition) {
                // Avoid remaining divider when animation starts
                continue
            }
            lastChildPosition = childPosition

            if (!mShowLastDivider && childPosition >= itemCount - lastDividerOffset) {
                // Don't draw divider for last line if mShowLastDivider = false
                continue
            }

            if (wasDividerAlreadyDrawn(childPosition, parent)) {
                // No need to draw divider again as it was drawn already by previous column
                continue
            }

            val groupIndex = getGroupIndex(childPosition, parent)
            if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)) {
                continue
            }

            val bounds = getDividerBound(groupIndex, parent, child)
            when (mDividerType) {
                DividerType.DRAWABLE -> {
                    val drawable = mDrawableProvider!!.drawableProvider(groupIndex, parent)
                    drawable.bounds = bounds
                    drawable.draw(c)
                }

                DividerType.PAINT -> {
                    mPaint = mPaintProvider!!.dividerPaint(groupIndex, parent)
                    c.drawLine(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds.bottom.toFloat(), mPaint!!)
                }

                DividerType.COLOR -> {
                    mPaint!!.color = mColorProvider!!.dividerColor(groupIndex, parent)
                    mPaint!!.strokeWidth = mSizeProvider!!.dividerSize(groupIndex, parent).toFloat()
                    c.drawLine(bounds.left.toFloat(), bounds.top.toFloat(), bounds.right.toFloat(), bounds.bottom.toFloat(), mPaint!!)
                }
            }
        }
    }

    override fun getItemOffsets(rect: Rect, v: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(v)
        val itemCount = parent.adapter!!.itemCount
        val lastDividerOffset = getLastDividerOffset(parent)
        if (!mShowLastDivider && position >= itemCount - lastDividerOffset) {
            // Don't set item offset for last line if mShowLastDivider = false
            return
        }

        val groupIndex = getGroupIndex(position, parent)
        if (mVisibilityProvider.shouldHideDivider(groupIndex, parent)) {
            return
        }

        setItemOffsets(rect, groupIndex, parent)
    }

    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * Check if recyclerview is reverse layout
     *
     * @param parent RecyclerView
     * @return true if recyclerview is reverse layout
     */
    protected fun isReverseLayout(parent: RecyclerView): Boolean {
        val layoutManager = parent.layoutManager
        return if (layoutManager is LinearLayoutManager) {
            layoutManager.reverseLayout
        } else {
            false
        }
    }

    private fun setSizeProvider(builder: Builder<*>) {
        mSizeProvider = builder.mSizeProvider
        if (mSizeProvider == null) {
            mSizeProvider = object : SizeProvider {
                override fun dividerSize(position: Int, parent: RecyclerView?): Int {
                    return DEFAULT_SIZE
                }
            }
        }
    }

    /**
     * In the case mShowLastDivider = false,
     * Returns offset for how many views we don't have to draw a divider for,
     * for LinearLayoutManager it is as simple as not drawing the last child divider,
     * but for a GridLayoutManager it needs to take the span count for the last items into account
     * until we use the span count configured for the grid.
     *
     * @param parent RecyclerView
     * @return offset for how many views we don't have to draw a divider or 1 if its a
     * LinearLayoutManager
     */
    private fun getLastDividerOffset(parent: RecyclerView): Int {
        if (parent.layoutManager is GridLayoutManager) {
            val layoutManager = parent.layoutManager as GridLayoutManager?
            val spanSizeLookup = layoutManager!!.spanSizeLookup
            val spanCount = layoutManager.spanCount
            val itemCount = parent.adapter!!.itemCount
            for (i in itemCount - 1 downTo 0) {
                if (spanSizeLookup.getSpanIndex(i, spanCount) == 0) {
                    return itemCount - i
                }
            }
        }

        return 1
    }

    /**
     * Determines whether divider was already drawn for the row the item is in,
     * effectively only makes sense for a grid
     *
     * @param position current view position to draw divider
     * @param parent   RecyclerView
     * @return true if the divider can be skipped as it is in the same row as the previous one.
     */
    private fun wasDividerAlreadyDrawn(position: Int, parent: RecyclerView): Boolean {
        if (parent.layoutManager is GridLayoutManager) {
            val layoutManager = parent.layoutManager as GridLayoutManager?
            val spanSizeLookup = layoutManager!!.spanSizeLookup
            val spanCount = layoutManager.spanCount
            return spanSizeLookup.getSpanIndex(position, spanCount) > 0
        }

        return false
    }

    /**
     * Returns a group index for GridLayoutManager.
     * for LinearLayoutManager, always returns position.
     *
     * @param position current view position to draw divider
     * @param parent   RecyclerView
     * @return group index of items
     */
    private fun getGroupIndex(position: Int, parent: RecyclerView): Int {
        if (parent.layoutManager is GridLayoutManager) {
            val layoutManager = parent.layoutManager as GridLayoutManager?
            val spanSizeLookup = layoutManager!!.spanSizeLookup
            val spanCount = layoutManager.spanCount
            return spanSizeLookup.getSpanGroupIndex(position, spanCount)
        }

        return position
    }

    ////////////////////////////////////////////////////////////////////////////////////

    @Suppress("UNCHECKED_CAST")
    open class Builder<T : Builder<T>>(val mContext: Context) {
        protected var mResources: Resources = mContext.resources

        var mPaintProvider: PaintProvider? = null
        var mColorProvider: ColorProvider? = null
        var mDrawableProvider: DrawableProvider? = null
        var mSizeProvider: SizeProvider? = null
        var mVisibilityProvider: VisibilityProvider = object : VisibilityProvider {
            override fun shouldHideDivider(position: Int, parent: RecyclerView?): Boolean {
                return false
            }
        }
        var mShowLastDivider: Boolean = false
        var mPositionInsideItem: Boolean = false

        ////////////////////////////////////////////////////////////////////////////////////

        fun setPaint(paint: Paint): T {
            return setPaintProvider(object : PaintProvider {
                override fun dividerPaint(position: Int, parent: RecyclerView?): Paint {
                    return paint
                }
            })
        }

        fun setPaintProvider(provider: PaintProvider): T {
            mPaintProvider = provider
            return this as T
        }

        fun setColorResId(@ColorRes intColorResId: Int): T {
            return setColor(mContext.gainColor(intColorResId))
        }

        fun setColor(@ColorInt intColor: Int): T {
            return setColorProvider(object : ColorProvider {
                override fun dividerColor(position: Int, parent: RecyclerView?): Int {
                    return intColor
                }
            })
        }

        fun setColorProvider(provider: ColorProvider): T {
            mColorProvider = provider
            return this as T
        }

        fun setDrawableResId(@DrawableRes intDrawableId: Int): T {
            return mContext.gainDrawable(intDrawableId)?.let { setDrawable(it) } ?: return this as T
        }

        fun setDrawable(drawable: Drawable): T {
            return setDrawableProvider(object : DrawableProvider {
                override fun drawableProvider(position: Int, parent: RecyclerView?): Drawable {
                    return drawable
                }
            })
        }

        fun setDrawableProvider(provider: DrawableProvider?): T {
            mDrawableProvider = provider
            return this as T
        }

        fun setSizeResId(@DimenRes intDimenId: Int): T {
            return setSize(mResources.getDimensionPixelSize(intDimenId))
        }

        fun setSize(size: Int): T {
            return setSizeProvider(object : SizeProvider {
                override fun dividerSize(position: Int, parent: RecyclerView?): Int {
                    return size
                }
            })
        }

        fun setSizeProvider(provider: SizeProvider?): T {
            mSizeProvider = provider
            return this as T
        }

        fun setVisibilityProvider(provider: VisibilityProvider): T {
            mVisibilityProvider = provider
            return this as T
        }

        fun isShowLastDivider(): T {
            mShowLastDivider = true
            return this as T
        }

        fun isPositionInsideItem(positionInsideItem: Boolean): T {
            mPositionInsideItem = positionInsideItem
            return this as T
        }

        protected fun checkBuilderParams() {
            if (mPaintProvider != null) {
                require(mColorProvider == null) { "Use setColor method of Paint class to specify line color. Do not provider ColorProvider if you set PaintProvider." }
                require(mSizeProvider == null) { "Use setStrokeWidth method of Paint class to specify line size. Do not provider SizeProvider if you set PaintProvider." }
            }
        }
    }
}