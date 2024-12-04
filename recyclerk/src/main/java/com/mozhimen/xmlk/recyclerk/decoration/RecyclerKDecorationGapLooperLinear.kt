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

    private fun equilibriumAssignment_ofLooperLinearLayoutManager(recyclerView: RecyclerView, itemView: View, outRect: Rect, gapOuter: Int, gapInner: Int = gapOuter / 2, gapOther: Int = gapOuter) {
        val itemCount = recyclerView.gainItemCount()// item 的个数
        val itemPosition = recyclerView.getChildAdapterPosition(itemView)// 当前 item 的 position
        val layoutManager = requireLayoutManager_ofLooperLinear(recyclerView) ?: return
        val orientation = layoutManager.orientation// 获取 LinearLayoutManager 的布局方向
        for (index in 0..itemCount) {// 遍历所有 item
            when (itemPosition) {
                0 -> {// 第一行/列
                    if (orientation == RecyclerView.VERTICAL) {// 第一行/列 && VERTICAL 布局方式 -> 对item的底部特殊处理
                        outRect.top = gapOuter
                        outRect.bottom = gapInner
                        outRect.left = gapOther
                        outRect.right = gapOther
                    } else {// 第一行/列 && HORIZONTAL 布局方式 -> 对item的右边特殊处理
                        outRect.top = gapOther
                        outRect.bottom = gapOther
                        outRect.left = gapOuter
                        outRect.right = gapInner
                    }
                }

                itemCount - 1 -> {// 最后一行/列
                    if (orientation == RecyclerView.VERTICAL) {// 最后一行/列 && VERTICAL 布局方式 -> 对item的顶部特殊处理
                        outRect.top = gapInner
                        outRect.bottom = gapOuter
                        outRect.left = gapOther
                        outRect.right = gapOther
                    } else {// 最后一行/列 && HORIZONTAL 布局方式 -> 对item的左边特殊处理
                        outRect.top = gapOther
                        outRect.bottom = gapOther
                        outRect.left = gapInner
                        outRect.right = gapOuter
                    }
                }

                else -> {// 中间的行/列
                    if (orientation == RecyclerView.VERTICAL) {// 中间的行/列 && VERTICAL 布局方式 -> 对item的顶部和底部特殊处理
                        outRect.top = gapInner
                        outRect.bottom = gapInner
                        outRect.left = gapOther
                        outRect.right = gapOther
                    } else {// 中间的行/列 && HORIZONTAL 布局方式 -> 对item的左边和右边特殊处理
                        outRect.top = gapOther
                        outRect.bottom = gapOther
                        outRect.left = gapInner
                        outRect.right = gapInner
                    }
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