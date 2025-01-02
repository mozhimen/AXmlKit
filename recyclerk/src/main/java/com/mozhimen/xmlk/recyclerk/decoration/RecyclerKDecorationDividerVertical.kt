package com.mozhimen.xmlk.recyclerk.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

/**
 * @ClassName RecyclerKDecorationVerticalDivider
 * @Description TODO
 * @Author mozhimen
 * @Date 2024/12/31
 * @Version 1.0
 */
//class RecyclerKDecorationVerticalDivider : RecyclerKDecorationFlexibleDivider {
//}
/**
 * Created by yqritc on 2015/01/15.
 */
class RecyclerKDecorationDividerVertical protected constructor(builder: Builder) : RecyclerKDecorationDividerFlexible(builder) {
    /**
     * Interface for controlling divider margin
     */
    interface MarginProvider {
        /**
         * Returns top margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return top margin
         */
        fun dividerTopMargin(position: Int, parent: RecyclerView?): Int

        /**
         * Returns bottom margin of divider.
         *
         * @param position Divider position (or group index for GridLayoutManager)
         * @param parent   RecyclerView
         * @return bottom margin
         */
        fun dividerBottomMargin(position: Int, parent: RecyclerView?): Int
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
        bounds.top = parent.paddingTop +
                mMarginProvider.dividerTopMargin(position, parent) + transitionY
        bounds.bottom = parent.height - parent.paddingBottom -
                mMarginProvider.dividerBottomMargin(position, parent) + transitionY

        val dividerSize = getDividerSize(position, parent)
        val isReverseLayout = isReverseLayout(parent)
        if (mDividerType == DividerType.DRAWABLE) {
            // set left and right position of divider
            if (isReverseLayout) {
                bounds.right = child.left - params.leftMargin + transitionX
                bounds.left = bounds.right - dividerSize
            } else {
                bounds.left = child.right + params.rightMargin + transitionX
                bounds.right = bounds.left + dividerSize
            }
        } else {
            // set center point of divider
            val halfSize = dividerSize / 2
            if (isReverseLayout) {
                bounds.left = child.left - params.leftMargin - halfSize + transitionX
            } else {
                bounds.left = child.right + params.rightMargin + halfSize + transitionX
            }
            bounds.right = bounds.left
        }

        if (mPositionInsideItem) {
            if (isReverseLayout) {
                bounds.left += dividerSize
                bounds.right += dividerSize
            } else {
                bounds.left -= dividerSize
                bounds.right -= dividerSize
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
            outRect[getDividerSize(position, parent), 0, 0] = 0
        } else {
            outRect[0, 0, getDividerSize(position, parent)] = 0
        }
    }

    /////////////////////////////////////////////////////////////////

    private fun getDividerSize(position: Int, parent: RecyclerView): Int {
        if (mPaintProvider != null) {
            return mPaintProvider!!.dividerPaint(position, parent)!!.strokeWidth.toInt()
        } else if (mSizeProvider != null) {
            return mSizeProvider!!.dividerSize(position, parent)
        } else if (mDrawableProvider != null) {
            val drawable = mDrawableProvider!!.drawableProvider(position, parent)
            return drawable.intrinsicWidth
        }
        throw RuntimeException("failed to get size")
    }

    /////////////////////////////////////////////////////////////////

    class Builder(context: Context) : RecyclerKDecorationDividerFlexible.Builder<Builder>(context) {
        internal var mMarginProvider: MarginProvider = object : MarginProvider {
            override fun dividerTopMargin(position: Int, parent: RecyclerView?): Int {
                return 0
            }

            override fun dividerBottomMargin(position: Int, parent: RecyclerView?): Int {
                return 0
            }
        }

        fun setMarginProvider(provider: MarginProvider): Builder {
            mMarginProvider = provider
            return this
        }

        fun setMargin(topMargin: Int, bottomMargin: Int): Builder {
            return setMarginProvider(object : MarginProvider {
                override fun dividerTopMargin(position: Int, parent: RecyclerView?): Int {
                    return topMargin
                }

                override fun dividerBottomMargin(position: Int, parent: RecyclerView?): Int {
                    return bottomMargin
                }
            })
        }

        fun setMargin(verticalMargin: Int): Builder {
            return setMargin(verticalMargin, verticalMargin)
        }

        fun setMarginResId(@DimenRes topMarginId: Int, @DimenRes bottomMarginId: Int): Builder {
            return setMarginResId(
                mResources.getDimensionPixelSize(topMarginId),
                mResources.getDimensionPixelSize(bottomMarginId)
            )
        }

        fun setMarginResId(@DimenRes verticalMarginId: Int): Builder {
            return setMarginResId(verticalMarginId, verticalMarginId)
        }

        fun build(): RecyclerKDecorationDividerVertical {
            checkBuilderParams()
            return RecyclerKDecorationDividerVertical(this)
        }
    }
}