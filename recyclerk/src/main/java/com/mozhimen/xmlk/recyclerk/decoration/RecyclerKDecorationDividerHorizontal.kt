package com.mozhimen.xmlk.recyclerk.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * @ClassName RecyclerKDcorationDividerHorizontal
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/12/31
 * @Version 1.0
 */
/**
 * Created by yqritc on 2015/01/15.
 */
class RecyclerKDecorationDividerHorizontal protected constructor(builder: Builder) : RecyclerKDecorationDividerFlexible(builder) {

    /**
     * Interface for controlling divider margin
     */
    interface MarginProvider {
        /**
         * Returns left margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return left margin
         */
        fun dividerLeftMargin(position: Int, parent: RecyclerView?): Int

        /**
         * Returns right margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return right margin
         */
        fun dividerRightMargin(position: Int, parent: RecyclerView?): Int
    }

    /////////////////////////////////////////////////////////////////

    private val mMarginProvider: MarginProvider

    /////////////////////////////////////////////////////////////////

    init {
        mMarginProvider = builder.mMarginProvider
    }

    /////////////////////////////////////////////////////////////////

    override fun getDividerBound(position: Int, parent: RecyclerView, child: View): Rect {
        val bounds = Rect(0, 0, 0, 0)
        val transitionX = ViewCompat.getTranslationX(child).toInt()
        val transitionY = ViewCompat.getTranslationY(child).toInt()
        val params = child.layoutParams as RecyclerView.LayoutParams
        bounds.left = parent.paddingLeft +
                mMarginProvider.dividerLeftMargin(position, parent) + transitionX
        bounds.right = parent.width - parent.paddingRight -
                mMarginProvider.dividerRightMargin(position, parent) + transitionX

        val dividerSize = getDividerSize(position, parent)
        val isReverseLayout = isReverseLayout(parent)
        if (mDividerType == DividerType.DRAWABLE) {
            // set top and bottom position of divider
            if (isReverseLayout) {
                bounds.bottom = child.top - params.topMargin + transitionY
                bounds.top = bounds.bottom - dividerSize
            } else {
                bounds.top = child.bottom + params.bottomMargin + transitionY
                bounds.bottom = bounds.top + dividerSize
            }
        } else {
            // set center point of divider
            val halfSize = dividerSize / 2
            if (isReverseLayout) {
                bounds.top = child.top - params.topMargin - halfSize + transitionY
            } else {
                bounds.top = child.bottom + params.bottomMargin + halfSize + transitionY
            }
            bounds.bottom = bounds.top
        }

        if (mPositionInsideItem) {
            if (isReverseLayout) {
                bounds.top += dividerSize
                bounds.bottom += dividerSize
            } else {
                bounds.top -= dividerSize
                bounds.bottom -= dividerSize
            }
        }

        return bounds
    }

    override fun setItemOffsets(outRect: Rect, position: Int, parent: RecyclerView) {
        if (mPositionInsideItem) {
            outRect[0, 0, 0] = 0
            return
        }

        if (isReverseLayout(parent)) {
            outRect[0, getDividerSize(position, parent), 0] = 0
        } else {
            outRect[0, 0, 0] = getDividerSize(position, parent)
        }
    }

    /////////////////////////////////////////////////////////////////

    private fun getDividerSize(position: Int, parent: RecyclerView): Int {
        if (mPaintProvider != null) {
            return mPaintProvider!!.dividerPaint(position, parent).strokeWidth.toInt()
        } else if (mSizeProvider != null) {
            return mSizeProvider!!.dividerSize(position, parent)
        } else if (mDrawableProvider != null) {
            val drawable = mDrawableProvider!!.drawableProvider(position, parent)
            return drawable.intrinsicHeight
        }
        throw RuntimeException("failed to get size")
    }

    /////////////////////////////////////////////////////////////////

    class Builder(context: Context) : RecyclerKDecorationDividerFlexible.Builder<Builder>(context) {
        internal var mMarginProvider: MarginProvider = object : MarginProvider {
            override fun dividerLeftMargin(position: Int, parent: RecyclerView?): Int {
                return 0
            }

            override fun dividerRightMargin(position: Int, parent: RecyclerView?): Int {
                return 0
            }
        }

        fun setMarginProvider(provider: MarginProvider): Builder {
            mMarginProvider = provider
            return this
        }

        fun setMargin(leftMargin: Int, rightMargin: Int): Builder {
            return setMarginProvider(object : MarginProvider {
                override fun dividerLeftMargin(position: Int, parent: RecyclerView?): Int {
                    return leftMargin
                }

                override fun dividerRightMargin(position: Int, parent: RecyclerView?): Int {
                    return rightMargin
                }
            })
        }

        fun setMargin(horizontalMargin: Int): Builder {
            return setMargin(horizontalMargin, horizontalMargin)
        }

        fun setMarginResId(@DimenRes leftMarginId: Int, @DimenRes rightMarginId: Int): Builder {
            return setMarginResId(
                mResources.getDimensionPixelSize(leftMarginId),
                mResources.getDimensionPixelSize(rightMarginId)
            )
        }

        fun setMarginResId(@DimenRes horizontalMarginId: Int): Builder {
            return setMarginResId(horizontalMarginId, horizontalMarginId)
        }

        fun build(): RecyclerKDecorationDividerHorizontal {
            checkBuilderParams()
            return RecyclerKDecorationDividerHorizontal(this)
        }
    }
}