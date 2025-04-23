package com.mozhimen.xmlk.recyclerk.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LooperLinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mozhimen.kotlin.utilk.androidx.recyclerview.gainItemCount

/**
 * @ClassName RecyclerDecorationGap2
 * @Description TODO
 * @Author Mozhimen & Kolin Zhao
 * @Version 1.0
 */
class RecyclerKDecorationGapLooperLinear : RecyclerKDecorationGapLinear {

    constructor(gapOuter: Int) : super(gapOuter)

    constructor(gapOuter: Int, gapInner: Int) : super(gapOuter, gapInner)

    constructor(gapOuter: Int, gapInner: Int, gapOther: Int) : super(gapOuter, gapInner, gapOther)

    ////////////////////////////////////////////////////////////////

    override fun equilibriumAssignment(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        equilibriumAssignment_ofLooperLinearLayoutManager(parent, view, outRect, _gapOuter, _gapInner, _gapOther)
    }

    ////////////////////////////////////////////////////////////////

    private fun equilibriumAssignment_ofLooperLinearLayoutManager(recyclerView: RecyclerView, itemView: View, outRect: Rect, gapOuter: Int, gapInner: Int = gapOuter / 2, gapOpposite: Int = gapOuter) {
        val itemPosition = recyclerView.getChildAdapterPosition(itemView).takeIf { it != RecyclerView.NO_POSITION } ?: return
        val itemCount = recyclerView.gainItemCount().coerceAtLeast(0)// item 的个数
        if (itemCount == 0) return
        val layoutManager = requireLayoutManager_ofLooperLinear(recyclerView) ?: return
        val isVertical = layoutManager.orientation == RecyclerView.VERTICAL
        when (itemPosition) {
            0 -> {// 第一行/列
                if (isVertical) {// 第一行/列 && VERTICAL 布局方式 -> 对item的底部特殊处理
                    outRect.left = gapOpposite
                    outRect.top = gapOuter
                    outRect.right = gapOpposite
                    outRect.bottom = gapInner
                } else {// 第一行/列 && HORIZONTAL 布局方式 -> 对item的右边特殊处理
                    outRect.left = gapOuter
                    outRect.top = gapOpposite
                    outRect.right = gapInner
                    outRect.bottom = gapOpposite
                }
            }

            itemCount - 1 -> {// 最后一行/列
                if (isVertical) {// 最后一行/列 && VERTICAL 布局方式 -> 对item的顶部特殊处理
                    outRect.left = gapOpposite
                    outRect.top = gapInner
                    outRect.right = gapOpposite
                    outRect.bottom = gapOuter
                } else {// 最后一行/列 && HORIZONTAL 布局方式 -> 对item的左边特殊处理
                    outRect.left = gapInner
                    outRect.top = gapOpposite
                    outRect.right = gapOuter
                    outRect.bottom = gapOpposite
                }
            }

            else -> {// 中间的行/列
                if (isVertical) {// 中间的行/列 && VERTICAL 布局方式 -> 对item的顶部和底部特殊处理
                    outRect.left = gapOpposite
                    outRect.top = gapInner
                    outRect.right = gapOpposite
                    outRect.bottom = gapInner
                } else {// 中间的行/列 && HORIZONTAL 布局方式 -> 对item的左边和右边特殊处理
                    outRect.left = gapInner
                    outRect.top = gapOpposite
                    outRect.right = gapInner
                    outRect.bottom = gapOpposite
                }
            }
        }
    }

    private fun requireLayoutManager_ofLooperLinear(recyclerView: RecyclerView): LooperLinearLayoutManager? {
        val layoutManager = recyclerView.layoutManager ?: return null
        if (layoutManager !is LooperLinearLayoutManager)
            throw IllegalStateException("Make sure you are using the LooperLinearLayoutManager")
        return layoutManager
    }
}